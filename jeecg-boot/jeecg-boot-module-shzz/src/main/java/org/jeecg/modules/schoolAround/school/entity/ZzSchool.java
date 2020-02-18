package org.jeecg.modules.schoolAround.school.entity;

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
 * @Description: 学校
 * @Author: jeecg-boot
 * @Date:   2020-02-18
 * @Version: V1.0
 */
@Data
@TableName("zz_school")
public class ZzSchool implements Serializable {
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
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
	/**地址*/
	@Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private java.lang.String addr;
	/**办学类型*/
	@Excel(name = "办学类型", width = 15)
    @ApiModelProperty(value = "办学类型")
    private java.lang.String bxlx;
	/**主管部门*/
	@Excel(name = "主管部门", width = 15)
    @ApiModelProperty(value = "主管部门")
    private java.lang.String zgOrg;
	/**校长姓名*/
	@Excel(name = "校长姓名", width = 15)
    @ApiModelProperty(value = "校长姓名")
    private java.lang.String xzName;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String lxfs;
	/**学生人数*/
	@Excel(name = "学生人数", width = 15)
    @ApiModelProperty(value = "学生人数")
    private java.lang.Integer stuNum;
	/**安保人数*/
	@Excel(name = "安保人数", width = 15)
    @ApiModelProperty(value = "安保人数")
    private java.lang.Integer abNum;
	/**分管安保责任人*/
	@Excel(name = "分管安保责任人", width = 15)
    @ApiModelProperty(value = "分管安保责任人")
    private java.lang.String fgabZrr;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String fgabLxfs;
	/**安保责任人*/
	@Excel(name = "安保责任人", width = 15)
    @ApiModelProperty(value = "安保责任人")
    private java.lang.String abZrr;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String abLxfs;
	/**治安责任人*/
	@Excel(name = "治安责任人", width = 15)
    @ApiModelProperty(value = "治安责任人")
    private java.lang.String zaZrr;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String zaLxfs;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
}
