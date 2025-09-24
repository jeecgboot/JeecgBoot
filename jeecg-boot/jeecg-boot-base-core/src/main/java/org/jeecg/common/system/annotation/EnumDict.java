package org.jeecg.common.system.annotation;

import java.lang.annotation.*;

/**
 * 将枚举类转化成字典数据
 * 
 * <<使用说明>>
 * 1. 枚举类需以 `Enum` 结尾，并且在类上添加 `@EnumDict` 注解。
 * 2. 需要手动将枚举类所在包路径** 添加到 `org.jeecg.common.system.util.ResourceUtil.BASE_SCAN_PACKAGES` 配置数组中。
 * 
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
