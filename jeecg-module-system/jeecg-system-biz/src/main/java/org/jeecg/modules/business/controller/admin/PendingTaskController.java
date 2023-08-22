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
import org.jeecg.modules.business.entity.PendingTask;
import org.jeecg.modules.business.service.IPendingTaskService;

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
 * @Description: to know if a task in launched
 * @Author: jeecg-boot
 * @Date:   2023-08-17
 * @Version: V1.0
 */
@Api(tags="pendingTask")
@RestController
@RequestMapping("/pendingTask")
@Slf4j
public class PendingTaskController extends JeecgController<PendingTask, IPendingTaskService> {
	@Autowired
	private IPendingTaskService pendingTaskService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pendingTask
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "to know if a task in launched-分页列表查询")
	@ApiOperation(value="to know if a task in launched-分页列表查询", notes="to know if a task in launched-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<PendingTask>> queryPageList(PendingTask pendingTask,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<PendingTask> queryWrapper = QueryGenerator.initQueryWrapper(pendingTask, req.getParameterMap());
		Page<PendingTask> page = new Page<PendingTask>(pageNo, pageSize);
		IPage<PendingTask> pageList = pendingTaskService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param pendingTask
	 * @return
	 */
	@AutoLog(value = "to know if a task in launched-添加")
	@ApiOperation(value="to know if a task in launched-添加", notes="to know if a task in launched-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody PendingTask pendingTask) {
		pendingTaskService.save(pendingTask);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param pendingTask
	 * @return
	 */
	@AutoLog(value = "to know if a task in launched-编辑")
	@ApiOperation(value="to know if a task in launched-编辑", notes="to know if a task in launched-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody PendingTask pendingTask) {
		pendingTaskService.updateById(pendingTask);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "to know if a task in launched-通过id删除")
	@ApiOperation(value="to know if a task in launched-通过id删除", notes="to know if a task in launched-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		pendingTaskService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "to know if a task in launched-批量删除")
	@ApiOperation(value="to know if a task in launched-批量删除", notes="to know if a task in launched-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pendingTaskService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "to know if a task in launched-通过id查询")
	@ApiOperation(value="to know if a task in launched-通过id查询", notes="to know if a task in launched-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<PendingTask> queryById(@RequestParam(name="id",required=true) String id) {
		PendingTask pendingTask = pendingTaskService.getById(id);
		if(pendingTask==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(pendingTask);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param pendingTask
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, PendingTask pendingTask) {
        return super.exportXls(request, pendingTask, PendingTask.class, "to know if a task in launched");
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
        return super.importExcel(request, response, PendingTask.class);
    }

	@PostMapping(value = "/reset")
	 public Result<?> resetTask(@RequestBody String taskCode) {
		pendingTaskService.setStatus(0, "BI");
		return Result.ok("Reset successful !");
	}
}
