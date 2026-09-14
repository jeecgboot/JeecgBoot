package org.jeecg.modules.airag.voice.util;

import com.alibaba.fastjson2.JSONObject;
import com.sun.net.httpserver.HttpServer;
import org.jeecg.config.AiChatConfig;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Description: <p>Tests for the MiniMax speech generation support</p>
 *
 * @author: octo-patch
 * @date: 2026/8/17
 */
class MiniMaxVoiceApiTest {

    @Test
    void detectsTheProviderCaseInsensitively() {
        assertTrue(MiniMaxVoiceApi.isProvider("MINIMAX"));
        assertTrue(MiniMaxVoiceApi.isProvider("minimax"));
        assertTrue(MiniMaxVoiceApi.isProvider(" MiniMax "));
        assertFalse(MiniMaxVoiceApi.isProvider("ZHIPU"));
        assertFalse(MiniMaxVoiceApi.isProvider(null));
    }

    @Test
    void exposesTheRegionalEndpoints() {
        Map<String, String> endpoints = MiniMaxVoiceApi.regionEndpoints();
        assertEquals(2, endpoints.size());
        assertEquals("https://api.minimax.io/v1/t2a_v2", endpoints.get("global_en"));
        assertEquals("https://api.minimaxi.com/v1/t2a_v2", endpoints.get("cn_zh"));
    }

    @Test
    void buildsTheOperationUrlFromEveryConfiguredApiHostShape() {
        assertEquals("https://api.minimax.io/v1/t2a_v2", MiniMaxVoiceApi.resolveEndpoint("https://api.minimax.io"));
        assertEquals("https://api.minimaxi.com/v1/t2a_v2", MiniMaxVoiceApi.resolveEndpoint("https://api.minimaxi.com/"));
        assertEquals("https://api.minimax.io/v1/t2a_v2", MiniMaxVoiceApi.resolveEndpoint("https://api.minimax.io/v1"));
        assertEquals("https://api.minimaxi.com/v1/t2a_v2", MiniMaxVoiceApi.resolveEndpoint("https://api.minimaxi.com/v1/"));
        assertEquals("https://api.minimax.io/v1/t2a_v2", MiniMaxVoiceApi.resolveEndpoint("https://api.minimax.io/v1/t2a_v2"));
    }

    @Test
    void rejectsAMissingApiHost() {
        assertThrows(IllegalArgumentException.class, () -> MiniMaxVoiceApi.resolveEndpoint(null));
        assertThrows(IllegalArgumentException.class, () -> MiniMaxVoiceApi.resolveEndpoint("  "));
        assertThrows(IllegalArgumentException.class, () -> MiniMaxVoiceApi.resolveEndpoint("/"));
    }

    @Test
    void resolvesTheModelAndKnowsTheSupportedModels() {
        assertEquals("speech-2.8-hd", MiniMaxVoiceApi.DEFAULT_MODEL);
        assertEquals(MiniMaxVoiceApi.DEFAULT_MODEL, MiniMaxVoiceApi.resolveModel(null));
        assertEquals(MiniMaxVoiceApi.DEFAULT_MODEL, MiniMaxVoiceApi.resolveModel(" "));
        assertEquals("speech-01-turbo", MiniMaxVoiceApi.resolveModel(" speech-01-turbo "));
        assertTrue(MiniMaxVoiceApi.isSupportedModel(MiniMaxVoiceApi.DEFAULT_MODEL));
        assertFalse(MiniMaxVoiceApi.isSupportedModel("glm-tts"));
        assertEquals(8, MiniMaxVoiceApi.supportedModels().size());
        assertTrue(MiniMaxVoiceApi.supportedModels().containsAll(java.util.List.of(
                "speech-2.8-hd", "speech-2.8-turbo", "speech-2.6-hd", "speech-2.6-turbo",
                "speech-02-hd", "speech-02-turbo", "speech-01-hd", "speech-01-turbo")));
    }

    @Test
    void buildsTheDocumentedRequestFields() {
        JSONObject body = MiniMaxVoiceApi.buildRequestBody("speech-2.6-hd", "hello", "female-1", 1.0, 0.0, null);

        assertEquals("speech-2.6-hd", body.getString("model"));
        assertEquals("hello", body.getString("text"));
        assertFalse(body.getBoolean("stream"));
        assertEquals("hex", body.getString("output_format"));

        JSONObject voiceSetting = body.getJSONObject("voice_setting");
        assertEquals("female-1", voiceSetting.getString("voice_id"));
        assertEquals(1.0, voiceSetting.getDoubleValue("speed"));
        assertEquals(1.0, voiceSetting.getDoubleValue("vol"));

        assertEquals("wav", body.getJSONObject("audio_setting").getString("format"));
    }

    @Test
    void keepsOptionalRequestFieldsOutOfTheBody() {
        JSONObject body = MiniMaxVoiceApi.buildRequestBody(null, "hello", "  ", 1.0, 0.0, null);

        assertEquals(MiniMaxVoiceApi.DEFAULT_MODEL, body.getString("model"));
        assertNull(body.getJSONObject("voice_setting").getString("voice_id"));
        assertNull(body.getString("language_boost"));
        assertNull(body.getString("pronunciation_dict"));
        assertNull(body.getString("voice_modify"));
        assertNull(body.getString("subtitle_enable"));
    }

    @Test
    void rejectsEmptyText() {
        assertThrows(IllegalArgumentException.class,
                () -> MiniMaxVoiceApi.buildRequestBody(null, " ", "female-1", 1.0, 0.0, null));
    }

    @Test
    void clampsSpeedAndConvertsVolumeGain() {
        assertEquals(0.5, MiniMaxVoiceApi.resolveSpeed(0.25));
        assertEquals(2.0, MiniMaxVoiceApi.resolveSpeed(4.0));
        assertEquals(1.5, MiniMaxVoiceApi.resolveSpeed(1.5));
        assertEquals(1.0, MiniMaxVoiceApi.resolveSpeed(0));

        assertEquals(1.0, MiniMaxVoiceApi.resolveVolume(0.0));
        assertTrue(MiniMaxVoiceApi.resolveVolume(6.0) > 1.0);
        assertTrue(MiniMaxVoiceApi.resolveVolume(-10.0) < 1.0);
        assertEquals(10.0, MiniMaxVoiceApi.resolveVolume(60.0));
        assertEquals(0.1, MiniMaxVoiceApi.resolveVolume(-60.0));
    }

    @Test
    void resolvesTheSupportedAudioFormats() {
        assertEquals("mp3", MiniMaxVoiceApi.resolveAudioFormat("mp3"));
        assertEquals("flac", MiniMaxVoiceApi.resolveAudioFormat("FLAC"));
        assertEquals("pcm", MiniMaxVoiceApi.resolveAudioFormat("pcm"));
        assertEquals("wav", MiniMaxVoiceApi.resolveAudioFormat(null));
        assertEquals("wav", MiniMaxVoiceApi.resolveAudioFormat("ogg"));
    }

    @Test
    void decodesTheHexAudioPayload() {
        byte[] audio = MiniMaxVoiceApi.extractAudio(
                "{\"data\":{\"audio\":\"52494646\",\"status\":2},\"base_resp\":{\"status_code\":0,\"status_msg\":\"success\"}}");
        assertArrayEquals(new byte[]{0x52, 0x49, 0x46, 0x46}, audio);
    }

    @Test
    void reportsAFailedStatusCode() {
        RuntimeException error = assertThrows(RuntimeException.class, () -> MiniMaxVoiceApi.extractAudio(
                "{\"base_resp\":{\"status_code\":1004,\"status_msg\":\"invalid api key\"}}"));
        assertTrue(error.getMessage().contains("1004"));
        assertTrue(error.getMessage().contains("invalid api key"));
    }

    @Test
    void reportsAMissingOrBrokenAudioPayload() {
        assertThrows(RuntimeException.class,
                () -> MiniMaxVoiceApi.extractAudio("{\"data\":{\"status\":1},\"base_resp\":{\"status_code\":0}}"));
        assertThrows(RuntimeException.class, () -> MiniMaxVoiceApi.extractAudio(""));
        assertThrows(RuntimeException.class, () -> MiniMaxVoiceApi.extractAudio("not json"));
        assertThrows(RuntimeException.class,
                () -> MiniMaxVoiceApi.extractAudio("{\"data\":{\"audio\":\"52494\"}}"));
        assertThrows(RuntimeException.class,
                () -> MiniMaxVoiceApi.extractAudio("{\"data\":{\"audio\":\"zzzz\"}}"));
    }

    @Test
    void writesTheDecodedAudioReturnedByTheOperation() throws Exception {
        AtomicReference<String> requestPath = new AtomicReference<>();
        AtomicReference<String> authorization = new AtomicReference<>();
        AtomicReference<String> requestBody = new AtomicReference<>();

        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            requestPath.set(exchange.getRequestURI().getPath());
            authorization.set(exchange.getRequestHeaders().getFirst("Authorization"));
            requestBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            byte[] response = ("{\"data\":{\"audio\":\"52494646\",\"status\":2},"
                    + "\"base_resp\":{\"status_code\":0,\"status_msg\":\"success\"}}")
                    .getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream body = exchange.getResponseBody()) {
                body.write(response);
            }
        });
        server.start();

        Path audioPath = Files.createTempFile("speech-", ".wav");
        try {
            AiChatConfig aiChatConfig = new AiChatConfig();
            AiChatConfig.VoiceModelConfig voiceConfig = aiChatConfig.getAiModelVoice();
            voiceConfig.setProvider(MiniMaxVoiceApi.PROVIDER);
            voiceConfig.setModel("speech-2.8-hd");
            voiceConfig.setApiKey("test-key");
            voiceConfig.setApiHost("http://127.0.0.1:" + server.getAddress().getPort());
            voiceConfig.setTimeout(30);

            VoiceApiHelper helper = new VoiceApiHelper();
            Field configField = VoiceApiHelper.class.getDeclaredField("aiChatConfig");
            configField.setAccessible(true);
            configField.set(helper, aiChatConfig);

            helper.generateAudio("hello", audioPath, "female-1", 1.0);

            assertEquals("/v1/t2a_v2", requestPath.get());
            assertEquals("Bearer test-key", authorization.get());
            assertArrayEquals(new byte[]{0x52, 0x49, 0x46, 0x46}, Files.readAllBytes(audioPath));

            JSONObject sentBody = JSONObject.parseObject(requestBody.get());
            assertEquals("speech-2.8-hd", sentBody.getString("model"));
            assertEquals("hello", sentBody.getString("text"));
            assertEquals("hex", sentBody.getString("output_format"));
            assertEquals("female-1", sentBody.getJSONObject("voice_setting").getString("voice_id"));
            assertEquals("wav", sentBody.getJSONObject("audio_setting").getString("format"));
        } finally {
            server.stop(0);
            Files.deleteIfExists(audioPath);
        }
    }

    @Test
    void keepsTheDefaultProviderOnTheExistingAudioSpeechPath() throws Exception {
        AtomicReference<String> requestPath = new AtomicReference<>();
        byte[] rawAudio = new byte[]{0x52, 0x49, 0x46, 0x46, 0x00, 0x01};

        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            requestPath.set(exchange.getRequestURI().getPath());
            exchange.getRequestBody().readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", "audio/wav");
            exchange.sendResponseHeaders(200, rawAudio.length);
            try (OutputStream body = exchange.getResponseBody()) {
                body.write(rawAudio);
            }
        });
        server.start();

        Path audioPath = Files.createTempFile("speech-", ".wav");
        try {
            AiChatConfig aiChatConfig = new AiChatConfig();
            AiChatConfig.VoiceModelConfig voiceConfig = aiChatConfig.getAiModelVoice();
            voiceConfig.setProvider("ZHIPU");
            voiceConfig.setModel("glm-tts");
            voiceConfig.setApiKey("test-key");
            voiceConfig.setApiHost("http://127.0.0.1:" + server.getAddress().getPort());
            voiceConfig.setTimeout(30);

            VoiceApiHelper helper = new VoiceApiHelper();
            Field configField = VoiceApiHelper.class.getDeclaredField("aiChatConfig");
            configField.setAccessible(true);
            configField.set(helper, aiChatConfig);

            helper.generateAudio("hello", audioPath, "tongtong", 1.0);

            assertEquals("/audio/speech", requestPath.get());
            assertArrayEquals(rawAudio, Files.readAllBytes(audioPath));
        } finally {
            server.stop(0);
            Files.deleteIfExists(audioPath);
        }
    }

    @Test
    void reportsAFailedOperationCall() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            exchange.getRequestBody().readAllBytes();
            byte[] response = "{\"base_resp\":{\"status_code\":1004,\"status_msg\":\"invalid api key\"}}"
                    .getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(401, response.length);
            try (OutputStream body = exchange.getResponseBody()) {
                body.write(response);
            }
        });
        server.start();

        Path audioPath = Files.createTempFile("speech-", ".wav");
        try {
            AiChatConfig aiChatConfig = new AiChatConfig();
            AiChatConfig.VoiceModelConfig voiceConfig = aiChatConfig.getAiModelVoice();
            voiceConfig.setProvider(MiniMaxVoiceApi.PROVIDER);
            voiceConfig.setApiKey("test-key");
            voiceConfig.setApiHost("http://127.0.0.1:" + server.getAddress().getPort());
            voiceConfig.setTimeout(30);

            VoiceApiHelper helper = new VoiceApiHelper();
            Field configField = VoiceApiHelper.class.getDeclaredField("aiChatConfig");
            configField.setAccessible(true);
            configField.set(helper, aiChatConfig);

            RuntimeException error = assertThrows(RuntimeException.class,
                    () -> helper.generateAudio("hello", audioPath, "female-1", 1.0));
            assertTrue(error.getMessage().contains("401"));
        } finally {
            server.stop(0);
            Files.deleteIfExists(audioPath);
        }
    }
}
