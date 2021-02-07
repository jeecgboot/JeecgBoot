package org.jeecg.boot.starter.lock.prop;

import lombok.Data;
import org.jeecg.boot.starter.lock.enums.RedisConnectionType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redisson配置映射类
 *
 * @author zyf
 * @date 2020-11-11
 */
@Data
@ConfigurationProperties(prefix = "jeecg.redisson")
public class RedissonProperties {

    /**
     * redis主机地址，ip：port，多个用逗号(,)分隔
     */
    private String address;
    /**
     * 连接类型
     */
    private RedisConnectionType type;
    /**
     * 密码
     */
    private String password;
    /**
     * 数据库(默认0)
     */
    private int database;

    /**
     * 是否装配redisson配置
     */
    private Boolean enabled = true;

}
