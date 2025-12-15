package org.jeecg.config.sign.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jeecg.config.sign.annotation.SignatureCheck;
import org.jeecg.config.sign.interceptor.SignAuthInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 基于AOP的签名验证切面
 * 复用SignAuthInterceptor的成熟签名验证逻辑
 * 
 * @author GitHub Copilot
 * @since 2025-12-15
 */
@Aspect
@Slf4j
@Component("signatureCheckAspect")
public class SignatureCheckAspect {
    
    /**
     * 复用SignAuthInterceptor的签名验证逻辑
     */
    private final SignAuthInterceptor signAuthInterceptor = new SignAuthInterceptor();

    /**
     * 验签切点：拦截所有标记了@SignatureCheck注解的方法
     */
    @Pointcut("@annotation(org.jeecg.config.sign.annotation.SignatureCheck)")
    private void signatureCheckPointCut() {
    }

    /**
     * 开始验签
     */
    @Before("signatureCheckPointCut()")
    public void doSignatureValidation(JoinPoint point) throws Exception {
        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        SignatureCheck signatureCheck = method.getAnnotation(SignatureCheck.class);
        
        log.info("AOP签名验证: {}.{}", method.getDeclaringClass().getSimpleName(), method.getName());
        
        // 如果注解被禁用，直接返回
        if (!signatureCheck.enabled()) {
            log.info("签名验证已禁用，跳过");
            return;
        }
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.error("无法获取请求上下文");
            throw new IllegalArgumentException("无法获取请求上下文");
        }
        
        HttpServletRequest request = attributes.getRequest();
        log.info("X-SIGN: {}, X-TIMESTAMP: {}", request.getHeader("X-SIGN"), request.getHeader("X-TIMESTAMP"));
        
        try {
            // 直接调用SignAuthInterceptor的验证逻辑
            signAuthInterceptor.validateSignature(request);
            log.info("AOP签名验证通过");
            
        } catch (IllegalArgumentException e) {
            // 使用注解中配置的错误消息，或者保留原始错误消息
            String errorMessage = signatureCheck.errorMessage();
            log.error("AOP签名验证失败: {}", e.getMessage());
            
            if ("Sign签名校验失败！".equals(errorMessage)) {
                // 如果是默认错误消息，使用原始的详细错误信息
                throw e;
            } else {
                // 如果是自定义错误消息，使用自定义消息
                throw new IllegalArgumentException(errorMessage, e);
            }
        } catch (Exception e) {
            // 包装其他异常
            String errorMessage = signatureCheck.errorMessage();
            log.error("AOP签名验证异常: {}", e.getMessage());
            throw new IllegalArgumentException(errorMessage, e);
        }
    }
}
