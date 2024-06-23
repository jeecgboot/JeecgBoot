package org.jeecg.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author scott
 */
@Configuration
public class SecuritySecureConfig {

    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }


    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // 登录成功处理类
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        http.authorizeRequests(authorize -> {
                    try {
                        authorize

                                //静态文件允许访问
                                .requestMatchers(adminContextPath + "/assets/**").permitAll()
                                //登录页面允许访问
                                .requestMatchers(adminContextPath + "/login", "/css/**", "/js/**", "/image/*").permitAll()
                                //其他所有请求需要登录
                                .anyRequest().authenticated()
                                .and()
                                //登录页面配置，用于替换security默认页面
                                .formLogin(formLogin -> formLogin.loginPage(adminContextPath + "/login").successHandler(successHandler))
                                //登出页面配置，用于替换security默认页面
                                .logout(logout -> logout.logoutUrl(adminContextPath + "/logout"))
                                .httpBasic(Customizer.withDefaults())
                                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                        .ignoringRequestMatchers(
                                        "/instances",
                                        "/actuator/**")
                                );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        return http.build();

    }

}