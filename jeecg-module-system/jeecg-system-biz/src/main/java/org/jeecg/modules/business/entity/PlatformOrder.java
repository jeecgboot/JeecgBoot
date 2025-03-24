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

/**
 * @Description: 平台订单表
 * @Author: jeecg-boot
 * @Date: 2024-06-25
 * @Version: V1.10
 */
@ApiModel(value = "platform_order对象", description = "平台订单表")
@Data
@TableName("platform_order")
public class PlatformOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status {
        Purchasing(3),
        Pending(2),
        Shipped(1);

        public final int code;

        Status(int code) {
            this.code = code;
        }
    }

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
     * 店铺ID
     */
    @Excel(name = "店铺ID", width = 15, dictTable = "shop", dicText = "erp_code", dicCode = "id")
    @Dict(dictTable = "shop", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "店铺ID")
    private String shopId;
    /**
     * 物流渠道
     */
    @Excel(name = "物流渠道", width = 15, dictTable = "logistic_channel", dicText = "zh_name", dicCode = "zh_name")
    @Dict(dictTable = "logistic_channel", dicText = "zh_name", dicCode = "zh_name")
    @ApiModelProperty(value = "物流渠道")
    private String logisticChannelName;
    /**
     * 平台订单号码
     */
    @Excel(name = "平台订单号码", width = 15)
    @ApiModelProperty(value = "平台订单号码")
    private String platformOrderId;
    /**
     * 平台订单交易号
     */
    @Excel(name = "平台订单交易号", width = 15)
    @ApiModelProperty(value = "平台订单交易号")
    private String platformOrderNumber;
    /**
     * ERP内订单ID
     */
    @Excel(name = "ERP内订单ID", width = 15)
    @ApiModelProperty(value = "ERP内订单ID")
    private String erpOrderId;
    /**
     * 物流跟踪号
     */
    @Excel(name = "物流跟踪号", width = 15)
    @ApiModelProperty(value = "物流跟踪号")
    private String trackingNumber;
    /**
     * 订单交易时间
     */
    @Excel(name = "订单交易时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单交易时间")
    private Date orderTime;
    /**
     * 订单发货时间
     */
    @Excel(name = "订单发货时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "订单发货时间")
    private Date shippingTime;
    /**
     * 订单收件人
     */
    @Excel(name = "订单收件人", width = 15)
    @ApiModelProperty(value = "订单收件人")
    private String recipient;
    /**
     * 订单收件人国家, represented by full english name
     */
    @Excel(name = "订单收件人国家", width = 15)
    @ApiModelProperty(value = "订单收件人国家")
    private String country;
    /**
     * 订单收件人邮编
     */
    @Excel(name = "订单收件人邮编", width = 15)
    @ApiModelProperty(value = "订单收件人邮编")
    private String postcode;
    /**
     * 物流挂号费
     */
    @Excel(name = "物流挂号费", width = 15)
    @ApiModelProperty(value = "物流挂号费")
    private java.math.BigDecimal fretFee;
    /**
     * 订单服务费
     */
    @Excel(name = "订单服务费", width = 15)
    @ApiModelProperty(value = "订单服务费")
    private java.math.BigDecimal orderServiceFee;
    /**
     * 物流保险费
     */
    @Excel(name = "物流保险费", width = 15)
    @ApiModelProperty(value = "物流保险费")
    private java.math.BigDecimal insuranceFee;
    /**
     * 物流发票号
     */
    @Excel(name = "物流发票号", width = 15, dictTable = "shipping_invoice", dicText = "invoice_number", dicCode = "id")
    @Dict(dictTable = "shipping_invoice", dicText = "invoice_number", dicCode = "id")
    @ApiModelProperty(value = "物流发票号")
    private String shippingInvoiceNumber;
    /**
     * 采购发票号
     * */
    @Excel(name = "采购发票号", width = 15)
    @ApiModelProperty(value = "采购发票号")
    private java.lang.String purchaseInvoiceNumber;
    /**
     * 采购状态
     */
    @Excel(name = "采购状态", width = 15, dictTable = "sku_status", dicText = "status_text", dicCode = "status_code")
    @Dict(dictTable = "sku_status", dicText = "status_text", dicCode = "status_code")
    @ApiModelProperty(value = "采购状态")
    private Integer status;
    /**
     * 合并订单目标订单ID
     */
    @Excel(name = "合并订单目标订单ID", width = 15)
    @ApiModelProperty(value = "合并订单目标订单ID")
    private String target;
    /**
     * ERP中状态
     */
    @Excel(name = "ERP中状态", width = 15)
    @ApiModelProperty(value = "ERP中状态")
    private String erpStatus;
    /**
     * 开票物流渠道名称
     */
    @Excel(name = "开票物流渠道名称", width = 15)
    @ApiModelProperty(value = "开票物流渠道名称")
    private String invoiceLogisticChannelName;
    /**
     * 物流内部单号
     */
    @Excel(name = "物流内部单号", width = 15)
    @ApiModelProperty(value = "物流内部单号")
    private String internalTrackingNumber;
    /**
     * 有货（1=有，0=没有）
     */
    @Excel(name = "有货（1=有，0=没有）", width = 15)
    @ApiModelProperty(value = "有货（1=有，0=没有）")
    private String productAvailable;
    /**
     * 有货（1=有，0=没有）
     * */
    @Excel(name = "有货（1=有，0=没有）", width = 15)
    @ApiModelProperty(value = "有货（1=有，0=没有）")
    private java.lang.String virtualProductAvailable;
    /**
     * 可以同步Shopify（1=可以，0=不可以）
     */
    @Excel(name = "可以同步Shopify（1=可以，0=不可以）", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "可以同步Shopify（1=可以，0=不可以）")
    private String readyForShopifySync;
    /**
     * 待审核订单（1=正常订单，2=异常订单）
     */
    @Excel(name = "待审核订单（1=正常订单，2=异常订单）", width = 15)
    @ApiModelProperty(value = "待审核订单（1=正常订单，2=异常订单）")
    private java.lang.String canSend;
    /**
     * 海外仓操作费
     */
    @Excel(name = "海外仓操作费", width = 15)
    @ApiModelProperty(value = "海外仓操作费")
    private java.math.BigDecimal pickingFee;
    /**
     * 包材费
     */
    @Excel(name = "包材费", width = 15)
    @ApiModelProperty(value = "包材费")
    private java.math.BigDecimal packagingMaterialFee;
    /**
     * 收件人城市
     */
    @Excel(name = "收件人城市", width = 15)
    @ApiModelProperty(value = "收件人城市")
    private java.lang.String city;
    /**
     * Shopify平台留言
     */
    @Excel(name = "Shopify平台留言", width = 15)
    @ApiModelProperty(value = "Shopify平台留言")
    private java.lang.String shopifyNote;
    /**
     * 个人税号
     */
    @Excel(name = "个人税号", width = 15)
    @ApiModelProperty(value = "个人税号")
    private java.lang.String taxNumber;
    /**
     * Shopify平台已同步(1=已同步，0=未同步)
     */
    @Excel(name = "Shopify平台已同步(1=已同步，0=未同步)", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "Shopify平台已同步(1=已同步，0=未同步)")
    private String shopifySynced;
    /**
     * 目的地转单号
     */
    @Excel(name = "目的地转单号", width = 15)
    @ApiModelProperty(value = "目的地转单号")
    private String localTrackingNumber;
}
