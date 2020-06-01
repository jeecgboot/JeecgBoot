package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
 * @Description: company_product_material
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Data
@TableName("company_product_material")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_product_material对象", description="company_product_material")
public class CompanyProductMaterial implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**类别*/
	@Excel(name = "类别", width = 15, dicCode = "output_type")
	@Dict(dicCode = "output_type")
    @ApiModelProperty(value = "类别")
    private java.lang.String outputType;
	/**产品名称*/
	@Excel(name = "产品名称", width = 15)
    @ApiModelProperty(value = "产品名称")
    private java.lang.String outputName;
	/**产量*/
	@Excel(name = "产量", width = 15)
    @ApiModelProperty(value = "产量")
    private java.lang.Double yield;
	/**最大储量*/
	@Excel(name = "最大储量", width = 15)
    @ApiModelProperty(value = "最大储量")
    private java.lang.String maxStore;
	/**CAS号*/
	@Excel(name = "CAS号", width = 15)
    @ApiModelProperty(value = "CAS号")
    private java.lang.String cas;
	/**储存方式*/
	@Excel(name = "储存方式", width = 15, dicCode = "store_type")
	@Dict(dicCode = "store_type")
    @ApiModelProperty(value = "储存方式")
    private java.lang.String storeType;
	/**危化品类别*/
	@Excel(name = "危化品类别", width = 15, dicCode = "hazardous_chemicals_category")
	@Dict(dicCode = "hazardous_chemicals_category")
    @ApiModelProperty(value = "危化品类别")
    private java.lang.String hazardousChemicalsCategory;
	/**主要危险性*/
	@Excel(name = "主要危险性", width = 15, dicCode = "main_risk")
	@Dict(dicCode = "main_risk")
    @ApiModelProperty(value = "主要危险性")
    private java.lang.String mainRisk;
	/**重点监管*/
	@Excel(name = "重点监管", width = 15, dicCode = "yes_or_no")
	@Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "重点监管")
    private java.lang.String supervision;
	/**剧毒*/
	@Excel(name = "剧毒", width = 15, dicCode = "yes_or_no")
	@Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "剧毒")
    private java.lang.String toxic;
	/**易制毒*/
	@Excel(name = "易制毒", width = 15, dicCode = "yes_or_no")
	@Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "易制毒")
    private java.lang.String precursorChemicals;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
	/**是否领证*/
	@Excel(name = "是否领证", width = 15, dicCode = "yes_or_no")
	@Dict(dicCode = "yes_or_no")
    @ApiModelProperty(value = "是否领证")
    private java.lang.String certified;
	/**主要原材料*/
	@Excel(name = "主要原材料", width = 15)
    @ApiModelProperty(value = "主要原材料")
    private java.lang.String rawMaterials;
	/**主要生产设备*/
	@Excel(name = "主要生产设备", width = 15)
    @ApiModelProperty(value = "主要生产设备")
    private java.lang.String proEquipment;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remake;

	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String companyId;
}
