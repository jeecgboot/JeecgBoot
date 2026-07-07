package com.xxl.job.admin.business.scheduler.misfire;

/**
 * Misfire Handler
 *
 * @author xuxueli 2020-10-29
 */
public abstract class MisfireHandler {

    /**
     * misfire handle
     *
     * @param jobId jobId
     */
    public abstract void handle(final int jobId);

}
