package org.jeecg.config;

/**
 * nocos配置方式枚举
 */
public enum RouterDataType {
    /**
     * 数据库加载路由配置
     */
    database,
    /**
     * 本地yml加载路由配置
     */
    yml,
    /**
     * nacos加载路由配置
     */
    nacos
}
