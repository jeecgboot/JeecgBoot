package org.jeecg.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jeecg.common.system.util.JwtUtil;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * 当用户被强退时，使客户端token失效
 * @author eightmonth@qq.com
 * @date 2024/3/7 17:30
 */
//@Component
@AllArgsConstructor
public class RedisTokenValidationFilter extends OncePerRequestFilter {
    private OAuth2AuthorizationService authorizationService;
    private JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求中获取token
        DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
        String token = defaultBearerTokenResolver.resolve(request);


        if (Objects.nonNull(token)) {
            // 检查认证信息是否已被清除，如果已被清除，则令该token失效
            OAuth2Authorization oAuth2Authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
            if (Objects.isNull(oAuth2Authorization)) {
                throw new OAuth2AuthenticationException(BearerTokenErrors.invalidToken("认证信息已失效，请重新登录"));
            }
        }
        filterChain.doFilter(request, response);
    }
}
