package com.xxl.job.admin.core.model;

import java.util.Date;

/**
 * xxl-job log for glue, used to track job code process
 * @author xuxueli 2016-5-19 17:57:46
 */
public class XxlJobLogGlue {
	
	private int id;
	private int jobId;				// 任务主键ID
	private String glueType;		// GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
	private String glueSource;
	private String glueRemark;
	private Date addTime;
	private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public String getGlueType() {
		return glueType;
	}

	public void setGlueType(String glueType) {
		this.glueType = glueType;
	}

	public String getGlueSource() {
		return glueSource;
	}

	public void setGlueSource(String glueSource) {
		this.glueSource = glueSource;
	}

	public String getGlueRemark() {
		return glueRemark;
	}

	public void setGlueRemark(String glueRemark) {
		this.glueRemark = glueRemark;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
