package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyEnvTrial;
import org.jeecg.modules.business.service.ICompanyEnvTrialService;

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
 * @Description: 环评审批信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Api(tags="环评审批信息")
@RestController
@RequestMapping("/company/envTrial")
@Slf4j
public class CompanyEnvTrialController extends JeecgController<CompanyEnvTrial, ICompanyEnvTrialService> {
	@Autowired
	private ICompanyEnvTrialService companyEnvTrialService;
	
	/**
	 * 分页列表查询
	 *
	 * @param companyEnvTrial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "环评审批信息-分页列表查询")
	@ApiOperation(value="环评审批信息-分页列表查询", notes="环评审批信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyEnvTrial companyEnvTrial,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyEnvTrial> queryWrapper = QueryGenerator.initQueryWrapper(companyEnvTrial, req.getParameterMap());
		Page<CompanyEnvTrial> page = new Page<CompanyEnvTrial>(pageNo, pageSize);
		IPage<CompanyEnvTrial> pageList = companyEnvTrialService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyEnvTrial
	 * @return
	 */
	@AutoLog(value = "环评审批信息-添加")
	@ApiOperation(value="环评审批信息-添加", notes="环评审批信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyEnvTrial companyEnvTrial) {
		companyEnvTrialService.save(companyEnvTrial);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyEnvTrial
	 * @return
	 */
	@AutoLog(value = "环评审批信息-编辑")
	@ApiOperation(value="环评审批信息-编辑", notes="环评审批信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyEnvTrial companyEnvTrial) {
		companyEnvTrialService.updateById(companyEnvTrial);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "环评审批信息-通过id删除")
	@ApiOperation(value="环评审批信息-通过id删除", notes="环评审批信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyEnvTrialService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "环评审批信息-批量删除")
	@ApiOperation(value="环评审批信息-批量删除", notes="环评审批信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyEnvTrialService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "环评审批信息-通过id查询")
	@ApiOperation(value="环评审批信息-通过id查询", notes="环评审批信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyEnvTrial companyEnvTrial = companyEnvTrialService.getById(id);
		if(companyEnvTrial==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyEnvTrial);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyEnvTrial
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyEnvTrial companyEnvTrial) {
        return super.exportXls(request, companyEnvTrial, CompanyEnvTrial.class, "环评审批信息");
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
        return super.importExcel(request, response, CompanyEnvTrial.class);
    }

}
