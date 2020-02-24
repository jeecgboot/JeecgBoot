package com.crtech.modules.cms.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * @Description: 新闻
 * @Author: jeecg-boot
 * @Date:   2019-09-21
 * @Version: V1.0
 */
@Data
@TableName("news")
public class News implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**文章标题*/
	@Excel(name = "文章标题", width = 15)
	private java.lang.String title;
	/**缩略图(新增)*/
	@Excel(name = "缩略图(新增)", width = 15)
	private java.lang.String thumbnail;
	/**分类ID*/
	@Excel(name = "分类ID", width = 15)
	private java.lang.Integer sortId;
	/**文章描述*/
	@Excel(name = "文章描述", width = 15)
	private java.lang.String description;
	/**状态*/
	@Excel(name = "状态", width = 15)
	private java.lang.Integer status;
	/**作者*/
	@Excel(name = "作者", width = 15)
	private java.lang.String author;
	/**主键*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**文章内容*/
	@Excel(name = "文章内容", width = 15)
	private java.lang.String content;
	/**扩展信息*/
	@Excel(name = "扩展信息", width = 15)
	private java.lang.String articleExtend;
	/**创建人登录名称*/
	@Excel(name = "创建人登录名称", width = 15)
	private java.lang.String createBy;
	/**发表时间*/
	@Excel(name = "发表时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date createTime;
	/**更新人登录名称*/
	@Excel(name = "更新人登录名称", width = 15)
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date updateTime;
}
