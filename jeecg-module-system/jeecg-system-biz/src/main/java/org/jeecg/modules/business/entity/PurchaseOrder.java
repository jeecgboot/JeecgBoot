package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
 * @Description: 商品采购订单
 * @Author: jeecg-boot
 * @Date: 2021-04-03
 * @Version: V1.0
 */
@ApiModel(value = "purchase_order对象", description = "商品采购订单")
@Data
@TableName("purchase_order")
public class PurchaseOrder implements Serializable {
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
     * 客户ID
     */
    @Excel(name = "客户ID", width = 15, dictTable = "client", dicText = "internal_code", dicCode = "id")
    @Dict(dictTable = "client", dicText = "internal_code", dicCode = "id")
    @ApiModelProperty(value = "客户ID")
    private String clientId;
    /**
     * 货币
     */
    @Excel(name = "货币ID", width = 15, dictTable = "currency", dicText = "code", dicCode = "id")
    @Dict(dictTable = "currency", dicText = "code", dicCode = "id")
    @ApiModelProperty(value = "货币ID")
    private java.lang.String currencyId;
    /**
     * 应付金额
     */
    @Excel(name = "应付金额", width = 15)
    @ApiModelProperty(value = "应付金额")
    private java.math.BigDecimal totalAmount;
    /**
     * 减免总金额
     */
    @Excel(name = "减免总金额", width = 15)
    @ApiModelProperty(value = "减免总金额")
    private java.math.BigDecimal discountAmount;
    /**
     * 最终金额
     */
    @Excel(name = "最终金额", width = 15)
    @ApiModelProperty(value = "最终金额")
    private java.math.BigDecimal finalAmount;
    /**
     * paid amount
     */
    @Excel(name = "已付金额", width = 15)
    @ApiModelProperty(value = "已付金额")
    private java.math.BigDecimal paidAmount;
    /**
     * 订单发票号
     */
    @Excel(name = "订单发票号", width = 15)
    @ApiModelProperty(value = "订单发票号")
    private String invoiceNumber;

    /**
     * 订单是否下了单
     */
    @Excel(name = "订单是否下了单", width = 15)
    @ApiModelProperty(value = "订单是否下了单")
    private boolean ordered;

    /**
     * status
     * 0: cancelled, 1: normal (default)
     */
    @Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.Integer status;

    /**
     * Payment document
     */
    @Excel(name = "payment document", width = 15)
    private transient java.lang.String paymentDocumentString;

    private byte[] paymentDocument;

    public byte[] getPaymentDocument(){
        if(paymentDocumentString==null){
            return null;
        }
        try {
            return paymentDocumentString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPaymentDocumentString(){
        if(paymentDocument==null || paymentDocument.length==0){
            return "";
        }
        try {
            return new String(paymentDocument,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * Inventory sheet
     */
    @Excel(name = "inventory sheet", width = 15)
    private transient java.lang.String inventoryDocumentString;

    private byte[] inventoryDocument;

    public byte[] getInventoryDocument(){
        if(inventoryDocumentString==null){
            return null;
        }
        try {
            return inventoryDocumentString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getInventoryDocumentString(){
        if(inventoryDocument==null || inventoryDocument.length==0){
            return "";
        }
        try {
            return new String(inventoryDocument,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
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
