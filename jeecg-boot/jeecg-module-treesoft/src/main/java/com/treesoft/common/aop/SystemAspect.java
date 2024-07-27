package com.treesoft.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * AOP 打印日志
 *
 * @author 00fly
 * @version [版本号, 2019年1月12日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
@Aspect
@Component
public class SystemAspect {

    @Around("within(com.treesoft.system.web.*)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = className + "." + method.getName();
        List args = Arrays.stream(joinPoint.getArgs()).map(e -> {
            if (Objects.isNull(e)) {
                return null;
            }
            return isBaseType(e) ? e+"("+e.getClass().getSimpleName()+")" : e.getClass().getSimpleName() + ".class";
        }).collect(Collectors.toList());
        StopWatch clock = new StopWatch();
        clock.start(methodName);
        Object object = joinPoint.proceed();
        clock.stop();
        log.info("[{} ms] method = {} {}", clock.getTotalTimeMillis(), clock.getLastTaskName(), args);
        return object;
    }

    private boolean isBaseType(Object object) {
        if (object instanceof String ||
                object instanceof Integer ||
                object instanceof Byte ||
                object instanceof Long ||
                object instanceof Double ||
                object instanceof Float ||
                object instanceof Character ||
                object instanceof Short ||
                object instanceof Boolean) {
            return true;
        }
        if (Objects.nonNull(object) && object.getClass().getName().startsWith("com.treesoft")) {
            return true;
        }
        return false;
    }

}
