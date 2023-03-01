package com.xxl.job.admin.core.model;

import java.util.Date;

public class XxlJobLogReport {

	private int id;

	private Date triggerDay;

	private int runningCount;

	private int sucCount;

	private int failCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTriggerDay() {
		return triggerDay;
	}

	public void setTriggerDay(Date triggerDay) {
		this.triggerDay = triggerDay;
	}

	public int getRunningCount() {
		return runningCount;
	}

	public void setRunningCount(int runningCount) {
		this.runningCount = runningCount;
	}

	public int getSucCount() {
		return sucCount;
	}

	public void setSucCount(int sucCount) {
		this.sucCount = sucCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

}
