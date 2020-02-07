package org.jeecg.modules.tsrq.xdry.entity;

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
 * @Description: 吸毒人员
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Data
@TableName("zz_xdry")
public class ZzXdry implements Serializable {
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
	/**姓名*/
	@Excel(name = "姓名", width = 15)
    @ApiModelProperty(value = "姓名")
    private java.lang.String name;
	/**身份证*/
	@Excel(name = "身份证", width = 15)
    @ApiModelProperty(value = "身份证")
    private java.lang.String sfz;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @ApiModelProperty(value = "性别")
    private java.lang.String sex;
	/**手机号*/
	@Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private java.lang.String phone;
	/**初次发现日期*/
	@Excel(name = "初次发现日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "初次发现日期")
    private java.util.Date ccfxDate;
	/**管控情况*/
	@Excel(name = "管控情况", width = 15)
    @ApiModelProperty(value = "管控情况")
    private java.lang.String gkqk;
	/**管控人员姓名*/
	@Excel(name = "管控人员姓名", width = 15)
    @ApiModelProperty(value = "管控人员姓名")
    private java.lang.String gkryName;
	/**管控人员手机号*/
	@Excel(name = "管控人员手机号", width = 15)
    @ApiModelProperty(value = "管控人员手机号")
    private java.lang.String gkryPhone;
	/**帮扶情况*/
	@Excel(name = "帮扶情况", width = 15)
    @ApiModelProperty(value = "帮扶情况")
    private java.lang.String bfqk;
	/**帮扶人员姓名*/
	@Excel(name = "帮扶人员姓名", width = 15)
    @ApiModelProperty(value = "帮扶人员姓名")
    private java.lang.String bfryName;
	/**帮扶人员手机号*/
	@Excel(name = "帮扶人员手机号", width = 15)
    @ApiModelProperty(value = "帮扶人员手机号")
    private java.lang.String bfryPhone;
	/**有无犯罪史*/
	@Excel(name = "有无犯罪史", width = 15)
    @ApiModelProperty(value = "有无犯罪史")
    private java.lang.String ywfzs;
	/**犯罪情况*/
	@Excel(name = "犯罪情况", width = 15)
    @ApiModelProperty(value = "犯罪情况")
    private java.lang.String fzqk;
	/**吸毒原因*/
	@Excel(name = "吸毒原因", width = 15)
    @ApiModelProperty(value = "吸毒原因")
    private java.lang.String xdyy;
	/**吸毒后果*/
	@Excel(name = "吸毒后果", width = 15)
    @ApiModelProperty(value = "吸毒后果")
    private java.lang.String xdhg;
}
