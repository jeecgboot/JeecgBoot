package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.InvoiceNumberReservation;
import org.jeecg.modules.business.service.IInvoiceNumberReservationService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: invoice number reservation
 * @Author: jeecg-boot
 * @Date:   2025-03-28
 * @Version: V1.0
 */
@Api(tags="invoice number reservation")
@RestController
@RequestMapping("/business/invoiceNumberReservation")
@Slf4j
public class InvoiceNumberReservationController extends JeecgController<InvoiceNumberReservation, IInvoiceNumberReservationService> {
	@Autowired
	private IInvoiceNumberReservationService invoiceNumberReservationService;
	
	/**
	 * 分页列表查询
	 *
	 * @param invoiceNumberReservation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "invoice number reservation-分页列表查询")
	@ApiOperation(value="invoice number reservation-分页列表查询", notes="invoice number reservation-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<InvoiceNumberReservation>> queryPageList(InvoiceNumberReservation invoiceNumberReservation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InvoiceNumberReservation> queryWrapper = QueryGenerator.initQueryWrapper(invoiceNumberReservation, req.getParameterMap());
		Page<InvoiceNumberReservation> page = new Page<InvoiceNumberReservation>(pageNo, pageSize);
		IPage<InvoiceNumberReservation> pageList = invoiceNumberReservationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param invoiceNumberReservation
	 * @return
	 */
	@AutoLog(value = "invoice number reservation-添加")
	@ApiOperation(value="invoice number reservation-添加", notes="invoice number reservation-添加")
	@RequiresPermissions("business:invoice_number_reservation:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody InvoiceNumberReservation invoiceNumberReservation) {
		invoiceNumberReservationService.save(invoiceNumberReservation);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param invoiceNumberReservation
	 * @return
	 */
	@AutoLog(value = "invoice number reservation-编辑")
	@ApiOperation(value="invoice number reservation-编辑", notes="invoice number reservation-编辑")
	@RequiresPermissions("business:invoice_number_reservation:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody InvoiceNumberReservation invoiceNumberReservation) {
		invoiceNumberReservationService.updateById(invoiceNumberReservation);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "invoice number reservation-通过id删除")
	@ApiOperation(value="invoice number reservation-通过id删除", notes="invoice number reservation-通过id删除")
	@RequiresPermissions("business:invoice_number_reservation:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		invoiceNumberReservationService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "invoice number reservation-批量删除")
	@ApiOperation(value="invoice number reservation-批量删除", notes="invoice number reservation-批量删除")
	@RequiresPermissions("business:invoice_number_reservation:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.invoiceNumberReservationService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "invoice number reservation-通过id查询")
	@ApiOperation(value="invoice number reservation-通过id查询", notes="invoice number reservation-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<InvoiceNumberReservation> queryById(@RequestParam(name="id",required=true) String id) {
		InvoiceNumberReservation invoiceNumberReservation = invoiceNumberReservationService.getById(id);
		if(invoiceNumberReservation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(invoiceNumberReservation);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param invoiceNumberReservation
    */
    @RequiresPermissions("business:invoice_number_reservation:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvoiceNumberReservation invoiceNumberReservation) {
        return super.exportXls(request, invoiceNumberReservation, InvoiceNumberReservation.class, "invoice number reservation");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:invoice_number_reservation:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, InvoiceNumberReservation.class);
    }

}
