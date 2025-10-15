package org.jeecg.modules.openapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 请求头表
 * @date 2024/12/10 14:37
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OpenApiHeader implements Serializable {
    private static final long serialVersionUID = 5032708503120184683L;


    /**
     * key
     */
    private String headerKey;

    /**
     * 是否必填(0:否，1：是)
     */
    private Integer required;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 说明
     */
    private String note;
}
