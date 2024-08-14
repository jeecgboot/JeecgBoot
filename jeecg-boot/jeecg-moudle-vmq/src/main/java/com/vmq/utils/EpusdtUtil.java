package com.vmq.utils;

import com.alibaba.fastjson.JSON;
import com.vmq.dto.usdt.Epusdt;
import com.vmq.dto.usdt.EpusdtEntity;
import com.vmq.entity.OtherSetting;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * author：github.com/panyoujies
 * date:   2022-05-23 15:56
 */
public class EpusdtUtil {

    /**
     * 创建支付
     *
     * @param setting  支付驱动
     * @param price 金额
     * @param payId 订单id
     * @param param 附加内容
     * @return 付款链接
     */
    public static EpusdtEntity createPayment(OtherSetting setting, String price, String payId, String param) {

        String key = setting.getAppKey();
        String create_url = setting.getCreateUrl();
        String notify_url = setting.getNotifyUrl();

        String bigDecimal = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        Epusdt dujiao = new Epusdt();
        dujiao.setAmount(new BigDecimal(bigDecimal));
        dujiao.setOrder_id(payId);
        dujiao.setNotify_url(notify_url + "/epusdt/notifyUrl");
        dujiao.setRedirect_url(notify_url + "/epusdt/returnUrl?order_id=" + payId);
        String sign = createSign(dujiao, key);
        dujiao.setSignature(sign);

        EpusdtEntity curl = HttpRequest.sendPost(create_url, JSON.parseObject(JSON.toJSONString(dujiao), Map.class),EpusdtEntity.class);
        return curl;
    }

    public static String createSign(Object dujiao, String signKey) {
        Map<String, Object> params = JSON.parseObject(JSON.toJSONString(dujiao), Map.class);
        params.remove("signature");
        // 使用HashMap，并使用Arrays.sort排序
        String[] sortedKeys = params.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys); // 排序请求参数
        StringBuilder builder = new StringBuilder();
        for (String key : sortedKeys) {
            if (ObjectUtils.isEmpty(params.get(key))) {
                continue;
            }
            builder.append(key).append("=").append(params.get(key)).append("&");
        }
        // 去除后一位&
        builder.deleteCharAt(builder.length() - 1).toString();
        /**
         * 拼接上appsecret
         */
        builder.append(signKey);
        String signValue = DigestUtils.md5DigestAsHex(builder.toString().getBytes());
        return signValue;
    }
}
