package org.jeecg.modules.airag.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 发送消息的入参
 * @Author: chenrui
 * @Date: 2025/2/25 11:47
 */
@NoArgsConstructor
@Data
public class ChatSendParams {

    public ChatSendParams(String content, String conversationId, String topicId, String appId) {
        this.content = content;
        this.conversationId = conversationId;
        this.topicId = topicId;
        this.appId = appId;
    }

    /**
     * 用户输入的聊天内容
     */
    private String content;

    /**
     * 对话会话ID
     */
    private String conversationId;

    /**
     * 对话主题ID（用于关联历史记录）
     */
    private String topicId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 图片列表
     */
    private List<String> images;

}
