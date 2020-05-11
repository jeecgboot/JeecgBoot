package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
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
import org.jeecg.modules.system.entity.SysCheckRule;
import org.jeecg.modules.system.service.ISysCheckRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * @Description: 编码校验规则
 * @Author: jeecg-boot
 * @Date: 2020-02-04
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "编码校验规则")
@RestController
@RequestMapping("/sys/checkRule")
public class SysCheckRuleController extends JeecgController<SysCheckRule, ISysCheckRuleService> {

    @Autowired
    private ISysCheckRuleService sysCheckRuleService;

    /**
     * 分页列表查询
     *
     * @param sysCheckRule
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @AutoLog(value = "编码校验规则-分页列表查询")
    @ApiOperation(value = "编码校验规则-分页列表查询", notes = "编码校验规则-分页列表查询")
    @GetMapping(value = "/list")
    public Result queryPageList(
            SysCheckRule sysCheckRule,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ) {
        QueryWrapper<SysCheckRule> queryWrapper = QueryGenerator.initQueryWrapper(sysCheckRule, request.getParameterMap());
        Page<SysCheckRule> page = new Page<>(pageNo, pageSize);
        IPage<SysCheckRule> pageList = sysCheckRuleService.page(page, queryWrapper);
        return Result.ok(pageList);
    }


    /**
     * 通过id查询
     *
     * @param ruleCode
     * @return
     */
    @AutoLog(value = "编码校验规则-通过Code校验传入的值")
    @ApiOperation(value = "编码校验规则-通过Code校验传入的值", notes = "编码校验规则-通过Code校验传入的值")
    @GetMapping(value = "/checkByCode")
    public Result checkByCode(
            @RequestParam(name = "ruleCode") String ruleCode,
            @RequestParam(name = "value") String value
    ) throws UnsupportedEncodingException {
        SysCheckRule sysCheckRule = sysCheckRuleService.getByCode(ruleCode);
        if (sysCheckRule == null) {
            return Result.error("该编码不存在");
        }
        JSONObject errorResult = sysCheckRuleService.checkValue(sysCheckRule, URLDecoder.decode(value, "UTF-8"));
        if (errorResult == null) {
            return Result.ok();
        } else {
            Result<Object> r = Result.error(errorResult.getString("message"));
            r.setResult(errorResult);
            return r;
        }
    }

    /**
     * 添加
     *
     * @param sysCheckRule
     * @return
     */
    @AutoLog(value = "编码校验规则-添加")
    @ApiOperation(value = "编码校验规则-添加", notes = "编码校验规则-添加")
    @PostMapping(value = "/add")
    public Result add(@RequestBody SysCheckRule sysCheckRule) {
        sysCheckRuleService.save(sysCheckRule);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysCheckRule
     * @return
     */
    @AutoLog(value = "编码校验规则-编辑")
    @ApiOperation(value = "编码校验规则-编辑", notes = "编码校验规则-编辑")
    @PutMapping(value = "/edit")
    public Result edit(@RequestBody SysCheckRule sysCheckRule) {
        sysCheckRuleService.updateById(sysCheckRule);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "编码校验规则-通过id删除")
    @ApiOperation(value = "编码校验规则-通过id删除", notes = "编码校验规则-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result delete(@RequestParam(name = "id", required = true) String id) {
        sysCheckRuleService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "编码校验规则-批量删除")
    @ApiOperation(value = "编码校验规则-批量删除", notes = "编码校验规则-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysCheckRuleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "编码校验规则-通过id查询")
    @ApiOperation(value = "编码校验规则-通过id查询", notes = "编码校验规则-通过id查询")
    @GetMapping(value = "/queryById")
    public Result queryById(@RequestParam(name = "id", required = true) String id) {
        SysCheckRule sysCheckRule = sysCheckRuleService.getById(id);
        return Result.ok(sysCheckRule);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysCheckRule
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysCheckRule sysCheckRule) {
        return super.exportXls(request, sysCheckRule, SysCheckRule.class, "编码校验规则");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysCheckRule.class);
    }

}
