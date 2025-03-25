package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 物流发票
 * @Author: jeecg-boot
 * @Date: 2022-12-20
 * @Version: V1.0
 */
@ApiModel(value = "invoice对象", description = "发票")
@Data
@TableName("all_invoices")
public class Invoice implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
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
     * 客户 ID
     */
    @Dict(dictTable = "client", dicText = "internal_code", dicCode = "id")
    @Excel(name = "客户", width = 15)
    @ApiModelProperty(value = "客户")
    private String clientId;
    /**
     * currency ID
     * */
    @Dict(dictTable = "currency", dicText = "code", dicCode = "id")
    @Excel(name = "currencyID", width = 15)
    @ApiModelProperty(value = "currency ID")
    private String currencyId;
    /**
     * 发票号码
     */
    @Excel(name = "发票号码", width = 15)
    @ApiModelProperty(value = "发票号码")
    private String invoiceNumber;
    /**
     * 应付金额
     */
    @Excel(name = "应付金额", width = 15)
    @ApiModelProperty(value = "应付金额")
    private BigDecimal totalAmount;
    /**
     * 减免金额
     */
    @Excel(name = "减免金额", width = 15)
    @ApiModelProperty(value = "减免金额")
    private BigDecimal discountAmount;
    /**
     * 最终金额
     */
    @Excel(name = "最终金额", width = 15)
    @ApiModelProperty(value = "最终金额")
    private BigDecimal finalAmount;
    /**
     * 已付金额
     */
    @Excel(name = "已付金额", width = 15)
    @ApiModelProperty(value = "已付金额")
    private BigDecimal paidAmount;

    @Excel(name = "type", width = 15)
    @ApiModelProperty(value = "type")
    private String type;

    /**
     * status
     * 0: cancelled, 1: normal (default)
     */
    @Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.Integer status;

    @Getter
    public enum InvoiceType {
        PURCHASE(1, "purchase_invoice"),
        SHIPPING(2, "shipping_invoice"),
        COMPLETE(7, "complete_invoice"),
        CREDIT(8, "credit_invoice");

        private final int type;
        private final String text;

        InvoiceType(int type, String text) {
            this.type = type;
            this.text = text;
        }
    }

    @Getter
    public enum Status {
        Cancelled (0),
        Normal (1);

        private final int code;
        Status(int code) {
            this.code = code;
        }
    }

    public static String getType(String invoiceNumber) {
        for(InvoiceType type : InvoiceType.values()) {
            // you can get the int value of a decimal digit char by subtracting '0'
            int typeNumber = invoiceNumber.charAt(8) - '0';
            if(type.getType() == typeNumber)
                return type.name();
        }
        throw new IllegalArgumentException("Incorrect invoice number : " + invoiceNumber);
    }
    public static boolean isInvoiceNumber(String invoiceNumber) {
        return invoiceNumber.matches("^[0-9]{4}-[0-9]{2}-[127][0-9]{3}$");
    }
}