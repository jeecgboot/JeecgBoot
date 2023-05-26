package org.jeecg.modules.order.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.order.entity.ResponseOrder;
import org.jeecg.modules.order.entity.RequestOrder;
import org.jeecg.modules.order.vo.RequestOrderPage;
import org.jeecg.modules.order.service.IRequestOrderService;
import org.jeecg.modules.order.service.IResponseOrderService;
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
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: Request Order
 * @Author: jeecg-boot
 * @Date:   2021-05-05
 * @Version: V1.0
 */
@Api(tags="Request Order")
@RestController
@RequestMapping("/order/requestOrder")
@Slf4j
public class RequestOrderController {
	@Autowired
	private IRequestOrderService requestOrderService;
	@Autowired
	private IResponseOrderService responseOrderService;
	
	/**
	 * 分页列表查询
	 *
	 * @param requestOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "Request Order-分页列表查询")
	@ApiOperation(value="Request Order-分页列表查询", notes="Request Order-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RequestOrder requestOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RequestOrder> queryWrapper = QueryGenerator.initQueryWrapper(requestOrder, req.getParameterMap());
		Page<RequestOrder> page = new Page<RequestOrder>(pageNo, pageSize);
		IPage<RequestOrder> pageList = requestOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param requestOrderPage
	 * @return
	 */
	@AutoLog(value = "Request Order-添加")
	@ApiOperation(value="Request Order-添加", notes="Request Order-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RequestOrderPage requestOrderPage) {
		RequestOrder requestOrder = new RequestOrder();
		BeanUtils.copyProperties(requestOrderPage, requestOrder);
		requestOrderService.saveMain(requestOrder, requestOrderPage.getResponseOrderList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param requestOrderPage
	 * @return
	 */
	@AutoLog(value = "Request Order-编辑")
	@ApiOperation(value="Request Order-编辑", notes="Request Order-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RequestOrderPage requestOrderPage) {
		RequestOrder requestOrder = new RequestOrder();
		BeanUtils.copyProperties(requestOrderPage, requestOrder);
		RequestOrder requestOrderEntity = requestOrderService.getById(requestOrder.getId());
		if(requestOrderEntity==null) {
			return Result.error("未找到对应数据");
		}
		requestOrderService.updateMain(requestOrder, requestOrderPage.getResponseOrderList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "Request Order-通过id删除")
	@ApiOperation(value="Request Order-通过id删除", notes="Request Order-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		requestOrderService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "Request Order-批量删除")
	@ApiOperation(value="Request Order-批量删除", notes="Request Order-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.requestOrderService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "Request Order-通过id查询")
	@ApiOperation(value="Request Order-通过id查询", notes="Request Order-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RequestOrder requestOrder = requestOrderService.getById(id);
		if(requestOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(requestOrder);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "Response Order通过主表ID查询")
	@ApiOperation(value="Response Order主表ID查询", notes="Response Order-通主表ID查询")
	@GetMapping(value = "/queryResponseOrderByMainId")
	public Result<?> queryResponseOrderListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ResponseOrder> responseOrderList = responseOrderService.selectByMainId(id);
		return Result.OK(responseOrderList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param requestOrder
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RequestOrder requestOrder) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<RequestOrder> queryWrapper = QueryGenerator.initQueryWrapper(requestOrder, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<RequestOrder> queryList = requestOrderService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<RequestOrder> requestOrderList = new ArrayList<RequestOrder>();
      if(oConvertUtils.isEmpty(selections)) {
          requestOrderList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          requestOrderList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<RequestOrderPage> pageList = new ArrayList<RequestOrderPage>();
      for (RequestOrder main : requestOrderList) {
          RequestOrderPage vo = new RequestOrderPage();
          BeanUtils.copyProperties(main, vo);
          List<ResponseOrder> responseOrderList = responseOrderService.selectByMainId(main.getId());
          vo.setResponseOrderList(responseOrderList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "Request Order列表");
      mv.addObject(NormalExcelConstants.CLASS, RequestOrderPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("Request Order数据", "导出人:"+sysUser.getRealname(), "Request Order"));
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
              List<RequestOrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), RequestOrderPage.class, params);
              for (RequestOrderPage page : list) {
                  RequestOrder po = new RequestOrder();
                  BeanUtils.copyProperties(page, po);
                  requestOrderService.saveMain(po, page.getResponseOrderList());
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
