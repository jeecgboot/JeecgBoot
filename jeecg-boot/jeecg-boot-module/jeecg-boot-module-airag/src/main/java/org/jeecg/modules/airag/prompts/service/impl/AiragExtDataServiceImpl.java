package org.jeecg.modules.airag.prompts.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.common.handler.AIChatParams;
import org.jeecg.modules.airag.common.handler.IAIChatHandler;
import org.jeecg.modules.airag.prompts.entity.AiragExtData;
import org.jeecg.modules.airag.prompts.mapper.AiragExtDataMapper;
import org.jeecg.modules.airag.prompts.service.IAiragExtDataService;
import org.jeecg.modules.airag.prompts.vo.AiragDebugVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: airag_ext_data
 * @Author: jeecg-boot
 * @Date:   2025-12-12
 * @Version: V1.0
 */
@Service("airagExtDataServiceImpl")
public class AiragExtDataServiceImpl extends ServiceImpl<AiragExtDataMapper, AiragExtData> implements IAiragExtDataService {

    @Autowired
    IAIChatHandler aiChatHandler;

    @Override
    public Result debugEvaluator(AiragDebugVo debugVo) {
        //1.提示词
        String prompt = debugVo.getPrompts();
        AssertUtils.assertNotEmpty("请输入提示词", prompt);

        //2.测试内容
        String content = debugVo.getContent();
        AssertUtils.assertNotEmpty("请输入测试内容", content);
        List<ChatMessage> messages = Arrays.asList(new SystemMessage(prompt), new UserMessage(content));

        //3.模型数据
        String modelId = debugVo.getModelId();
        AssertUtils.assertNotEmpty("请选择模型", modelId);

        //4.模型参数
        String modelParam = debugVo.getModelParam();
        // 默认大模型参数
        AIChatParams params = new AIChatParams();
        params.setTemperature(0.8);
        params.setTopP(0.9);
        params.setPresencePenalty(0.1);
        params.setFrequencyPenalty(0.1);

        if(oConvertUtils.isNotEmpty(modelParam)){
            JSONObject param = JSON.parseObject(modelParam);
            if(param.containsKey("temperature")){
                params.setTemperature(param.getDoubleValue("temperature"));
            }
            if(param.containsKey("topP")){
                params.setTemperature(param.getDoubleValue("topP"));
            }
            if(param.containsKey("presencePenalty")){
                params.setTemperature(param.getDoubleValue("presencePenalty"));
            }
            if(param.containsKey("frequencyPenalty")){
                params.setTemperature(param.getDoubleValue("frequencyPenalty"));
            }
        }
        //5.AI问答
        String promptValue = aiChatHandler.completions(modelId,messages, params);
        if (promptValue == null || promptValue.isEmpty()) {
            return Result.error("生成失败");
        }
        return Result.OK("success", promptValue);
    }

    /**
     * 查询AI问答记录
     * @param id
     * @return
     */
    @Override
    public List<AiragExtData> queryTrackById(String id) {
        LambdaQueryWrapper<AiragExtData> lqw = new LambdaQueryWrapper<AiragExtData>()
                .eq(AiragExtData::getMetadata, id)
                .orderByDesc(AiragExtData::getVersion)
                .orderByDesc(AiragExtData::getCreateTime);
        List<AiragExtData> list = this.baseMapper.selectList(lqw);
        return list;
    }
}
