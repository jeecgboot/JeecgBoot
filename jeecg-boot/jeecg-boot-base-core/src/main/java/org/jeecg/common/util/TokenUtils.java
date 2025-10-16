package org.jeecg.common.util;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.TenantConstant;
import org.jeecg.common.desensitization.util.SensitiveInfoUtil;
import org.jeecg.common.exception.JeecgBoot401Exception;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @Author scott
 * @Date 2019/9/23 14:12
 * @Description: 编程校验token有效性
 */
@Slf4j
public class TokenUtils {

    /**
     * 获取 request 里传递的 token
     *
     * @param request
     * @return
     */
    public static String getTokenByRequest(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        
        String token = request.getParameter("token");
        if (token == null) {
            token = request.getHeader("X-Access-Token");
        }
        return token;
    }
    
    /**
     * 获取 request 里传递的 token
     * @return
     */
    public static String getTokenByRequest() {
        String token = null;
        try {
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            token = TokenUtils.getTokenByRequest(request);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return token;
    }

    /**
     * 获取 request 里传递的 tenantId (租户ID)
     *
     * @param request
     * @return
     */
    public static String getTenantIdByRequest(HttpServletRequest request) {
        String tenantId = request.getParameter(TenantConstant.TENANT_ID);
        if (tenantId == null) {
            tenantId = oConvertUtils.getString(request.getHeader(CommonConstant.TENANT_ID));
        }

        if (oConvertUtils.isNotEmpty(tenantId) && "undefined".equals(tenantId)) {
            return null;
        }
        return tenantId;
    }

    /**
     * 获取 request 里传递的 lowAppId (低代码应用ID)
     *
     * @param request
     * @return
     */
    public static String getLowAppIdByRequest(HttpServletRequest request) {
        String lowAppId = request.getParameter(TenantConstant.FIELD_LOW_APP_ID);
        if (lowAppId == null) {
            lowAppId = oConvertUtils.getString(request.getHeader(TenantConstant.X_LOW_APP_ID));
        }
        return lowAppId;
    }

    /**
     * 验证Token（已重写为Sa-Token实现）
     */
    public static boolean verifyToken(HttpServletRequest request, CommonAPI commonApi) {
        log.debug(" -- url --" + request.getRequestURL());
        String token = getTokenByRequest(request);
        return TokenUtils.verifyToken(token, commonApi);
    }

    /**
     * 验证Token（已重写为Sa-Token实现）
     */
    public static boolean verifyToken(String token, CommonAPI commonApi) {
        if (StringUtils.isBlank(token)) {
            throw new JeecgBoot401Exception("token不能为空!");
        }

        // 使用Sa-Token校验token
        Object username = StpUtil.getLoginIdByToken(token);
        if (username == null) {
            throw new JeecgBoot401Exception("token非法无效!");
        }

        // 查询用户信息
        LoginUser user = commonApi.getUserByName(username.toString());
        if (user == null) {
            throw new JeecgBoot401Exception("用户不存在!");
        }
        
        // 判断用户状态
        if (user.getStatus() != 1) {
            throw new JeecgBoot401Exception("账号已被锁定,请联系管理员!");
        }
        
        return true;
    }

}
