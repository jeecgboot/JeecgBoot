package org.jeecg.modules.online.cgreport.entity;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @Description: 在线报表配置
 * @author: jeecg-boot
 * @date: 2019-03-08
 * @version: V1.0
 */
@Data
@TableName("onl_cgreport_param")
public class OnlCgreportParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/** id */
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/** 动态报表ID */
	private java.lang.String cgrheadId;
	/** 参数字段 */
	private java.lang.String paramName;
	/** 参数文本 */
	private java.lang.String paramTxt;
	/** 参数默认值 */
	private java.lang.String paramValue;
	/** 排序 */
	private java.lang.Integer orderNum;
	/** 创建人登录名称 */
	private java.lang.String createBy;
	/** 创建日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/** 更新人登录名称 */
	private java.lang.String updateBy;
	/** 更新日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
