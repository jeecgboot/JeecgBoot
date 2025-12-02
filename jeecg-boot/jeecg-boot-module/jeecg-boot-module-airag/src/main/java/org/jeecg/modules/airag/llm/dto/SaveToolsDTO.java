package org.jeecg.modules.airag.llm.dto;

import lombok.Data;

/**
 * 保存插件工具DTO
 * fro [QQYUN-12453]【AI】支持插件
 * @author chenrui
 * @date 2025/10/30
 */
@Data
public class SaveToolsDTO {
    /**
     * 插件ID
     */
    private String id;

    /**
     * 工具列表JSON字符串
     */
    private String tools;
}

