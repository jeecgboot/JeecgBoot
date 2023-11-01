package org.jeecg.modules.im.configuration;

import java.lang.annotation.*;

/**
 * 自定义用户操作日志注解
 * @author wu
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface ClientOperLog {
    String module() default ""; // 操作模块
    String type() default "";  // 操作类型
    String desc() default "";  // 操作说明

    String TYPE_CREATE = "新增";
    String TYPE_DELETE = "删除";
    String TYPE_UPDATE = "更新";
    String TYPE_SEARCH = "查询";
}