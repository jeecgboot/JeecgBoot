package org.jeecg.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

/**
 * 仪盘表请求query体携带的token
 * @author eightmonth
 * @date 2024/7/3 14:04
 */
@Slf4j
@Component
@Order(value = Integer.MIN_VALUE)
public class CopyTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 以下为undertow定制代码，如切换其它servlet容器，需要同步更换
        String token = request.getHeader("Authorization");
        String bearerToken = request.getParameter("token");
        String headerBearerToken = request.getHeader("X-Access-Token");
        String finalToken;

        log.debug("【仪盘表请求query体携带的token】CopyTokenFilter token: {}, bearerToken: {}, headerBearerToken: {}", token, bearerToken, headerBearerToken);

        if (StringUtils.hasText(token)) {
            finalToken = "bearer " + token;
        } else if (StringUtils.hasText(bearerToken)) {
            finalToken = "bearer " + bearerToken;
        } else if (StringUtils.hasText(headerBearerToken)) {
            finalToken = "bearer " + headerBearerToken;
        } else {
            finalToken = null;
        }

        if (finalToken != null) {
            HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
                @Override
                public String getHeader(String name) {
                    if ("Authorization".equalsIgnoreCase(name)) {
                        return finalToken;
                    }
                    return super.getHeader(name);
                }

                @Override
                public Enumeration<String> getHeaders(String name) {
                    if ("Authorization".equalsIgnoreCase(name)) {
                        return Collections.enumeration(Collections.singleton(finalToken));
                    }
                    return super.getHeaders(name);
                }

                @Override
                public Enumeration<String> getHeaderNames() {
                    List<String> names = Collections.list(super.getHeaderNames());
                    if (!names.contains("Authorization")) {
                        names.add("Authorization");
                    }
                    return Collections.enumeration(names);
                }
            };
            filterChain.doFilter(wrapper, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}