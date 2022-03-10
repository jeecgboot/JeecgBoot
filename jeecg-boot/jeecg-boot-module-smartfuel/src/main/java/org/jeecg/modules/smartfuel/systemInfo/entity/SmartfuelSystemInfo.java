package org.jeecg.modules.smartfuel.systemInfo.entity;

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
 * @Description: system_info
 * @Author: jeecg-boot
 * @Date:   2022-03-09
 * @Version: V1.0
 */
@Data
@TableName("smartfuel_system_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="smartfuel_system_info对象", description="system_info")
public class SmartfuelSystemInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
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
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**电厂全称*/
	@Excel(name = "电厂全称", width = 15)
    @ApiModelProperty(value = "电厂全称")
    private java.lang.String plantName;
	/**电厂简称*/
	@Excel(name = "电厂简称", width = 15)
    @ApiModelProperty(value = "电厂简称")
    private java.lang.String plantShortName;
	/**系统名称*/
	@Excel(name = "系统名称", width = 15)
    @ApiModelProperty(value = "系统名称")
    private java.lang.String systemName;
	/**集团名称*/
	@Excel(name = "集团名称", width = 15)
    @ApiModelProperty(value = "集团名称")
    private java.lang.String groupName;
	/**logo*/
	@Excel(name = "logo", width = 15)
    @ApiModelProperty(value = "logo")
    private java.lang.String logo;
}
