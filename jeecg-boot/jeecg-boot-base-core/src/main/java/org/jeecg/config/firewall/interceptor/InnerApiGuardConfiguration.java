package org.jeecg.config.firewall.interceptor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 单体模式注册内部接口拦截器 {@link InnerApiGuardInterceptor}
 * <p>
 * 只在类路径不存在 jeecg-boot-starter-cloud 时（单体模式）生效；微服务模式下内部接口需供 Feign 调用，由网关拦截外部访问。
 *
 * @author scott
 * @since 2026-10-14 单体模式下内部接口可被任意登录用户直接调用
 */
@Configuration
@ConditionalOnMissingClass("org.jeecg.config.JeecgCloudBaseConfig")
public class InnerApiGuardConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new InnerApiGuardInterceptor()).addPathPatterns("/**").order(Ordered.HIGHEST_PRECEDENCE);
    }
}
