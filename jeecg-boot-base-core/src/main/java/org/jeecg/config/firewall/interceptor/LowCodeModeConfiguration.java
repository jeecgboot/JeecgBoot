package org.jeecg.config.firewall.interceptor;

import org.jeecg.config.firewall.interceptor.enums.LowCodeUrlsEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LowCodeModeConfiguration implements WebMvcConfigurer {

    public LowCodeModeInterceptor payInterceptor() {
        return new LowCodeModeInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(payInterceptor()).addPathPatterns(LowCodeUrlsEnum.getLowCodeInterceptUrls());
    }
}
