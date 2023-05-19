package org.jeecg.modules.wo.order.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;

/**
 * @Description: 订单管理
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("wo_order")
@JsonIgnoreProperties(value = {"details"})
@ApiModel(value="wo_order对象", description="订单管理")
public class WoOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id")
	private Integer id;
	/**用户标识*/
	@ApiModelProperty(value = "用户标识")
	@Dict(dictTable = "wo_member", dicCode = "id", dicText = "name")
	private Integer memberId;
	/**门店标识*/
	@ApiModelProperty(value = "门店标识")
	@Dict(dictTable = "wo_store", dicCode = "id", dicText = "name")
	private Integer storeId;
	/**订单编号*/
	@ApiModelProperty(value = "订单编号")
	private String orderSeq;
	/**桌号*/
	@ApiModelProperty(value = "桌号")
	private Integer tableNum;
	/**支付金额*/
	@ApiModelProperty(value = "支付金额")
	private Double paymentAmount;
	/**订单状态: -2 已退单 -1 支付失败 0 未支付 1 已支付*/
	@ApiModelProperty(value = "订单状态")
	@Dict(dicCode = "order_status")
	private Integer status;


	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;


	/**支付时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "支付时间")
	private Date payTime;


	public synchronized String generateSeq() {
		synchronized (this.memberId) {
			return UUID.randomUUID().toString();
		}
	}

	@TableField(exist = false)
	private List<WoOrderDetail> details;
}
