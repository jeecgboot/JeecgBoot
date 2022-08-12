package org.jeecg.common.desensitization.annotation;


import org.jeecg.common.desensitization.enums.SensitiveEnum;

import java.lang.annotation.*;

/**
 * 在字段上定义 标识字段存储的信息是敏感的
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SensitiveField {

    /**
     * 不同类型处理不同
     * @return
     */
    SensitiveEnum type() default SensitiveEnum.ENCODE;
}
