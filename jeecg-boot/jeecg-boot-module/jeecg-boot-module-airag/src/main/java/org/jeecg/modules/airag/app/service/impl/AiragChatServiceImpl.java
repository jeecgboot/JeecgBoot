package org.jeecg.modules.airag.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.service.TokenStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.*;
import org.jeecg.modules.airag.app.consts.AiAppConsts;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.app.mapper.AiragAppMapper;
import org.jeecg.modules.airag.app.service.IAiragChatService;
import org.jeecg.modules.airag.app.vo.AppDebugParams;
import org.jeecg.modules.airag.app.vo.ChatConversation;
import org.jeecg.modules.airag.app.vo.ChatSendParams;
import org.jeecg.modules.airag.common.consts.AiragConsts;
import org.jeecg.modules.airag.common.handler.AIChatParams;
import org.jeecg.modules.airag.common.handler.IAIChatHandler;
import org.jeecg.modules.airag.common.utils.AiragLocalCache;
import org.jeecg.modules.airag.common.vo.LlmPlugin;
import org.jeecg.modules.airag.common.vo.MessageHistory;
import org.jeecg.modules.airag.common.vo.event.EventData;
import org.jeecg.modules.airag.common.vo.event.EventFlowData;
import org.jeecg.modules.airag.common.vo.event.EventMessageData;
import org.jeecg.modules.airag.flow.consts.FlowConsts;
import org.jeecg.modules.airag.flow.entity.AiragFlow;
import org.jeecg.modules.airag.flow.service.IAiragFlowService;
import org.jeecg.modules.airag.flow.vo.api.FlowRunParams;
import org.jeecg.modules.airag.llm.entity.AiragModel;
import org.jeecg.modules.airag.llm.handler.AIChatHandler;
import org.jeecg.modules.airag.llm.handler.JeecgToolsProvider;
import org.jeecg.modules.airag.llm.mapper.AiragModelMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
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
    AiragAppMapper airagAppMapper;

    @Autowired
    IAiragFlowService airagFlowService;

    @Autowired
    private ISysBaseAPI sysBaseApi;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    JeecgToolsProvider jeecgToolsProvider;

    @Autowired
    AiragModelMapper airagModelMapper;

    /**
     * 重新接收消息
     */
    private static final ExecutorService SSE_THREAD_POOL = Executors.newFixedThreadPool(10); // 最大10个线程

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
            app = airagAppMapper.getByIdIgnoreTenant(chatSendParams.getAppId());
        }
        ChatConversation chatConversation = getOrCreateChatConversation(app, conversationId);
        // 更新标题
        if (oConvertUtils.isEmpty(chatConversation.getTitle())) {
            chatConversation.setTitle(userMessage.length() > 5 ? userMessage.substring(0, 5) : userMessage);
        }
        //update-begin---author:chenrui ---date:20251106  for：[issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程------------
        // 保存工作流入参配置（如果有）
        if (oConvertUtils.isObjectNotEmpty(chatSendParams.getFlowInputs())) {
            chatConversation.setFlowInputs(chatSendParams.getFlowInputs());
        }
        //update-end---author:chenrui ---date:20251106  for：[issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程------------
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
        //update-begin---author:chenrui ---date:20251106  for：[issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程------------
        // 保存工作流入参配置（如果有）
        if (oConvertUtils.isObjectNotEmpty(appDebugParams.getFlowInputs())) {
            chatConversation.setFlowInputs(appDebugParams.getFlowInputs());
        }
        //update-end---author:chenrui ---date:20251106  for：[issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程------------
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
        } catch (Exception e) {
            if(!e.getMessage().contains("ResponseBodyEmitter has already completed")){
                log.error("终止会话时发生错误", e);
            }
            try {
                // 防止异常冒泡
                emitter.completeWithError(e);
            } catch (Exception ignore) {}
        } finally {
            // 从缓存中移除emitter
            AiragLocalCache.remove(AiragConsts.CACHE_TYPE_SSE, eventData.getRequestId());
            // 关闭emitter
            try {
                emitter.complete();
            } catch (Exception ignore) {}
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
        //update-begin---author:chenrui ---date:20251106  for：[issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程------------
        // 返回消息列表和会话设置信息
        Map<String, Object> result = new HashMap<>();
        // 过滤掉工具调用相关的消息（前端不需要展示）
        List<MessageHistory> messages = chatConversation.getMessages();
        if (oConvertUtils.isObjectNotEmpty(messages)) {
            messages = messages.stream()
                    .filter(msg -> !AiragConsts.MESSAGE_ROLE_TOOL.equals(msg.getRole()))
                    .map(msg -> {
                        // 克隆消息对象，移除工具执行请求信息（前端不需要）
                        MessageHistory displayMsg = MessageHistory.builder()
                                .conversationId(msg.getConversationId())
                                .topicId(msg.getTopicId())
                                .role(msg.getRole())
                                .content(msg.getContent())
                                .images(msg.getImages())
                                .datetime(msg.getDatetime())
                                .build();
                        // 不设置toolExecutionRequests和toolExecutionResult
                        return displayMsg;
                    })
                    .collect(Collectors.toList());
        }
        result.put("messages", messages);
        result.put("flowInputs", chatConversation.getFlowInputs());
        return Result.ok(result);
        //update-end---author:chenrui ---date:20251106  for：[issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程------------
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
    public Result<?> initChat(String appId) {
        AiragApp app = airagAppMapper.getByIdIgnoreTenant(appId);
        //update-begin---author:chenrui ---date:20251106  for：[issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程------------
        if(AiAppConsts.APP_TYPE_CHAT_FLOW.equalsIgnoreCase(app.getType())) {
            AiragFlow flow = airagFlowService.getById(app.getFlowId());
            String flowMetadata = flow.getMetadata();
            if(oConvertUtils.isNotEmpty(flowMetadata)) {
                JSONObject flowMetadataJson = JSONObject.parseObject(flowMetadata);
                JSONArray flowMetadataInputs = flowMetadataJson.getJSONArray(FlowConsts.FLOW_METADATA_INPUTS);
                if(oConvertUtils.isObjectNotEmpty(flowMetadataInputs)) {
                    String appMetadataStr = app.getMetadata();
                    JSONObject appMetadataJson;
                    if(oConvertUtils.isEmpty(appMetadataStr)){
                        appMetadataJson = new JSONObject();
                    } else {
                        appMetadataJson = JSONObject.parseObject(appMetadataStr);
                    }
                    appMetadataJson.put(AiAppConsts.APP_METADATA_FLOW_INPUTS, flowMetadataInputs);
                    app.setMetadata(appMetadataJson.toJSONString());
                }
            }
        }
        //update-end---author:chenrui ---date:20251106  for：[issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程------------
        
        //update-begin---author:chenrui ---date:202501XX  for：在initChat接口中返回模型供应商信息，避免前端多次调用模型查询接口------------
        // 如果应用有模型ID，查询模型信息并将供应商、类型、名称等信息添加到metadata中
        if (oConvertUtils.isNotEmpty(app.getModelId())) {
            AiragModel model = airagModelMapper.getByIdIgnoreTenant(app.getModelId());
            if (model != null) {
                String appMetadataStr = app.getMetadata();
                JSONObject appMetadataJson;
                if(oConvertUtils.isEmpty(appMetadataStr)){
                    appMetadataJson = new JSONObject();
                } else {
                    appMetadataJson = JSONObject.parseObject(appMetadataStr);
                }
                // 将模型信息添加到metadata中
                JSONObject modelInfo = new JSONObject();
                modelInfo.put("provider", model.getProvider());
                modelInfo.put("modelType", model.getModelType());
                modelInfo.put("modelName", model.getModelName());
                appMetadataJson.put("modelInfo", modelInfo);
                app.setMetadata(appMetadataJson.toJSONString());
            }
        }
        //update-end---author:chenrui ---date:202501XX  for：在initChat接口中返回模型供应商信息，避免前端多次调用模型查询接口------------
        
        return Result.ok(app);
    }

    @Override
    public SseEmitter receiveByRequestId(String requestId) {
        AssertUtils.assertNotEmpty("请选择会话",requestId);
        if(AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId) == null){
            return null;
        }
        List<EventData> datas = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE_HISTORY_MSG, requestId);
        if(null == datas){
            return null;
        }
        SseEmitter emitter = createSSE(requestId);
        // 120秒
        final long timeoutMillis = 120_000L;
        // 使用线程池提交任务
        SSE_THREAD_POOL.submit(() -> {
            int lastIndex = 0;
            long lastActiveTime = System.currentTimeMillis();
            try {
                while (true) {
                    if(lastIndex < datas.size()) {
                        try {
                            EventData eventData = datas.get(lastIndex++);
                            String eventStr = JSONObject.toJSONString(eventData);
                            log.debug("[AI应用]继续接收-接收LLM返回消息:{}", eventStr);
                            emitter.send(SseEmitter.event().data(eventStr));
                            // 有新消息，重置计时
                            lastActiveTime = System.currentTimeMillis();
                        } catch (IOException e) {
                            log.error("[AI应用]继续接收-发送消息失败");
                        }
                    } else {
                        // 没有新消息了
                        if (AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId) == null) {
                            // 主线程sse已经被移除,退出线程.
                            log.info("[AI应用]继续接收-SSE消息推送完成: {}", requestId);
                            break;
                        } else if (System.currentTimeMillis() - lastActiveTime > timeoutMillis) {
                            // 主线程未结束,等待超时,
                            log.warn("[AI应用]继续接收-等待消息更新超时，释放线程: {}", requestId);
                            break;
                        } else {
                            // 主线程未结束, 未超时, 休眠一会再查
                            log.warn("[AI应用]继续接收-等待消息更新: {}", requestId);
                            Thread.sleep(500);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("SSE消息推送异常", e);
            } finally {
                try {
                    // 发送完成事件
                    emitter.send(SseEmitter.event().data(new EventData(requestId, null, EventData.EVENT_MESSAGE_END)));
                } catch (Exception e) {
                    log.error("终止会话时发生错误", e);
                    try {
                        // 防止异常冒泡
                        emitter.completeWithError(e);
                    } catch (Exception ignore) {}
                } finally {
                    // 关闭emitter
                    try {
                        emitter.complete();
                    } catch (Exception ignore) {}
                }
            }
        });
        return emitter;
    }

    /**
     * 创建SSE
     * @param requestId
     * @return
     * @author chenrui
     * @date 2025/8/12 15:30
     */
    private static SseEmitter createSSE(String requestId) {
        SseEmitter emitter = new SseEmitter(-0L);
        emitter.onError(throwable -> {
            log.warn("SEE向客户端发送消息失败: {}", throwable.getMessage());
            AiragLocalCache.remove(AiragConsts.CACHE_TYPE_SSE, requestId);
            AiragLocalCache.remove(AiragConsts.CACHE_TYPE_SSE_SEND_TIME, requestId);
            try {
                emitter.complete();
            } catch (Exception ignore) {}
        });
        return emitter;
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
    private String getConversationCacheKey(String conversationId, HttpServletRequest httpRequest) {
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
    private void saveChatConversation(ChatConversation chatConversation, boolean temp, HttpServletRequest httpRequest) {
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
                        if (oConvertUtils.isObjectNotEmpty(images) && !images.isEmpty()) {
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
                        // 重建AI消息，包括工具执行请求
                        if (oConvertUtils.isObjectNotEmpty(history.getToolExecutionRequests())) {
                            // 有工具执行请求，重建带工具调用的AiMessage
                            List<ToolExecutionRequest> toolRequests = history.getToolExecutionRequests().stream()
                                    .map(toolReq -> ToolExecutionRequest.builder()
                                            .id(toolReq.getId())
                                            .name(toolReq.getName())
                                            .arguments(toolReq.getArguments())
                                            .build())
                                    .collect(Collectors.toList());
                            chatMessage = AiMessage.from(history.getContent(), toolRequests);
                        } else {
                            chatMessage = new AiMessage(history.getContent());
                        }
                        break;
                    case AiragConsts.MESSAGE_ROLE_TOOL:
                        // 重建工具执行结果消息
                        // 需要重建ToolExecutionRequest，第一个参数是request对象，第二个参数是result字符串
                        ToolExecutionRequest recreatedRequest = ToolExecutionRequest.builder()
                                .id(history.getContent()) // content字段存储的是工具执行的id
                                .name("unknown") // 工具名称在重建时不重要，因为主要用于AI理解结果
                                .arguments("{}")
                                .build();
                        chatMessage = ToolExecutionResultMessage.from(recreatedRequest, history.getToolExecutionResult());
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
    private void appendMessage(List<ChatMessage> messages, ChatMessage message, ChatConversation chatConversation, String topicId) {

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
        MessageHistory historyMessage = MessageHistory.builder().conversationId(chatConversation.getId()).topicId(topicId).datetime(DateUtils.now()).build();
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
            AiMessage aiMessage = (AiMessage) message;
            historyMessage.setContent(aiMessage.text());
            // 处理工具执行请求
            if (oConvertUtils.isObjectNotEmpty(aiMessage.toolExecutionRequests())) {
                List<MessageHistory.ToolExecutionRequestHistory> toolRequests = new ArrayList<>();
                for (ToolExecutionRequest request : aiMessage.toolExecutionRequests()) {
                    toolRequests.add(MessageHistory.ToolExecutionRequestHistory.from(
                            request.id(),
                            request.name(),
                            request.arguments()
                    ));
                }
                historyMessage.setToolExecutionRequests(toolRequests);
            }
        } else if (message.type().equals(ChatMessageType.TOOL_EXECUTION_RESULT)) {
            // 工具执行结果消息
            historyMessage.setRole(AiragConsts.MESSAGE_ROLE_TOOL);
            ToolExecutionResultMessage toolMessage = (ToolExecutionResultMessage) message;
            historyMessage.setContent(toolMessage.id());
            historyMessage.setToolExecutionResult(toolMessage.text());
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
        SseEmitter emitter = createSSE(requestId);
        // 缓存emitter
        AiragLocalCache.put(AiragConsts.CACHE_TYPE_SSE, requestId, emitter);
        // 缓存开始发送时间
        log.info("[AI-CHAT]开始发送消息,requestId:{}", requestId);
        AiragLocalCache.put(AiragConsts.CACHE_TYPE_SSE_SEND_TIME, requestId, System.currentTimeMillis());
        // 初始化历史消息缓存
        AiragLocalCache.put(AiragConsts.CACHE_TYPE_SSE_HISTORY_MSG, requestId, new CopyOnWriteArrayList<>());
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
                    sendWithAppChat(requestId, messages, chatConversation, topicId, sendParams);
                }
            } else {
                // 发消息
                AIChatParams aiChatParams = new AIChatParams();
                if (oConvertUtils.isObjectNotEmpty(sendParams.getEnableSearch())) {
                    aiChatParams.setEnableSearch(sendParams.getEnableSearch());
                }
                sendWithDefault(requestId, chatConversation, topicId, null, messages, aiChatParams);
            }
            // 发送就绪消息
            EventData eventRequestId = new EventData(requestId, null, EventData.EVENT_INIT_REQUEST_ID, chatConversation.getId(), topicId);
            eventRequestId.setData(EventMessageData.builder().message("").build());
            sendMessage2Client(emitter, eventRequestId);
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
        
        //update-begin---author:chenrui ---date:20251106  for：[issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程------------
        // 添加工作流的额外参数（从conversation的flowInputs中读取）
        if (oConvertUtils.isObjectNotEmpty(chatConversation.getFlowInputs())) {
            flowInputParams.putAll(chatConversation.getFlowInputs());
        }
        //update-end---author:chenrui ---date:20251106  for：[issues/8545]新建AI应用的时候只能选择没有自定义参数的AI流程------------
        
        flowRunParams.setInputParams(flowInputParams);
        HttpServletRequest httpRequest = SpringContextUtils.getHttpServletRequest();
        flowRunParams.setHttpRequest(httpRequest);
        // 流程结束后,记录ai返回并保存会话
        // sse
        SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
        flowRunParams.setEventCallback(eventData -> {
            if (EventData.EVENT_FLOW_FINISHED.equals(eventData.getEvent())) {
                // 打印耗时日志
                printChatDuration(requestId, "流程执行完毕");
                // 已经执行完了,删除时间缓存
                AiragLocalCache.remove(AiragConsts.CACHE_TYPE_SSE_SEND_TIME, requestId);
                EventFlowData data = (EventFlowData) eventData.getData();
                if(data.isSuccess()) {
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
                        EventMessageData messageEventData = EventMessageData.builder().message(aiMessage.text()).build();
                        msgEventData.setData(messageEventData);
                        msgEventData.setRequestId(requestId);
                        sendMessage2Client(emitter, msgEventData);
                        appendMessage(messages, aiMessage, chatConversation, topicId);
                        // 保存会话
                        saveChatConversation(chatConversation, false, httpRequest);
                    }
                }else{
                    //update-begin---author:chenrui ---date:20250425  for：[QQYUN-12203]AI 聊天，超时或者服务器报错，给个友好提示------------
                    // 失败
                    String message = data.getMessage();
                    if (message != null && message.contains(FlowConsts.FLOW_ERROR_MSG_LLM_TIMEOUT)) {
                        message = "当前用户较多，排队中，请稍后再试！";
                        EventData errEventData = new EventData(requestId, null, EventData.EVENT_MESSAGE, chatConversation.getId(), topicId);
                        errEventData.setData(EventMessageData.builder().message("\n" + message).build());
                        sendMessage2Client(emitter, errEventData);
                        errEventData = new EventData(requestId, null, EventData.EVENT_MESSAGE_END, chatConversation.getId(), topicId);
                        // 如果是超时,主动关闭SSE,防止流程切面中返回异常消息导致前端不能正常展示上面的{普通消息}.
                        closeSSE(emitter, errEventData);
                    }
                    //update-end---author:chenrui ---date:20250425  for：[QQYUN-12203]AI 聊天，超时或者服务器报错，给个友好提示------------
                }
            }
        });
        // 打印流程耗时日志
        printChatDuration(requestId, "开始执行流程");
        airagFlowService.runFlow(flowRunParams);
    }


    /**
     * 发送app聊天
     *
     * @param requestId
     * @param messages
     * @param chatConversation
     * @param topicId
     * @param sendParams
     * @return
     * @author chenrui
     * @date 2025/2/28 10:41
     */
    private void sendWithAppChat(String requestId, List<ChatMessage> messages, ChatConversation chatConversation, String topicId, ChatSendParams sendParams) {
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
            if (oConvertUtils.isNotEmpty(metadata)) {
                if (metadata.containsKey("temperature")) {
                    aiChatParams.setTemperature(metadata.getDouble("temperature"));
                }
                if (metadata.containsKey("topP")) {
                    aiChatParams.setTopP(metadata.getDouble("topP"));
                }
                if (metadata.containsKey("presencePenalty")) {
                    aiChatParams.setPresencePenalty(metadata.getDouble("presencePenalty"));
                }
                if (metadata.containsKey("frequencyPenalty")) {
                    aiChatParams.setFrequencyPenalty(metadata.getDouble("frequencyPenalty"));
                }
                if (metadata.containsKey("maxTokens")) {
                    aiChatParams.setMaxTokens(metadata.getInteger("maxTokens"));
                }
            }
        }

        // AI应用插件（支持MCP和自定义插件）
        String plugins = aiApp.getPlugins();
        if (oConvertUtils.isNotEmpty(plugins)) {
            List<String> pluginIds = new ArrayList<>();
            JSONArray pluginArray = JSONArray.parseArray(plugins);
            pluginArray.stream().filter(Objects::nonNull)
                    .map(o -> JSONObject.parseObject(o.toString(), LlmPlugin.class))
                    .forEach(plugin -> {
                        // 支持MCP和插件类型
                        if (plugin.getCategory().equals(AiragConsts.PLUGIN_CATEGORY_MCP) 
                                || plugin.getCategory().equals(AiragConsts.PLUGIN_CATEGORY_PLUGIN)) {
                            pluginIds.add(plugin.getPluginId());
                        }
                    });
            if (oConvertUtils.isNotEmpty(pluginIds)) {
                aiChatParams.setPluginIds(pluginIds);
            }
        }

        // 设置网络搜索参数（如果前端传递了）
        if (sendParams != null && oConvertUtils.isObjectNotEmpty(sendParams.getEnableSearch())) {
            aiChatParams.setEnableSearch(sendParams.getEnableSearch());
        }

        // 打印流程耗时日志
        printChatDuration(requestId, "构造应用自定义参数完成");
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
    private void sendWithDefault(String requestId, ChatConversation chatConversation, String topicId, String modelId, List<ChatMessage> messages, AIChatParams aiChatParams) {
        // 调用ai聊天
        if (null == aiChatParams) {
            aiChatParams = new AIChatParams();
        }
        // 如果是默认app,加载系统默认工具
        if(chatConversation.getApp().getId().equals(AiAppConsts.DEFAULT_APP_ID)){
            aiChatParams.setTools(jeecgToolsProvider.getDefaultTools());
        }
        aiChatParams.setKnowIds(chatConversation.getApp().getKnowIds());
        aiChatParams.setMaxMsgNumber(oConvertUtils.getInt(chatConversation.getApp().getMsgNum(), 5));
        aiChatParams.setCurrentHttpRequest(SpringContextUtils.getHttpServletRequest());
        aiChatParams.setReturnThinking(true);
        HttpServletRequest httpRequest = SpringContextUtils.getHttpServletRequest();
        TokenStream chatStream;
        try {
            // 打印流程耗时日志
            printChatDuration(requestId, "开始向LLM发送消息");
            if (oConvertUtils.isNotEmpty(modelId)) {
                chatStream = aiChatHandler.chat(modelId, messages, aiChatParams);
            } else {
                chatStream = aiChatHandler.chatByDefaultModel(messages, aiChatParams);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // sse
            SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
            if (null == emitter) {
                log.warn("[AI应用]接收LLM返回会话已关闭{}", requestId);
                return;
            }
            String errMsg = "调用大模型接口失败，详情请查看后台日志。";
            if(e instanceof JeecgBootException){
                errMsg = e.getMessage();
            }
            EventData eventData = new EventData(requestId, null, EventData.EVENT_FLOW_ERROR, chatConversation.getId(), topicId);
            eventData.setData(EventFlowData.builder().success(false).message(errMsg).build());
            closeSSE(emitter, eventData);
            throw new JeecgBootBizTipException("调用大模型接口失败:" + e.getMessage());
        }
        /**
         * 是否正在思考
         */
        AtomicBoolean isThinking = new AtomicBoolean(false);
        // ai聊天响应逻辑
        chatStream.onPartialResponse((String resMessage) -> {
            //update-begin---author:wangshuai---date:2025-11-07---for:[issues/8506]/[issues/8260]/[issues/8166]新增推理模型的支持---
            if(isThinking.get()){
                //思考过程结束
                this.sendThinkEnd(requestId, chatConversation, topicId);
                isThinking.set(false);
            }
            //update-end---author:wangshuai---date:2025-11-07---for:[issues/8506]/[issues/8260]/[issues/8166]新增推理模型的支持---
            EventData eventData = new EventData(requestId, null, EventData.EVENT_MESSAGE, chatConversation.getId(), topicId);
            EventMessageData messageEventData = EventMessageData.builder().message(resMessage).build();
            eventData.setData(messageEventData);
            eventData.setRequestId(requestId);
            // sse
            SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
            if (null == emitter) {
                log.warn("[AI应用]接收LLM返回会话已关闭");
                return;
            }
            sendMessage2Client(emitter, eventData);
        }).onToolExecuted((toolExecution) -> {
            // 打印工具执行结果
            log.debug("[AI应用]工具执行结果: toolName={}, toolId={}, result={}",
                    toolExecution.request().name(), 
                    toolExecution.request().id(), 
                    toolExecution.result());
            // 将工具执行结果存储到消息历史中
            ToolExecutionResultMessage toolResultMessage = ToolExecutionResultMessage.from(
                    toolExecution.request(),
                    toolExecution.result()
            );
            appendMessage(messages, toolResultMessage, chatConversation, topicId);
        }).onIntermediateResponse((chatResponse) -> {
            // 中间响应：包含tool_calls的AI消息
            AiMessage aiMessage = chatResponse.aiMessage();
            if (aiMessage != null && oConvertUtils.isObjectNotEmpty(aiMessage.toolExecutionRequests())) {
                // 保存包含工具调用请求的AI消息
                log.debug("[AI应用]保存包含工具调用的AI消息: toolCallsCount={}", aiMessage.toolExecutionRequests().size());
                appendMessage(messages, aiMessage, chatConversation, topicId);
            }
        }).onPartialThinking((partialThinking) -> {
            try {
                if (oConvertUtils.isEmpty(partialThinking)) {
                    return;
                }
                isThinking.set(true);
                String text = partialThinking.text();
                // 构造事件数据（EVENT_THINKING 以便前端统一处理）
                EventData thinkingEvent = new EventData(requestId, null, EventData.EVENT_THINKING, chatConversation.getId(), topicId);
                thinkingEvent.setData(EventMessageData.builder().message(text).build());
                thinkingEvent.setRequestId(requestId);
                // 获取当前缓存的 emitter
                SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
                if (null == emitter) {
                    log.warn("[AI应用]思考过程发送失败，SSE 已关闭: {}", requestId);
                    return;
                }
                // 发送给客户端并缓存历史
                sendMessage2Client(emitter, thinkingEvent);
            } catch (Exception e) {
                log.error("发送思考过程异常", e);
            }
        }).onCompleteResponse((responseMessage) -> {
            // 打印流程耗时日志
            printChatDuration(requestId, "LLM输出消息完成");
            AiragLocalCache.remove(AiragConsts.CACHE_TYPE_SSE_SEND_TIME, requestId);
            // 记录ai的回复
            AiMessage aiMessage = responseMessage.aiMessage();
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
                appendMessage(messages, aiMessage, chatConversation, topicId);
                // 保存会话
                saveChatConversation(chatConversation, false, httpRequest);
                closeSSE(emitter, eventData);
            } else if (FinishReason.LENGTH.equals(finishReason)) {
                // 上下文长度超过限制
                log.error("调用模型异常:上下文长度超过限制:{}", responseMessage.tokenUsage());
                EventData eventData = new EventData(requestId, null, EventData.EVENT_MESSAGE, chatConversation.getId(), topicId);
                eventData.setData(EventMessageData.builder().message("\n上下文长度超过限制，请调整模型最大Tokens").build());
                sendMessage2Client(emitter, eventData);
                eventData = new EventData(requestId, null, EventData.EVENT_MESSAGE_END, chatConversation.getId(), topicId);
                closeSSE(emitter, eventData);
            } else {
                // 异常结束
                log.error("调用模型异常:" + respText);
                if (respText.contains("insufficient Balance")) {
                    respText = "大语言模型账号余额不足!";
                }
                EventData eventData = new EventData(requestId, null, EventData.EVENT_FLOW_ERROR, chatConversation.getId(), topicId);
                eventData.setData(EventFlowData.builder().success(false).message(respText).build());
                closeSSE(emitter, eventData);
            }
        }).onError((Throwable error) -> {
            // 打印流程耗时日志
            printChatDuration(requestId, "LLM输出消息异常");
            AiragLocalCache.remove(AiragConsts.CACHE_TYPE_SSE_SEND_TIME, requestId);
            // sse
            SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
            if (null == emitter) {
                log.warn("[AI应用]接收LLM返回会话已关闭{}", requestId);
                return;
            }
            log.error(error.getMessage(), error);
            String errMsg = error.getMessage();
            if (errMsg != null && errMsg.contains("timeout")) {
                //update-begin---author:chenrui ---date:20250425  for：[QQYUN-12203]AI 聊天，超时或者服务器报错，给个友好提示------------
                errMsg = "当前用户较多，排队中，请稍后再试！";
                EventData eventData = new EventData(requestId, null, EventData.EVENT_MESSAGE, chatConversation.getId(), topicId);
                eventData.setData(EventMessageData.builder().message("\n" + errMsg).build());
                sendMessage2Client(emitter, eventData);
                eventData = new EventData(requestId, null, EventData.EVENT_MESSAGE_END, chatConversation.getId(), topicId);
                closeSSE(emitter, eventData);
                //update-end---author:chenrui ---date:20250425  for：[QQYUN-12203]AI 聊天，超时或者服务器报错，给个友好提示------------
            } else {
                errMsg = "调用大模型接口失败，详情请查看后台日志。";
                // 根据常见异常关键字做细致翻译
                for (Map.Entry<String, String> entry : AIChatHandler.MODEL_ERROR_MAP.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (error.getMessage().contains(key)) {
                        errMsg = value;
                    }
                }
                EventData eventData = new EventData(requestId, null, EventData.EVENT_FLOW_ERROR, chatConversation.getId(), topicId);
                eventData.setData(EventFlowData.builder().success(false).message(errMsg).build());
                closeSSE(emitter, eventData);
            }
        }).start();
    }

    /**
     * 发送思考过程结束
     * 
     * @param requestId
     * @param chatConversation
     * @param topicId
     */
    private void sendThinkEnd(String requestId, ChatConversation chatConversation, String topicId) {
        EventData eventData = new EventData(requestId, null, EventData.EVENT_THINKING_END, chatConversation.getId(), topicId);
        EventMessageData messageEventData = EventMessageData.builder().message("").build();
        eventData.setData(messageEventData);
        eventData.setRequestId(requestId);
        SseEmitter emitter = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE, requestId);
        if (null == emitter) {
            log.warn("[AI应用]接收LLM返回会话已关闭");
            return;
        }
        sendMessage2Client(emitter, eventData);
    }

    /**
     * 发送消息到客户端
     *
     * @param emitter
     * @param eventData
     * @author chenrui
     * @date 2025/4/22 19:58
     */
    private static void sendMessage2Client(SseEmitter emitter, EventData eventData) {
        try {
            log.info("发送消息:{}", eventData.getRequestId());
            String eventStr = JSONObject.toJSONString(eventData);
            log.debug("[AI应用]接收LLM返回消息:{}", eventStr);
            emitter.send(SseEmitter.event().data(eventStr));
            List<EventData> historyMsg = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE_HISTORY_MSG, eventData.getRequestId());
            if (null == historyMsg) {
                historyMsg = new CopyOnWriteArrayList<>();
                AiragLocalCache.put(AiragConsts.CACHE_TYPE_SSE_HISTORY_MSG, eventData.getRequestId(), historyMsg);
            }
            historyMsg.add(eventData);
        } catch (IOException e) {
            log.error("发送消息失败", e);
        }
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
            String systemMsgStr = "根据用户的问题,总结会话标题.\n" + "要求如下:\n" + "1. 使用中文回答.\n" + "2. 标题长度控制在5个汉字10个英文字符以内\n" + "3. 直接回复会话标题,不要有其他任何无关描述\n" + "4. 如果无法总结,回复不知道\n";
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
     *
     * @param httpRequest
     * @return
     * @author chenrui
     * @date 2025/3/27 15:05
     */
    private String getUsername(HttpServletRequest httpRequest) {
        try {
            TokenUtils.getTokenByRequest();
            String token;
            if (null != httpRequest) {
                token = TokenUtils.getTokenByRequest(httpRequest);
            } else {
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


    /**
     * 打印耗时
     * @param requestId
     * @param message
     * @author chenrui
     * @date 2025/4/28 15:15
     */
    private static void printChatDuration(String requestId,String message) {
        Long beginTime = AiragLocalCache.get(AiragConsts.CACHE_TYPE_SSE_SEND_TIME, requestId);
        if (null != beginTime) {
            log.info("[AI-CHAT]{},requestId:{},耗时:{}s", message, requestId, (System.currentTimeMillis() - beginTime) / 1000);
        }
    }
}