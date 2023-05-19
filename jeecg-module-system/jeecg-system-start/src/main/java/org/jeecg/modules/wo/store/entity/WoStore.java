package org.jeecg.modules.wo.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 点餐门店
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("wo_store")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wo_store对象", description="点餐门店")
public class WoStore {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**门店名称*/
	@Excel(name = "门店名称", width = 15)
    @ApiModelProperty(value = "门店名称")
	private String name;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话")
	private String telephone;
	/**门店地址*/
	@Excel(name = "门店地址", width = 15)
    @ApiModelProperty(value = "门店地址")
	private String address;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
	private Double lon;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
	private Double lat;
	/**桌数*/
	@Excel(name = "桌数", width = 15)
    @ApiModelProperty(value = "桌数")
	private Integer tableCount;
	/**是否营业*/
	@Excel(name = "是否营业", width = 15)
    @ApiModelProperty(value = "是否营业")
	@Dict(dicCode = "yn")
	private Integer status;
	/**收款账号*/
	@Excel(name = "收款账号", width = 15)
    @ApiModelProperty(value = "收款账号")
	private String wechatAccount;
}
