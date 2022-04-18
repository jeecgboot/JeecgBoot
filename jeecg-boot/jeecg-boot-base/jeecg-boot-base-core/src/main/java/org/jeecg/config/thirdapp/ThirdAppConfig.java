package org.jeecg.config.thirdapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 第三方App对接配置
 * @author: jeecg-boot
 */
@Configuration
public class ThirdAppConfig {

    /**
     * 钉钉
     */
    public final static String DINGTALK = "DINGTALK";
    /**
     * 企业微信
     */
    public final static String WECHAT_ENTERPRISE = "WECHAT_ENTERPRISE";

    /**
     * 是否启用 第三方App对接
     */
    @Value("${third-app.enabled:false}")
    private boolean enabled;

    /**
     * 系统类型，目前支持：WECHAT_ENTERPRISE（企业微信）；DINGTALK （钉钉）
     */
    @Autowired
    private ThirdAppTypeConfig type;

    public boolean isEnabled() {
        return enabled;
    }

    public ThirdAppConfig setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * 获取企业微信配置
     */
    public ThirdAppTypeItemVo getWechatEnterprise() {
        return this.type.getWECHAT_ENTERPRISE();
    }

    /**
     * 获取钉钉配置
     */
    public ThirdAppTypeItemVo getDingtalk() {
        return this.type.getDINGTALK();
    }

    /**
     * 获取企业微信是否启用
     */
    public boolean isWechatEnterpriseEnabled() {
        try {
            return this.enabled && this.getWechatEnterprise().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取钉钉是否启用
     */
    public boolean isDingtalkEnabled() {
        try {
            return this.enabled && this.getDingtalk().isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

}
