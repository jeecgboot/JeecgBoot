package org.jeecg.common.config.mqtoken;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 存放token到上下文供队列调用feign使用
 * @author zyf
 */
public class TransmitUserTokenFilter implements Filter {

    private static String X_ACCESS_TOKEN="X-Access-Token";

    public TransmitUserTokenFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.initUserInfo((HttpServletRequest) request);
        chain.doFilter(request, response);
    }

    private void initUserInfo(HttpServletRequest request) {
        String token = request.getHeader(X_ACCESS_TOKEN);
        if (token!=null) {
            try {
                //将token放入上下文中
                UserTokenContext.setToken(token);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void destroy() {
    }
}