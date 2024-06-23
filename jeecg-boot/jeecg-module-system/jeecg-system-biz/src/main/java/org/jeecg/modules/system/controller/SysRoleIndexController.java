package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysRoleIndex;
import org.jeecg.modules.system.service.ISysRoleIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 角色首页配置
 * @Author: jeecg-boot
 * @Date: 2022-03-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "角色首页配置")
@RestController
@RequestMapping("/sys/sysRoleIndex")
public class SysRoleIndexController extends JeecgController<SysRoleIndex, ISysRoleIndexService> {
    @Autowired
    private ISysRoleIndexService sysRoleIndexService;

    /**
     * 分页列表查询
     *
     * @param sysRoleIndex
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "角色首页配置-分页列表查询")
    @ApiOperation(value = "角色首页配置-分页列表查询", notes = "角色首页配置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysRoleIndex sysRoleIndex,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SysRoleIndex> queryWrapper = QueryGenerator.initQueryWrapper(sysRoleIndex, req.getParameterMap());
        Page<SysRoleIndex> page = new Page<SysRoleIndex>(pageNo, pageSize);
        IPage<SysRoleIndex> pageList = sysRoleIndexService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param sysRoleIndex
     * @return
     */
    @RequiresPermissions("system:roleindex:add")
    @AutoLog(value = "角色首页配置-添加")
    @ApiOperation(value = "角色首页配置-添加", notes = "角色首页配置-添加")
    @PostMapping(value = "/add")
    //@DynamicTable(value = DynamicTableConstant.SYS_ROLE_INDEX)
    public Result<?> add(@RequestBody SysRoleIndex sysRoleIndex,HttpServletRequest request) {
        sysRoleIndexService.save(sysRoleIndex);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysRoleIndex
     * @return
     */
    @RequiresPermissions("system:roleindex:edit")
    @AutoLog(value = "角色首页配置-编辑")
    @ApiOperation(value = "角色首页配置-编辑", notes = "角色首页配置-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    //@DynamicTable(value = DynamicTableConstant.SYS_ROLE_INDEX)
    public Result<?> edit(@RequestBody SysRoleIndex sysRoleIndex,HttpServletRequest request) {
        sysRoleIndexService.updateById(sysRoleIndex);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "角色首页配置-通过id删除")
    @ApiOperation(value = "角色首页配置-通过id删除", notes = "角色首页配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysRoleIndexService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "角色首页配置-批量删除")
    @ApiOperation(value = "角色首页配置-批量删除", notes = "角色首页配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysRoleIndexService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "角色首页配置-通过id查询")
    @ApiOperation(value = "角色首页配置-通过id查询", notes = "角色首页配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysRoleIndex sysRoleIndex = sysRoleIndexService.getById(id);
        return Result.OK(sysRoleIndex);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysRoleIndex
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysRoleIndex sysRoleIndex) {
        return super.exportXls(request, sysRoleIndex, SysRoleIndex.class, "角色首页配置");
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
        return super.importExcel(request, response, SysRoleIndex.class);
    }

    /**
     * 通过code查询
     *
     * @param roleCode
     * @return
     */
    @AutoLog(value = "角色首页配置-通过code查询")
    @ApiOperation(value = "角色首页配置-通过code查询", notes = "角色首页配置-通过code查询")
    @GetMapping(value = "/queryByCode")
    //@DynamicTable(value = DynamicTableConstant.SYS_ROLE_INDEX)
    public Result<?> queryByCode(@RequestParam(name = "roleCode", required = true) String roleCode,HttpServletRequest request) {
        SysRoleIndex sysRoleIndex = sysRoleIndexService.getOne(new LambdaQueryWrapper<SysRoleIndex>().eq(SysRoleIndex::getRoleCode, roleCode));
        return Result.OK(sysRoleIndex);
    }

    /**
     * 查询默认首页配置
     */
    @GetMapping("/queryDefIndex")
    public Result<SysRoleIndex> queryDefIndex() {
        SysRoleIndex defIndexCfg = sysRoleIndexService.queryDefaultIndex();
        return Result.OK(defIndexCfg);
    }

    /**
     * 更新默认首页配置
     */
    @RequiresPermissions("system:permission:setDefIndex")
    @PutMapping("/updateDefIndex")
    public Result<?> updateDefIndex(
            @RequestParam("url") String url,
            @RequestParam("component") String component,
            @RequestParam("isRoute") Boolean isRoute
    ) {
        boolean success = sysRoleIndexService.updateDefaultIndex(url, component, isRoute);
        if (success) {
            return Result.OK("设置成功");
        } else {
            return Result.error("设置失败");
        }
    }

}
