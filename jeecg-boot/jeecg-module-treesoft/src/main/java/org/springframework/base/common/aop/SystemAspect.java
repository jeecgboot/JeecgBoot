package org.springframework.base.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * AOP 打印日志
 *
 * @author 00fly
 * @version [版本号, 2019年1月12日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Aspect
@Component
public class SystemAspect {
    static final Logger LOGGER = LoggerFactory.getLogger(SystemAspect.class);

    @Around("within(org.springframework.base.system.web..*)")
    public Object around(ProceedingJoinPoint joinPoint)
            throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = className + "." + method.getName();
        Object[] args = joinPoint.getArgs();
        StopWatch clock = new StopWatch();
        clock.start(methodName);
        Object object = joinPoint.proceed();
        clock.stop();
        LOGGER.info("running {} ms, method = {} {}", clock.getTotalTimeMillis(), clock.getLastTaskName(), Arrays.asList(args));
        return object;
    }

}
