package org.jeecg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author scott
 * @date 2020/05/26
 * 路由配置信息
 */
@Configuration
@RefreshScope
public class GatewayRoutersConfig {
    /**
     * 路由配置方式：database，yml，nacos
     */
    public String dataType;
    public String serverAddr;
    public String namespace;
    public String dataId;
    public String routeGroup;
    public String username;
    public String password;

    @Value("${jeecg.route.config.data-type:#{null}}")
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    @Value("${jeecg.route.config.data-id:#{null}}")
    public void setRouteDataId(String dataId) {
        this.dataId = dataId + ".json";
    }

    @Value("${spring.cloud.nacos.config.group:DEFAULT_GROUP:#{null}}")
    public void setRouteGroup(String routeGroup) {
        this.routeGroup = routeGroup;
    }

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    @Value("${spring.cloud.nacos.config.namespace:#{null}}")
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Value("${spring.cloud.nacos.config.username:#{null}}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${spring.cloud.nacos.config.password:#{null}}")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDataType() {
        return dataType;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getDataId() {
        return dataId;
    }

    public String getRouteGroup() {
        return routeGroup;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
