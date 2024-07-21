package com.bomaos.common.core.pays.payjs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bomaos.settings.entity.Pays;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class sendPayjs {

    /**
     * payjs 支付
     *
     * @param pays             本地支付驱动
     * @param price            付款金额
     * @param ordersMember     本地订单号
     * @param goodsName        商品名称
     * @param goodsDescription 自定义内容
     * @return 返回收款二维码
     * @throws IOException
     */
    public static String pay(Pays pays, String price, String ordersMember, String goodsName, String goodsDescription) throws IOException {

        Map mapTypes = JSON.parseObject(pays.getConfig());

        String mchId = mapTypes.get("mchId").toString();  //
        String key = mapTypes.get("key").toString(); //
        String notifyUrl = mapTypes.get("notify_url").toString() + "/payjs/notify";   //

        BigDecimal bigDecimal = new BigDecimal(price);
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        String format = new DecimalFormat("0.##").format(multiply);

        Map<String, Object> payData = new HashMap<>();
        payData.put("mchid", mchId);
        payData.put("total_fee", format);
        payData.put("out_trade_no", ordersMember); // 订单号 随便输的，自己生成一下就好了
        payData.put("body", goodsName);
        payData.put("attach", goodsDescription);
        payData.put("notify_url", notifyUrl);

        /**
         * 支付宝付款参数
         */
        if (pays.getDriver().equals("payjs_alipay")) {
            payData.put("type", "alipay");
        }

        // 进行sign签名
        payData.put("sign", SignUtil.sign(payData, key));

        // 请求payjs获取二维码
        String result = HttpsUtils.sendPost(PayjsConfig.nativeUrl, JSON.toJSONString(payData), null);
        JSONObject jsonObject = JSON.parseObject(result);
        String code_url = jsonObject.get("code_url").toString(); // 获取付款二维码

        return code_url;
    }
}
