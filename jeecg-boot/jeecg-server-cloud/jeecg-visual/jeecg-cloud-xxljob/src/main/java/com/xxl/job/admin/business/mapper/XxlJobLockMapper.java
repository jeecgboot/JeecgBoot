package com.xxl.job.admin.business.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * job lock
 *
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface XxlJobLockMapper {

    /**
     * get schedule lock
     */
    String scheduleLock();

}