package com.vone.mq.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(true);
        String token = (String) session.getAttribute("token");
        Map<String, Object> map = new HashMap<>();
        try {
            log.info(token);
            //获取请求头中的令牌
            if (token == null) {
                return false;
            }
            String username = JWTUtil.getUsername(token);
            return Objects.nonNull(username);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token无效");
        }
        return false;
    }
}
