package org.jeecg.modules.smartfuel.systemInfo.controller;

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
import org.jeecg.modules.smartfuel.systemInfo.entity.SmartfuelSystemInfo;
import org.jeecg.modules.smartfuel.systemInfo.service.ISmartfuelSystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: system_info
 * @Author: jeecg-boot
 * @Date: 2022-03-09
 * @Version: V1.0
 */
@Api(tags = "system_info")
@RestController
@RequestMapping("/systemInfo/smartfuelSystemInfo")
@Slf4j
public class SmartfuelSystemInfoController extends JeecgController<SmartfuelSystemInfo, ISmartfuelSystemInfoService> {
    @Autowired
    private ISmartfuelSystemInfoService smartfuelSystemInfoService;

    /**
     * 分页列表查询
     *
     * @param smartfuelSystemInfo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "system_info-分页列表查询")
    @ApiOperation(value = "system_info-分页列表查询", notes = "system_info-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SmartfuelSystemInfo smartfuelSystemInfo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SmartfuelSystemInfo> queryWrapper = QueryGenerator.initQueryWrapper(smartfuelSystemInfo, req.getParameterMap());
        Page<SmartfuelSystemInfo> page = new Page<SmartfuelSystemInfo>(pageNo, pageSize);
        IPage<SmartfuelSystemInfo> pageList = smartfuelSystemInfoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param smartfuelSystemInfo
     * @return
     */
    @AutoLog(value = "system_info-添加")
    @ApiOperation(value = "system_info-添加", notes = "system_info-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SmartfuelSystemInfo smartfuelSystemInfo) {
        smartfuelSystemInfoService.save(smartfuelSystemInfo);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param smartfuelSystemInfo
     * @return
     */
    @AutoLog(value = "system_info-编辑")
    @ApiOperation(value = "system_info-编辑", notes = "system_info-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SmartfuelSystemInfo smartfuelSystemInfo) {
        smartfuelSystemInfoService.updateById(smartfuelSystemInfo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "system_info-通过id删除")
    @ApiOperation(value = "system_info-通过id删除", notes = "system_info-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        smartfuelSystemInfoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "system_info-批量删除")
    @ApiOperation(value = "system_info-批量删除", notes = "system_info-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.smartfuelSystemInfoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "system_info-通过id查询")
    @ApiOperation(value = "system_info-通过id查询", notes = "system_info-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SmartfuelSystemInfo smartfuelSystemInfo = smartfuelSystemInfoService.getById(id);
        if (smartfuelSystemInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartfuelSystemInfo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param smartfuelSystemInfo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SmartfuelSystemInfo smartfuelSystemInfo) {
        return super.exportXls(request, smartfuelSystemInfo, SmartfuelSystemInfo.class, "system_info");
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
        return super.importExcel(request, response, SmartfuelSystemInfo.class);
    }

    /**
     * 获取系统信息
     * 将最新一条数据以JSON对象形式返回
     *
     * @return
     */
    @AutoLog(value = "system_info-获取系统信息")
    @ApiOperation(value = "system_info-获取系统信息", notes = "system_info-获取系统信息")
    @GetMapping(value = "/getSystemInfo")
    public Result<?> getSystemInfo() {
        SmartfuelSystemInfo smartfuelSystemInfo = smartfuelSystemInfoService.getSystemInfo();
        if (smartfuelSystemInfo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(smartfuelSystemInfo);
    }

}
