package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.util.TaskUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class SpringJob implements Job {

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			QuartzJob scheduleJob = (QuartzJob) context.getMergedJobDataMap().get("scheduleJob");
				log.error("任务执行 = [" + scheduleJob.getSpringId()+"."+scheduleJob.getSpringMethodName() + "]----------启动开始");
				TaskUtil.invokMethod(scheduleJob);
				log.error("任务执行 = [" + scheduleJob.getSpringId()+"."+scheduleJob.getSpringMethodName() + "]----------执行结束");


		}
}
