package com.vmq.aop;

import com.vmq.utils.LogUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

/**
 * 控制台打印日志
 */
@Slf4j
@Aspect
@Component
public class SystemAspect {

    @SneakyThrows
    @Around("within(com.vmq.controller.*)")
    public Object around(ProceedingJoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = className + "." + method.getName();
        StopWatch clock = new StopWatch();
        clock.start(methodName);
        Object object = joinPoint.proceed();
        clock.stop();
        String param = "";
        if (!"loginValid".equals(method.getName()) && !"regist".equals(method.getName())) {
            param = LogUtil.getResquestParam(joinPoint);
        } else {
            param = String.valueOf(joinPoint.getArgs()[0]);
        }
        log.info("[{} ms] method = {} {}", clock.getTotalTimeMillis(), clock.getLastTaskName(), param);
        return object;
    }



}
