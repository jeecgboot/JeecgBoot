package org.jeecg.modules.auth.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@TableName("tenant_asset_field_value")
@Data
public class AssetFieldValue implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;

//    /**
//     * 租户ID
//     */
//    @Schema(description = "租户ID")
//    private Integer tenantId;

    /**
     * 资产ID
     */
    @Schema(description = "资产ID")
    private Long assetId;

    /**
     * 字段定义ID
     */
    @Schema(description = "字段定义ID")
    private Long fieldDefinitionId;

//    /**
//     * 字段Key
//     */
//    @Schema(description = "字段Key")
//    private String fieldKey;

    /**
     * 字段值
     */
    @Schema(description = "对应key的文本值")
    private String valueText;

    /**
     * 字段值
     */
    @Schema(description = "对应key的数字值")
    private Double valueNumber;

    /**
     * 字段值
     */
    @Schema(description = "对应key的日期值")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date valueDate;

    /**
     * 字段值
     */
    @Schema(description = "对应key的时间值")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm:ss")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date valueTime;

    /**
     * 字段值
     */
    @Schema(description = "对应key的日期时间值")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date valueDatetime;

    /**
     * 字段值
     */
    @Schema(description = "对应key的布尔值")
    private Boolean valueBoolean;

    /**
     * 字段值
     */
    @Schema(description = "对应key的JSON值")
    private String valueJson;

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