package org.jeecg.modules.wo.order.vo;

import java.util.List;

import org.jeecg.modules.wo.order.entity.WoOrderDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 订单管理
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@ApiModel(value="wo_orderPage对象", description="订单管理")
public class WoOrderPage {
	
	/**id*/
	private Integer id;
	/**用户标识*/
  	@Excel(name = "用户标识", width = 15)
	private Integer memberId;
	/**门店标识*/
  	@Excel(name = "门店标识", width = 15)
	private Integer storeId;
	/**订单编号*/
  	@Excel(name = "订单编号", width = 15)
	private String orderSeq;
	/**桌号*/
  	@Excel(name = "桌号", width = 15)
	private Integer tableNum;
	/**支付金额*/
  	@Excel(name = "支付金额", width = 15)
	private Double paymentAmount;
	/**订单状态: -2 已退单 -1 支付失败 0 未支付 1 已支付*/
  	@Excel(name = "订单状态: -2 已退单 -1 支付失败 0 未支付 1 已支付", width = 15)
	private Integer status;
	/**创建时间*/
  	@Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
  	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createTime;
	/**支付时间*/
  	@Excel(name = "支付时间", width = 15, format = "yyyy-MM-dd")
  	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date payTime;
	
	@ExcelCollection(name="订单明细")
	@ApiModelProperty(value = "订单明细")
	private List<WoOrderDetail> woOrderDetailList;
	
}
