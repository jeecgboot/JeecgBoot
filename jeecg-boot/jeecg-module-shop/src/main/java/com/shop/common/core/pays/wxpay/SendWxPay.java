package com.shop.common.core.pays.wxpay;

import com.alibaba.fastjson.JSON;
import com.shop.entity.Pays;
import com.github.wxpay.sdk.WXPay;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class SendWxPay {

    /**
     * 微信官方支付 Native
     *
     * @param pays             支付驱动
     * @param price            金额
     * @param ordersMember     商品id
     * @param goodsName        商品名称
     * @param goodsDescription 附加内容
     * @param ip               用户ip地址
     * @return 返回url
     */
    public static String payNattve(Pays pays, String price, String ordersMember, String goodsName, String goodsDescription, String ip) {

        Map mapTypes = JSON.parseObject(pays.getConfig());
        String appId = mapTypes.get("appId").toString();
        String mchId = mapTypes.get("mchId").toString();
        String key = mapTypes.get("key").toString();
        String notifyUrl = mapTypes.get("notify_url").toString() + "/wxpay/notify";   // 异步通知地址

        /**
         * 处理金额
         */
        BigDecimal bigDecimal = new BigDecimal(price);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        String money = new DecimalFormat("0.##").format(multiply);

        WXPayConfigImpl wxPayConfig = new WXPayConfigImpl();
        wxPayConfig.setAppID(appId);
        wxPayConfig.setMchID(mchId);
        wxPayConfig.setKey(key);
        wxPayConfig.setCertPath("resources/cert/wxpay/apiclient_cert.p12"); // 证书地址
        wxPayConfig.setPayNotifyUrl(notifyUrl); // 异步通知

        WXPay wxPay = new WXPay(wxPayConfig);

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("body", goodsName);   // 商品描述
        requestMap.put("out_trade_no", ordersMember);  // 商户订单号
        requestMap.put("total_fee", money);   // 总金额
        requestMap.put("spbill_create_ip", ip); // 终端IP
        requestMap.put("trade_type", "NATIVE");  // Native支付类型
        requestMap.put("product_id", ordersMember);  // trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
        requestMap.put("attach", goodsDescription);  // 附加数据
        requestMap.put("notify_url", wxPayConfig.getPayNotifyUrl());   // 接收微信支付异步通知回调地址

        try {
            Map<String, String> stringStringMap = wxPay.unifiedOrder(requestMap);
            String result_code = stringStringMap.get("result_code");
            if ("SUCCESS".equals(result_code)) {
                String code_url = stringStringMap.get("code_url");
                return code_url;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微信官方h5支付
     *
     * @param pays             支付驱动
     * @param price            金额
     * @param ordersMember     商品id
     * @param goodsName        商品名称
     * @param goodsDescription 附加内容
     * @param ip               用户ip地址
     * @return 返回url
     */
    public static String payMweb(Pays pays, String price, String ordersMember, String goodsName, String goodsDescription, String ip) {

        Map mapTypes = JSON.parseObject(pays.getConfig());
        String appId = mapTypes.get("appId").toString();
        String mchId = mapTypes.get("mchId").toString();
        String key = mapTypes.get("key").toString();
        String notifyUrl = mapTypes.get("notify_url").toString() + "/wxpay/notify";   // 异步通知地址
        String wap_url = mapTypes.get("notify_url").toString();

        //支付回调页面
        String REDIRECT_URL = wap_url + "/search/order/";

        /**
         * 处理金额
         */
        BigDecimal bigDecimal = new BigDecimal(price);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        String money = new DecimalFormat("0.##").format(multiply);

        WXPayConfigImpl wxPayConfig = new WXPayConfigImpl();
        wxPayConfig.setAppID(appId);
        wxPayConfig.setMchID(mchId);
        wxPayConfig.setKey(key);
        wxPayConfig.setCertPath("resources/cert/wxpay/apiclient_cert.p12"); // 证书地址
        wxPayConfig.setPayNotifyUrl(notifyUrl); // 异步通知

        WXPay wxPay = new WXPay(wxPayConfig);

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("body", goodsName);   // 商品描述
        requestMap.put("out_trade_no", ordersMember);  // 商户订单号
        requestMap.put("total_fee", money);   // 总金额
        requestMap.put("trade_type", "MWEB");  // Mweb支付类型
        requestMap.put("spbill_create_ip", ip); // 终端IP

        Map<String, String> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("type", "Wap");
        map1.put("wap_url", wap_url);
        map1.put("wap_name", goodsName);
        String jsonString = JSON.toJSONString(map1);
        map.put("h5_info", jsonString);

        String jsonString1 = JSON.toJSONString(map);
        requestMap.put("scene_info", jsonString1);
        requestMap.put("attach", goodsDescription);  // 附加数据
        requestMap.put("notify_url", wxPayConfig.getPayNotifyUrl());   // 接收微信支付异步通知回调地址

        try {
            Map<String, String> stringStringMap = wxPay.unifiedOrder(requestMap);
            String result_code = stringStringMap.get("result_code");
            if ("SUCCESS".equals(result_code)) {
                // String trade_type = stringStringMap.get("trade_type");
                String mweb_url = stringStringMap.get("mweb_url");
                String url = URLEncoder.encode(REDIRECT_URL + ordersMember, "utf-8");
                mweb_url = mweb_url + "&redirect_url=" + url;
                return mweb_url;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
