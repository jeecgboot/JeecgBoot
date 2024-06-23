package org.jeecg.modules.demo.gpt.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.demo.gpt.vo.ChatHistoryVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

//update-begin---author:chenrui ---date:20240126  for：【QQYUN-7932】AI助手------------

/**
 * AI助手聊天Service
 * @author chenrui
 * @date 2024/1/26 20:08
 */
public interface ChatService {
    /**
     * 创建SSE
     * @return
     */
    SseEmitter createChat();

    /**
     * 关闭SSE
     */
    void closeChat();

    /**
     * 客户端发送消息到服务端
     *
     * @param topicId
     * @param message
     * @author chenrui
     * @date 2024/1/26 20:01
     */
    void sendMessage(String topicId, String message);

    //update-begin---author:chenrui ---date:20240223  for：[QQYUN-8225]聊天记录保存------------
    /**
     * 保存聊天记录
     * @param chatHistoryVO
     * @return
     * @author chenrui
     * @date 2024/2/22 13:37
     */
    Result<?> saveHistory(ChatHistoryVO chatHistoryVO);

    /**
     * 查询聊天记录
     * @return
     * @author chenrui
     * @date 2024/2/22 13:59
     */
    Result<ChatHistoryVO> getHistoryByTopic();
    //update-end---author:chenrui ---date:20240223  for：[QQYUN-8225]聊天记录保存------------
}

//update-end---author:chenrui ---date:20240126  for：【QQYUN-7932】AI助手------------