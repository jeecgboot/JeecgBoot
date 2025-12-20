package org.jeecg.monitor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.monitor.entity.BizAlertRule;
import org.jeecg.monitor.service.IBizAlertRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 预警规则 前端控制器
 * </p>
 *
 * @author jeecg-boot
 * @since 2025-06-20
 */
@RestController
@RequestMapping("/monitor/alertRule")
@Slf4j
public class BizAlertRuleController {

    @Autowired
    private IBizAlertRuleService bizAlertRuleService;

    /**
     * 分页列表查询
     *
     * @param bizAlertRule 预警规则对象
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param req          请求对象
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<IPage<BizAlertRule>> queryPageList(
            BizAlertRule bizAlertRule,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {
        Result<IPage<BizAlertRule>> result = new Result<>();
        var queryWrapper = QueryGenerator.initQueryWrapper(bizAlertRule, req.getParameterMap());
        Page<BizAlertRule> page = new Page<>(pageNo, pageSize);
        IPage<BizAlertRule> pageList = bizAlertRuleService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加预警规则
     *
     * @param bizAlertRule 预警规则对象
     * @return 添加结果
     */
    @PostMapping("/add")
    public Result<BizAlertRule> add(@RequestBody BizAlertRule bizAlertRule) {
        Result<BizAlertRule> result = new Result<>();
        try {
            bizAlertRuleService.save(bizAlertRule);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑预警规则
     *
     * @param bizAlertRule 预警规则对象
     * @return 编辑结果
     */
    @PutMapping("/edit")
    public Result<BizAlertRule> edit(@RequestBody BizAlertRule bizAlertRule) {
        Result<BizAlertRule> result = new Result<>();
        try {
            bizAlertRuleService.updateById(bizAlertRule);
            result.success("编辑成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 删除预警规则
     *
     * @param id 预警规则ID
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            bizAlertRuleService.removeById(id);
            return Result.OK("删除成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("删除失败！");
        }
    }

}