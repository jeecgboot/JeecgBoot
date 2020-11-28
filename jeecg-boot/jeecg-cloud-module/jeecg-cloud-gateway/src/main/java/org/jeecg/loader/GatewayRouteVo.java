package org.jeecg.loader;

import lombok.Data;

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
