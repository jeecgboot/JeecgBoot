package org.jeecg.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 加载项目配置
 */
@Component("jeeccgBaseConfig")
@ConfigurationProperties(prefix = "jeecg")
public class JeeccgBaseConfig {
    /**
     * 是否启用安全模式
     */
    private Boolean safeMode = false;

    public Boolean getSafeMode() {
        return safeMode;
    }

    public void setSafeMode(Boolean safeMode) {
        this.safeMode = safeMode;
    }
}
