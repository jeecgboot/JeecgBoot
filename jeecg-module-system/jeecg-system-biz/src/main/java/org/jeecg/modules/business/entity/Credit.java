package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: credit
 * @Author: jeecg-boot
 * @Date:   2023-08-24
 * @Version: V1.0
 */
@Data
@TableName("credit")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="credit对象", description="credit")
public class Credit implements Serializable {
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
    /**client_id*/
    @Excel(name = "client_id", width = 15, dictTable = "client WHERE active = '1'", dicText = "internal_code", dicCode = "id")
    @Dict(dictTable = "client WHERE active = '1'", dicText = "internal_code", dicCode = "id")
    @ApiModelProperty(value = "client_id")
    private java.lang.String clientId;
    /**amount*/
    @Excel(name = "amount", width = 15)
    @ApiModelProperty(value = "amount")
    private java.math.BigDecimal amount;
    /**currency_id*/
    @Excel(name = "currency_id", width = 15, dictTable = "currency WHERE code <> 'RMB'", dicText = "code", dicCode = "id")
    @Dict(dictTable = "currency WHERE code <> 'RMB'", dicText = "code", dicCode = "id")
    @ApiModelProperty(value = "currency_id")
    private java.lang.String currencyId;
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
}
