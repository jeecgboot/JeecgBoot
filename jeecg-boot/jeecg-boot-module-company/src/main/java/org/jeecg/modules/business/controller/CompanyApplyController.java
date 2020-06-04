package org.jeecg.modules.business.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyBaseinfo;
import org.jeecg.modules.business.service.ICompanyApplyService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ICompanyBaseinfoService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 企业申报基础表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Api(tags="企业申报基础表")
@RestController
@RequestMapping("/company/apply")
@Slf4j
public class CompanyApplyController extends JeecgController<CompanyApply, ICompanyApplyService> {
	@Autowired
	private ICompanyApplyService companyApplyService;
	 @Autowired
	 private ICompanyBaseinfoService companyBaseinfoService;
	/**
	 * 分页列表查询
	 *
	 * @param companyApply
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "企业申报基础表-分页列表查询")
	@ApiOperation(value="企业申报基础表-分页列表查询", notes="企业申报基础表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyApply companyApply,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyApply> queryWrapper = QueryGenerator.initQueryWrapper(companyApply, req.getParameterMap());
		Page<CompanyApply> page = new Page<CompanyApply>(pageNo, pageSize);
		IPage<CompanyApply> pageList = companyApplyService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyApply
	 * @return
	 */
	@AutoLog(value = "企业申报基础表-添加")
	@ApiOperation(value="企业申报基础表-添加", notes="企业申报基础表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyApply companyApply) {
		companyApplyService.save(companyApply);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyApply
	 * @return
	 */
	@AutoLog(value = "企业申报基础表-编辑")
	@ApiOperation(value="企业申报基础表-编辑", notes="企业申报基础表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyApply companyApply) {
		companyApplyService.updateById(companyApply);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业申报基础表-通过id删除")
	@ApiOperation(value="企业申报基础表-通过id删除", notes="企业申报基础表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyApplyService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "企业申报基础表-批量删除")
	@ApiOperation(value="企业申报基础表-批量删除", notes="企业申报基础表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyApplyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "企业申报基础表-通过id查询")
	@ApiOperation(value="企业申报基础表-通过id查询", notes="企业申报基础表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyApply companyApply = companyApplyService.getById(id);
		if(companyApply==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyApply);
	}
	 /**
	  * 通过id查询
	  *
	  * @param companyId
	  * @return
	  */
	 @AutoLog(value = "企业申报基础表-通过id查询")
	 @ApiOperation(value="企业申报基础表-通过id查询", notes="企业申报基础表-通过id查询")
	 @GetMapping(value = "/queryLatestArchivedData")
	 public Result<?> queryLatestArchivedData(@RequestParam(name="companyId",required=true) String companyId,@RequestParam(name="fromTable",required=true) String fromTable) {
	 	 //查询正常状态的  申报信息
		 List<CompanyApply> companyApplys = companyApplyService.list(new QueryWrapper<CompanyApply>().lambda()
				 .eq(CompanyApply::getCompanyId,companyId).eq(CompanyApply::getStatus, Constant.status.NORMAL).eq(CompanyApply::getFromTable,fromTable));//审核通过的

		 if(companyApplys==null||companyApplys.isEmpty()) {
			 return Result.ok();
		 }else{
		 	//查询到了数据
//			 companyApplys.forEach(companyApply -> {
			 	//查询审核通过的，最近的一条数据
//			 });
			 //或者直接查询基础信息数据表
			 CompanyBaseinfo companyBaseinfo = companyBaseinfoService.queryByCompanyId( companyId);
			 return Result.ok(companyBaseinfo);
		 }

	 }
    /**
    * 导出excel
    *
    * @param request
    * @param companyApply
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyApply companyApply) {
        return super.exportXls(request, companyApply, CompanyApply.class, "企业申报基础表");
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
        return super.importExcel(request, response, CompanyApply.class);
    }

}
