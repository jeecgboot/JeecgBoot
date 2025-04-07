package org.jeecg.modules.airag.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.config.shiro.IgnoreAuth;
import org.jeecg.modules.airag.app.consts.AiAppConsts;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.app.service.IAiragAppService;
import org.jeecg.modules.airag.app.service.IAiragChatService;
import org.jeecg.modules.airag.app.vo.AppDebugParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Description: AI应用
 * @Author: jeecg-boot
 * @Date: 2025-02-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/airag/app")
@Slf4j
public class AiragAppController extends JeecgController<AiragApp, IAiragAppService> {
    @Autowired
    private IAiragAppService airagAppService;

    @Autowired
    private IAiragChatService airagChatService;

    /**
     * 分页列表查询
     *
     * @param airagApp
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<IPage<AiragApp>> queryPageList(AiragApp airagApp,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        QueryWrapper<AiragApp> queryWrapper = QueryGenerator.initQueryWrapper(airagApp, req.getParameterMap());
        Page<AiragApp> page = new Page<AiragApp>(pageNo, pageSize);
        IPage<AiragApp> pageList = airagAppService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 新增或编辑
     *
     * @param airagApp
     * @return
     */
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody AiragApp airagApp) {
        AssertUtils.assertNotEmpty("参数异常", airagApp);
        AssertUtils.assertNotEmpty("请输入应用名称", airagApp.getName());
        AssertUtils.assertNotEmpty("请选择应用类型", airagApp.getType());
        airagApp.setStatus(AiAppConsts.STATUS_ENABLE);
        airagAppService.saveOrUpdate(airagApp);
        return Result.OK("保存完成!", airagApp.getId());
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        airagAppService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.airagAppService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @IgnoreAuth
    @GetMapping(value = "/queryById")
    public Result<AiragApp> queryById(@RequestParam(name = "id", required = true) String id) {
        AiragApp airagApp = airagAppService.getById(id);
        if (airagApp == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(airagApp);
    }


    /**
     * 调试应用
     *
     * @param appDebugParams
     * @return
     * @author chenrui
     * @date 2025/2/28 10:49
     */
    @PostMapping(value = "/debug")
    public SseEmitter debugApp(@RequestBody AppDebugParams appDebugParams) {
        return airagChatService.debugApp(appDebugParams);
    }

    /**
     * 根据需求生成提示词
     *
     * @param prompt
     * @return
     * @author chenrui
     * @date 2025/3/12 15:30
     */
    @GetMapping(value = "/prompt/generate")
    public Result<?> generatePrompt(@RequestParam(name = "prompt", required = true) String prompt) {
        return (Result<?>) airagAppService.generatePrompt(prompt,true);
    }

    /**
     * 根据需求生成提示词
     *
     * @param prompt
     * @return
     * @author chenrui
     * @date 2025/3/12 15:30
     */
    @PostMapping(value = "/prompt/generate")
    public SseEmitter generatePromptSse(@RequestParam(name = "prompt", required = true) String prompt) {
        return (SseEmitter) airagAppService.generatePrompt(prompt,false);
    }

}
