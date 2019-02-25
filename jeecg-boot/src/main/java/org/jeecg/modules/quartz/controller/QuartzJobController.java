package org.jeecg.modules.quartz.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: Controller
 * @Description: 定时任务在线管理 
 * @author： jeecg-boot 
 * @date：     2019-01-02 
 * @version：V1.0
 */
@RestController
@RequestMapping("/sys/quartzJob")
@Slf4j
public class QuartzJobController {
	@Autowired
	private IQuartzJobService quartzJobService;
	@Autowired
	private Scheduler scheduler;

	/**
	 * 分页列表查询
	 * 
	 * @param quartzJob
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<QuartzJob>> queryPageList(QuartzJob quartzJob, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<QuartzJob>> result = new Result<IPage<QuartzJob>>();
		QueryWrapper<QuartzJob> queryWrapper = new QueryWrapper<QuartzJob>(quartzJob);
		Page<QuartzJob> page = new Page<QuartzJob>(pageNo, pageSize);
		// 排序逻辑 处理
		String column = req.getParameter("column");
		String order = req.getParameter("order");
		if (oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
			if ("asc".equals(order)) {
				queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
			} else {
				queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
			}
		}
		IPage<QuartzJob> pageList = quartzJobService.page(page, queryWrapper);
//		log.info("查询当前页：" + pageList.getCurrent());
//		log.info("查询当前页数量：" + pageList.getSize());
//		log.info("查询结果数量：" + pageList.getRecords().size());
//		log.info("数据总数：" + pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * 添加定时任务
	 * 
	 * @param quartzJob
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<?> add(@RequestBody QuartzJob quartzJob) {
		Result<QuartzJob> result = new Result<QuartzJob>();

		List<QuartzJob> list = quartzJobService.findByJobClassName(quartzJob.getJobClassName());
		if (list != null && list.size() > 0) {
			return Result.error("该定时任务类名已存在");
		}
		try {
			schedulerAdd(quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
			//
			quartzJob.setDelFlag(CommonConstant.DEL_FLAG_0);
			quartzJob.setStatus(CommonConstant.STATUS_NORMAL);
			quartzJobService.save(quartzJob);
			result.success("创建定时任务成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("创建定时任务失败，"+e.getMessage());
		}
		return result;
	}

	/**
	 * 更新定时任务
	 * 
	 * @param quartzJob
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public Result<QuartzJob> eidt(@RequestBody QuartzJob quartzJob) {
		Result<QuartzJob> result = new Result<QuartzJob>();
		QuartzJob quartzJobEntity = quartzJobService.getById(quartzJob.getId());
		if (quartzJobEntity == null) {
			result.error500("未找到对应实体");
		} else {
			schedulerDelete(quartzJob.getJobClassName().trim());
			if(CommonConstant.STATUS_NORMAL == quartzJob.getStatus()) {
				schedulerAdd(quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
			}
			boolean ok = quartzJobService.updateById(quartzJob);
			if (ok) {
				result.success("更新定时任务成功!");
			}
		}
		return result;
	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<QuartzJob> delete(@RequestParam(name = "id", required = true) String id) {
		Result<QuartzJob> result = new Result<QuartzJob>();
		QuartzJob quartzJob = quartzJobService.getById(id);
		if (quartzJob == null) {
			result.error500("未找到对应实体");
		} else {
			QuartzJob job = quartzJobService.getById(id);
			schedulerDelete(job.getJobClassName().trim());
			boolean ok = quartzJobService.removeById(id);
			if (ok) {
				result.success("删除成功!");
			}
		}

		return result;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<QuartzJob> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<QuartzJob> result = new Result<QuartzJob>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		} else {
			for (String id : Arrays.asList(ids.split(","))) {
				QuartzJob job = quartzJobService.getById(id);
				schedulerDelete(job.getJobClassName().trim());
				quartzJobService.removeById(id);
			}
			result.success("删除定时任务成功!");
		}
		return result;
	}

	/**
	 * 暂停定时任务
	 * @param job
	 * @return
	 */
	@RequestMapping(value = "/pause", method = RequestMethod.POST)
	@ApiOperation(value = "暂停定时任务")
	public Result<Object> pauseJob(@RequestBody QuartzJob job) {

		try {
			scheduler.pauseJob(JobKey.jobKey(job.getJobClassName().trim()));
		} catch (SchedulerException e) {
			throw new JeecgBootException("暂停定时任务失败");
		}
		job.setStatus(CommonConstant.STATUS_DISABLE);
		quartzJobService.updateById(job);
		return Result.ok("暂停定时任务成功");
	}

	/**
	 * 启动定时任务
	 * @param job
	 * @return
	 */
	@RequestMapping(value = "/resume", method = RequestMethod.POST)
	@ApiOperation(value = "恢复定时任务")
	public Result<Object> resumeJob(@RequestBody QuartzJob job) {

		try {
			scheduler.resumeJob(JobKey.jobKey(job.getJobClassName().trim()));
		} catch (SchedulerException e) {
			throw new JeecgBootException("恢复定时任务失败");
		}
		job.setStatus(CommonConstant.STATUS_NORMAL);
		quartzJobService.updateById(job);
		return Result.ok("恢复定时任务成功");
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public Result<QuartzJob> queryById(@RequestParam(name = "id", required = true) String id) {
		Result<QuartzJob> result = new Result<QuartzJob>();
		QuartzJob quartzJob = quartzJobService.getById(id);
		if (quartzJob == null) {
			result.error500("未找到对应实体");
		} else {
			result.setResult(quartzJob);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	 * 添加定时任务
	 * 
	 * @param jobClassName
	 * @param cronExpression
	 * @param parameter
	 */
	private void schedulerAdd(String jobClassName, String cronExpression, String parameter) {

		try {
			// 启动调度器
			scheduler.start();

			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName).usingJobData("parameter", parameter).build();

			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName).withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error(e.toString());
			throw new JeecgBootException("创建定时任务失败");
		} catch (Exception e) {
			throw new JeecgBootException("后台找不到该类名任务");
		}
	}

	/**
	 * 删除定时任务
	 * 
	 * @param jobClassName
	 */
	private void schedulerDelete(String jobClassName) {

		try {
			scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName));
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName));
			scheduler.deleteJob(JobKey.jobKey(jobClassName));
		} catch (Exception e) {
			throw new JeecgBootException("删除定时任务失败");
		}
	}

	private static Job getClass(String classname) throws Exception {
		Class<?> class1 = Class.forName(classname);
		return (Job) class1.newInstance();
	}

}
