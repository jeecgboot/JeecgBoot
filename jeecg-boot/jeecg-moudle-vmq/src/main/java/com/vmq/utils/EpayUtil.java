package com.vmq.utils;

import com.alibaba.fastjson.JSON;
import com.vmq.entity.OtherSetting;
import com.vmq.entity.PayOrder;
import org.springframework.util.DigestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EpayUtil {

    /**
     *
     * @param epaySetting
     * @param type alipay、wxpay、qqpay
     * @param price 金额
     * @param payId 订单号
     * @param param 自定义参数
     * @return
     */
    public static String epaySendPay(OtherSetting epaySetting, String type, String price, String payId, String param) {

        String key = epaySetting.getAppKey();
        String pid = epaySetting.getAppId();
        String create_url = epaySetting.getCreateUrl();
        String notify_url = epaySetting.getNotifyUrl();

        String notifyUrl = notify_url + "/epay/notifyUrl";
        String returnUrl = notify_url + "/epay/returnUrl";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pid", pid);
        paramMap.put("type", "alipay");
        paramMap.put("out_trade_no", payId);
        paramMap.put("notify_url", notifyUrl);
        paramMap.put("return_url", returnUrl);
        paramMap.put("name", param);
        paramMap.put("money", price);
        // 签名
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
        String signValue = DigestUtils.md5DigestAsHex(stringSignTemp.getBytes());

        return signValue;
    }

    public static void main(String args[]) {
        OtherSetting setting = new OtherSetting();
        setting.setCreateUrl("https://yi-pay.com");
        setting.setNotifyUrl("https://yi-pay.com");
        setting.setAppId("1002");
        setting.setAppKey("Cw6cx1o8d7wefA0D487cO80Hf74h1dh");
        String url = epaySendPay(setting,"wxpay","0.01",String.valueOf(System.currentTimeMillis()),"test");
        String result = HttpRequest.sendGet(url,"");
        System.out.println(url);
        System.out.println(result);
    }

}
