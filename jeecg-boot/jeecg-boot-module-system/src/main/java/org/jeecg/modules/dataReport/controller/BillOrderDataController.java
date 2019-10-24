package org.jeecg.modules.dataReport.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.dataReport.entity.BillOrderData;
import org.jeecg.modules.dataReport.service.IBillOrderDataService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 业务接单提单数据
 * @Author: Zhao
 * @Date:   2019-10-12
 * @Version: V1.0
 */
@Slf4j
@Api(tags="业务接单提单数据")
@RestController
@RequestMapping("/org.jeecg.modules.dataReport/billOrderData")
public class BillOrderDataController {
	@Autowired
	private IBillOrderDataService billOrderDataService;

	/**
	  * 分页列表查询
	 * @param billOrderData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "业务接单提单数据-分页列表查询")
	@ApiOperation(value="业务接单提单数据-分页列表查询", notes="业务接单提单数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BillOrderData>> queryPageList(BillOrderData billOrderData,
													  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													  HttpServletRequest req) {
		Result<IPage<BillOrderData>> result = new Result<IPage<BillOrderData>>();
		QueryWrapper<BillOrderData> queryWrapper = QueryGenerator.initQueryWrapper(billOrderData, req.getParameterMap());
		System.out.println(queryWrapper.getSqlSegment());//打印前端自带的查询where后的条件
		Page<BillOrderData> page = new Page<BillOrderData>(pageNo, pageSize);
		IPage<BillOrderData> pageList = billOrderDataService.getBillOrderDateList(page,queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	  *   添加
	 * @param billOrderData
	 * @return
	 */
	@AutoLog(value = "业务接单提单数据-添加")
	@ApiOperation(value="业务接单提单数据-添加", notes="业务接单提单数据-添加")
	@PostMapping(value = "/add")
	public Result<BillOrderData> add(@RequestBody BillOrderData billOrderData) {
		Result<BillOrderData> result = new Result<BillOrderData>();
		try {
			billOrderDataService.save(billOrderData);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	  *  编辑
	 * @param billOrderData
	 * @return
	 */
	@AutoLog(value = "业务接单提单数据-编辑")
	@ApiOperation(value="业务接单提单数据-编辑", notes="业务接单提单数据-编辑")
	@PutMapping(value = "/edit")
	public Result<BillOrderData> edit(@RequestBody BillOrderData billOrderData) {
		Result<BillOrderData> result = new Result<BillOrderData>();
		BillOrderData billOrderDataEntity = billOrderDataService.getById(billOrderData.getId());
		if(billOrderDataEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = billOrderDataService.updateById(billOrderData);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}

		return result;
	}

	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "业务接单提单数据-通过id删除")
	@ApiOperation(value="业务接单提单数据-通过id删除", notes="业务接单提单数据-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		try {
			billOrderDataService.removeById(id);
		} catch (Exception e) {
			log.error("删除失败",e.getMessage());
			return Result.error("删除失败!");
		}
		return Result.ok("删除成功!");
	}

	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "业务接单提单数据-批量删除")
	@ApiOperation(value="业务接单提单数据-批量删除", notes="业务接单提单数据-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<BillOrderData> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<BillOrderData> result = new Result<BillOrderData>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.billOrderDataService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "业务接单提单数据-通过id查询")
	@ApiOperation(value="业务接单提单数据-通过id查询", notes="业务接单提单数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BillOrderData> queryById(@RequestParam(name="id",required=true) String id) {
		Result<BillOrderData> result = new Result<BillOrderData>();
		BillOrderData billOrderData = billOrderDataService.getById(id);
		if(billOrderData==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(billOrderData);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(BillOrderData billOrderData,HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
	  QueryWrapper<BillOrderData> queryWrapper = QueryGenerator.initQueryWrapper(billOrderData, request.getParameterMap());
	  System.out.println(queryWrapper.getSqlSegment());//打印前端自带的查询where后的条件

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<BillOrderData> pageList = billOrderDataService.getBillOrderDateList(queryWrapper);
	  LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "业务接单提单数据列表");
      mv.addObject(NormalExcelConstants.CLASS, BillOrderData.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("业务接单提单数据列表数据", "导出人:" + user.getRealname() , "导出信息"));
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
              List<BillOrderData> listBillOrderDatas = ExcelImportUtil.importExcel(file.getInputStream(), BillOrderData.class, params);
              billOrderDataService.saveBatch(listBillOrderDatas);
              return Result.ok("文件导入成功！数据行数:" + listBillOrderDatas.size());
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
      return Result.ok("文件导入失败！");
  }

}
