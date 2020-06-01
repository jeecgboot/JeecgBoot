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
 * @Description: 行政处罚信息
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Data
@TableName("company_admin_penalties")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_admin_penalties对象", description="行政处罚信息")
public class CompanyAdminPenalties implements Serializable {
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
	/**发文日期*/
	@Excel(name = "发文日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发文日期")
    private java.util.Date reportDate;
	/**文件名称*/
	@Excel(name = "文件名称", width = 15)
    @ApiModelProperty(value = "文件名称")
    private java.lang.String documentName;
	/**文件编号*/
	@Excel(name = "文件编号", width = 15)
    @ApiModelProperty(value = "文件编号")
    private java.lang.String documentNo;
	/**文件上传*/
	@Excel(name = "文件上传", width = 15)
    @ApiModelProperty(value = "文件上传")
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
