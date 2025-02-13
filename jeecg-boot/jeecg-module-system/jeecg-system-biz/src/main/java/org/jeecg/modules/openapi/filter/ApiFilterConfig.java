package org.jeecg.modules.openapi.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @date 2024/12/19 17:09
 */
@Configuration
public class ApiFilterConfig {

    /**
     *
     * @Description: 【注册api加密过滤器】
     */
    @Bean
    public FilterRegistrationBean<ApiAuthFilter> authFilter() {
        FilterRegistrationBean<ApiAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ApiAuthFilter());
        registration.setName("apiAuthFilter");
        registration.addUrlPatterns("/openapi/call/*");
        return registration;
    }
}
