package org.jeecg.modules.fwxx.fwxx.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 房屋信息
 * @Author: jeecg-boot
 * @Date:   2020-02-20
 * @Version: V1.0
 */
@Data
@TableName("zz_fwxx")
public class ZzFwxx implements Serializable {
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
	/**房主*/
	@Excel(name = "房主", width = 15)
    @ApiModelProperty(value = "房主")
    private java.lang.String fz;
	/**身份证号*/
	@Excel(name = "身份证号", width = 15)
    @ApiModelProperty(value = "身份证号")
    private java.lang.String sfzh;
	/**房屋编号*/
	@Excel(name = "房屋编号", width = 15)
    @ApiModelProperty(value = "房屋编号")
    private java.lang.String fwbh;
	/**房屋类别*/
	@Excel(name = "房屋类别", width = 15)
    @ApiModelProperty(value = "房屋类别")
    private java.lang.String fwlb;
	/**房屋性质*/
	@Excel(name = "房屋性质", width = 15)
    @ApiModelProperty(value = "房屋性质")
    private java.lang.String fwxz;
	/**房屋用途*/
	@Excel(name = "房屋用途", width = 15)
    @ApiModelProperty(value = "房屋用途")
    private java.lang.String fwyt;
	/**使用形式*/
	@Excel(name = "使用形式", width = 15)
    @ApiModelProperty(value = "使用形式")
    private java.lang.String syxs;
	/**室内面积(m2)*/
	@Excel(name = "室内面积(m2)", width = 15)
    @ApiModelProperty(value = "室内面积(m2)")
    private java.lang.Integer snmj;
}
