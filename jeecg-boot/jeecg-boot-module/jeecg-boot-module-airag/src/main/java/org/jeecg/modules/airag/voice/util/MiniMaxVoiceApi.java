package org.jeecg.modules.airag.voice.util;

import com.alibaba.fastjson2.JSONObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: <p>Request building and response parsing for the MiniMax speech generation API.</p>
 * <p>The synchronous text to audio operation is a JSON POST that answers with the generated audio
 * encoded as a hex string, so it cannot reuse the streaming audio branch of {@link VoiceApiHelper}.</p>
 *
 * @author: octo-patch
 * @date: 2026/8/17
 */
public final class MiniMaxVoiceApi {

    /**
     * Provider key expected in jeecg.ai-chat.ai-model-voice.provider
     */
    public static final String PROVIDER = "MINIMAX";

    /**
     * Model used when no model is configured
     */
    public static final String DEFAULT_MODEL = "speech-2.8-hd";

    /**
     * Audio format used when no supported format is configured
     */
    public static final String DEFAULT_AUDIO_FORMAT = "wav";

    /**
     * Path of the synchronous text to audio operation
     */
    private static final String T2A_PATH = "/v1/t2a_v2";

    /**
     * Version prefix of the text to audio path, kept separate so an api host that already
     * carries the version segment is not doubled
     */
    private static final String API_VERSION_SEGMENT = "/v1";

    /**
     * Response value that asks for the audio inline as a hex string instead of a download link
     */
    private static final String OUTPUT_FORMAT_HEX = "hex";

    /**
     * Status code returned in base_resp when the request succeeded
     */
    private static final int SUCCESS_STATUS_CODE = 0;

    /**
     * Regional endpoints of the synchronous text to audio operation
     */
    private static final Map<String, String> REGION_ENDPOINTS;

    /**
     * Models accepted by the text to audio operation
     */
    private static final List<String> SUPPORTED_MODELS = List.of(
            "speech-2.8-hd",
            "speech-2.8-turbo",
            "speech-2.6-hd",
            "speech-2.6-turbo",
            "speech-02-hd",
            "speech-02-turbo",
            "speech-01-hd",
            "speech-01-turbo");

    /**
     * Formats accepted by audio_setting.format
     */
    private static final List<String> SUPPORTED_AUDIO_FORMATS = List.of("mp3", "wav", "flac", "pcm");

    /**
     * Range accepted by voice_setting.speed
     */
    private static final double MIN_SPEED = 0.5;
    private static final double MAX_SPEED = 2.0;

    /**
     * Range accepted by voice_setting.vol, which is a gain factor rather than a decibel value
     */
    private static final double MIN_VOLUME = 0.1;
    private static final double MAX_VOLUME = 10.0;

    static {
        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("global_en", "https://api.minimax.io" + T2A_PATH);
        endpoints.put("cn_zh", "https://api.minimaxi.com" + T2A_PATH);
        REGION_ENDPOINTS = Collections.unmodifiableMap(endpoints);
    }

    private MiniMaxVoiceApi() {
    }

    /**
     * Whether the configured provider selects this speech implementation
     *
     * @param provider configured provider
     */
    public static boolean isProvider(String provider) {
        return PROVIDER.equalsIgnoreCase(trimToEmpty(provider));
    }

    /**
     * Regional endpoints of the text to audio operation, keyed by region
     */
    public static Map<String, String> regionEndpoints() {
        return REGION_ENDPOINTS;
    }

    /**
     * Models accepted by the text to audio operation
     */
    public static List<String> supportedModels() {
        return SUPPORTED_MODELS;
    }

    /**
     * Whether the model is one of the documented text to audio models
     *
     * @param model configured model
     */
    public static boolean isSupportedModel(String model) {
        return SUPPORTED_MODELS.contains(trimToEmpty(model));
    }

    /**
     * Resolve the model, falling back to the default model when none is configured
     *
     * @param model configured model
     */
    public static String resolveModel(String model) {
        String resolved = trimToEmpty(model);
        return resolved.isEmpty() ? DEFAULT_MODEL : resolved;
    }

    /**
     * Build the text to audio request url from the configured api host, which may be given as a
     * bare regional host, as a host carrying the version segment, or as the full operation url
     *
     * @param apiHost configured api host
     */
    public static String resolveEndpoint(String apiHost) {
        String host = trimToEmpty(apiHost);
        while (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        if (host.isEmpty()) {
            throw new IllegalArgumentException(
                    "The speech api host is not configured, expected one of the regional endpoints: "
                            + String.join(", ", REGION_ENDPOINTS.values()));
        }
        if (host.endsWith(T2A_PATH)) {
            return host;
        }
        if (host.endsWith(API_VERSION_SEGMENT)) {
            return host + T2A_PATH.substring(API_VERSION_SEGMENT.length());
        }
        return host + T2A_PATH;
    }

    /**
     * Resolve the audio format, falling back to the default format when the configured value is
     * not accepted by the operation
     *
     * @param audioFormat requested audio format
     */
    public static String resolveAudioFormat(String audioFormat) {
        String resolved = trimToEmpty(audioFormat).toLowerCase();
        return SUPPORTED_AUDIO_FORMATS.contains(resolved) ? resolved : DEFAULT_AUDIO_FORMAT;
    }

    /**
     * Clamp the configured speed into the range accepted by the operation
     *
     * @param speed configured speed
     */
    public static double resolveSpeed(double speed) {
        if (speed <= 0) {
            return 1.0;
        }
        return Math.min(MAX_SPEED, Math.max(MIN_SPEED, speed));
    }

    /**
     * Convert the configured decibel gain into the gain factor accepted by voice_setting.vol
     *
     * @param volumeGainDb configured volume gain in decibel
     */
    public static double resolveVolume(double volumeGainDb) {
        double volume = Math.pow(10, volumeGainDb / 20.0);
        return Math.min(MAX_VOLUME, Math.max(MIN_VOLUME, volume));
    }

    /**
     * Build the body of the synchronous text to audio request
     *
     * @param model        configured model
     * @param text         text to convert
     * @param voice        voice id
     * @param speed        speech speed
     * @param volumeGainDb volume gain in decibel
     * @param audioFormat  requested audio format
     */
    public static JSONObject buildRequestBody(String model, String text, String voice, double speed,
                                             double volumeGainDb, String audioFormat) {
        if (trimToEmpty(text).isEmpty()) {
            throw new IllegalArgumentException("The text of a speech request must not be empty");
        }

        JSONObject voiceSetting = new JSONObject();
        String voiceId = trimToEmpty(voice);
        if (!voiceId.isEmpty()) {
            voiceSetting.put("voice_id", voiceId);
        }
        voiceSetting.put("speed", resolveSpeed(speed));
        voiceSetting.put("vol", resolveVolume(volumeGainDb));

        JSONObject audioSetting = new JSONObject();
        audioSetting.put("format", resolveAudioFormat(audioFormat));

        JSONObject body = new JSONObject();
        body.put("model", resolveModel(model));
        body.put("text", text);
        body.put("stream", false);
        body.put("output_format", OUTPUT_FORMAT_HEX);
        body.put("voice_setting", voiceSetting);
        body.put("audio_setting", audioSetting);
        return body;
    }

    /**
     * Read the generated audio out of a text to audio response
     *
     * @param responseBody response body of the text to audio request
     */
    public static byte[] extractAudio(String responseBody) {
        if (trimToEmpty(responseBody).isEmpty()) {
            throw new RuntimeException("The speech api returned an empty response");
        }

        JSONObject response;
        try {
            response = JSONObject.parseObject(responseBody);
        } catch (Exception e) {
            throw new RuntimeException("The speech api returned a response that is not valid json", e);
        }
        if (response == null) {
            throw new RuntimeException("The speech api returned a response that is not valid json");
        }

        JSONObject baseResp = response.getJSONObject("base_resp");
        if (baseResp != null) {
            Integer statusCode = baseResp.getInteger("status_code");
            if (statusCode != null && statusCode != SUCCESS_STATUS_CODE) {
                throw new RuntimeException("The speech api reported status code " + statusCode + ": "
                        + trimToEmpty(baseResp.getString("status_msg")));
            }
        }

        JSONObject data = response.getJSONObject("data");
        String audio = data == null ? null : trimToEmpty(data.getString("audio"));
        if (audio == null || audio.isEmpty()) {
            Integer status = data == null ? null : data.getInteger("status");
            throw new RuntimeException("The speech api returned no audio payload, data status: " + status);
        }
        return decodeHex(audio);
    }

    /**
     * Decode the hex encoded audio payload
     *
     * @param hex hex encoded audio
     */
    static byte[] decodeHex(String hex) {
        int length = hex.length();
        if (length % 2 != 0) {
            throw new RuntimeException("The speech api returned an audio payload of odd length");
        }
        byte[] audio = new byte[length / 2];
        for (int index = 0; index < length; index += 2) {
            int high = Character.digit(hex.charAt(index), 16);
            int low = Character.digit(hex.charAt(index + 1), 16);
            if (high < 0 || low < 0) {
                throw new RuntimeException("The speech api returned an audio payload that is not hex encoded");
            }
            audio[index / 2] = (byte) ((high << 4) + low);
        }
        return audio;
    }

    private static String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    /**
     * Description of the accepted models, used when the configured model is unknown
     */
    public static String supportedModelsDescription() {
        return String.join(", ", SUPPORTED_MODELS);
    }
}
