package org.jeecg.modules.system.aspect.model;

import lombok.Data;
import org.jeecg.common.aspect.annotation.DictField;

import java.lang.reflect.Field;

/**
 * 实体信息
 *
 * @author zhou
 * @date 2019-11-26 12:41
 * @since JDK1.8
 */
@Data
public class FieldInfo {
    private static final long serialVersionUID = 2524981871601153757L;
    /**
     * 实体属性名称
     */
    private String filedName;

    /**
     * 属性信息系
     */
    private Field field;

    /**
     * 实体上注解
     */
    private DictField dictField;


    /**
     * 待翻译字段类型
     */
    private Class<?> fileTypeClass;

    /**
     * 类型
     */
    private int fileType;

}
