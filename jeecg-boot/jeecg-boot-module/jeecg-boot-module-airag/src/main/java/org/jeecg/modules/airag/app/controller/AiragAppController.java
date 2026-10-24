package org.jeecg.modules.airag.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.airag.app.consts.AiAppConsts;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.app.service.IAiragAppService;
import org.jeecg.modules.airag.app.service.IAiragChatService;
import org.jeecg.modules.airag.app.vo.AiArticleWriteVersionVo;
import org.jeecg.modules.airag.app.vo.AppDebugParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import org.jeecg.common.system.vo.DictModel;

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
    @RequiresPermissions("airag:app:list")
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
     * 字典列表查询（不分页，按创建时间倒序）
     *
     * @param airagApp 支持通过实体字段动态过滤，如 type 等
     * @param req HTTP请求
     * @return 应用字典列表
     */
    @GetMapping(value = "/listDict")
    @RequiresPermissions(value = {"airag:app:listDict", "airag:flow:debug"}, logical = Logical.OR)
    public Result<List<DictModel>> listDict(AiragApp airagApp, HttpServletRequest req) {
        QueryWrapper<AiragApp> queryWrapper = QueryGenerator.initQueryWrapper(airagApp, req.getParameterMap());
        queryWrapper.select("id", "name");
        queryWrapper.orderByDesc("create_time");
        List<AiragApp> list = airagAppService.list(queryWrapper);
        List<DictModel> dictList = list.stream()
            .map(app -> new DictModel(app.getId(), app.getName()))
            .collect(Collectors.toList());
        return Result.OK(dictList);
    }

    /**
     * 新增或编辑
     *
     * @param airagApp
     * @return
     */
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    @RequiresPermissions("airag:app:edit")
    public Result<String> edit(@RequestBody AiragApp airagApp, HttpServletRequest request) {
        AssertUtils.assertNotEmpty("参数异常", airagApp);
        AssertUtils.assertNotEmpty("请输入应用名称", airagApp.getName());
        AssertUtils.assertNotEmpty("请选择应用类型", airagApp.getType());
        //update-begin---author:zhangdaihao ---date:20260415  for：[issues/9462]AI应用edit接口跨租户数据写入漏洞------------
        //SaaS多租户隔离：禁止跨租户写入，防止通过请求体伪造tenantId污染其他租户数据
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            String currentTenantId = TokenUtils.getTenantIdByRequest(request);
            if (airagApp.getId() != null && !airagApp.getId().isEmpty()) {
                AiragApp dbApp = airagAppService.getById(airagApp.getId());
                if (dbApp == null || !dbApp.getTenantId().equals(currentTenantId)) {
                    return Result.error("保存AI应用失败，不能修改其他租户的AI应用！");
                }
            }
            //强制使用当前登录租户，忽略客户端传入值
            airagApp.setTenantId(currentTenantId);
        }
        //update-end---author:zhangdaihao ---date:20260415  for：[issues/9462]AI应用edit接口跨租户数据写入漏洞------------
        airagApp.setStatus(AiAppConsts.STATUS_ENABLE);
        airagAppService.saveOrUpdate(airagApp);
        return Result.OK("保存完成!", airagApp.getId());
    }

	/**
	 * 复制应用
	 *
	 * @param id 原应用ID
	 * @param request HTTP请求
	 * @return 新应用ID
	 * @author scott
	 * @since 2026-08-06 【LHZP-1512】AI应用增加复制功能
	 */
	@PostMapping(value = "/copy")
	@RequiresPermissions("airag:app:edit")
	public Result<String> copy(@RequestParam(name = "id") String id, HttpServletRequest request) {
		AssertUtils.assertNotEmpty("id必须填写", id);
		String copiedAppId = airagAppService.copyApp(id, TokenUtils.getTenantIdByRequest(request));
		return Result.OK("复制成功", copiedAppId);
	}

    /**
     * 发布应用
     *
     * @return
     */
    @RequestMapping(value = "/release", method = RequestMethod.POST)
    @RequiresPermissions("airag:app:release")
    public Result<String> release(@RequestParam(name = "id") String id,
                                  @RequestParam(name = "release") Boolean release,
                                  HttpServletRequest request) {
        AssertUtils.assertNotEmpty("id必须填写", id);
        if (release == null) {
            release = true;
        }
        AiragApp app = airagAppService.getById(id);
        if (app == null) {
            return Result.error("应用不存在");
        }
        // SaaS 多租户隔离：禁止跨租户发布/取消发布
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            String currentTenantId = TokenUtils.getTenantIdByRequest(request);
            if (oConvertUtils.isEmpty(app.getTenantId()) || !app.getTenantId().equals(currentTenantId)) {
                return Result.error("操作失败，不能操作其他租户的AI应用！");
            }
        }
        String shareToken = airagAppService.releaseApp(id, release);
        // result 固定返回 shareToken（取消发布为 null），避免与提示文案混用
        Result<String> result = Result.OK(shareToken);
        result.setMessage(release ? "发布成功" : "取消发布成功");
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    @RequiresPermissions("airag:app:delete")
    public Result<String> delete(HttpServletRequest request,@RequestParam(name = "id", required = true) String id) {
        //update-begin---author:chenrui ---date:20250606  for：[issues/8337]关于ai工作列表的数据权限问题 #8337------------
        //如果是saas隔离的情况下，判断当前租户id是否是当前租户下的
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            AiragApp app = airagAppService.getById(id);
            //获取当前租户
            String currentTenantId = TokenUtils.getTenantIdByRequest(request);
            if (null == app || !app.getTenantId().equals(currentTenantId)) {
                return Result.error("删除AI应用失败，不能删除其他租户的AI应用！");
            }
        }
        //update-end---author:chenrui ---date:20250606  for：[issues/8337]关于ai工作列表的数据权限问题 #8337------------
        airagAppService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    @RequiresPermissions("airag:app:queryById")
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

    /**
     * 根据应用ID生成变量和记忆提示词 (SSE)
     * for: 【QQYUN-14479】提示词单独拆分
     * @param variables
     * @return
     */
    @PostMapping(value = "/prompt/generateMemoryByAppId")
    public SseEmitter generatePromptByAppIdSse(@RequestParam(name = "variables") String variables,
                                               @RequestParam(name = "memoryId") String memoryId) {
        return (SseEmitter) airagAppService.generateMemoryByAppId(variables, memoryId,false);
    }

    /**
     * 写作保存
     */
    @PostMapping("/save/article/write")
    public Result<String> saveArticleWrite(@RequestBody AiArticleWriteVersionVo aiWriteVersionVo) {
        airagAppService.saveArticleWrite(aiWriteVersionVo);
        return Result.OK("保存成功！");
    }
    
    /**
     * 写作删除
     */
    @DeleteMapping("/delete/article/write")
    public Result<String> deleteArticleWrite(@RequestParam(name = "version") String version) {
        AssertUtils.assertNotEmpty("版本号不能为空", version);
        airagAppService.deleteArticleWrite(version);
        return Result.OK("删除成功！");
    }
    
    /**
     * 写作查询
     */
    @GetMapping("/list/article/write")
    public Result<List<AiArticleWriteVersionVo>> listArticleWrite() {
        List<AiArticleWriteVersionVo> list = airagAppService.listArticleWrite();
        return Result.OK(list);
    }
}
