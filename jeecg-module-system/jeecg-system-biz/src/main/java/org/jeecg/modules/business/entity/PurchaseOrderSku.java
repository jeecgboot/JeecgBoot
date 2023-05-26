package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.UnsupportedEncodingException;

/**
 * @Description: 商品采购订单SKU
 * @Author: jeecg-boot
 * @Date: 2021-04-03
 * @Version: V1.0
 */
@ApiModel(value = "purchase_order对象", description = "商品采购订单")
@Data
@TableName("purchase_order_sku")
public class PurchaseOrderSku implements Serializable {
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 商品采购订单ID
     */
    @Dict(dictTable = "purchase_order", dicText = "invoice_number", dicCode = "id")
    @ApiModelProperty(value = "商品采购订单ID")
    private String purchaseOrderId;
    /**
     * 采购SKU ID
     */
    @Excel(name = "SKU", width = 15, dictTable = "sku", dicText = "erp_code", dicCode = "id")
    @Dict(dictTable = "sku", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "采购SKU ID")
    private String skuId;
    /**
     * 采购产品件数
     */
    @Excel(name = "采购产品件数", width = 15)
    @ApiModelProperty(value = "采购产品件数")
    private Integer quantity;
    /**
     * 采购总价
     */
    @ApiModelProperty(value = "采购总价")
    private java.math.BigDecimal totalAmount;
}
