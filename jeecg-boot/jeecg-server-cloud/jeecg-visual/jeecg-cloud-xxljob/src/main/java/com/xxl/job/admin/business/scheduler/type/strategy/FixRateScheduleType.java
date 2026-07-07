package com.xxl.job.admin.business.scheduler.type.strategy;

import com.xxl.job.admin.business.model.XxlJobInfo;
import com.xxl.job.admin.business.scheduler.type.ScheduleType;
import com.xxl.tool.core.DateTool;

import java.util.Date;

public class FixRateScheduleType extends ScheduleType {

    @Override
    public Date generateNextTriggerTime(XxlJobInfo jobInfo, Date fromTime) throws Exception {

        // generate next trigger time, fix rate delay
        Date nextTriggerTime = new Date(fromTime.getTime() + Long.parseLong(jobInfo.getScheduleConf()) * 1000L);

        // assign second:
        if (nextTriggerTime.getTime() % 1000 != 0) {
            nextTriggerTime = DateTool.addSeconds(DateTool.setMilliseconds(nextTriggerTime, 0), 1);
        }

        return nextTriggerTime;

    }

}
