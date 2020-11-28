package org.jeecg.config;

import org.jeecg.filter.GlobalAccessTokenFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * @author scott
 * @date 2020/5/26
 * 路由限流配置
 */
@Configuration
public class RateLimiterConfiguration {
    /**
     * IP限流 (通过exchange对象可以获取到请求信息，这边用了HostName)
     */
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }

    /**
     * 用户限流 (通过exchange对象可以获取到请求信息，获取当前请求的用户 TOKEN)
     */
    @Bean
    public KeyResolver userKeyResolver() {
        //使用这种方式限流，请求Header中必须携带X-Access-Token参数
        return exchange -> Mono.just(exchange.getRequest().getHeaders().getFirst(GlobalAccessTokenFilter.X_ACCESS_TOKEN));
    }

    /**
     * 接口限流 (获取请求地址的uri作为限流key)
     */
    @Bean
    public KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

}
