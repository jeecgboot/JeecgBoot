package org.jeecg.common.aspect.annotation;

import java.lang.annotation.*;

/**
 * online请求拦截专用注解
 * @author: jeecg-boot
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface OnlineAuth {

    /**
     * 请求关键字，在xxx/code之前的字符串
     * @return
     */
    String value();
}
