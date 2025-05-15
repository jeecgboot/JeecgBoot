package org.jeecg.modules.airag.app.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.airag.app.vo.AppDebugParams;
import org.jeecg.modules.airag.app.vo.ChatConversation;
import org.jeecg.modules.airag.app.vo.ChatSendParams;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * ai聊天
 *
 * @author chenrui
 * @date 2025/2/25 13:36
 */
public interface IAiragChatService {

    /**
     * 发送消息
     *
     * @param chatSendParams
     * @return
     * @author chenrui
     * @date 2025/2/25 13:39
     */
    SseEmitter send(ChatSendParams chatSendParams);


    /**
     * 调试应用
     *
     * @param appDebugParams
     * @return
     * @author chenrui
     * @date 2025/2/28 10:49
     */
    SseEmitter debugApp(AppDebugParams appDebugParams);

    /**
     * 停止响应
     *
     * @param requestId
     * @return
     * @author chenrui
     * @date 2025/2/25 17:17
     */
    Result<?> stop(String requestId);

    /**
     * 获取所有对话
     *
     * @param appId
     * @return
     * @author chenrui
     * @date 2025/2/26 14:48
     */
    Result<?> getConversations(String appId);

    /**
     * 获取对话聊天记录
     *
     * @param conversationId
     * @return
     * @author chenrui
     * @date 2025/2/26 15:16
     */
    Result<?> getMessages(String conversationId);

    /**
     * 删除会话
     *
     * @param conversationId
     * @return
     * @author chenrui
     * @date 2025/3/3 16:55
     */
    Result<?> deleteConversation(String conversationId);

    /**
     * 更新会话标题
     * @param updateTitleParams
     * @return
     * @author chenrui
     * @date 2025/3/3 17:02
     */
    Result<?> updateConversationTitle(ChatConversation updateTitleParams);

    /**
     * 清空消息
     * @param conversationId
     * @return
     * @author chenrui
     * @date 2025/3/3 19:49
     */
    Result<?> clearMessage(String conversationId);
}
