package org.jeecg.common.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典注解
 * @author: dangzhenghui
 * @date: 2019年03月17日-下午9:37:16
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
    /**
     * 方法描述:  数据code
     * 作    者： dangzhenghui
     * 日    期： 2019年03月17日-下午9:37:16
     *
     * @return 返回类型： String
     */
    String dicCode();

    /**
     * 方法描述:  数据Text
     * 作    者： dangzhenghui
     * 日    期： 2019年03月17日-下午9:37:16
     *
     * @return 返回类型： String
     */
    String dicText() default "";

    /**
     * 方法描述: 数据字典表
     * 作    者： dangzhenghui
     * 日    期： 2019年03月17日-下午9:37:16
     *
     * @return 返回类型： String
     */
    String dictTable() default "";


    //update-begin---author:chenrui ---date:20231221  for：[issues/#5643]解决分布式下表字典跨库无法查询问题------------
    /**
     * 方法描述: 数据字典表所在数据源名称
     * 作    者： chenrui
     * 日    期： 2023年12月20日-下午4:58
     *
     * @return 返回类型： String
     */
    String ds() default "";
    //update-end---author:chenrui ---date:20231221  for：[issues/#5643]解决分布式下表字典跨库无法查询问题------------
}
