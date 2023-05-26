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
import org.jeecg.modules.business.entity.SkuLogisticChoice;
import org.jeecg.modules.business.service.ISkuLogisticChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: SKU物流选择
 * @Author: jeecg-boot
 * @Date: 2021-10-07
 * @Version: V1.0
 */
@Api(tags = "SKU物流选择")
@RestController
@RequestMapping("/skuLogisticChoice/skuLogisticChoice")
@Slf4j
public class SkuLogisticChoiceController extends JeecgController<SkuLogisticChoice, ISkuLogisticChoiceService> {
    @Autowired
    private ISkuLogisticChoiceService skuLogisticChoiceService;

    /**
     * 分页列表查询
     *
     * @param skuLogisticChoice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "SKU物流选择-分页列表查询")
    @ApiOperation(value = "SKU物流选择-分页列表查询", notes = "SKU物流选择-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SkuLogisticChoice skuLogisticChoice,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SkuLogisticChoice> queryWrapper = QueryGenerator.initQueryWrapper(skuLogisticChoice, req.getParameterMap());
        Page<SkuLogisticChoice> page = new Page<SkuLogisticChoice>(pageNo, pageSize);
        IPage<SkuLogisticChoice> pageList = skuLogisticChoiceService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param skuLogisticChoice
     * @return
     */
    @AutoLog(value = "SKU物流选择-添加")
    @ApiOperation(value = "SKU物流选择-添加", notes = "SKU物流选择-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SkuLogisticChoice skuLogisticChoice) {
        skuLogisticChoiceService.save(skuLogisticChoice);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param skuLogisticChoice
     * @return
     */
    @AutoLog(value = "SKU物流选择-编辑")
    @ApiOperation(value = "SKU物流选择-编辑", notes = "SKU物流选择-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SkuLogisticChoice skuLogisticChoice) {
        skuLogisticChoiceService.updateById(skuLogisticChoice);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "SKU物流选择-通过id删除")
    @ApiOperation(value = "SKU物流选择-通过id删除", notes = "SKU物流选择-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        skuLogisticChoiceService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "SKU物流选择-批量删除")
    @ApiOperation(value = "SKU物流选择-批量删除", notes = "SKU物流选择-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.skuLogisticChoiceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "SKU物流选择-通过id查询")
    @ApiOperation(value = "SKU物流选择-通过id查询", notes = "SKU物流选择-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SkuLogisticChoice skuLogisticChoice = skuLogisticChoiceService.getById(id);
        if (skuLogisticChoice == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(skuLogisticChoice);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param skuLogisticChoice
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SkuLogisticChoice skuLogisticChoice) {
        return super.exportXls(request, skuLogisticChoice, SkuLogisticChoice.class, "SKU物流选择");
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
        return super.importExcel(request, response, SkuLogisticChoice.class);
    }

}
