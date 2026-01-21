package org.jeecg.modules.airag.prompts.entity;

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
 * @Description: airag_prompts
 * @Author: jeecg-boot
 * @Date:   2025-12-12
 * @Version: V1.0
 */
@Data
@TableName("airag_prompts")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="airag_prompts")
public class AiragPrompts implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private java.lang.String id;
	/**提示词名称*/
	@Excel(name = "提示词名称", width = 15)
    @Schema(description = "提示词名称")
    private java.lang.String name;
	/**提示词名称*/
	@Excel(name = "提示key", width = 15)
    @Schema(description = "提示key")
    private java.lang.String promptKey;
	/**提示词功能描述*/
	@Excel(name = "提示词功能描述", width = 15)
    @Schema(description = "提示词功能描述")
    private java.lang.String description;
	/**提示词模板内容，支持变量占位符如 {{variable}}*/
	@Excel(name = "提示词模板内容，支持变量占位符如 {{variable}}", width = 15)
    @Schema(description = "提示词模板内容，支持变量占位符如 {{variable}}")
    private java.lang.String content;
	/**提示词分类*/
	@Excel(name = "提示词分类", width = 15)
    @Schema(description = "提示词分类")
    private java.lang.String category;
	/**标签，多个逗号分割*/
	@Excel(name = "标签，多个逗号分割", width = 15)
    @Schema(description = "标签，多个逗号分割")
    private java.lang.String tags;
	/**适配的大模型ID*/
	@Excel(name = "适配的大模型ID", width = 15)
    @Schema(description = "适配的大模型ID")
    private java.lang.String modelId;
	/**大模型的参数配置*/
	@Excel(name = "大模型的参数配置", width = 15)
    @Schema(description = "大模型的参数配置")
    private java.lang.String modelParam;
	/**状态（0:未发布 1:已发布）*/
	@Excel(name = "状态（0:未发布 1:已发布）", width = 15)
    @Schema(description = "状态（0:未发布 1:已发布）")
    private java.lang.String status;
	/**版本号(格式 0.0.1)*/
	@Excel(name = "版本号(格式 0.0.1)", width = 15)
    @Schema(description = "版本号(格式 0.0.1)")
    private java.lang.String version;
	/**删除状态（0未删除 1已删除）*/
	@Excel(name = "删除状态（0未删除 1已删除）", width = 15)
    @Schema(description = "删除状态（0未删除 1已删除）")
    @TableLogic
    private java.lang.Integer delFlag;
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
	/**租户id*/
	@Excel(name = "租户id", width = 15)
    @Schema(description = "租户id")
    private java.lang.String tenantId;
}
