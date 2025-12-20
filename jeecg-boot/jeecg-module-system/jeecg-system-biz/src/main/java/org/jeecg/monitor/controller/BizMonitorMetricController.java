package org.jeecg.monitor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.monitor.entity.BizMonitorMetric;
import org.jeecg.monitor.service.IBizMonitorMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 业务指标快照 前端控制器
 * </p>
 *
 * @author jeecg-boot
 * @since 2025-06-20
 */
@RestController
@RequestMapping("/monitor/bizMetric")
@Slf4j
public class BizMonitorMetricController {

    @Autowired
    private IBizMonitorMetricService bizMonitorMetricService;

    /**
     * 分页列表查询
     *
     * @param bizMonitorMetric 业务指标对象
     * @param pageNo           页码
     * @param pageSize         每页条数
     * @param req              请求对象
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<IPage<BizMonitorMetric>> queryPageList(
            BizMonitorMetric bizMonitorMetric,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {
        Result<IPage<BizMonitorMetric>> result = new Result<>();
        var queryWrapper = QueryGenerator.initQueryWrapper(bizMonitorMetric, req.getParameterMap());
        Page<BizMonitorMetric> page = new Page<>(pageNo, pageSize);
        IPage<BizMonitorMetric> pageList = bizMonitorMetricService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

}