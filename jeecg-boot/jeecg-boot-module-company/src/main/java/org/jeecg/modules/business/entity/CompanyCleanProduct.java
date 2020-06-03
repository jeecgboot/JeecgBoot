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
 * @Description: 清洁生产
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
@Data
@TableName("company_clean_product")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_clean_product对象", description="清洁生产")
public class CompanyCleanProduct implements Serializable {
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
	/**清洁生产报告名称*/
	@Excel(name = "清洁生产报告名称", width = 15)
    @ApiModelProperty(value = "清洁生产报告名称")
    private String reportName;
	/**报告时间*/
	@Excel(name = "报告时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "报告时间")
    private Date reportTime;
	/**清洁生成报告及专家意见*/
	@Excel(name = "清洁生成报告及专家意见", width = 15)
    @ApiModelProperty(value = "清洁生成报告及专家意见")
    private String opinionFiles;
	/**落实情况简要描述*/
	@Excel(name = "落实情况简要描述", width = 15)
    @ApiModelProperty(value = "落实情况简要描述")
    private String conditionDescribe;
	/**落实情况附件*/
	@Excel(name = "落实情况附件", width = 15)
    @ApiModelProperty(value = "落实情况附件")
    private String describeFiles;
}
