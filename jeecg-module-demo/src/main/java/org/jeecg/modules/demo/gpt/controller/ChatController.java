package org.jeecg.modules.demo.gpt.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.demo.gpt.service.ChatService;
import org.jeecg.modules.demo.gpt.vo.ChatHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

//update-begin---author:chenrui ---date:20240126  for：【QQYUN-7932】AI助手------------

/**
 * @Description: chatGpt-聊天接口
 * @Author: chenrui
 * @Date: 2024/1/9 16:30
 */
@Controller
@RequestMapping("/test/ai/chat")
public class ChatController {

    @Autowired
    ChatService chatService;

    /**
     * 创建sse连接
     *
     * @return
     */
    @GetMapping(value = "/send")
    public SseEmitter createConnect(@RequestParam(name = "topicId", required = false) String topicId, @RequestParam(name = "message", required = true) String message) {
        SseEmitter sse = chatService.createChat();
        chatService.sendMessage(topicId, message);
        return sse;
    }

    //update-begin---author:chenrui ---date:20240223  for：[QQYUN-8225]聊天记录保存------------
    /**
     * 保存聊天记录
     * @param chatHistoryVO
     * @return
     * @author chenrui
     * @date 2024/2/22 13:54
     */
    @PostMapping(value = "/history/save")
    @ResponseBody
    public Result<?> saveHistory(@RequestBody ChatHistoryVO chatHistoryVO) {
        return chatService.saveHistory(chatHistoryVO);
    }

    /**
     * 查询聊天记录
     * @return
     * @author chenrui
     * @date 2024/2/22 14:03
     */
    @GetMapping(value = "/history/get")
    @ResponseBody
    public Result<ChatHistoryVO> getHistoryByTopic() {
        return chatService.getHistoryByTopic();
    }
    //update-end---author:chenrui ---date:20240223  for：[QQYUN-8225]聊天记录保存------------

    /**
     * 关闭连接
     */
    @GetMapping(value = "/close")
    public void closeConnect() {
        chatService.closeChat();
    }


}
//update-end---author:chenrui ---date:20240126  for：【QQYUN-7932】AI助手------------
