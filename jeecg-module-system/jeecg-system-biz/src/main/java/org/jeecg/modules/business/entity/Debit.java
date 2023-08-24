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
 * @Description: debit
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Data
@TableName("debit")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="debit对象", description="debit")
public class Debit implements Serializable {
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
	@Excel(name = "client_id", width = 15)
    @ApiModelProperty(value = "client_id")
    private java.lang.String clientId;
	/**amount*/
	@Excel(name = "amount", width = 15)
    @ApiModelProperty(value = "amount")
    private java.math.BigDecimal amount;
	/**currency_id*/
	@Excel(name = "currency_id", width = 15)
    @ApiModelProperty(value = "currency_id")
    private java.lang.String currencyId;
	/**invoice_number*/
	@Excel(name = "invoice_number", width = 15)
    @ApiModelProperty(value = "invoice_number")
    private java.lang.String invoiceNumber;
}
