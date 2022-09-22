package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysFiles;
import org.jeecg.modules.system.service.ISysFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 知识库-文档管理
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "知识库-文档管理")
@RestController
@RequestMapping("/sys/files")
public class SysFilesController extends JeecgController<SysFiles, ISysFilesService> {
    @Autowired
    private ISysFilesService sysFilesService;

    /**
     * 分页列表查询
     *
     * @param sysFiles
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "知识库-文档管理-分页列表查询")
    @ApiOperation(value = "知识库-文档管理-分页列表查询", notes = "知识库-文档管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysFiles sysFiles,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SysFiles> queryWrapper = QueryGenerator.initQueryWrapper(sysFiles, req.getParameterMap());
        Page<SysFiles> page = new Page<SysFiles>(pageNo, pageSize);
        IPage<SysFiles> pageList = sysFilesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param sysFiles
     * @return
     */
    @AutoLog(value = "知识库-文档管理-添加")
    @ApiOperation(value = "知识库-文档管理-添加", notes = "知识库-文档管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysFiles sysFiles) {
        sysFilesService.save(sysFiles);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysFiles
     * @return
     */
    @AutoLog(value = "知识库-文档管理-编辑")
    @ApiOperation(value = "知识库-文档管理-编辑", notes = "知识库-文档管理-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody SysFiles sysFiles) {
        sysFilesService.updateById(sysFiles);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "知识库-文档管理-通过id删除")
    @ApiOperation(value = "知识库-文档管理-通过id删除", notes = "知识库-文档管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysFilesService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "知识库-文档管理-批量删除")
    @ApiOperation(value = "知识库-文档管理-批量删除", notes = "知识库-文档管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysFilesService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "知识库-文档管理-通过id查询")
    @ApiOperation(value = "知识库-文档管理-通过id查询", notes = "知识库-文档管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysFiles sysFiles = sysFilesService.getById(id);
        return Result.OK(sysFiles);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysFiles
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysFiles sysFiles) {
        return super.exportXls(request, sysFiles, SysFiles.class, "知识库-文档管理");
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
        return super.importExcel(request, response, SysFiles.class);
    }

}
