package org.jeecg.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * websocket 前端将token放到子协议里传入 与后端建立连接时需要用到http协议，此处用于校验token的有效性
 * @Author taoYan
 * @Date 2022/4/21 17:01
 **/
@Slf4j
public class WebsocketFilter implements Filter {

    private static final String TOKEN_KEY = "Sec-WebSocket-Protocol";

    private static CommonAPI commonApi;

    private static RedisUtil redisUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (commonApi == null) {
            commonApi = SpringContextUtils.getBean(CommonAPI.class);
        }
        if (redisUtil == null) {
            redisUtil = SpringContextUtils.getBean(RedisUtil.class);
        }
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String token = request.getHeader(TOKEN_KEY);

        log.debug("Websocket连接 Token安全校验，Path = {}，token:{}", request.getRequestURI(), token);

        try {
            TokenUtils.verifyToken(token, commonApi, redisUtil);
        } catch (Exception exception) {
            //log.error("Websocket连接 Token安全校验失败，IP:{}, Token:{}, Path = {}，异常：{}", oConvertUtils.getIpAddrByRequest(request), token, request.getRequestURI(), exception.getMessage());
            log.debug("Websocket连接 Token安全校验失败，IP:{}, Token:{}, Path = {}，异常：{}", oConvertUtils.getIpAddrByRequest(request), token, request.getRequestURI(), exception.getMessage());
            return;
        }
        //update-begin---author:scott ---date:20261013  for：【安全 GHSA-5j5w-pm5g-5wr8】系统通知websocket(/websocket/{userId})绑定token身份，禁止订阅他人的实时消息-----------
        String uri = request.getRequestURI();
        int idx = uri.indexOf("/websocket/");
        if (idx >= 0) {
            String rest = uri.substring(idx + "/websocket/".length());
            int slash = rest.indexOf('/');
            String pathUserId = slash >= 0 ? rest.substring(0, slash) : rest;
            // 前端路径userId格式为 {真实userId}_{wsClientId}(md5(token))，取下划线前的真实userId比对；同时兼容无后缀的纯userId
            int underline = pathUserId.indexOf('_');
            String realPathUserId = underline >= 0 ? pathUserId.substring(0, underline) : pathUserId;
            LoginUser loginUser = commonApi.getUserByName(JwtUtil.getUsername(token));
            if (loginUser == null || !loginUser.getId().equals(realPathUserId)) {
                log.warn("Websocket连接 用户身份与路径userId不一致，已拒绝。Path = {}", uri);
                return;
            }
        }
        //update-end---author:scott ---date:20261013  for：【安全 GHSA-5j5w-pm5g-5wr8】系统通知websocket(/websocket/{userId})绑定token身份，禁止订阅他人的实时消息-----------
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        response.setHeader(TOKEN_KEY, token);
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
