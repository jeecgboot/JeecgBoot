package org.jeecg.modules.business.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.CompanyBaseinfo;
import org.jeecg.modules.business.entity.CompanySysuser;
import org.jeecg.modules.business.service.ICompanyBaseinfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.ICompanySysuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: company_baseinfo
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
@Api(tags="company_baseinfo")
@RestController
@RequestMapping("/company/companyBaseinfo")
@Slf4j
public class CompanyBaseinfoController extends JeecgController<CompanyBaseinfo, ICompanyBaseinfoService> {
	@Autowired
	private ICompanyBaseinfoService companyBaseinfoService;
	 @Autowired
	 private ICompanySysuserService companySysuserService;

	/**
	 * 分页列表查询
	 *
	 * @param companyBaseinfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "company_baseinfo-分页列表查询")
	@ApiOperation(value="company_baseinfo-分页列表查询", notes="company_baseinfo-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyBaseinfo companyBaseinfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyBaseinfo> queryWrapper = QueryGenerator.initQueryWrapper(companyBaseinfo, req.getParameterMap());
		Page<CompanyBaseinfo> page = new Page<CompanyBaseinfo>(pageNo, pageSize);
		IPage<CompanyBaseinfo> pageList = companyBaseinfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	 @AutoLog(value = "company_baseinfo-通过用户id-分页列表查询")
	 @ApiOperation(value="company_baseinfo-通过用户id-分页列表查询", notes="company_baseinfo-通过用户id-分页列表查询")
	 @GetMapping(value = "/listByUserId/{userId}")
	 public Result<?> queryByUserIdRestful(@PathVariable  String userId,CompanyBaseinfo companyBaseinfo,
										   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										   HttpServletRequest req) {
		 List<String> companyIds = new ArrayList<>();
		 if (queryCompanyIds(userId, companyIds)) return Result.error("未找到对应数据");
		 Map<String, String[]> parameterMap = new HashMap(req.getParameterMap());
		 parameterMap.put("companyId_MultiString",new String[]{String.join(",", companyIds)});
		 QueryWrapper<CompanyBaseinfo> queryWrapper = QueryGenerator.initQueryWrapper(companyBaseinfo, parameterMap);
		 Page<CompanyBaseinfo> page = new Page<>(pageNo, pageSize);
		 IPage<CompanyBaseinfo> pageList = companyBaseinfoService.page(page, queryWrapper);
		 return Result.ok(pageList);

	 }
	/**
	 *   添加
	 *
	 * @param companyBaseinfo
	 * @return
	 */
	@AutoLog(value = "company_baseinfo-添加")
	@ApiOperation(value="company_baseinfo-添加", notes="company_baseinfo-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyBaseinfo companyBaseinfo) {
		companyBaseinfoService.save(companyBaseinfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyBaseinfo
	 * @return
	 */
	@AutoLog(value = "company_baseinfo-编辑")
	@ApiOperation(value="company_baseinfo-编辑", notes="company_baseinfo-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyBaseinfo companyBaseinfo) {
		companyBaseinfoService.updateById(companyBaseinfo);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "company_baseinfo-通过id删除")
	@ApiOperation(value="company_baseinfo-通过id删除", notes="company_baseinfo-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyBaseinfoService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "company_baseinfo-批量删除")
	@ApiOperation(value="company_baseinfo-批量删除", notes="company_baseinfo-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyBaseinfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "company_baseinfo-通过id查询")
	@ApiOperation(value="company_baseinfo-通过id查询", notes="company_baseinfo-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyBaseinfo companyBaseinfo = companyBaseinfoService.getById(id);
		if(companyBaseinfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyBaseinfo);
	}
	/* *//**
	  * 通过userid查询
	  *
	  * @param userId
	  * @return
	  *//*
	 @AutoLog(value = "company_baseinfo-通过用户id查询")
	 @ApiOperation(value="company_baseinfo-通过用户id查询", notes="company_baseinfo-通过用户id查询")
	 @GetMapping(value = "/queryByUserId")
	 public Result<?> queryByUserId(@RequestParam(name="userId",required=true) String userId) {
		 return resultByUserId(userId);
	 }*/

/*
	 private Result<?> resultByUserId(@PathVariable String userId) {
		 List<String> companyIds = new ArrayList<>();
		 if (queryCompanyIds(userId, companyIds)) return Result.error("未找到对应数据");

		 //根据企业人员信息 查找企业数据
		 List<CompanyBaseinfo> companyBaseinfos = companyBaseinfoService.list(new QueryWrapper<CompanyBaseinfo>().lambda().eq(CompanyBaseinfo::getStatus, "NORAML")
				 .in(CompanyBaseinfo::getCompanyId, companyIds));

		 List<String> companyNameList = new ArrayList<>();
		 companyBaseinfos.forEach(companySysuser -> {
			 companyNameList.add(companySysuser.getShortName());
		 });
		 String companyNames = String.join(",", companyNameList);
		 return Result.ok(companyNames);
	 }*/

	 private boolean queryCompanyIds(@PathVariable String userId, List<String> companyIds) {
		 List<CompanySysuser> companySysusers = companySysuserService.list(userId);

		 if (companySysusers.isEmpty())
			 return true;

		 companySysusers.forEach(companySysuser -> {
			 companyIds.add(companySysuser.getCompanyId());
		 });
		 return false;
	 }

	 /**
	  * 通过userid查询
	  *
	  * @param token
	  * @return
	  */
	 @AutoLog(value = "company_baseinfo-通过用户id查询")
	 @ApiOperation(value="company_baseinfo-通过用户id查询", notes="company_baseinfo-通过用户id查询")
	 @GetMapping(value = "/queryByToken")
	 public Result<?> queryByToken(@RequestParam(name = "token", required = true) String token) {

		 if (oConvertUtils.isEmpty(token)) {
			 return Result.error("TOKEN不允许为空！");
		 }
		 log.info(" ------ 通过令牌获取用户拥有的访问菜单 ---- TOKEN ------ " + token);
		 String username = JwtUtil.getUsername(token);

	 	 List<CompanySysuser>  companySysusers = companySysuserService.list(
	 	 		new QueryWrapper<CompanySysuser>().lambda().eq(CompanySysuser :: getSysUsername,username));

		 if(companySysusers.isEmpty())
			 return Result.error("未找到对应数据");
		 List<String> companyIds = new ArrayList<>();
		 companySysusers.forEach(companySysuser -> {
			 companyIds.add(companySysuser.getCompanyId());
		 });

		 //根据企业人员信息 查找企业数据
		 List<CompanyBaseinfo> companyBaseinfos = companyBaseinfoService.list(new QueryWrapper<CompanyBaseinfo>().lambda().eq(CompanyBaseinfo :: getStatus,"NORAML")
				 .in(CompanyBaseinfo :: getCompanyId, companyIds));

		 List<String> companyNameList = new ArrayList<>();
		 companyBaseinfos.forEach(companySysuser -> {
			 companyNameList.add(companySysuser.getShortName());
		 });
		 String companyNames = String.join(",", companyNameList);
		 return Result.ok(companyNames);
	 }
    /**
    * 导出excel
    *
    * @param request
    * @param companyBaseinfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyBaseinfo companyBaseinfo) {
        return super.exportXls(request, companyBaseinfo, CompanyBaseinfo.class, "company_baseinfo");
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
        return super.importExcel(request, response, CompanyBaseinfo.class);
    }

}
