package org.jeecg.config.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.config.security.password.PasswordGrantAuthenticationConvert;
import org.jeecg.config.security.password.PasswordGrantAuthenticationProvider;
import org.jeecg.modules.base.service.BaseCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @author eightmonth@qq.com
 * @date 2024/1/2 9:29
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private JdbcTemplate jdbcTemplate;
    private OAuth2AuthorizationService authorizationService;
    private final CommonAPI commonAPI;
    private final RedisUtil redisUtil;
    private final JeecgBaseConfig jeecgBaseConfig;
    private final BaseCommonService baseCommonService;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint.accessTokenRequestConverter(new PasswordGrantAuthenticationConvert())
                        .authenticationProvider(new PasswordGrantAuthenticationProvider(jwtCustomizer(), authorizationService, tokenGenerator(), commonAPI, redisUtil, jeecgBaseConfig, baseCommonService)))
                //开启OpenID Connect 1.0（其中oidc为OpenID Connect的缩写）。 访问 /.well-known/openid-configuration即可获取认证信息
                .oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0
        http
                //将需要认证的请求，重定向到login页面行登录认证。
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/sys/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // 使用jwt处理接收到的access token
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                //设置所有请求都需要认证，未认证的请求都被重定向到login页面进行登录
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/cas/client/validateLogin")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/randomImage/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/checkCaptcha")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/login")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/mLogin")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/logout")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/thirdLogin/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/getEncryptedString")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/sms")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/phoneLogin")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/user/checkOnlyUser")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/user/register")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/user/phoneVerification")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/user/passwordChange")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/auth/2step-code")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/common/static/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/common/pdf/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/generic/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/getLoginQrcode/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/getQrcodeToken/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/checkAuth")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/doc.html")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.js")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.css")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.html")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.svg")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.pdf")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.jpg")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.png")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.gif")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.ico")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.ttf")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.woff")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.woff2")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/druid/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger**/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/webjars/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/WW_verify*")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sys/annountCement/show/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/jmreport/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.js.map")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/**/*.css.map")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/view")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/page/queryById")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/onlDragDatasetHead/getAllChartData")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/onlDragDatasetHead/getTotalData")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/mock/json/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/test/bigScreen/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/bigscreen/template1/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/bigscreen/template1/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/websocket/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/newsWebsocket/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/vxeSocket/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/test/seata/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/error")).permitAll()
                        .anyRequest().authenticated()
                )
                .cors(cors -> cors
                        .configurationSource(req -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.applyPermitDefaultValues();
                            config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                            return config;
                        }))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    /**
     * 注册客户端信息
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     *配置 JWK，为JWT(id_token)提供加密密钥，用于加密/解密或签名/验签
     * JWK详细见：https://datatracker.ietf.org/doc/html/draft-ietf-jose-json-web-key-41
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     *生成RSA密钥对，给上面jwkSource() 方法的提供密钥对
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * 配置jwt解析器
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     *配置认证服务器请求地址
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     *配置token生成器
     */
    @Bean
    OAuth2TokenGenerator<?> tokenGenerator() {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource()));
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            JwsHeader.Builder headers = context.getJwsHeader();
            JwtClaimsSet.Builder claims = context.getClaims();
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                // 自定义 access_token headers/claims
                claims.claim("username", context.getPrincipal().getName());

            } else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                // 自定义 id_token headers/claims for
                claims.claim(IdTokenClaimNames.AUTH_TIME, Date.from(Instant.now()));

            }
        };
    }

}
