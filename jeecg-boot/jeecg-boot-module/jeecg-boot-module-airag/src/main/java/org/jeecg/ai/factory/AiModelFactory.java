package org.jeecg.ai.factory;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenChatRequestParameters;
import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.community.model.dashscope.WanxImageSize;
import dev.langchain4j.community.model.qianfan.QianfanChatModel;
import dev.langchain4j.community.model.qianfan.QianfanChatModelNameEnum;
import dev.langchain4j.community.model.qianfan.QianfanEmbeddingModel;
import dev.langchain4j.community.model.qianfan.QianfanEmbeddingModelNameEnum;
import dev.langchain4j.community.model.qianfan.QianfanStreamingChatModel;
import dev.langchain4j.community.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.community.model.zhipu.ZhipuAiEmbeddingModel;
import dev.langchain4j.community.model.zhipu.ZhipuAiStreamingChatModel;
import dev.langchain4j.community.model.zhipu.chat.ChatCompletionModel;
import dev.langchain4j.community.model.zhipu.embedding.EmbeddingModel;
import dev.langchain4j.community.model.zhipu.image.ImageModelName;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.anthropic.AnthropicChatModelName;
import dev.langchain4j.model.anthropic.AnthropicStreamingChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.openai.OpenAiImageModelName;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.apache.commons.lang.StringUtils;
import org.jeecg.ai.custom.zhipu.CustomZhipuAiImageModel;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI模型工厂类
 * 覆盖 jeecg-boot-starter-chatgpt 中的同名类，
 * 修复 DashScope(QWEN) 流式模型缺少 incrementalOutput=true 的问题。
 * fix: https://github.com/jeecgboot/JeecgBoot/issues/9446
 */
public class AiModelFactory {
    public static final String AIMODEL_TYPE_OPENAI = "OPENAI";
    public static final String AIMODEL_TYPE_ZHIPU = "ZHIPU";
    public static final String AIMODEL_TYPE_QIANFAN = "QIANFAN";
    public static final String AIMODEL_TYPE_QWEN = "QWEN";
    public static final String AIMODEL_TYPE_DEEPSEEK = "DEEPSEEK";
    public static final String AIMODEL_TYPE_OLLAMA = "OLLAMA";
    public static final String AIMODEL_TYPE_ANTHROPIC = "ANTHROPIC";

    private static final ConcurrentHashMap<String, Object> chatModelCache = new ConcurrentHashMap<>();

    private static Object getCache(String key) {
        return chatModelCache.get(key);
    }

    private static void setCache(String key, Object model) {
        chatModelCache.put(key, model);
    }

    public static ChatModel createChatModel(AiModelOptions options) {
        assertNotEmpty("请设置模型参数", options);
        assertNotEmpty("请选择AI模型供应商", options.getProvider());
        String cacheKey = options.toString();
        Object cachedModel = getCache(cacheKey);
        if (cachedModel != null) {
            return (ChatModel) cachedModel;
        }
        String apiKey = options.getApiKey();
        String secretKey = options.getSecretKey();
        String baseUrl = options.getBaseUrl();
        String modelName = options.getModelName();
        double temperature = getDouble(options.getTemperature(), 0.7);
        double topP = getDouble(options.getTopP(), 1.0);
        double presencePenalty = getDouble(options.getPresencePenalty(), 0.0);
        double frequencyPenalty = getDouble(options.getFrequencyPenalty(), 0.0);
        double repetitionPenalty = 1.0 + (presencePenalty + frequencyPenalty) / 2.0;
        int timeout = getInteger(options.getTimeout(), 120);
        Integer maxTokens = options.getMaxTokens();
        ChatModel chatModel = null;
        switch (options.getProvider().toUpperCase()) {
            case "OPENAI": {
                assertNotEmpty("apiKey不能为空", apiKey);
                baseUrl = ensureOpenAiUrlEnd(baseUrl);
                modelName = getString(modelName, OpenAiChatModelName.GPT_3_5_TURBO.toString());
                OpenAiChatModel.OpenAiChatModelBuilder openAIBuilder = OpenAiChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).modelName(modelName)
                        .temperature(temperature).topP(topP)
                        .presencePenalty(presencePenalty).frequencyPenalty(frequencyPenalty)
                        .timeout(Duration.ofSeconds(timeout)).maxRetries(0);
                if (null != maxTokens) {
                    openAIBuilder.maxTokens(maxTokens);
                }
                chatModel = openAIBuilder.build();
                break;
            }
            case "ZHIPU": {
                assertNotEmpty("apiKey不能为空", apiKey);
                modelName = getString(modelName, ChatCompletionModel.GLM_4_FLASH.toString());
                ZhipuAiChatModel.ZhipuAiChatModelBuilder zhipuBuilder = ZhipuAiChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).model(modelName)
                        .temperature(temperature).topP(topP)
                        .maxRetries(0)
                        .readTimeout(Duration.ofSeconds(timeout))
                        .connectTimeout(Duration.ofSeconds(timeout));
                if (null != maxTokens) {
                    zhipuBuilder.maxToken(maxTokens);
                }
                chatModel = zhipuBuilder.build();
                break;
            }
            case "QIANFAN": {
                assertNotEmpty("apiKey不能为空", apiKey);
                assertNotEmpty("secretKey不能为空", secretKey);
                modelName = getString(modelName, QianfanChatModelNameEnum.YI_34B_CHAT.getModelName());
                QianfanChatModel.QianfanChatModelBuilder qianfanBuilder = QianfanChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).secretKey(secretKey).modelName(modelName)
                        .temperature(temperature).topP(topP)
                        .penaltyScore(repetitionPenalty).maxRetries(0);
                if (null != maxTokens) {
                    qianfanBuilder.maxOutputTokens(maxTokens);
                }
                chatModel = qianfanBuilder.build();
                break;
            }
            case "QWEN": {
                assertNotEmpty("apiKey不能为空", apiKey);
                modelName = getString(modelName, "qwen-plus");
                boolean enableSearch = getBool(options.getEnableSearch(), false);
                QwenChatModel.QwenChatModelBuilder qwenBuilder = QwenChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).modelName(modelName)
                        .temperature((float) temperature).topP(topP)
                        .enableSearch(enableSearch);
                if (!(modelName.contains("-vl-") || modelName.contains("-audio-") || modelName.contains("-omni-"))) {
                    qwenBuilder.repetitionPenalty((float) repetitionPenalty);
                }
                if (null != maxTokens) {
                    qwenBuilder.maxTokens(maxTokens);
                }
                chatModel = qwenBuilder.build();
                break;
            }
            case "OLLAMA": {
                assertNotEmpty("baseUrl不能为空", baseUrl);
                assertNotEmpty("请选择模型", modelName);
                chatModel = OllamaChatModel.builder().baseUrl(baseUrl).modelName(modelName)
                        .temperature(temperature).topP(topP)
                        .repeatPenalty(repetitionPenalty)
                        .timeout(Duration.ofSeconds(timeout)).maxRetries(3).build();
                break;
            }
            case "DEEPSEEK": {
                assertNotEmpty("apiKey不能为空", apiKey);
                baseUrl = getString(baseUrl, "https://api.deepseek.com/v1");
                baseUrl = ensureOpenAiUrlEnd(baseUrl);
                modelName = getString(modelName, "deepseek-chat");
                OpenAiChatModel.OpenAiChatModelBuilder dsBuilder = OpenAiChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).modelName(modelName)
                        .temperature(temperature).topP(topP)
                        .presencePenalty(presencePenalty).frequencyPenalty(frequencyPenalty)
                        .timeout(Duration.ofSeconds(timeout));
                if (null != maxTokens) {
                    dsBuilder.maxTokens(maxTokens);
                }
                if (null != options.getReturnThinking()) {
                    dsBuilder.returnThinking(options.getReturnThinking());
                }
                chatModel = dsBuilder.build();
                break;
            }
            case "ANTHROPIC": {
                assertNotEmpty("apiKey不能为空", apiKey);
                baseUrl = getString(baseUrl, "https://api.anthropic.com/v1");
                modelName = getString(modelName, AnthropicChatModelName.CLAUDE_SONNET_4_5_20250929.toString());
                AnthropicChatModel.AnthropicChatModelBuilder anthropicBuilder = AnthropicChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).modelName(modelName)
                        .temperature(temperature).topP(topP)
                        .timeout(Duration.ofSeconds(timeout)).maxRetries(0);
                if (null != maxTokens) {
                    anthropicBuilder.maxTokens(maxTokens);
                }
                chatModel = anthropicBuilder.build();
            }
        }
        setCache(cacheKey, chatModel);
        return chatModel;
    }

    public static StreamingChatModel createStreamingChatModel(AiModelOptions options) {
        assertNotEmpty("请设置模型参数", options);
        assertNotEmpty("请选择AI模型供应商", options.getProvider());
        String cacheKey = "STEAM_" + options.toString();
        Object cachedModel = getCache(cacheKey);
        if (cachedModel != null) {
            return (StreamingChatModel) cachedModel;
        }
        String apiKey = options.getApiKey();
        String secretKey = options.getSecretKey();
        String baseUrl = options.getBaseUrl();
        String modelName = options.getModelName();
        double temperature = getDouble(options.getTemperature(), 0.7);
        double topP = getDouble(options.getTopP(), 1.0);
        double presencePenalty = getDouble(options.getPresencePenalty(), 0.0);
        double frequencyPenalty = getDouble(options.getFrequencyPenalty(), 0.0);
        double repetitionPenalty = 1.0 + (presencePenalty + frequencyPenalty) / 2.0;
        int timeout = getInteger(options.getTimeout(), 120);
        Integer maxTokens = options.getMaxTokens();
        StreamingChatModel chatModel = null;
        switch (options.getProvider().toUpperCase()) {
            case "OPENAI": {
                assertNotEmpty("apiKey不能为空", apiKey);
                baseUrl = ensureOpenAiUrlEnd(baseUrl);
                modelName = getString(modelName, OpenAiChatModelName.GPT_3_5_TURBO.toString());
                OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder openAIBuilder = OpenAiStreamingChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).modelName(modelName)
                        .temperature(temperature).topP(topP)
                        .presencePenalty(presencePenalty).frequencyPenalty(frequencyPenalty)
                        .timeout(Duration.ofSeconds(timeout));
                if (null != maxTokens) {
                    openAIBuilder.maxTokens(maxTokens);
                }
                chatModel = openAIBuilder.build();
                break;
            }
            case "ZHIPU": {
                assertNotEmpty("apiKey不能为空", apiKey);
                modelName = getString(modelName, ChatCompletionModel.GLM_4_FLASH.toString());
                ZhipuAiStreamingChatModel.ZhipuAiStreamingChatModelBuilder zhipuBuilder = ZhipuAiStreamingChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).model(modelName)
                        .temperature(temperature).topP(topP)
                        .readTimeout(Duration.ofSeconds(timeout))
                        .connectTimeout(Duration.ofSeconds(timeout));
                if (null != maxTokens) {
                    zhipuBuilder.maxToken(maxTokens);
                }
                chatModel = zhipuBuilder.build();
                break;
            }
            case "QIANFAN": {
                assertNotEmpty("apiKey不能为空", apiKey);
                assertNotEmpty("secretKey不能为空", secretKey);
                modelName = getString(modelName, QianfanChatModelNameEnum.YI_34B_CHAT.getModelName());
                chatModel = QianfanStreamingChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).secretKey(secretKey).modelName(modelName)
                        .temperature(temperature).topP(topP)
                        .penaltyScore(repetitionPenalty).build();
                break;
            }
            case "QWEN": {
                assertNotEmpty("apiKey不能为空", apiKey);
                modelName = getString(modelName, "qwen-plus");
                Boolean enableSearch = getBool(options.getEnableSearch(), false);
                // fix: 设置 supportIncrementalOutput=true，修复 qwen3.5-plus 等模型流式调用报错问题
                // https://github.com/jeecgboot/JeecgBoot/issues/9446
                QwenChatRequestParameters qwenRequestParams = QwenChatRequestParameters.builder()
                        .supportIncrementalOutput(true)
                        .build();
                QwenStreamingChatModel.QwenStreamingChatModelBuilder qwenBuilder = QwenStreamingChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).modelName(modelName)
                        .temperature((float) temperature).topP(topP)
                        .enableSearch(enableSearch)
                        .defaultRequestParameters(qwenRequestParams);
                if (!(modelName.contains("-vl-") || modelName.contains("-audio-") || modelName.contains("-omni-"))) {
                    qwenBuilder.repetitionPenalty((float) repetitionPenalty);
                }
                if (null != maxTokens) {
                    qwenBuilder.maxTokens(maxTokens);
                }
                chatModel = qwenBuilder.build();
                break;
            }
            case "OLLAMA": {
                assertNotEmpty("baseUrl不能为空", baseUrl);
                assertNotEmpty("请选择模型", modelName);
                chatModel = OllamaStreamingChatModel.builder().baseUrl(baseUrl).modelName(modelName)
                        .temperature(temperature).topP(topP)
                        .repeatPenalty(repetitionPenalty)
                        .timeout(Duration.ofSeconds(timeout)).build();
                break;
            }
            case "DEEPSEEK": {
                assertNotEmpty("apiKey不能为空", apiKey);
                baseUrl = getString(baseUrl, "https://api.deepseek.com/v1");
                baseUrl = ensureOpenAiUrlEnd(baseUrl);
                modelName = getString(modelName, "deepseek-chat");
                OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder dsBuilder = OpenAiStreamingChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).modelName(modelName)
                        .temperature(temperature).topP(topP)
                        .presencePenalty(presencePenalty).frequencyPenalty(frequencyPenalty)
                        .timeout(Duration.ofSeconds(timeout));
                if (null != maxTokens) {
                    dsBuilder.maxTokens(maxTokens);
                }
                if (null != options.getReturnThinking()) {
                    dsBuilder.returnThinking(options.getReturnThinking());
                }
                chatModel = dsBuilder.build();
                break;
            }
            case "ANTHROPIC": {
                assertNotEmpty("apiKey不能为空", apiKey);
                baseUrl = getString(baseUrl, "https://api.anthropic.com/v1");
                modelName = getString(modelName, AnthropicChatModelName.CLAUDE_SONNET_4_5_20250929.toString());
                AnthropicStreamingChatModel.AnthropicStreamingChatModelBuilder anthropicStreamBuilder = AnthropicStreamingChatModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).modelName(modelName)
                        .temperature(temperature).topP(topP)
                        .timeout(Duration.ofSeconds(timeout));
                if (null != maxTokens) {
                    anthropicStreamBuilder.maxTokens(maxTokens);
                }
                chatModel = anthropicStreamBuilder.build();
            }
        }
        setCache(cacheKey, chatModel);
        return chatModel;
    }

    public static dev.langchain4j.model.embedding.EmbeddingModel createEmbeddingModel(AiModelOptions options) {
        assertNotEmpty("请设置模型参数", options);
        assertNotEmpty("请选择AI模型供应商", options.getProvider());
        String cacheKey = options.toString();
        Object cachedModel = getCache(cacheKey);
        if (cachedModel != null) {
            return (dev.langchain4j.model.embedding.EmbeddingModel) cachedModel;
        }
        String apiKey = options.getApiKey();
        String secretKey = options.getSecretKey();
        String baseUrl = options.getBaseUrl();
        String modelName = options.getModelName();
        int timeout = getInteger(options.getTimeout(), 120);
        dev.langchain4j.model.embedding.EmbeddingModel embeddingModel = switch (options.getProvider().toUpperCase()) {
            case AIMODEL_TYPE_OPENAI -> {
                assertNotEmpty("apiKey不能为空", apiKey);
                baseUrl = ensureOpenAiUrlEnd(baseUrl);
                modelName = getString(modelName, OpenAiEmbeddingModelName.TEXT_EMBEDDING_ADA_002.toString());
                yield OpenAiEmbeddingModel.builder().apiKey(apiKey).baseUrl(baseUrl).modelName(modelName)
                        .timeout(Duration.ofSeconds(timeout)).maxRetries(0).build();
            }
            case AIMODEL_TYPE_ZHIPU -> {
                assertNotEmpty("apiKey不能为空", apiKey);
                modelName = getString(modelName, EmbeddingModel.EMBEDDING_2.toString());
                yield ZhipuAiEmbeddingModel.builder().apiKey(apiKey).baseUrl(baseUrl).model(modelName)
                        .readTimeout(Duration.ofSeconds(timeout)).connectTimeout(Duration.ofSeconds(timeout))
                        .dimensions(1536).maxRetries(0).build();
            }
            case AIMODEL_TYPE_QIANFAN -> {
                assertNotEmpty("apiKey不能为空", apiKey);
                assertNotEmpty("secretKey不能为空", secretKey);
                modelName = getString(modelName, QianfanEmbeddingModelNameEnum.EMBEDDING_V1.getModelName());
                yield QianfanEmbeddingModel.builder().apiKey(apiKey).baseUrl(baseUrl).secretKey(secretKey)
                        .modelName(modelName).maxRetries(0).build();
            }
            case AIMODEL_TYPE_QWEN -> {
                assertNotEmpty("apiKey不能为空", apiKey);
                modelName = getString(modelName, "text-embedding-v2");
                yield QwenEmbeddingModel.builder().apiKey(apiKey).baseUrl(baseUrl).modelName(modelName).build();
            }
            case AIMODEL_TYPE_OLLAMA -> {
                assertNotEmpty("baseUrl不能为空", baseUrl);
                assertNotEmpty("请选择模型", modelName);
                yield OllamaEmbeddingModel.builder().baseUrl(baseUrl).modelName(modelName)
                        .maxRetries(3).timeout(Duration.ofSeconds(timeout)).maxRetries(0).build();
            }
            default -> throw new RuntimeException("不支持的模型");
        };
        setCache(cacheKey, embeddingModel);
        return embeddingModel;
    }

    public static ImageModel createImageModel(AiModelOptions options) {
        assertNotEmpty("请设置模型参数", options);
        assertNotEmpty("请选择AI模型供应商", options.getProvider());
        String cacheKey = "IMAGE_" + options.toString();
        Object cachedModel = getCache(cacheKey);
        if (cachedModel != null) {
            return (ImageModel) cachedModel;
        }
        String apiKey = options.getApiKey();
        String baseUrl = options.getBaseUrl();
        String modelName = options.getModelName();
        int timeout = getInteger(options.getTimeout(), 120);
        ImageModel imageModel = null;
        switch (options.getProvider().toUpperCase()) {
            case "OPENAI": {
                assertNotEmpty("apiKey不能为空", apiKey);
                baseUrl = ensureOpenAiUrlEnd(baseUrl);
                modelName = getString(modelName, OpenAiImageModelName.DALL_E_3.toString());
                OpenAiImageModel.OpenAiImageModelBuilder builder = OpenAiImageModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).modelName(modelName)
                        .timeout(Duration.ofSeconds(timeout)).maxRetries(0)
                        .logRequests(true).logResponses(true);
                if (StringUtils.isNotEmpty(options.getImageSize())
                        && ("dall-e-2".equals(options.getModelName()) || "dall-e-3".equals(options.getModelName()))) {
                    builder.size(options.getImageSize());
                }
                imageModel = builder.build();
                break;
            }
            case "ZHIPU": {
                assertNotEmpty("apiKey不能为空", apiKey);
                modelName = getString(modelName, ImageModelName.COGVIEW_3.toString());
                CustomZhipuAiImageModel.CustomZhipuAiImageModelBuilder imageModelBuilder = CustomZhipuAiImageModel.builder()
                        .apiKey(apiKey).baseUrl(baseUrl).model(modelName)
                        .readTimeout(Duration.ofSeconds(timeout)).connectTimeout(Duration.ofSeconds(timeout))
                        .maxRetries(0).logRequests(true).logResponses(true)
                        .watermarkEnabled(false).size(options.getImageSize());
                if (StringUtils.isNotEmpty(options.getImageSize())) {
                    imageModelBuilder.size(options.getImageSize());
                }
                imageModel = imageModelBuilder.build();
                break;
            }
            case "QWEN": {
                assertNotEmpty("apiKey不能为空", apiKey);
                modelName = getString(modelName, "wanx-v1");
                WanxImageModel.WanxImageModelBuilder wanxBuilder = WanxImageModel.builder()
                        .apiKey(apiKey).watermark(false).baseUrl(baseUrl).modelName(modelName);
                if (StringUtils.isNotEmpty(options.getImageSize())) {
                    wanxBuilder.size(WanxImageSize.of(options.getImageSize()));
                }
                imageModel = wanxBuilder.build();
                break;
            }
            default: {
                throw new RuntimeException("不支持的模型");
            }
        }
        setCache(cacheKey, imageModel);
        return imageModel;
    }

    public static void assertNotEmpty(String msg, Object obj) {
        if (isObjectEmpty(obj)) {
            throw new RuntimeException(msg);
        }
    }

    public static boolean isObjectEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return isEmpty(obj);
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj instanceof Iterable) {
            return isObjectEmpty(((Iterable<?>) obj).iterator());
        }
        if (obj instanceof Iterator) {
            return !((Iterator<?>) obj).hasNext();
        }
        if (isArray(obj)) {
            return 0 == Array.getLength(obj);
        }
        return false;
    }

    private static String ensureOpenAiUrlEnd(String baseUrl) {
        if (StringUtils.isNotEmpty(baseUrl)) {
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            if (!baseUrl.endsWith("v1")) {
                baseUrl = baseUrl + (baseUrl.endsWith("/") ? "" : "/") + "v1";
            }
        }
        return baseUrl;
    }

    public static boolean isArray(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj.getClass().isArray();
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if ("".equals(object)) {
            return true;
        }
        return "null".equals(object);
    }

    public static String getString(CharSequence str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str.toString();
    }

    public static Integer getInteger(Integer object, Integer defval) {
        if (object == null) {
            return defval;
        }
        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException e) {
            return defval;
        }
    }

    public static Double getDouble(Double object, Double defval) {
        if (object == null) {
            return defval;
        }
        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException e) {
            return defval;
        }
    }

    public static Boolean getBool(Boolean object, Boolean defval) {
        if (object == null) {
            return defval;
        }
        try {
            return Boolean.parseBoolean(object.toString());
        } catch (Exception e) {
            return defval;
        }
    }
}
