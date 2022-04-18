package org.jeecg.common.config.mqtoken;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 存放临时令牌Token到线程上下文
 *
 * 供队列、定时任务 feign调用使用（解决无会话Token问题）
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
                //将Token放入当前线程上下文中
                UserTokenContext.setToken(token);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {
    }
}