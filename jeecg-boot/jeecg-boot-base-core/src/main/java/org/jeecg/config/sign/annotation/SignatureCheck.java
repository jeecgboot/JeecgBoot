package org.jeecg.config.sign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 签名校验注解
 * 用于方法级别的签名验证，功能等同于yml中的jeecg.signUrls配置
 * 参考DragSignatureAspect的设计思路，使用AOP切面实现
 * 
 * @author GitHub Copilot
 * @since 2025-12-15
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SignatureCheck {
    
    /**
     * 是否启用签名校验
     * @return true-启用(默认), false-禁用
     */
    boolean enabled() default true;
    
    /**
     * 签名校验失败时的错误消息
     * @return 错误消息
     */
    String errorMessage() default "Sign签名校验失败！";
    
}
