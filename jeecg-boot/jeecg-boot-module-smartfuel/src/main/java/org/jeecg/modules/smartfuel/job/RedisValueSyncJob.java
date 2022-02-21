package org.jeecg.modules.smartfuel.job;

import cn.hutool.core.date.DateTime;
import lombok.var;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.smartfuel.util.InfluxDBUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 * 示例带参定时任务
 * 
 * @Author Scott
 */
@Slf4j
public class RedisValueSyncJob implements Job {

	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;
	@Autowired
	private InfluxDBUtil influx;

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		var list= new ArrayList<String>() {
			{
				this.add("BF_IN[0]");
				this.add("BF_IN[33]");
				this.add("BF_IN[35]");
				this.add("BF_IN[36]");
			}
		};
		var res = influx.QueryInterval(LocalDateTime.of(2022, 2, 17, 13, 00, 00)
				,LocalDateTime.of(2022, 2, 17, 14, 00, 00),list,"10s","1");

		log.info(" Job Execution key："+jobExecutionContext.getJobDetail().getKey());
		log.info( String.format("welcome %s! Jeecg-Boot 带参数定时任务 SampleParamJob !   时间:" + DateUtils.now(), this.parameter));
	}
}
