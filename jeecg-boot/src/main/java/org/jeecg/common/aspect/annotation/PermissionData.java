package org.jeecg.common.aspect.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
  *  数据权限注解
 * @author taoyan
 * @date 2019年4月11日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface PermissionData {
	String value() default "";
	
	
	/**
	 * 配置菜单的组件路径,用于数据权限
	 */
	String pageComponent() default "";
}