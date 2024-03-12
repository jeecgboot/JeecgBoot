package org.jeecg.config.security.phone;

import org.jeecg.config.security.LoginType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * 手机号模式认证专用token类型，方法spring authorization server进行认证流转，配合convert使用
 * @author EightMonth
 * @date 2024/1/1
 */
public class PhoneGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    public PhoneGrantAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(LoginType.PHONE), clientPrincipal, additionalParameters);
    }

}
