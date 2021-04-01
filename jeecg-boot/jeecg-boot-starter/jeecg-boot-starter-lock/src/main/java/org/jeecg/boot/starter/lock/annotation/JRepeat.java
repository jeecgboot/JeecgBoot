package org.jeecg.boot.starter.lock.annotation;

/**
 * @author zyf
 */

import java.lang.annotation.*;

/**
 * 防止重复提交的注解
 *
 * @author 2019年6月18日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface JRepeat {

    /**
     * 超时时间
     *
     * @return
     */
    int lockTime();


    /**
     * redis 锁key的
     *
     * @return redis 锁key
     */
    String lockKey() default "";



}
