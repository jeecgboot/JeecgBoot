package org.jeecg.modules.demo.cursorstudent.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 课程管理模块
 * @Author: jeecg-boot
 * @Date:   2025-06-11
 * @Version: V1.0
 */
@Data
@TableName("coursemanagementmodule")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="课程管理模块")
public class Cursorstudent implements Serializable {
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
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
    @Schema(description = "课程名称")
    private java.lang.String coursename;
	/**课程代码*/
	@Excel(name = "课程代码", width = 15)
    @Schema(description = "课程代码")
    private java.lang.String coursecode;
	/**任课老师*/
	@Excel(name = "任课老师", width = 15)
    @Schema(description = "任课老师")
    private java.lang.String instructor;
	/**上课地点*/
	@Excel(name = "上课地点", width = 15)
    @Schema(description = "上课地点")
    private java.lang.String location;
}
