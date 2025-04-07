package org.jeecg.modules.airag.llm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.airag.llm.entity.AiragModel;
import org.jeecg.modules.airag.llm.service.IAiragModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

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
    public Result<String> add(@RequestBody AiragModel airagModel) {
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
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        airagModelService.removeById(id);
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
        this.airagModelService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
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

}
