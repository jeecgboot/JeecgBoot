package org.jeecg.boot.starter.lock.annotation;

import java.lang.annotation.*;

/**
 * Redisson分布式锁注解
 *
 * @author zyf
 * @date 2020-11-11
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DistributedLock {

    /**
     * 要锁的参数索引
     */
    int[] fieldIndexs() default {};

    /**
     * 要锁的参数的属性名
     */
    String[] fieldNames() default {};

    /**
     * 分布式锁名称
     *
     * @return String
     */
    String lockKey() default "";

    /**
     * 锁超时时间（单位：秒） 如果超过还没有解锁的话,就强制解锁
     *
     * @return int
     */
    int expireSeconds() default 10;

    /**
     * 等待多久（单位：秒）-1 则表示一直等待
     *
     * @return int
     */
    int waitTime() default 5;

    /**
     * 未取到锁时提示信息
     *
     * @return
     */
    String failMsg() default "获取锁失败，请稍后重试";
}
