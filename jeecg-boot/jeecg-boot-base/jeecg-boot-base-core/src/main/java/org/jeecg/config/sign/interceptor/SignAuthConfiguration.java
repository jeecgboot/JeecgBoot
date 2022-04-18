package org.jeecg.config.sign.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 签名 拦截器配置
 * @author: jeecg-boot
 */
@Configuration
public class SignAuthConfiguration implements WebMvcConfigurer {
    public static String[] SIGN_URL_LIST = new String[]{"/sys/dict/getDictItems/*", "/sys/dict/loadDict/*",
            "/sys/dict/loadDictOrderByValue/*", "/sys/dict/loadDictItem/*", "/sys/dict/loadTreeData",
            "/sys/api/queryTableDictItemsByCode", "/sys/api/queryFilterTableDictInfo", "/sys/api/queryTableDictByKeys",
            "/sys/api/translateDictFromTable", "/sys/api/translateDictFromTableByKeys"};
    @Bean
    public SignAuthInterceptor signAuthInterceptor() {
        return new SignAuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signAuthInterceptor()).addPathPatterns(SIGN_URL_LIST);
    }
}
