package org.jeecg.config;

import org.jeecg.config.vo.*;
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
     * 上传模式  
     * 本地：local\Minio：minio\阿里云：alioss
     */
    private String uploadType;
    
    /**
     * 平台安全模式配置
     */
    private Firewall firewall;
    
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

    /**
     * 文件预览
     */
    private String fileViewDomain;
     /**
     * ES配置
     */
    private Elasticsearch elasticsearch;

    /**
     * 微信支付
     * @return
     */
    private WeiXinPay weiXinPay;
    
    
    public Elasticsearch getElasticsearch() {
        return elasticsearch;
    }

    public void setElasticsearch(Elasticsearch elasticsearch) {
        this.elasticsearch = elasticsearch;
    }

    public Firewall getFirewall() {
        return firewall;
    }

    public void setFirewall(Firewall firewall) {
        this.firewall = firewall;
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


    public String getFileViewDomain() {
        return fileViewDomain;
    }

    public void setFileViewDomain(String fileViewDomain) {
        this.fileViewDomain = fileViewDomain;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public WeiXinPay getWeiXinPay() {
        return weiXinPay;
    }

    public void setWeiXinPay(WeiXinPay weiXinPay) {
        this.weiXinPay = weiXinPay;
    }
    
}
