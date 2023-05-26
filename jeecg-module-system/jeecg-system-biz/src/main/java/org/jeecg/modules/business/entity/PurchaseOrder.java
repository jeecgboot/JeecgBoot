package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 客户ID
     */
    @Excel(name = "客户ID", width = 15, dictTable = "client", dicText = "internal_code", dicCode = "id")
    @Dict(dictTable = "client", dicText = "internal_code", dicCode = "id")
    @ApiModelProperty(value = "客户ID")
    private String clientId;
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
     * 订单发票号
     */
    @Excel(name = "订单发票号", width = 15)
    @ApiModelProperty(value = "订单发票号")
    private String invoiceNumber;

    /**
     * Purchase status
     */
    private String status;

    /**
     * Payment document
     */
    @ApiModelProperty(value = "Payment document")
    private String paymentDocument;
}
