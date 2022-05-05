package org.jeecg.boot.starter.lock.annotation;

import org.jeecg.boot.starter.lock.enums.LockModel;

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
public @interface JLock {

    /**
     * 锁的模式:如果不设置,自动模式,当参数只有一个.使用 REENTRANT 参数多个 MULTIPLE
     */
    LockModel lockModel() default LockModel.AUTO;

    /**
     * 如果keys有多个,如果不设置,则使用 联锁
     * @return
     */
    String[] lockKey() default {};

    /**
     * key的静态常量:当key的spel的值是LIST,数组时使用+号连接将会被spel认为这个变量是个字符串
     * @return
     */
    String keyConstant() default "";


    /**
     * 锁超时时间,默认30000毫秒
     *
     * @return int
     */
    long expireSeconds() default 30000L;

    /**
     * 等待加锁超时时间,默认10000毫秒 -1 则表示一直等待
     *
     * @return int
     */
     long waitTime() default 10000L;

    /**
     * 未取到锁时提示信息
     *
     * @return
     */
    String failMsg() default "获取锁失败，请稍后重试";
}
