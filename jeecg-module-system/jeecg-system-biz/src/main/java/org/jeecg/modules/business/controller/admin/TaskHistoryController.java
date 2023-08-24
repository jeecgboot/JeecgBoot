package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.TaskHistory;
import org.jeecg.modules.business.service.ITaskHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static org.jeecg.modules.business.entity.TaskHistory.TaskStatus.CANCELLED;

/**
 * @Description: task history
 * @Author: jeecg-boot
 * @Date:   2023-08-22
 * @Version: V1.0
 */
@Api(tags="taskHistory")
@RestController
@RequestMapping("/taskHistory")
@Slf4j
public class TaskHistoryController extends JeecgController<TaskHistory, ITaskHistoryService> {
	@Autowired
	private ITaskHistoryService taskHistoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param taskHistory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "task history-分页列表查询")
	@ApiOperation(value="task history-分页列表查询", notes="task history-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TaskHistory>> queryPageList(TaskHistory taskHistory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TaskHistory> queryWrapper = QueryGenerator.initQueryWrapper(taskHistory, req.getParameterMap());
		Page<TaskHistory> page = new Page<TaskHistory>(pageNo, pageSize);
		IPage<TaskHistory> pageList = taskHistoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param taskHistory
	 * @return
	 */
	@AutoLog(value = "task history-添加")
	@ApiOperation(value="task history-添加", notes="task history-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TaskHistory taskHistory) {
		taskHistoryService.save(taskHistory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param taskHistory
	 * @return
	 */
	@AutoLog(value = "task history-编辑")
	@ApiOperation(value="task history-编辑", notes="task history-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TaskHistory taskHistory) {
		taskHistoryService.updateById(taskHistory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "task history-通过id删除")
	@ApiOperation(value="task history-通过id删除", notes="task history-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		taskHistoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "task history-批量删除")
	@ApiOperation(value="task history-批量删除", notes="task history-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.taskHistoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "task history-通过id查询")
	@ApiOperation(value="task history-通过id查询", notes="task history-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TaskHistory> queryById(@RequestParam(name="id",required=true) String id) {
		TaskHistory taskHistory = taskHistoryService.getById(id);
		if(taskHistory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(taskHistory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param taskHistory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TaskHistory taskHistory) {
        return super.exportXls(request, taskHistory, TaskHistory.class, "task history");
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
        return super.importExcel(request, response, TaskHistory.class);
    }

	 /**
	  * Reset sets all running tasks to -1
	  * @param taskCode code of task
	  * @return
	  */
	 @GetMapping(value = "/reset")
	 public Result<?> resetTask(@RequestParam("task") String taskCode) {
		 List<TaskHistory> allRunningTasks = taskHistoryService.getAllRunningTasksByCode(taskCode);
		 for(TaskHistory taskHistory: allRunningTasks) {
			 taskHistory.setOngoing(CANCELLED.getCode());
			 taskHistoryService.updateById(taskHistory);
		 }
		 return Result.ok("Reset successful !");
	 }
}
