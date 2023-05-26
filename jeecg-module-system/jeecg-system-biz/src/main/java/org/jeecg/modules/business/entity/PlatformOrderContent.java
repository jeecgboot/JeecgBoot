package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @Description: 平台订单内容
 * @Author: jeecg-boot
 * @Date: 2023-02-09
 * @Version: V1.3
 */
@ApiModel(value = "platform_order对象", description = "平台订单表")
@Data
@TableName("platform_order_content")
public class PlatformOrderContent implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 平台订单ID
     */
    @Excel(name = "平台订单ID", width = 15, dictTable = "platform_order", dicText = "platform_order_id", dicCode = "id")
    @Dict(dictTable = "platform_order", dicText = "platform_order_id", dicCode = "id")
    @ApiModelProperty(value = "平台订单ID")
    private String platformOrderId;
    /**
     * SKU ID
     */
    @Excel(name = "SKU ID", width = 15, dictTable = "sku", dicText = "erp_code", dicCode = "id")
    @Dict(dictTable = "sku", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "SKU ID")
    private String skuId;
    /**
     * SKU数量
     */
    @Excel(name = "SKU数量", width = 15)
    @ApiModelProperty(value = "SKU数量")
    private Integer quantity;
    /**
     * 商品采购总费用
     */
    @Excel(name = "商品采购总费用", width = 15)
    @ApiModelProperty(value = "商品采购总费用")
    private java.math.BigDecimal purchaseFee;
    /**
     * 物流总费用
     */
    @Excel(name = "物流总费用", width = 15)
    @ApiModelProperty(value = "物流总费用")
    private java.math.BigDecimal shippingFee;
    /**
     * 服务总费用
     */
    @Excel(name = "服务总费用", width = 15)
    @ApiModelProperty(value = "服务总费用")
    private java.math.BigDecimal serviceFee;
    /**
     * 增值税
     */
    @Excel(name = "增值税", width = 15)
    @ApiModelProperty(value = "增值税")
    private java.math.BigDecimal vat;
    /**
     * SKU采购状态
     */
    @ApiModelProperty(value = "SKU 状态")
    @Excel(name = "SKU采购状态", width = 15, dictTable = "sku_status", dicText = "status_text", dicCode = "status_code")
    @Dict(dictTable = "sku_status", dicText = "status_text", dicCode = "status_code")
    private Integer status;
    /**
     * ERP中状态
     */
    @Excel(name = "ERP中状态", width = 15)
    @ApiModelProperty(value = "ERP中状态")
    private String erpStatus;

    /**
     * 有货（1=有，0=没有）
     */
    @Excel(name = "有货（1=有，0=没有）", width = 15)
    @ApiModelProperty(value = "有货（1=有，0=没有）")
    private String productAvailable;
    /**
     * 海外仓操作费
     */
    @Excel(name = "海外仓操作费", width = 15)
    @ApiModelProperty(value = "海外仓操作费")
    private java.math.BigDecimal pickingFee;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlatformOrderContent content = (PlatformOrderContent) o;
        return id.equals(content.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

 }
