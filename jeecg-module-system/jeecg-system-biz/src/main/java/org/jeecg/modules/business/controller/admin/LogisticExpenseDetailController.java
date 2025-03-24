package org.jeecg.modules.business.controller.admin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.LogisticExpenseDetail;
import org.jeecg.modules.business.service.ILogisticCompanyService;
import org.jeecg.modules.business.service.ILogisticExpenseDetailService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.LogisticCompanyEnum;
import org.jeecg.modules.business.vo.ResponsesWithMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @Description: logistic_expense_detail
 * @Author: jeecg-boot
 * @Date:   2025-02-25
 * @Version: V1.0
 */
@Api(tags="logistic_expense_detail")
@RestController
@RequestMapping("/logisticExpenseDetail")
@Slf4j
public class LogisticExpenseDetailController extends JeecgController<LogisticExpenseDetail, ILogisticExpenseDetailService> {
	@Autowired
	private ILogisticExpenseDetailService logisticExpenseDetailService;
	@Autowired
	private ILogisticCompanyService logisticCompanyService;

	/**
	 * 分页列表查询
	 *
	 * @param logisticExpenseDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "logistic_expense_detail-分页列表查询")
	@ApiOperation(value="logistic_expense_detail-分页列表查询", notes="logistic_expense_detail-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<LogisticExpenseDetail>> queryPageList(LogisticExpenseDetail logisticExpenseDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LogisticExpenseDetail> queryWrapper = QueryGenerator.initQueryWrapper(logisticExpenseDetail, req.getParameterMap());
		Page<LogisticExpenseDetail> page = new Page<LogisticExpenseDetail>(pageNo, pageSize);
		IPage<LogisticExpenseDetail> pageList = logisticExpenseDetailService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param logisticExpenseDetail
	 * @return
	 */
	@AutoLog(value = "logistic_expense_detail-添加")
	@ApiOperation(value="logistic_expense_detail-添加", notes="logistic_expense_detail-添加")
	@RequiresPermissions("business:logistic_expense_detail:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody LogisticExpenseDetail logisticExpenseDetail) {
		logisticExpenseDetailService.save(logisticExpenseDetail);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param logisticExpenseDetail
	 * @return
	 */
	@AutoLog(value = "logistic_expense_detail-编辑")
	@ApiOperation(value="logistic_expense_detail-编辑", notes="logistic_expense_detail-编辑")
	@RequiresPermissions("business:logistic_expense_detail:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody LogisticExpenseDetail logisticExpenseDetail) {
		logisticExpenseDetailService.updateById(logisticExpenseDetail);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "logistic_expense_detail-通过id删除")
	@ApiOperation(value="logistic_expense_detail-通过id删除", notes="logistic_expense_detail-通过id删除")
	@RequiresPermissions("business:logistic_expense_detail:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		logisticExpenseDetailService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "logistic_expense_detail-批量删除")
	@ApiOperation(value="logistic_expense_detail-批量删除", notes="logistic_expense_detail-批量删除")
	@RequiresPermissions("business:logistic_expense_detail:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.logisticExpenseDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "logistic_expense_detail-通过id查询")
	@ApiOperation(value="logistic_expense_detail-通过id查询", notes="logistic_expense_detail-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<LogisticExpenseDetail> queryById(@RequestParam(name="id",required=true) String id) {
		LogisticExpenseDetail logisticExpenseDetail = logisticExpenseDetailService.getById(id);
		if(logisticExpenseDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(logisticExpenseDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param logisticExpenseDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LogisticExpenseDetail logisticExpenseDetail) {
        return super.exportXls(request, logisticExpenseDetail, LogisticExpenseDetail.class, "logistic_expense_detail");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request) throws IOException, ServletException {
		log.info("Importing logistic expense detail excel");
		ResponsesWithMsg responses = new ResponsesWithMsg();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		if(file == null) {
			return Result.error(400,"Missing file");
		}
		request.getParameterMap().forEach((k, v) -> log.info("key: {}, value: {}", k, Arrays.asList(v)));
		String logisticCompany = request.getParameterValues("logisticCompany")[0];
		System.out.println(logisticCompany);
		if(logisticCompany == null) {
			return Result.error(400,"Missing logistic company param");
		}
		try {
			LogisticCompanyEnum logisticCompanyEnum = LogisticCompanyEnum.valueOf(logisticCompany);
			logisticExpenseDetailService.importExcel(file, logisticCompanyEnum);
		} catch (IllegalArgumentException e) {
			return Result.error(400,"Invalid logistic company param");
		}

		return Result.OK();
    }
}