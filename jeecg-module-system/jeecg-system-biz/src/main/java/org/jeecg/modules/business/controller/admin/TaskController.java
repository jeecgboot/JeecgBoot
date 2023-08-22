package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.Task;
import org.jeecg.modules.business.service.ITaskService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: tasks
 * @Author: jeecg-boot
 * @Date:   2023-08-22
 * @Version: V1.0
 */
@Api(tags="tasks")
@RestController
@RequestMapping("/business/task")
@Slf4j
public class TaskController extends JeecgController<Task, ITaskService> {
	@Autowired
	private ITaskService taskService;

	/**
	 * 分页列表查询
	 *
	 * @param task
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "tasks-分页列表查询")
	@ApiOperation(value="tasks-分页列表查询", notes="tasks-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Task>> queryPageList(Task task,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Task> queryWrapper = QueryGenerator.initQueryWrapper(task, req.getParameterMap());
		Page<Task> page = new Page<Task>(pageNo, pageSize);
		IPage<Task> pageList = taskService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param task
	 * @return
	 */
	@AutoLog(value = "tasks-添加")
	@ApiOperation(value="tasks-添加", notes="tasks-添加")
	@RequiresPermissions("business:task:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Task task) {
		taskService.save(task);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param task
	 * @return
	 */
	@AutoLog(value = "tasks-编辑")
	@ApiOperation(value="tasks-编辑", notes="tasks-编辑")
	@RequiresPermissions("business:task:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Task task) {
		taskService.updateById(task);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "tasks-通过id删除")
	@ApiOperation(value="tasks-通过id删除", notes="tasks-通过id删除")
	@RequiresPermissions("business:task:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		taskService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "tasks-批量删除")
	@ApiOperation(value="tasks-批量删除", notes="tasks-批量删除")
	@RequiresPermissions("business:task:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.taskService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "tasks-通过id查询")
	@ApiOperation(value="tasks-通过id查询", notes="tasks-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Task> queryById(@RequestParam(name="id",required=true) String id) {
		Task task = taskService.getById(id);
		if(task==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(task);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param task
    */
    @RequiresPermissions("business:task:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Task task) {
        return super.exportXls(request, task, Task.class, "tasks");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:task:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Task.class);
    }

}
