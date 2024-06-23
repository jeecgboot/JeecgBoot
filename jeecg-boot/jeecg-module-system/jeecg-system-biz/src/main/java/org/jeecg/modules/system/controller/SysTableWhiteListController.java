package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysTableWhiteList;
import org.jeecg.modules.system.service.ISysTableWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 系统表白名单
 * @Author: jeecg-boot
 * @Date: 2023-09-12
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "系统表白名单")
@RestController
@RequestMapping("/sys/tableWhiteList")
public class SysTableWhiteListController extends JeecgController<SysTableWhiteList, ISysTableWhiteListService> {

    @Autowired
    private ISysTableWhiteListService sysTableWhiteListService;

    /**
     * 分页列表查询
     *
     * @param sysTableWhiteList
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(
            SysTableWhiteList sysTableWhiteList,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req
    ) {
        QueryWrapper<SysTableWhiteList> queryWrapper = QueryGenerator.initQueryWrapper(sysTableWhiteList, req.getParameterMap());
        Page<SysTableWhiteList> page = new Page<>(pageNo, pageSize);
        IPage<SysTableWhiteList> pageList = sysTableWhiteListService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param sysTableWhiteList
     * @return
     */
    @AutoLog(value = "系统表白名单-添加")
    @ApiOperation(value = "系统表白名单-添加", notes = "系统表白名单-添加")
    @RequiresRoles("admin")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysTableWhiteList sysTableWhiteList) {
        if (sysTableWhiteListService.add(sysTableWhiteList)) {
            return Result.OK("添加成功！");
        } else {
            return Result.error("添加失败！");
        }
    }

    /**
     * 编辑
     *
     * @param sysTableWhiteList
     * @return
     */
    @AutoLog(value = "系统表白名单-编辑")
    @ApiOperation(value = "系统表白名单-编辑", notes = "系统表白名单-编辑")
    @RequiresRoles("admin")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody SysTableWhiteList sysTableWhiteList) {
        if (sysTableWhiteListService.edit(sysTableWhiteList)) {
            return Result.OK("编辑成功！");
        } else {
            return Result.error("编辑失败！");
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统表白名单-通过id删除")
    @ApiOperation(value = "系统表白名单-通过id删除", notes = "系统表白名单-通过id删除")
    @RequiresRoles("admin")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        if (sysTableWhiteListService.deleteByIds(id)) {
            return Result.OK("删除成功！");
        } else {
            return Result.error("删除失败！");
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "系统表白名单-批量删除")
    @ApiOperation(value = "系统表白名单-批量删除", notes = "系统表白名单-批量删除")
    @RequiresRoles("admin")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        if (sysTableWhiteListService.deleteByIds(ids)) {
            return Result.OK("批量删除成功！");
        } else {
            return Result.error("批量删除失败！");
        }
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统表白名单-通过id查询")
    @ApiOperation(value = "系统表白名单-通过id查询", notes = "系统表白名单-通过id查询")
    @RequiresRoles("admin")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysTableWhiteList sysTableWhiteList = sysTableWhiteListService.getById(id);
        return Result.OK(sysTableWhiteList);
    }

}
