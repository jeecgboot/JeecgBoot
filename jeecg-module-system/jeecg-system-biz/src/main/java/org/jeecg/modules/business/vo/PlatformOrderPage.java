package org.jeecg.modules.business.vo;

import java.util.List;

import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 平台订单表
 * @Author: jeecg-boot
 * @Date:   2022-12-23
 * @Version: V1.5
 */
@Data
@ApiModel(value = "platform_orderPage对象", description = "平台订单表")
public class PlatformOrderPage {

    /**
     * 主键
     */
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
     * 订单收件人国家
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
     * 物流发票号
     */
    @Excel(name = "物流发票号", width = 15, dictTable = "shipping_invoice", dicText = "invoice_number", dicCode = "id")
    @Dict(dictTable = "shipping_invoice", dicText = "invoice_number", dicCode = "id")
    @ApiModelProperty(value = "物流发票号")
    private String shippingInvoiceNumber;
    /**
     * 采购状态
     */
    @Excel(name = "采购状态", width = 15)
    @ApiModelProperty(value = "采购状态")
    private String status;
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

    @ExcelCollection(name = "平台订单内容")
    @ApiModelProperty(value = "平台订单内容")
    private List<PlatformOrderContent> platformOrderContentList;

	/**
     * 可以同步Shopify（1=可以，0=不可以）
     * */
	@Excel(name = "可以同步Shopify（1=可以，0=不可以）", width = 15)
    @ApiModelProperty(value = "可以同步Shopify（1=可以，0=不可以）")
    private String readyForShopifySync;
	/**
     * 待审核订单 1.否 2.是
     * */
	@Excel(name = "待审核订单 1.否 2.是", width = 15)
	@ApiModelProperty(value = "待审核订单 1.否 2.是")
    private String canSend;
}
