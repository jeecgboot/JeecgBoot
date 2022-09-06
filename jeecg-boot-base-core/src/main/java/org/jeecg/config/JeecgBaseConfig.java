package org.jeecg.config;

import org.jeecg.config.vo.DomainUrl;
import org.jeecg.config.vo.Path;
import org.jeecg.config.vo.Shiro;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 加载项目配置
 * @author: jeecg-boot
 */
@Component("jeecgBaseConfig")
@ConfigurationProperties(prefix = "jeecg")
public class JeecgBaseConfig {
    /**
     * 签名密钥串(字典等敏感接口)
     * @TODO 降低使用成本加的默认值,实际以 yml配置 为准
     */
    private String signatureSecret = "dd05f1c54d63749eda95f9fa6d49v442a";
    /**
     * 需要加强校验的接口清单
     */
    private String signUrls;

    /**
     * 是否启用安全模式
     */
    private Boolean safeMode = false;
    /**
     * shiro拦截排除
     */
    private Shiro shiro;
    /**
     * 上传文件配置
     */
    private Path path;

    /**
     * 前端页面访问地址
     * pc: http://localhost:3100
     * app: http://localhost:8051
     */
    private DomainUrl domainUrl;

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

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public DomainUrl getDomainUrl() {
        return domainUrl;
    }

    public void setDomainUrl(DomainUrl domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getSignUrls() {
        return signUrls;
    }

    public void setSignUrls(String signUrls) {
        this.signUrls = signUrls;
    }
}
