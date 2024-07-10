package org.jeecg.config.security;

import io.undertow.servlet.spec.HttpServletRequestImpl;
import io.undertow.util.HttpString;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 复制仪盘表请求query体携带的token
 * @author eightmonth
 * @date 2024/7/3 14:04
 */
@Component
@Order(value = Integer.MIN_VALUE)
public class CopyTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 以下为undertow定制代码，如切换其它servlet容器，需要同步更换
        HttpServletRequestImpl undertowRequest = (HttpServletRequestImpl) request;
        String bearerToken = request.getParameter("token");
        String headerBearerToken = request.getHeader("X-Access-Token");
        if (StringUtils.hasText(bearerToken)) {
            undertowRequest.getExchange().getRequestHeaders().add(new HttpString("Authorization"), "bearer " + bearerToken);
        } else if (StringUtils.hasText(headerBearerToken)) {
            undertowRequest.getExchange().getRequestHeaders().add(new HttpString("Authorization"), "bearer " + headerBearerToken);
        }
        filterChain.doFilter(undertowRequest, response);
    }


}
