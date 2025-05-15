package org.jeecg.modules.airag.app.vo;

import lombok.Data;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.common.vo.MessageHistory;

import java.util.Date;
import java.util.List;

/**
 * @Description: 聊天会话
 * @Author: chenrui
 * @Date: 2025/2/25 14:56
 */
@Data
public class ChatConversation {

    /**
     * 会话id
     */
    private String id;

    /**
     * 会话标题
     */
    private String title;

    /**
     * 消息记录
     */
    private List<MessageHistory> messages;

    /**
     * app
     */
    private AiragApp app;

    /**
     * 创建时间
     */
    private Date createTime;
}
