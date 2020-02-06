package org.jeecg.modules.ldzrz.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 综治领导责任制
 * @Author: jeecg-boot
 * @Date:   2020-02-06
 * @Version: V1.0
 */
@Data
@TableName("zz_ldzrz")
public class ZzLdzrz implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@Excel(name = "所属部门", width = 15)
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**实施地区*/
	@Excel(name = "实施地区", width = 15)
    @ApiModelProperty(value = "实施地区")
    private java.lang.String ssdq;
	/**被实施地区层级*/
	@Excel(name = "被实施地区层级", width = 15)
    @ApiModelProperty(value = "被实施地区层级")
    private java.lang.String bssdqLevel;
	/**被实施主体名称*/
	@Excel(name = "被实施主体名称", width = 15)
    @ApiModelProperty(value = "被实施主体名称")
    private java.lang.String bssztName;
	/**被实施主体层级*/
	@Excel(name = "被实施主体层级", width = 15)
    @ApiModelProperty(value = "被实施主体层级")
    private java.lang.String bssztLevel;
	/**政策种类*/
	@Excel(name = "政策种类", width = 15)
    @ApiModelProperty(value = "政策种类")
    private java.lang.String zczl;
}
