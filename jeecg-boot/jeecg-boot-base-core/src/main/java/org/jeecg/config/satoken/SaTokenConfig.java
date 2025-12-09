package org.jeecg.config.satoken;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.config.satoken.ignore.InMemoryIgnoreAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: jeecg-boot
 * @description: Sa-Token 配置类
 */
@Slf4j
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SaTokenConfig implements WebMvcConfigurer {

    @Resource
    private JeecgBaseConfig jeecgBaseConfig;
    @Autowired
    private Environment env;
    @Autowired
    private CommonAPI commonAPI;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * Sa-Token 整合 jwt (Simple 模式)
     * 使用JWT-Simple模式生成标准JWT格式的token
     * 并支持从URL参数"token"读取token（兼容原系统）
     */
    @Bean
    @Primary
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple() {
            /**
             * 获取当前请求的 Token 值
             * 优先级：Header > URL参数token > URL参数X-Access-Token
             */
            @Override
            public String getTokenValue() {
                try {
                    SaRequest request = SaHolder.getRequest();
                    
                    // 1. 优先从Header中获取
                    String tokenValue = request.getHeader(getConfigOrGlobal().getTokenName());
                    if (oConvertUtils.isNotEmpty(tokenValue)) {
                        return tokenValue;
                    }
                    
                    // 2. 从URL参数"token"获取（兼容原系统）
                    tokenValue = request.getParam("token");
                    if (oConvertUtils.isNotEmpty(tokenValue)) {
                        return tokenValue;
                    }
                    
                    // 3. 从URL参数"X-Access-Token"获取
                    tokenValue = request.getParam(getConfigOrGlobal().getTokenName());
                    if (oConvertUtils.isNotEmpty(tokenValue)) {
                        return tokenValue;
                    }
                } catch (Exception e) {
                    log.debug("获取token失败: {}", e.getMessage());
                }
                
                // 4. 如果都没有，使用默认逻辑
                return super.getTokenValue();
            }
        };
    }

    /**
     * 注册 Sa-Token 拦截器，打开注解式鉴权功能
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    /**
     * 注册 Sa-Token 全局过滤器
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                // 指定 [拦截路由] 与 [放行路由]
                .addInclude("/**")
                .setExcludeList(getExcludeUrls())
                // 认证函数: 每次请求执行
                .setAuth(obj -> {
                    // 检查是否是免认证路径
                    String servletPath = SaHolder.getRequest().getRequestPath();
                    if (InMemoryIgnoreAuth.contains(servletPath)) {
                        return;
                    }
                    
                    // 校验 token：如果请求中带有 token，先切换到对应的登录会话再校验
                    try {
                        String token = StpUtil.getTokenValue();
                        if (oConvertUtils.isNotEmpty(token)) {
                            // 根据 token 获取 loginId 并切换到对应的登录会话
                            Object loginId = StpUtil.getLoginIdByToken(token);
                            if (loginId != null) {
                                StpUtil.switchTo(loginId);

                                // 需要手工自动续签，默认参数auto-renew:true 不好使
                                long activeTimeout = StpUtil.stpLogic.getConfigOrGlobal().getActiveTimeout();
                                if (activeTimeout > 0) {
                                    // 获取当前token的活跃剩余时间
                                    long tokenActiveTimeout = StpUtil.getTokenActiveTimeout();

                                    // 如果剩余活跃时间少于总活跃时间的一半，进行续签
                                    if (tokenActiveTimeout > 0 && tokenActiveTimeout < (activeTimeout / 2)) {
                                        StpUtil.stpLogic.updateLastActiveToNow(token);
                                        log.info("【Sa-Token拦截器】Token续签成功，剩余活跃时间: {}秒", tokenActiveTimeout);
                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        // 如果获取 loginId 失败，说明 token 无效或未登录，让 checkLogin 抛出异常
                        log.debug("切换登录会话失败: {}", e.getMessage());
                    }
                    
                    // 最终校验登录状态
                    StpUtil.checkLogin();
                    
                    // 租户校验逻辑
                    checkTenantAuthorization();
                })
                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    log.warn("Sa-Token 认证失败：用户未登录或token无效");
                    log.warn("请求路径: {}, Method: {}，Token: {}", SaHolder.getRequest().getRequestPath(), SaHolder.getRequest().getMethod(), StpUtil.getTokenValue());
                    
                    // 返回401状态码
                    SaHolder.getResponse().setStatus(401).setHeader("Content-Type", "application/json;charset=UTF-8");
                    return org.jeecg.common.system.util.JwtUtil.responseErrorJson(401, CommonConstant.TOKEN_IS_INVALID_MSG);
                })
                // 前置函数：在每次认证函数之前执行（BeforeAuth 不受 includeList 与 excludeList 的限制，所有请求都会进入）
                .setBeforeAuth(r -> {
                    // 设置跨域配置
                    Object cloudServer = env.getProperty(CommonConstant.CLOUD_SERVER_KEY);
                    // 如果cloudServer为空 则说明是单体 需要加载跨域配置【微服务跨域切换】
                    if (cloudServer == null) {
                        SaHolder.getResponse()
                                // 允许指定域访问跨域资源
                                .setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, SaHolder.getRequest().getHeader(HttpHeaders.ORIGIN))
                                // 允许所有请求方式
                                .setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
                                // 有效时间
                                .setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600")
                                // 允许的header参数
                                .setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, SaHolder.getRequest().getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS))
                                // 允许携带凭证
                                .setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                    }
                    
                    // OPTIONS预检请求，直接返回
                    SaRouter.match(SaHttpMethod.OPTIONS).free(r2 -> {
                        SaHolder.getResponse().setStatus(HttpStatus.OK.value());
                    });

                    // 设置当前线程上下文的租户ID
                    String tenantId = SaHolder.getRequest().getHeader(CommonConstant.TENANT_ID);
                    TenantContext.setTenant(tenantId);
                    log.debug("===【TenantContext 线程设置】=== 请求路径: {}, 租户ID: {}", SaHolder.getRequest().getRequestPath(), tenantId);
                });
    }

    /**
     * spring过滤装饰器 <br/>
     * 支持异步请求的过滤器装饰
     */
    @Bean
    public FilterRegistrationBean<SaServletFilter> saTokenFilterRegistration() {
        FilterRegistrationBean<SaServletFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(getSaServletFilter());
        registration.setName("SaServletFilter");
        // 支持异步请求
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
        // 拦截所有请求
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        registration.setAsyncSupported(true);  // 支持异步请求
        return registration;
    }

    /**
     * 获取排除URL列表
     */
    private List<String> getExcludeUrls() {
        List<String> excludeUrls = new ArrayList<>();
        
        // 支持yml方式，配置拦截排除
        if (jeecgBaseConfig != null && jeecgBaseConfig.getShiro() != null) {
            String shiroExcludeUrls = jeecgBaseConfig.getShiro().getExcludeUrls();
            if (oConvertUtils.isNotEmpty(shiroExcludeUrls)) {
                String[] permissionUrl = shiroExcludeUrls.split(",");
                excludeUrls.addAll(Arrays.asList(permissionUrl));
            }
        }

        // 添加默认排除路径
        excludeUrls.addAll(Arrays.asList(
                "/sys/cas/client/validateLogin", // cas验证登录
                "/sys/randomImage/**", // 登录验证码接口排除
                "/sys/checkCaptcha", // 登录验证码接口排除
                "/sys/smsCheckCaptcha", // 短信次数发送太多验证码排除
                "/sys/login", // 登录接口排除
                "/sys/mLogin", // 登录接口排除
                "/sys/logout", // 登出接口排除
                "/sys/thirdLogin/**", // 第三方登录
                "/sys/getEncryptedString", // 获取加密串
                "/sys/sms", // 短信验证码
                "/sys/phoneLogin", // 手机登录
                "/sys/user/checkOnlyUser", // 校验用户是否存在
                "/sys/user/register", // 用户注册
                "/sys/user/phoneVerification", // 用户忘记密码验证手机号
                "/sys/user/passwordChange", // 用户更改密码
                "/auth/2step-code", // 登录验证码
                "/sys/common/static/**", // 图片预览 & 下载文件不限制token
                "/sys/common/pdf/**", // pdf预览
                "/generic/**", // pdf预览需要文件
                "/sys/getLoginQrcode/**", // 登录二维码
                "/sys/getQrcodeToken/**", // 监听扫码
                "/sys/checkAuth", // 授权接口排除
                "/openapi/call/**", // 开放平台接口排除
                
                // 排除静态资源后缀
                "/",
                "/doc.html",
                "**/*.js",
                "**/*.css",
                "**/*.html",
                "**/*.svg",
                "**/*.pdf",
                "**/*.jpg",
                "**/*.png",
                "**/*.gif",
                "**/*.ico",
                "**/*.ttf",
                "**/*.woff",
                "**/*.woff2",
                "**/*.glb",
                "**/*.wasm",
                "**/*.js.map",
                "**/*.css.map",
                
                "/druid/**",
                "/swagger-ui.html",
                "/swagger*/**",
                "/webjars/**",
                "/v3/**",
                
                // 排除消息通告查看详情页面（用于第三方APP）
                "/sys/annountCement/show/**",
                
                // 积木报表和积木BI排除
                "/jmreport/**",
                "/drag/lib/**",
                "/drag/list/**",
                "/drag/favicon.ico",
                "/drag/view",
                "/drag/page/queryById",
                "/drag/page/addVisitsNumber",
                "/drag/page/queryTemplateList",
                "/drag/share/view/**",
                "/drag/onlDragDatasetHead/getAllChartData",
                "/drag/onlDragDatasetHead/getTotalData",
                "/drag/onlDragDatasetHead/getMapDataByCode",
                "/drag/onlDragDatasetHead/getTotalDataByCompId",
                "/drag/mock/json/**",
                "/drag/onlDragDatasetHead/getDictByCodes",
                "/drag/onlDragDatasetHead/queryAllById",
                "/jimubi/view",
                "/jimubi/share/view/**",
                
                // 大屏模板例子
                "/test/bigScreen/**",
                "/bigscreen/template1/**",
                "/bigscreen/template2/**",
                
                // websocket排除
                "/websocket/**", // 系统通知和公告
                "/newsWebsocket/**", // CMS模块
                "/vxeSocket/**", // JVxeTable无痕刷新示例
                "/dragChannelSocket/**", // 仪表盘（按钮通信）
                
                // App vue3版本查询版本接口
                "/sys/version/app3version",
                
                // 测试模块排除
                "/test/seata/**",
                
                // 错误路径排除
                "/error",
                
                // 企业微信证书排除
                "/WW_verify*"
        ));

        return excludeUrls;
    }
    
    /**
     * 校验用户的tenant_id和前端传过来的是否一致
     * 
     * <p>实现逻辑：
     * <ul>
     *   <li>1. 获取当前登录用户信息</li>
     *   <li>2. 检查用户是否配置了租户信息</li>
     *   <li>3. 获取前端请求头中的租户ID</li>
     *   <li>4. 校验用户所属租户中是否包含当前请求的租户ID</li>
     *   <li>5. 如果校验失败，从数据库重新查询用户信息并再次校验</li>
     *   <li>6. 最终校验失败则抛出异常</li>
     * </ul>
     * 
     * @throws NotLoginException 租户授权变更异常
     */
    private void checkTenantAuthorization() {
        log.debug("------ 租户校验开始 ------");
        // 如果未开启租户控制，直接返回
        if (!MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            return;
        }
        
        try {
            // 获取当前登录用户信息
            LoginUser loginUser = TokenUtils.getLoginUser(LoginUserUtils.getUsername(), commonAPI, redisUtil);
            if (loginUser == null) {
                return;
            }
            
            String username = loginUser.getUsername();
            String userTenantIds = loginUser.getRelTenantIds();
            
            // 如果用户未配置租户信息，直接返回
            if (oConvertUtils.isEmpty(userTenantIds)) {
                return;
            }
            
            // 获取前端请求头中的租户ID
            String loginTenantId = TokenUtils.getTenantIdByRequest(SpringContextUtils.getHttpServletRequest());
            log.info("登录租户：{}", loginTenantId);
            log.info("用户拥有那些租户：{}", userTenantIds);
            
            // 登录用户无租户，前端header中租户ID值为 0
            String str = "0";
            if (oConvertUtils.isEmpty(loginTenantId) || str.equals(loginTenantId)) {
                return;
            }
            
            String[] userTenantIdsArray = userTenantIds.split(",");
            if (!oConvertUtils.isIn(loginTenantId, userTenantIdsArray)) {
                boolean isAuthorization = false;
                
                //========================================================================
                // 查询用户信息（如果租户不匹配从数据库中重新查询一次用户信息）
                String loginUserKey = CacheConstant.SYS_USERS_CACHE + "::" + username;
                redisUtil.del(loginUserKey);
                
                LoginUser loginUserFromDb = commonAPI.getUserByName(username);
                LoginUserUtils.setSessionUser(loginUserFromDb);
                if (loginUserFromDb != null && oConvertUtils.isNotEmpty(loginUserFromDb.getRelTenantIds())) {
                    String[] newArray = loginUserFromDb.getRelTenantIds().split(",");
                    if (oConvertUtils.isIn(loginTenantId, newArray)) {
                        isAuthorization = true;
                    }
                }
                //========================================================================

                if (!isAuthorization) {
                    log.info("租户异常——登录租户：{}", loginTenantId);
                    log.info("租户异常——用户拥有租户组：{}", userTenantIds);
                    throw new NotLoginException("登录租户授权变更，请重新登陆!", StpUtil.TYPE, NotLoginException.KICK_OUT);
                }
            }
            
        }catch (Exception e) {
            log.error("租户校验异常：{}", e.getMessage(), e);
        }
    }
}

