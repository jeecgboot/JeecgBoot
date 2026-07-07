package com.xxl.job.admin.business.scheduler.thread;

import com.xxl.job.admin.business.model.XxlJobInfo;
import com.xxl.job.admin.business.model.XxlJobLog;
import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.job.admin.business.scheduler.trigger.TriggerTypeEnum;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.tool.concurrent.CyclicThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * job fail-monitor helper
 *
 * @author xuxueli 2015-9-1 18:05:56
 */
public class JobFailAlarmMonitorHelper {
	private static Logger logger = LoggerFactory.getLogger(JobFailAlarmMonitorHelper.class);
	

	// ---------------------- monitor ----------------------

	/**
	 * monitor thread
	 */
	private CyclicThread monitorThread;

	/**
	 * start
	 */
	public void start(){

		monitorThread = new CyclicThread("JobFailAlarmMonitorHelper#monitorThread", true, new Runnable() {
			@Override
			public void run() {
				List<Long> failLogIds = XxlJobAdminBootstrap.getInstance().getXxlJobLogMapper().findFailJobLogIds(1000);
				if (failLogIds!=null && !failLogIds.isEmpty()) {
					for (long failLogId: failLogIds) {

						// lock log
						int lockRet = XxlJobAdminBootstrap.getInstance().getXxlJobLogMapper().updateAlarmStatus(failLogId, 0, -1);
						if (lockRet < 1) {
							continue;
						}
						XxlJobLog log = XxlJobAdminBootstrap.getInstance().getXxlJobLogMapper().load(failLogId);
						XxlJobInfo info = XxlJobAdminBootstrap.getInstance().getXxlJobInfoMapper().loadById(log.getJobId());

						// 1、fail retry monitor
						if (log.getExecutorFailRetryCount() > 0) {
							XxlJobAdminBootstrap.getInstance().getJobTriggerPoolHelper().trigger(log.getJobId(), TriggerTypeEnum.RETRY, (log.getExecutorFailRetryCount()-1), log.getExecutorShardingParam(), log.getExecutorParam(), null);
							String retryMsg = "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>"+ I18nUtil.getString("jobconf_trigger_type_retry") +"<<<<<<<<<<< </span><br>";
							log.setTriggerMsg(log.getTriggerMsg() + retryMsg);
							XxlJobAdminBootstrap.getInstance().getXxlJobLogMapper().updateTriggerInfo(log);
						}

						// 2、fail alarm monitor
						int newAlarmStatus = 0;		// 告警状态：0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败
						if (info != null) {
							boolean alarmResult = XxlJobAdminBootstrap.getInstance().getJobAlarmer().alarm(info, log);
							newAlarmStatus = alarmResult?2:3;
						} else {
							newAlarmStatus = 1;
						}

						XxlJobAdminBootstrap.getInstance().getXxlJobLogMapper().updateAlarmStatus(failLogId, -1, newAlarmStatus);
					}
				}
			}
		}, 10 * 1000L, true);
		monitorThread.start();
	}

	/**
	 * stop
	 */
	public void stop(){
		monitorThread.stop();
	}

}
