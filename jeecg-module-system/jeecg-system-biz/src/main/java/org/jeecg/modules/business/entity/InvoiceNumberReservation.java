package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: invoice number reservation
 * @Author: jeecg-boot
 * @Date:   2025-03-28
 * @Version: V1.0
 */
@Data
@TableName("invoice_number_reservation")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="invoice_number_reservation对象", description="invoice number reservation")
public class InvoiceNumberReservation implements Serializable {
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
	/**invoice type*/
	@Excel(name = "invoice type", width = 15)
    @ApiModelProperty(value = "invoice type")
    private java.lang.Integer type;
	/**invoice number*/
	@Excel(name = "invoice number", width = 15)
    @ApiModelProperty(value = "invoice number")
    private java.lang.Integer number;
	/**year*/
	@Excel(name = "year", width = 15)
    @ApiModelProperty(value = "year")
    private java.lang.String year;
	/**month*/
	@Excel(name = "month", width = 15)
    @ApiModelProperty(value = "month")
    private java.lang.String month;
}
