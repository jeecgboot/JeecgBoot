package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.LogisticInsurance;
import org.jeecg.modules.business.service.ILogisticInsuranceService;

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
 * @Description: logistic insurance
 * @Author: jeecg-boot
 * @Date:   2025-03-04
 * @Version: V1.0
 */
@Api(tags="logistic insurance")
@RestController
@RequestMapping("/business/logisticInsurance")
@Slf4j
public class LogisticInsuranceController extends JeecgController<LogisticInsurance, ILogisticInsuranceService> {
	@Autowired
	private ILogisticInsuranceService logisticInsuranceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param logisticInsurance
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "logistic insurance-分页列表查询")
	@ApiOperation(value="logistic insurance-分页列表查询", notes="logistic insurance-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<LogisticInsurance>> queryPageList(LogisticInsurance logisticInsurance,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LogisticInsurance> queryWrapper = QueryGenerator.initQueryWrapper(logisticInsurance, req.getParameterMap());
		Page<LogisticInsurance> page = new Page<LogisticInsurance>(pageNo, pageSize);
		IPage<LogisticInsurance> pageList = logisticInsuranceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param logisticInsurance
	 * @return
	 */
	@AutoLog(value = "logistic insurance-添加")
	@ApiOperation(value="logistic insurance-添加", notes="logistic insurance-添加")
	@RequiresPermissions("business:logistic_insurance:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody LogisticInsurance logisticInsurance) {
		logisticInsuranceService.save(logisticInsurance);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param logisticInsurance
	 * @return
	 */
	@AutoLog(value = "logistic insurance-编辑")
	@ApiOperation(value="logistic insurance-编辑", notes="logistic insurance-编辑")
	@RequiresPermissions("business:logistic_insurance:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody LogisticInsurance logisticInsurance) {
		logisticInsuranceService.updateById(logisticInsurance);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "logistic insurance-通过id删除")
	@ApiOperation(value="logistic insurance-通过id删除", notes="logistic insurance-通过id删除")
	@RequiresPermissions("business:logistic_insurance:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		logisticInsuranceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "logistic insurance-批量删除")
	@ApiOperation(value="logistic insurance-批量删除", notes="logistic insurance-批量删除")
	@RequiresPermissions("business:logistic_insurance:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.logisticInsuranceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "logistic insurance-通过id查询")
	@ApiOperation(value="logistic insurance-通过id查询", notes="logistic insurance-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<LogisticInsurance> queryById(@RequestParam(name="id",required=true) String id) {
		LogisticInsurance logisticInsurance = logisticInsuranceService.getById(id);
		if(logisticInsurance==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(logisticInsurance);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param logisticInsurance
    */
    @RequiresPermissions("business:logistic_insurance:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LogisticInsurance logisticInsurance) {
        return super.exportXls(request, logisticInsurance, LogisticInsurance.class, "logistic insurance");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:logistic_insurance:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, LogisticInsurance.class);
    }

}
