package org.jeecg.modules.business.vo;

import java.util.List;

import org.jeecg.modules.business.entity.Sku;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 商品
 * @Author: jeecg-boot
 * @Date:   2021-05-31
 * @Version: V1.0
 */
@Data
@ApiModel(value="productPage对象", description="商品")
public class ProductPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private String id;
	/**商品代码*/
	@Excel(name = "商品代码", width = 15)
	@ApiModelProperty(value = "商品代码")
    private String code;
	/**中文名*/
	@Excel(name = "中文名", width = 15)
	@ApiModelProperty(value = "中文名")
    private String zhName;
	/**英文名*/
	@Excel(name = "英文名", width = 15)
	@ApiModelProperty(value = "英文名")
    private String enName;
	/**敏感属性ID*/
	@Excel(name = "敏感属性ID", width = 15, dictTable = "sensitive_attribute", dicText = "zh_name", dicCode = "id")
    @Dict(dictTable = "sensitive_attribute", dicText = "zh_name", dicCode = "id")
	@ApiModelProperty(value = "敏感属性ID")
    private String sensitiveAttributeId;
	/**发票名称*/
	@Excel(name = "发票名称", width = 15)
	@ApiModelProperty(value = "发票名称")
    private String invoiceName;
	/**重量，单位为克*/
	@Excel(name = "重量，单位为克", width = 15)
	@ApiModelProperty(value = "重量，单位为克")
    private Integer weight;
	/**体积重，单位为立方厘米*/
	@Excel(name = "体积重，单位为立方厘米", width = 15)
	@ApiModelProperty(value = "体积重，单位为立方厘米")
    private Integer volume;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
    private String updateBy;
	/**最低采购数量*/
	@Excel(name = "最低采购数量", width = 15)
	@ApiModelProperty(value = "最低采购数量")
	private java.lang.Integer moq;
	@ExcelCollection(name="SKU表")
	@ApiModelProperty(value = "SKU表")
	private List<Sku> skuList;

}
