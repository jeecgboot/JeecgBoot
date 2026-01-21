package org.jeecg.modules.airag.llm.service;

import java.util.Map;

/**
 * @Description: 获取流程mcp服务
 * @Author: wangshuai
 * @Date: 2025-12-22 15:34:20
 * @Version: V1.0
 */
public interface IAiragFlowPluginService {

    /**
     * 同步所有启用的流程到MCP插件配置
     *
     * @param flowIds 多个流程id
     */
    Map<String, Object> getFlowsToPlugin(String flowIds);
}
