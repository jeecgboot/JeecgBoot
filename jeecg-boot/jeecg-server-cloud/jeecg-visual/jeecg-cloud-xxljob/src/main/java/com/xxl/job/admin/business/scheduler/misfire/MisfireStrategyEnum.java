package com.xxl.job.admin.business.scheduler.misfire;

import com.xxl.job.admin.business.scheduler.misfire.strategy.MisfireDoNothing;
import com.xxl.job.admin.business.scheduler.misfire.strategy.MisfireFireOnceNow;
import com.xxl.job.admin.framework.util.I18nUtil;

/**
 * @author xuxueli 2020-10-29 21:11:23
 */
public enum MisfireStrategyEnum {

    /**
     * do nothing
     */
    DO_NOTHING(I18nUtil.getString("misfire_strategy_do_nothing"), new MisfireDoNothing()),

    /**
     * fire once now
     */
    FIRE_ONCE_NOW(I18nUtil.getString("misfire_strategy_fire_once_now"), new MisfireFireOnceNow());

    private final String title;
    private final MisfireHandler misfireHandler;

    MisfireStrategyEnum(String title, MisfireHandler misfireHandler) {
        this.title = title;
        this.misfireHandler = misfireHandler;
    }

    public String getTitle() {
        return title;
    }

    public MisfireHandler getMisfireHandler() {
        return misfireHandler;
    }

    /**
     * match misfire strategy
     *
     * @param name          name of misfire strategy
     * @param defaultItem   default misfire strategy
     * @return misfire strategy
     */
    public static MisfireStrategyEnum match(String name, MisfireStrategyEnum defaultItem){
        for (MisfireStrategyEnum item: MisfireStrategyEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }

}
