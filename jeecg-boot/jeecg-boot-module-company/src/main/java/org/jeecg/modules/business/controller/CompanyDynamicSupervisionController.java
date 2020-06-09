package org.jeecg.modules.business.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyDynamicSupervision;
import org.jeecg.modules.business.entity.CompanySupervisoryMonitor;
import org.jeecg.modules.business.service.ICompanyDynamicSupervisionService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ICompanySysuserService;
import org.jeecg.modules.business.vo.CompanyDynamicSupervisionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 企业年度动态监管
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
@Api(tags="企业年度动态监管")
@RestController
@RequestMapping("/cds/companyDynamicSupervision")
@Slf4j
public class CompanyDynamicSupervisionController extends JeecgController<CompanyDynamicSupervision, ICompanyDynamicSupervisionService> {
	@Autowired
	private ICompanyDynamicSupervisionService companyDynamicSupervisionService;
	/**
	 * 分页列表查询
	 *
	 * @param companyDynamicSupervision
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-分页列表查询")
	@ApiOperation(value="企业年度动态监管-分页列表查询", notes="企业年度动态监管-分页列表查询")
	@GetMapping(value = "/list/{companyId}")
	public Result<?> queryPageList(@PathVariable String companyId, CompanyDynamicSupervision companyDynamicSupervision,
								   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
								   HttpServletRequest req) {
		Map<String, String[]> parameterMap = new HashMap(req.getParameterMap());
		parameterMap.put("companyId_MultiString",new String[]{String.join(",", companyId)});
		QueryWrapper<CompanyDynamicSupervision> queryWrapper = QueryGenerator.initQueryWrapper(companyDynamicSupervision, parameterMap);
		Page<CompanyDynamicSupervision> page = new Page<CompanyDynamicSupervision>(pageNo, pageSize);
		IPage<CompanyDynamicSupervision> pageList = companyDynamicSupervisionService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	 /**
	  * 分页列表查询
	  *
	  * @param companyDynamicSupervision
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
//	 @AutoLog(value = "企业年度动态监管-分页列表查询")
//	 @ApiOperation(value="企业年度动态监管-分页列表查询", notes="企业年度动态监管-分页列表查询")
//	 @GetMapping(value = "/list2/xxx")
//	 public Result<?> queryPageListVO(@RequestParam(name = "companyId",required = true) String companyId,@RequestParam(name = "status") String status ,CompanyDynamicSupervisionVO companyDynamicSupervision,
//									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
//									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//									HttpServletRequest req) {
//		 Map<String, String[]> parameterMap = new HashMap(req.getParameterMap());
//		 parameterMap.put("companyId_MultiString",new String[]{String.join(",", companyId)});
//		 parameterMap.put("status",new String[]{String.join(",",status)});
//		 QueryWrapper<CompanyDynamicSupervisionVO> queryWrapper = QueryGenerator.initQueryWrapper(companyDynamicSupervision, parameterMap);
//		 Page<CompanyDynamicSupervisionVO> page = new Page<CompanyDynamicSupervisionVO>(pageNo, pageSize);
//		 IPage<CompanyDynamicSupervisionVO> pageList = companyDynamicSupervisionService.getCompanyDynamicSupervision(page, String.join(",", companyId),String.join(",",status));
//		 return Result.ok(pageList);
//	 }

	 /**
	  * 分页列表查询
	  *
	  * @param companyDynamicSupervision
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "企业年度动态监管-分页列表查询")
	 @ApiOperation(value="企业年度动态监管-分页列表查询", notes="企业年度动态监管-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<?> queryPageList(CompanyDynamicSupervisionVO companyDynamicSupervision,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 String companyId = req.getParameter("companyId");
		 String status = req.getParameter("status");
		 String companyName = req.getParameter("companyName");
		 String reportYear = req.getParameter("reportYear");
//		 QueryWrapper<CompanyDynamicSupervisionVO> queryWrapper = QueryGenerator.initQueryWrapper(companyDynamicSupervision,req.getParameterMap());
		 Page<CompanyDynamicSupervisionVO> page = new Page<>(pageNo, pageSize);
		 IPage<CompanyDynamicSupervisionVO> pageList = companyDynamicSupervisionService.getCompanyDynamicSupervision(page,companyId,status,companyName,reportYear);
		 return Result.ok(pageList);
	 }
	/**
	 *   添加
	 *
	 * @param companyDynamicSupervision
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-添加")
	@ApiOperation(value="企业年度动态监管-添加", notes="企业年度动态监管-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyDynamicSupervision companyDynamicSupervision) {
		companyDynamicSupervisionService.save(companyDynamicSupervision);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyDynamicSupervision
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-编辑")
	@ApiOperation(value="企业年度动态监管-编辑", notes="企业年度动态监管-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyDynamicSupervision companyDynamicSupervision) {
		companyDynamicSupervisionService.updateById(companyDynamicSupervision);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-通过id删除")
	@ApiOperation(value="企业年度动态监管-通过id删除", notes="企业年度动态监管-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyDynamicSupervisionService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-批量删除")
	@ApiOperation(value="企业年度动态监管-批量删除", notes="企业年度动态监管-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyDynamicSupervisionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业年度动态监管-通过id查询")
	@ApiOperation(value="企业年度动态监管-通过id查询", notes="企业年度动态监管-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyDynamicSupervision companyDynamicSupervision = companyDynamicSupervisionService.getById(id);
		if(companyDynamicSupervision==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyDynamicSupervision);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyDynamicSupervision
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyDynamicSupervision companyDynamicSupervision) {
        return super.exportXls(request, companyDynamicSupervision, CompanyDynamicSupervision.class, "企业年度动态监管");
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
        return super.importExcel(request, response, CompanyDynamicSupervision.class);
    }

}
