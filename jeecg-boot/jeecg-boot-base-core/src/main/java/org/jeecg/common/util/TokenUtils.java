package org.jeecg.common.util;

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
     * 验证Token
     */
    public static boolean verifyToken(HttpServletRequest request, CommonAPI commonApi, RedisUtil redisUtil) {
        log.debug(" -- url --" + request.getRequestURL());
        String token = getTokenByRequest(request);
        return TokenUtils.verifyToken(token, commonApi, redisUtil);
    }

    /**
     * 验证Token
     */
    public static boolean verifyToken(String token, CommonAPI commonApi, RedisUtil redisUtil) {
        if (StringUtils.isBlank(token)) {
            throw new JeecgBoot401Exception("token不能为空!");
        }

        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new JeecgBoot401Exception("token非法无效!");
        }

        // 查询用户信息
        LoginUser user = TokenUtils.getLoginUser(username, commonApi, redisUtil);
        //LoginUser user = commonApi.getUserByName(username);
        if (user == null) {
            throw new JeecgBoot401Exception("用户不存在!");
        }
        // 判断用户状态
        if (user.getStatus() != 1) {
            throw new JeecgBoot401Exception("账号已被锁定,请联系管理员!");
        }
        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, username, user.getPassword(), redisUtil)) {
            // 用户登录Token过期提示信息
            String userLoginTokenErrorMsg = oConvertUtils.getString(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN_ERROR_MSG + token));
            throw new JeecgBoot401Exception(oConvertUtils.isEmpty(userLoginTokenErrorMsg)? CommonConstant.TOKEN_IS_INVALID_MSG: userLoginTokenErrorMsg);
        }
        return true;
    }

    /**
     * 刷新token（保证用户在线操作不掉线）
     * @param token
     * @param userName
     * @param passWord
     * @param redisUtil
     * @return
     */
    private static boolean jwtTokenRefresh(String token, String userName, String passWord, RedisUtil redisUtil) {
        String cacheToken = oConvertUtils.getString(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token));
        if (oConvertUtils.isNotEmpty(cacheToken)) {
            // 校验token有效性
            if (!JwtUtil.verify(cacheToken, userName, passWord)) {
                // 从token中解析客户端类型，保持续期时使用相同的客户端类型
                String clientType = JwtUtil.getClientType(token);
                String newAuthorization = JwtUtil.sign(userName, passWord, clientType);
                // 根据客户端类型设置对应的缓存有效时间
                long expireTime = CommonConstant.CLIENT_TYPE_APP.equalsIgnoreCase(clientType) 
                    ? JwtUtil.APP_EXPIRE_TIME * 2 / 1000 
                    : JwtUtil.EXPIRE_TIME * 2 / 1000;
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, expireTime);
            }
            return true;
        }
        return false;
    }

    /**
     * 获取登录用户
     *
     * @param commonApi
     * @param username
     * @return
     */
    public static LoginUser getLoginUser(String username, CommonAPI commonApi, RedisUtil redisUtil) {
        LoginUser loginUser = null;
        String loginUserKey = CacheConstant.SYS_USERS_CACHE + "::" + username;
        //【重要】此处通过redis原生获取缓存用户，是为了解决微服务下system服务挂了，其他服务互调不通问题---
        if (redisUtil.hasKey(loginUserKey)) {
            try {
                loginUser = (LoginUser) redisUtil.get(loginUserKey);
                //解密用户
                SensitiveInfoUtil.handlerObject(loginUser, false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            // 查询用户信息
            loginUser = commonApi.getUserByName(username);
        }
        return loginUser;
    }
}
