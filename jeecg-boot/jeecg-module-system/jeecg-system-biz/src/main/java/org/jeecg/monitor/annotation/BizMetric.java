package org.jeecg.monitor.annotation;

import java.lang.annotation.*;

/**
 * 业务指标注解，用于标记需要采集业务数据的方法
 *
 * @author JeecgBoot
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BizMetric {
    /**
     * 指标编码
     */
    String metricCode();

    /**
     * 指标名称
     */
    String metricName();

    /**
     * 业务类型
     */
    String bizType() default "";

    /**
     * 指标单位
     */
    String unit() default "";
}
