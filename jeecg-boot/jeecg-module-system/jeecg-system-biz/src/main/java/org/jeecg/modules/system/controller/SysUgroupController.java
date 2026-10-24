package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.system.entity.SysUgroup;
import org.jeecg.modules.system.service.ISysUgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Date;
 /**
 * @Description: 用户组表
 * @Author: jeecg-boot
 * @Date:   2026-02-27
 * @Version: V1.0
 */
@Tag(name="用户组表")
@RestController
@RequestMapping("/sys/ugroup")
@Slf4j
public class SysUgroupController extends JeecgController<SysUgroup, ISysUgroupService> {
	@Autowired
	private ISysUgroupService sysUgroupService;


	/**
	 * 分页列表查询
	 *
	 * @param sysUgroup
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "用户组表-分页列表查询")
	@Operation(summary="用户组表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysUgroup>> queryPageList(SysUgroup sysUgroup,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {


        QueryWrapper<SysUgroup> queryWrapper = QueryGenerator.initQueryWrapper(sysUgroup, req.getParameterMap());
		//update-begin---author:scott ---date:20261014  for：多租户模式下只查当前租户的用户组，防止跨租户读取-----------
		if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
			queryWrapper.eq("tenant_id", oConvertUtils.getInt(TokenUtils.getTenantIdByRequest(req), 0));
		}
		//update-end---author:scott ---date:20261014  for：多租户模式下只查当前租户的用户组，防止跨租户读取-----------
		Page<SysUgroup> page = new Page<SysUgroup>(pageNo, pageSize);
		IPage<SysUgroup> pageList = sysUgroupService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysUgroup
	 * @return
	 */
	@AutoLog(value = "用户组表-添加")
	@Operation(summary="用户组表-添加")
	@RequiresPermissions("system:sys_ugroup:add")
	@PostMapping(value = "/add")
	public Result<SysUgroup> add(@RequestBody SysUgroup sysUgroup) {
		Result<SysUgroup> result = new Result<SysUgroup>();
		try {
			sysUgroup.setCreateTime(new Date());
			sysUgroupService.save(sysUgroup);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	 *  编辑
	 *
	 * @param sysUgroup
	 * @return
	 */
	@AutoLog(value = "用户组表-编辑")
	@Operation(summary="用户组表-编辑")
	@RequiresPermissions("system:sys_ugroup:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SysUgroup sysUgroup) {
		sysUgroupService.updateById(sysUgroup);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户组表-通过id删除")
	@Operation(summary="用户组表-通过id删除")
	@RequiresPermissions("system:sys_ugroup:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sysUgroupService.deleteById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户组表-批量删除")
	@Operation(summary="用户组表-批量删除")
	@RequiresPermissions("system:sys_ugroup:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysUgroupService.deleteByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "用户组表-通过id查询")
	@Operation(summary="用户组表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysUgroup> queryById(@RequestParam(name="id",required=true) String id, HttpServletRequest req) {
		SysUgroup sysUgroup = sysUgroupService.getById(id);
		if(sysUgroup==null) {
			return Result.error("未找到对应数据");
		}
		//update-begin---author:scott ---date:20261014  for：多租户模式下只能查看当前租户的用户组，防止按id跨租户读取-----------
		if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL && !java.util.Objects.equals(oConvertUtils.getInt(TokenUtils.getTenantIdByRequest(req), 0), sysUgroup.getTenantId())) {
			return Result.error("未找到对应数据");
		}
		//update-end---author:scott ---date:20261014  for：多租户模式下只能查看当前租户的用户组，防止按id跨租户读取-----------
		return Result.OK(sysUgroup);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysUgroup
    */
    @RequiresPermissions("system:sys_ugroup:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysUgroup sysUgroup) {
        return super.exportXls(request, sysUgroup, SysUgroup.class, "用户组表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("system:sys_ugroup:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysUgroup.class);
    }

}
