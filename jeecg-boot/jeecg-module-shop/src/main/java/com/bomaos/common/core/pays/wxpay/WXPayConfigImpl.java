package com.bomaos.common.core.pays.wxpay;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;
import lombok.ToString;

import java.io.InputStream;

@Data
@ToString
public class WXPayConfigImpl implements WXPayConfig {

    /**
     * 获取 App ID
     *
     * @return App ID
     */
    private String appID;

    /**
     * 获取 Mch ID
     *
     * @return Mch ID
     */
    private String mchID;

    /**
     * 获取 API 密钥
     *
     * @return API密钥
     */
    private String key;

    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    private String certPath;

    /**
     * HTTP(S) 连接超时时间，单位毫秒
     *
     * @return
     */
    private int httpConnectTimeoutMs = 8000;

    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     *
     * @return
     */
    private int httpReadTimeoutMs = 10000;

    /**
     * 微信支付异步通知地址
     */
    private String payNotifyUrl;

    /**
     * 微信退款异步通知地址
     */
    private String refundNotifyUrl;

    @Override
    public InputStream getCertStream() {
        InputStream certStream = getClass().getClassLoader().getResourceAsStream(certPath);
        return certStream;
    }

}
