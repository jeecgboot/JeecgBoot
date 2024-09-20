package org.jeecg.modules.demo.gpt.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.demo.gpt.cache.LocalCache;
import org.jeecg.modules.demo.gpt.listeners.OpenAISSEEventSourceListener;
import org.jeecg.modules.demo.gpt.service.ChatService;
import org.jeecg.modules.demo.gpt.vo.ChatHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//update-begin---author:chenrui ---date:20240126  for：【QQYUN-7932】AI助手------------

/**
 * AI助手聊天Service
 * @author chenrui
 * @date 2024/1/26 20:07
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    //update-begin---author:chenrui ---date:20240223  for：[QQYUN-8225]聊天记录保存------------
    private static final String CACHE_KEY_PREFIX = "ai:chart:";

    /**
     *
     */
    private static final String CACHE_KEY_MSG_CONTEXT = "msg_content";


    /**
     *
     */
    private static final String CACHE_KEY_MSG_HISTORY = "msg_history";

    @Autowired
    RedisTemplate redisTemplate;
    //update-end---author:chenrui ---date:20240223  for：[QQYUN-8225]聊天记录保存------------

    private OpenAiStreamClient openAiStreamClient = null;

    //update-begin---author:chenrui ---date:20240131  for：[QQYUN-8212]fix 没有配置启动报错------------

    /**
     * 防止client不能成功注入
     * @return
     * @author chenrui
     * @date 2024/2/3 23:08
     */
    private OpenAiStreamClient ensureClient(){
        if (null == this.openAiStreamClient){
            //update-begin---author:chenrui ---date:20240625  for：[TV360X-1570]给于更友好的提示，提示未配置ai------------
            try {
                this.openAiStreamClient = SpringContextUtils.getBean(OpenAiStreamClient.class);
            } catch (Exception ignored) {
                sendErrorMsg("如果您想使用AI助手，请先设置相应配置!");
            }
            //update-end---author:chenrui ---date:20240625  for：[TV360X-1570]给于更友好的提示，提示未配置ai------------
        }
        return this.openAiStreamClient;
    }
    //update-end---author:chenrui ---date:20240131  for：[QQYUN-8212]fix 没有配置启动报错------------

    private String getUserId() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return sysUser.getId();
    }

    @Override
    public SseEmitter createChat() {
        String uid = getUserId();
        //默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(-0L);
        //完成后回调
        sseEmitter.onCompletion(() -> {
            log.info("[{}]结束连接...................",uid);
            LocalCache.CACHE.remove(uid);
        });
        //超时回调
        sseEmitter.onTimeout(() -> {
            log.info("[{}]连接超时...................", uid);
        });
        //异常回调
        sseEmitter.onError(
                throwable -> {
                    try {
                        log.info("[{}]连接异常,{}", uid, throwable.toString());
                        sseEmitter.send(SseEmitter.event()
                                .id(uid)
                                .name("发生异常！")
                                .data(Message.builder().content("发生异常请重试！").build())
                                .reconnectTime(3000));
                        LocalCache.CACHE.put(uid, sseEmitter);
                    } catch (IOException e) {
                        log.error(e.getMessage(),e);
                    }
                }
        );
        try {
            sseEmitter.send(SseEmitter.event().reconnectTime(5000));
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        LocalCache.CACHE.put(uid, sseEmitter);
        log.info("[{}]创建sse连接成功！", uid);
        return sseEmitter;
    }

    @Override
    public void closeChat() {
        String uid = getUserId();
        SseEmitter sse = (SseEmitter) LocalCache.CACHE.get(uid);
        if (sse != null) {
            sse.complete();
            //移除
            LocalCache.CACHE.remove(uid);
        }
    }

    @Override
    public void sendMessage(String topicId, String message) {
        String uid = getUserId();
        if (StrUtil.isBlank(message)) {
            log.info("参数异常，message为null");
            throw new BaseException("参数异常，message不能为空~");
        }
        if (StrUtil.isBlank(topicId)) {
            topicId = UUIDGenerator.generate();
        }
        //update-begin---author:chenrui ---date:20240223  for：[QQYUN-8225]聊天记录保存------------
        log.info("话题id:{}", topicId);
        String cacheKey = CACHE_KEY_PREFIX + uid + "_" + topicId;
        String messageContext = (String) redisTemplate.opsForHash().get(cacheKey, CACHE_KEY_MSG_CONTEXT);
        List<Message> msgHistory = new ArrayList<>();
        if (StrUtil.isNotBlank(messageContext)) {
            List<Message> messages = JSONArray.parseArray(messageContext, Message.class);
            msgHistory = messages == null ? new ArrayList<>() : messages;
        }
        Message currentMessage = Message.builder().content(message).role(Message.Role.USER).build();
        msgHistory.add(currentMessage);

        SseEmitter sseEmitter = (SseEmitter) LocalCache.CACHE.get(uid);
        if (sseEmitter == null) {
            log.info("聊天消息推送失败uid:[{}],没有创建连接，请重试。", uid);
            throw new JeecgBootException("聊天消息推送失败uid:[{}],没有创建连接，请重试。~");
        }
        //update-begin---author:chenrui ---date:20240625  for：[TV360X-1570]给于更友好的提示，提示未配置ai------------
        OpenAiStreamClient client = ensureClient();
        if (null != client) {
            OpenAISSEEventSourceListener openAIEventSourceListener = new OpenAISSEEventSourceListener(topicId, sseEmitter);
            ChatCompletion completion = ChatCompletion
                    .builder()
                    .messages(msgHistory)
                    .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                    .build();
            client.streamChatCompletion(completion, openAIEventSourceListener);
            redisTemplate.opsForHash().put(cacheKey, CACHE_KEY_MSG_CONTEXT, JSONUtil.toJsonStr(msgHistory));
            //update-end---author:chenrui ---date:20240223  for：[QQYUN-8225]聊天记录保存------------
            Result.ok(completion.tokens());
        }
        //update-end---author:chenrui ---date:20240625  for：[TV360X-1570]给于更友好的提示，提示未配置ai------------
    }

    //update-begin---author:chenrui ---date:20240223  for：[QQYUN-8225]聊天记录保存------------
    @Override
    public Result<?> saveHistory(ChatHistoryVO chatHistoryVO) {
        String uid = getUserId();
        String cacheKey = CACHE_KEY_PREFIX + CACHE_KEY_MSG_HISTORY + ":" + uid;
        redisTemplate.opsForValue().set(cacheKey, chatHistoryVO.getContent());
        return Result.OK("保存成功");
    }

    @Override
    public Result<ChatHistoryVO> getHistoryByTopic() {
        String uid = getUserId();
        String cacheKey = CACHE_KEY_PREFIX + CACHE_KEY_MSG_HISTORY + ":" + uid;
        String historyContent = (String) redisTemplate.opsForValue().get(cacheKey);
        ChatHistoryVO chatHistoryVO = new ChatHistoryVO();
        chatHistoryVO.setContent(historyContent);
        return Result.OK(chatHistoryVO);
    }
    //update-end---author:chenrui ---date:20240223  for：[QQYUN-8225]聊天记录保存------------

    /**
     * 发送异常消息给前端
     * [TV360X-1570]给于更友好的提示，提示未配置ai
     *
     * @param msg
     * @author chenrui
     * @date 2024/6/25 10:38
     */
    private void sendErrorMsg(String msg) {
        String uid = getUserId();
        SseEmitter sseEmitter = (SseEmitter) LocalCache.CACHE.get(uid);
        if (sseEmitter == null) {
            return;
        }
        try {
            sseEmitter.send(SseEmitter.event()
                    .id("[ERR]")
                    .data(Message.builder().content(msg).build())
                    .reconnectTime(3000));
            sseEmitter.send(SseEmitter.event()
                    .id("[DONE]")
                    .data("[DONE]")
                    .reconnectTime(3000));
            sseEmitter.complete();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}

//update-end---author:chenrui ---date:20240126  for：【QQYUN-7932】AI助手------------
