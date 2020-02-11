package org.jeecg.modules.shza.zddqpc.entity;

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
 * @Description: 重点地区排查整治
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Data
@TableName("zz_zddqpc")
public class ZzZddqpc implements Serializable {
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
	/**重点地区名称*/
	@Excel(name = "重点地区名称", width = 15)
    @ApiModelProperty(value = "重点地区名称")
    private java.lang.String zddqName;
	/**治安突出问题*/
	@Excel(name = "治安突出问题", width = 15)
    @ApiModelProperty(value = "治安突出问题")
    private java.lang.String zatcwt;
	/**涉及区域类型*/
	@Excel(name = "涉及区域类型", width = 15)
    @ApiModelProperty(value = "涉及区域类型")
    private java.lang.String sjqyType;
	/**涉及区域范围*/
	@Excel(name = "涉及区域范围", width = 15)
    @ApiModelProperty(value = "涉及区域范围")
    private java.lang.String sjqyfw;
	/**整治牵头单位*/
	@Excel(name = "整治牵头单位", width = 15)
    @ApiModelProperty(value = "整治牵头单位")
    private java.lang.String zzqtdw;
	/**单位负责人*/
	@Excel(name = "单位负责人", width = 15)
    @ApiModelProperty(value = "单位负责人")
    private java.lang.String dwfzr;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String phone;
	/**整治参与单位*/
	@Excel(name = "整治参与单位", width = 15)
    @ApiModelProperty(value = "整治参与单位")
    private java.lang.String zzcydw;
	/**整改时限*/
	@Excel(name = "整改时限", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "整改时限")
    private java.util.Date zgsx;
	/**期间破获刑事案件数量*/
	@Excel(name = "期间破获刑事案件数量", width = 15)
    @ApiModelProperty(value = "期间破获刑事案件数量")
    private java.lang.Integer phxs;
	/**期间破获治安案件数量*/
	@Excel(name = "期间破获治安案件数量", width = 15)
    @ApiModelProperty(value = "期间破获治安案件数量")
    private java.lang.Integer phza;
	/**效果评估*/
	@Excel(name = "效果评估", width = 15)
    @ApiModelProperty(value = "效果评估")
    private java.lang.String xgpg;
	/**整治情况*/
	@Excel(name = "整治情况", width = 15)
    @ApiModelProperty(value = "整治情况")
    private java.lang.String zzqk;
}
