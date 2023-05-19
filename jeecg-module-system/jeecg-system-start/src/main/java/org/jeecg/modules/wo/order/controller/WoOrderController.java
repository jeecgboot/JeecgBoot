package org.jeecg.modules.wo.order.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.wo.order.entity.WoOrderDetail;
import org.jeecg.modules.wo.order.entity.WoOrder;
import org.jeecg.modules.wo.order.vo.WoOrderPage;
import org.jeecg.modules.wo.order.service.IWoOrderService;
import org.jeecg.modules.wo.order.service.IWoOrderDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 订单管理
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Api(tags="订单管理")
@RestController
@RequestMapping("/order/woOrder")
@Slf4j
public class WoOrderController {
	@Autowired
	private IWoOrderService woOrderService;
	@Autowired
	private IWoOrderDetailService woOrderDetailService;
	
	/**
	 * 分页列表查询
	 *
	 * @param woOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "订单管理-分页列表查询")
	@ApiOperation(value="订单管理-分页列表查询", notes="订单管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WoOrder woOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WoOrder> queryWrapper = QueryGenerator.initQueryWrapper(woOrder, req.getParameterMap());
		Page<WoOrder> page = new Page<WoOrder>(pageNo, pageSize);
		IPage<WoOrder> pageList = woOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param woOrderPage
	 * @return
	 */
	@AutoLog(value = "订单管理-添加")
	@ApiOperation(value="订单管理-添加", notes="订单管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WoOrderPage woOrderPage) {
		WoOrder woOrder = new WoOrder();
		BeanUtils.copyProperties(woOrderPage, woOrder);
		woOrderService.saveMain(woOrder, woOrderPage.getWoOrderDetailList());
		return Result.OK("添加成功!");
	}
	
	/**
	 * 编辑
	 *
	 * @param woOrderPage
	 * @return
	 */
	@AutoLog(value = "订单管理-编辑")
	@ApiOperation(value="订单管理-编辑", notes="订单管理-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody WoOrderPage woOrderPage) {
		WoOrder woOrder = new WoOrder();
		BeanUtils.copyProperties(woOrderPage, woOrder);
		woOrderService.updateMain(woOrder, woOrderPage.getWoOrderDetailList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单管理-通过id删除")
	@ApiOperation(value="订单管理-通过id删除", notes="订单管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
	    woOrderService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "订单管理-批量删除")
	@ApiOperation(value="订单管理-批量删除", notes="订单管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.woOrderService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
     *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单管理-通过id查询")
	@ApiOperation(value="订单管理-通过id查询", notes="订单管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WoOrder woOrder = woOrderService.getById(id);
		return Result.OK(woOrder);
	}
	
	/**
	 * 通过id查询
     *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单明细-通过主表ID查询")
	@ApiOperation(value="订单明细-通过主表ID查询", notes="订单管理-通过主表ID查询")
	@GetMapping(value = "/queryWoOrderDetailByMainId")
	public Result<?> queryWoOrderDetailListByMainId(@RequestParam(name="id",required=true) String id) {
		List<WoOrderDetail> woOrderDetailList = woOrderDetailService.selectByMainId(id);
		return Result.OK(woOrderDetailList);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param woOrder
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, WoOrder woOrder) {
      // Step.1 组装查询条件
      QueryWrapper<WoOrder> queryWrapper = QueryGenerator.initQueryWrapper(woOrder, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<WoOrderPage> pageList = new ArrayList<WoOrderPage>();
      List<WoOrder> woOrderList = woOrderService.list(queryWrapper);
      for (WoOrder temp : woOrderList) {
          WoOrderPage vo = new WoOrderPage();
          BeanUtils.copyProperties(temp, vo);
          List<WoOrderDetail> woOrderDetailList = woOrderDetailService.selectByMainId(temp.getId().toString());
          vo.setWoOrderDetailList(woOrderDetailList);
          pageList.add(vo);
      }
      //Step.3 调用AutoPoi导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "订单管理");
      mv.addObject(NormalExcelConstants.CLASS, WoOrderPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("订单管理数据", "导出人:"+sysUser.getRealname(), "订单管理"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<WoOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), WoOrderPage.class, params);
              for (WoOrderPage page : list) {
                  WoOrder po = new WoOrder();
                  BeanUtils.copyProperties(page, po);
                  woOrderService.saveMain(po, page.getWoOrderDetailList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
  }

}
