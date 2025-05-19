package org.jeecg.modules.airag.llm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: AIRag知识库
 * @Author: jeecg-boot
 * @Date: 2025-02-18
 * @Version: V1.0
 */
@Schema(description="AIRag知识库")
@Data
@TableName("airag_knowledge")
public class AiragKnowledge implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private java.util.Date createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;

    /**
     * 所属部门
     */
    @Schema(description = "所属部门")
    private String sysOrgCode;

    /**
     * 租户id
     */
    @Excel(name = "租户id", width = 15)
    @Schema(description = "租户id")
    private String tenantId;

    /**
     * 知识库名称
     */
    @Excel(name = "知识库名称", width = 15)
    @Schema(description = "知识库名称")
    private String name;

    /**
     * 向量模型id
     */
    @Excel(name = "向量模型id", width = 15, dictTable = "airag_model where model_type = 'EMBED'", dicText = "name", dicCode = "id")
    @Dict(dictTable = "airag_model where model_type = 'EMBED'", dicText = "name", dicCode = "id")
    @Schema(description = "向量模型id")
    private String embedId;

    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @Schema(description = "描述")
    private String descr;

    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @Schema(description = "状态")
    private String status;
}
