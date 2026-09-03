package org.jeecg.modules.airag.llm.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.service.tool.ToolExecutor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.flow.consts.FlowConsts;
import org.jeecg.modules.airag.flow.entity.AiragFlow;
import org.jeecg.modules.airag.flow.service.IAiragFlowService;
import org.jeecg.modules.airag.flow.vo.api.SubFlowResult;
import org.jeecg.modules.airag.flow.vo.flow.config.FlowNodeConfig;
import org.jeecg.modules.airag.llm.consts.FlowPluginContent;
import org.jeecg.modules.airag.llm.entity.AiragMcp;
import org.jeecg.modules.airag.llm.handler.PluginToolBuilder;
import org.jeecg.modules.airag.llm.service.IAiragFlowPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Description: 流程同步到MCP服务实现类
 * @Author: wangshuai
 * @Date: 2025-12-22
 * @Version: V1.0
 */
@Service
@Slf4j
public class AiragFlowPluginServiceImpl implements IAiragFlowPluginService {

    @Autowired
    private IAiragFlowService airagFlowService;

    @Override
    public Map<String, Object> getFlowsToPlugin(String flowIds, String appId, String memoryId) {
        return doGetFlowsToPlugin(flowIds, appId, memoryId, null);
    }

    @Override
    public Map<String, Object> getFlowsToPlugin(String flowIds, String appId, String memoryId, List<String> images) {
        return doGetFlowsToPlugin(flowIds, appId, memoryId, images);
    }

    @Override
    public Map<String, Object> getFlowsToPlugin(String flowIds) {
        return doGetFlowsToPlugin(flowIds, null, null, null);
    }

    private Map<String, Object> doGetFlowsToPlugin(String flowIds, String appId, String memoryId, List<String> images) {
        log.info("开始构建流程插件");
        // 1. 查询所有启用的流程
        LambdaQueryWrapper<AiragFlow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AiragFlow::getStatus, FlowConsts.FLOW_STATUS_ENABLE, FlowConsts.FLOW_STATUS_RELEASE);
        queryWrapper.in(AiragFlow::getId, Arrays.asList(flowIds.split(SymbolConstant.COMMA)));
        List<AiragFlow> flows = airagFlowService.list(queryWrapper);
        HttpServletRequest httpServletRequest = SpringContextUtils.getHttpServletRequest();
        if (flows.isEmpty()) {
            log.info("当前应用所选流程没有启用的流程");
            return null;
        }
        //返回数据
        Map<String, Object> result = new HashMap<>();
        //插件
        //插件id
        AiragMcp tool = new AiragMcp();
        // 2. 构建插件
        String id = UUID.randomUUID().toString().replace("-", "");
        tool.setId(id);
        // 插件名称
        tool.setName(FlowPluginContent.PLUGIN_NAME);
        // 描述
        tool.setDescr(FlowPluginContent.PLUGIN_DESC);
        tool.setStatus(FlowConsts.FLOW_STATUS_ENABLE);
        tool.setSynced(CommonConstant.STATUS_1_INT);
        tool.setCategory("plugin");
        tool.setEndpoint("");
        int toolCount = 0;
        //构建拆件工具
        for (AiragFlow flow : flows) {
            try {

                SubFlowResult flowVo = new SubFlowResult(flow);
                // 获取入参参数
                JSONArray parameter = getInputParameter(flow, flowVo, images);
                // 获取出参参数
                JSONArray outParams = getOutputParameter(flow, flowVo);
                // name必须符合 ^[a-zA-Z0-9_-]+$
                String validToolName = FlowPluginContent.FLOW_TOOL_NAME_PREFIX + flow.getId();
                // 将原始名称拼接到描述中
                String description = flow.getName();
                if (oConvertUtils.isNotEmpty(flow.getDescr())) {
                    description += " : " + flow.getDescr();
                }
                // 构建插件请求路径（携带应用上下文参数）
                String pluginPath = FlowPluginContent.PLUGIN_REQUEST_URL + flow.getId();
                pluginPath = appendContextParams(pluginPath, appId, memoryId);
                //构造工具参数
                String flowTool = buildParameter(parameter, outParams, pluginPath, tool.getTools(), validToolName, description);
                tool.setTools(flowTool);
                toolCount++;
            } catch (Exception e) {
                log.error("处理流程[{}]转换插件失败: {}", flow.getName(), e.getMessage());
            }
        }
        String tenantId = TokenUtils.getTenantIdByRequest(httpServletRequest);
        //构建元数据（请求头）
        String meataData = buildMetadata(toolCount, tenantId);
        tool.setMetadata(meataData);
        Map<ToolSpecification, ToolExecutor> tools = PluginToolBuilder.buildTools(tool, httpServletRequest);
        result.put("pluginTool", tools);
        result.put("pluginId", id);
        log.info("构建流程插件结束");
        return result;
    }

    /**
     * 构建元数据
     *
     * @param toolCount
     * @param tenantId
     */
    private String buildMetadata(int toolCount, String tenantId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FlowPluginContent.TOKEN_PARAM_NAME, FlowPluginContent.X_ACCESS_TOKEN);
        jsonObject.put(FlowPluginContent.TOOL_COUNT, toolCount);
        jsonObject.put(FlowPluginContent.AUTH_TYPE, FlowPluginContent.TOKEN);
        jsonObject.put(FlowPluginContent.TOKEN_PARAM_VALUE, "");
        jsonObject.put(CommonConstant.TENANT_ID, oConvertUtils.getInt(tenantId, 0));
        return jsonObject.toJSONString();
    }

    /**
     * 构建参数
     *
     * @param parameter
     * @param outParams
     * @param pluginPath 插件请求路径（已包含appId等上下文参数）
     * @param tools
     * @param description
     * @param name
     */
    private String buildParameter(JSONArray parameter, JSONArray outParams, String pluginPath, String tools, String name, String description) {
        JSONArray paramArray = new JSONArray();
        JSONObject parameterObject = new JSONObject();
        parameterObject.put(FlowPluginContent.NAME, name);
        parameterObject.put(FlowPluginContent.DESCRIPTION, description);
        parameterObject.put(FlowPluginContent.PATH, pluginPath);
        parameterObject.put(FlowPluginContent.METHOD, FlowPluginContent.POST);
        parameterObject.put(FlowPluginContent.ENABLED, true);
        parameterObject.put(FlowPluginContent.PARAMETERS, parameter);
        parameterObject.put(FlowPluginContent.RESPONSES, outParams);
        if (oConvertUtils.isNotEmpty(tools)) {
            paramArray = JSONArray.parseArray(tools);
            paramArray.add(parameterObject);
        } else {
            paramArray.add(parameterObject);
        }
        return paramArray.toJSONString();
    }

    /**
     * 将应用上下文参数追加到插件请求路径中
     *
     * @param path     原始路径
     * @param appId    应用ID
     * @param memoryId 记忆库ID
     * @return 追加查询参数后的路径
     */
    private String appendContextParams(String path, String appId, String memoryId) {
        StringBuilder sb = new StringBuilder(path);
        boolean hasParam = false;
        if (oConvertUtils.isNotEmpty(appId)) {
            sb.append("?appId=").append(urlEncode(appId));
            hasParam = true;
        }
        if (oConvertUtils.isNotEmpty(memoryId)) {
            sb.append(hasParam ? "&" : "?").append("memoryId=").append(urlEncode(memoryId));
        }
        return sb.toString();
    }

    /**
     * URL编码
     */
    private String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * 获取参数
     *
     * @param flow
     * @param flowVo
     */
    private JSONArray getInputParameter(AiragFlow flow, SubFlowResult flowVo, List<String> images) {
        JSONArray parameters = new JSONArray();
/*
        String metadata = flow.getMetadata();
        if (oConvertUtils.isNotEmpty(metadata)) {
            JSONObject jsonObject = JSONObject.parseObject(metadata);
            if (jsonObject.containsKey(FlowPluginContent.INPUTS)) {
                JSONArray jsonArray = jsonObject.getJSONArray(FlowPluginContent.INPUTS);
                jsonArray.forEach(item -> {
                    if (oConvertUtils.isNotEmpty(item.toString())) {
                        JSONObject json = JSONObject.parseObject(item.toString());
                        json.put(FlowPluginContent.LOCATION, FlowPluginContent.LOCATION_BODY);
                    }
                });
                parameters.addAll(jsonArray);
            }
        }
*/
        // 仅使用开始节点的入参(flowVo.getInputParams())：其 name 取真实字段名(field),可正确作为请求体的key。
        // 不再叠加 metadata.inputs
        // 直接作为参数会导致请求体的key错误(取不到字段),且与开始节点入参重复(同一字段出现两次)。
        List<FlowNodeConfig.NodeParam> inputParams = flowVo.getInputParams();
        if (inputParams != null) {
            for (FlowNodeConfig.NodeParam param : inputParams) {
                String field = param.getField();
                // 历史记录由聊天服务在直连流程时自动注入,作为工具入参暴露给模型只会成为噪音且无法被有效填写
                if (FlowConsts.FLOW_INPUT_PARAM_HISTORY.equals(field)) {
                    continue;
                }
                JSONObject p = new JSONObject();
                // 参数名
                p.put(FlowPluginContent.NAME, param.getField());
                if (FlowConsts.FLOW_INPUT_PARAM_IMAGES.equals(field)) {
                    // 图片由服务端从当前聊天消息注入，避免模型自行编造或遗漏图片地址。
                    p.put("hidden", true);
                    p.put(FlowPluginContent.DEFAULT_VALUE, images);
                }
                // 参数描述
                String paramDesc = oConvertUtils.getString(param.getName(), param.getField());
                p.put(FlowPluginContent.DESCRIPTION, paramDesc);
                // 类型
                p.put(FlowPluginContent.TYPE, oConvertUtils.getString(param.getType(), FlowPluginContent.TYPE_STRING));
                // 所有参数都在Body中
                p.put(FlowPluginContent.LOCATION, FlowPluginContent.LOCATION_BODY);
                boolean required = param.getRequired() != null && param.getRequired();
                p.put(FlowPluginContent.REQUIRED, required);
                parameters.add(p);
            }
        }
        return parameters;
    }

    /**
     * 构建返回值
     */
    private JSONArray getOutputParameter(AiragFlow flow, SubFlowResult flowVo) {
        JSONArray parameters = new JSONArray();
        String metadata = flow.getMetadata();
        if (oConvertUtils.isNotEmpty(metadata)) {
            JSONObject jsonObject = JSONObject.parseObject(metadata);
            if (jsonObject.containsKey(FlowPluginContent.OUTPUTS)) {
                JSONArray jsonArray = jsonObject.getJSONArray(FlowPluginContent.OUTPUTS);
                parameters.addAll(jsonArray);
            }
        }
//        List<FlowNodeConfig.NodeParam> outputParams = flowVo.getOutputParams();
//        if (outputParams != null) {
//            for (FlowNodeConfig.NodeParam param : outputParams) {
//                JSONObject p = new JSONObject();
//                // 参数名
//                p.put("name", param.getField());
//                String paramDesc = param.getName();
//                if (oConvertUtils.isEmpty(paramDesc)) {
//                    paramDesc = param.getField();
//                }
//                // 参数描述
//                p.put("description", paramDesc);
//                // 类型
//                p.put("type", oConvertUtils.getString(param.getType(), "String"));
//                parameters.add(p);
//            }
//        }
        return parameters;
    }
}
