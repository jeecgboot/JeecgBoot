package com.shop.common.core.pays.yungouos;

import com.alibaba.fastjson.JSON;
import com.shop.entity.Pays;
import com.yungouos.pay.alipay.AliPay;
import com.yungouos.pay.wxpay.WxPay;

import java.util.Map;

public class YunGouosConfig {

    /**
     * yungou微信支付接口
     *
     * @param pays             支付配置
     * @param price            金额
     * @param ordersMember     本地订单号
     * @param goodsDescription 附加内容
     * @return
     */
    public static String yunGouosWxPay(Pays pays, String price, String ordersMember, String goodsName, String goodsDescription) {
        Map mapTypes = JSON.parseObject(pays.getConfig());

        String mchId = mapTypes.get("mchId").toString();  //
        String key = mapTypes.get("key").toString(); //
        String notifyUrl = mapTypes.get("notify_url").toString() + "/yungouos/notify";   //

        String result = null;

        try {
            /**
             * 扫码支付 返回二维码连接
             */
            result = WxPay.nativePay(ordersMember, price, mchId, goodsName, "1", goodsDescription, notifyUrl, null, null, null, null, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param pays
     * @param price
     * @param ordersMember
     * @param goodsName
     * @param goodsDescription
     * @return
     */
    public static String yunGouosAliPay(Pays pays, String price, String ordersMember, String goodsName, String goodsDescription) {
        Map mapTypes = JSON.parseObject(pays.getConfig());

        String mchId = mapTypes.get("mchId").toString();  //
        String key = mapTypes.get("key").toString(); //
        String notifyUrl = mapTypes.get("notify_url").toString() + "/yungouos/notify";   //

        String result = null;

        try {
            /**
             * 扫码支付 返回二维码连接
             */
            result = AliPay.nativePay(ordersMember, price, mchId, goodsName, "1", goodsDescription, notifyUrl, key);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
