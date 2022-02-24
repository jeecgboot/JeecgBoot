package org.jeecg.config;

import org.jeecg.config.vo.Shiro;
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
    /**
     * shiro拦截排除
     */
    private Shiro shiro;
    /**
     * 签名密钥串(字典等敏感接口)
     * @TODO 降低使用成本加的默认值,实际以 yml配置 为准
     */
    private String signatureSecret = "dd05f1c54d63749eda95f9fa6d49v442a";

    public Boolean getSafeMode() {
        return safeMode;
    }

    public void setSafeMode(Boolean safeMode) {
        this.safeMode = safeMode;
    }

    public String getSignatureSecret() {
        return signatureSecret;
    }

    public void setSignatureSecret(String signatureSecret) {
        this.signatureSecret = signatureSecret;
    }

    public Shiro getShiro() {
        return shiro;
    }

    public void setShiro(Shiro shiro) {
        this.shiro = shiro;
    }
}
