package org.jeecg.modules.airag.app.vo;

import lombok.Data;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.common.vo.MessageHistory;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /**
     * 流程入参配置（工作流的额外参数设置）
     * key: 参数field, value: 参数值
     * for [issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程
     */
    private Map<String, Object> flowInputs;
}
