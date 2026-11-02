package org.jeecg.modules.airag.llm.service;

import java.util.List;
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

    /**
     * 获取流程插件（携带应用上下文参数）
     *
     * @param flowIds  多个流程id
     * @param appId    应用ID（变量节点需要）
     * @param memoryId 记忆库ID（记忆节点需要）
     */
    Map<String, Object> getFlowsToPlugin(String flowIds, String appId, String memoryId);

    // update-begin--author:wangshuai---date:20261012---for：【issues/9872】智能体中，涉及图片输入的流程无法关联对话中上传的图片
    /**
     * 获取流程插件（携带应用上下文和当前消息图片）
     *
     * @param flowIds  多个流程id
     * @param appId    应用ID（变量节点需要）
     * @param memoryId 记忆库ID（记忆节点需要）
     * @param images   当前消息上传的图片（流程开始节点的 images 入参需要）
     */
    Map<String, Object> getFlowsToPlugin(String flowIds, String appId, String memoryId, List<String> images);
    // update-end--author:wangshuai---date:20261012---for：【issues/9872】智能体中，涉及图片输入的流程无法关联对话中上传的图片
}
