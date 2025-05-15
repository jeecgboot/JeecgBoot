package org.jeecg.modules.airag.llm.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;


import io.swagger.v3.oas.annotations.media.Schema;

import java.io.UnsupportedEncodingException;

/**
 * @Description: airag知识库文档
 * @Author: jeecg-boot
 * @Date: 2025-02-18
 * @Version: V1.0
 */
@Schema(description="airag知识库文档")
@Data
@TableName("airag_knowledge_doc")
public class AiragKnowledgeDoc implements Serializable {
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
    private Date createTime;

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
    private Date updateTime;

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
     * 知识库id
     */
    @Schema(description = "知识库id")
    private String knowledgeId;

    /**
     * 标题
     */
    @Excel(name = "标题", width = 15)
    @Schema(description = "标题")
    private String title;

    /**
     * 类型
     */
    @Excel(name = "类型", width = 15, dicCode = "know_doc_type")
    @Schema(description = "类型")
    private String type;

    /**
     * 内容
     */
    @Excel(name = "内容", width = 15)
    @Schema(description = "内容")
    private String content;

    /**
     * 元数据,存储上传文件的存储目录以及网站站点 <br/>
     * eg. {"filePath":"https://xxxxxx","website":"http://hellp.jeecg.com"}
     */
    @Excel(name = "元数据", width = 15)
    @Schema(description = "元数据")
    private String metadata;

    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @Schema(description = "状态")
    private String status;

    /**
     * 服务器基础路径
     */
    @TableField(exist = false)
    private String baseUrl;
}
