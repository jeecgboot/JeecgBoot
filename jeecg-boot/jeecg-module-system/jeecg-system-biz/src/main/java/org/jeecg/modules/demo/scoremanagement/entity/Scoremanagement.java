package org.jeecg.modules.demo.scoremanagement.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 成绩管理模块
 * @Author: jeecg-boot
 * @Date:   2025-06-11
 * @Version: V1.0
 */
@Data
@TableName("scoremanagement")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="成绩管理模块")
public class Scoremanagement implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private java.lang.String id;
	/**创建人*/
    @Schema(description = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private java.lang.String sysOrgCode;
	/**学生*/
	@Excel(name = "学生", width = 15)
    @Schema(description = "学生")
    private java.lang.String astudent;
	/**学号*/
	@Excel(name = "学号", width = 15)
    @Schema(description = "学号")
    private java.lang.String studentid;
	/**课程名字*/
	@Excel(name = "课程名字", width = 15)
    @Schema(description = "课程名字")
    private java.lang.String coursename;
	/**课程代码*/
	@Excel(name = "课程代码", width = 15)
    @Schema(description = "课程代码")
    private java.lang.String coursecode;
	/**考试名字*/
	@Excel(name = "考试名字", width = 15)
    @Schema(description = "考试名字")
    private java.lang.String examname;
	/**考试时间*/
	@Excel(name = "考试时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "考试时间")
    private java.util.Date examtime;
	/**学生成绩*/
	@Excel(name = "学生成绩", width = 15)
    @Schema(description = "学生成绩")
    private java.lang.Double studentscore;
}
