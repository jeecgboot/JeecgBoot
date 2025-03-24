package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 物流发票
 * @Author: jeecg-boot
 * @Date: 2022-12-20
 * @Version: V1.0
 */
@ApiModel(value = "shipping_invoice对象", description = "物流发票")
@Data
@TableName("shipping_invoice")
public class ShippingInvoice implements Serializable {
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
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 客户 ID
     */
    @Dict(dictTable = "client", dicText = "internal_code", dicCode = "id")
    @Excel(name = "客户", width = 15)
    @ApiModelProperty(value = "客户")
    private String clientId;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
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
    private java.math.BigDecimal totalAmount;
    /**
     * 减免金额
     */
    @Excel(name = "减免金额", width = 15)
    @ApiModelProperty(value = "减免金额")
    private java.math.BigDecimal discountAmount;
    /**
     * 最终金额
     */
    @Excel(name = "最终金额", width = 15)
    @ApiModelProperty(value = "最终金额")
    private java.math.BigDecimal finalAmount;
    /**
     * 已付金额
     */
    @Excel(name = "已付金额", width = 15)
    @ApiModelProperty(value = "已付金额")
    private java.math.BigDecimal paidAmount;
    /**
     * currency ID
     * */
    @Dict(dictTable = "currency", dicText = "code", dicCode = "id")
    @Excel(name = "currencyID", width = 15)
    @ApiModelProperty(value = "currency ID")
    private java.lang.String currencyId;
    /**
     * status
     * 0: cancelled, 1: normal (default)
     */
    @Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.Integer status;

    public ShippingInvoice() {
//        this.id = null;
//        this.createBy = null;
//        this.createTime = new Date();
//        this.updateBy = null;
//        this.updateTime = new Date();
//        this.invoiceNumber = null;
//        this.totalAmount = null;
//        this.discountAmount = null;
//        this.finalAmount = null;
//        this.paidAmount = null;
    }
    public ShippingInvoice(String id,
                                 String createBy,
                                 Date createTime,
                                 String updateBy,
                                 Date updateTime,
                                 String clientId,
                                 String invoiceNumber,
                                 BigDecimal totalAmount,
                                 BigDecimal discountAmount,
                                 BigDecimal finalAmount,
                                 BigDecimal paidAmount,
                           String currencyId) {
        this.id = id;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
        this.clientId = clientId;
        this.invoiceNumber = invoiceNumber;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.paidAmount = paidAmount;
        this.currencyId = currencyId;
    }
    public static ShippingInvoice of(
            String username,
            String clientId,
            String invoiceNumber,
            BigDecimal totalAmount,
            BigDecimal discountAmount,
            BigDecimal paidAmount,
            String currencyId
    ) {
        return new ShippingInvoice(null, username, new Date(), username, new Date(), clientId,
                invoiceNumber, totalAmount, discountAmount, totalAmount.subtract(discountAmount), paidAmount, currencyId);
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
}
