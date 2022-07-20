package org.jeecg.common.system.annotation;

import java.lang.annotation.*;

/**
 * 将枚举类转化成字典数据
 * @Author taoYan
 * @Date 2022/7/8 10:34
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnumDict {

    /**
     * 作为字典数据的唯一编码
     */
    String value() default "";
}
