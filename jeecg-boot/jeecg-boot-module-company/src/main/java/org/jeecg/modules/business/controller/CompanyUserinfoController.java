package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyUserinfo;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyUserinfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: company_userinfo
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Api(tags="company_userinfo")
@RestController
@RequestMapping("/companyUserinfo")
@Slf4j
public class CompanyUserinfoController extends JeecgController<CompanyUserinfo, ICompanyUserinfoService> {
	@Autowired
	private ICompanyUserinfoService companyUserinfoService;
	@Autowired
	private ICompanyApplyService companyApplyService;
	/**
	 * 分页列表查询
	 *
	 * @param companyUserinfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "company_userinfo-分页列表查询")
	@ApiOperation(value="company_userinfo-分页列表查询", notes="company_userinfo-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyUserinfo companyUserinfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyUserinfo> queryWrapper = QueryGenerator.initQueryWrapper(companyUserinfo, req.getParameterMap());
		Page<CompanyUserinfo> page = new Page<CompanyUserinfo>(pageNo, pageSize);
		IPage<CompanyUserinfo> pageList = companyUserinfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyUserinfo
	 * @return
	 */
	@AutoLog(value = "company_userinfo-添加")
	@ApiOperation(value="company_userinfo-添加", notes="company_userinfo-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyUserinfo companyUserinfo) {
		companyUserinfo.setStatus(Constant.status.TEMPORARY);//暂存
		companyUserinfoService.save(companyUserinfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyUserinfo
	 * @return
	 */
	@AutoLog(value = "company_userinfo-编辑")
	@ApiOperation(value="company_userinfo-编辑", notes="company_userinfo-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyUserinfo companyUserinfo) {
		if(Constant.status.TEMPORARY.equals(companyUserinfo.getStatus()))
			companyUserinfoService.updateById(companyUserinfo);

		else{
			//不是暂存的编辑  都是新增暂存状态
			companyUserinfo.setStatus(Constant.status.PEND);//暂存
			companyUserinfo.setId(null);
			companyUserinfoService.save(companyUserinfo);

		}

		return Result.ok("编辑成功!");
	}

	 /**
	  *  用户信息申报
	  *
	  * @param companyUserinfo 用户信息
	  * @return
	  */
	 @AutoLog(value = "用户信息申报（新增或编辑时直接申报）")
	 @ApiOperation(value="用户信息申报", notes="新增或编辑时直接申报")
	 @PostMapping(value = "/editAndApply")
	 public Result<?> editAndApply(@RequestBody CompanyUserinfo companyUserinfo) {

	 	//新增申报记录
		 CompanyApply companyApply = new CompanyApply();
		String oldId = "";
	 	//申报   1新增申报
	 	if(StrUtil.isEmpty(companyUserinfo.getId()))
			companyUserinfoService.save(companyUserinfo);
	 	//编辑申报 2、编辑暂存数据
	 	else if(Constant.status.TEMPORARY.equals(companyUserinfo.getStatus())) {
			companyUserinfo.setStatus(Constant.status.PEND);//暂存
			companyUserinfoService.updateById(companyUserinfo);
		}else{
			//编辑申报 3、编辑正常数据
			oldId = companyUserinfo.getId();
	 		//不是暂存的编辑  都是新增暂存状态
			companyUserinfo.setStatus(Constant.status.PEND);//暂存
			companyUserinfo.setId(null);
			companyUserinfoService.save(companyUserinfo);

		}
		 //新增申报记录
		 companyApplyService.saveByBase(companyUserinfo.getCompanyId(),companyUserinfo.getId(),Constant.status.PEND,oldId,Constant.tables.USERINFO);
		 return Result.ok("申报成功!");

	 }
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "company_userinfo-通过id删除")
	@ApiOperation(value="company_userinfo-通过id删除", notes="company_userinfo-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyUserinfoService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "company_userinfo-批量删除")
	@ApiOperation(value="company_userinfo-批量删除", notes="company_userinfo-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		this.companyUserinfoService.update(new UpdateWrapper<CompanyUserinfo>().lambda().
//		in(CompanyUserinfo::getId,Arrays.asList(ids.split(",")).
//		set(CompanyUserinfo::getStatus))));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "company_userinfo-通过id查询")
	@ApiOperation(value="company_userinfo-通过id查询", notes="company_userinfo-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyUserinfo companyUserinfo = companyUserinfoService.getById(id);
		if(companyUserinfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyUserinfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyUserinfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyUserinfo companyUserinfo) {
        return super.exportXls(request, companyUserinfo, CompanyUserinfo.class, "company_userinfo");
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
        return super.importExcel(request, response, CompanyUserinfo.class);
    }

}
