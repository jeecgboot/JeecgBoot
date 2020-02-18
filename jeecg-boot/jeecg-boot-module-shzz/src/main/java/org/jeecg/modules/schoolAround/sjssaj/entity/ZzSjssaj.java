package org.jeecg.modules.schoolAround.sjssaj.entity;

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
 * @Description: 涉及师生案件
 * @Author: jeecg-boot
 * @Date:   2020-02-18
 * @Version: V1.0
 */
@Data
@TableName("zz_sjssaj")
public class ZzSjssaj implements Serializable {
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
	/**案（事）件名*/
	@Excel(name = "案（事）件名", width = 15)
    @ApiModelProperty(value = "案（事）件名")
    private java.lang.String name;
	/**案（事）件编号*/
	@Excel(name = "案（事）件编号", width = 15)
    @ApiModelProperty(value = "案（事）件编号")
    private java.lang.String code;
	/**发生日期*/
	@Excel(name = "发生日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发生日期")
    private java.util.Date fsDate;
	/**发案地*/
	@Excel(name = "发案地", width = 15)
    @ApiModelProperty(value = "发案地")
    private java.lang.String faAddr;
	/**发生地点*/
	@Excel(name = "发生地点", width = 15)
    @ApiModelProperty(value = "发生地点")
    private java.lang.String fsAddr;
	/**案（事）件性质*/
	@Excel(name = "案（事）件性质", width = 15)
    @ApiModelProperty(value = "案（事）件性质")
    private java.lang.String ajxz;
	/**案（事）件情况*/
	@Excel(name = "案（事）件情况", width = 15)
    @ApiModelProperty(value = "案（事）件情况")
    private java.lang.String ajqk;
	/**主犯（嫌疑人）姓名*/
	@Excel(name = "主犯（嫌疑人）姓名", width = 15)
    @ApiModelProperty(value = "主犯（嫌疑人）姓名")
    private java.lang.String zfName;
	/**主犯（嫌疑人）证件类型*/
	@Excel(name = "主犯（嫌疑人）证件类型", width = 15)
    @ApiModelProperty(value = "主犯（嫌疑人）证件类型")
    private java.lang.String zfZjlx;
	/**主犯（嫌疑人）证件号*/
	@Excel(name = "主犯（嫌疑人）证件号", width = 15)
    @ApiModelProperty(value = "主犯（嫌疑人）证件号")
    private java.lang.String zfZjhm;
	/**是否破案*/
	@Excel(name = "是否破案", width = 15)
    @ApiModelProperty(value = "是否破案")
    private java.lang.String sfPa;
	/**作案人数*/
	@Excel(name = "作案人数", width = 15)
    @ApiModelProperty(value = "作案人数")
    private java.lang.Integer zaNum;
	/**在逃人数*/
	@Excel(name = "在逃人数", width = 15)
    @ApiModelProperty(value = "在逃人数")
    private java.lang.Integer ztNum;
	/**抓捕人数*/
	@Excel(name = "抓捕人数", width = 15)
    @ApiModelProperty(value = "抓捕人数")
    private java.lang.Integer zbNum;
	/**破案侦破情况*/
	@Excel(name = "破案侦破情况", width = 15)
    @ApiModelProperty(value = "破案侦破情况")
    private java.lang.String pazpqk;
}
