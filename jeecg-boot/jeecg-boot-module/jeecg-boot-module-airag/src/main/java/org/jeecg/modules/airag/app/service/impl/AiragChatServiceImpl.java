package org.jeecg.modules.airag.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.service.TokenStream;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.*;
import org.jeecg.modules.airag.app.consts.AiAppConsts;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.app.service.IAiragAppService;
import org.jeecg.modules.airag.app.service.IAiragChatService;
import org.jeecg.modules.airag.app.vo.AppDebugParams;
import org.jeecg.modules.airag.app.vo.ChatConversation;
import org.jeecg.modules.airag.app.vo.ChatSendParams;
import org.jeecg.modules.airag.common.consts.AiragConsts;
import org.jeecg.modules.airag.common.handler.AIChatParams;
import org.jeecg.modules.airag.common.handler.IAIChatHandler;
import org.jeecg.modules.airag.common.utils.AiragLocalCache;
import org.jeecg.modules.airag.common.vo.MessageHistory;
import org.jeecg.modules.airag.common.vo.event.EventData;
import org.jeecg.modules.airag.common.vo.event.EventFlowData;
import org.jeecg.modules.airag.common.vo.event.EventMessageData;
import org.jeecg.modules.airag.flow.consts.FlowConsts;
import org.jeecg.modules.airag.flow.service.IAiragFlowService;
import org.jeecg.modules.airag.flow.vo.api.FlowRunParams;
import org.jeecg.modules.airag.llm.entity.AiragModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


/**
 * AI助手聊天Service
 *
 * @author chenrui
 * @date 2024/1/26 20:07
 */
@Service
@Slf4j
public class AiragChatServiceImpl implements IAiragChatService {

    @Autowired
    IAIChatHandler aiChatHandler;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IAiragAppService airagAppService;

    @Autowired
    IAiragFlowService airagFlowService;

    @Autowired
    private ISysBaseAPI sysBaseApi;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public SseEmitter send(ChatSendParams chatSendParams) {
        AssertUtils.assertNotEmpty("参数异常", chatSendParams);
        String userMessage = chatSendParams.getContent();
        AssertUtils.assertNotEmpty("至少发送一条消息", userMessage);

        // 获取会话信息
        String conversationId = chatSendParams.getConversationId();
        String topicId = oConvertUtils.getString(chatSendParams.getTopicId(), UUIDGenerator.generate());
        // 获取app信息
        AiragApp app = null;
        if (oConvertUtils.isNotEmpty(chatSendParams.getAppId())) {
            app = airagAppService.getById(chatSendParams.getAppId());
        }
        ChatConversation chatConversation = getOrCreateChatConversation(app, conversationId);
        // 更新标题
        if (oConvertUtils.isEmpty(chatConversation.getTitle())) {
            chatConversation.setTitle(userMessage.length() > 5 ? userMessage.substring(0, 5) : userMessage);
        }
        // 发送消息
        return doChat(chatConversation, topicId, chatSendParams);
    }

    @Override
    public SseEmitter debugApp(AppDebugParams appDebugParams) {
        AssertUtils.assertNotEmpty("参数异常", appDebugParams);
        String userMessage = appDebugParams.getContent();
        AssertUtils.assertNotEmpty("至少发送一条消息", userMessage);
        AssertUtils.assertNotEmpty("应用信息不能为空", appDebugParams.getApp());
        // 获取会话信息
        String topicId = oConvertUtils.getString(appDebugParams.getTopicId(), UUIDGenerator.generate());
        AiragApp app = appDebugParams.getApp();
        app.setId("__DEBUG_APP");
        ChatConversation chatConversation = getOrCreateChatConversation(app, topicId);
        // 发送消息
        SseEmitter emitter = doChat(chatConversation, topicId, appDebugParams);
        //保存会话
        saveChatConversation(chatConversation, true, null);
        return emitter;
    }


    @Override
    public Result<?> stop(String requestId) {
        AssertUtils.assertNotEmpty("requestId不能为空", requestId);
        // 从缓存中获取对应的SseEmitter
        SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
        if (emitter != null) {
            closeSSE(emitter, new EventData(requestId, null, EventData.EVENT_MESSAGE_END));
            return Result.ok("会话已成功终止");
        } else {
            return Result.error("未找到对应的会话");
        }
    }

    /**
     * 关闭sse
     *
     * @param emitter
     * @param eventData
     * @throws IOException
     * @author chenrui
     * @date 2025/2/27 15:56
     */
    private static void closeSSE(SseEmitter emitter, EventData eventData) {
        AssertUtils.assertNotEmpty("请求id不能为空", eventData);
        if (null == emitter) {
            log.warn("会话已关闭");
            return;
        }
        try {
            // 发送完成事件
            emitter.send(SseEmitter.event().data(eventData));
        } catch (IOException e) {
            log.error("终止会话时发生错误", e);
        } finally {
            // 从缓存中移除emitter
            AiragLocalCache.remove(AiragConsts.CACHE_TYPE_SSE, eventData.getRequestId());
            // 关闭emitter
            emitter.complete();
        }
    }

    @Override
    public Result<?> getConversations(String appId) {
        if (oConvertUtils.isEmpty(appId)) {
            appId = AiAppConsts.DEFAULT_APP_ID;
        }
        String key = getConversationDirCacheKey(null);
        key = key + ":*";
        List<String> keys = redisUtil.scan(key);
        // 如果键集合为空，返回空列表
        if (keys.isEmpty()) {
            return Result.ok(Collections.emptyList());
        }

        // 遍历键集合，获取对应的 ChatConversation 对象
        List<ChatConversation> conversations = new ArrayList<>();
        for (Object k : keys) {
            ChatConversation conversation = (ChatConversation) redisTemplate.boundValueOps(k).get();

            if (conversation != null) {
                AiragApp app = conversation.getApp();
                if (null == app) {
                    continue;
                }
                String conversationAppId = app.getId();
                if (appId.equals(conversationAppId)) {
                    conversation.setApp(null);
                    conversation.setMessages(null);
                    conversations.add(conversation);
                }
            }
        }

        // 对会话列表按创建时间降序排序
        conversations.sort((o1, o2) -> {
            Date date1 = o1.getCreateTime();
            Date date2 = o2.getCreateTime();
            if (date1 == null && date2 == null) {
                return 0;
            }
            if (date1 == null) {
                return 1;
            }
            if (date2 == null) {
                return -1;
            }
            return date2.compareTo(date1);
        });

        // 返回结果
        return Result.ok(conversations);
    }

    @Override
    public Result<?> getMessages(String conversationId) {
        AssertUtils.assertNotEmpty("请先选择会话", conversationId);
        String key = getConversationCacheKey(conversationId, null);
        if (oConvertUtils.isEmpty(key)) {
            return Result.ok(Collections.emptyList());
        }
        ChatConversation chatConversation = (ChatConversation) redisTemplate.boundValueOps(key).get();
        if (oConvertUtils.isObjectEmpty(chatConversation)) {
            return Result.ok(Collections.emptyList());
        }
        return Result.ok(chatConversation.getMessages());
    }

    @Override
    public Result<?> clearMessage(String conversationId) {
        AssertUtils.assertNotEmpty("请先选择会话", conversationId);
        String key = getConversationCacheKey(conversationId, null);
        if (oConvertUtils.isEmpty(key)) {
            return Result.ok(Collections.emptyList());
        }
        ChatConversation chatConversation = (ChatConversation) redisTemplate.boundValueOps(key).get();
        if (null != chatConversation && oConvertUtils.isObjectNotEmpty(chatConversation.getMessages())) {
            chatConversation.getMessages().clear();
            saveChatConversation(chatConversation);
        }
        return Result.ok();
    }

    @Override
    public Result<?> deleteConversation(String conversationId) {
        AssertUtils.assertNotEmpty("请选择要删除的会话", conversationId);
        String key = getConversationCacheKey(conversationId, null);
        if (oConvertUtils.isNotEmpty(key)) {
            Boolean delete = redisTemplate.delete(key);
            if (delete) {
                return Result.ok();
            } else {
                return Result.error("删除会话失败");
            }
        }
        log.warn("[ai-chat]删除会话:未找到会话:{}", conversationId);
        return Result.ok();
    }

    @Override
    public Result<?> updateConversationTitle(ChatConversation updateTitleParams) {
        AssertUtils.assertNotEmpty("请先选择会话", updateTitleParams);
        AssertUtils.assertNotEmpty("请先选择会话", updateTitleParams.getId());
        AssertUtils.assertNotEmpty("请输入会话标题", updateTitleParams.getTitle());
        String key = getConversationCacheKey(updateTitleParams.getId(), null);
        if (oConvertUtils.isEmpty(key)) {
            log.warn("[ai-chat]删除会话:未找到会话:{}", updateTitleParams.getId());
            return Result.ok();
        }
        ChatConversation chatConversation = (ChatConversation) redisTemplate.boundValueOps(key).get();
        chatConversation.setTitle(updateTitleParams.getTitle());
        saveChatConversation(chatConversation);
        return Result.ok();
    }

    /**
     * 获取会话缓存key
     *
     * @param conversationId
     * @param httpRequest
     * @return
     * @author chenrui
     * @date 2025/2/25 19:27
     */
    private String getConversationCacheKey(String conversationId,HttpServletRequest httpRequest) {
        if (oConvertUtils.isEmpty(conversationId)) {
            return null;
        }
        String key = getConversationDirCacheKey(httpRequest);
        key = key + ":" + conversationId;
        return key;
    }

    /**
     * 获取当前用户会话的缓存目录
     *
     * @param httpRequest
     * @return
     * @author chenrui
     * @date 2025/2/26 15:09
     */
    private String getConversationDirCacheKey(HttpServletRequest httpRequest) {
        String username = getUsername(httpRequest);
        // 如果用户不存在,获取当前请求的sessionid
        if (oConvertUtils.isEmpty(username)) {
            try {
                if (null == httpRequest) {
                    httpRequest = SpringContextUtils.getHttpServletRequest();
                }
                username = httpRequest.getSession().getId();
            } catch (Exception e) {
                log.error("获取当前请求的sessionid失败", e);
            }
        }
        AssertUtils.assertNotEmpty("请先登录", username);
        return "airag:chat:" + username;
    }

    /**
     * 获取会话
     *
     * @param app
     * @param conversationId
     * @return
     * @author chenrui
     * @date 2025/2/25 19:19
     */
    @NotNull
    private ChatConversation getOrCreateChatConversation(AiragApp app, String conversationId) {
        if (oConvertUtils.isObjectEmpty(app)) {
            app = new AiragApp();
            app.setId(AiAppConsts.DEFAULT_APP_ID);
        }
        ChatConversation chatConversation = null;
        String key = getConversationCacheKey(conversationId, null);
        if (oConvertUtils.isNotEmpty(key)) {
            chatConversation = (ChatConversation) redisTemplate.boundValueOps(key).get();
        }
        if (null == chatConversation) {
            chatConversation = createConversation(conversationId);
        }
        chatConversation.setApp(app);
        return chatConversation;
    }

    /**
     * 创建新的会话
     *
     * @param conversationId
     * @return
     * @author chenrui
     * @date 2025/2/26 15:53
     */
    @NotNull
    private ChatConversation createConversation(String conversationId) {
        // 新会话
        conversationId = oConvertUtils.getString(conversationId, UUIDGenerator.generate());
        ChatConversation chatConversation = new ChatConversation();
        chatConversation.setId(conversationId);
        chatConversation.setCreateTime(new Date());
        return chatConversation;
    }

    /**
     * 保存会话
     *
     * @param chatConversation
     * @author chenrui
     * @date 2025/2/25 19:27
     */
    private void saveChatConversation(ChatConversation chatConversation) {
        saveChatConversation(chatConversation, false, null);
    }

    /**
     * 保存会话
     *
     * @param chatConversation
     * @param temp             是否临时会话
     * @author chenrui
     * @date 2025/2/25 19:27
     */
    private void saveChatConversation(ChatConversation chatConversation, boolean temp,HttpServletRequest httpRequest) {
        if (null == chatConversation) {
            return;
        }
        String key = getConversationCacheKey(chatConversation.getId(), httpRequest);
        if (oConvertUtils.isEmpty(key)) {
            return;
        }
        BoundValueOperations chatRedisCacheOp = redisTemplate.boundValueOps(key);
        chatRedisCacheOp.set(chatConversation);
        if (temp) {
            chatRedisCacheOp.expire(3, TimeUnit.HOURS);
        }
    }

    /**
     * 构造消息
     *
     * @param conversation
     * @param topicId
     * @return
     * @author chenrui
     * @date 2025/2/25 15:26
     */
    private List<ChatMessage> collateMessage(ChatConversation conversation, String topicId) {
        List<MessageHistory> messagesHistory = conversation.getMessages();
        if (oConvertUtils.isObjectEmpty(messagesHistory)) {
            return new LinkedList<>();
        }
        LinkedList<ChatMessage> chatMessages = new LinkedList<>();
        for (int i = messagesHistory.size() - 1; i >= 0; i--) {
            MessageHistory history = messagesHistory.get(i);
            if (topicId.equals(history.getTopicId())) {
                ChatMessage chatMessage = null;
                switch (history.getRole()) {
                    case AiragConsts.MESSAGE_ROLE_USER:
                        List<Content> contents = new ArrayList<>();
                        List<MessageHistory.ImageHistory> images = history.getImages();
                        if (oConvertUtils.isObjectNotEmpty(images)
                                && !images.isEmpty()) {
                            contents.addAll(images.stream().map(imageHistory -> {
                                if (oConvertUtils.isNotEmpty(imageHistory.getUrl())) {
                                    return ImageContent.from(imageHistory.getUrl());
                                } else {
                                    return ImageContent.from(imageHistory.getBase64Data(), imageHistory.getMimeType());
                                }
                            }).collect(Collectors.toList()));
                        }
                        contents.add(TextContent.from(history.getContent()));
                        chatMessage = UserMessage.from(contents);
                        break;
                    case AiragConsts.MESSAGE_ROLE_AI:
                        chatMessage = new AiMessage(history.getContent());
                        break;
                }
                if (null == chatMessage) {
                    continue;
                }
                chatMessages.addFirst(chatMessage);
            }
        }
        return chatMessages;
    }


    /**
     * 追加消息
     *
     * @param messages
     * @param message
     * @param chatConversation
     * @param topicId
     * @return
     * @author chenrui
     * @date 2025/2/25 19:05
     */
    private void appendMessage(List<ChatMessage> messages, ChatMessage message, ChatConversation
            chatConversation, String topicId) {

        if (message.type().equals(ChatMessageType.SYSTEM)) {
            // 系统消息,放到消息列表最前面,并且不记录历史
            messages.add(0, message);
            return;
        } else {
            messages.add(message);
        }
        List<MessageHistory> histories = chatConversation.getMessages();
        if (oConvertUtils.isObjectEmpty(histories)) {
            histories = new ArrayList<>();
        }
        // 消息记录
        MessageHistory historyMessage = MessageHistory.builder()
                .conversationId(chatConversation.getId())
                .topicId(topicId)
                .datetime(DateUtils.now())
                .build();
        if (message.type().equals(ChatMessageType.USER)) {
            historyMessage.setRole(AiragConsts.MESSAGE_ROLE_USER);
            StringBuilder textContent = new StringBuilder();
            List<MessageHistory.ImageHistory> images = new ArrayList<>();
            List<Content> contents = ((UserMessage) message).contents();
            contents.forEach(content -> {
                if (content.type().equals(ContentType.IMAGE)) {
                    ImageContent imageContent = (ImageContent) content;
                    Image image = imageContent.image();
                    MessageHistory.ImageHistory imageMessage = MessageHistory.ImageHistory.from(image.url(), image.base64Data(), image.mimeType());
                    images.add(imageMessage);
                } else if (content.type().equals(ContentType.TEXT)) {
                    textContent.append(((TextContent) content).text()).append("\n");
                }
            });
            historyMessage.setContent(textContent.toString());
            historyMessage.setImages(images);
        } else if (message.type().equals(ChatMessageType.AI)) {
            historyMessage.setRole(AiragConsts.MESSAGE_ROLE_AI);
            historyMessage.setContent(((AiMessage) message).text());
        }
        histories.add(historyMessage);
        chatConversation.setMessages(histories);
    }

    /**
     * 发送聊天消息
     *
     * @param chatConversation
     * @param topicId
     * @param sendParams
     * @return
     * @author chenrui
     * @date 2025/2/28 11:04
     */
    @NotNull
    private SseEmitter doChat(ChatConversation chatConversation, String topicId, ChatSendParams sendParams) {
        // 从历史消息中组装本次的消息列表
        List<ChatMessage> messages = collateMessage(chatConversation, topicId);

        AiragApp aiApp = chatConversation.getApp();
        // 每次会话都生成一个新的,用来缓存emitter
        String requestId = UUIDGenerator.generate();
        SseEmitter emitter = new SseEmitter(-0L);
        // 缓存emitter
        AiragLocalCache.put(AiragConsts.CACHE_TYPE_SSE, requestId, emitter);
        try {
            // 组装用户消息
            UserMessage userMessage = aiChatHandler.buildUserMessage(sendParams.getContent(), sendParams.getImages());
            // 追加消息
            appendMessage(messages, userMessage, chatConversation, topicId);
            /* 这里应该是有几种情况:
             * 1. 非ai应用:获取默认模型->开始聊天
             * 2. AI应用-聊天助手(ChatAssistant):从应用信息组装模型和提示词->开始聊天
             * 3. AI应用-聊天流程(ChatFlow):从应用信息获取模型,流程,组装入参->调用工作流
             */
            if (null != aiApp && !AiAppConsts.DEFAULT_APP_ID.equals(aiApp.getId())) {
                // ai应用:查询应用信息(ChatAssistant,chatflow),模型信息,组装模型-提示词,知识库等
                if (AiAppConsts.APP_TYPE_CHAT_FLOW.equals(aiApp.getType())) {
                    // ai应用:聊天流程(ChatFlow)
                    sendWithFlow(requestId, aiApp.getFlowId(), chatConversation, topicId, messages, sendParams);
                } else {
                    // AI应用-聊天助手(ChatAssistant):从应用信息组装模型和提示词
                    sendWithAppChat(requestId, messages, chatConversation, topicId);
                }
            } else {
                // 发消息
                sendWithDefault(requestId, chatConversation, topicId, null, messages, null);
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            EventData eventData = new EventData(requestId, null, EventData.EVENT_FLOW_ERROR, chatConversation.getId(), topicId);
            eventData.setData(EventFlowData.builder().success(false).message(e.getMessage()).build());
            closeSSE(emitter, eventData);
        }
        return emitter;
    }

    /**
     * 运行流程
     *
     * @param requestId
     * @param flowId
     * @param chatConversation
     * @param topicId
     * @param messages
     * @param sendParams
     * @author chenrui
     * @date 2025/2/27 14:55
     */
    private void sendWithFlow(String requestId, String flowId, ChatConversation chatConversation, String topicId, List<ChatMessage> messages, ChatSendParams sendParams) {
        FlowRunParams flowRunParams = new FlowRunParams();
        flowRunParams.setRequestId(requestId);
        flowRunParams.setFlowId(flowId);
        flowRunParams.setConversationId(chatConversation.getId());
        flowRunParams.setTopicId(topicId);
        // 支持流式
        flowRunParams.setResponseMode(FlowConsts.FLOW_RESPONSE_MODE_STREAMING);
        Map<String, Object> flowInputParams = new HashMap<>();
        List<MessageHistory> histories = new ArrayList<>();
        if (oConvertUtils.isObjectNotEmpty(chatConversation.getMessages())) {
            // 创建历史消息的副本(不直接操作原来的list)
            histories.addAll(chatConversation.getMessages());
            // 移除最后一条历史消息(最后一条是当前发出去的这一条消息)
            histories.remove(histories.size() - 1);
        }
        flowInputParams.put(FlowConsts.FLOW_INPUT_PARAM_HISTORY, histories);
        flowInputParams.put(FlowConsts.FLOW_INPUT_PARAM_QUESTION, sendParams.getContent());
        flowInputParams.put(FlowConsts.FLOW_INPUT_PARAM_IMAGES, sendParams.getImages());
        flowRunParams.setInputParams(flowInputParams);
        HttpServletRequest httpRequest = SpringContextUtils.getHttpServletRequest();
        flowRunParams.setHttpRequest(httpRequest);
        // 流程结束后,记录ai返回并保存会话
        // sse
        SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
        flowRunParams.setEventCallback(eventData -> {
            if (EventData.EVENT_FLOW_FINISHED.equals(eventData.getEvent())) {
                EventFlowData data = (EventFlowData) eventData.getData();
                Object outputs = data.getOutputs();
                if (oConvertUtils.isObjectNotEmpty(outputs)) {
                    AiMessage aiMessage;
                    if (outputs instanceof String) {
                        // 兼容推理模型
                        String messageText = String.valueOf(outputs);
                        messageText = messageText.replaceAll("<think>([\\s\\S]*?)</think>", "> $1");
                        aiMessage = new AiMessage(messageText);
                    } else {
                        aiMessage = new AiMessage(JSONObject.toJSONString(outputs));
                    }
                    EventData msgEventData = new EventData(requestId, null, EventData.EVENT_MESSAGE, chatConversation.getId(), topicId);
                    EventMessageData messageEventData = EventMessageData.builder()
                            .message(aiMessage.text())
                            .build();
                    msgEventData.setData(messageEventData);
                    try {
                        String eventStr = JSONObject.toJSONString(msgEventData);
                        log.debug("[AI应用]接收FLOW返回消息:{}", eventStr);
                        emitter.send(SseEmitter.event().data(eventStr));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    appendMessage(messages, aiMessage, chatConversation, topicId);
                    // 保存会话
                    saveChatConversation(chatConversation, false, httpRequest);
                }
            }
        });
        airagFlowService.runFlow(flowRunParams);
    }


    /**
     * 发送app聊天
     *
     * @param requestId
     * @param messages
     * @param chatConversation
     * @param topicId
     * @return
     * @author chenrui
     * @date 2025/2/28 10:41
     */
    private void sendWithAppChat(String requestId, List<ChatMessage> messages, ChatConversation chatConversation, String topicId) {
        AiragApp aiApp = chatConversation.getApp();
        String modelId = aiApp.getModelId();
        AssertUtils.assertNotEmpty("请先选择模型", modelId);
        // AI应用提示词
        String prompt = aiApp.getPrompt();
        if (oConvertUtils.isNotEmpty(prompt)) {
            appendMessage(messages, new SystemMessage(prompt), chatConversation, topicId);
        }

        AIChatParams aiChatParams = new AIChatParams();
        // AI应用自定义的模型参数
        String metadataStr = aiApp.getMetadata();
        if (oConvertUtils.isNotEmpty(metadataStr)) {
            JSONObject metadata = JSONObject.parseObject(metadataStr);
            if(oConvertUtils.isNotEmpty(metadata)){
                if (metadata.containsKey("temperature")) {
                    aiChatParams.setTemperature(metadata.getDouble("temperature"));
                }
                if (metadata.containsKey("topP")) {
                    aiChatParams.setTopP(metadata.getDouble("temperature"));
                }
                if (metadata.containsKey("presencePenalty")) {
                    aiChatParams.setPresencePenalty(metadata.getDouble("temperature"));
                }
                if (metadata.containsKey("frequencyPenalty")) {
                    aiChatParams.setFrequencyPenalty(metadata.getDouble("temperature"));
                }
                if (metadata.containsKey("maxTokens")) {
                    aiChatParams.setMaxTokens(metadata.getInteger("temperature"));
                }
            }
        }
        // 发消息
        sendWithDefault(requestId, chatConversation, topicId, modelId, messages, aiChatParams);
    }

    /**
     * 处理聊天
     * 向大模型发送消息并接受响应
     *
     * @param chatConversation
     * @param topicId
     * @param modelId
     * @param messages
     * @return
     * @author chenrui
     * @date 2025/2/25 19:24
     */
    private void sendWithDefault(String requestId, ChatConversation chatConversation, String topicId, String modelId,
                                 List<ChatMessage> messages,AIChatParams aiChatParams) {
        // 调用ai聊天
        if(null == aiChatParams){
            aiChatParams = new AIChatParams();
        }
        aiChatParams.setKnowIds(chatConversation.getApp().getKnowIds());
        aiChatParams.setMaxMsgNumber(oConvertUtils.getInt(chatConversation.getApp().getMsgNum(), 5));
        HttpServletRequest httpRequest = SpringContextUtils.getHttpServletRequest();
        TokenStream chatStream;
        try {
            if (oConvertUtils.isNotEmpty(modelId)) {
                chatStream = aiChatHandler.chat(modelId, messages, aiChatParams);
            } else {
                chatStream = aiChatHandler.chatByDefaultModel(messages, aiChatParams);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new JeecgBootBizTipException("调用大模型接口失败:" + e.getMessage());
        }
        /**
         * 是否正在思考
         */
        AtomicBoolean isThinking = new AtomicBoolean(false);
        // ai聊天响应逻辑
        chatStream.onNext((String resMessage) -> {
                    // 兼容推理模型
                    if ("<think>".equals(resMessage)) {
                        isThinking.set(true);
                        resMessage = "> ";
                    }
                    if ("</think>".equals(resMessage)) {
                        isThinking.set(false);
                        resMessage = "\n\n";
                    }
                    if (isThinking.get()) {
                        if (null != resMessage && resMessage.contains("\n")) {
                            resMessage = "\n> ";
                        }
                    }
                    EventData eventData = new EventData(requestId, null, EventData.EVENT_MESSAGE, chatConversation.getId(), topicId);
                    EventMessageData messageEventData = EventMessageData.builder()
                            .message(resMessage)
                            .build();
                    eventData.setData(messageEventData);
                    // sse
                    SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
                    if (null == emitter) {
                        log.warn("[AI应用]接收LLM返回会话已关闭");
                        return;
                    }
                    try {
                        String eventStr = JSONObject.toJSONString(eventData);
                        log.debug("[AI应用]接收LLM返回消息:{}", eventStr);
                        emitter.send(SseEmitter.event().data(eventStr));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onComplete((responseMessage) -> {
                    // 记录ai的回复
                    AiMessage aiMessage = responseMessage.content();
                    FinishReason finishReason = responseMessage.finishReason();
                    String respText = aiMessage.text();
                    // sse
                    SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
                    if (null == emitter) {
                        log.warn("[AI应用]接收LLM返回会话已关闭");
                        return;
                    }
                    if (FinishReason.STOP.equals(finishReason) || null == finishReason) {
                        // 正常结束
                        EventData eventData = new EventData(requestId, null, EventData.EVENT_MESSAGE_END, chatConversation.getId(), topicId);
                        try {
                            log.debug("[AI应用]接收LLM返回消息完成:{}", respText);
                            emitter.send(SseEmitter.event().data(eventData));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        appendMessage(messages, aiMessage, chatConversation, topicId);
                        // 保存会话
                        saveChatConversation(chatConversation,false,httpRequest);
                        closeSSE(emitter, eventData);
                    } else if (FinishReason.TOOL_EXECUTION.equals(finishReason)) {
                        // 需要执行工具
                        // TODO author: chenrui for: date:2025/3/7
                    } else {
                        // 异常结束
                        log.error("调用模型异常:" + respText);
                        if (respText.contains("insufficient Balance")) {
                            respText = "大预言模型账号余额不足!";
                        }
                        EventData eventData = new EventData(requestId, null, EventData.EVENT_FLOW_ERROR, chatConversation.getId(), topicId);
                        eventData.setData(EventFlowData.builder().success(false).message(respText).build());
                        closeSSE(emitter, eventData);
                    }
                })
                .onError((Throwable error) -> {
                    // sse
                    SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
                    if (null == emitter) {
                        log.warn("[AI应用]接收LLM返回会话已关闭");
                        return;
                    }
                    String errMsg = "调用大模型接口失败:" + error.getMessage();
                    log.error(errMsg, error);
                    EventData eventData = new EventData(requestId, null, EventData.EVENT_FLOW_ERROR, chatConversation.getId(), topicId);
                    eventData.setData(EventFlowData.builder().success(false).message(errMsg).build());
                    closeSSE(emitter, eventData);
                })
                .start();
    }

    /**
     * 发送聊天返回结果
     *
     * @author chenrui
     * @date 2025/2/28 11:05
     */
    private static class ChatResult {
        public final SseEmitter emitter;
        public final AiragModel chatModel;

        public ChatResult(SseEmitter emitter, AiragModel chatModel) {
            this.emitter = emitter;
            this.chatModel = chatModel;
        }
    }


    /**
     * 总结会话标题
     * 几个问题: <br/>
     * 1. 如果在发消息时同步总结会话标题,会导致接口很慢甚至超时.
     * 2. 但如果异步更新会话标题会导致消息记录丢失(不全)或者标题丢失,需要写很多逻辑去保证最终一致
     * so 暂时先不用AI更新会话标题. 后期如果需要单独再增加一个接口,由前端调用或者在第一次消息接收完成后再异步更新
     *
     * @param chatConversation
     * @param question
     * @param modelId
     * @return
     * @author chenrui
     * @date 2025/2/25 17:12
     */
    protected void summaryConversationTitle(ChatConversation chatConversation, String question, String modelId) {
        if (oConvertUtils.isEmpty(chatConversation.getId())) {
            return;
        }
        String key = getConversationCacheKey(chatConversation.getId(), null);
        if (oConvertUtils.isEmpty(key)) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            List<ChatMessage> messages = new LinkedList<>();
            String systemMsgStr = "根据用户的问题,总结会话标题.\n" +
                    "要求如下:\n" +
                    "1. 使用中文回答.\n" +
                    "2. 标题长度控制在5个汉字10个英文字符以内\n" +
                    "3. 直接回复会话标题,不要有其他任何无关描述\n" +
                    "4. 如果无法总结,回复不知道\n";
            messages.add(new SystemMessage(systemMsgStr));
            messages.add(new UserMessage(question));
            String summaryTitle;
            try {
                summaryTitle = aiChatHandler.completions(modelId, messages, null);
                log.info("总结会话完成{}", summaryTitle);
                if (summaryTitle.equalsIgnoreCase("不知道")) {
                    summaryTitle = "";
                }
            } catch (Exception e) {
                log.warn("AI总结会话失败" + e.getMessage(), e);
                summaryTitle = "";
            }
            // 更新会话标题
            ChatConversation cachedConversation = (ChatConversation) redisTemplate.boundValueOps(key).get();
            if (null == cachedConversation) {
                cachedConversation = chatConversation;
            }
            if (oConvertUtils.isEmpty(chatConversation.getTitle())) {
                // 再次判断标题是否为空,只有标题为空才更新
                if (oConvertUtils.isNotEmpty(summaryTitle)) {
                    cachedConversation.setTitle(summaryTitle);
                } else {
                    cachedConversation.setTitle(question.length() > 5 ? question.substring(0, 5) : question);
                }
                //保存会话
                saveChatConversation(cachedConversation);
            }
        });
    }

    /**
     * 获取用户名
     * @param httpRequest
     * @return
     * @author chenrui
     * @date 2025/3/27 15:05
     */
    private String getUsername(HttpServletRequest httpRequest) {
        try {
            TokenUtils.getTokenByRequest();
            String token;
            if(null != httpRequest){
                token = TokenUtils.getTokenByRequest(httpRequest);
            }else{
                token = TokenUtils.getTokenByRequest();
            }
            if (TokenUtils.verifyToken(token, sysBaseApi, redisUtil)) {
                return JwtUtil.getUsername(token);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}