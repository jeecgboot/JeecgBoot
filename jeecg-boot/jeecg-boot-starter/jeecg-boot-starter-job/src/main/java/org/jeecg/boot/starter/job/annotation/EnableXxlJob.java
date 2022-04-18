package org.jeecg.boot.starter.job.annotation;

import org.jeecg.boot.starter.job.config.XxlJobConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zyf
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ XxlJobConfiguration.class })
public @interface EnableXxlJob {

}
