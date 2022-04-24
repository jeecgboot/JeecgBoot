package org.jeecg.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author scott
 * @date 2020/05/26
 * 路由配置信息
 */
@Slf4j
@Configuration
@RefreshScope
public class GatewayRoutersConfiguration {

    public static final long DEFAULT_TIMEOUT = 30000;
    public static String SERVER_ADDR;
    public static String NAMESPACE;
    public static String DATA_ID;
    public static String ROUTE_GROUP;
    public static String USERNAME;
    public static String PASSWORD;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    public void setServerAddr(String serverAddr) {
        SERVER_ADDR = serverAddr;
    }

    @Value("${spring.cloud.nacos.discovery.namespace}")
    public void setNamespace(String namespace) {
        NAMESPACE = namespace;
    }

    @Value("${jeecg.route.config.data-id:#{null}}")
    public void setRouteDataId(String dataId) {
        DATA_ID = dataId + ".json";
    }

    @Value("${jeecg.route.config.group:DEFAULT_GROUP:#{null}}")
    public void setRouteGroup(String routeGroup) {
        ROUTE_GROUP = routeGroup;
    }


    @Value("${spring.cloud.nacos.config.username}")
    public void setUsername(String username) {
        USERNAME = username;
    }

    @Value("${spring.cloud.nacos.config.password}")
    public void setPassword(String password) {
        PASSWORD = password;
    }


    /**
     * 接口地址（通过9999端口直接访问）
     *
     * @param indexHtml
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> indexRouter(@Value("classpath:/META-INF/resources/doc.html") final org.springframework.core.io.Resource indexHtml) {
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(indexHtml));
    }

}
