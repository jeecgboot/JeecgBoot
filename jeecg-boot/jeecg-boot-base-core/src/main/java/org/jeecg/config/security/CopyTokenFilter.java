package org.jeecg.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 复制仪盘表请求query体携带的token
 * 注意：改为容器无关实现，避免 Undertow 专有类型转换导致在 Tomcat 下 ClassCastException。
 *
 * 来源优先级：
 * 1. Authorization 头（若存在则规范为 Bearer <token> 格式）
 * 2. 查询参数 token
 * 3. 自定义头 X-Access-Token
 *
 * 若最终获得 token，则通过请求包装器注入 Authorization 头，保持对下游过滤器/安全链可见。
 */
@Component
@Order(value = Integer.MIN_VALUE)
public class CopyTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 容器无关实现：根据 header/参数提取 token，并以 Authorization 注入
        String tokenHeader = request.getHeader("Authorization");
        String candidate = null;
        if (StringUtils.hasText(tokenHeader)) {
            String trimmed = tokenHeader.trim();
            if (startsWithIgnoreCase(trimmed, "Bearer ")) {
                candidate = trimmed;
            } else if (!trimmed.contains(" ")) { // 纯 token，无空格，视为需要规范化
                candidate = trimmed;
            } // 其他认证方案（如 Basic ...）保持不处理
        } else {
            String bearerToken = request.getParameter("token");
            String headerBearerToken = request.getHeader("X-Access-Token");
            if (StringUtils.hasText(bearerToken)) {
                candidate = bearerToken.trim();
            } else if (StringUtils.hasText(headerBearerToken)) {
                candidate = headerBearerToken.trim();
            }
        }

        if (StringUtils.hasText(candidate)) {
            final String authValue = startsWithIgnoreCase(candidate, "Bearer ") ? candidate : ("Bearer " + candidate);
            HttpServletRequest wrapped = new AuthorizationHeaderRequestWrapper(request, authValue);
            filterChain.doFilter(wrapped, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    private static class AuthorizationHeaderRequestWrapper extends HttpServletRequestWrapper {
        private final String authorization;

        AuthorizationHeaderRequestWrapper(HttpServletRequest request, String authorization) {
            super(request);
            this.authorization = authorization;
        }

        @Override
        public String getHeader(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return authorization;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return Collections.enumeration(Collections.singletonList(authorization));
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Set<String> names = new LinkedHashSet<>();
            Enumeration<String> e = super.getHeaderNames();
            while (e.hasMoreElements()) {
                names.add(e.nextElement());
            }
            names.add("Authorization");
            return Collections.enumeration(names);
        }
    }
}
