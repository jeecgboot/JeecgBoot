package org.jeecg.modules.order.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: Response Order
 * @Author: jeecg-boot
 * @Date:   2021-05-05
 * @Version: V1.0
 */
@ApiModel(value="request_order对象", description="Request Order")
@Data
@TableName("response_order")
public class ResponseOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**request id (foreigner key)*/
    @ApiModelProperty(value = "request id (foreigner key)")
    private java.lang.String requestId;
	/**request_quantity*/
	@Excel(name = "request_quantity", width = 15)
    @ApiModelProperty(value = "request_quantity")
    private java.lang.Integer requestQuantity;
	/**employee*/
    @ApiModelProperty(value = "employee")
    private java.lang.String createBy;
	/**create time*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "create time")
    private java.util.Date createTime;
	/**avalable time*/
	@Excel(name = "avalable time", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "avalable time")
    private java.util.Date avalable;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.Integer status;
	/**bid unit price*/
	@Excel(name = "bid unit price", width = 15)
    @ApiModelProperty(value = "bid unit price")
    private java.lang.String bidUnitPrice;
	/**bid unit shipfee*/
	@Excel(name = "bid unit shipfee", width = 15)
    @ApiModelProperty(value = "bid unit shipfee")
    private java.lang.String bidUnitShipfee;
	/**unit registration fee*/
	@Excel(name = "unit registration fee", width = 15)
    @ApiModelProperty(value = "unit registration fee")
    private java.lang.String registrationFee;
	/**source country*/
	@Excel(name = "source country", width = 15)
    @ApiModelProperty(value = "source country")
    private java.lang.String source;
	/**destination country*/
	@Excel(name = "destination country", width = 15)
    @ApiModelProperty(value = "destination country")
    private java.lang.String destination;
	/**discount (%)*/
	@Excel(name = "discount (%)", width = 15)
    @ApiModelProperty(value = "discount (%)")
    private java.lang.Integer discount;
	/**bit total price*/
	@Excel(name = "bit total price", width = 15)
    @ApiModelProperty(value = "bit total price")
    private java.lang.String bitTotalPrice;
}
