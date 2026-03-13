package org.jeecg.modules.asset.entity;

import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 资产表
 * 
 * @author: local.clk
 * @date: 2026-02-09
 */
@Data
@TableName("tenant_asset")
@Schema(description = "资产表")
public class Asset {
    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;

    /**
     * 资产名称
     */
    @Schema(description = "资产名称")
    @Excel(name = "资产名称", width = 15)
    private String name;

    /**
     * 条码
     */
    @Schema(description = "条码")
    @Excel(name = "条码", width = 15)
    private String barcode;

    /**
     * 序列号
     */
    @Schema(description = "序列号")
    @Excel(name = "序列号", width = 15)
    private String serialNumber;

    /**
     * 固定资产ID
     */
    @Schema(description = "固定资产ID")
    @Excel(name = "固定资产ID", width = 15)
    private String faId;

    /**
     * 品牌
     */
    @Schema(description = "品牌")
    @Excel(name = "品牌", width = 15)
    private String brand;

    /**
     * 分类
     */
    @Schema(description = "分类")
    @Excel(name = "分类", width = 15)
    private String category;

    /**
     * 位置
     */
    @Schema(description = "位置")
    @Excel(name = "位置", width = 15)
    private String location;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @Excel(name = "状态", width = 15)
    private String status;

//    /**
//     * 租户ID
//     */
//    @Schema(description = "租户ID")
//    @Excel(name = "租户ID", width = 15)
//    private String tenantId;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @Excel(name = "描述", width = 15)
    private String description;

    /**
     * 是否删除
     */
    @Schema(description = "是否删除")
    @Excel(name = "是否删除", width = 15)
    @TableLogic
    @JsonIgnore
    private Integer isDeleted;

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
