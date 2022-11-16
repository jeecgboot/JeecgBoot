package org.jeecg.modules.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.mapper.QuartzJobMapper;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date: 2019-04-28
 * @Version: V1.1
 */
@Slf4j
@Service
public class QuartzJobServiceImpl extends ServiceImpl<QuartzJobMapper, QuartzJob> implements IQuartzJobService {
	@Autowired
	private QuartzJobMapper quartzJobMapper;
	@Autowired
	private Scheduler scheduler;

	/**
	 * 立即执行的任务分组
	 */
	private static final String JOB_TEST_GROUP = "test_group";

	@Override
	public List<QuartzJob> findByJobClassName(String jobClassName) {
		return quartzJobMapper.findByJobClassName(jobClassName);
	}

	/**
	 * 保存&启动定时任务
	 */
	@Override
	@Transactional(rollbackFor = JeecgBootException.class)
	public boolean saveAndScheduleJob(QuartzJob quartzJob) {
		// DB设置修改
		quartzJob.setDelFlag(CommonConstant.DEL_FLAG_0);
		boolean success = this.save(quartzJob);
		if (success) {
			if (CommonConstant.STATUS_NORMAL.equals(quartzJob.getStatus())) {
				// 定时器添加
				this.schedulerAdd(quartzJob.getId(), quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
			}
		}
		return success;
	}

	/**
	 * 恢复定时任务
	 */
	@Override
	@Transactional(rollbackFor = JeecgBootException.class)
	public boolean resumeJob(QuartzJob quartzJob) {
		quartzJob.setStatus(CommonConstant.STATUS_NORMAL);
		boolean update = this.updateById(quartzJob);
		if (!update){
			return false;
		}
		if (checkExists(quartzJob)) {
			try {
				scheduler.resumeJob(JobKey.jobKey(quartzJob.getId()));
			} catch (SchedulerException e) {
				throw new JeecgBootException(e.getMessage(), e);
			}
		}else {
			schedulerAdd(quartzJob.getId(), quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
		}
		return true;
	}

	private boolean checkExists(QuartzJob quartzJob) {
		try {
			return scheduler.checkExists(JobKey.jobKey(quartzJob.getId()));
		} catch (SchedulerException e) {
			log.warn(e.getMessage(), e);
			return false;
		}
	}



	/**
	 * 编辑&启停定时任务
	 * @throws SchedulerException 
	 */
	@Override
	@Transactional(rollbackFor = JeecgBootException.class)
	public boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException {
		boolean update = this.updateById(quartzJob);
		if (!update) {
			return false;
		}
		if (CommonConstant.STATUS_NORMAL.equals(quartzJob.getStatus())) {
			// 防止重复添加Job
			if(schedulerDelete(quartzJob.getId())) {
				schedulerAdd(quartzJob.getId(), quartzJob.getJobClassName().trim(), quartzJob.getCronExpression().trim(), quartzJob.getParameter());
			}
		}else{
			scheduler.pauseJob(JobKey.jobKey(quartzJob.getId()));
		}
		return true;
	}

	/**
	 * 删除&停止删除定时任务
	 */
	@Override
	@Transactional(rollbackFor = JeecgBootException.class)
	public boolean deleteAndStopJob(QuartzJob job) {
		boolean ok = this.removeById(job.getId());
		if (!ok){
			return false;
		}
		boolean schedulerDel = schedulerDelete(job.getId());
		if (!schedulerDel) {
			throw new JeecgBootException("schedulerDelete failed");
		}
		return true;
	}

	@Override
	public void execute(QuartzJob quartzJob) throws Exception {
		String jobName = quartzJob.getJobClassName().trim();
		Date startDate = new Date();
		String ymd = DateUtils.date2Str(startDate,DateUtils.yyyymmddhhmmss.get());
		String identity =  jobName + ymd;
		//3秒后执行 只执行一次
		// update-begin--author:sunjianlei ---- date:20210511--- for：定时任务立即执行，延迟3秒改成0.1秒-------
		startDate.setTime(startDate.getTime() + 100L);
		// update-end--author:sunjianlei ---- date:20210511--- for：定时任务立即执行，延迟3秒改成0.1秒-------
		// 定义一个Trigger
		SimpleTrigger trigger = (SimpleTrigger)TriggerBuilder.newTrigger()
				.withIdentity(identity, JOB_TEST_GROUP)
				.startAt(startDate)
				.build();
		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(getClass(jobName).getClass()).withIdentity(identity).usingJobData("parameter", quartzJob.getParameter()).build();
		// 将trigger和 jobDetail 加入这个调度
		scheduler.scheduleJob(jobDetail, trigger);
		// 启动scheduler
		scheduler.start();
	}

	@Override
	@Transactional(rollbackFor = JeecgBootException.class)
	public void pause(QuartzJob quartzJob){
		schedulerDelete(quartzJob.getId());
		quartzJob.setStatus(CommonConstant.STATUS_DISABLE);
		this.updateById(quartzJob);
	}

	/**
	 * 添加定时任务
	 *
	 * @param jobClassName
	 * @param cronExpression
	 * @param parameter
	 */
	private void schedulerAdd(String id, String jobClassName, String cronExpression, String parameter) {
		try {
			// 启动调度器
			scheduler.start();

			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(id).usingJobData("parameter", parameter).build();

			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(id).withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			throw new JeecgBootException("创建定时任务失败", e);
		} catch (RuntimeException e) {
			throw new JeecgBootException(e.getMessage(), e);
		}catch (Exception e) {
			throw new JeecgBootException("后台找不到该类名：" + jobClassName, e);
		}
	}

	/**
	 * 删除定时任务
	 * 
	 * @param id
	 */
	private boolean schedulerDelete(String id) {
		try {
			return scheduler.deleteJob(JobKey.jobKey(id));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new JeecgBootException("删除定时任务失败");
		}
	}

	private static Job getClass(String classname) throws Exception {
		Class<?> class1 = Class.forName(classname);
		return (Job) class1.newInstance();
	}

}
