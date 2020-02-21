package org.jeecg.modules.persona.jwry.entity;

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
 * @Description: 境外人员
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Data
@TableName("zz_jwry")
public class ZzJwry implements Serializable {
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
	/**外文姓*/
	@Excel(name = "外文姓", width = 15)
    @ApiModelProperty(value = "外文姓")
    private java.lang.String wyx;
	/**外文名*/
	@Excel(name = "外文名", width = 15)
    @ApiModelProperty(value = "外文名")
    private java.lang.String wym;
	/**中文姓名*/
	@Excel(name = "中文姓名", width = 15)
    @ApiModelProperty(value = "中文姓名")
    private java.lang.String zwxm;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
    private java.lang.String sex;
	/**生日*/
	@Excel(name = "生日", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生日")
    private java.util.Date birthday;
	/**国籍*/
	@Excel(name = "国籍", width = 15)
    @ApiModelProperty(value = "国籍")
    private java.lang.String gj;
	/**宗教信仰*/
	@Excel(name = "宗教信仰", width = 15)
    @ApiModelProperty(value = "宗教信仰")
    private java.lang.String zjxy;
	/**证件类型*/
	@Excel(name = "证件类型", width = 15)
    @ApiModelProperty(value = "证件类型")
    private java.lang.String zjlx;
	/**证件号*/
	@Excel(name = "证件号", width = 15)
    @ApiModelProperty(value = "证件号")
    private java.lang.String zjh;
	/**证件有效期*/
	@Excel(name = "证件有效期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "证件有效期")
    private java.util.Date zjyxq;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String lxfs;
	/**来华目的*/
	@Excel(name = "来华目的", width = 15)
    @ApiModelProperty(value = "来华目的")
    private java.lang.String lhmd;
	/**职业类别*/
	@Excel(name = "职业类别", width = 15)
    @ApiModelProperty(value = "职业类别")
    private java.lang.String zylb;
	/**职业*/
	@Excel(name = "职业", width = 15)
    @ApiModelProperty(value = "职业")
    private java.lang.String zy;
	/**服务处所*/
	@Excel(name = "服务处所", width = 15)
    @ApiModelProperty(value = "服务处所")
    private java.lang.String fwcs;
	/**现住地*/
	@Excel(name = "现住地", width = 15)
    @ApiModelProperty(value = "现住地")
    private java.lang.String xzd;
	/**现住门（楼）详*/
	@Excel(name = "现住门（楼）详", width = 15)
    @ApiModelProperty(value = "现住门（楼）详")
    private java.lang.String xzm;
	/**抵达日期*/
	@Excel(name = "抵达日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "抵达日期")
    private java.util.Date ddDate;
	/**预计离开日期*/
	@Excel(name = "预计离开日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "预计离开日期")
    private java.util.Date yjlkDate;
	/**是否重点关注*/
	@Excel(name = "是否重点关注", width = 15)
    @ApiModelProperty(value = "是否重点关注")
    private java.lang.String sfZdgz;
}
