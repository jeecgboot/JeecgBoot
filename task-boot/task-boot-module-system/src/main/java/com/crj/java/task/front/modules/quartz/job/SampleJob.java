package com.crj.java.task.front.modules.quartz.job;

import com.crj.java.task.front.common.util.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;

/**
 * 示例不带参定时任务
 *
 * @Author Crj
 */
@Slf4j
public class SampleJob implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		log.info(String.format(" Task-Boot 普通定时任务 SampleJob !  时间:" + DateUtils.getTimestamp()));
	}
}
