package org.jeecg.modules.demo.gpt.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Objects;
import java.util.function.Consumer;

//update-begin---author:chenrui ---date:20240126  for：【QQYUN-7932】AI助手------------
/**
 * OpenAI的SSE监听
 * @author chenrui
 * @date 2024/1/26 20:06
 */
@Slf4j
public class OpenAISSEEventSourceListener extends EventSourceListener {

    private long tokens;

    private SseEmitter sseEmitter;

    private String topicId;
    /**
     * 回复消息内容
     */
    private String messageContent = "";

    /**
     * 完成回复回调
     */
    private Consumer<String> doneCallback;

    /**
     * 是否正在思考
     */
    private boolean isThinking = false;

    public OpenAISSEEventSourceListener(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    public OpenAISSEEventSourceListener(String topicId,SseEmitter sseEmitter){
        this.topicId = topicId;
        this.sseEmitter = sseEmitter;
    }

    /**
     * 设置消息完成响应时的回调
     * for [QQYUN-11102/QQYUN-11109]兼容deepseek模型,支持tink标签
     * @param doneCallback
     * @author chenrui
     * @date 2025/2/7 18:14
     */
    public void onDone(Consumer<String> doneCallback){
        this.doneCallback = doneCallback;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        log.info("ai-chat建立sse连接...");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {
        log.debug("ai-chat返回数据：{}", data);
        tokens += 1;
        if (data.equals("[DONE]")) {
            log.info("ai-chat返回数据结束了");
            this.doneCallback.accept(messageContent);
            messageContent = "";
            sseEmitter.send(SseEmitter.event()
                    .id("[TOKENS]")
                    .data("<br/><br/>tokens：" + tokens())
                    .reconnectTime(3000));
            sseEmitter.send(SseEmitter.event()
                    .id("[DONE]")
                    .data("[DONE]")
                    .reconnectTime(3000));
            // 传输完成后自动关闭sse
            sseEmitter.complete();
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ChatCompletionResponse completionResponse = mapper.readValue(data, ChatCompletionResponse.class); // 读取Json
        try {
            //update-begin---author:chenrui ---date:20250207  for：[QQYUN-11102/QQYUN-11109]兼容deepseek模型,支持think标签------------
            // 兼容think标签
            Message delta = completionResponse.getChoices().get(0).getDelta();
            if (null != delta) {
                String content = delta.getContent();
                if("<think>".equals(content)){
                    isThinking = true;
                    content = "> ";
                    delta.setContent(content);
                }
                if("</think>".equals(content)){
                    isThinking = false;
                    content = "\n\n";
                    delta.setContent(content);
                }
                if (isThinking) {
                    if (null != content && content.contains("\n")) {
                        content = "\n> ";
                        delta.setContent(content);
                    }
                }else {
                    // 响应消息体不记录思考过程
                    messageContent += null == content ? "" : content;
                }



                log.info("ai-chat返回数据,发送给前端:" + content);
                sseEmitter.send(SseEmitter.event()
                        .id(this.topicId)
                        .data(delta)
                        .reconnectTime(3000));
            }
            //update-end---author:chenrui ---date:20250207  for：[QQYUN-11102/QQYUN-11109]兼容deepseek模型,支持think标签------------
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            eventSource.cancel();
        }
    }


    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        log.info("流式输出返回值总共{}tokens", tokens() - 2);
        log.info("ai-chat关闭sse连接...");
    }


    @SneakyThrows
    @Override
    public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
        String errMsg = "";
        ResponseBody body = null == response ? null:response.body();
        if (Objects.nonNull(body)) {
            log.error("ai-chat  sse连接异常data：{}，异常：{}", body.string(), t.getMessage());
            errMsg = body.string();
        } else {
            log.error("ai-chat  sse连接异常data：{}，异常：{}", response, t.getMessage());
            errMsg = t.getMessage();
        }
        eventSource.cancel();
        sseEmitter.send(SseEmitter.event()
                .id("[ERR]")
                .data(Message.builder().content(explainErr(errMsg)).build())
                .reconnectTime(3000));
        sseEmitter.send(SseEmitter.event()
                .id("[DONE]")
                .data("[DONE]")
                .reconnectTime(3000));
        sseEmitter.complete();
    }

    private String explainErr(String errMsg){
        if(StringUtils.isEmpty(errMsg)){
            return "";
        }
        if(errMsg.contains("Rate limit")){
            return "请求频率太快了,请等待20秒再试.";
        }
        return errMsg;
    }

    /**
     * tokens
     * @return
     */
    public long tokens() {
        return tokens;
    }
}

//update-end---author:chenrui ---date:20240126  for：【QQYUN-7932】AI助手------------