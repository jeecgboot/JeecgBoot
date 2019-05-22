package org.jeecg.modules.demo.test.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;
import org.jeecg.modules.demo.test.entity.JeecgOrderMain;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import org.jeecg.modules.demo.test.service.IJeecgOrderCustomerService;
import org.jeecg.modules.demo.test.service.IJeecgOrderMainService;
import org.jeecg.modules.demo.test.service.IJeecgOrderTicketService;
import org.jeecg.modules.demo.test.vo.JeecgOrderMainPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title: Controller
 * @Description: 订单 
 * @Author: jeecg-boot 
 * @Date:2019-02-15 
 * @Version: V1.0
 */
@RestController
@RequestMapping("/test/jeecgOrderMain")
@Slf4j
public class JeecgOrderMainController {
	@Autowired
	private IJeecgOrderMainService jeecgOrderMainService;
	@Autowired
	private IJeecgOrderCustomerService jeecgOrderCustomerService;
	@Autowired
	private IJeecgOrderTicketService jeecgOrderTicketService;

	/**
	 * 分页列表查询
	 * 
	 * @param jeecgOrderMain
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<JeecgOrderMain>> queryPageList(JeecgOrderMain jeecgOrderMain, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<JeecgOrderMain>> result = new Result<IPage<JeecgOrderMain>>();
		QueryWrapper<JeecgOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(jeecgOrderMain, req.getParameterMap());
		Page<JeecgOrderMain> page = new Page<JeecgOrderMain>(pageNo, pageSize);
		IPage<JeecgOrderMain> pageList = jeecgOrderMainService.page(page, queryWrapper);
		// log.debug("查询当前页："+pageList.getCurrent());
		// log.debug("查询当前页数量："+pageList.getSize());
		// log.debug("查询结果数量："+pageList.getRecords().size());
		// log.debug("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * 添加
	 * 
	 * @param jeecgOrderMain
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<JeecgOrderMain> add(@RequestBody JeecgOrderMainPage jeecgOrderMainPage) {
		Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
		try {
			JeecgOrderMain jeecgOrderMain = new JeecgOrderMain();
			BeanUtils.copyProperties(jeecgOrderMainPage, jeecgOrderMain);
			jeecgOrderMainService.saveMain(jeecgOrderMain, jeecgOrderMainPage.getJeecgOrderCustomerList(), jeecgOrderMainPage.getJeecgOrderTicketList());
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 编辑
	 * 
	 * @param jeecgOrderMain
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<JeecgOrderMain> eidt(@RequestBody JeecgOrderMainPage jeecgOrderMainPage) {
		Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
		JeecgOrderMain jeecgOrderMain = new JeecgOrderMain();
		BeanUtils.copyProperties(jeecgOrderMainPage, jeecgOrderMain);
		JeecgOrderMain jeecgOrderMainEntity = jeecgOrderMainService.getById(jeecgOrderMain.getId());
		if (jeecgOrderMainEntity == null) {
			result.error500("未找到对应实体");
		} else {
			jeecgOrderMainService.updateMain(jeecgOrderMain, jeecgOrderMainPage.getJeecgOrderCustomerList(), jeecgOrderMainPage.getJeecgOrderTicketList());
			result.success("修改成功!");
		}

		return result;
	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<JeecgOrderMain> delete(@RequestParam(name = "id", required = true) String id) {
		Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
		JeecgOrderMain jeecgOrderMain = jeecgOrderMainService.getById(id);
		if (jeecgOrderMain == null) {
			result.error500("未找到对应实体");
		} else {
			jeecgOrderMainService.delMain(id);
			result.success("删除成功!");
		}

		return result;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<JeecgOrderMain> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		} else {
			this.jeecgOrderMainService.delBatchMain(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<JeecgOrderMain> queryById(@RequestParam(name = "id", required = true) String id) {
		Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
		JeecgOrderMain jeecgOrderMain = jeecgOrderMainService.getById(id);
		if (jeecgOrderMain == null) {
			result.error500("未找到对应实体");
		} else {
			result.setResult(jeecgOrderMain);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryOrderCustomerListByMainId")
	public Result<List<JeecgOrderCustomer>> queryOrderCustomerListByMainId(@RequestParam(name = "id", required = true) String id) {
		Result<List<JeecgOrderCustomer>> result = new Result<List<JeecgOrderCustomer>>();
		List<JeecgOrderCustomer> jeecgOrderCustomerList = jeecgOrderCustomerService.selectCustomersByMainId(id);
		result.setResult(jeecgOrderCustomerList);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryOrderTicketListByMainId")
	public Result<List<JeecgOrderTicket>> queryOrderTicketListByMainId(@RequestParam(name = "id", required = true) String id) {
		Result<List<JeecgOrderTicket>> result = new Result<List<JeecgOrderTicket>>();
		List<JeecgOrderTicket> jeecgOrderTicketList = jeecgOrderTicketService.selectTicketsByMainId(id);
		result.setResult(jeecgOrderTicketList);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, JeecgOrderMain jeecgOrderMain) {
		// Step.1 组装查询条件
		QueryWrapper<JeecgOrderMain> queryWrapper = QueryGenerator.initQueryWrapper(jeecgOrderMain, request.getParameterMap());
		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<JeecgOrderMainPage> pageList = new ArrayList<JeecgOrderMainPage>();

		List<JeecgOrderMain> jeecgOrderMainList = jeecgOrderMainService.list(queryWrapper);
		for (JeecgOrderMain orderMain : jeecgOrderMainList) {
			JeecgOrderMainPage vo = new JeecgOrderMainPage();
			BeanUtils.copyProperties(orderMain, vo);
			// 查询机票
			List<JeecgOrderTicket> jeecgOrderTicketList = jeecgOrderTicketService.selectTicketsByMainId(orderMain.getId());
			vo.setJeecgOrderTicketList(jeecgOrderTicketList);
			// 查询客户
			List<JeecgOrderCustomer> jeecgOrderCustomerList = jeecgOrderCustomerService.selectCustomersByMainId(orderMain.getId());
			vo.setJeecgOrderCustomerList(jeecgOrderCustomerList);
			pageList.add(vo);
		}

		// 导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "一对多导出文件名字");
		// 注解对象Class
		mv.addObject(NormalExcelConstants.CLASS, JeecgOrderMainPage.class);
		// 自定义表格参数
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("自定义导出Excel内容标题", "导出人:Jeecg", "自定义Sheet名字"));
		// 导出数据列表
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;
	}

	/**
	 * 通过excel导入数据
	 * 
	 * @param request
	 * @param
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
			params.setHeadRows(2);
			params.setNeedSave(true);
			try {
				List<JeecgOrderMainPage> list = ExcelImportUtil.importExcel(file.getInputStream(), JeecgOrderMainPage.class, params);
				for (JeecgOrderMainPage page : list) {
					JeecgOrderMain po = new JeecgOrderMain();
					BeanUtils.copyProperties(page, po);
					jeecgOrderMainService.saveMain(po, page.getJeecgOrderCustomerList(), page.getJeecgOrderTicketList());
				}
				return Result.ok("文件导入成功！");
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return Result.error("文件导入失败："+e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return Result.error("文件导入失败！");
	}

}
