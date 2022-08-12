package com.alibaba.csp.sentinel.dashboard.rule.nacos;

/**
 * @Description: nacos配置
 * @author: zyf
 * @date: 2022/03/01$
 * @version: V1.0
 */
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nacos.server")
@Data
public class NacosConfigProperties {

    private String ip;

    private String namespace;

    private String username;

    private String password;

    private String groupId;

    public String getServerAddr() {
        return this.getIp();
    }

}