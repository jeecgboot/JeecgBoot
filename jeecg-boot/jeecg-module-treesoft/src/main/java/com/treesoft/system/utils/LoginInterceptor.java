package com.treesoft.system.utils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(true);
        String userName = (String) session.getAttribute("LOGIN_USER_NAME");
        Map<String, String> map = new HashMap<>();//LoginController.getLoginUserMap();
        map.put(userName, session.getId());
        if (userName != null) {
            String tempID = map.get(userName);
            String sessionId = session.getId();
            if (!sessionId.equals(tempID)) {
                PrintWriter out = response.getWriter();
                StringBuilder builder = new StringBuilder();
                response.setContentType("text/html;charset=UTF-8");
                builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
                builder.append("parent.$.messager.alert(\"操作提示\", \"您好,该帐号已在其他地方登录！\",\"error\");");
                builder.append(" </script>");
                out.print(builder);
                out.close();
                return false;
            }
        }
        if (userName == null) {
            PrintWriter out = response.getWriter();
            StringBuilder builder = new StringBuilder();
            response.setContentType("text/html;charset=UTF-8");
            builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
            builder.append("window.top.location.href=\"");
            builder.append(request.getContextPath());
            builder.append("/treesoft/login\";</script>");
            out.print(builder);
            out.close();
            return false;
        }
        return true;
    }
}
