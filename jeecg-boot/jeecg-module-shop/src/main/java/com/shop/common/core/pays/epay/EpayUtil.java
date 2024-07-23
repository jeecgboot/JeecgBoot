package com.shop.common.core.pays.epay;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.shop.entity.Pays;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EpayUtil {

    public static String epaySendPay(Pays pays, String price, String payId, String param) {
        Map mapTypes = JSON.parseObject(pays.getConfig());

        String key = mapTypes.get("key").toString();
        String pid = mapTypes.get("pid").toString();
        String create_url = mapTypes.get("create_url").toString();
        String notify_url = mapTypes.get("notify_url").toString();

        String notifyUrl = notify_url + "/epay/notifyUrl";
        String returnUrl = notify_url + "/epay/returnUrl";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pid", pid);

        String type = "";
        if (pays.getDriver().equals("epay_alipay")) {
            type = "alipay";
            paramMap.put("type", "alipay");
        } else if (pays.getDriver().equals("epay_wxpay")) {
            type = "wxpay";
            paramMap.put("type", "wxpay");
        } else if (pays.getDriver().equals("epay_qqpay")) {
            type = "qqpay";
            paramMap.put("type", "qqpay");
        }

        paramMap.put("out_trade_no", payId);
        paramMap.put("notify_url", notifyUrl);
        paramMap.put("return_url", returnUrl);
        paramMap.put("name", param);
        paramMap.put("money", price);
        String sign = createSign(paramMap, key);

        paramMap.put("sign", sign);
        String sign_type = "MD5";
        paramMap.put("sign_type", sign_type);

        String urls = create_url + "/submit.php"
                + "?pid=" + pid + "&type=" + type + "&out_trade_no=" + payId
                + "&notify_url=" + notifyUrl + "&return_url=" + returnUrl
                + "&name=" + param + "&money=" + price + "&sign=" + sign
                + "&sign_type=" + sign_type;

        return urls;
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
        params.remove("sign_type");

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
        String result = builder.deleteCharAt(builder.length() - 1).toString();
        /**
         * 拼接上appsecret
         */
        String stringSignTemp = result + privateKey;
        String signValue = SecureUtil.md5(stringSignTemp);

        return signValue;
    }

}
