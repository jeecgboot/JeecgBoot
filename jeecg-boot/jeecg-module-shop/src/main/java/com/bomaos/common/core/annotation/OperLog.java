package com.bomaos.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志记录注解
 * Created by Panyoujie on 2020-03-21 17:03
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperLog {

    /**
     * 模块
     */
    String value();

    /**
     * 功能
     */
    String desc();

    /**
     * 是否记录请求参数
     */
    boolean param() default true;

    /**
     * 是否记录返回结果
     */
    boolean result() default false;

}
