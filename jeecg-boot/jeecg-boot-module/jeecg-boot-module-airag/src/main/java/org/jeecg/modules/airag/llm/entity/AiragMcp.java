package org.jeecg.modules.airag.llm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: MCP
 * @Author: jeecg-boot
 * @Date: 2025-10-20
 * @Version: V1.0
 */
@Data
@TableName("airag_mcp")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "MCP")
public class AiragMcp implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private java.lang.String id;
    /**
     * 应用图标
     */
    @Excel(name = "应用图标", width = 15)
    @Schema(description = "应用图标")
    private java.lang.String icon;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @Schema(description = "名称")
    private java.lang.String name;
    /**
     * 描述
     */
    @Excel(name = "描述", width = 15)
    @Schema(description = "描述")
    private java.lang.String descr;
    /**
     * 类型（plugin=插件，mcp=MCP）
     * for [QQYUN-12453]【AI】支持插件
     */
    @Excel(name = "类型（plugin=插件，mcp=MCP）", width = 15)
    @Schema(description = "类型（plugin=插件，mcp=MCP）")
    private java.lang.String category;
    /**
     * mcp类型（sse：sse类型；stdio：标准类型）
     */
    @Excel(name = "mcp类型（sse：sse类型；stdio：标准类型）", width = 15)
    @Schema(description = "mcp类型（sse：sse类型；stdio：标准类型）")
    private java.lang.String type;
    /**
     * 服务端点（SSE类型为URL，stdio类型为命令）
     */
    @Excel(name = "服务端点（SSE类型为URL，stdio类型为命令）", width = 15)
    @Schema(description = "服务端点（SSE类型为URL，stdio类型为命令）")
    private java.lang.String endpoint;
    /**
     * 请求头（sse类型）、环境变量（stdio类型）
     */
    @Excel(name = "请求头（sse类型）、环境变量（stdio类型）", width = 15)
    @Schema(description = "请求头（sse类型）、环境变量（stdio类型）")
    private java.lang.String headers;
    /**
     * 工具列表
     */
    @Excel(name = "工具列表", width = 15)
    @Schema(description = "工具列表")
    private java.lang.String tools;
    /**
     * 状态（enable=启用、disable=禁用）
     */
    @Excel(name = "状态（enable=启用、disable=禁用）", width = 15)
    @Schema(description = "状态（enable=启用、disable=禁用）")
    private java.lang.String status;
    /**
     * 是否同步
     */
    @Excel(name = "是否同步", width = 15)
    @Schema(description = "是否同步")
    private java.lang.Integer synced;
    /**
     * 元数据
     */
    @Excel(name = "元数据", width = 15)
    @Schema(description = "元数据")
    private java.lang.String metadata;
    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private java.lang.String createBy;
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
    private java.lang.String updateBy;
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
    private java.lang.String sysOrgCode;
    /**
     * 租户id
     */
    @Excel(name = "租户id", width = 15)
    @Schema(description = "租户id")
    private java.lang.String tenantId;
}
