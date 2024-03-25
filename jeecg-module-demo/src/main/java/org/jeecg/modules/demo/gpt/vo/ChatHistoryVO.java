package org.jeecg.modules.demo.gpt.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 聊天记录
 * @Author: chenrui
 * @Date: 2024/2/22 13:36
 */
@Data
public class ChatHistoryVO implements Serializable {
    private static final long serialVersionUID = 3238429500037511283L;

    /**
     * 话题id
     */
    String topicId;

    /**
     * 聊天记录内容
     */
    String content;
}
