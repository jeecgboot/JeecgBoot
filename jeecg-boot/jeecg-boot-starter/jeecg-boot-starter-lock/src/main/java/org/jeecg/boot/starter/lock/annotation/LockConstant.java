package org.jeecg.boot.starter.lock.annotation;

/**
 * @author zyf
 * @date 2019/10/26 18:26
 */

/**
 * 分布式锁枚举类
 * @author zyf
 */
public enum LockConstant {
    /**
     * 通用锁常量
     */
    COMMON("commonLock:", 1, 500, "请勿重复点击");
    /**
     * 分布式锁前缀
     */
    private String keyPrefix;
    /**
     * 等到最大时间，强制获取锁
     */
    private int waitTime;
    /**
     * 锁失效时间
     */
    private int leaseTime;
    /**
     * 加锁提示
     */
    private String message;

    LockConstant(String keyPrefix, int waitTime, int leaseTime, String message) {
        this.keyPrefix = keyPrefix;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
        this.message = message;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(int leaseTime) {
        this.leaseTime = leaseTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
