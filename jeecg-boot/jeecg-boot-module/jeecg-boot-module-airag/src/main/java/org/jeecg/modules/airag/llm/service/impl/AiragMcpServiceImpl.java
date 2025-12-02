package org.jeecg.modules.airag.llm.service.impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.model.chat.request.json.*;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.llm.entity.AiragMcp;
import org.jeecg.modules.airag.llm.mapper.AiragMcpMapper;
import org.jeecg.modules.airag.llm.service.IAiragMcpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: MCP
 * @Author: jeecg-boot
 * @Date: 2025-10-20
 * @Version: V1.0
 */
@Service("airagMcpServiceImpl")
@Slf4j
public class AiragMcpServiceImpl extends ServiceImpl<AiragMcpMapper, AiragMcp> implements IAiragMcpService {

    @Autowired
    private ObjectMapper objectMapper; // 使用全局配置的 Jackson ObjectMapper

    /**
     * 新增或编辑Mcpserver
     *
     * @param airagMcp MCP对象
     * @return 返回保存后的MCP对象
     * @author chenrui
     * @date 2025/10/21
     */
    @Override
    public Result<String> edit(AiragMcp airagMcp) {
        // 校验必填项
        if (airagMcp.getName() == null || airagMcp.getName().trim().isEmpty()) {
            return Result.error("名称不能为空");
        }
        //update-begin---author:chenrui ---date:20251031  for：[QQYUN-12453]【AI】支持插件------------
        // 设置默认category
        if (oConvertUtils.isEmpty(airagMcp.getCategory())) {
            airagMcp.setCategory("mcp");
        }
        // 对于MCP类型，需要校验type和endpoint
        if ("mcp".equalsIgnoreCase(airagMcp.getCategory())) {
            if (airagMcp.getType() == null || airagMcp.getType().trim().isEmpty()) {
                return Result.error("MCP类型不能为空");
            }
            if (airagMcp.getEndpoint() == null || airagMcp.getEndpoint().trim().isEmpty()) {
                return Result.error("服务端点不能为空");
            }
        } else if ("plugin".equalsIgnoreCase(airagMcp.getCategory())) {
            // 对于插件类型，BaseURL可选，不填时使用当前系统地址
            // 不再校验endpoint是否为空
        } else {
            // 未知类型，默认为MCP并校验
            if (airagMcp.getEndpoint() == null || airagMcp.getEndpoint().trim().isEmpty()) {
                return Result.error("服务端点不能为空");
            }
        }
        //update-end---author:chenrui ---date:20251031  for：[QQYUN-12453]【AI】支持插件------------

        if (airagMcp.getId() == null || airagMcp.getId().trim().isEmpty()) {
            // 设置默认值
            airagMcp.setStatus("enable");
            //update-begin---author:chenrui ---date:20251031  for：[QQYUN-12453]【AI】支持插件------------
            // 只有MCP类型才设置synced字段，插件类型不需要同步默认为已同步
            if ("mcp".equalsIgnoreCase(airagMcp.getCategory())) {
                airagMcp.setSynced(CommonConstant.STATUS_0_INT);
            } else {
                airagMcp.setSynced(CommonConstant.STATUS_1_INT);
            }
            //update-end---author:chenrui ---date:20251031  for：[QQYUN-12453]【AI】支持插件------------
            // 新增
            this.save(airagMcp);
        } else {
            // 编辑
            this.updateById(airagMcp);
        }
        return Result.OK("保存成功");
    }

    /**
     * 同步mcp的工具列表
     *
     * @param id mcp主键
     * @return 工具列表
     * @author chenrui
     * @date 2025/10/21
     */
    @Override
    public Result<?> sync(String id) {
        AiragMcp mcp = this.getById(id);
        if (mcp == null) {
            return Result.error("未找到对应的MCP对象");
        }
        //update-begin---author:chenrui ---date:20251031  for：[QQYUN-12453]【AI】支持插件------------
        // 只有MCP类型才支持同步，插件类型不支持
        String category = mcp.getCategory();
        if (oConvertUtils.isEmpty(category)) {
            category = "mcp"; // 兼容旧数据
        }
        if (!"mcp".equalsIgnoreCase(category)) {
            return Result.error("只有MCP类型才支持同步操作");
        }
        //update-end---author:chenrui ---date:20251031  for：[QQYUN-12453]【AI】支持插件------------
        String type = mcp.getType();
        String endpoint = mcp.getEndpoint();
        Map<String, String> headers = null;
        if (oConvertUtils.isNotEmpty(mcp.getHeaders())) {
            try {
                headers = JSONObject.parseObject(mcp.getHeaders(), Map.class);
            } catch (JSONException e) {
                headers = null;
            }
        }
        if (type == null || endpoint == null) {
            return Result.error("MCP类型或端点为空");
        }
        McpClient mcpClient = null;
        try {
            if ("sse".equalsIgnoreCase(type)) {
                HttpMcpTransport.Builder builder = new HttpMcpTransport.Builder()
                        .sseUrl(endpoint)
                        .logRequests(true)
                        .logResponses(true);
                mcpClient = new DefaultMcpClient.Builder().transport(builder.build()).build();
            } else if ("stdio".equalsIgnoreCase(type)) {
                // stdio 类型：endpoint 可能是一个命令行，需要拆分为命令列表
//                List<String> cmdParts = Arrays.asList(endpoint.trim().split("\\s+"));
//                StdioMcpTransport.Builder builder = new StdioMcpTransport.Builder()
//                        .command(cmdParts)
//                        .environment(headers);
//                mcpClient = new DefaultMcpClient.Builder().transport(builder.build()).build();
                return Result.error("不支持的MCP类型:" + type);
            } else {
                return Result.error("不支持的MCP类型:" + type);
            }
            List<ToolSpecification> toolSpecifications = mcpClient.listTools();
            // 先尝试直接使用 ObjectMapper 序列化，若结果为 {} 则回退到反射 Map
            List<Map<String, Object>> specMaps = toolSpecifications.stream()
                    .map(spec -> {
                        try {
                            String raw = objectMapper.writeValueAsString(spec);
                            if (raw != null && raw.length() > 2) {
                                // 直接反序列化成 Map，保留 Jackson 认出的字段
                                return objectMapper.readValue(raw, new TypeReference<Map<String, Object>>() {
                                });
                            }
                        } catch (Exception ignore) {
                        }
                        return convertToolSpec(spec);
                    })
                    .collect(Collectors.toList());
            String jsonList;
            try {
                jsonList = objectMapper.writeValueAsString(specMaps);
            } catch (JsonProcessingException e) {
                jsonList = JSONObject.toJSONString(specMaps);
            }
            String firstJson = specMaps.isEmpty() ? "null" : safeWriteJson(specMaps.get(0));
            log.info("MCP工具列表 id={}, size={}, first={}", id, toolSpecifications.size(), firstJson);
            mcp.setTools(jsonList);
            mcp.setSynced(1);

            Map<String,Object> metadata = new HashMap<>();
            metadata.put("tool_count", toolSpecifications.size());
            mcp.setMetadata(objectMapper.writeValueAsString(metadata));
            this.updateById(mcp);
            return Result.OK(specMaps);
        } catch (Exception e) {
            String message = e.getMessage();
            if (e instanceof IllegalArgumentException) {
                message = "，MCP客户端参数错误";
            }
            log.error("同步MCP工具失败 id={}, error={}", id, message, e);
            return Result.error("同步失败" + message);
        } finally {
            if (mcpClient != null) {
                try {
                    Method closeMethod = mcpClient.getClass().getMethod("close");
                    closeMethod.invoke(mcpClient);
                } catch (NoSuchMethodException ignore) {
                } catch (Exception ex) {
                    log.warn("关闭MCP客户端失败 id={}, error={}", id, ex.getMessage());
                }
            }
        }
    }

    // 安全序列化单个对象为 JSON 字符串
    private String safeWriteJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return String.valueOf(obj);
        }
    }

    // 反射将 ToolSpecification 转成 Map，兼容 record/私有字段/仅 Jackson 注解场景 -> 改为直接调用访问器
    private Map<String, Object> convertToolSpec(ToolSpecification spec) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (spec == null) {
            return map;
        }
        map.put("name", spec.name());
        map.put("description", spec.description());
        try {
            Object params = spec.parameters();
            if (params != null) {
                JsonObjectSchema obj = (JsonObjectSchema) params;
                List<Map<String, Object>> fields = new ArrayList<>();
                if (obj.properties() != null) {
                    obj.properties().forEach((fieldName, fieldSchema) -> {
                        Map<String, Object> fieldMap = new LinkedHashMap<>();
                        fieldMap.put("name", fieldName);
                        fieldMap.put("description", extractDescription(fieldSchema));
                        // 若需要标记必填
                        if (obj.required() != null && obj.required().contains(fieldName)) {
                            fieldMap.put("required", true);
                        }
                        fields.add(fieldMap);
                    });
                }
                map.put("parameters", fields);
            }
        } catch (Exception ignored) {
        }
        return map;
    }

    // 提取各类型 schema 的描述
    private String extractDescription(Object schema) {
        if (schema == null) return null;
        try {
            if (schema instanceof JsonStringSchema) return ((JsonStringSchema) schema).description();
            if (schema instanceof JsonNumberSchema) return ((JsonNumberSchema) schema).description();
            if (schema instanceof JsonBooleanSchema) return ((JsonBooleanSchema) schema).description();
            if (schema instanceof JsonArraySchema) return ((JsonArraySchema) schema).description();
            if (schema instanceof JsonEnumSchema) return ((JsonEnumSchema) schema).description();
            if (schema instanceof JsonObjectSchema) return ((JsonObjectSchema) schema).description();
        } catch (Exception ignored) {
        }
        return schema.toString();
    }

    /**
     * 修改状态
     *
     * @param id     MCP主键
     * @param action 操作（enable/disable）
     * @return 操作结果
     * @author chenrui
     * @date 2025/10/21 11:00
     */
    @Override
    public Result<?> toggleStatus(String id, String action) {
        if (oConvertUtils.isEmpty(id)) {
            return Result.error("id不能为空");
        }
        if (oConvertUtils.isEmpty(action)) {
            return Result.error("action不能为空");
        }
        String normalized = action.toLowerCase();
        if (!"enable".equals(normalized) && !"disable".equals(normalized)) {
            return Result.error("action只能为enable或disable");
        }
        AiragMcp mcp = this.getById(id);
        if (mcp == null) {
            return Result.error("未找到对应的MCP服务");
        }
        if (normalized.equalsIgnoreCase(mcp.getStatus())) {
            return Result.OK("操作成功");
        }
        mcp.setStatus(normalized);
        this.updateById(mcp);
        return Result.OK("操作成功");
    }

    /**
     * 保存插件工具（仅更新tools字段）
     * for [QQYUN-12453]【AI】支持插件
     * @param id 插件ID
     * @param tools 工具列表JSON字符串
     * @return 操作结果
     * @author chenrui
     * @date 2025/10/30
     */
    @Override
    public Result<String> saveTools(String id, String tools) {
        if (oConvertUtils.isEmpty(id)) {
            return Result.error("插件ID不能为空");
        }
        AiragMcp mcp = this.getById(id);
        if (mcp == null) {
            return Result.error("未找到对应的插件");
        }
        // 验证是否为插件类型
        String category = mcp.getCategory();
        if (oConvertUtils.isEmpty(category)) {
            category = "mcp"; // 兼容旧数据
        }
        if (!"plugin".equalsIgnoreCase(category)) {
            return Result.error("只有插件类型才能保存工具");
        }
        // 更新tools字段
        mcp.setTools(tools);
        
        // 更新metadata中的tool_count
        try {
            com.alibaba.fastjson.JSONArray toolsArray = com.alibaba.fastjson.JSONArray.parseArray(tools);
            int toolCount = toolsArray != null ? toolsArray.size() : 0;
            
            // 解析现有metadata
            JSONObject metadata = new JSONObject();
            if (oConvertUtils.isNotEmpty(mcp.getMetadata())) {
                try {
                    JSONObject metadataJson = JSONObject.parseObject(mcp.getMetadata());
                    if (metadataJson != null) {
                        metadata.putAll(metadataJson);
                    }
                } catch (Exception e) {
                    log.warn("解析metadata失败，将重新创建: {}", mcp.getMetadata());
                }
            }
            
            // 更新tool_count
            metadata.put("tool_count", toolCount);
            
            // 保存metadata
            mcp.setMetadata(metadata.toJSONString());
        } catch (Exception e) {
            log.warn("更新工具数量失败: {}", e.getMessage());
            // 即使更新tool_count失败，也不影响保存tools
        }
        
        this.updateById(mcp);
        return Result.OK("保存成功");
    }
}
