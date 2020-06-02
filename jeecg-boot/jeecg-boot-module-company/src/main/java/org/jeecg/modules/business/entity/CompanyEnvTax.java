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
 * @Description: 环保税信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Data
@TableName("company_env_tax")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_env_tax对象", description="环保税信息")
public class CompanyEnvTax implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**数据状态*/
	@Excel(name = "数据状态", width = 15)
    @ApiModelProperty(value = "数据状态")
    private String status;
	/**企业id*/
	@Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
    private String companyId;
	/**许可证编号*/
	@Excel(name = "许可证编号", width = 15)
    @ApiModelProperty(value = "许可证编号")
    private String licenceCode;
	/**排放口大类*/
	@Excel(name = "排放口大类", width = 15, dicCode = "ventcategory")
	@Dict(dicCode = "ventcategory")
    @ApiModelProperty(value = "排放口大类")
    private String ventCategory;
	/**排放口编号*/
	@Excel(name = "排放口编号", width = 15)
    @ApiModelProperty(value = "排放口编号")
    private String ventCode;
	/**排放口名称*/
	@Excel(name = "排放口名称", width = 15)
    @ApiModelProperty(value = "排放口名称")
    private String ventName;
	/**排污口税源编号*/
	@Excel(name = "排污口税源编号", width = 15)
    @ApiModelProperty(value = "排污口税源编号")
    private String taxsourceCode;
	/**排放方式*/
	@Excel(name = "排放方式", width = 15, dicCode = "letmode")
	@Dict(dicCode = "letmode")
    @ApiModelProperty(value = "排放方式")
    private String letMode;
	/**排污口位置*/
	@Excel(name = "排污口位置", width = 15)
    @ApiModelProperty(value = "排污口位置")
    private String ventLocate;
	/**排污口经度*/
	@Excel(name = "排污口经度", width = 15)
    @ApiModelProperty(value = "排污口经度")
    private String ventLongitude;
	/**排污口纬度*/
	@Excel(name = "排污口纬度", width = 15)
    @ApiModelProperty(value = "排污口纬度")
    private String ventLatitude;
	/**水污染排污去向*/
	@Excel(name = "水污染排污去向", width = 15, dicCode = "letdirection")
	@Dict(dicCode = "letdirection")
    @ApiModelProperty(value = "水污染排污去向")
    private String letDirection;
	/**大气污染物排放口类别*/
	@Excel(name = "大气污染物排放口类别", width = 15, dicCode = "venttype")
	@Dict(dicCode = "venttype")
    @ApiModelProperty(value = "大气污染物排放口类别")
    private String ventType;
	/**主管税务科所*/
	@Excel(name = "主管税务科所", width = 15)
    @ApiModelProperty(value = "主管税务科所")
    private String taxDepartment;
}
