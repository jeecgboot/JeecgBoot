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
@TableName("onl_cgreport_item")
public class OnlCgreportItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/** id */
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/** 报表ID */
	private java.lang.String cgrheadId;
	/** 字段名字 */
	private java.lang.String fieldName;
	/** 字段文本 */
	private java.lang.String fieldTxt;
	/** fieldWidth */
	private java.lang.Integer fieldWidth;
	/** 字段类型 */
	private java.lang.String fieldType;
	/** 查询模式 */
	private java.lang.String searchMode;
	/** 是否排序 0否,1是 */
	private java.lang.Integer isOrder;
	/** 是否查询 0否,1是 */
	private java.lang.Integer isSearch;
	/** 字典CODE */
	private java.lang.String dictCode;
	/** 字段跳转URL */
	private java.lang.String fieldHref;
	/** 是否显示 0否,1显示 */
	private java.lang.Integer isShow;
	/** 排序 */
	private java.lang.Integer orderNum;
	/** 取值表达式 */
	private java.lang.String replaceVal;
	/** 创建人 */
	private java.lang.String createBy;
	/** 创建时间 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/** 修改人 */
	private java.lang.String updateBy;
	/** 修改时间 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
