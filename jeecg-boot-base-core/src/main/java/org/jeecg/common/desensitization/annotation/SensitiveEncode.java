package org.jeecg.common.desensitization.annotation;

import java.lang.annotation.*;

/**
 * 加密注解
 *
 * 在方法上声明 将方法返回对象中的敏感字段 加密/格式化
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SensitiveEncode {

    /**
     * 指明需要脱敏的实体类class
     * @return
     */
    Class entity() default Object.class;
}
