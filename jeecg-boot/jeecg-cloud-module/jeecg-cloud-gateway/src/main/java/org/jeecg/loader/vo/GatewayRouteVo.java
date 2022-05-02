package org.jeecg.loader.vo;

import lombok.Data;

/**
 * 路由参数模型
 * @author zyf
 * @date: 2022/4/21 10:55
 */
@Data
public class GatewayRouteVo {
    private String id;
    private String name;
    private String uri;
    private String predicates;
    private String filters;
    private Integer stripPrefix;
    private Integer retryable;
    private Integer persist;
    private Integer status;
}
