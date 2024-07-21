package com.bomaos.common.core.pays.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.bomaos.settings.entity.Pays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * author：panyoujie
 * date：2022-0902
 */
public class AlipayUtil {

    private static final Logger logger = LoggerFactory.getLogger(AlipayUtil.class);

    /**
     * 支付宝当面付
     *
     * @param subject     自定义内容
     * @param orderNo     订单号
     * @param totalAmount 订单金额
     * @return
     */
    public static String getFaceToFace(Pays pays, String subject, String orderNo, String totalAmount) {
        // 获取支付配置信息
        Map mapTypes = JSON.parseObject(pays.getConfig());
        String app_id = mapTypes.get("app_id").toString();
        String private_key = mapTypes.get("private_key").toString();
        String alipay_public_key = mapTypes.get("alipay_public_key").toString();
        String notify_url = mapTypes.get("notify_url").toString() + "/alipay/notify";   // 异步通知地址

        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions(app_id, private_key, alipay_public_key, notify_url));
        try {
            // 2. 发起API调用（使用面对面支付中的预下单）
            AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace().preCreate(subject, orderNo, totalAmount);
            // 3. 处理响应或异常
            if ("10000".equals(response.code)) {
                logger.info("调用成功：{}", response.qrCode);
                return response.qrCode; //返回二维码
            } else {
                logger.error("调用失败，原因：{},{}", response.msg, response.subMsg);
            }
        } catch (Exception e) {
            logger.error("调用遭遇异常，原因：{}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 支付宝电脑网站支付
     *
     * @param subject     自定义内容
     * @param orderNo     订单号
     * @param totalAmount 订单金额
     * @return
     */
    public static String getPcPage(Pays pays, String subject, String orderNo, String totalAmount) {
        // 获取支付配置信息
        Map mapTypes = JSON.parseObject(pays.getConfig());
        String app_id = mapTypes.get("app_id").toString();
        String private_key = mapTypes.get("private_key").toString();
        String alipay_public_key = mapTypes.get("alipay_public_key").toString();
        String url = mapTypes.get("notify_url").toString();
        String notify_url = url + "/alipay/notify";
        String return_url = url + "/alipay/return_url";

        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions(app_id, private_key, alipay_public_key, notify_url));
        try {
            // 2. 发起API调用（使用面对面支付中的预下单）
            AlipayTradePagePayResponse response = Factory.Payment.Page().pay(subject, orderNo, totalAmount, return_url);
            // 3. 处理响应或异常
            return response.getBody();
        } catch (Exception e) {
            logger.error("调用遭遇异常，原因：{}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Config getOptions(String appId, String privateKey, String publicKey, String notifyUrl) {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";

        // 请更换为您的AppId
        config.appId = appId;
        // 请更换为您的PKCS8格式的应用私钥
        config.merchantPrivateKey = privateKey;
        config.alipayPublicKey = publicKey;
        // 如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.notifyUrl = notifyUrl; //这里是支付宝接口回调地址

        return config;
    }

}
