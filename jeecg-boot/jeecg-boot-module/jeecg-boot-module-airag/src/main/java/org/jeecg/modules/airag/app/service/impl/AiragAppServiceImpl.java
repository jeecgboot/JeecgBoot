package org.jeecg.modules.airag.app.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.service.TokenStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootBizTipException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.app.consts.AiAppConsts;
import org.jeecg.modules.airag.app.consts.Prompts;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.app.mapper.AiragAppMapper;
import org.jeecg.modules.airag.app.service.IAiragAppService;
import org.jeecg.modules.airag.app.vo.AiArticleWriteVersionVo;
import org.jeecg.modules.airag.app.vo.AppVariableVo;
import org.jeecg.modules.airag.common.consts.AiragConsts;
import org.jeecg.modules.airag.common.handler.AIChatParams;
import org.jeecg.modules.airag.common.handler.IAIChatHandler;
import org.jeecg.modules.airag.common.utils.AiragLocalCache;
import org.jeecg.modules.airag.common.vo.event.EventData;
import org.jeecg.modules.airag.common.vo.event.EventFlowData;
import org.jeecg.modules.airag.common.vo.event.EventMessageData;
import org.jeecg.modules.airag.llm.entity.AiragKnowledge;
import org.jeecg.modules.airag.llm.service.IAiragKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description: AI应用
 * @Author: jeecg-boot
 * @Date: 2025-02-26
 * @Version: V1.0
 */
@Slf4j
@Service
public class AiragAppServiceImpl extends ServiceImpl<AiragAppMapper, AiragApp> implements IAiragAppService {

    @Autowired
    IAIChatHandler aiChatHandler;

    @Autowired
    private IAiragKnowledgeService airagKnowledgeService;
    
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Object generatePrompt(String prompt, boolean blocking) {
        AssertUtils.assertNotEmpty("请输入提示词", prompt);
        List<ChatMessage> messages = Arrays.asList(new SystemMessage(Prompts.GENERATE_LLM_PROMPT), new UserMessage(prompt));

        AIChatParams params = new AIChatParams();
        params.setTemperature(0.8);
        params.setTopP(0.9);
        params.setPresencePenalty(0.1);
        params.setFrequencyPenalty(0.1);
        if(blocking){
            String promptValue = aiChatHandler.completionsByDefaultModel(messages, params);
            if (promptValue == null || promptValue.isEmpty()) {
                return Result.error("生成失败");
            }
            return Result.OK("success", promptValue);
        }else{
            //update-begin---author:wangshuai---date:2026-01-08---for: 将流式输出单独抽出去，变量和记忆也需要---
            return startSseChat(messages, params);
            //update-end---author:wangshuai---date:2026-01-08---for: 将流式输出单独抽出去，变量和记忆也需要---
        }
    }

    //update-begin---author:wangshuai---date:2026-01-05---for:【QQYUN-14479】增加一个开启记忆的按钮。下面为提示词和记忆，将记忆提示词单独拆分---
    @Override
    public Object generateMemoryByAppId(String variables, String memoryId, boolean blocking) {
        if(oConvertUtils.isEmpty(variables) && oConvertUtils.isEmpty(memoryId)){
            throw new JeecgBootBizTipException("请先添加变量或者记忆后再次重试！");
        }
        // 构建变量描述
        StringBuilder variablesDesc = new StringBuilder();
        if (oConvertUtils.isNotEmpty(variables)) {
            List<AppVariableVo> variableList = JSONArray.parseArray(variables, AppVariableVo.class);
            if (variableList != null && !variableList.isEmpty()) {
                for (AppVariableVo var : variableList) {
                    if (var.getEnable() != null && !var.getEnable()) {
                        continue;
                    }
                    String name = var.getName();
                    if (oConvertUtils.isNotEmpty(var.getAction())) {
                        String action = var.getAction();
                        if (oConvertUtils.isNotEmpty(name)) {
                            try {
                                // 使用正则替换未被{{}}包裹的变量名
                                String regex = "(?<!\\{\\{)\\b" + Pattern.quote(name) + "\\b(?!\\}\\})";
                                action = action.replaceAll(regex, "{{" + name + "}}");
                            } catch (Exception e) {
                                log.warn("变量名替换异常: name={}", name, e);
                            }
                        }
                        variablesDesc.append(action).append("\n");
                    } else {
                        variablesDesc.append("- {{").append(name).append("}}");
                        if (oConvertUtils.isNotEmpty(var.getDescription())) {
                            variablesDesc.append(": ").append(var.getDescription());
                        }
                        variablesDesc.append("\n");
                    }
                }
            }
        }
        
        // 构建Prompt
        StringBuilder promptBuilder = new StringBuilder(Prompts.GENERATE_GUIDE_HEADER);
        if (!variablesDesc.isEmpty()) {
            promptBuilder.append(String.format(Prompts.GENERATE_VAR_PART, variablesDesc.toString()));
        }

        // 构建记忆状态描述
        if (oConvertUtils.isNotEmpty(memoryId)) {
            String memoryDescr = "";
            AiragKnowledge memory = airagKnowledgeService.getById(memoryId);
            if (memory != null && oConvertUtils.isNotEmpty(memory.getDescr())) {
                memoryDescr += "记忆库描述：" + memory.getDescr();
            }
            promptBuilder.append(String.format(Prompts.GENERATE_MEMORY_PART, memoryDescr));
        }

        String prompt = promptBuilder.toString();

        List<ChatMessage> messages = List.of(new UserMessage(prompt));

        AIChatParams params = new AIChatParams();
        params.setTemperature(0.7);

        if(blocking){
            String promptValue = aiChatHandler.completionsByDefaultModel(messages, params);
            if (promptValue == null || promptValue.isEmpty()) {
                return Result.error("生成失败");
            }
            return Result.OK("success", promptValue);
        }else{
            return startSseChat(messages, params);
        }
    }

    /**
     * 发送聊天
     * @param messages
     * @param params
     * @return
     */
    private SseEmitter startSseChat(List<ChatMessage> messages, AIChatParams params) {
        SseEmitter emitter = new SseEmitter(-0L);
        // 异步运行(流式)
        TokenStream tokenStream = aiChatHandler.chatByDefaultModel(messages, params);
        /**
         * 是否正在思考
         */
        AtomicBoolean isThinking = new AtomicBoolean(false);
        String requestId = UUIDGenerator.generate();
        // ai聊天响应逻辑
        tokenStream.onPartialResponse((String resMessage) -> {
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
                    EventData eventData = new EventData(requestId, null, EventData.EVENT_MESSAGE);
                    EventMessageData messageEventData = EventMessageData.builder()
                            .message(resMessage)
                            .build();
                    eventData.setData(messageEventData);
                    try {
                        String eventStr = JSONObject.toJSONString(eventData);
                        log.debug("[AI应用]接收LLM返回消息:{}", eventStr);
                        emitter.send(SseEmitter.event().data(eventStr));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onCompleteResponse((responseMessage) -> {
                    // 记录ai的回复
                    AiMessage aiMessage = responseMessage.aiMessage();
                    FinishReason finishReason = responseMessage.finishReason();
                    String respText = aiMessage.text();
                    if (FinishReason.STOP.equals(finishReason) || null == finishReason) {
                        // 正常结束
                        EventData eventData = new EventData(requestId, null, EventData.EVENT_MESSAGE_END);
                        try {
                            log.debug("[AI应用]接收LLM返回消息完成:{}", respText);
                            emitter.send(SseEmitter.event().data(eventData));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        closeSSE(emitter, eventData);
                    } else {
                        // 异常结束
                        log.error("调用模型异常:" + respText);
                        if (respText.contains("insufficient Balance")) {
                            respText = "大预言模型账号余额不足!";
                        }
                        EventData eventData = new EventData(requestId, null, EventData.EVENT_FLOW_ERROR);
                        eventData.setData(EventFlowData.builder().success(false).message(respText).build());
                        closeSSE(emitter, eventData);
                    }
                })
                .onError((Throwable error) -> {
                    // sse
                    String errMsg = "调用大模型接口失败:" + error.getMessage();
                    log.error(errMsg, error);
                    EventData eventData = new EventData(requestId, null, EventData.EVENT_FLOW_ERROR);
                    eventData.setData(EventFlowData.builder().success(false).message(errMsg).build());
                    closeSSE(emitter, eventData);
                })
                .start();
        return emitter;
    }
    //update-end---author:wangshuai---date:2026-01-05---for:【QQYUN-14479】增加一个开启记忆的按钮。下面为提示词和记忆，将记忆提示词单独拆分---

    private static void closeSSE(SseEmitter emitter, EventData eventData) {
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


    /**
     * 写作列表
     */
    @Override
    public List<AiArticleWriteVersionVo> listArticleWrite() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String redisKey = StrUtil.format(AiAppConsts.ARTICLE_WRITER_KEY, loginUser.getUsername());
        Object data = redisTemplate.opsForValue().get(redisKey);
        if (data == null) {
            return new ArrayList<>();
        }
        List<AiArticleWriteVersionVo> aiWriteViewVoList = (List<AiArticleWriteVersionVo>) data;
        Collections.reverse(aiWriteViewVoList);
        return aiWriteViewVoList;
    }

    /**
     * 写作报错
     */
    @Override
    public void saveArticleWrite(AiArticleWriteVersionVo aiWriteVersionVo) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String redisKey = StrUtil.format(AiAppConsts.ARTICLE_WRITER_KEY, loginUser.getUsername());
        //先查看redis中是否存在
        Object data = redisTemplate.opsForValue().get(redisKey);
        if(null != data){
            List<AiArticleWriteVersionVo> aiWriteVersionVos = (List<AiArticleWriteVersionVo>) data;
            aiWriteVersionVo.setVersion("V"+(aiWriteVersionVos.size() + 1));
            aiWriteVersionVos.add(aiWriteVersionVo);
            redisTemplate.opsForValue().set(redisKey, aiWriteVersionVos);
        }else{
            List<AiArticleWriteVersionVo> aiWriteVersionVos = new ArrayList<>();
            aiWriteVersionVo.setVersion("V1");
            aiWriteVersionVos.add(aiWriteVersionVo);
            redisTemplate.opsForValue().set(redisKey, aiWriteVersionVos);
        }
    }

    /**
     * 写作删除
     */
    @Override
    public void deleteArticleWrite(String version) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String redisKey = StrUtil.format(AiAppConsts.ARTICLE_WRITER_KEY, loginUser.getUsername());
        Object data = redisTemplate.opsForValue().get(redisKey);
        if (data == null) {
            return;
        }
        List<AiArticleWriteVersionVo> aiWriteVersionVos = (List<AiArticleWriteVersionVo>) data;
        if (aiWriteVersionVos.isEmpty()) {
            return;
        }
        List<AiArticleWriteVersionVo> newList = aiWriteVersionVos.stream()
                .filter(vo -> !version.equals(vo.getVersion()))
                .collect(Collectors.toList());
        if (newList.isEmpty()) {
            redisTemplate.delete(redisKey);
        } else {
            redisTemplate.opsForValue().set(redisKey, newList);
        }
    }
}
