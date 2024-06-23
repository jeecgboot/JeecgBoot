package org.jeecg.common.desensitization.annotation;

import java.lang.annotation.*;

/**
 * 解密注解
 *
 * 在方法上定义 将方法返回对象中的敏感字段 解密，需要注意的是，如果没有加密过，解密会出问题，返回原字符串
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SensitiveDecode {

    /**
     * 指明需要脱敏的实体类class
     * @return
     */
    Class entity() default Object.class;
}
