package org.jeecg.config.shiro.ignore;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.config.shiro.IgnoreAuth;
import org.springframework.beans.factory.InitializingBean;
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
@Component
@AllArgsConstructor
public class IgnoreAuthPostProcessor implements InitializingBean {

    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    @Override
    public void afterPropertiesSet() throws Exception {

        long startTime = System.currentTimeMillis();
        
        List<String> ignoreAuthUrls = new ArrayList<>();
        Set<Class<?>> restControllers = requestMappingHandlerMapping.getHandlerMethods().values().stream().map(HandlerMethod::getBeanType).collect(Collectors.toSet());
        for (Class<?> restController : restControllers) {
            ignoreAuthUrls.addAll(postProcessRestController(restController));
        }

        log.info("Init Token ignoreAuthUrls Config [ 集合 ]  ：{}", ignoreAuthUrls);
        if (!CollectionUtils.isEmpty(ignoreAuthUrls)) {
            InMemoryIgnoreAuth.set(ignoreAuthUrls);
        }

        // 计算方法的耗时
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("Init Token ignoreAuthUrls Config [ 耗时 ] ：" + elapsedTime + "毫秒");
    }

    private List<String> postProcessRestController(Class<?> clazz) {
        List<String> ignoreAuthUrls = new ArrayList<>();
        RequestMapping base = clazz.getAnnotation(RequestMapping.class);
        String[] baseUrl = Objects.nonNull(base) ? base.value() : new String[]{};
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(IgnoreAuth.class) && method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String[] uri = requestMapping.value();
                ignoreAuthUrls.addAll(rebuildUrl(baseUrl, uri));
            } else if (method.isAnnotationPresent(IgnoreAuth.class) && method.isAnnotationPresent(GetMapping.class)) {
                GetMapping requestMapping = method.getAnnotation(GetMapping.class);
                String[] uri = requestMapping.value();
                ignoreAuthUrls.addAll(rebuildUrl(baseUrl, uri));
            } else if (method.isAnnotationPresent(IgnoreAuth.class) && method.isAnnotationPresent(PostMapping.class)) {
                PostMapping requestMapping = method.getAnnotation(PostMapping.class);
                String[] uri = requestMapping.value();
                ignoreAuthUrls.addAll(rebuildUrl(baseUrl, uri));
            } else if (method.isAnnotationPresent(IgnoreAuth.class) && method.isAnnotationPresent(PutMapping.class)) {
                PutMapping requestMapping = method.getAnnotation(PutMapping.class);
                String[] uri = requestMapping.value();
                ignoreAuthUrls.addAll(rebuildUrl(baseUrl, uri));
            } else if (method.isAnnotationPresent(IgnoreAuth.class) && method.isAnnotationPresent(DeleteMapping.class)) {
                DeleteMapping requestMapping = method.getAnnotation(DeleteMapping.class);
                String[] uri = requestMapping.value();
                ignoreAuthUrls.addAll(rebuildUrl(baseUrl, uri));
            } else if (method.isAnnotationPresent(IgnoreAuth.class) && method.isAnnotationPresent(PatchMapping.class)) {
                PatchMapping requestMapping = method.getAnnotation(PatchMapping.class);
                String[] uri = requestMapping.value();
                ignoreAuthUrls.addAll(rebuildUrl(baseUrl, uri));
            }
        }

        return ignoreAuthUrls;
    }

    private List<String> rebuildUrl(String[] bases, String[] uris) {
        List<String> urls = new ArrayList<>();
        if (bases.length > 0) {
            for (String base : bases) {
                for (String uri : uris) {
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
