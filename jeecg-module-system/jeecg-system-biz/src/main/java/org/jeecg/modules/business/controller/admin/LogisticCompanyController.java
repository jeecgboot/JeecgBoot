package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.LogisticCompany;
import org.jeecg.modules.business.service.ILogisticCompanyService;

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
 * @Description: logistic_company
 * @Author: jeecg-boot
 * @Date:   2025-03-03
 * @Version: V1.0
 */
@Api(tags="logistic_company")
@RestController
@RequestMapping("/logisticCompany")
@Slf4j
public class LogisticCompanyController extends JeecgController<LogisticCompany, ILogisticCompanyService> {
	@Autowired
	private ILogisticCompanyService logisticCompanyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param logisticCompany
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "logistic_company-分页列表查询")
	@ApiOperation(value="logistic_company-分页列表查询", notes="logistic_company-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<LogisticCompany>> queryPageList(LogisticCompany logisticCompany,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LogisticCompany> queryWrapper = QueryGenerator.initQueryWrapper(logisticCompany, req.getParameterMap());
		Page<LogisticCompany> page = new Page<LogisticCompany>(pageNo, pageSize);
		IPage<LogisticCompany> pageList = logisticCompanyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param logisticCompany
	 * @return
	 */
	@AutoLog(value = "logistic_company-添加")
	@ApiOperation(value="logistic_company-添加", notes="logistic_company-添加")
	@RequiresPermissions("business:logistic_company:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody LogisticCompany logisticCompany) {
		logisticCompanyService.save(logisticCompany);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param logisticCompany
	 * @return
	 */
	@AutoLog(value = "logistic_company-编辑")
	@ApiOperation(value="logistic_company-编辑", notes="logistic_company-编辑")
	@RequiresPermissions("business:logistic_company:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody LogisticCompany logisticCompany) {
		logisticCompanyService.updateById(logisticCompany);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "logistic_company-通过id删除")
	@ApiOperation(value="logistic_company-通过id删除", notes="logistic_company-通过id删除")
	@RequiresPermissions("business:logistic_company:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		logisticCompanyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "logistic_company-批量删除")
	@ApiOperation(value="logistic_company-批量删除", notes="logistic_company-批量删除")
	@RequiresPermissions("business:logistic_company:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.logisticCompanyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "logistic_company-通过id查询")
	@ApiOperation(value="logistic_company-通过id查询", notes="logistic_company-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<LogisticCompany> queryById(@RequestParam(name="id",required=true) String id) {
		LogisticCompany logisticCompany = logisticCompanyService.getById(id);
		if(logisticCompany==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(logisticCompany);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param logisticCompany
    */
    @RequiresPermissions("business:logistic_company:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LogisticCompany logisticCompany) {
        return super.exportXls(request, logisticCompany, LogisticCompany.class, "logistic_company");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:logistic_company:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, LogisticCompany.class);
    }

}
