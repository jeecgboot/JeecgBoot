package org.jeecg.modules.airag.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.service.tool.ToolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.app.consts.AiAppConsts;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.app.service.IAiragVariableService;
import org.jeecg.modules.airag.app.vo.AppVariableVo;
import org.jeecg.modules.airag.common.handler.AIChatParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: AI应用变量服务实现
 * @Author: jeecg-boot
 * @Date: 2025-02-26
 * @Version: V1.0
 */
@Service
@Slf4j
public class AiragVariableServiceImpl implements IAiragVariableService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String CACHE_PREFIX = "airag:app:var:";

    /**
     * 初始化变量（仅不存在时设置）
     *
     * @param username
     * @param appId
     * @param name
     * @param defaultValue
     */
    @Override
    public void initVariable(String username, String appId, String name, String defaultValue) {
        if (oConvertUtils.isEmpty(username) || oConvertUtils.isEmpty(appId) || oConvertUtils.isEmpty(name)) {
            return;
        }
        String key = CACHE_PREFIX + appId + ":" + username;
        redisTemplate.opsForHash().putIfAbsent(key, name, defaultValue != null ? defaultValue : "");
    }

    /**
     * 追加提示词
     * 
     * @param username
     * @param app
     * @return
     */
    @Override
    public String additionalPrompt(String username, AiragApp app) {
        String memoryPrompt = app.getMemoryPrompt();
        String prompt = app.getPrompt();
        
        if (oConvertUtils.isEmpty(memoryPrompt)) {
            return prompt;
        }
        String variablesStr = app.getVariables();
        if (oConvertUtils.isEmpty(variablesStr)) {
            return prompt;
        }

        List<AppVariableVo> variableList = JSONArray.parseArray(variablesStr, AppVariableVo.class);
        if (variableList == null || variableList.isEmpty()) {
            return prompt;
        }

        String key = CACHE_PREFIX + app.getId() + ":" + username;
        Map<Object, Object> savedValues = redisTemplate.opsForHash().entries(key);

        for (AppVariableVo variable : variableList) {
            if (variable.getEnable() != null && !variable.getEnable()) {
                continue;
            }
            String name = variable.getName();
            String value = variable.getDefaultValue();

            // 优先使用Redis中的值
            if (savedValues.containsKey(name)) {
                Object savedVal = savedValues.get(name);
                if (savedVal != null) {
                    value = String.valueOf(savedVal);
                }
            }

            if (value == null) {
                value = "";
            }

            // 替换 {{name}}
            memoryPrompt = memoryPrompt.replace("{{" + name + "}}", value);
        }
        return prompt + "\n" + memoryPrompt;
    }

    /**
     * 更新变量值
     * 
     * @param userId
     * @param appId
     * @param name
     * @param value
     */
    @Override
    public void updateVariable(String userId, String appId, String name, String value) {
        if (oConvertUtils.isEmpty(userId) || oConvertUtils.isEmpty(appId) || oConvertUtils.isEmpty(name)) {
            return;
        }
        String key = CACHE_PREFIX + appId + ":" + userId;
        redisTemplate.opsForHash().put(key, name, value);
    }


    /**
     * 添加变量更新工具
     *
     * @param params
     * @param aiApp
     * @param username
     */
    @Override
    public void addUpdateVariableTool(AiragApp aiApp, String username, AIChatParams params) {
        if (params.getTools() == null) {
            params.setTools(new HashMap<>());
        }
        if (!AiAppConsts.IZ_OPEN_MEMORY.equals(aiApp.getIzOpenMemory())) {
            return;
        }
        // 构建变量描述信息
        String variablesStr = aiApp.getVariables();
        List<AppVariableVo> variableList = null;
        if (oConvertUtils.isNotEmpty(variablesStr)) {
            variableList = JSONArray.parseArray(variablesStr, AppVariableVo.class);
        }

        //工具描述
        StringBuilder descriptionBuilder = new StringBuilder("更新应用变量的值。仅当检测到变量的新值与当前值不一致时调用。如果已调用过或值未变，请勿重复调用。");
        if (variableList != null && !variableList.isEmpty()) {
            descriptionBuilder.append("\n\n可用变量列表：");
            for (AppVariableVo var : variableList) {
                if (var.getEnable() != null && !var.getEnable()) {
                    continue;
                }
                descriptionBuilder.append("\n- ").append(var.getName());
                if (oConvertUtils.isNotEmpty(var.getDescription())) {
                    descriptionBuilder.append(": ").append(var.getDescription());
                }
            }
            descriptionBuilder.append("\n\n注意：variableName必须是上述列表中的名称之一。");
        }

        //构建更新变量的工具
        ToolSpecification spec = ToolSpecification.builder()
                .name("update_variable")
                .description(descriptionBuilder.toString())
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("variableName", "变量名称")
                        .addStringProperty("value", "变量值")
                        .required("variableName", "value")
                        .build())
                .build();

        //监听工具的调用
        ToolExecutor executor = (toolExecutionRequest, memoryId) -> {
            try {
                JSONObject args = JSONObject.parseObject(toolExecutionRequest.arguments());
                String name = args.getString("variableName");
                String value = args.getString("value");
                IAiragVariableService variableService = SpringContextUtils.getBean(IAiragVariableService.class);
                //更新变量值
                variableService.updateVariable(username, aiApp.getId(), name, value);
                return "变量 " + name + " 已更新为: " + value;
            } catch (Exception e) {
                log.error("更新变量失败", e);
                return "更新变量失败: " + e.getMessage();
            }
        };

        params.getTools().put(spec, executor);
    }
}
