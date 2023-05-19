package org.jeecg.modules.wo.menu.entity;

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
 * @Description: 点餐菜品
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("wo_menu")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="wo_menu对象", description="点餐菜品")
public class WoMenu {
    
	/**id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
	private Integer id;
	/**菜品名称*/
	@Excel(name = "菜品名称", width = 15)
    @ApiModelProperty(value = "菜品名称")
	private String name;
	/**分类名称*/
	@Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称")
	private String className;
	/**口味*/
	@Excel(name = "口味", width = 15)
    @ApiModelProperty(value = "口味")
	private String taste;
	/**门店ID*/
	@Excel(name = "门店ID", width = 15)
    @ApiModelProperty(value = "门店ID")
	@Dict(dictTable = "wo_store", dicCode = "id", dicText = "name")
	private Integer storeId;
	/**菜品售价*/
	@Excel(name = "菜品售价", width = 15)
    @ApiModelProperty(value = "菜品售价")
	private Double price;
	/**是否展示: 1是0否*/
	@Excel(name = "是否展示: 1是0否", width = 15)
    @ApiModelProperty(value = "是否展示: 1是0否")
	@Dict(dicCode = "yn")
	private Integer online;
	/**图片*/
	@Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
	private String img;
}
