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
 * @Description: 企业年度动态监管
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
@Data
@TableName("company_dynamic_supervision")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_dynamic_supervision对象", description="企业年度动态监管")
public class CompanyDynamicSupervision implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**数据状态*/
	@Excel(name = "数据状态", width = 15, dicCode = "statue")
	@Dict(dicCode = "statue")
    @ApiModelProperty(value = "数据状态")
    private java.lang.String status;
	/**企业id*/
	@Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
    private java.lang.String companyId;
	/**申报年份*/
	@Excel(name = "申报年份", width = 15)
    @ApiModelProperty(value = "申报年份")
    private java.lang.Integer reportYear;
	/**材料类型*/
	@Excel(name = "材料类型", width = 15, dicCode = "supervision_document_type")
	@Dict(dicCode = "supervision_document_type")
    @ApiModelProperty(value = "材料类型")
    private java.lang.String documentType;
	/**材料名称*/
	@Excel(name = "材料名称", width = 15)
    @ApiModelProperty(value = "材料名称")
    private java.lang.String documentName;
	/**内容*/
	@Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;
	/**申报人*/
    @ApiModelProperty(value = "申报人")
    private java.lang.String createBy;
	/**申报时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "申报时间")
    private java.util.Date createTime;
	/**审核人*/
    @ApiModelProperty(value = "审核人")
    private java.lang.String updateBy;
	/**审核时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审核时间")
    private java.util.Date updateTime;
}
