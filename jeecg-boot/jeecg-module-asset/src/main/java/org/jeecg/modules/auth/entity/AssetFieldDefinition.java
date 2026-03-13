package org.jeecg.modules.auth.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import org.jeecg.modules.auth.config.FieldCategoryEnum;
import org.jeecg.modules.auth.config.FieldTypeEnum;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 资产字段定义表
 *
 * @author: local.clk
 * @date: 2026-02-09
 */
@Data
@TableName("tenant_asset_field_definition")
@Schema(description = "资产字段定义表")
public class AssetFieldDefinition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;

//    /**
//     * 租户ID
//     */
//    @Schema(description = "租户ID")
//    private Integer tenantId;

    /**
     * 字段Key
     */
    @Schema(title = "字段Key", description = "非必填；如果传入系统会校验是否重复")
    @Excel(name = "字段Key", width = 15)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String fieldKey;

    /**
     * 字段名称
     */
    @Schema(description = "字段名称")
    @Excel(name = "字段名称", width = 15)
    private String fieldName;

    /**
     * 字段分类
     */
    @Schema(description = "字段分类")
    @Excel(name = "字段分类", width = 15)
    private FieldCategoryEnum fieldCategory;

    /**
     * 字段类型
     */
    @Schema(description = "字段类型")
    @Excel(name = "字段类型", width = 15)
    private FieldTypeEnum fieldType;

    /**
     * 额外数据
     */
    @Schema(description = "额外数据")
    @Excel(name = "额外数据", width = 15)
    private String extraData;

    /**
     * 字段值
     */
    @Schema(description = "排序")
    @Excel(name = "排序", width = 15)
    private Integer sort;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Excel(name = "状态", width = 15)
    private Integer status;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @Excel(name = "创建人", width = 15)
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    @Excel(name = "更新人", width = 15)
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
