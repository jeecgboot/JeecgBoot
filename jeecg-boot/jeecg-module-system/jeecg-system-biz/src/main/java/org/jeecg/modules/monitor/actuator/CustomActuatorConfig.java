package org.jeecg.modules.monitor.actuator;

import org.jeecg.modules.monitor.actuator.httptrace.CustomInMemoryHttpTraceRepository;
import org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceProperties;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义健康监控配置类
 *
 * @Author: chenrui
 * @Date: 2024/5/13 17:20
 */
@Configuration
@EnableConfigurationProperties(HttpTraceProperties.class)
@AutoConfigureBefore(HttpTraceAutoConfiguration.class)
public class CustomActuatorConfig {

    /**
     * 请求追踪
     * @return
     * @author chenrui
     * @date 2024/5/14 14:52
     */
    @Bean
    @ConditionalOnProperty(prefix = "management.trace.http", name = "enabled", matchIfMissing = true)
    @ConditionalOnMissingBean(HttpTraceRepository.class)
    public CustomInMemoryHttpTraceRepository traceRepository() {
        return new CustomInMemoryHttpTraceRepository();
    }

}
