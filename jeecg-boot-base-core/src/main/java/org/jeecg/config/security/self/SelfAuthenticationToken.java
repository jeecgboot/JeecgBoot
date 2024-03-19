package org.jeecg.config.security.self;

import org.jeecg.config.security.LoginType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * 自用生成token，不支持对外请求，仅为程序内部生成token
 * @author eightmonth
 * @date 2024/3/19 11:37
 */
public class SelfAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
    public SelfAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(LoginType.SELF), clientPrincipal, additionalParameters);
    }
}
