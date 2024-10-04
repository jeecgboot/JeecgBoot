package org.jeecg.modules.business.entity;

import java.io.Serializable;

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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: SKU criteria value
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Data
@TableName("sku_criteria_value")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sku_criteria_value对象", description="SKU criteria value")
public class SkuCriteriaValue implements Serializable {
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
	/**value zh name*/
	@Excel(name = "value zh name", width = 15)
    @ApiModelProperty(value = "value zh name")
    private java.lang.String zhName;
	/**value en name*/
	@Excel(name = "value en name", width = 15)
    @ApiModelProperty(value = "value en name")
    private java.lang.String enName;
	/**value in code form*/
	@Excel(name = "value in code form", width = 15)
    @ApiModelProperty(value = "value in code form")
    private java.lang.String code;
	/**sku criteria id (#fk)*/
	@Excel(name = "sku criteria id (#fk)", width = 15, dictTable = "sku_criteria", dicText = "name_en", dicCode = "id")
	@Dict(dictTable = "sku_criteria", dicText = "name_en", dicCode = "id")
    @ApiModelProperty(value = "sku criteria id (#fk)")
    private java.lang.String skuCriteriaId;
	/**sku criteria value category*/
	@Excel(name = "sku criteria value category", width = 15, dictTable = "sku_criteria_value_category", dicText = "name", dicCode = "id")
	@Dict(dictTable = "sku_criteria_value_category", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "sku criteria value category")
    private java.lang.String category;
}
