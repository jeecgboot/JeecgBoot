package com.xxl.job.admin.business.model.dto;

import com.xxl.job.admin.business.model.XxlJobLog;
import com.xxl.tool.core.DateTool;

public class XxlJobLogDTO {

    private long id;

    // job info
    private int jobGroup;
    private int jobId;

    // execute info
    private String executorAddress;
    private String executorHandler;
    private String executorParam;
    private String executorShardingParam;
    private int executorFailRetryCount;

    // trigger info
    private String triggerTime;
    private int triggerCode;
    private String triggerMsg;

    // handle info
    private String handleTime;
    private int handleCode;
    private String handleMsg;

    // alarm info
    private int alarmStatus;

    public XxlJobLogDTO(XxlJobLog xxlJobLog) {
        this.id = xxlJobLog.getId();
        this.jobGroup = xxlJobLog.getJobGroup();
        this.jobId = xxlJobLog.getJobId();
        this.executorAddress = xxlJobLog.getExecutorAddress();
        this.executorHandler = xxlJobLog.getExecutorHandler();
        this.executorParam = xxlJobLog.getExecutorParam();
        this.executorShardingParam = xxlJobLog.getExecutorShardingParam();
        this.executorFailRetryCount = xxlJobLog.getExecutorFailRetryCount();
        this.triggerTime = xxlJobLog.getTriggerTime() != null ? DateTool.formatDateTime(xxlJobLog.getTriggerTime()) : null;
        this.triggerCode = xxlJobLog.getTriggerCode();
        this.triggerMsg = xxlJobLog.getTriggerMsg();
        this.handleTime = xxlJobLog.getHandleTime() != null ? DateTool.formatDateTime(xxlJobLog.getHandleTime()) : null;
        this.handleCode = xxlJobLog.getHandleCode();
        this.handleMsg = xxlJobLog.getHandleMsg();
        this.alarmStatus = xxlJobLog.getAlarmStatus();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(int jobGroup) {
        this.jobGroup = jobGroup;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getExecutorAddress() {
        return executorAddress;
    }

    public void setExecutorAddress(String executorAddress) {
        this.executorAddress = executorAddress;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getExecutorShardingParam() {
        return executorShardingParam;
    }

    public void setExecutorShardingParam(String executorShardingParam) {
        this.executorShardingParam = executorShardingParam;
    }

    public int getExecutorFailRetryCount() {
        return executorFailRetryCount;
    }

    public void setExecutorFailRetryCount(int executorFailRetryCount) {
        this.executorFailRetryCount = executorFailRetryCount;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public int getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(int triggerCode) {
        this.triggerCode = triggerCode;
    }

    public String getTriggerMsg() {
        return triggerMsg;
    }

    public void setTriggerMsg(String triggerMsg) {
        this.triggerMsg = triggerMsg;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public int getHandleCode() {
        return handleCode;
    }

    public void setHandleCode(int handleCode) {
        this.handleCode = handleCode;
    }

    public String getHandleMsg() {
        return handleMsg;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

}
