package org.jeecg.config.thirdapp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 第三方APP配置
 *
 * @author sunjianlei
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "third-app.type")
public class ThirdAppTypeConfig {

    /**
     * 对应企业微信配置
     */
    private ThirdAppTypeItemVo WECHAT_ENTERPRISE;
    /**
     * 对应钉钉配置
     */
    private ThirdAppTypeItemVo DINGTALK;

}
