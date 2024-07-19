package com.treesoft.common.config;

import com.treesoft.system.utils.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns("/static/**","/treesoft/login","/treesoft/logout","/treesoft/loginVaildate")
                .addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
