package org.jeecg.config.security;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jeecg.config.security.app.AppGrantAuthenticationConvert;
import org.jeecg.config.security.app.AppGrantAuthenticationProvider;
import org.jeecg.config.security.password.PasswordGrantAuthenticationConvert;
import org.jeecg.config.security.password.PasswordGrantAuthenticationProvider;
import org.jeecg.config.security.phone.PhoneGrantAuthenticationConvert;
import org.jeecg.config.security.phone.PhoneGrantAuthenticationProvider;
import org.jeecg.config.security.social.SocialGrantAuthenticationConvert;
import org.jeecg.config.security.social.SocialGrantAuthenticationProvider;
import org.jeecg.config.shiro.ignore.InMemoryIgnoreAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
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
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * spring authorization server核心配置
 * @author eightmonth@qq.com
 * @date 2024/1/2 9:29
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private JdbcTemplate jdbcTemplate;
    private OAuth2AuthorizationService authorizationService;
    private JeecgAuthenticationConvert jeecgAuthenticationConvert;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // 注册自定义登录类型
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint.accessTokenRequestConverter(new PasswordGrantAuthenticationConvert())
                        .authenticationProvider(new PasswordGrantAuthenticationProvider(authorizationService, tokenGenerator())))
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint.accessTokenRequestConverter(new PhoneGrantAuthenticationConvert())
                        .authenticationProvider(new PhoneGrantAuthenticationProvider(authorizationService, tokenGenerator())))
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint.accessTokenRequestConverter(new AppGrantAuthenticationConvert())
                        .authenticationProvider(new AppGrantAuthenticationProvider(authorizationService, tokenGenerator())))
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint.accessTokenRequestConverter(new SocialGrantAuthenticationConvert())
                        .authenticationProvider(new SocialGrantAuthenticationProvider(authorizationService, tokenGenerator())))
                //开启OpenID Connect 1.0（其中oidc为OpenID Connect的缩写）。 访问 /.well-known/openid-configuration即可获取认证信息
                .oidc(Customizer.withDefaults());
        http
                //将需要认证的请求，重定向到login页面行登录认证。
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/sys/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                //设置所有请求都需要认证，未认证的请求都被重定向到login页面进行登录
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(InMemoryIgnoreAuth.get().stream().map(AntPathRequestMatcher::antMatcher).toList().toArray(new AntPathRequestMatcher[0])).permitAll()
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
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/getUserInfo")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/list")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/view")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/onlDragDatasetHead/getLoginUser")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/page/queryById")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/onlDragDatasetHead/getAllChartData")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/onlDragDatasetHead/getTotalData")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/drag/mock/json/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/test/bigScreen/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/bigscreen/visual/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/bigscreen/category/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/bigscreen/map/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/bigscreen/template1/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/bigscreen/template1/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/websocket/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/newsWebsocket/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/vxeSocket/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/test/seata/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/error")).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(cors -> cors
                        .configurationSource(req -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.applyPermitDefaultValues();
                            config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                            return config;
                        }))
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jeecgAuthenticationConvert)));
        return http.build();
    }

    /**
     * 数据库保存注册客户端信息
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
    @SneakyThrows
    public JWKSource<SecurityContext> jwkSource() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        // 如果不设置secureRandom，会存在一个问题，当应用重启后，原有的token将会全部失效，因为重启的keyPair与之前已经不同
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        // 重要！生产环境需要修改！
        secureRandom.setSeed("jeecg".getBytes());
        keyPairGenerator.initialize(256, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();

        ECKey jwk = new ECKey.Builder(Curve.P_256, publicKey)
                .privateKey(privateKey)
                .keyID("jeecg")
                .build();
        JWKSet jwkSet = new JWKSet(jwk);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 配置jwt解析器
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
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
                new JeecgOAuth2AccessTokenGenerator(new NimbusJwtEncoder(jwkSource())),
                new OAuth2RefreshTokenGenerator()
        );
    }

}
