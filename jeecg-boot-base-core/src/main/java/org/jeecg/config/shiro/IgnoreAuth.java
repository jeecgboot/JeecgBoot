package org.jeecg.config.shiro;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 免认证注解，认证系统结合spring MVC的@RequestMapping获取请求路径进行免登录配置
 * @author eightmonth@qq.com
 * @date 2024/2/28 9:58
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreAuth {
}
