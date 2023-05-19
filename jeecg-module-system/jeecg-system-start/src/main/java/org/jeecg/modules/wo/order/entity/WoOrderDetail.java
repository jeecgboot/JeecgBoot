package org.jeecg.modules.wo.order.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 订单明细
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("wo_order_detail")
@ApiModel(value="wo_order对象", description="订单管理")
public class WoOrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id")
	private Integer id;
	/**订单标识*/
	@ApiModelProperty(value = "订单标识")
	private Integer orderId;
	/**菜品标识*/
    @Excel(name = "菜品标识", width = 15)
	@ApiModelProperty(value = "菜品标识")
	@Dict(dictTable = "wo_menu", dicCode = "id", dicText = "name")
	private Integer menuId;
	/**数量*/
    @Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private Integer num;
	/**口味*/
    @Excel(name = "口味", width = 15)
	@ApiModelProperty(value = "口味")
	private String taste;
}
