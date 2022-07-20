package org.jeecg.config.sign.interceptor;

import org.jeecg.config.filter.RequestBodyReserveFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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

    //update-begin-author:taoyan date:20220427 for: issues/I53J5E post请求X_SIGN签名拦截校验后报错, request body 为空
    @Bean
    public RequestBodyReserveFilter requestBodyReserveFilter(){
        return new RequestBodyReserveFilter();
    }

    @Bean
    public FilterRegistrationBean reqBodyFilterRegistrationBean(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(requestBodyReserveFilter());
        registration.setName("requestBodyReserveFilter");
        // 建议此处只添加post请求地址而不是所有的都需要走过滤器
        registration.addUrlPatterns(SIGN_URL_LIST);
        return registration;
    }
    //update-end-author:taoyan date:20220427 for: issues/I53J5E post请求X_SIGN签名拦截校验后报错, request body 为空

}
