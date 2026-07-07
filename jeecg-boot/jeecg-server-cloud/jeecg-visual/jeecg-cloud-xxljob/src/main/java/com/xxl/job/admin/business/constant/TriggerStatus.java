package com.xxl.job.admin.business.constant;

/**
 * Trigger Status
 *
 * @author xuxueli 2019-05-04
 */
public enum TriggerStatus {

    /**
     * Stopped
     */
    STOPPED(0, "stopped"),

    /**
     * Running
     */
    RUNNING(1, "running");

    private int value;
    private String desc;

    TriggerStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
