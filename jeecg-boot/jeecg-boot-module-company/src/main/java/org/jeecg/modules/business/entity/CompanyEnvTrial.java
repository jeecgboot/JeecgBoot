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
 * @Description: 环评审批信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Data
@TableName("company_env_trial")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_env_trial对象", description="环评审批信息")
public class CompanyEnvTrial implements Serializable {
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
	/**数据状态*/
	@Excel(name = "数据状态", width = 15)
    @ApiModelProperty(value = "数据状态")
    private java.lang.String status;
	/**企业id*/
	@Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
    private java.lang.String companyId;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;
	/**批复文件号*/
	@Excel(name = "批复文件号", width = 15)
    @ApiModelProperty(value = "批复文件号")
    private java.lang.String approveFilenum;
	/**审批单位*/
	@Excel(name = "审批单位", width = 15)
    @ApiModelProperty(value = "审批单位")
    private java.lang.String approveUnit;
	/**审批时间*/
	@Excel(name = "审批时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "审批时间")
    private java.util.Date approveDate;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private java.lang.String annex;
}
