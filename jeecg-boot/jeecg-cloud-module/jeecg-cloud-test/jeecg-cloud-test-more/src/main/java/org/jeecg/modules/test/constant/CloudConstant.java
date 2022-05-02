package org.jeecg.modules.test.constant;

/**
 * 微服务单元测试常量定义
 * @author: zyf
 * @date: 2022/04/21
 */
public interface CloudConstant {

    /**
     * MQ测试队列名字
     */
    public final static String MQ_JEECG_PLACE_ORDER = "jeecg_place_order";

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
