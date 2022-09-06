package org.jeecg.modules.quartz.service;

import java.util.List;

import org.jeecg.modules.quartz.entity.QuartzJob;
import org.quartz.SchedulerException;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date: 2019-04-28
 * @Version: V1.1
 */
public interface IQuartzJobService extends IService<QuartzJob> {

    /**
     * 通过类名寻找定时任务
     * @param jobClassName 类名
     * @return List<QuartzJob>
     */
	List<QuartzJob> findByJobClassName(String jobClassName);

    /**
     * 保存定时任务
     * @param quartzJob
     * @return boolean
     */
	boolean saveAndScheduleJob(QuartzJob quartzJob);

    /**
     * 编辑定时任务
     * @param quartzJob
     * @return boolean
     * @throws SchedulerException
     */
	boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;

    /**
     * 删除定时任务
     * @param quartzJob
     * @return boolean
     */
	boolean deleteAndStopJob(QuartzJob quartzJob);

    /**
     * 恢复定时任务
     * @param quartzJob
     * @return
     */
	boolean resumeJob(QuartzJob quartzJob);

	/**
	 * 执行定时任务
	 * @param quartzJob
     * @throws Exception
	 */
	void execute(QuartzJob quartzJob) throws Exception;

	/**
	 * 暂停任务
	 * @param quartzJob
	 * @throws SchedulerException
	 */
	void pause(QuartzJob quartzJob);
}
