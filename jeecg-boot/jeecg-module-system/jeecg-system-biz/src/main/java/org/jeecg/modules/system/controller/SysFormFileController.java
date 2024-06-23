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
import org.jeecg.modules.system.entity.SysFormFile;
import org.jeecg.modules.system.service.ISysFormFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 表单评论文件
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "表单评论文件")
@RestController
@RequestMapping("/sys/formFile")
public class SysFormFileController extends JeecgController<SysFormFile, ISysFormFileService> {
    @Autowired
    private ISysFormFileService sysFormFileService;

    /**
     * 分页列表查询
     *
     * @param sysFormFile
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "表单评论文件-分页列表查询")
    @ApiOperation(value = "表单评论文件-分页列表查询", notes = "表单评论文件-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysFormFile sysFormFile,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SysFormFile> queryWrapper = QueryGenerator.initQueryWrapper(sysFormFile, req.getParameterMap());
        Page<SysFormFile> page = new Page<SysFormFile>(pageNo, pageSize);
        IPage<SysFormFile> pageList = sysFormFileService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param sysFormFile
     * @return
     */
    @AutoLog(value = "表单评论文件-添加")
    @ApiOperation(value = "表单评论文件-添加", notes = "表单评论文件-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysFormFile sysFormFile) {
        sysFormFileService.save(sysFormFile);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysFormFile
     * @return
     */
    @AutoLog(value = "表单评论文件-编辑")
    @ApiOperation(value = "表单评论文件-编辑", notes = "表单评论文件-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody SysFormFile sysFormFile) {
        sysFormFileService.updateById(sysFormFile);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "表单评论文件-通过id删除")
    @ApiOperation(value = "表单评论文件-通过id删除", notes = "表单评论文件-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysFormFileService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "表单评论文件-批量删除")
    @ApiOperation(value = "表单评论文件-批量删除", notes = "表单评论文件-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysFormFileService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "表单评论文件-通过id查询")
    @ApiOperation(value = "表单评论文件-通过id查询", notes = "表单评论文件-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysFormFile sysFormFile = sysFormFileService.getById(id);
        return Result.OK(sysFormFile);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysFormFile
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysFormFile sysFormFile) {
        return super.exportXls(request, sysFormFile, SysFormFile.class, "表单评论文件");
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
        return super.importExcel(request, response, SysFormFile.class);
    }

}
