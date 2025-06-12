package org.jeecg.modules.demo.cursorstudent.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.demo.cursorstudent.entity.Cursorstudent;
import org.jeecg.modules.demo.cursorstudent.service.ICursorstudentService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 课程管理模块
 * @Author: jeecg-boot
 * @Date:   2025-06-11
 * @Version: V1.0
 */
@Tag(name="课程管理模块")
@RestController
@RequestMapping("/cursorstudent/cursorstudent")
@Slf4j
public class CursorstudentController extends JeecgController<Cursorstudent, ICursorstudentService> {
	@Autowired
	private ICursorstudentService cursorstudentService;
	
	/**
	 * 分页列表查询
	 *
	 * @param cursorstudent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "课程管理模块-分页列表查询")
	@Operation(summary="课程管理模块-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Cursorstudent>> queryPageList(Cursorstudent cursorstudent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<Cursorstudent> queryWrapper = QueryGenerator.initQueryWrapper(cursorstudent, req.getParameterMap());
		Page<Cursorstudent> page = new Page<Cursorstudent>(pageNo, pageSize);
		IPage<Cursorstudent> pageList = cursorstudentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param cursorstudent
	 * @return
	 */
	@AutoLog(value = "课程管理模块-添加")
	@Operation(summary="课程管理模块-添加")
	@RequiresPermissions("cursorstudent:coursemanagementmodule:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Cursorstudent cursorstudent) {
		cursorstudentService.save(cursorstudent);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param cursorstudent
	 * @return
	 */
	@AutoLog(value = "课程管理模块-编辑")
	@Operation(summary="课程管理模块-编辑")
	@RequiresPermissions("cursorstudent:coursemanagementmodule:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Cursorstudent cursorstudent) {
		cursorstudentService.updateById(cursorstudent);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "课程管理模块-通过id删除")
	@Operation(summary="课程管理模块-通过id删除")
	@RequiresPermissions("cursorstudent:coursemanagementmodule:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		cursorstudentService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "课程管理模块-批量删除")
	@Operation(summary="课程管理模块-批量删除")
	@RequiresPermissions("cursorstudent:coursemanagementmodule:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.cursorstudentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "课程管理模块-通过id查询")
	@Operation(summary="课程管理模块-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Cursorstudent> queryById(@RequestParam(name="id",required=true) String id) {
		Cursorstudent cursorstudent = cursorstudentService.getById(id);
		if(cursorstudent==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(cursorstudent);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param cursorstudent
    */
    @RequiresPermissions("cursorstudent:coursemanagementmodule:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Cursorstudent cursorstudent) {
        return super.exportXls(request, cursorstudent, Cursorstudent.class, "课程管理模块");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("cursorstudent:coursemanagementmodule:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Cursorstudent.class);
    }

}
