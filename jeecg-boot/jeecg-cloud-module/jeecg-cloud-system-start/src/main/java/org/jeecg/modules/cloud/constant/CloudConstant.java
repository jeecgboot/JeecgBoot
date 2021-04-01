package org.jeecg.modules.cloud.constant;

/**
 * 微服务单元测试常量定义
 */
public interface CloudConstant {

    /**
     * 微服务名【对应模块jeecg-boot-module-demo】
     */
    public final static String SERVER_NAME_JEECGDEMO = "jeecg-demo";

    /**
     * MQ测试队列名字
     */
    public final static String MQ_JEECG_PLACE_ORDER = "jeecg_place_order";
    public final static String MQ_JEECG_PLACE_ORDER_TIME = "jeecg_place_order_time";

    /**
     * MQ测试消息总线
     */
    public final static String MQ_DEMO_BUS_EVENT = "demoBusEvent";

    /**
     * 分布式锁lock key
     */
    public final static String REDISSON_DEMO_LOCK_KEY1 = "demoLockKey1";
    public final static String REDISSON_DEMO_LOCK_KEY2 = "demoLockKey2";

}
