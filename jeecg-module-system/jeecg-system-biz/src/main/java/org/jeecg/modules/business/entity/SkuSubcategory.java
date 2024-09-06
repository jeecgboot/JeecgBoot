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
 * @Description: sku erpCode sub category
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Data
@TableName("sku_subcategory")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sku_subcategory对象", description="sku erpCode sub category")
public class SkuSubcategory implements Serializable {
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
	/**zh name*/
	@Excel(name = "zh name", width = 15)
    @ApiModelProperty(value = "zh name")
    private java.lang.String nameZh;
	/**english name*/
	@Excel(name = "english name", width = 15)
    @ApiModelProperty(value = "english name")
    private java.lang.String nameEn;
	/**code*/
	@Excel(name = "code", width = 15)
    @ApiModelProperty(value = "code")
    private java.lang.String code;
	/**sku category id*/
	@Excel(name = "sku category id", width = 15, dictTable = "sku_category", dicText = "code", dicCode = "id")
	@Dict(dictTable = "sku_category", dicText = "code", dicCode = "id")
    @ApiModelProperty(value = "sku category id")
    private java.lang.String skuCategoryId;
}
