package com.vone.mq.config;

import com.vone.mq.utils.JWTUtil;
import com.vone.mq.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(true);
        Map<String, Object> map = new HashMap<>();
        try {
            String token = (String) session.getAttribute("token");
            if (token == null) {
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
            String username = JWTUtil.getUsername(token);
            return !StringUtils.isEmpty(username);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token无效");
        }
        return false;
    }
}
