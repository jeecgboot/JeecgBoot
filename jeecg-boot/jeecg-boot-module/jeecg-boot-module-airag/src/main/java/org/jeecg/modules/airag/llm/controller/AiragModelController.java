package org.jeecg.modules.airag.llm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.embedding.EmbeddingModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.ai.factory.AiModelFactory;
import org.jeecg.ai.factory.AiModelOptions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.airag.llm.consts.LLMConsts;
import org.jeecg.modules.airag.llm.entity.AiragModel;
import org.jeecg.modules.airag.llm.handler.AIChatHandler;
import org.jeecg.modules.airag.llm.handler.EmbeddingHandler;
import org.jeecg.modules.airag.llm.service.IAiragModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

/**
 * @Description: AiRag模型配置
 * @Author: jeecg-boot
 * @Date: 2025-02-14
 * @Version: V1.0
 */
@Tag(name = "AiRag模型配置")
@RestController
@RequestMapping("/airag/airagModel")
@Slf4j
public class AiragModelController extends JeecgController<AiragModel, IAiragModelService> {
    @Autowired
    private IAiragModelService airagModelService;

    @Autowired
    AIChatHandler aiChatHandler;

    /**
     * 分页列表查询
     *
     * @param airagModel
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<IPage<AiragModel>> queryPageList(AiragModel airagModel, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<AiragModel> queryWrapper = QueryGenerator.initQueryWrapper(airagModel, req.getParameterMap());
        Page<AiragModel> page = new Page<AiragModel>(pageNo, pageSize);
        IPage<AiragModel> pageList = airagModelService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param airagModel
     * @return
     */
    @PostMapping(value = "/add")
    @RequiresPermissions("airag:model:add")
    public Result<String> add(@RequestBody AiragModel airagModel) {
        // 验证 模型名称/模型类型/基础模型
        AssertUtils.assertNotEmpty("模型名称不能为空", airagModel.getName());
        AssertUtils.assertNotEmpty("模型类型不能为空", airagModel.getModelType());
        AssertUtils.assertNotEmpty("基础模型不能为空", airagModel.getModelName());
        airagModelService.save(airagModel);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param airagModel
     * @return
     */
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @RequiresPermissions("airag:model:edit")
    public Result<String> edit(@RequestBody AiragModel airagModel) {
        airagModelService.updateById(airagModel);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    @RequiresPermissions("airag:model:delete")
    public Result<String> delete(HttpServletRequest request, @RequestParam(name = "id", required = true) String id) {
        //update-begin---author:chenrui ---date:20250606  for：[issues/8337]关于ai工作列表的数据权限问题 #8337------------
        //如果是saas隔离的情况下，判断当前租户id是否是当前租户下的
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            AiragModel model = airagModelService.getById(id);
            //获取当前租户
            String currentTenantId = TokenUtils.getTenantIdByRequest(request);
            if (null == model || !model.getTenantId().equals(currentTenantId)) {
                return Result.error("删除AI模型失败，不能删除其他租户的AI模型！");
            }
        }
        //update-end---author:chenrui ---date:20250606  for：[issues/8337]关于ai工作列表的数据权限问题 #8337------------
        airagModelService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<AiragModel> queryById(@RequestParam(name = "id", required = true) String id) {
        AiragModel airagModel = airagModelService.getById(id);
        if (airagModel == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(airagModel);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param airagModel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AiragModel airagModel) {
        return super.exportXls(request, airagModel, AiragModel.class, "AiRag模型配置");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AiragModel.class);
    }

    @PostMapping(value = "/test")
    public Result<?> test(@RequestBody AiragModel airagModel) {
        // 验证 模型名称/模型类型/基础模型
        AssertUtils.assertNotEmpty("模型名称不能为空", airagModel.getName());
        AssertUtils.assertNotEmpty("模型类型不能为空", airagModel.getModelType());
        AssertUtils.assertNotEmpty("基础模型不能为空", airagModel.getModelName());
        try {
            if(LLMConsts.MODEL_TYPE_LLM.equals(airagModel.getModelType())){
                aiChatHandler.completions(airagModel, Collections.singletonList(UserMessage.from("test connection")), null);
            }else{
                AiModelOptions aiModelOptions = EmbeddingHandler.buildModelOptions(airagModel);
                EmbeddingModel embeddingModel = AiModelFactory.createEmbeddingModel(aiModelOptions);
                embeddingModel.embed("test text");
            }
        }catch (Exception e){
            log.error("测试模型连接失败", e);
            return Result.error("测试模型连接失败" + e.getMessage());
        }
        return Result.OK("测试模型连接成功");
    }

}
