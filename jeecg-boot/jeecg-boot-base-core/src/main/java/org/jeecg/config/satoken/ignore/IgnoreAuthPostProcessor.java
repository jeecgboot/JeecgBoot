package org.jeecg.config.satoken.ignore;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.config.satoken.IgnoreAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 扫描@IgnoreAuth注解的url，存储到内存中
 * @author eightmonth
 * @date 2024/4/18 15:09
 */
@Component
@Slf4j
public class IgnoreAuthPostProcessor implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<String> ignoreAuthList = new ArrayList<>();
        
        // 获取所有的RequestMapping
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        
        handlerMethods.forEach((mapping, handlerMethod) -> {
            // 获取方法上的@IgnoreAuth注解
            IgnoreAuth ignoreAuth = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), IgnoreAuth.class);
            
            if (ignoreAuth != null && mapping.getPathPatternsCondition() != null) {
                // 获取路径模式
                mapping.getPathPatternsCondition().getPatterns().forEach(pattern -> {
                    String path = pattern.getPatternString();
                    ignoreAuthList.add(path);
                });
            }
        });
        
        InMemoryIgnoreAuth.set(ignoreAuthList);
        log.info("Sa-Token 免认证路径加载完成，共{}条: {}", ignoreAuthList.size(), ignoreAuthList);
    }
}

