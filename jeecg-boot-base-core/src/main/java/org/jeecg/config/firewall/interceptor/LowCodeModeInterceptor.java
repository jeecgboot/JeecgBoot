package org.jeecg.config.firewall.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.config.firewall.interceptor.enums.LowCodeUrlsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * 低代码模式（dev:开发模式，prod:发布模式——关闭所有在线开发配置能力）
 * <p>
 * prod开启后会关闭以下功能，只保留功能测试（拥有admin角色账号，可以使用配置能力）
 * 1.online表单的所有配置功能，代码生成和导入表功能
 * 2.online报表的所有配置功能，和sql解析
 * 3.online图表的所有配置功能，和sql解析
 * 4.仪表盘的在线配置功能，和sql解析
 * 5.大屏的在线配置功能，和sql解析
 * 
 * 积木的逻辑单独处理
 * 1.积木报表的在线配置功能，和sql解析
 *
 * @author qinfeng
 * @date 20230904
 */
@Slf4j
public class LowCodeModeInterceptor implements HandlerInterceptor {
    /**
     * 低代码开发模式
     */
    public static final String LOW_CODE_MODE_DEV = "dev";
    public static final String LOW_CODE_MODE_PROD = "prod";

    @Resource
    private JeecgBaseConfig jeecgBaseConfig;
    @Autowired
    private CommonAPI commonAPI;

    /**
     * 在请求处理之前进行调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //1、验证是否开启低代码开发模式控制
        if (jeecgBaseConfig == null) {
            jeecgBaseConfig = SpringContextUtils.getBean(JeecgBaseConfig.class);
        }

        if (jeecgBaseConfig.getFirewall()!=null && LowCodeModeInterceptor.LOW_CODE_MODE_PROD.equals(jeecgBaseConfig.getFirewall().getLowCodeMode())) {
            String requestURI = request.getRequestURI().substring(request.getContextPath().length());
            log.info("低代码模式，拦截请求路径：" + requestURI);
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            Set<String> hasRoles = null;
            if (loginUser == null) {
                loginUser = commonAPI.getUserByName(JwtUtil.getUserNameByToken(SpringContextUtils.getHttpServletRequest()));
                //当前登录人拥有的角色
                hasRoles = commonAPI.queryUserRolesById(loginUser.getId());
            }
            
            log.info("get loginUser info: {}", loginUser);
            log.info("get loginRoles info: {}", hasRoles != null ? hasRoles.toArray() : "空");
            
            //拥有的角色 和 允许开发角色存在交集
            boolean hasIntersection = CommonUtils.hasIntersection(hasRoles, CommonConstant.allowDevRoles);
            //如果是超级管理员 或者 允许开发的角色，则不做限制
            if (loginUser!=null && ("admin".equals(loginUser.getUsername()) || hasIntersection)) {
                return true;
            }
            
            this.returnErrorMessage(response);
            return false;
        }
        return true;
    }


    /**
     * 返回结果
     *
     * @param response
     */
    private void returnErrorMessage(HttpServletResponse response) {
        //校验失败返回前端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            Result<?> result = Result.error("低代码开发模式为发布模式，不允许使用在线配置！！");
            out.print(JSON.toJSON(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

