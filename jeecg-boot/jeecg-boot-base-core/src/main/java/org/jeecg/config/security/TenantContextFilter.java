package org.jeecg.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 多租户上下文过滤器
 * 从请求头中获取租户ID并设置到TenantContext中
 * 对应shiro版本中JwtFilter里的TenantContext.setTenant逻辑
 *
 * @author jeecg-boot
 */
@Slf4j
@Component
public class TenantContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 从请求头获取租户ID并设置到TenantContext（多租户用到）
        String tenantId = request.getHeader(CommonConstant.TENANT_ID);
        TenantContext.setTenant(tenantId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 请求结束后清除，防止线程复用导致租户信息污染
            TenantContext.clear();
        }
    }
}
