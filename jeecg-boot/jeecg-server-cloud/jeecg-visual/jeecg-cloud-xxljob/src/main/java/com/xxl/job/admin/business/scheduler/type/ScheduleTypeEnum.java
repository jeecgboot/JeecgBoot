package com.xxl.job.admin.business.scheduler.type;

import com.xxl.job.admin.business.scheduler.type.strategy.CronScheduleType;
import com.xxl.job.admin.business.scheduler.type.strategy.FixRateScheduleType;
import com.xxl.job.admin.business.scheduler.type.strategy.NoneScheduleType;
import com.xxl.job.admin.framework.util.I18nUtil;

/**
 * @author xuxueli 2020-10-29 21:11:23
 */
public enum ScheduleTypeEnum {

    NONE(I18nUtil.getString("schedule_type_none"), new NoneScheduleType()),

    /**
     * schedule by cron
     */
    CRON(I18nUtil.getString("schedule_type_cron"), new CronScheduleType()),

    /**
     * schedule by fixed rate (in seconds)
     */
    FIX_RATE(I18nUtil.getString("schedule_type_fix_rate"), new FixRateScheduleType()),

    /**
     * schedule by fix delay (in seconds)， after the last time
     */
    /*FIX_DELAY(I18nUtil.getString("schedule_type_fix_delay"))*/;

    private final String title;
    private final ScheduleType scheduleType;;

    ScheduleTypeEnum(String title, ScheduleType scheduleType) {
        this.title = title;
        this.scheduleType = scheduleType;
    }

    public String getTitle() {
        return title;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    /**
     * match by name
     *
     * @param name          name of ScheduleTypeEnum
     * @param defaultItem   default item
     * @return ScheduleTypeEnum
     */
    public static ScheduleTypeEnum match(String name, ScheduleTypeEnum defaultItem){
        for (ScheduleTypeEnum item: ScheduleTypeEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }

}
