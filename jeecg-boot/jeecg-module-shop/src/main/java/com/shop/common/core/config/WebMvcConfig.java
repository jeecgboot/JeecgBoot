package com.shop.common.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置, 拦截器、资源映射等都在此配置
 * 2019-06-12 10:11
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 支持跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").maxAge(3600)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "x-requested-with", "X-Custom-Header", "Authorization");
    }

    /**
     * 支持PUT、DELETE请求
     */
    @Bean
    public FormContentFilter httpPutFormContentFilter() {
        return new FormContentFilter();
    }

}
