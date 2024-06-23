package org.jeecg.config.security;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

/**
 * spring authorization server 注册客户端便捷工具类
 * @author eightmonth@qq.com
 * @date 2024/3/7 11:22
 */
@Component
@AllArgsConstructor
public class ClientService {

    private RegisteredClientRepository registeredClientRepository;

    /**
     * 修改客户端token有效期
     * 认证码、设备码有效期与accessToken有效期保持一致
     */
    public void updateTokenValidation(String clientId, Long accessTokenValidation, Long refreshTokenValidation){
        RegisteredClient registeredClient = findByClientId(clientId);
        RegisteredClient.Builder builder = RegisteredClient.from(registeredClient);
        TokenSettings tokenSettings = TokenSettings.builder()
                .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                .accessTokenTimeToLive(Duration.ofSeconds(accessTokenValidation))
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .reuseRefreshTokens(true)
                .refreshTokenTimeToLive(Duration.ofSeconds(refreshTokenValidation))
                .authorizationCodeTimeToLive(Duration.ofSeconds(accessTokenValidation))
                .deviceCodeTimeToLive(Duration.ofSeconds(accessTokenValidation))
                .build();
        builder.tokenSettings(tokenSettings);
        registeredClientRepository.save(builder.build());
    }

    /**
     * 修改客户端授权类型
     * @param clientId
     * @param grantTypes
     */
    public void updateGrantType(String clientId, Set<AuthorizationGrantType> grantTypes) {
        RegisteredClient registeredClient = findByClientId(clientId);
        RegisteredClient.Builder builder = RegisteredClient.from(registeredClient);
        for (AuthorizationGrantType grantType : grantTypes) {
            builder.authorizationGrantType(grantType);
        }
        registeredClientRepository.save(builder.build());
    }

    /**
     * 修改客户端重定向uri
     * @param clientId
     * @param redirectUris
     */
    public void updateRedirectUris(String clientId, String redirectUris) {
        RegisteredClient registeredClient = findByClientId(clientId);
        RegisteredClient.Builder builder = RegisteredClient.from(registeredClient);
        builder.redirectUri(redirectUris);
        registeredClientRepository.save(builder.build());
    }

    /**
     * 修改客户端授权范围
     * @param clientId
     * @param scopes
     */
    public void updateScopes(String clientId, Set<String> scopes) {
        RegisteredClient registeredClient = findByClientId(clientId);
        RegisteredClient.Builder builder = RegisteredClient.from(registeredClient);
        for (String scope : scopes) {
            builder.scope(scope);
        }
        registeredClientRepository.save(builder.build());
    }

    public RegisteredClient findByClientId(String clientId) {
        return registeredClientRepository.findByClientId(clientId);
    }

}
