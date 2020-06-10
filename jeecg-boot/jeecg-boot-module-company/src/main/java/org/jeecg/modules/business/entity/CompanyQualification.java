package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 企业资质
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Data
@TableName("company_qualification")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="company_qualification对象", description="企业资质")
public class CompanyQualification implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
	/**企业id*/
	@Excel(name = "企业id", width = 15)
    @ApiModelProperty(value = "企业id")
    private String companyId;
	/**资质类型*/
	@Excel(name = "资质类型", width = 15)
    @ApiModelProperty(value = "资质类型")
    private String type;

    /**新增的申报id*/
    @ApiModelProperty(value = "新增的申报id")
    private String applyAddId;
    /**删除的申报id*/
    @ApiModelProperty(value = "删除的申报id")
    private String applyDeleteId;

}
