package org.jeecg.config.shiro.ignore;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.config.shiro.IgnoreAuth;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 在spring boot初始化时，根据@RestController注解获取当前spring容器中的bean
 * @author eightmonth
 * @date 2024/4/18 11:35
 */
@Slf4j
@Lazy(false)
@Component
@AllArgsConstructor
public class IgnoreAuthPostProcessor implements InitializingBean {

    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    @Override
    public void afterPropertiesSet() throws Exception {

        long startTime = System.currentTimeMillis();
        
        List<String> ignoreAuthUrls = new ArrayList<>();
        
        // 优化：直接从HandlerMethod过滤，避免重复扫描
        requestMappingHandlerMapping.getHandlerMethods().values().stream()
            .filter(handlerMethod -> handlerMethod.getMethod().isAnnotationPresent(IgnoreAuth.class))
            .forEach(handlerMethod -> {
                Class<?> clazz = handlerMethod.getBeanType();
                Method method = handlerMethod.getMethod();
                ignoreAuthUrls.addAll(processIgnoreAuthMethod(clazz, method));
            });

        log.info("Init Token ignoreAuthUrls Config [ 集合 ]  ：{}", ignoreAuthUrls);
        if (!CollectionUtils.isEmpty(ignoreAuthUrls)) {
            InMemoryIgnoreAuth.set(ignoreAuthUrls);
        }

        // 计算方法的耗时
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("Init Token ignoreAuthUrls Config [ 耗时 ] ：" + elapsedTime + "ms");
    }

    // 优化：新方法处理单个@IgnoreAuth方法，减少重复注解检查
    private List<String> processIgnoreAuthMethod(Class<?> clazz, Method method) {
        RequestMapping base = clazz.getAnnotation(RequestMapping.class);
        String[] baseUrl = Objects.nonNull(base) ? base.value() : new String[]{};
        
        String[] uri = null;
        if (method.isAnnotationPresent(RequestMapping.class)) {
            uri = method.getAnnotation(RequestMapping.class).value();
        } else if (method.isAnnotationPresent(GetMapping.class)) {
            uri = method.getAnnotation(GetMapping.class).value();
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            uri = method.getAnnotation(PostMapping.class).value();
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            uri = method.getAnnotation(PutMapping.class).value();
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            uri = method.getAnnotation(DeleteMapping.class).value();
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            uri = method.getAnnotation(PatchMapping.class).value();
        }
        
        return uri != null ? rebuildUrl(baseUrl, uri) : Collections.emptyList();
    }

    private List<String> rebuildUrl(String[] bases, String[] uris) {
        List<String> urls = new ArrayList<>();
        if (bases.length > 0) {
            for (String base : bases) {
                for (String uri : uris) {
                    // 如果uri包含路径占位符, 则需要将其替换为*
                    if (uri.matches(".*\\{.*}.*")) {
                        uri = uri.replaceAll("\\{.*?}", "*");
                    }
                    urls.add(prefix(base) + prefix(uri));
                }
            }
        } else {
            Arrays.stream(uris).forEach(uri -> {
                urls.add(prefix(uri));
            });
        }
        return urls;
    }

    private String prefix(String seg) {
        return seg.startsWith("/") ? seg : "/"+seg;
    }
}
