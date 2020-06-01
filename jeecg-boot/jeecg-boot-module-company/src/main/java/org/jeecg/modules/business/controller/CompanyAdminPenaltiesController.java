package org.jeecg.modules.business.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyAdminPenalties;
import org.jeecg.modules.business.service.ICompanyAdminPenaltiesService;

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

 /**
 * @Description: 行政处罚信息
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Api(tags="行政处罚信息")
@RestController
@RequestMapping("/cap/companyAdminPenalties")
@Slf4j
public class CompanyAdminPenaltiesController extends JeecgController<CompanyAdminPenalties, ICompanyAdminPenaltiesService> {
	@Autowired
	private ICompanyAdminPenaltiesService companyAdminPenaltiesService;
	
	/**
	 * 分页列表查询
	 *
	 * @param companyAdminPenalties
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-分页列表查询")
	@ApiOperation(value="行政处罚信息-分页列表查询", notes="行政处罚信息-分页列表查询")
	@GetMapping(value = "/list/{companyId}")
	public Result<?> queryPageList(@PathVariable String companyId, CompanyAdminPenalties companyAdminPenalties,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Map<String, String[]> parameterMap = new HashMap(req.getParameterMap());
		parameterMap.put("companyId_MultiString",new String[]{String.join(",", companyId)});
		QueryWrapper<CompanyAdminPenalties> queryWrapper = QueryGenerator.initQueryWrapper(companyAdminPenalties, parameterMap);
		Page<CompanyAdminPenalties> page = new Page<CompanyAdminPenalties>(pageNo, pageSize);
		IPage<CompanyAdminPenalties> pageList = companyAdminPenaltiesService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyAdminPenalties
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-添加")
	@ApiOperation(value="行政处罚信息-添加", notes="行政处罚信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyAdminPenalties companyAdminPenalties) {
		companyAdminPenaltiesService.save(companyAdminPenalties);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyAdminPenalties
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-编辑")
	@ApiOperation(value="行政处罚信息-编辑", notes="行政处罚信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyAdminPenalties companyAdminPenalties) {
		companyAdminPenaltiesService.updateById(companyAdminPenalties);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-通过id删除")
	@ApiOperation(value="行政处罚信息-通过id删除", notes="行政处罚信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyAdminPenaltiesService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-批量删除")
	@ApiOperation(value="行政处罚信息-批量删除", notes="行政处罚信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyAdminPenaltiesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "行政处罚信息-通过id查询")
	@ApiOperation(value="行政处罚信息-通过id查询", notes="行政处罚信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyAdminPenalties companyAdminPenalties = companyAdminPenaltiesService.getById(id);
		if(companyAdminPenalties==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyAdminPenalties);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyAdminPenalties
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyAdminPenalties companyAdminPenalties) {
        return super.exportXls(request, companyAdminPenalties, CompanyAdminPenalties.class, "行政处罚信息");
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
        return super.importExcel(request, response, CompanyAdminPenalties.class);
    }

}
