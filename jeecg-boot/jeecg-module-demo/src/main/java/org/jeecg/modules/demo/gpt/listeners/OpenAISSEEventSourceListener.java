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

    public OpenAISSEEventSourceListener(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    public OpenAISSEEventSourceListener(String topicId,SseEmitter sseEmitter){
        this.topicId = topicId;
        this.sseEmitter = sseEmitter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        log.info("OpenAI建立sse连接...");
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {
        log.debug("OpenAI返回数据：{}", data);
        tokens += 1;
        if (data.equals("[DONE]")) {
            log.info("OpenAI返回数据结束了");
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
            sseEmitter.send(SseEmitter.event()
                    .id(this.topicId)
                    .data(completionResponse.getChoices().get(0).getDelta())
                    .reconnectTime(3000));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            eventSource.cancel();
        }
    }


    @Override
    public void onClosed(@NotNull EventSource eventSource) {
        log.info("流式输出返回值总共{}tokens", tokens() - 2);
        log.info("OpenAI关闭sse连接...");
    }


    @SneakyThrows
    @Override
    public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
        String errMsg = "";
        ResponseBody body = null == response ? null:response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", body.string(), t.getMessage());
            errMsg = body.string();
        } else {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t.getMessage());
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