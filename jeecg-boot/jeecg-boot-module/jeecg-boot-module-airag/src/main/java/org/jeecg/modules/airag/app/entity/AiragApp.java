package org.jeecg.modules.airag.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: AI应用
 * @Author: jeecg-boot
 * @Date: 2025-02-26
 * @Version: V1.0
 */
@Data
@TableName("airag_app")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="AI应用")
public class AiragApp implements Serializable {
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
     * 应用名称
     */
    @Excel(name = "应用名称", width = 15)
    @Schema(description = "应用名称")
    private String name;
    /**
     * 应用描述
     */
    @Excel(name = "应用描述", width = 15)
    @Schema(description = "应用描述")
    private String descr;
    /**
     * 应用图标
     */
    @Excel(name = "应用图标", width = 15)
    @Schema(description = "应用图标")
    private String icon;
    /**
     * 应用类型
     */
    @Excel(name = "应用类型", width = 15, dicCode = "ai_app_type")
    @Dict(dicCode = "ai_app_type")
    @Schema(description = "应用类型")
    private String type;
    /**
     * 开场白
     */
    @Excel(name = "开场白", width = 15)
    @Schema(description = "开场白")
    private String prologue;
    /**
     * 预设问题
     */
    @Excel(name = "预设问题", width = 15)
    @Schema(description = "预设问题")
    private String presetQuestion;
    /**
     * 提示词
     */
    @Excel(name = "提示词", width = 15)
    @Schema(description = "提示词")
    private String prompt;
    /**
     * 模型配置
     */
    @Excel(name = "模型配置", width = 15, dictTable = "airag_model where model_type = 'LLM' ", dicText = "name", dicCode = "id")
    @Dict(dictTable = "airag_model where model_type = 'LLM' ", dicText = "name", dicCode = "id")
    @Schema(description = "模型配置")
    private String modelId;
    /**
     * 历史消息数
     */
    @Excel(name = "历史消息数", width = 15)
    @Schema(description = "历史消息数")
    private Integer msgNum;
    /**
     * 知识库
     */
    @Excel(name = "知识库", width = 15, dictTable = "airag_knowledge where status = 'enable'", dicText = "name", dicCode = "id")
    @Dict(dictTable = "airag_knowledge where status = 'enable'", dicText = "name", dicCode = "id")
    @Schema(description = "知识库")
    private String knowledgeIds;
    /**
     * 流程
     */
    @Excel(name = "流程", width = 15, dictTable = "airag_flow where status = 'enable' ", dicText = "name", dicCode = "id")
    @Dict(dictTable = "airag_flow where status = 'enable' ", dicText = "name", dicCode = "id")
    @Schema(description = "流程")
    private String flowId;
    /**
     * 快捷指令
     */
    @Excel(name = "快捷指令", width = 15)
    @Schema(description = "快捷指令")
    private String quickCommand;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @Schema(description = "状态")
    private String status;


    /**
     * 元数据
     */
    @Excel(name = "元数据", width = 15)
    @Schema(description = "元数据")
    private String metadata;

    /**
     * 知识库ids
     */
    @TableField(exist = false)
    private List<String> knowIds;

    /**
     * 获取知识库id
     *
     * @return
     * @author chenrui
     * @date 2025/2/28 11:45
     */
    public List<String> getKnowIds() {
        if (oConvertUtils.isNotEmpty(knowledgeIds)) {
            String[] knowIds = knowledgeIds.split(",");
            return Arrays.asList(knowIds);
        } else {
            return new ArrayList<>(0);
        }
    }
}
