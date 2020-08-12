package org.jeecg.common.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要字典翻译的方法
 *
 * @author zxiaozhou
 * @date 2020-06-22 17:27
 * @since JDK1.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DictMethod {
}
