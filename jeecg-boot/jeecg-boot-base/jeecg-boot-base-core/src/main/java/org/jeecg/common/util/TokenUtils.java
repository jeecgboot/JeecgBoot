package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBoot401Exception;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;

import javax.servlet.http.HttpServletRequest;

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
        String token = request.getParameter("token");
        if (token == null) {
            token = request.getHeader("X-Access-Token");
        }
        return token;
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
            throw new JeecgBoot401Exception(CommonConstant.TOKEN_IS_INVALID_MSG);
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
                String newAuthorization = JwtUtil.sign(userName, passWord);
                // 设置Toekn缓存有效时间
                redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, newAuthorization);
                redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
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
        if(redisUtil.hasKey(loginUserKey)){
            loginUser = (LoginUser) redisUtil.get(loginUserKey);
        }else{
            // 查询用户信息
            loginUser = commonApi.getUserByName(username);
        }
        return loginUser;
    }
}
