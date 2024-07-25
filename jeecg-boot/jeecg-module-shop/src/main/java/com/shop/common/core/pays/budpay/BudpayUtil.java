package com.shop.common.core.pays.budpay;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shop.entity.Pays;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BudpayUtil {

    public static String budpaySendPay(Pays pays, String price, String payId, String param) {
        Map mapTypes = JSON.parseObject(pays.getConfig());

        String key = mapTypes.get("key").toString();
        String pid = mapTypes.get("pid").toString();
        String create_url = mapTypes.get("create_url").toString();
        String notify_url = mapTypes.get("notify_url").toString();

        String notifyUrl = notify_url + "/budpay/notifyUrl";
        String returnUrl = notify_url + "/budpay/returnUrl";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("method", "epay://pay/create");
        paramMap.put("mch_id", pid);
        if (pays.getDriver().equals("budpay_alipay")) {
            paramMap.put("pay_type", "alipay");
        } else if (pays.getDriver().equals("budpay_wechat")) {
            paramMap.put("pay_type", "wechat");
        }

        paramMap.put("out_trade_no", payId);
        paramMap.put("notify_url", notifyUrl);
        paramMap.put("return_url", returnUrl);
        paramMap.put("name", param);
        paramMap.put("amount", price);
        String sign = createSign(paramMap, key);

        paramMap.put("sign", sign);

        String httpsPost = RequestUtil.getHttpsPost(create_url, paramMap);
        JSONObject jsonObject = JSON.parseObject(httpsPost);
        String url = jsonObject.get("url").toString();
        return url;
    }

    /**
     * 生成密钥
     *
     * @param params
     * @param privateKey
     * @return
     */
    public static String createSign(Map<String, Object> params, String privateKey) {
        // 生成签名前先去除sign
        params.remove("sign");
        Map<String, String> newmap = new HashMap<>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            String value = (String) param.getValue();
            if (StringUtils.isBlank(value)) {
                continue;
            }
            newmap.put(param.getKey(), param.getValue().toString());
        }

        // 使用HashMap，并使用Arrays.sort排序
        String[] sortedKeys = newmap.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);// 排序请求参数

        StringBuilder builder = new StringBuilder();
        for (String key : sortedKeys) {
            if (StringUtils.isBlank(key)) {
                continue;
            }
            builder.append(key).append("=").append(params.get(key)).append("&");
        }
        /**
         * 拼接上appsecret
         */
        String stringSignTemp = builder + "key=" + privateKey;
        String signValue = SecureUtil.md5(stringSignTemp);

        return signValue;
    }

}
