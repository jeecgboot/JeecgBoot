package org.jeecg.modules.airag.app.service;

import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.common.handler.AIChatParams;

public interface IAiragVariableService {
    /**
     * 更新变量值
     *
     * @param userId
     * @param appId
     * @param name
     * @param value
     */
    void updateVariable(String userId, String appId, String name, String value);

    /**
     * 追加提示词
     *
     * @param username
     * @param app
     * @return
     */
    String additionalPrompt(String username, AiragApp app);

    /**
     * 初始化变量（仅不存在时设置）
     *
     * @param userId
     * @param appId
     * @param name
     * @param defaultValue
     */
    void initVariable(String userId, String appId, String name, String defaultValue);

    /**
     * 添加变量更新工具
     *
     * @param params
     * @param aiApp
     * @param username
     */
    void addUpdateVariableTool(AiragApp aiApp, String username, AIChatParams params);
}
