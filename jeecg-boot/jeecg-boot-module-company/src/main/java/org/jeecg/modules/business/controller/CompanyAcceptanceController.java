package org.jeecg.modules.business.controller;

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
import org.jeecg.modules.business.entity.CompanyAcceptance;
import org.jeecg.modules.business.service.ICompanyAcceptanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: company_acceptance
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
@Api(tags="company_acceptance")
@RestController
@RequestMapping("/business/companyAcceptance")
@Slf4j
public class CompanyAcceptanceController extends JeecgController<CompanyAcceptance, ICompanyAcceptanceService> {
	@Autowired
	private ICompanyAcceptanceService companyAcceptanceService;

	/**
	 * 分页列表查询
	 *
	 * @param companyAcceptance
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "company_acceptance-分页列表查询")
	@ApiOperation(value="company_acceptance-分页列表查询", notes="company_acceptance-分页列表查询")
	@GetMapping(value = "/list/{companyId}")
	public Result<?> queryPageList(CompanyAcceptance companyAcceptance,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,@PathVariable String companyId) {
		Map<String, String[]> parameterMap = new HashMap(req.getParameterMap());
		parameterMap.put("companyId_MultiString",new String[]{String.join(",", companyId)});
		QueryWrapper<CompanyAcceptance> queryWrapper = QueryGenerator.initQueryWrapper(companyAcceptance, parameterMap);
		Page<CompanyAcceptance> page = new Page<CompanyAcceptance>(pageNo, pageSize);
		IPage<CompanyAcceptance> pageList = companyAcceptanceService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyAcceptance
	 * @return
	 */
	@AutoLog(value = "company_acceptance-添加")
	@ApiOperation(value="company_acceptance-添加", notes="company_acceptance-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyAcceptance companyAcceptance) {
		companyAcceptanceService.save(companyAcceptance);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyAcceptance
	 * @return
	 */
	@AutoLog(value = "company_acceptance-编辑")
	@ApiOperation(value="company_acceptance-编辑", notes="company_acceptance-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyAcceptance companyAcceptance) {
		companyAcceptanceService.updateById(companyAcceptance);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "company_acceptance-通过id删除")
	@ApiOperation(value="company_acceptance-通过id删除", notes="company_acceptance-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyAcceptanceService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "company_acceptance-批量删除")
	@ApiOperation(value="company_acceptance-批量删除", notes="company_acceptance-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyAcceptanceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "company_acceptance-通过id查询")
	@ApiOperation(value="company_acceptance-通过id查询", notes="company_acceptance-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyAcceptance companyAcceptance = companyAcceptanceService.getById(id);
		if(companyAcceptance==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyAcceptance);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyAcceptance
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyAcceptance companyAcceptance) {
        return super.exportXls(request, companyAcceptance, CompanyAcceptance.class, "company_acceptance");
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
        return super.importExcel(request, response, CompanyAcceptance.class);
    }

}
