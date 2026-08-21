package org.jeecg.modules.airag.voice.util;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.config.AiChatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;


/**
* @Description: <p>统一封装 语音 HTTP API 调用逻辑，供语音模块和视频模块复用</p>
* 
* @author: wangshuai
* @date: 2026/3/13 16:19
*/
@Slf4j
@Component
public class VoiceApiHelper {

    @Autowired
    private AiChatConfig aiChatConfig;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    /**
     * 调用 TTS API 生成语音文件（使用yml默认voice和speed）
     *
     * @param text      要转换的文本
     * @param audioPath 音频输出路径
     */
    public void generateAudio(String text, Path audioPath) throws IOException, InterruptedException {
        AiChatConfig.VoiceModelConfig config = aiChatConfig.getAiModelVoice();
        generateAudio(text, audioPath, config.getVoice(), config.getSpeed());
    }

    /**
     * 调用 TTS API 生成语音文件（自定义voice和speed）
     *
     * @param text      要转换的文本
     * @param audioPath 音频输出路径
     * @param voice     声色
     * @param speed     语速
     */
    public void generateAudio(String text, Path audioPath, String voice, double speed) throws IOException, InterruptedException {
        AiChatConfig.VoiceModelConfig config = aiChatConfig.getAiModelVoice();
        if (MiniMaxVoiceApi.isProvider(config.getProvider())) {
            generateAudioByMiniMax(text, audioPath, voice, speed, config);
            return;
        }
        String apiHost = config.getApiHost();
        String url = apiHost.endsWith("/") ? apiHost + "audio/speech" : apiHost + "/audio/speech";

        JSONObject body = new JSONObject();
        body.put("model", config.getModel());
        body.put("input", text);
        body.put("voice", voice);
        body.put("speed", speed);
        body.put("response_format", "wav");

        log.info("TTS请求: url={}, model={}, voice={}, speed={}, textLength={}", url, config.getModel(), voice, speed, text.length());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + config.getApiKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toJSONString()))
                .timeout(Duration.ofSeconds(config.getTimeout()))
                .build();

        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() != 200) {
            String errorBody = new String(response.body().readAllBytes());
            log.error("TTS API调用失败: status={}, body={}", response.statusCode(), errorBody);
            throw new RuntimeException("TTS API调用失败，状态码: " + response.statusCode() + "，" + errorBody);
        }

        try (InputStream is = response.body()) {
            Files.copy(is, audioPath, StandardCopyOption.REPLACE_EXISTING);
        }
        log.info("TTS语音已生成: {}", audioPath);
    }

    /**
     * Generate a speech file with the MiniMax text to audio operation, which answers with a json
     * body carrying the audio as a hex string instead of streaming the audio bytes
     *
     * @param text      text to convert
     * @param audioPath audio output path
     * @param voice     voice id
     * @param speed     speech speed
     * @param config    speech model configuration
     */
    private void generateAudioByMiniMax(String text, Path audioPath, String voice, double speed,
                                        AiChatConfig.VoiceModelConfig config) throws IOException, InterruptedException {
        String url = MiniMaxVoiceApi.resolveEndpoint(config.getApiHost());
        String model = MiniMaxVoiceApi.resolveModel(config.getModel());
        if (!MiniMaxVoiceApi.isSupportedModel(model)) {
            log.warn("Unknown speech model {}, supported models are: {}", model,
                    MiniMaxVoiceApi.supportedModelsDescription());
        }
        JSONObject body = MiniMaxVoiceApi.buildRequestBody(model, text, voice, speed, config.getVolume(), null);

        log.info("Speech request: url={}, model={}, voice={}, speed={}, textLength={}", url, model, voice, speed,
                text.length());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + config.getApiKey())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toJSONString(), StandardCharsets.UTF_8))
                .timeout(Duration.ofSeconds(config.getTimeout()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) {
            log.error("Speech api call failed: status={}, body={}", response.statusCode(), response.body());
            throw new RuntimeException("Speech api call failed, status code: " + response.statusCode() + ", "
                    + response.body());
        }

        Files.write(audioPath, MiniMaxVoiceApi.extractAudio(response.body()));
        log.info("Speech file generated: {}", audioPath);
    }
}
