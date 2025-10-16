package org.jeecg.config.satoken;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
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
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
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
                            }
                        }
                    } catch (Exception e) {
                        // 如果获取 loginId 失败，说明 token 无效或未登录，让 checkLogin 抛出异常
                        log.debug("切换登录会话失败: {}", e.getMessage());
                    }
                    
                    // 最终校验登录状态
                    StpUtil.checkLogin();
                })
                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    log.warn("Sa-Token 认证失败：用户未登录或token无效");
                    // Filter 层的异常无法被 @ExceptionHandler 捕获，需要直接返回 JSON 响应
                    SaHolder.getResponse()
                        .setStatus(401)
                        .setHeader("Content-Type", "application/json;charset=UTF-8");
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
        // 拦截所有请求（修复：原来只拦截特定异步接口，导致其他接口不检查登录状态）
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
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
                
                // 积木报表排除
                "/jmreport/**",
                // 积木BI大屏和仪表盘排除
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
}

