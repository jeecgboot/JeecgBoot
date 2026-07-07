package com.xxl.job.admin.framework.web.xxlsso;

import com.xxl.sso.core.auth.interceptor.XxlSsoWebInterceptor;
import com.xxl.sso.core.bootstrap.XxlSsoBootstrap;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xuxueli 2018-11-15
 */
@Configuration
public class XxlSsoConfig implements WebMvcConfigurer {


    @Value("${xxl-sso.token.key}")
    private String tokenKey;

    @Value("${xxl-sso.token.timeout}")
    private long tokenTimeout;

    @Value("${xxl-sso.client.excluded.paths}")
    private String excludedPaths;

    @Value("${xxl-sso.client.login.path}")
    private String loginPath;


    @Resource
    private SimpleLoginStore loginStore;


    /**
     * 1、配置 XxlSsoBootstrap
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public XxlSsoBootstrap xxlSsoBootstrap() {

        XxlSsoBootstrap bootstrap = new XxlSsoBootstrap();
        bootstrap.setLoginStore(loginStore);
        bootstrap.setTokenKey(tokenKey);
        bootstrap.setTokenTimeout(tokenTimeout);
        return bootstrap;
    }

    /**
     * 2、配置 XxlSso 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 2.1、build xxl-sso interceptor
        XxlSsoWebInterceptor webInterceptor = new XxlSsoWebInterceptor(excludedPaths, loginPath);

        // 2.2、add interceptor
        registry.addInterceptor(webInterceptor).addPathPatterns("/**");
    }

}
