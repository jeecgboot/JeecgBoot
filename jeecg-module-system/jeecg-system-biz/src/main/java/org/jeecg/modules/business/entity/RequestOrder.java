package org.jeecg.modules.order.entity;

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
 * @Description: Request Order
 * @Author: jeecg-boot
 * @Date:   2021-05-05
 * @Version: V1.0
 */
@ApiModel(value="request_order对象", description="Request Order")
@Data
@TableName("request_order")
public class RequestOrder implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**product name*/
	@Excel(name = "product name", width = 15)
    @ApiModelProperty(value = "product name")
    private java.lang.String name;
	/**client name*/
    @ApiModelProperty(value = "client name")
    private java.lang.String createBy;
	/**create time*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "create time")
    private java.util.Date createTime;
	/**status*/
	@Excel(name = "status", width = 15)
    @ApiModelProperty(value = "status")
    private java.lang.Integer status;
	/**url 1*/
	@Excel(name = "url 1", width = 15)
    @ApiModelProperty(value = "url 1")
    private java.lang.String url1;
	/**url 2*/
	@Excel(name = "url 2", width = 15)
    @ApiModelProperty(value = "url 2")
    private java.lang.String url2;
	/**url 3*/
	@Excel(name = "url 3", width = 15)
    @ApiModelProperty(value = "url 3")
    private java.lang.String url3;
	/**image*/
	@Excel(name = "image", width = 15)
    @ApiModelProperty(value = "image")
    private java.lang.String image;
	/**description*/
	@Excel(name = "description", width = 15)
    @ApiModelProperty(value = "description")
    private java.lang.String description;
	/**quantity*/
	@Excel(name = "quantity", width = 15)
    @ApiModelProperty(value = "quantity")
    private java.lang.Integer quantity;
	/**unit price*/
	@Excel(name = "unit price", width = 15)
    @ApiModelProperty(value = "unit price")
    private java.lang.Double unitPrice;
	/**unit shipfee*/
	@Excel(name = "unit shipfee", width = 15)
    @ApiModelProperty(value = "unit shipfee")
    private java.lang.Double unitShipfee;
	/**destination country*/
	@Excel(name = "destination country", width = 15)
    @ApiModelProperty(value = "destination country")
    private java.lang.String destination;
	/**comment*/
	@Excel(name = "comment", width = 15)
    @ApiModelProperty(value = "comment")
    private java.lang.String comment;
	/**file 1*/
	@Excel(name = "file 1", width = 15)
    @ApiModelProperty(value = "file 1")
    private java.lang.String file1;
	/**file 2*/
	@Excel(name = "file 2", width = 15)
    @ApiModelProperty(value = "file 2")
    private java.lang.String file2;
	/**file 3*/
	@Excel(name = "file 3", width = 15)
    @ApiModelProperty(value = "file 3")
    private java.lang.String file3;
}
