package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
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
public class ShippingInvoiceEntity implements Serializable {
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

    @PersistenceConstructor
    public ShippingInvoiceEntity(String id,
                           String createBy,
                           Date createTime,
                           String updateBy,
                           Date updateTime,
                           String invoiceNumber,
                           BigDecimal totalAmount,
                           BigDecimal discountAmount,
                           BigDecimal finalAmount,
                           BigDecimal paidAmount) {
        this.id = id;
        this.createBy = createBy;
        this.createTime = createTime;
        this.updateBy = updateBy;
        this.updateTime = updateTime;
        this.invoiceNumber = invoiceNumber;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.paidAmount = paidAmount;
    }

    public static ShippingInvoiceEntity of(
            String username,
            String invoiceNumber,
            BigDecimal totalAmount,
            BigDecimal discountAmount,
            BigDecimal paidAmount
    ) {
        return new ShippingInvoiceEntity(null, username, new Date(), username, new Date(),
                invoiceNumber, totalAmount, discountAmount, totalAmount.subtract(discountAmount), paidAmount);
    }
}
