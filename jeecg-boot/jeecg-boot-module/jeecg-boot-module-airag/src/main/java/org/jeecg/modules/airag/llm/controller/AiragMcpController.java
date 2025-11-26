package org.jeecg.modules.airag.llm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.airag.llm.entity.AiragMcp;
import org.jeecg.modules.airag.llm.service.IAiragMcpService;
import org.jeecg.modules.airag.llm.dto.SaveToolsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: MCP
 * @Author: jeecg-boot
 * @Date: 2025-10-20
 * @Version: V1.0
 */
@Tag(name = "MCP")
@RestController("airagMcpController")
@RequestMapping("/airag/airagMcp")
@Slf4j
public class AiragMcpController extends JeecgController<AiragMcp, IAiragMcpService> {
    @Autowired
    private IAiragMcpService airagMcpService;

    /**
     * 分页列表查询
     *
     * @param airagMcp
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @Operation(summary = "MCP-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<AiragMcp>> queryPageList(AiragMcp airagMcp,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {

        QueryWrapper<AiragMcp> queryWrapper = QueryGenerator.initQueryWrapper(airagMcp, req.getParameterMap());
        Page<AiragMcp> page = new Page<AiragMcp>(pageNo, pageSize);
        IPage<AiragMcp> pageList = airagMcpService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 保存
     *
     * @param airagMcp
     * @return
     */
    @Operation(summary = "MCP-保存")
    @PostMapping(value = "/save")
    public Result<String> save(@RequestBody AiragMcp airagMcp) {
        return airagMcpService.edit(airagMcp);
    }



    /**
     * 保存并同步
     *
     * @param airagMcp
     * @return
     * @author chenrui
     * @date 2025/10/21 10:54
     */
    @Operation(summary = "MCP-保存并同步")
    @PostMapping(value = "/saveAndSync")
    public Result<?> saveAndSync(@RequestBody AiragMcp airagMcp) {
        Result<String> saveResult = airagMcpService.edit(airagMcp);
        if (!saveResult.isSuccess()) {
            return saveResult;
        }
        String id = airagMcp.getId();
        if (id == null || id.trim().isEmpty()) {
            return Result.error("保存失败");
        }
        return airagMcpService.sync(id);
    }

    /**
     * 同步MCP信息
     *
     * @param id
     * @return
     * @author chenrui
     * @date 2025/10/20 20:09
     */
    @Operation(summary = "MCP-同步MCP信息")
    @PostMapping(value = "/sync/{id}")
    public Result<?> sync(@PathVariable(name = "id", required = true) String id) {
        return airagMcpService.sync(id);
    }


    /**
     * 启用/禁用MCP信息
     *
     * @param action 启用：enable，禁用：disable
     * @return
     * @author chenrui
     * @date 2025/10/20 20:13
     */
    @Operation(summary = "MCP-启用/禁用MCP信息")
    @PostMapping(value = "/status/{id}/{action}")
    public Result<?> toggleStatus(@PathVariable(name = "id",required = true) String id,
                                  @PathVariable(name = "action", required = true) String action) {
        return airagMcpService.toggleStatus(id,action);
    }

    /**
     * 保存插件工具
     * for [QQYUN-12453]【AI】支持插件
     * @param dto 包含插件ID和工具列表JSON字符串的DTO
     * @return
     * @author chenrui
     * @date 2025/10/30
     */
    @Operation(summary = "MCP-保存插件工具")
    @PostMapping(value = "/saveTools")
    public Result<String> saveTools(@RequestBody SaveToolsDTO dto) {
        return airagMcpService.saveTools(dto.getId(), dto.getTools());
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @Operation(summary = "MCP-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        airagMcpService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @Operation(summary = "MCP-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<AiragMcp> queryById(@RequestParam(name = "id", required = true) String id) {
        AiragMcp airagMcp = airagMcpService.getById(id);
        if (airagMcp == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(airagMcp);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param airagMcp
     */
//    @RequiresPermissions("llm:airag_mcp:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AiragMcp airagMcp) {
        return super.exportXls(request, airagMcp, AiragMcp.class, "MCP");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
//    @RequiresPermissions("llm:airag_mcp:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AiragMcp.class);
    }

}
