package org.jeecg.modules.mcp;

import com.alibaba.fastjson.JSONObject;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonStringSchema;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.ai.assistant.AiChatAssistant;
import org.jeecg.ai.factory.AiModelFactory;
import org.jeecg.ai.factory.AiModelOptions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description: llm工具调用测试接口
 * @Author: chenrui
 * @Date: 2025/8/22 14:13
 */
@RestController
@RequestMapping("/ai/tools/test")
@Slf4j
public class LlmToolsTestController {


    @Autowired
    ISysBaseAPI sysBaseAPI;

    // 根据环境构建模型配置；缺少关键项则返回 null 以便测试跳过
    private static AiModelOptions buildModelOptionsFromEnv() {
        String baseUrl = "https://api.gpt.ge";
        String apiKey = "sk-ZLhvUUGPGyERkPya632f3f18209946F7A51d4479081a3dFb";
        String modelName = "gpt-4.1-mini";
        return AiModelOptions.builder()
                .provider(AiModelFactory.AIMODEL_TYPE_OPENAI)
                .modelName(modelName)
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();
    }


    @GetMapping(value = "/queryUser")
    public Result<?> queryUser(@RequestParam(value = "prompt", required = true) String prompt) {
        AiModelOptions modelOp = buildModelOptionsFromEnv();
        ToolSpecification toolSpecification = ToolSpecification.builder()
                .name("query_user_by_name")
                .description("通过通过用户名查询用户信息,返回用户信息列表")
                .parameters(JsonObjectSchema.builder()
                        .addProperties(Map.of(
                                "username", JsonStringSchema.builder()
                                        .description("用户名,多个可以使用逗号分隔")
                                        .build()
                        ))
                        .build())
                .build();
        ToolExecutor toolExecutor = (toolExecutionRequest, memoryId) -> {
            Map<String, Object> arguments = JSONObject.parseObject(toolExecutionRequest.arguments());
            String username = arguments.get("username").toString();
            List<JSONObject> users = sysBaseAPI.queryUsersByUsernames(username);
            return JSONObject.toJSONString(users);
        };

        // 构建同步聊天模型并通过 AiChatAssistant 发起一次非流式对话
        ChatModel chatModel = AiModelFactory.createChatModel(modelOp);
        AiChatAssistant bot = AiServices.builder(AiChatAssistant.class)
                .chatModel(chatModel)
                .tools(Map.of(toolSpecification, toolExecutor))
                .tools()
                .build();
        String chat = bot.chat(prompt);
        log.info("聊天回复: " + chat);
        return Result.OK(chat);
    }

    // 根据环境构建 MCP 工具提供者；缺少 URL 则返回 null 以便测试跳过
    private static McpToolProvider buildMcpTool(String sseUrl) {
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl(sseUrl)
                .logRequests(true)
                .logResponses(true)
                .build();
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();
        return McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();
    }

    /**
     * 测试JeecgMcp
     * @author chenrui
     * @date 2025/8/22 10:10
     */
    @GetMapping(value = "/queryUserByMcp")
    public Result<?> testJeecgToolProvider(@RequestParam(value = "prompt", required = true) String prompt) {
//             prompt = "查询一下有没有用户名是admin的用户信息";
//             prompt = "新建一个顶级部门,部门名称是:测试部门,机构类别是公司";
        AiModelOptions modelOp = buildModelOptionsFromEnv();
        McpToolProvider toolProvider = buildMcpTool("http://localhost:8080/jeecgboot/sse");

        // 构建同步聊天模型并通过 AiChatAssistant 发起一次非流式对话
        ChatModel chatModel = AiModelFactory.createChatModel(modelOp);
        AiChatAssistant bot = AiServices.builder(AiChatAssistant.class)
                .chatModel(chatModel)
                .toolProvider(toolProvider)
                .build();
        String chat = bot.chat(prompt);
        log.info("聊天回复: " + chat);
        return Result.OK(chat);
    }


}
