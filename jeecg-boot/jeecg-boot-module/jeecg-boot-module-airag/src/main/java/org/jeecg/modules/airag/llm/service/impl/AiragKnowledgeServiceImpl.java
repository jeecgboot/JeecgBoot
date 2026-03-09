package org.jeecg.modules.airag.llm.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.service.tool.ToolExecutor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.flow.consts.FlowConsts;
import org.jeecg.modules.airag.llm.consts.FlowPluginContent;
import org.jeecg.modules.airag.llm.entity.AiragKnowledge;
import org.jeecg.modules.airag.llm.entity.AiragMcp;
import org.jeecg.modules.airag.llm.handler.PluginToolBuilder;
import org.jeecg.modules.airag.llm.mapper.AiragKnowledgeMapper;
import org.jeecg.modules.airag.llm.service.IAiragKnowledgeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: AIRag知识库
 * @Author: jeecg-boot
 * @Date: 2025-02-18
 * @Version: V1.0
 */
@Slf4j
@Service
public class AiragKnowledgeServiceImpl extends ServiceImpl<AiragKnowledgeMapper, AiragKnowledge> implements IAiragKnowledgeService {
    
    @Override
    public Map<String, Object> getPluginMemory(String memoryId) {
        //step 1获取知识库
        AiragKnowledge airagKnowledge = this.baseMapper.selectById(memoryId);
        if(airagKnowledge == null){
            return null;
        }
        return this.getKnowledgeToPlugin(memoryId,airagKnowledge.getDescr());
    }

    /**
     * 获取插件信息
     *
     * @param knowledgeId
     * @param descr
     * @return
     */
    public Map<String, Object> getKnowledgeToPlugin(String knowledgeId, String descr) {
        //step1 构建插件
        log.info("开始构建记忆库插件");
        if (oConvertUtils.isEmpty(knowledgeId)) {
            return null;
        }

        HttpServletRequest httpServletRequest = SpringContextUtils.getHttpServletRequest();

        //返回数据
        Map<String, Object> result = new HashMap<>();
        //插件
        //插件id
        AiragMcp tool = new AiragMcp();
        // 2. 构建插件
        tool.setId(knowledgeId);
        // 插件名称
        tool.setName(FlowPluginContent.PLUGIN_MEMORY_NAME);
        // 描述
        tool.setDescr(FlowPluginContent.PLUGIN_MEMORY_DESC);
        tool.setStatus(FlowConsts.FLOW_STATUS_ENABLE);
        tool.setSynced(CommonConstant.STATUS_1_INT);
        tool.setCategory("plugin");
        tool.setEndpoint("");

        JSONArray toolsArray = new JSONArray();
        // 添加记忆
        toolsArray.add(buildAddMemoryTool(knowledgeId,descr));
        // 查询记忆
        toolsArray.add(buildQueryMemoryTool(knowledgeId,descr));
        tool.setTools(toolsArray.toJSONString());
        String tenantId = TokenUtils.getTenantIdByRequest(httpServletRequest);
        //构建元数据（请求头）
        String meataData = buildMetadata(tenantId);
        tool.setMetadata(meataData);
        Map<ToolSpecification, ToolExecutor> tools = PluginToolBuilder.buildTools(tool, httpServletRequest);
        result.put("pluginTool", tools);
        result.put("pluginId", knowledgeId);
        log.info("构建记忆库插件结束");
        return result;
    }

    /**
     * 构建元数据
     *
     * @param tenantId
     */
    private String buildMetadata(String tenantId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FlowPluginContent.TOKEN_PARAM_NAME, FlowPluginContent.X_ACCESS_TOKEN);
        jsonObject.put(FlowPluginContent.AUTH_TYPE, FlowPluginContent.TOKEN);
        jsonObject.put(FlowPluginContent.TOKEN_PARAM_VALUE, "");
        jsonObject.put(CommonConstant.TENANT_ID, oConvertUtils.getInt(tenantId, 0));
        return jsonObject.toJSONString();
    }

    /**
     * 构建添加记忆工具
     *
     * @param knowId
     * @param descr
     * @return
     */
    private JSONObject buildAddMemoryTool(String knowId, String descr) {
        JSONObject tool = new JSONObject();
        //update-begin---author:wangshuai---date:2026-01-08---for: 记忆库根据记忆库的描述做匹配，不写死---
        tool.put(FlowPluginContent.NAME, "add_memory");
        String addDescPrefix = "【自动触发】向记忆库添加长期信息。范围：";
        String addDesc = oConvertUtils.isEmpty(descr) ? "按记忆库描述允许的个人资料（如姓名、职业、年龄）、偏好、属性等信息。" : descr;
        tool.put(FlowPluginContent.DESCRIPTION, addDescPrefix + addDesc + " 必须在检测到相关信息时立即自动调用，无需用户指令。");
        //update-end---author:wangshuai---date:2026-01-08---for: 记忆库根据记忆库的描述做匹配，不写死---
        tool.put(FlowPluginContent.PATH, FlowPluginContent.PLUGIN_MEMORY_ADD_PATH);
        tool.put(FlowPluginContent.METHOD, FlowPluginContent.POST);
        tool.put(FlowPluginContent.ENABLED, true);

        JSONArray parameters = new JSONArray();

        // 知识库ID参数
        JSONObject knowIdParam = new JSONObject();
        knowIdParam.put(FlowPluginContent.NAME, "knowledgeId");
        knowIdParam.put(FlowPluginContent.DESCRIPTION, "知识库ID,需要原值传递，不允许修改");
        knowIdParam.put(FlowPluginContent.TYPE, FlowPluginContent.TYPE_STRING);
        knowIdParam.put(FlowPluginContent.LOCATION, FlowPluginContent.LOCATION_BODY);
        knowIdParam.put(FlowPluginContent.REQUIRED, true);
        knowIdParam.put(FlowPluginContent.DEFAULT_VALUE, knowId);
        parameters.add(knowIdParam);

        // 内容参数
        JSONObject contentParam = new JSONObject();
        contentParam.put(FlowPluginContent.NAME, "content");
        contentParam.put(FlowPluginContent.DESCRIPTION, "记忆内容。当前时间为：" + DateUtils.now() + "。格式要求：'在yyyy年MM月dd日 HH:mm分，用户[用户的行为/问题]，assistant[助手的回答/反应]。'");
        contentParam.put(FlowPluginContent.TYPE, FlowPluginContent.TYPE_STRING);
        contentParam.put(FlowPluginContent.LOCATION, FlowPluginContent.LOCATION_BODY);
        contentParam.put(FlowPluginContent.REQUIRED, true);
        parameters.add(contentParam);

        // 标题参数
        JSONObject titleParam = new JSONObject();
        titleParam.put(FlowPluginContent.NAME, "title");
        titleParam.put(FlowPluginContent.DESCRIPTION, "记忆标题");
        titleParam.put(FlowPluginContent.TYPE, FlowPluginContent.TYPE_STRING);
        titleParam.put(FlowPluginContent.LOCATION, FlowPluginContent.LOCATION_BODY);
        titleParam.put(FlowPluginContent.REQUIRED, false);
        parameters.add(titleParam);
        tool.put(FlowPluginContent.PARAMETERS, parameters);

        // 响应
        JSONArray responses = new JSONArray();
        tool.put(FlowPluginContent.RESPONSES, responses);

        return tool;
    }

    /**
     * 构建查询记忆工具
     *
     * @param knowId
     * @param descr
     * @return
     */
    private JSONObject buildQueryMemoryTool(String knowId, String descr) {
        JSONObject tool = new JSONObject();
        //update-begin---author:wangshuai---date:2026-01-08---for: 记忆库根据记忆库的描述做匹配，不写死---
        String addDescPrefix = "【自动触发】向记忆库检索信息。范围：";
        String addDesc = oConvertUtils.isEmpty(descr) ? "按记忆库描述允许的个人资料（如姓名、职业、年龄）、偏好、属性等信息。" : descr;
        tool.put(FlowPluginContent.NAME, "query_memory");
        tool.put(FlowPluginContent.DESCRIPTION, addDescPrefix + addDesc + " 必须在检测到相关信息时立即自动调用，无需用户指令。");
        //update-end---author:wangshuai---date:2026-01-08---for: 记忆库根据记忆库的描述做匹配，不写死---
        tool.put(FlowPluginContent.PATH, FlowPluginContent.PLUGIN_MEMORY_QUERY_PATH);
        tool.put(FlowPluginContent.METHOD, FlowPluginContent.POST);
        tool.put(FlowPluginContent.ENABLED, true);

        JSONArray parameters = new JSONArray();

        // 知识库ID参数
        JSONObject knowIdParam = new JSONObject();
        knowIdParam.put(FlowPluginContent.NAME, "knowledgeId");
        knowIdParam.put(FlowPluginContent.DESCRIPTION, "知识库ID,需要原值传递，不允许修改");
        knowIdParam.put(FlowPluginContent.TYPE, FlowPluginContent.TYPE_STRING);
        knowIdParam.put(FlowPluginContent.LOCATION, FlowPluginContent.LOCATION_BODY);
        knowIdParam.put(FlowPluginContent.REQUIRED, true);
        knowIdParam.put(FlowPluginContent.DEFAULT_VALUE, knowId);
        parameters.add(knowIdParam);

        // 查询内容参数
        JSONObject queryTextParam = new JSONObject();
        queryTextParam.put(FlowPluginContent.NAME, "queryText");
        queryTextParam.put(FlowPluginContent.DESCRIPTION, "查询内容");
        queryTextParam.put(FlowPluginContent.TYPE, FlowPluginContent.TYPE_STRING);
        queryTextParam.put(FlowPluginContent.LOCATION, FlowPluginContent.LOCATION_BODY);
        queryTextParam.put(FlowPluginContent.REQUIRED, true);
        parameters.add(queryTextParam);
        
        tool.put(FlowPluginContent.PARAMETERS, parameters);
        // 响应
        JSONArray responses = new JSONArray();
        tool.put(FlowPluginContent.RESPONSES, responses);

        return tool;
    }

}
