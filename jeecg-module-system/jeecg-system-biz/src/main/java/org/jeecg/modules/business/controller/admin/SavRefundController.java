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
import org.jeecg.modules.business.entity.SavRefund;
import org.jeecg.modules.business.entity.SavRefundWithDetail;
import org.jeecg.modules.business.service.ISavRefundService;
import org.jeecg.modules.business.service.ISavRefundWithDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 售后退款
 * @Author: jeecg-boot
 * @Date: 2022-08-12
 * @Version: V1.0
 */
@Api(tags = "售后退款")
@RestController
@RequestMapping("/savRefund/savRefund")
@Slf4j
public class SavRefundController extends JeecgController<SavRefund, ISavRefundService> {
    @Autowired
    private ISavRefundService savRefundService;

    @Autowired
    private ISavRefundWithDetailService savRefundWithShopCodeService;

    /**
     * 分页列表查询
     *
     * @param savRefund
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "售后退款-分页列表查询")
    @ApiOperation(value = "售后退款-分页列表查询", notes = "售后退款-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SavRefundWithDetail savRefund,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SavRefundWithDetail> queryWrapper = QueryGenerator.initQueryWrapper(savRefund, req.getParameterMap());
        Page<SavRefundWithDetail> page = new Page<>(pageNo, pageSize);
        IPage<SavRefundWithDetail> pageList = savRefundWithShopCodeService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param savRefund
     * @return
     */
    @AutoLog(value = "售后退款-添加")
    @ApiOperation(value = "售后退款-添加", notes = "售后退款-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SavRefund savRefund) {
        savRefundService.save(savRefund);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param savRefund
     * @return
     */
    @AutoLog(value = "售后退款-编辑")
    @ApiOperation(value = "售后退款-编辑", notes = "售后退款-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SavRefund savRefund) {
        savRefundService.updateById(savRefund);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "售后退款-通过id删除")
    @ApiOperation(value = "售后退款-通过id删除", notes = "售后退款-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        savRefundService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "售后退款-批量删除")
    @ApiOperation(value = "售后退款-批量删除", notes = "售后退款-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.savRefundService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "售后退款-通过id查询")
    @ApiOperation(value = "售后退款-通过id查询", notes = "售后退款-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SavRefund savRefund = savRefundService.getById(id);
        if (savRefund == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(savRefund);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param savRefund
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SavRefund savRefund) {
        return super.exportXls(request, savRefund, SavRefund.class, "售后退款");
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
        return super.importExcel(request, response, SavRefund.class);
    }

}
