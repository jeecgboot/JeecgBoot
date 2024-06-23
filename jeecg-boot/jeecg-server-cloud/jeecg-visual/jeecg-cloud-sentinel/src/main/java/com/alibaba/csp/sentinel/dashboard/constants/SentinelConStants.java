package com.alibaba.csp.sentinel.dashboard.constants;

/**
 * sentinel常量配置
 * @author zyf
 */
public class SentinelConStants {
    public static final String GROUP_ID = "SENTINEL_GROUP";

    /**
     * 流控规则
     */
    public static final String FLOW_DATA_ID_POSTFIX = "-flow-rules";
    /**
     * 热点参数
     */
    public static final String PARAM_FLOW_DATA_ID_POSTFIX = "-param-rules";
    /**
     * 降级规则
     */
    public static final String DEGRADE_DATA_ID_POSTFIX = "-degrade-rules";
    /**
     * 系统规则
     */
    public static final String SYSTEM_DATA_ID_POSTFIX = "-system-rules";
    /**
     * 授权规则
     */
    public static final String AUTHORITY_DATA_ID_POSTFIX = "-authority-rules";

    /**
     * 网关API
     */
    public static final String GETEWAY_API_DATA_ID_POSTFIX = "-api-rules";
    /**
     * 网关流控规则
     */
    public static final String GETEWAY_FLOW_DATA_ID_POSTFIX = "-flow-rules";
}
