package org.jeecg.config;

import org.jeecg.config.vo.GaoDeApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 高德账号配置
 */
@Lazy(false)
@Configuration("jeecgGaodeBaseConfig")
@ConfigurationProperties(prefix = "jeecg.jmreport")
public class JeecgGaodeBaseConfig {

    /**
     * 高德开放API配置
     */
    private GaoDeApi gaoDeApi;

    public GaoDeApi getGaoDeApi() {
        return gaoDeApi;
    }

    public void setGaoDeApi(GaoDeApi gaoDeApi) {
        this.gaoDeApi = gaoDeApi;
    }
    
}
