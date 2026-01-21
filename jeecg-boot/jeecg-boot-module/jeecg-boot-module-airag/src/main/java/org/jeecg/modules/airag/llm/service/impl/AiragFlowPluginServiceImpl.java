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
    public Map<String, Object> getFlowsToPlugin(String flowIds) {
        log.info("开始构建流程插件");
        // 1. 查询所有启用的流程
        LambdaQueryWrapper<AiragFlow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiragFlow::getStatus, FlowConsts.FLOW_STATUS_ENABLE);
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

                SubFlowResult subFlow = new SubFlowResult(flow);
                // 获取入参参数
                JSONArray parameter = getInputParameter(flow, subFlow);
                // 获取出参参数
                JSONArray outParams = getOutputParameter(flow, subFlow);
                // name必须符合 ^[a-zA-Z0-9_-]+$
                String validToolName = "flow_" + flow.getId();
                // 将原始名称拼接到描述中
                String description = flow.getName();
                if (oConvertUtils.isNotEmpty(flow.getDescr())) {
                    description += " : " + flow.getDescr();
                }
                //构造工具参数
                String flowTool = buildParameter(parameter, outParams, flow.getId(), tool.getTools(), validToolName, description);
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
     * @param flowId
     * @param tools
     * @param description
     * @param name
     */
    private String buildParameter(JSONArray parameter, JSONArray outParams, String flowId, String tools, String name, String description) {
        JSONArray paramArray = new JSONArray();
        JSONObject parameterObject = new JSONObject();
        parameterObject.put(FlowPluginContent.NAME, name);
        parameterObject.put(FlowPluginContent.DESCRIPTION, description);
        parameterObject.put(FlowPluginContent.PATH, FlowPluginContent.PLUGIN_REQUEST_URL + flowId);
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
     * 获取参数
     *
     * @param flow
     * @param subFlow
     */
    private JSONArray getInputParameter(AiragFlow flow, SubFlowResult subFlow) {
        JSONArray parameters = new JSONArray();
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
        //需要获取子流程的参数，子流程的参数是单独封装的，否则在流程执行的时候会报错缺少参数
        List<FlowNodeConfig.NodeParam> inputParams = subFlow.getInputParams();
        if (inputParams != null) {
            for (FlowNodeConfig.NodeParam param : inputParams) {
                JSONObject p = new JSONObject();
                // 参数名
                p.put(FlowPluginContent.NAME, param.getField());
                String paramDesc = param.getName();
                if (oConvertUtils.isEmpty(paramDesc)) {
                    paramDesc = param.getField();
                }
                // 参数描述
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
    private JSONArray getOutputParameter(AiragFlow flow, SubFlowResult subFlow) {
        JSONArray parameters = new JSONArray();
        String metadata = flow.getMetadata();
        if (oConvertUtils.isNotEmpty(metadata)) {
            JSONObject jsonObject = JSONObject.parseObject(metadata);
            if (jsonObject.containsKey(FlowPluginContent.OUTPUTS)) {
                JSONArray jsonArray = jsonObject.getJSONArray(FlowPluginContent.OUTPUTS);
                parameters.addAll(jsonArray);
            }
        }
//        List<FlowNodeConfig.NodeParam> outputParams = subFlow.getOutputParams();
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
