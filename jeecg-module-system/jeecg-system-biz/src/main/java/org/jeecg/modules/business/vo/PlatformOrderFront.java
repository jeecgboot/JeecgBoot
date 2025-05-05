package org.jeecg.modules.business.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 平台订单表
 * @Author: jeecg-boot
 * @Date:   2023-11-17
 * @Version: V1.0
 */
@Data
@ApiModel(value = "platform_order_front对象", description = "平台订单表")
public class PlatformOrderFront {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 店铺ID
     */
    @Excel(name = "店铺ID", width = 15, dictTable = "shop", dicText = "name", dicCode = "id")
    @Dict(dictTable = "shop", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "店铺ID")
    private String shopId;
    /**
     * 平台订单交易号
     */
    @Excel(name = "平台订单交易号", width = 15)
    @ApiModelProperty(value = "平台订单交易号")
    private String platformOrderNumber;
    /**
     * 平台订单号码
     */
    @Excel(name = "平台订单号码", width = 15)
    @ApiModelProperty(value = "平台订单号码")
    private String platformOrderId;
    /**
     * 物流渠道
     */
    @Excel(name = "物流渠道", width = 15, dictTable = "logistic_channel", dicText = "zh_name", dicCode = "zh_name")
    @Dict(dictTable = "logistic_channel", dicText = "zh_name", dicCode = "zh_name")
    @ApiModelProperty(value = "物流渠道")
    private String logisticChannelName;
    /**
     * 开票物流渠道名称
     */
    @Excel(name = "开票物流渠道名称", width = 15)
    @ApiModelProperty(value = "开票物流渠道名称")
    private String invoiceLogisticChannelName;
    /**
     * 物流内部单号
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
     * 订单收件人国家
     */
    @Excel(name = "订单收件人国家", width = 15)
    @ApiModelProperty(value = "订单收件人国家")
    private String country;
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
     * 采购发票号
     * */
    @Excel(name = "采购发票号", width = 15)
    @ApiModelProperty(value = "采购发票号")
    private java.lang.String purchaseInvoiceNumber;
    /**
     * ERP中状态
     */
    @Excel(name = "ERP中状态", width = 15)
    @ApiModelProperty(value = "ERP中状态")
    private String erpStatus;
    /**
     * 待审核订单（1=正常订单，2=异常订单）
     */
    @Excel(name = "待审核订单（1=正常订单，2=异常订单）", width = 15)
    @ApiModelProperty(value = "待审核订单（1=正常订单，2=异常订单）")
    private java.lang.String canSend;
    /**
     * 有货（1=有，0=没有）
     */
    @Excel(name = "有货（1=有，0=没有）", width = 15)
    @ApiModelProperty(value = "有货（1=有，0=没有）")
    private String productAvailable;
    /**
     * 可开物流票（0=不可，1=可）
     */
    @Excel(name = "可开物流票（0=不可，1=可）", width = 15)
    @ApiModelProperty(value = "可开物流票（0=不可，1=可）")
    private String shippingAvailable;
    /**
     * 可开采购票（0=不可，1=可）
     */
    @Excel(name = "可开采购票（0=不可，1=可）", width = 15)
    @ApiModelProperty(value = "可开采购票（0=不可，1=可）")
    private String purchaseAvailable;
    /**
     * 不一致的产品（1=有，0=没有）
     */
    @Excel(name = "不一致的产品（1=有，0=没有）", width = 15)
    @ApiModelProperty(value = "不一致的产品（1=有，0=没有）")
    private Integer hasDesyncedSku;

    private Integer totalCount;

    public enum invoiceStatus {
        Unavailable("-1"),
        Available("0"),
        Invoiced("1"),
        Paid("2");

        public final String code;

        invoiceStatus(String code) {
            this.code = code;
        }
    }
    public enum productStatus {
        Unavailable("0"),
        Available("1"),
        Ordered("2");

        public final String code;

        productStatus(String code) {
            this.code = code;
        }
    }
}
