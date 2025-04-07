package org.jeecg.modules.airag.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.config.shiro.IgnoreAuth;
import org.jeecg.modules.airag.app.service.IAiragChatService;
import org.jeecg.modules.airag.app.vo.ChatConversation;
import org.jeecg.modules.airag.app.vo.ChatSendParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


/**
 * airag应用-chat
 *
 * @Author: chenrui
 * @Date: 2025-02-25 11:40
 */
@Slf4j
@RestController
@RequestMapping("/airag/chat")
public class AiragChatController {

    @Autowired
    IAiragChatService chatService;


    /**
     * 发送消息
     *
     * @return 返回一个Result对象，表示发送消息的结果
     * @author chenrui
     * @date 2025/2/25 11:42
     */
    @IgnoreAuth
    @PostMapping(value = "/send")
    public SseEmitter send(@RequestBody ChatSendParams chatSendParams) {
        return chatService.send(chatSendParams);
    }

    /**
     * 发送消息 <br/>
     * 兼容旧版浏览器
     * @param content
     * @param conversationId
     * @param topicId
     * @param appId
     * @return
     * @author chenrui
     * @date 2025/2/25 18:13
     */
    @GetMapping(value = "/send")
    public SseEmitter sendByGet(@RequestParam("content") String content,
                                @RequestParam(value = "conversationId", required = false) String conversationId,
                                @RequestParam(value = "topicId", required = false) String topicId,
                                @RequestParam(value = "appId", required = false) String appId) {
        ChatSendParams chatSendParams = new ChatSendParams(content, conversationId, topicId, appId);
        return chatService.send(chatSendParams);
    }

    /**
     * 获取所有对话
     *
     * @return 返回一个Result对象，包含所有对话的信息
     * @author chenrui
     * @date 2025/2/25 11:42
     */
    @IgnoreAuth
    @GetMapping(value = "/conversations")
    public Result<?> getConversations(@RequestParam(value = "appId", required = false) String appId) {
        return chatService.getConversations(appId);
    }

    /**
     * 删除会话
     *
     * @param id
     * @return
     * @author chenrui
     * @date 2025/3/3 16:55
     */
    @IgnoreAuth
    @DeleteMapping(value = "/conversation/{id}")
    public Result<?> deleteConversation(@PathVariable("id") String id) {
        return chatService.deleteConversation(id);
    }

    /**
     * 更新会话标题
     *
     * @param updateTitleParams
     * @return
     * @author chenrui
     * @date 2025/3/3 16:55
     */
    @IgnoreAuth
    @PutMapping(value = "/conversation/update/title")
    public Result<?> updateConversationTitle(@RequestBody ChatConversation updateTitleParams) {
        return chatService.updateConversationTitle(updateTitleParams);
    }

    /**
     * 获取消息
     *
     * @return 返回一个Result对象，包含消息的信息
     * @author chenrui
     * @date 2025/2/25 11:42
     */
    @IgnoreAuth
    @GetMapping(value = "/messages")
    public Result<?> getMessages(@RequestParam(value = "conversationId", required = true) String conversationId) {
        return chatService.getMessages(conversationId);
    }

    /**
     * 清空消息
     *
     * @return
     * @author chenrui
     * @date 2025/2/25 11:42
     */
    @IgnoreAuth
    @GetMapping(value = "/messages/clear/{conversationId}")
    public Result<?> clearMessage(@PathVariable(value = "conversationId") String conversationId) {
        return chatService.clearMessage(conversationId);
    }


    /**
     * 根据请求ID停止某个请求的处理
     *
     * @param requestId 请求的唯一标识符，用于识别和停止特定的请求
     * @return 返回一个Result对象，表示停止请求的结果
     * @author chenrui
     * @date 2025/2/25 11:42
     */
    @IgnoreAuth
    @GetMapping(value = "/stop/{requestId}")
    public Result<?> stop(@PathVariable(name = "requestId", required = true) String requestId) {
        return chatService.stop(requestId);
    }

}
