package org.jeecg.modules.openapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * query部分参数表
 * @date 2024/12/10 14:37
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OpenApiParam implements Serializable {
    private static final long serialVersionUID = -6174831468578022357L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 接口ID
     */
    private String apiId;

    /**
     * key
     */
    private String paramKey;

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
