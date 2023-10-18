package org.jeecg.config.vo;

import lombok.Data;

@Data
public class WeiXinPay {
    /**
     * 微信公众号id
     */
    private String appId;
    /**
     * 商户号id
     */
    private String mchId;
    /**
     * 商户号秘钥
     */
    private String apiKey;
    /**
     * 回调地址
     */
    private String notifyUrl;
    /**
     * 是否开启会员认证
     */
    private Boolean openVipLimit;
    /**
     * 证书路径
     */
    private String certPath;
}
