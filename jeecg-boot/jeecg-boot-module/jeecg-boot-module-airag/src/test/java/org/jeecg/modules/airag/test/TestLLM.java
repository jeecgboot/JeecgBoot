//package org.jeecg.modules.airag.test;
//
//import dev.langchain4j.agent.tool.ToolParameters;
//import dev.langchain4j.agent.tool.ToolSpecification;
//import dev.langchain4j.data.message.*;
//import dev.langchain4j.model.chat.ChatLanguageModel;
//import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
//import dev.langchain4j.model.openai.OpenAiChatModel;
//import dev.langchain4j.model.output.Response;
//import dev.langchain4j.service.AiServices;
//import dev.langchain4j.service.TokenStream;
//import lombok.extern.slf4j.Slf4j;
//import org.jeecg.ai.assistant.AiChatAssistant;
//import org.jeecg.ai.factory.AiModelFactory;
//import org.jeecg.ai.factory.AiModelOptions;
//import org.jeecg.ai.handler.AIParams;
//import org.jeecg.ai.handler.LLMHandler;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.io.Serial;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CountDownLatch;
//
///**
// * @Description: 流程测试
// * @Author: chenrui
// * @Date: 2025/2/11 16:11
// */
//@Slf4j
//public class TestLLM {
//
//    @Test
//    public void sendByModel() {
//        String apiKey = "sk-xxx";
//        String baseUrl = "https://api.v3.cm/v1";
//        String modelName = "gpt-3.5-turbo";
//        double temperature = 0.7;
//        ChatLanguageModel llmModel = OpenAiChatModel.builder()
//                .apiKey(apiKey)
//                .baseUrl(baseUrl)
//                .modelName(modelName)
//                .temperature(temperature)
//                .build();
//        String hello = llmModel.generate("hello");
//        System.out.println(hello);
//    }
//
//    @Test
//    public void testChat() {
//        AiModelOptions options = AiModelOptions.builder()
//                .provider(AiModelFactory.AIMODEL_TYPE_OPENAI)
//                .apiKey("sk-xxx")
//                .baseUrl("https://api.v3.cm/v1")
//                .build();
//        ChatLanguageModel chatModel = AiModelFactory.createChatModel(options);
//        AiServices<AiChatAssistant> chatAssistantBuilder = AiServices.builder(AiChatAssistant.class);
//        chatAssistantBuilder.chatLanguageModel(chatModel);
//        AiChatAssistant chatAssistant = chatAssistantBuilder.build();
//        String str = chatAssistant.chat("hello");
//        System.out.println(str);
//    }
//
//    @Test
//    public void sendImg() throws IOException {
//        AiModelOptions options = AiModelOptions.builder()
//                .provider(AiModelFactory.AIMODEL_TYPE_OPENAI)
//                .modelName("gpt-4o-mini")
//                .apiKey("sk-xxx")
//                .baseUrl("https://api.v3.cm/v1")
//                .build();
//        ChatLanguageModel chatModel = AiModelFactory.createChatModel(options);
//
//        // 读取文件并转换为 base64 编码字符串
//        byte[] fileContent = Files.readAllBytes(Paths.get("src/test/resources/test.jpg"));
//        String base64Data = Base64.getEncoder().encodeToString(fileContent);
//
//        // 获取文件的 MIME 类型
//        String mimeType = Files.probeContentType(Paths.get("src/test/resources/test.jpg"));
//
//        // 构建 ImageContent 对象
//        ImageContent imageContent = new ImageContent(base64Data, mimeType);
//
//        UserMessage userMessage = UserMessage.from(
//                TextContent.from("你看到了什么"),
//                // 构建 ImageContent 对象
//                new ImageContent(base64Data, mimeType)
//        );
//        Response<AiMessage> generate = chatModel.generate(userMessage);
//        System.out.println(generate.content());
//    }
//
//    @Test
//    public void testSendImsWithLLMHandler() throws IOException {
//        AiModelOptions options = AiModelOptions.builder()
//                .provider(AiModelFactory.AIMODEL_TYPE_OPENAI)
//                .modelName("gpt-4o-mini")
//                .apiKey("sk-xxx")
//                .baseUrl("https://api.v3.cm/v1")
//                .build();
//        ChatLanguageModel chatModel = AiModelFactory.createChatModel(options);
//
//        // 读取文件并转换为 base64 编码字符串
//        byte[] fileContent = Files.readAllBytes(Paths.get("src/test/resources/test.jpg"));
//        String base64Data = Base64.getEncoder().encodeToString(fileContent);
//
//        // 获取文件的 MIME 类型
//        String mimeType = Files.probeContentType(Paths.get("src/test/resources/test.jpg"));
//
//        // 构建 ImageContent 对象
//        ImageContent imageContent = new ImageContent(base64Data, mimeType);
//
//        UserMessage userMessage = UserMessage.from(
//                TextContent.from("你看到了什么"),
//                // 构建 ImageContent 对象
//                new ImageContent(base64Data, mimeType)
//        );
//        LLMHandler llmHandler = new LLMHandler();
//        AIParams aiParams = new AIParams();
//        aiParams.setProvider(AiModelFactory.AIMODEL_TYPE_OPENAI);
//        aiParams.setModelName("gpt-4o-mini");
//        aiParams.setApiKey("sk-xxx");
//        aiParams.setBaseUrl("https://api.v3.cm/v1");
//        String completions = llmHandler.completions(Collections.singletonList(userMessage), aiParams);
//        System.out.println(completions);
//    }
//
//    @Test
//    public void testChatImsWithLLMHandler() throws IOException, InterruptedException {
//        AiModelOptions options = AiModelOptions.builder()
//                .provider(AiModelFactory.AIMODEL_TYPE_OPENAI)
//                .modelName("gpt-4o-mini")
//                .apiKey("sk-xxx")
//                .baseUrl("https://api.v3.cm/v1")
//                .build();
//        ChatLanguageModel chatModel = AiModelFactory.createChatModel(options);
//
//        // 读取文件并转换为 base64 编码字符串
//        byte[] fileContent = Files.readAllBytes(Paths.get("src/test/resources/test.jpg"));
//        String base64Data = Base64.getEncoder().encodeToString(fileContent);
//
//        // 获取文件的 MIME 类型
//        String mimeType = Files.probeContentType(Paths.get("src/test/resources/test.jpg"));
//
//        // 构建 ImageContent 对象
//        ImageContent imageContent = new ImageContent(base64Data, mimeType);
//        UserMessage userMessage = UserMessage.from(
//                TextContent.from("你看到了什么"),
//                // 构建 ImageContent 对象
//                imageContent,
//                ImageContent.from("https://jeecgdev.oss-cn-beijing.aliyuncs.com/temp/logo-qqy_1741658353407.png")
//        );
//        LLMHandler llmHandler = new LLMHandler();
//        AIParams aiParams = new AIParams();
//        aiParams.setProvider(AiModelFactory.AIMODEL_TYPE_OPENAI);
//        aiParams.setModelName("gpt-4o-mini");
//        aiParams.setApiKey("sk-xxx");
//        aiParams.setBaseUrl("https://api.v3.cm/v1");
//        TokenStream chat = llmHandler.chat(Collections.singletonList(userMessage), aiParams);
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        chat.onNext(s -> System.out.println(s))
//                .onComplete(s -> {
//                    System.out.println(s);
//                    countDownLatch.countDown();
//                })
//                .onError(e -> {
//                    System.out.println(e.getMessage());
//                    countDownLatch.countDown();
//                }).start();
//        countDownLatch.await();
//
//    }
//
//
//    @Test
//    public void testFuncCalling() {
//        String apiKey = "sk-";
//        String baseUrl = "https://api.v3.cm/v1";
//        String modelName = "gpt-4o-mini";
//        double temperature = 0.7;
//        ChatLanguageModel llmModel = OpenAiChatModel.builder()
//                .apiKey(apiKey)
//                .baseUrl(baseUrl)
//                .modelName(modelName)
//                .temperature(temperature)
//                .build();
//
//        List<ToolSpecification> toolSpecifications = new ArrayList<>();
//        Map<String, Map<String, Object>> properties = new ConcurrentHashMap<>();
//
//        properties.put("location", new HashMap<>() {
//            @Serial
//            private static final long serialVersionUID = -8440944665582258534L;
//            {
//                put("type", "string");
//                put("description", "The city and state, e.g. San Francisco, CA");
//            }
//        });
//        ToolSpecification toolSpecification = ToolSpecification.builder()
//                .name("get_current_weather")
//                .description("Get the current weather in a given location")
//                .parameters(ToolParameters.builder()
//                        .properties(properties)
//                        .required(List.of("location"))
//                        .type("string")
//                        .build())
//                .build();
//        toolSpecifications.add(toolSpecification);
//
//        List<ChatMessage> messages = new  ArrayList<>();
//        messages.add(UserMessage.from("How is the weather in Beijing today?"));
//
//        Response<AiMessage> resp = llmModel.generate(messages, toolSpecifications);
//        System.out.println(resp);
//
//        /*Response { content = AiMessage { text = null
//        toolExecutionRequests = [ToolExecutionRequest { id = "call_hjPaRh9WAHv6Ib7KpgHpp8Tl", name = "get_current_weather", arguments = "{"location":"Beijing, China"}" }] },
//        tokenUsage = TokenUsage { inputTokenCount = 69, outputTokenCount = 19, totalTokenCount = 88 }, finishReason = TOOL_EXECUTION, metadata = {} }*/
//    }
//
//
//}
