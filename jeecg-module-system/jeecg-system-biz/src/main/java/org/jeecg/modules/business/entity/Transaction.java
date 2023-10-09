package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: transaction
 * @Author: jeecg-boot
 * @Date:   2023-09-08
 * @Version: V1.0
 */
@Data
@TableName("transaction")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="transaction对象", description="transaction")
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**type*/
	@Excel(name = "type", width = 15)
    @ApiModelProperty(value = "type")
    private java.lang.String type;
	/**client code*/
	@Excel(name = "client code", width = 15)
    @ApiModelProperty(value = "client ID")
    private java.lang.String clientId;
	/**proof*/
	@Excel(name = "proof", width = 15)
    private transient java.lang.String paymentProofString;

    private byte[] paymentProof;

    public byte[] getPaymentProof(){
        if(paymentProofString==null){
            return null;
        }
        try {
            return paymentProofString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPaymentProofString(){
        if(paymentProof==null || paymentProof.length==0){
            return "";
        }
        try {
            return new String(paymentProof,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
	/**invoice number*/
	@Excel(name = "invoice number", width = 15)
    @ApiModelProperty(value = "invoice number")
    private java.lang.String invoiceNumber;
	/**shipping fee*/
	@Excel(name = "shipping fee", width = 15)
    @ApiModelProperty(value = "shippingFee")
    private java.math.BigDecimal shippingFee;
	/**purchase fee*/
	@Excel(name = "purchase fee", width = 15)
    @ApiModelProperty(value = "purchaseFee")
    private java.math.BigDecimal purchaseFee;
	/**amount*/
	@Excel(name = "amount", width = 15)
    @ApiModelProperty(value = "amount")
    private java.math.BigDecimal amount;
	/**currency*/
	@Excel(name = "currency", width = 15)
    @ApiModelProperty(value = "currency")
    private java.lang.String currency;
}
