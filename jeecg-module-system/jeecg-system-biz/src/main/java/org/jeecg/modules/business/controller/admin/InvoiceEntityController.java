package org.jeecg.modules.business.controller.admin;

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
import org.jeecg.modules.business.entity.InvoiceEntity;
import org.jeecg.modules.business.service.IInvoiceEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Api(tags = "发票实体")
@RestController
@RequestMapping("/invoiceEntity")
@Slf4j
public class InvoiceEntityController extends JeecgController<InvoiceEntity, IInvoiceEntityService> {
    @Autowired
    private IInvoiceEntityService invoiceEntityService;

    @ApiOperation(value = "发票实体-分页列表查询", notes = "发票实体-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<InvoiceEntity>> queryPageList(InvoiceEntity invoiceEntity,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        QueryWrapper<InvoiceEntity> queryWrapper = QueryGenerator.initQueryWrapper(invoiceEntity, req.getParameterMap());
        Page<InvoiceEntity> page = new Page<>(pageNo, pageSize);
        IPage<InvoiceEntity> pageList = invoiceEntityService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "发票实体-添加")
    @ApiOperation(value = "发票实体-添加", notes = "发票实体-添加")
    @RequiresPermissions("invoice_entity:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody InvoiceEntity invoiceEntity) {
        invoiceEntityService.save(invoiceEntity);
        return Result.OK("添加成功！");
    }

    @AutoLog(value = "发票实体-编辑")
    @ApiOperation(value = "发票实体-编辑", notes = "发票实体-编辑")
    @RequiresPermissions("invoice_entity:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody InvoiceEntity invoiceEntity) {
        invoiceEntityService.updateById(invoiceEntity);
        return Result.OK("编辑成功!");
    }

    @AutoLog(value = "发票实体-通过id删除")
    @ApiOperation(value = "发票实体-通过id删除", notes = "发票实体-通过id删除")
    @RequiresPermissions("invoice_entity:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        invoiceEntityService.removeById(id);
        return Result.OK("删除成功!");
    }

    @AutoLog(value = "发票实体-批量删除")
    @ApiOperation(value = "发票实体-批量删除", notes = "发票实体-批量删除")
    @RequiresPermissions("invoice_entity:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        invoiceEntityService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    @ApiOperation(value = "发票实体-通过id查询", notes = "发票实体-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<InvoiceEntity> queryById(@RequestParam(name = "id", required = true) String id) {
        InvoiceEntity invoiceEntity = invoiceEntityService.getById(id);
        if (invoiceEntity == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(invoiceEntity);
    }

    @ApiOperation(value = "发票实体-按客户查询", notes = "发票实体-按客户查询")
    @GetMapping(value = "/listByClientId")
    public Result<?> listByClientId(@RequestParam(name = "clientId", required = true) String clientId,
                                    @RequestParam(name = "activeOnly", defaultValue = "false") Boolean activeOnly) {
        if (activeOnly) {
            return Result.OK(invoiceEntityService.selectActiveByClientId(clientId));
        }
        return Result.OK(invoiceEntityService.selectByMainId(clientId));
    }

    @RequiresPermissions("invoice_entity:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvoiceEntity invoiceEntity) {
        return super.exportXls(request, invoiceEntity, InvoiceEntity.class, "发票实体");
    }

    @RequiresPermissions("invoice_entity:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, InvoiceEntity.class);
    }
}
