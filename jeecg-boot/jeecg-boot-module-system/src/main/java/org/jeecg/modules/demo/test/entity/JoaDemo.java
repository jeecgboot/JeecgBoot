package org.jeecg.modules.demo.test.entity;

import java.io.Serializable;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @Description: 流程测试
 * @Author: jeecg-boot
 * @Date:   2019-05-14
 * @Version: V1.0
 */
@Data
@TableName("joa_demo")
public class JoaDemo implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**请假人*/
	@Excel(name = "请假人", width = 15)
	private java.lang.String name;
	/**请假天数*/
	@Excel(name = "请假天数", width = 15)
	private java.lang.Integer days;
	/**开始时间*/
	@Excel(name = "开始时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date beginDate;
	/**请假结束时间*/
	@Excel(name = "请假结束时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date endDate;
	/**请假原因*/
	@Excel(name = "请假原因", width = 15)
	private java.lang.String reason;
	/**流程状态*/
	@Excel(name = "流程状态", width = 15)
	private java.lang.String bpmStatus;
	/**创建人id*/
	@Excel(name = "创建人id", width = 15)
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**修改人id*/
	@Excel(name = "修改人id", width = 15)
	private java.lang.String updateBy;
}
