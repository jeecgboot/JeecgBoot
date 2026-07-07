package org.jeecg.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

import java.net.URI;

/**
 * @author scott
 */
@Configuration
public class SecuritySecureConfig {

    private final AdminServerProperties adminServer;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminServer = adminServerProperties;
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http
            .authorizeExchange(authorize -> authorize
                .pathMatchers(adminServer.path("/assets/**")).permitAll()
                .pathMatchers(adminServer.path("/login")).permitAll()
                .pathMatchers("/actuator/health/**").permitAll()
                .anyExchange().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage(adminServer.path("/login"))
                .authenticationSuccessHandler(loginSuccessHandler(adminServer.path("/")))
            )
            .logout(logout -> logout
                .logoutUrl(adminServer.path("/logout"))
                .logoutSuccessHandler(logoutSuccessHandler(adminServer.path("/login?logout")))
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .build();
    }

    private ServerAuthenticationSuccessHandler loginSuccessHandler(String uri) {
        RedirectServerAuthenticationSuccessHandler handler = new RedirectServerAuthenticationSuccessHandler();
        handler.setLocation(URI.create(uri));
        return handler;
    }

    private ServerLogoutSuccessHandler logoutSuccessHandler(String uri) {
        RedirectServerLogoutSuccessHandler handler = new RedirectServerLogoutSuccessHandler();
        handler.setLogoutSuccessUrl(URI.create(uri));
        return handler;
    }
}
