package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("quotation")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Quotation", description = "Quote record")
public class Quotation implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "Created by")
    private String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "Created time")
    private Date createTime;

    @ApiModelProperty(value = "Updated by")
    private String updateBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "Updated time")
    private Date updateTime;

    @ApiModelProperty(value = "Org code")
    private String sysOrgCode;

    @Excel(name = "Status", width = 15)
    @ApiModelProperty(value = "Quote status: 0=in progress, 1=completed")
    private String status;

    @Excel(name = "Product Name", width = 15)
    @ApiModelProperty(value = "Product name")
    private String productName;

    @Excel(name = "Supplier SKU", width = 15)
    @ApiModelProperty(value = "Supplier SKU")
    private String supplierSku;

    @Excel(name = "MOQ", width = 15)
    @ApiModelProperty(value = "MOQ")
    private String moq;

    @Excel(name = "Photo", width = 15)
    @ApiModelProperty(value = "Photo")
    private String photo;

    @Excel(name = "Customer Link", width = 15)
    @ApiModelProperty(value = "Customer link")
    private String customerUrl;

    @Excel(name = "Customer Price EUR", width = 15)
    @ApiModelProperty(value = "Customer price EUR")
    private BigDecimal customerPrice;

    @Excel(name = "Country", width = 15, dictTable = "country", dicText = "name_en", dicCode = "id")
    @Dict(dictTable = "country", dicText = "name_en", dicCode = "id")
    @ApiModelProperty(value = "Country")
    private String country;

    @Excel(name = "Category", width = 15)
    @ApiModelProperty(value = "Category")
    private String category;

    @Excel(name = "Declared Value", width = 15)
    @ApiModelProperty(value = "Declared value")
    private BigDecimal declaredValue;

    @Excel(name = "IOSS Rate", width = 15)
    @ApiModelProperty(value = "IOSS rate")
    private BigDecimal iossRate;

    @Excel(name = "IOSS Fee", width = 15)
    @ApiModelProperty(value = "IOSS fee, computed from declared value and rate")
    @TableField(exist = false)
    private BigDecimal iossFee;

    @Excel(name = "Logistic Channel", width = 15)
    @Dict(dictTable = "logistic_channel", dicText = "zh_name", dicCode = "id")
    @ApiModelProperty(value = "Logistic channel")
    private String logisticChannel;

    @Excel(name = "Delivery Time", width = 15)
    @ApiModelProperty(value = "Delivery time")
    private String livraison;

    @Excel(name = "Prix d'achat", width = 15)
    @ApiModelProperty(value = "Prix d'achat")
    @TableField(exist = false)
    private BigDecimal prixAchat;

    @Excel(name = "Logistics Fee EUR", width = 15)
    @ApiModelProperty(value = "Logistics fee EUR")
    @TableField(exist = false)
    private BigDecimal logisticsFee;

    @Excel(name = "Total Fee EUR", width = 15)
    @ApiModelProperty(value = "Total fee EUR")
    @TableField(exist = false)
    private BigDecimal totalFee;

    @Excel(name = "Purchase Price RMB", width = 15)
    @ApiModelProperty(value = "Purchase price RMB")
    private BigDecimal purchasePriceRmb;

    @Excel(name = "Size Range", width = 15)
    @ApiModelProperty(value = "Size range")
    private String sizeRange;

    @Excel(name = "Domestic Shipping RMB", width = 15)
    @ApiModelProperty(value = "Domestic shipping RMB")
    private BigDecimal domesticShippingRmb;

    @Excel(name = "Cost RMB", width = 15)
    @ApiModelProperty(value = "Cost RMB")
    @TableField(exist = false)
    private BigDecimal costRmb;

    @Excel(name = "Cost EUR", width = 15)
    @ApiModelProperty(value = "Cost EUR")
    @TableField(exist = false)
    private BigDecimal costEur;

    @Excel(name = "Sale Price RMB", width = 15)
    @ApiModelProperty(value = "Sale price RMB")
    private BigDecimal salePriceRmb;

    @Excel(name = "Sale Price EUR", width = 15)
    @ApiModelProperty(value = "Sale price EUR")
    @TableField(exist = false)
    private BigDecimal salePriceEur;

    @Excel(name = "Partner Sale Price", width = 15)
    @ApiModelProperty(value = "Partner sale price")
    private Integer partnerSalePrice;

    @Excel(name = "Profit RMB", width = 15)
    @ApiModelProperty(value = "Profit RMB")
    @TableField(exist = false)
    private BigDecimal profitRmb;

    @Excel(name = "Profit EUR", width = 15)
    @ApiModelProperty(value = "Profit EUR")
    @TableField(exist = false)
    private BigDecimal profitEur;

    @Excel(name = "Margin", width = 15)
    @ApiModelProperty(value = "Margin")
    @TableField(exist = false)
    private BigDecimal margin;

    @Excel(name = "Express Weight g", width = 15)
    @ApiModelProperty(value = "Express weight g")
    @TableField(exist = false)
    private Integer expressWeightG;

    @Excel(name = "Gross Weight g", width = 15)
    @ApiModelProperty(value = "Gross weight g")
    private Integer grossWeightG;

    @Excel(name = "Pack Weight g", width = 15)
    @ApiModelProperty(value = "Pack weight g")
    private String packWeightG;

    @Excel(name = "Sales Remark", width = 15)
    @ApiModelProperty(value = "Sales remark")
    private String salesRemark;

    @Excel(name = "Supplier Link", width = 15)
    @ApiModelProperty(value = "Supplier link")
    private String supplierLink;

    @Excel(name = "Supplier Name", width = 15)
    @ApiModelProperty(value = "Supplier name")
    private String supplierName;

    @Excel(name = "Supplier Price RMB", width = 15)
    @ApiModelProperty(value = "Supplier price RMB")
    private String supplierPrice;

    @Excel(name = "In Stock", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "In stock")
    private Integer inStock;

    @Excel(name = "Product Size", width = 15)
    @ApiModelProperty(value = "Product size")
    private String productSize;

    @Excel(name = "Product Size Image", width = 15)
    @ApiModelProperty(value = "Product size image")
    private String productSizeImg;

    @Excel(name = "Volumetric", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "Volumetric")
    private BigDecimal isVolumetric;

    @Excel(name = "Package Length", width = 15)
    @ApiModelProperty(value = "Package length")
    private BigDecimal packageLength;

    @Excel(name = "Package Width", width = 15)
    @ApiModelProperty(value = "Package width")
    private BigDecimal packageWidth;

    @Excel(name = "Package Height", width = 15)
    @ApiModelProperty(value = "Package height")
    private BigDecimal packageHeight;

    @Excel(name = "Specification", width = 15)
    @ApiModelProperty(value = "Specification")
    private String specification;

    @Excel(name = "Color Note", width = 15)
    @ApiModelProperty(value = "Color note")
    private String colorNote;

    @Excel(name = "Color Image", width = 15)
    @ApiModelProperty(value = "Color image")
    private String colorImg;

    @Excel(name = "Outer Package Image", width = 15)
    @ApiModelProperty(value = "Outer package image")
    private String outerPackageImg;

    @Excel(name = "Inquiry ID", width = 15)
    @ApiModelProperty(value = "Related inquiry ID, null for direct quote")
    private String inquiryId;

    @TableField(exist = false)
    @ApiModelProperty(value = "Inquiry client ID for filtering")
    private String inquiryClient;

    @TableField(exist = false)
    @ApiModelProperty(value = "Inquiry sales ID for filtering")
    private String inquirySales;

    @TableField(exist = false)
    @ApiModelProperty(value = "Inquiry country ID for filtering")
    private String inquiryCountry;

    @Excel(name = "Priority Mode", width = 15, dicCode = "priority_mode")
    @Dict(dicCode = "priority_mode")
    @ApiModelProperty(value = "Priority mode")
    @TableField(value = "prioritymode")
    private String priorityMode;
}
