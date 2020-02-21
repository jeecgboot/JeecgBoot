package org.jeecg.modules.persona.czf.entity;

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
 * @Description: 出租房
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Data
@TableName("zz_czf")
public class ZzCzf implements Serializable {
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
	/**房屋编号*/
	@Excel(name = "房屋编号", width = 15)
    @ApiModelProperty(value = "房屋编号")
    private java.lang.String fwCode;
	/**房屋地址*/
	@Excel(name = "房屋地址", width = 15)
    @ApiModelProperty(value = "房屋地址")
    private java.lang.String fwAddr;
	/**建筑用途*/
	@Excel(name = "建筑用途", width = 15)
    @ApiModelProperty(value = "建筑用途")
    private java.lang.String jzyt;
	/**建筑面积*/
	@Excel(name = "建筑面积", width = 15)
    @ApiModelProperty(value = "建筑面积")
    private java.lang.String jzmj;
	/**房主姓名*/
	@Excel(name = "房主姓名", width = 15)
    @ApiModelProperty(value = "房主姓名")
    private java.lang.String fzName;
	/**证件类型*/
	@Excel(name = "证件类型", width = 15)
    @ApiModelProperty(value = "证件类型")
    private java.lang.String zjlx;
	/**证件号*/
	@Excel(name = "证件号", width = 15)
    @ApiModelProperty(value = "证件号")
    private java.lang.String zjh;
	/**房主联系方式*/
	@Excel(name = "房主联系方式", width = 15)
    @ApiModelProperty(value = "房主联系方式")
    private java.lang.String fzLxfs;
	/**房主现住详址*/
	@Excel(name = "房主现住详址", width = 15)
    @ApiModelProperty(value = "房主现住详址")
    private java.lang.String fzAddr;
	/**出租用途*/
	@Excel(name = "出租用途", width = 15)
    @ApiModelProperty(value = "出租用途")
    private java.lang.String czyt;
	/**隐患类型*/
	@Excel(name = "隐患类型", width = 15)
    @ApiModelProperty(value = "隐患类型")
    private java.lang.String yhlx;
	/**承租人姓名*/
	@Excel(name = "承租人姓名", width = 15)
    @ApiModelProperty(value = "承租人姓名")
    private java.lang.String czrName;
	/**承租人身份证*/
	@Excel(name = "承租人身份证", width = 15)
    @ApiModelProperty(value = "承租人身份证")
    private java.lang.String czrSfz;
	/**承租人联系方式*/
	@Excel(name = "承租人联系方式", width = 15)
    @ApiModelProperty(value = "承租人联系方式")
    private java.lang.String czrLxfs;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
}
