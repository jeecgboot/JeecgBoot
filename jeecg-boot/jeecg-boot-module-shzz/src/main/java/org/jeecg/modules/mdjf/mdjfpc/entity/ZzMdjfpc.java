package org.jeecg.modules.mdjf.mdjfpc.entity;

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
 * @Description: 矛盾纠纷排查化解
 * @Author: jeecg-boot
 * @Date:   2020-02-18
 * @Version: V1.0
 */
@Data
@TableName("zz_mdjfpc")
public class ZzMdjfpc implements Serializable {
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
	/**事件名称*/
	@Excel(name = "事件名称", width = 15)
    @ApiModelProperty(value = "事件名称")
    private java.lang.String eventName;
	/**发生日期	*/
	@Excel(name = "发生日期	", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发生日期	")
    private java.util.Date fsDate;
	/**发生地点*/
	@Excel(name = "发生地点", width = 15)
    @ApiModelProperty(value = "发生地点")
    private java.lang.String fsdd;
	/**事件规模*/
	@Excel(name = "事件规模", width = 15)
    @ApiModelProperty(value = "事件规模")
    private java.lang.String sjgm;
	/**事件类别*/
	@Excel(name = "事件类别", width = 15)
    @ApiModelProperty(value = "事件类别")
    private java.lang.String sjlb;
	/**涉及人数*/
	@Excel(name = "涉及人数", width = 15)
    @ApiModelProperty(value = "涉及人数")
    private java.lang.Integer sjrs;
	/**事件简述	*/
	@Excel(name = "事件简述	", width = 15)
    @ApiModelProperty(value = "事件简述	")
    private java.lang.String sjjs;
	/**涉及单位*/
	@Excel(name = "涉及单位", width = 15)
    @ApiModelProperty(value = "涉及单位")
    private java.lang.String sjdw;
	/**主要当事人证件代码*/
	@Excel(name = "主要当事人证件代码", width = 15)
    @ApiModelProperty(value = "主要当事人证件代码")
    private java.lang.String zydsrZjdm;
	/**主要当事人证件号码*/
	@Excel(name = "主要当事人证件号码", width = 15)
    @ApiModelProperty(value = "主要当事人证件号码")
    private java.lang.String zydsrZjhm;
	/**主要当事人姓名*/
	@Excel(name = "主要当事人姓名", width = 15)
    @ApiModelProperty(value = "主要当事人姓名")
    private java.lang.String zydsrName;
	/**主要当事人性别*/
	@Excel(name = "主要当事人性别", width = 15)
    @ApiModelProperty(value = "主要当事人性别")
    private java.lang.String zydsrSex;
	/**主要当事人民族*/
	@Excel(name = "主要当事人民族", width = 15)
    @ApiModelProperty(value = "主要当事人民族")
    private java.lang.String zydsrMz;
	/**主要当事人学历*/
	@Excel(name = "主要当事人学历", width = 15)
    @ApiModelProperty(value = "主要当事人学历")
    private java.lang.String zydsrXl;
	/**主要当事人人员类别*/
	@Excel(name = "主要当事人人员类别", width = 15)
    @ApiModelProperty(value = "主要当事人人员类别")
    private java.lang.String zydsrRylb;
	/**主要当事人居住详址*/
	@Excel(name = "主要当事人居住详址", width = 15)
    @ApiModelProperty(value = "主要当事人居住详址")
    private java.lang.String zydsrAdress;
	/**化解时限*/
	@Excel(name = "化解时限", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "化解时限")
    private java.util.Date hjsx;
	/**化解方式*/
	@Excel(name = "化解方式", width = 15)
    @ApiModelProperty(value = "化解方式")
    private java.lang.String hjfs;
	/**化解组织*/
	@Excel(name = "化解组织", width = 15)
    @ApiModelProperty(value = "化解组织")
    private java.lang.String hjzz;
	/**化解责任人姓名*/
	@Excel(name = "化解责任人姓名", width = 15)
    @ApiModelProperty(value = "化解责任人姓名")
    private java.lang.String hjzrrName;
	/**化解责任人联系方式*/
	@Excel(name = "化解责任人联系方式", width = 15)
    @ApiModelProperty(value = "化解责任人联系方式")
    private java.lang.String hjzrrPhone;
	/**化解是否成功*/
	@Excel(name = "化解是否成功", width = 15)
    @ApiModelProperty(value = "化解是否成功")
    private java.lang.String sfHjcg;
	/**化解情况*/
	@Excel(name = "化解情况", width = 15)
    @ApiModelProperty(value = "化解情况")
    private java.lang.String hjqk;
	/**考评日期*/
	@Excel(name = "考评日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "考评日期")
    private java.util.Date kpDate;
	/**考评意见*/
	@Excel(name = "考评意见", width = 15)
    @ApiModelProperty(value = "考评意见")
    private java.lang.String kpyj;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
}
