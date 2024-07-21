package com.bomaos.common.core.pays.jiepay;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.bomaos.settings.entity.Pays;

import java.util.Map;

public class JiepaySend {

    public static String jiePayUtils(Pays pays, String price, String payId, String param) {

        Map mapTypes = JSON.parseObject(pays.getConfig());

        String appid = mapTypes.get("appid").toString();
        String apptoken = mapTypes.get("apptoken").toString();

        String sign = SecureUtil.md5(appid + apptoken);

        Integer type = 1;
        if (pays.getDriver().equals("jiepay_alipay")) {
            type = 1;
        } else if (pays.getDriver().equals("jiepay_wxpay")) {
            type = 2;
        }

        String is_ok = "true";
        String url = "http://pay.joo.life/Corder?appid="
                + appid + "&code=" + type + "&order_id="
                + payId + "&order_rmb=" + price + "&sign="
                + sign + "&is_ok=" + is_ok + "&diy=" + param;

        return url;
    }

}
