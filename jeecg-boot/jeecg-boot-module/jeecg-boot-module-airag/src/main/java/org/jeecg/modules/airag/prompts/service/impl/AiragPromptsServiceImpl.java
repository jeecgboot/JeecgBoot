package org.jeecg.modules.airag.prompts.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.airag.common.handler.AIChatParams;
import org.jeecg.modules.airag.common.handler.IAIChatHandler;
import org.jeecg.modules.airag.prompts.consts.AiPromptsConsts;
import org.jeecg.modules.airag.prompts.entity.AiragExtData;
import org.jeecg.modules.airag.prompts.entity.AiragPrompts;
import org.jeecg.modules.airag.prompts.mapper.AiragPromptsMapper;
import org.jeecg.modules.airag.prompts.service.IAiragExtDataService;
import org.jeecg.modules.airag.prompts.service.IAiragPromptsService;
import org.jeecg.modules.airag.prompts.vo.AiragExperimentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Description: airag_prompts
 * @Author: jeecg-boot
 * @Date:   2025-12-12
 * @Version: V1.0
 */
@Slf4j
@Service("airagPromptsServiceImpl")
public class AiragPromptsServiceImpl extends ServiceImpl<AiragPromptsMapper, AiragPrompts> implements IAiragPromptsService {
    @Autowired
    IAIChatHandler aiChatHandler;

    @Autowired
    IAiragExtDataService airagExtDataService;

    @Autowired
    private JeecgBaseConfig jeecgBaseConfig;
    // 创建静态线程池，确保整个应用生命周期中只有一个实例
    private static final ExecutorService executor = new ThreadPoolExecutor(
            4, // 核心线程数
            8, // 最大线程数
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100), // 防止内存溢出
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
    );

    /**
     * 提示词实验
     * @param experimentVo
     * @return
     */
    @Override
    public Result<?> promptExperiment(AiragExperimentVo experimentVo, HttpServletRequest request) {
        log.info("开始执行提示词实验，参数：{}", JSON.toJSONString(experimentVo));

        // 参数验证
        String promptKey = experimentVo.getPromptKey();
        AssertUtils.assertNotEmpty("请选择提示词", promptKey);
        String dataId = experimentVo.getExtDataId();
        AssertUtils.assertNotEmpty("请选择数据集", dataId);

        Map<String, String> fieldMappings = experimentVo.getMappings();
        AssertUtils.assertNotEmpty("请配置字段映射", fieldMappings);

        try {
            //1.查询提示词
            AiragPrompts airagPrompts = this.baseMapper.selectOne(new LambdaQueryWrapper<AiragPrompts>().eq(AiragPrompts::getPromptKey, promptKey));
            AssertUtils.assertNotEmpty("未找到指定的提示词", airagPrompts);
            String modelParam = airagPrompts.getModelParam();
             // 过滤提示词变量
            JSONArray promptVariables;
            if(oConvertUtils.isNotEmpty(modelParam)){
                JSONObject airagPromptsParams = JSON.parseObject(modelParam);
                if(airagPromptsParams.containsKey("promptVariables")){
                    promptVariables = airagPromptsParams.getJSONArray("promptVariables");
                } else {
                    promptVariables = null;
                }
            } else {
                promptVariables = null;
            }
            //2.查询数据集
            AiragExtData airagExtData = airagExtDataService.getById(dataId);
            AssertUtils.assertNotEmpty("未找到指定的数据集", airagExtData);
            String datasetValue = airagExtData.getDatasetValue();
            if(oConvertUtils.isEmpty(datasetValue)){
                return Result.error("评测集不能为空！");
            }

            //3.异步调用 根据映射字段，调用评估器测评
            JSONObject datasetObj = JSONObject.parseObject(datasetValue);
            //评测列配置
            JSONArray columns = datasetObj.getJSONArray("columns");
            //评测题库
            JSONArray datasetArray = datasetObj.getJSONArray("dataSource");
            AssertUtils.assertNotEmpty("数据集中没有找到数据源", datasetArray);
            AssertUtils.assertTrue("数据源为空", datasetArray.size() > 0);

            //测评结果集 - 使用线程安全的CopyOnWriteArrayList
            List<JSONObject> scoreResult = new CopyOnWriteArrayList<>();

            // 批量提交任务
            List<CompletableFuture<Void>> futures = IntStream.range(0, datasetArray.size())
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        log.info("开始处理第{}条数据", i + 1);
                        //定义返回结果
                        JSONObject result = new JSONObject();
                        //评测数据
                        JSONObject dataset = datasetArray.getJSONObject(i);
                        result.putAll(dataset);
                        //用户问题
                        String userQuery = dataset.getString(fieldMappings.get("user_query"));
                        result.put("userQuery", userQuery);
                        //变量处理
                       if(!CollectionUtils.isEmpty(promptVariables)){
                           String content = airagPrompts.getContent();
                           for (Object var : promptVariables){
                              JSONObject variable = JSONObject.parseObject(var.toString());
                              String name = dataset.getString(fieldMappings.get(variable.getString("name")));
                              //提示词默认变量值
                              String defaultValue = variable.getString("value");
                              // 获取目标类型
                              String dataType = findDataType(columns, variable);
                               if("FILE".equals(dataType)){
                                  defaultValue = getFileAccessHttpUrl(request, defaultValue);
                                  name = getFileAccessHttpUrl(request, name);
                              }
                              if(oConvertUtils.isNotEmpty(name)){
                                  //提示词 评估集变量值替换
                                  content = content.replaceAll(variable.getString("name"), name);
                              }else if(oConvertUtils.isNotEmpty(defaultValue)){
                                  content = content.replaceAll(variable.getString("name"), defaultValue);
                              }
                          }
                           airagPrompts.setContent(content);
                       }

                        //提示词答案
                        String promptAnswer = getPromptAnswer(airagPrompts, dataset, fieldMappings);
                        result.put("promptAnswer", promptAnswer);

                        //评估器答案
                        String answerScore = getAnswerScore(promptAnswer, dataset, fieldMappings, airagExtData);
                        result.put("answerScore", answerScore);

                        scoreResult.add(result);
                        log.info("第{}条数据处理完成", i + 1);
                    } catch (Exception e) {
                        log.error("处理第{}条数据时发生异常", i + 1, e);
                        // 重新抛出异常，让CompletableFuture捕获
                        throw new CompletionException(e);
                    }
                }, executor))
                .collect(Collectors.toList());

            // 非阻塞方式处理完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("批量处理失败", ex);
                        // 更新状态为失败
                        airagExtData.setStatus(AiPromptsConsts.STATUS_FAILED);
                    } else {
                        log.info("所有数据处理完成，共处理{}条数据", scoreResult.size());
                        // 查询已存在的评测记录
                        List<AiragExtData> existingTracks = airagExtDataService.queryTrackById(dataId);
                        Integer version = 1;
                        if(!CollectionUtils.isEmpty(existingTracks)) {
                            version = existingTracks.stream()
                                    .map(AiragExtData::getVersion)
                                    .max(Integer::compareTo)
                                    .orElse(0) + 1;
                        }
                        for (JSONObject item : scoreResult) {
                            // 保存结果
                            AiragExtData track = new AiragExtData();
                            //关联评估器ID
                            track.setMetadata(dataId);
                            //定义类型
                            track.setBizType(AiPromptsConsts.BIZ_TYPE_TRACK);
                            //定义版本
                            track.setVersion(version);
                            //定义状态
                            track.setStatus(AiPromptsConsts.STATUS_COMPLETED);
                            //定义评测结果
                            track.setDataValue(item.toJSONString());
                            airagExtDataService.save(track);
                        }
                        // 更新状态为完成
                        airagExtData.setStatus(AiPromptsConsts.STATUS_COMPLETED);
                    }
                    airagExtDataService.updateById(airagExtData);
                });

            //4.修改状态进行中
            airagExtData.setStatus(AiPromptsConsts.STATUS_RUNNING);
            airagExtDataService.updateById(airagExtData);

            log.info("提示词实验已提交，共{}条数据待处理", datasetArray.size());
            return Result.OK("实验已开始，正在处理数据");
        } catch (Exception e) {
            log.error("提示词实验执行失败", e);
            return Result.error("实验执行失败：" + e.getMessage());
        }
    }


    /**
     * 提示词回答的结果
     * @param airagPrompts
     * @param questions
     * @param fieldMappings
     * @return
     */
    public String getPromptAnswer(AiragPrompts airagPrompts, JSONObject questions, Map<String, String> fieldMappings) {
        try {
            //0.判断是否配置了判断fieldMappings的value值 是否包含actual_output
            if (!fieldMappings.containsValue("actual_output")) {
                log.warn("字段映射中没有配置actual_output");
                return null;
            }

            //1.提示词
            String prompt = airagPrompts.getContent();
            AssertUtils.assertNotEmpty("请输入提示词", prompt);

            String userQuery = questions.getString(fieldMappings.get("user_query"));
            AssertUtils.assertNotEmpty("请输入测试内容", userQuery);

            //2.ai问题组装
            List<ChatMessage> messages = Arrays.asList(new SystemMessage(prompt), new UserMessage(userQuery));

            //3.模型数据
            String modelId = airagPrompts.getModelId();
            AssertUtils.assertNotEmpty("请选择模型", modelId);

            //4.模型参数
            String modelParam = airagPrompts.getModelParam();
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
                    params.setTopP(param.getDoubleValue("topP")); // 修复：设置到正确的字段
                }
                if(param.containsKey("presencePenalty")){
                    params.setPresencePenalty(param.getDoubleValue("presencePenalty")); // 修复：设置到正确的字段
                }
                if(param.containsKey("frequencyPenalty")){
                    params.setFrequencyPenalty(param.getDoubleValue("frequencyPenalty")); // 修复：设置到正确的字段
                }
            }

            log.debug("调用AI模型，模型ID：{}，参数：{}", modelId, JSON.toJSONString(params));
            //5.AI问答
            String promptAnswer = aiChatHandler.completions(modelId, messages, params);
            log.debug("AI模型返回结果：{}", promptAnswer);

            return promptAnswer;
        } catch (Exception e) {
            log.error("获取提示词回答失败", e);
            return null;
        }
    }

    /**
     * 评测答案分数
     * @return
     */
    public String getAnswerScore(String promptAnswer, JSONObject questions, Map<String, String> fieldMappings, AiragExtData airagExtData) {
        try {
            //1.提示词
            String prompt = airagExtData.getDataValue();
            AssertUtils.assertNotEmpty("请输入提示词", prompt);
            prompt += "定义返回格式： 得分：最终的得分，必须输出一个数字，表示满足Prompt中评分标准的程度。得分范围从 0.0 到 1.0，1.0 表示完全满足评分标准，0.0 表示完全不满足评分标准。\n" +
                    "原因：{对得分的可读性的解释，说明打分原因}。最后，必须用一句话结束理由，该句话为：因此，应该给出的分数是<你评测的的得分>。请勿返回提问的问题、添加分析过程、解释说明等内容，只返回要求的格式内容";

            String userQuery = "输入的内容：";
            //2.拼接测试内容
            for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
                // 评估器中的key
                String key = entry.getKey();
                // 评估器中的映射的key
                String value = entry.getValue();
                String valueData;
                if("actual_output".equalsIgnoreCase(value)){
                    valueData = promptAnswer;
                }else{
                    valueData = questions.getString(value);
                }
                userQuery += (key + ":" + valueData + " ");
            }
            List<ChatMessage> messages = Arrays.asList(new SystemMessage(prompt), new UserMessage(userQuery));

            //3.模型数据
            String metadata = airagExtData.getMetadata();
            if(oConvertUtils.isNotEmpty(metadata)){
                JSONObject modelParam = JSONObject.parseObject(metadata);
                String modelId = modelParam.getString("modelId");
                AssertUtils.assertNotEmpty("评估器模型ID不能为空", modelId);

                // 默认大模型参数
                AIChatParams params = new AIChatParams();
                params.setTemperature(0.8);
                params.setTopP(0.9);
                params.setPresencePenalty(0.1);
                params.setFrequencyPenalty(0.1);

                if(oConvertUtils.isNotEmpty(modelParam)){
                    if(modelParam.containsKey("temperature")){
                        params.setTemperature(modelParam.getDoubleValue("temperature"));
                    }
                    if(modelParam.containsKey("topP")){
                        params.setTopP(modelParam.getDoubleValue("topP")); // 修复：设置到正确的字段
                    }
                    if(modelParam.containsKey("presencePenalty")){
                        params.setPresencePenalty(modelParam.getDoubleValue("presencePenalty")); // 修复：设置到正确的字段
                    }
                    if(modelParam.containsKey("frequencyPenalty")){
                        params.setFrequencyPenalty(modelParam.getDoubleValue("frequencyPenalty")); // 修复：设置到正确的字段
                    }
                }

                log.debug("调用评估器模型，模型ID：{}，参数：{}", modelId, JSON.toJSONString(params));
                //5.AI问答
                String answerScore = aiChatHandler.completions(modelId, messages, params);
                log.debug("评估器模型返回结果：{}", answerScore);

                return answerScore;
            }
            return null;
        } catch (Exception e) {
            log.error("获取答案评分失败", e);
            return null;
        }
    }

    /**
     *
     * @param columns
     * @param variable
     * @return
     */
    public static String findDataType(JSONArray columns, JSONObject variable) {
        // 获取目标字段值
        String targetName = variable.getString("name");

        // 使用 Stream API 查找并获取 dataType
        return columns.stream()
                .map(obj -> JSONObject.parseObject(obj.toString()))
                .filter(column -> targetName.equals(column.getString("name")))
                .findFirst()
                .map(column -> column.getString("dataType"))
                .orElse(null); // 如果没有找到，返回 null
    }
    /**
     * 获取图片地址
     * @param request
     * @param url
     * @return
     */
    private String getFileAccessHttpUrl(HttpServletRequest request,String url){
        if(oConvertUtils.isNotEmpty(url) && url.startsWith("http")){
            return url;
        }else{
            return CommonUtils.getBaseUrl(request) + "/sys/common/static/" + url;
        }
    }
}
