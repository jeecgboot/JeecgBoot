package org.jeecg.modules.airag.llm.handler;

import com.alibaba.fastjson.JSONObject;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.*;
import dev.langchain4j.exception.InvalidRequestException;
import dev.langchain4j.exception.ToolExecutionException;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.ai.handler.LLMHandler;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.common.util.filter.SsrfFileTypeFilter;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.common.consts.AiragConsts;
import org.jeecg.modules.airag.common.handler.AIChatParams;
import org.jeecg.modules.airag.common.handler.IAIChatHandler;
import org.jeecg.modules.airag.common.handler.McpToolProviderWrapper;
import org.jeecg.modules.airag.llm.consts.LLMConsts;
import org.jeecg.modules.airag.llm.entity.AiragMcp;
import org.jeecg.modules.airag.llm.entity.AiragModel;
import org.jeecg.modules.airag.llm.mapper.AiragMcpMapper;
import org.jeecg.modules.airag.llm.mapper.AiragModelMapper;
import org.jeecg.config.AiRagConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * 大模型聊天工具类
 *
 * @Author: chenrui
 * @Date: 2025/2/18 14:31
 */
@Slf4j
@Component
public class AIChatHandler implements IAIChatHandler {

    @Autowired
    AiragModelMapper airagModelMapper;

    @Autowired
    AiragMcpMapper airagMcpMapper;

    @Autowired
    EmbeddingHandler embeddingHandler;

    @Autowired
    LLMHandler llmHandler;

    @Autowired
    AiRagConfigBean aiRagConfigBean;

    @Value(value = "${jeecg.path.upload:}")
    private String uploadpath;

    /**
     * 问答
     *
     * @param modelId
     * @param messages
     * @return
     * @author chenrui
     * @date 2025/2/18 21:03
     */
    @Override
    public String completions(String modelId, List<ChatMessage> messages) {
        AssertUtils.assertNotEmpty("至少发送一条消息", messages);
        AssertUtils.assertNotEmpty("请选择模型", modelId);
        // 整理消息
        return completions(modelId, messages, null);
    }

    /**
     * 问答
     *
     * @param modelId
     * @param messages
     * @param params
     * @return
     * @author chenrui
     * @date 2025/2/18 21:03
     */
    @Override
    public String completions(String modelId, List<ChatMessage> messages, AIChatParams params) {
        AssertUtils.assertNotEmpty("至少发送一条消息", messages);
        AssertUtils.assertNotEmpty("请选择模型", modelId);

        AiragModel airagModel = airagModelMapper.getByIdIgnoreTenant(modelId);
        AssertUtils.assertSame("模型未激活,请先在[AI模型配置]中[测试激活]模型", airagModel.getActivateFlag(), 1);
        return completions(airagModel, messages, params);
    }

    /**
     * 问答
     *
     * @param airagModel
     * @param messages
     * @param params
     * @return
     * @author chenrui
     * @date 2025/2/24 17:30
     */
    public String completions(AiragModel airagModel, List<ChatMessage> messages, AIChatParams params) {
        params = mergeParams(airagModel, params);
        String resp;
        try {
            resp = llmHandler.completions(messages, params);
        } catch (ToolExecutionException | InvalidRequestException e) {
            log.error(e.getMessage(), e);
            return "";
        } catch (Exception e) {
            // langchain4j 异常友好提示
            String errMsg = "调用大模型接口失败，详情请查看后台日志。";
            if (oConvertUtils.isNotEmpty(e.getMessage())) {
                String exceptionMsg = e.getMessage();
                
                // 检查是否是工具调用消息序列不完整的异常
                if (exceptionMsg.contains("messages with role 'tool' must be a response to a preceeding message with 'tool_calls'")) {
                    errMsg = "消息序列不完整，可能是因为历史消息数量设置过小导致工具调用上下文丢失。建议增加历史消息数量后重试。";
                    log.error("AI模型调用异常: 工具调用消息序列不完整，建议增加历史消息数量。异常详情: {}", exceptionMsg, e);
                    throw new JeecgBootException(errMsg);
                }
                
                // 根据常见异常关键字做细致翻译
                for (Map.Entry<String, String> entry : MODEL_ERROR_MAP.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (exceptionMsg.contains(key)) {
                        errMsg = value;
                        break;
                    }
                }
            }
            log.error("AI模型调用异常: {}", errMsg, e);
            throw new JeecgBootException(errMsg);
        }
        if (resp.contains("</think>")
                && (null == params.getNoThinking() || params.getNoThinking())) {
            String[] thinkSplit = resp.split("</think>");
            resp = thinkSplit[thinkSplit.length - 1];
        }
        return resp;
    }

    /**
     * 使用默认模型问答
     *
     * @param messages
     * @param params
     * @return
     * @author chenrui
     * @date 2025/3/12 15:13
     */
    @Override
    public String completionsByDefaultModel(List<ChatMessage> messages, AIChatParams params) {
        return completions(new AiragModel(), messages, params);
    }

    /**
     * 聊天(流式)
     *
     * @param modelId
     * @param messages
     * @return
     * @author chenrui
     * @date 2025/2/20 21:06
     */
    @Override
    public TokenStream chat(String modelId, List<ChatMessage> messages) {
        return chat(modelId, messages, null);
    }

    /**
     * 聊天(流式)
     *
     * @param modelId
     * @param messages
     * @param params
     * @return
     * @author chenrui
     * @date 2025/2/18 21:03
     */
    @Override
    public TokenStream chat(String modelId, List<ChatMessage> messages, AIChatParams params) {
        AssertUtils.assertNotEmpty("至少发送一条消息", messages);
        AssertUtils.assertNotEmpty("请选择模型", modelId);

        AiragModel airagModel = airagModelMapper.getByIdIgnoreTenant(modelId);
        AssertUtils.assertSame("模型未激活,请先在[AI模型配置]中[测试激活]模型", airagModel.getActivateFlag(), 1);
        return chat(airagModel, messages, params);
    }

    /**
     * 聊天(流式)
     *
     * @param airagModel
     * @param messages
     * @param params
     * @return
     * @author chenrui
     * @date 2025/2/24 17:29
     */
    private TokenStream chat(AiragModel airagModel, List<ChatMessage> messages, AIChatParams params) {
        params = mergeParams(airagModel, params);
        return llmHandler.chat(messages, params);
    }

    /**
     * 使用默认模型聊天
     *
     * @param messages
     * @param params
     * @return
     * @author chenrui
     * @date 2025/3/12 15:13
     */
    @Override
    public TokenStream chatByDefaultModel(List<ChatMessage> messages, AIChatParams params) {
        return chat(new AiragModel(), messages, params);
    }

    /**
     * 合并 airagmodel和params,params为准
     *
     * @param airagModel
     * @param params
     * @return
     * @author chenrui
     * @date 2025/3/11 17:45
     */
    private AIChatParams mergeParams(AiragModel airagModel, AIChatParams params) {
        if (null == airagModel) {
            return params;
        }
        if (params == null) {
            params = new AIChatParams();
        }

        params.setProvider(airagModel.getProvider());
        params.setModelName(airagModel.getModelName());
        params.setBaseUrl(airagModel.getBaseUrl());
        if (oConvertUtils.isObjectNotEmpty(airagModel.getCredential())) {
            JSONObject modelCredential = JSONObject.parseObject(airagModel.getCredential());
            params.setApiKey(oConvertUtils.getString(modelCredential.getString("apiKey"), null));
            params.setSecretKey(oConvertUtils.getString(modelCredential.getString("secretKey"), null));
        }
        if (oConvertUtils.isObjectNotEmpty(airagModel.getModelParams())) {
            JSONObject modelParams = JSONObject.parseObject(airagModel.getModelParams());
            if (oConvertUtils.isObjectEmpty(params.getTemperature())) {
                params.setTemperature(modelParams.getDouble("temperature"));
            }
            if (oConvertUtils.isObjectEmpty(params.getTopP())) {
                params.setTopP(modelParams.getDouble("topP"));
            }
            if (oConvertUtils.isObjectEmpty(params.getPresencePenalty())) {
                params.setPresencePenalty(modelParams.getDouble("presencePenalty"));
            }
            if (oConvertUtils.isObjectEmpty(params.getFrequencyPenalty())) {
                params.setFrequencyPenalty(modelParams.getDouble("frequencyPenalty"));
            }
            if (oConvertUtils.isObjectEmpty(params.getMaxTokens())) {
                params.setMaxTokens(modelParams.getInteger("maxTokens"));
            }
            if (oConvertUtils.isObjectEmpty(params.getTimeout())) {
                params.setTimeout(modelParams.getInteger("timeout"));
            }
            if (oConvertUtils.isObjectEmpty(params.getEnableSearch())) {
                params.setEnableSearch(modelParams.getBoolean("enableSearch"));
            }
        }

        // RAG
        List<String> knowIds = params.getKnowIds();
        if (oConvertUtils.isObjectNotEmpty(knowIds)) {
            QueryRouter queryRouter = embeddingHandler.getQueryRouter(knowIds, params.getTopNumber(), params.getSimilarity());
            params.setQueryRouter(queryRouter);
        }

        // 设置确保maxTokens值正确
        if (oConvertUtils.isObjectNotEmpty(params.getMaxTokens()) && params.getMaxTokens() <= 0) {
            params.setMaxTokens(null);
        }

        // 默认超时时间
        if(oConvertUtils.isObjectEmpty(params.getTimeout())){
            params.setTimeout(AiragConsts.DEFAULT_TIMEOUT);
        }

        //deepseek-reasoner 推理模型不支持插件tool
        String modelName = airagModel.getModelName();
        if(!LLMConsts.DEEPSEEK_REASONER.equals(modelName)){
            // 插件/MCP处理
            buildPlugins(params);
        }

        return params;
    }

    /**
     * 构造插件和MCP工具
     * for [QQYUN-12453]【AI】支持插件
     * for [QQYUN-9234] MCP服务连接关闭 - 使用包装器保存连接引用
     * @param params
     * @author chenrui
     * @date 2025/10/31 14:04
     */
    private void buildPlugins(AIChatParams params) {
        List<String> pluginIds = params.getPluginIds();

        if(oConvertUtils.isObjectNotEmpty(pluginIds)){
            List<McpToolProvider> mcpToolProviders = new ArrayList<>();
            List<McpToolProviderWrapper> mcpToolProviderWrappers = new ArrayList<>();
            Map<ToolSpecification, ToolExecutor> pluginTools = new HashMap<>();

            for (String pluginId : pluginIds.stream().distinct().collect(Collectors.toList())) {
                AiragMcp airagMcp = airagMcpMapper.selectById(pluginId);
                if (airagMcp == null) {
                    continue;
                }

                String category = airagMcp.getCategory();
                if (oConvertUtils.isEmpty(category)) {
                    // 兼容旧数据：如果没有category字段，默认为mcp
                    category = "mcp";
                }

                if ("mcp".equalsIgnoreCase(category)) {
                    // MCP类型：构建McpToolProviderWrapper（包含连接引用用于后续关闭）
                    // for [QQYUN-9234] MCP服务连接关闭
                    McpToolProviderWrapper wrapper = buildMcpToolProviderWrapper(
                            airagMcp.getName(),
                            airagMcp.getType(),
                            airagMcp.getEndpoint(),
                            airagMcp.getHeaders(),
                            aiRagConfigBean.getAllowSensitiveNodes()
                    );
                    if (wrapper != null) {
                        mcpToolProviders.add(wrapper.getMcpToolProvider());
                        mcpToolProviderWrappers.add(wrapper);
                    }
                } else if ("plugin".equalsIgnoreCase(category)) {
                    // 插件类型：构建ToolSpecification和ToolExecutor
                    Map<ToolSpecification, ToolExecutor> tools = PluginToolBuilder.buildTools(airagMcp, params.getCurrentHttpRequest());
                    if (tools != null && !tools.isEmpty()) {
                        pluginTools.putAll(tools);
                    }
                }
            }

            // 设置MCP工具提供者
            if (!mcpToolProviders.isEmpty()) {
                params.setMcpToolProviders(mcpToolProviders);
            }
            
            // 保存MCP连接包装器，用于后续关闭
            // for [QQYUN-9234] MCP服务连接关闭
            if (!mcpToolProviderWrappers.isEmpty()) {
                params.setMcpToolProviderWrappers(mcpToolProviderWrappers);
            }

            // 设置插件工具
            if (!pluginTools.isEmpty()) {
                if (params.getTools() == null) {
                    params.setTools(new HashMap<>());
                }
                params.getTools().putAll(pluginTools);
            }
        }
    }

    @Override
    public UserMessage buildUserMessage(String content, List<String> images) {
        AssertUtils.assertNotEmpty("请输入消息内容", content);
        List<Content> contents = new ArrayList<>();
        contents.add(TextContent.from(content));
        if (oConvertUtils.isObjectNotEmpty(images)) {
            // 获取所有图片,将他们转换为ImageContent
            List<ImageContent> imageContents = buildImageContents(images);
            contents.addAll(imageContents);
        }
        return UserMessage.from(contents);
    }

    @Override
    public List<ImageContent> buildImageContents(List<String> images) {
        List<ImageContent> imageContents = new ArrayList<>();
        for (String imageUrl : images) {
            Matcher matcher = LLMConsts.WEB_PATTERN.matcher(imageUrl);
            if (matcher.matches()) {
                // 来源于网络
                imageContents.add(ImageContent.from(imageUrl));
            } else {
                // 本地文件
                String filePath = uploadpath + File.separator + imageUrl;
                // 读取文件并转换为 base64 编码字符串
                try {
                    SsrfFileTypeFilter.checkPathTraversal(filePath);
                    Path path = Paths.get(filePath);
                    byte[] fileContent = Files.readAllBytes(path);
                    String base64Data = Base64.getEncoder().encodeToString(fileContent);
                    // 获取文件的 MIME 类型
                    String mimeType = Files.probeContentType(path);
                    // 构建 ImageContent 对象
                    imageContents.add(ImageContent.from(base64Data, mimeType));
                } catch (IOException e) {
                    log.error("读取文件失败: {}", imageUrl, e);
                    throw new RuntimeException("发送消息失败,读取文件异常:" + e.getMessage(), e);
                }
            }
        }
        return imageContents;
    }

    //================================================= begin【QQYUN-12145】【AI】AI 绘画创作 ========================================
    /**
     * 文本生成图片
     * @param modelId
     * @param messages
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> imageGenerate(String modelId, String messages, AIChatParams params) {
        AssertUtils.assertNotEmpty("至少发送一条消息", messages);
        AssertUtils.assertNotEmpty("请选择图片大模型", modelId);
        AiragModel airagModel = airagModelMapper.getByIdIgnoreTenant(modelId);
        return this.imageGenerate(airagModel, messages, params);
    }

    /**
     * 文本生成图片
     *
     * @param airagModel
     * @param messages
     * @param params
     * @return
     */
    public List<Map<String, Object>> imageGenerate(AiragModel airagModel, String messages, AIChatParams params) {
        params = mergeParams(airagModel, params);
        try {
            return llmHandler.imageGenerate(messages, params);
        } catch (Exception e) {
            String errMsg = "调用绘画AI接口失败，详情请查看后台日志。";
            if (oConvertUtils.isNotEmpty(e.getMessage())) {
                // 根据常见异常关键字做细致翻译
                for (Map.Entry<String, String> entry : MODEL_ERROR_MAP.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (e.getMessage().contains(key)) {
                        errMsg = value;
                        break;
                    }
                }
            }
            log.error("AI模型调用异常: {}", errMsg, e);
            throw new JeecgBootException(errMsg);
        }
    }


    /**
     * 图生图
     * 
     * @param modelId
     * @param messages
     * @param images
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> imageEdit(String modelId, String messages, List<String> images, AIChatParams params) {
        AiragModel airagModel = airagModelMapper.getByIdIgnoreTenant(modelId);
        params = mergeParams(airagModel, params);
        List<String> originalImageBase64List = getFirstImageBase64(images);
        try {
            return llmHandler.imageEdit(messages, originalImageBase64List, params);
        } catch (Exception e) {
            String errMsg = "调用绘画AI接口失败，详情请查看后台日志。";
            if (oConvertUtils.isNotEmpty(e.getMessage())) {
                // 根据常见异常关键字做细致翻译
                for (Map.Entry<String, String> entry : MODEL_ERROR_MAP.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (errMsg.contains(key)) {
                        errMsg = value;
                        break;
                    }
                }
            }
            log.error("AI模型调用异常: {}", errMsg, e);
            throw new JeecgBootException(errMsg);
        }
    }

    /**
     * 需要将图片转换成Base64编码
     * @param images 图片路径列表
     * @return Base64编码字符串
     */
    private List<String> getFirstImageBase64(List<String> images) {
        List<String> originalImageBase64List = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (String imageUrl : images) {
                Matcher matcher = LLMConsts.WEB_PATTERN.matcher(imageUrl);
                try {
                    byte[] fileContent;
                    if (matcher.matches()) {
                        // 来源于网络
                        java.net.URL url = new java.net.URL(imageUrl);
                        java.net.URLConnection conn = url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(10000);
                        try (java.io.InputStream in = conn.getInputStream()) {
                            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
                            int nRead;
                            byte[] data = new byte[1024];
                            while ((nRead = in.read(data, 0, data.length)) != -1) {
                                buffer.write(data, 0, nRead);
                            }
                            buffer.flush();
                            fileContent = buffer.toByteArray();
                        }
                    } else {
                        // 本地文件
                        String filePath = uploadpath + File.separator + imageUrl;
                        SsrfFileTypeFilter.checkPathTraversal(filePath);
                        Path path = Paths.get(filePath);
                        fileContent = Files.readAllBytes(path);
                    }
                    originalImageBase64List.add(Base64.getEncoder().encodeToString(fileContent));
                } catch (Exception e) {
                    log.error("图片读取失败: {}", imageUrl, e);
                    throw new JeecgBootException("图片读取失败: " + imageUrl);
                }
            }
        }
        return originalImageBase64List;
    }
    //================================================= end 【QQYUN-12145】【AI】AI 绘画创作 ========================================
}
