package org.jeecg.modules.business.controller.admin;

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
import org.jeecg.modules.business.entity.ExchangeRates;
import org.jeecg.modules.business.service.IExchangeRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 汇率表
 * @Author: jeecg-boot
 * @Date: 2021-06-26
 * @Version: V1.0
 */
@Api(tags = "汇率表")
@RestController
@RequestMapping("/exchange_rates/exchangeRates")
@Slf4j
public class ExchangeRatesController extends JeecgController<ExchangeRates, IExchangeRatesService> {
    @Autowired
    private IExchangeRatesService exchangeRatesService;

    /**
     * 分页列表查询
     *
     * @param exchangeRates
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "汇率表-分页列表查询")
    @ApiOperation(value = "汇率表-分页列表查询", notes = "汇率表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ExchangeRates exchangeRates,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ExchangeRates> queryWrapper = QueryGenerator.initQueryWrapper(exchangeRates, req.getParameterMap());
        Page<ExchangeRates> page = new Page<ExchangeRates>(pageNo, pageSize);
        IPage<ExchangeRates> pageList = exchangeRatesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param exchangeRates
     * @return
     */
    @AutoLog(value = "汇率表-添加")
    @ApiOperation(value = "汇率表-添加", notes = "汇率表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ExchangeRates exchangeRates) {
        exchangeRatesService.save(exchangeRates);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param exchangeRates
     * @return
     */
    @AutoLog(value = "汇率表-编辑")
    @ApiOperation(value = "汇率表-编辑", notes = "汇率表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ExchangeRates exchangeRates) {
        exchangeRatesService.updateById(exchangeRates);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "汇率表-通过id删除")
    @ApiOperation(value = "汇率表-通过id删除", notes = "汇率表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        exchangeRatesService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "汇率表-批量删除")
    @ApiOperation(value = "汇率表-批量删除", notes = "汇率表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.exchangeRatesService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "汇率表-通过id查询")
    @ApiOperation(value = "汇率表-通过id查询", notes = "汇率表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ExchangeRates exchangeRates = exchangeRatesService.getById(id);
        if (exchangeRates == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(exchangeRates);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param exchangeRates
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ExchangeRates exchangeRates) {
        return super.exportXls(request, exchangeRates, ExchangeRates.class, "汇率表");
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
        return super.importExcel(request, response, ExchangeRates.class);
    }

}
