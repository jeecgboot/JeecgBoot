package org.jeecg.modules.airag.llm.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolExecutor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.RestUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.llm.entity.AiragMcp;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

/**
 * 插件工具构建器
 * 根据插件配置构建ToolSpecification和ToolExecutor
 * for [QQYUN-12453]【AI】支持插件
 *
 * @author chenrui
 * @date 2025/10/30
 */
@Slf4j
public class PluginToolBuilder {

    /**
     * 从插件配置构建工具Map
     *
     * @param airagMcp 插件配置
     * @return Map<ToolSpecification, ToolExecutor>
     */
    public static Map<ToolSpecification, ToolExecutor> buildTools(AiragMcp airagMcp, HttpServletRequest currentHttpRequest) {
        Map<ToolSpecification, ToolExecutor> tools = new HashMap<>();
        if (airagMcp == null || oConvertUtils.isEmpty(airagMcp.getTools())) {
            return tools;
        }

        try {
            JSONArray toolsArray = JSONArray.parseArray(airagMcp.getTools());
            if (toolsArray == null || toolsArray.isEmpty()) {
                return tools;
            }

            String baseUrl = airagMcp.getEndpoint();
            // 如果baseUrl为空，使用当前系统地址
            if (oConvertUtils.isEmpty(baseUrl)) {
                if (currentHttpRequest != null) {
                    baseUrl = CommonUtils.getBaseUrl(currentHttpRequest);
                    log.info("插件[{}]的BaseURL为空，使用系统地址: {}", airagMcp.getName(), baseUrl);
                } else {
                    log.warn("插件[{}]的BaseURL为空且无法获取系统地址，跳过工具构建", airagMcp.getName());
                    return tools;
                }
            }

            // 解析headers
            Map<String, String> headersMap = parseHeaders(airagMcp.getHeaders());
            
            // 解析并应用授权配置（从metadata中读取）
            applyAuthConfig(headersMap, airagMcp.getMetadata(), currentHttpRequest);

            for (int i = 0; i < toolsArray.size(); i++) {
                JSONObject toolConfig = toolsArray.getJSONObject(i);
                if (toolConfig == null) {
                    continue;
                }

                try {
                    ToolSpecification spec = buildToolSpecification(toolConfig);
                    ToolExecutor executor = buildToolExecutor(toolConfig, baseUrl, headersMap);
                    if (spec != null && executor != null) {
                        tools.put(spec, executor);
                    }
                } catch (Exception e) {
                    log.error("构建插件工具失败，工具配置: {}", toolConfig.toJSONString(), e);
                }
            }
        } catch (Exception e) {
            log.error("解析插件工具配置失败，插件: {}", airagMcp.getName(), e);
        }

        return tools;
    }

    /**
     * 构建ToolSpecification
     */
    private static ToolSpecification buildToolSpecification(JSONObject toolConfig) {
        String name = toolConfig.getString("name");
        String description = toolConfig.getString("description");

        if (oConvertUtils.isEmpty(name) || oConvertUtils.isEmpty(description)) {
            log.warn("工具配置缺少name或description字段");
            return null;
        }

        // 构建完整的描述信息（包含响应参数配置）
        StringBuilder fullDescription = new StringBuilder(description);
        
        // 解析响应参数并拼接到描述中
        JSONArray responses = toolConfig.getJSONArray("responses");
        if (responses != null && !responses.isEmpty()) {
            fullDescription.append("\n\n返回值说明：");
            for (int i = 0; i < responses.size(); i++) {
                JSONObject responseParam = responses.getJSONObject(i);
                if (responseParam == null) {
                    continue;
                }
                String paramName = responseParam.getString("name");
                String paramDesc = responseParam.getString("description");
                String paramType = responseParam.getString("type");

                if (oConvertUtils.isEmpty(paramName)) {
                    continue;
                }

                fullDescription.append("\n- ").append(paramName);
                if (oConvertUtils.isNotEmpty(paramType)) {
                    fullDescription.append(" (").append(paramType).append(")");
                }
                if (oConvertUtils.isNotEmpty(paramDesc)) {
                    fullDescription.append(": ").append(paramDesc);
                }
            }
        }

        JsonObjectSchema.Builder schemaBuilder = JsonObjectSchema.builder();

        // 解析请求参数
        JSONArray parameters = toolConfig.getJSONArray("parameters");
        if (parameters != null && !parameters.isEmpty()) {
            List<String> requiredParams = new ArrayList<>();
            for (int i = 0; i < parameters.size(); i++) {
                JSONObject param = parameters.getJSONObject(i);
                if (param == null) {
                    continue;
                }
                String paramName = param.getString("name");
                String paramDesc = param.getString("description");
                String paramType = param.getString("type");

                if (oConvertUtils.isEmpty(paramName)) {
                    continue;
                }

                // 根据参数类型添加属性
                if ("String".equalsIgnoreCase(paramType) || "string".equalsIgnoreCase(paramType)) {
                    schemaBuilder.addStringProperty(paramName, paramDesc != null ? paramDesc : "");
                } else if ("Number".equalsIgnoreCase(paramType) || "number".equalsIgnoreCase(paramType)
                        || "Integer".equalsIgnoreCase(paramType) || "integer".equalsIgnoreCase(paramType)) {
                    schemaBuilder.addNumberProperty(paramName, paramDesc != null ? paramDesc : "");
                } else if ("Boolean".equalsIgnoreCase(paramType) || "boolean".equalsIgnoreCase(paramType)) {
                    schemaBuilder.addBooleanProperty(paramName, paramDesc != null ? paramDesc : "");
                } else {
                    // 默认作为String处理
                    schemaBuilder.addStringProperty(paramName, paramDesc != null ? paramDesc : "");
                }

                // 检查是否必须
                Boolean required = param.getBooleanValue("required");
                if (required != null && required) {
                    requiredParams.add(paramName);
                }
            }

            if (!requiredParams.isEmpty()) {
                schemaBuilder.required(requiredParams.toArray(new String[0]));
            }
        }

        return ToolSpecification.builder()
                .name(name)
                .description(fullDescription.toString())
                .parameters(schemaBuilder.build())
                .build();
    }

    /**
     * 构建ToolExecutor
     */
    private static ToolExecutor buildToolExecutor(JSONObject toolConfig, String baseUrl, Map<String, String> defaultHeaders) {
        String path = toolConfig.getString("path");
        String method = toolConfig.getString("method");
        JSONArray parameters = toolConfig.getJSONArray("parameters");

        if (oConvertUtils.isEmpty(path) || oConvertUtils.isEmpty(method)) {
            log.warn("工具配置缺少path或method字段");
            return null;
        }

        return (toolExecutionRequest, memoryId) -> {
            try {
                // 解析AI传入的参数
                JSONObject args = JSONObject.parseObject(toolExecutionRequest.arguments());

                // 构建完整URL
                String url = buildUrl(baseUrl, path, parameters, args);

                // 构建请求方法
                HttpMethod httpMethod = parseHttpMethod(method);

                // 构建请求头
                HttpHeaders httpHeaders = buildHttpHeaders(parameters, args, defaultHeaders);

                // 构建请求参数
                JSONObject urlVariables = buildUrlVariables(parameters, args);
                Object body = buildRequestBody(parameters, args, httpHeaders);

                // 发送HTTP请求
                ResponseEntity<String> response = RestUtil.request(url, httpMethod, httpHeaders, urlVariables, body, String.class);

                // 直接返回原始响应字符串，不进行解析
                return response.getBody() != null ? response.getBody() : "";
            } catch (HttpClientErrorException e) {
                log.error("插件工具HTTP请求失败: {}", e.getMessage(), e);
                return "请求失败: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
            } catch (Exception e) {
                log.error("插件工具执行失败: {}", e.getMessage(), e);
                return "工具执行失败: " + e.getMessage();
            }
        };
    }

    /**
     * 构建完整URL（处理Path参数）
     */
    private static String buildUrl(String baseUrl, String path, JSONArray parameters, JSONObject args) {
        String fullPath = path;
        if (!path.startsWith("/")) {
            fullPath = "/" + path;
        }
        // 拼接URL时防止出现双斜杠
        if (baseUrl.endsWith("/") && fullPath.startsWith("/")) {
            fullPath = fullPath.substring(1);
        }
        String url = baseUrl + fullPath;

        // 替换Path参数
        if (parameters != null && args != null) {
            for (int i = 0; i < parameters.size(); i++) {
                JSONObject param = parameters.getJSONObject(i);
                if (param == null) {
                    continue;
                }
                String paramName = param.getString("name");
                String paramLocation = param.getString("location");

                if (!"Path".equalsIgnoreCase(paramLocation)) {
                    continue;
                }

                Object value = args.get(paramName);
                if (value != null) {
                    url = url.replace("{" + paramName + "}", value.toString());
                }
            }
        }

        return url;
    }

    /**
     * 构建请求头
     */
    private static HttpHeaders buildHttpHeaders(JSONArray parameters, JSONObject args, Map<String, String> defaultHeaders) {
        HttpHeaders httpHeaders = new HttpHeaders();

        // 添加默认请求头
        if (defaultHeaders != null) {
            defaultHeaders.forEach(httpHeaders::set);
        }

        // 添加Header类型的参数
        if (parameters != null && args != null) {
            for (int i = 0; i < parameters.size(); i++) {
                JSONObject param = parameters.getJSONObject(i);
                if (param == null) {
                    continue;
                }
                String paramName = param.getString("name");
                String paramLocation = param.getString("location");

                if (!"Header".equalsIgnoreCase(paramLocation)) {
                    continue;
                }

                Object value = args.get(paramName);
                if (value != null) {
                    httpHeaders.set(paramName, value.toString());
                }
            }
        }

        // 如果请求体不为空且没有设置Content-Type，默认设置为application/json
        if (!httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }

        return httpHeaders;
    }

    /**
     * 构建URL查询参数
     */
    private static JSONObject buildUrlVariables(JSONArray parameters, JSONObject args) {
        JSONObject urlVariables = new JSONObject();

        if (parameters == null || args == null) {
            return urlVariables;
        }

        for (int i = 0; i < parameters.size(); i++) {
            JSONObject param = parameters.getJSONObject(i);
            if (param == null) {
                continue;
            }
            String paramName = param.getString("name");
            String paramLocation = param.getString("location");

            String location = paramLocation != null ? paramLocation : "";
            // 显式指定Query类型，或者未指定类型（默认作为Query）
            boolean isQueryParam = "Query".equalsIgnoreCase(location);
            boolean isOtherType = "Body".equalsIgnoreCase(location) || "Form-Data".equalsIgnoreCase(location)
                    || "Header".equalsIgnoreCase(location) || "Path".equalsIgnoreCase(location);
            
            if (isQueryParam || !isOtherType) {
                Object value = args.get(paramName);
                if (value != null) {
                    urlVariables.put(paramName, value);
                }
            }
        }

        return urlVariables.isEmpty() ? null : urlVariables;
    }

    /**
     * 构建请求体
     */
    private static Object buildRequestBody(JSONArray parameters, JSONObject args, HttpHeaders httpHeaders) {
        if (parameters == null || args == null) {
            return null;
        }

        boolean hasBody = false;
        boolean hasFormData = false;

        // 检查是否有Body或Form-Data类型的参数
        for (int i = 0; i < parameters.size(); i++) {
            JSONObject param = parameters.getJSONObject(i);
            if (param == null) {
                continue;
            }
            String paramLocation = param.getString("location");

            if ("Body".equalsIgnoreCase(paramLocation)) {
                hasBody = true;
            } else if ("Form-Data".equalsIgnoreCase(paramLocation)) {
                hasFormData = true;
            }
        }

        // Body和Form-Data互斥
        if (hasBody && hasFormData) {
            log.warn("工具配置同时包含Body和Form-Data类型参数，优先使用Body");
            hasFormData = false;
        }

        if (hasBody) {
            // Body类型：构建JSON对象
            JSONObject body = new JSONObject();
            for (int i = 0; i < parameters.size(); i++) {
                JSONObject param = parameters.getJSONObject(i);
                if (param == null) {
                    continue;
                }
                String paramName = param.getString("name");
                String paramLocation = param.getString("location");

                if (!"Body".equalsIgnoreCase(paramLocation) ) {
                    continue;
                }

                Object value = args.get(paramName);
                if (value != null) {
                    body.put(paramName, value);
                } else {
                    // 检查是否有默认值
                    String defaultValue = param.getString("defaultValue");
                    if (oConvertUtils.isNotEmpty(defaultValue)) {
                        body.put(paramName, defaultValue);
                    }
                }
            }
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return body.isEmpty() ? null : body;
        } else if (hasFormData) {
            // Form-Data类型：构建JSON对象（RestUtil会处理）
            JSONObject formData = new JSONObject();
            for (int i = 0; i < parameters.size(); i++) {
                JSONObject param = parameters.getJSONObject(i);
                if (param == null) {
                    continue;
                }
                String paramName = param.getString("name");
                String paramLocation = param.getString("location");

                if (!"Form-Data".equalsIgnoreCase(paramLocation)) {
                    continue;
                }

                Object value = args.get(paramName);
                if (value != null) {
                    formData.put(paramName, value);
                } else {
                    String defaultValue = param.getString("defaultValue");
                    if (oConvertUtils.isNotEmpty(defaultValue)) {
                        formData.put(paramName, defaultValue);
                    }
                }
            }
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            return formData.isEmpty() ? null : formData;
        }

        return null;
    }

    /**
     * 解析HTTP方法
     */
    private static HttpMethod parseHttpMethod(String method) {
        try {
            return HttpMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("无效的HTTP方法: {}，使用默认GET", method);
            return HttpMethod.GET;
        }
    }


    /**
     * 解析headers JSON字符串为Map
     */
    private static Map<String, String> parseHeaders(String headersStr) {
        Map<String, String> headersMap = new HashMap<>();
        if (oConvertUtils.isEmpty(headersStr)) {
            return headersMap;
        }

        try {
            JSONObject headersJson = JSONObject.parseObject(headersStr);
            if (headersJson != null) {
                headersJson.forEach((key, value) -> {
                    if (value != null) {
                        headersMap.put(key, value.toString());
                    }
                });
            }
        } catch (Exception e) {
            log.warn("解析headers失败: {}", headersStr);
        }

        return headersMap;
    }

    /**
     * 应用授权配置到headers
     * 从metadata中读取授权配置，如果是Token授权，添加到headers中
     * 如果授权类型为token但没有设置token值，则从TokenUtils获取当前请求的token
     *
     * @param headersMap 请求头Map
     * @param metadataStr 元数据JSON字符串
     */
    private static void applyAuthConfig(Map<String, String> headersMap, String metadataStr, HttpServletRequest currentHttpRequest) {
        if (oConvertUtils.isEmpty(metadataStr)) {
            return;
        }

        try {
            JSONObject metadata = JSONObject.parseObject(metadataStr);
            if (metadata == null) {
                return;
            }

            String authType = metadata.getString("authType");
            if (oConvertUtils.isEmpty(authType) || !"token".equalsIgnoreCase(authType)) {
                return;
            }

            // Token授权方式：从metadata中获取token配置并添加到headers
            String tokenParamName = metadata.getString("tokenParamName");
            String tokenParamValue = metadata.getString("tokenParamValue");

            // 如果token参数名存在，但token值未设置，尝试从TokenUtils获取当前请求的token
            if (oConvertUtils.isNotEmpty(tokenParamName) && oConvertUtils.isEmpty(tokenParamValue)) {
                try {
                    // 注意：TokenUtils需要获取当前线程的request，所以必须在同步调用中使用
                    String currentToken = TokenUtils.getTokenByRequest();
                    if(oConvertUtils.isEmpty(currentToken) && currentHttpRequest != null) {
                        currentToken = TokenUtils.getTokenByRequest(currentHttpRequest);
                    }
                    if (oConvertUtils.isNotEmpty(currentToken)) {
                        tokenParamValue = currentToken;
                        log.debug("从TokenUtils获取Token并添加到请求头: {} = {}", tokenParamName, 
                            currentToken.length() > 10 ? currentToken.substring(0, 10) + "..." : currentToken);
                    } else {
                        log.warn("Token授权配置中tokenParamValue为空，且无法从TokenUtils获取当前请求的token");
                    }
                } catch (Exception e) {
                    log.warn("从TokenUtils获取token失败: {}", e.getMessage());
                }
            }

            if (oConvertUtils.isNotEmpty(tokenParamName) && oConvertUtils.isNotEmpty(tokenParamValue)) {
                // 如果headers中已存在同名header，优先使用metadata中的配置（覆盖）
                headersMap.put(tokenParamName, tokenParamValue);
                // 日志中只显示token的前几个字符，避免泄露完整token
                String tokenPreview = tokenParamValue.length() > 10 
                    ? tokenParamValue.substring(0, 10) + "..." 
                    : tokenParamValue;
                log.debug("添加Token授权到请求头: {} = {}", tokenParamName, tokenPreview);
            } else {
                log.warn("Token授权配置不完整: tokenParamName={}, tokenParamValue={}", tokenParamName, tokenParamValue != null ? "***" : null);
            }
        } catch (Exception e) {
            log.warn("解析授权配置失败: {}", metadataStr, e);
        }
    }
}

