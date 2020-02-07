package org.jeecg.modules.tsrq.sqjzry.entity;

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
 * @Description: 社区矫正人员
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Data
@TableName("zz_sqjzry")
public class ZzSqjzry implements Serializable {
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
	/**矫正人员编号*/
	@Excel(name = "矫正人员编号", width = 15)
    @ApiModelProperty(value = "矫正人员编号")
    private java.lang.String jzrybh;
	/**原羁押场所*/
	@Excel(name = "原羁押场所", width = 15)
    @ApiModelProperty(value = "原羁押场所")
    private java.lang.String yjycs;
	/**矫正类别*/
	@Excel(name = "矫正类别", width = 15)
    @ApiModelProperty(value = "矫正类别")
    private java.lang.String jzlb;
	/**案件类别*/
	@Excel(name = "案件类别", width = 15)
    @ApiModelProperty(value = "案件类别")
    private java.lang.String ajlb;
	/**具体罪名*/
	@Excel(name = "具体罪名", width = 15)
    @ApiModelProperty(value = "具体罪名")
    private java.lang.String jtzm;
	/**原判刑期*/
	@Excel(name = "原判刑期", width = 15)
    @ApiModelProperty(value = "原判刑期")
    private java.lang.String ypxq;
	/**判刑开始日期*/
	@Excel(name = "判刑开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "判刑开始日期")
    private java.util.Date pxksDate;
	/**判刑结束日期*/
	@Excel(name = "判刑结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "判刑结束日期")
    private java.util.Date pxjsDate;
	/**矫正开始日期*/
	@Excel(name = "矫正开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "矫正开始日期")
    private java.util.Date jzksDate;
	/**矫正结束日期*/
	@Excel(name = "矫正结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "矫正结束日期")
    private java.util.Date jzjsDate;
	/**接受方式*/
	@Excel(name = "接受方式", width = 15)
    @ApiModelProperty(value = "接受方式")
    private java.lang.String jsfs;
	/**“四史”情况*/
	@Excel(name = "“四史”情况", width = 15)
    @ApiModelProperty(value = "“四史”情况")
    private java.lang.String sshqk;
	/**是否累惯犯*/
	@Excel(name = "是否累惯犯", width = 15)
    @ApiModelProperty(value = "是否累惯犯")
    private java.lang.String sflgf;
	/**三涉情况*/
	@Excel(name = "三涉情况", width = 15)
    @ApiModelProperty(value = "三涉情况")
    private java.lang.String ssqk;
	/**是否建矫正组*/
	@Excel(name = "是否建矫正组", width = 15)
    @ApiModelProperty(value = "是否建矫正组")
    private java.lang.String sfjjzz;
	/**人员组成情况*/
	@Excel(name = "人员组成情况", width = 15)
    @ApiModelProperty(value = "人员组成情况")
    private java.lang.String ryzcqk;
	/**矫正解除类型*/
	@Excel(name = "矫正解除类型", width = 15)
    @ApiModelProperty(value = "矫正解除类型")
    private java.lang.String jzjclx;
	/**是否有托管*/
	@Excel(name = "是否有托管", width = 15)
    @ApiModelProperty(value = "是否有托管")
    private java.lang.String sfytg;
	/**托管原因*/
	@Excel(name = "托管原因", width = 15)
    @ApiModelProperty(value = "托管原因")
    private java.lang.String tgyy;
	/**检查监督情况*/
	@Excel(name = "检查监督情况", width = 15)
    @ApiModelProperty(value = "检查监督情况")
    private java.lang.String tgJcjdqk;
	/**托管纠正情况*/
	@Excel(name = "托管纠正情况", width = 15)
    @ApiModelProperty(value = "托管纠正情况")
    private java.lang.String tgjzqk;
	/**是否有漏管*/
	@Excel(name = "是否有漏管", width = 15)
    @ApiModelProperty(value = "是否有漏管")
    private java.lang.String sfylg;
	/**漏管原因*/
	@Excel(name = "漏管原因", width = 15)
    @ApiModelProperty(value = "漏管原因")
    private java.lang.String lgyy;
	/**检查监督情况*/
	@Excel(name = "检查监督情况", width = 15)
    @ApiModelProperty(value = "检查监督情况")
    private java.lang.String lgJcjdqk;
	/**漏管纠正情况*/
	@Excel(name = "漏管纠正情况", width = 15)
    @ApiModelProperty(value = "漏管纠正情况")
    private java.lang.String lgjzqk;
	/**奖惩情况*/
	@Excel(name = "奖惩情况", width = 15)
    @ApiModelProperty(value = "奖惩情况")
    private java.lang.String jcqk;
	/**刑罚执行情况*/
	@Excel(name = "刑罚执行情况", width = 15)
    @ApiModelProperty(value = "刑罚执行情况")
    private java.lang.String xfzxqk;
	/**是否重新犯罪*/
	@Excel(name = "是否重新犯罪", width = 15)
    @ApiModelProperty(value = "是否重新犯罪")
    private java.lang.String sfcxfz;
	/**重新犯罪名称*/
	@Excel(name = "重新犯罪名称", width = 15)
    @ApiModelProperty(value = "重新犯罪名称")
    private java.lang.String cxfzmc;
}
