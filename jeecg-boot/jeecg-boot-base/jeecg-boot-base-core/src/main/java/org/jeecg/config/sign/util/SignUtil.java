package org.jeecg.config.sign.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.SortedMap;

/**
 * 签名工具类
 * 
 * @author show
 * @date 10:01 2019/5/30
 */
@Slf4j
public class SignUtil {
    //签名密钥串(前后端要一致，正式发布请自行修改)
    private static final String signatureSecret = "dd05f1c54d63749eda95f9fa6d49v442a";
    public static final String xPathVariable = "x-path-variable";

    /**
     * @param params
     *            所有的请求参数都会在这里进行排序加密
     * @return 验证签名结果
     */
    public static boolean verifySign(SortedMap<String, String> params,String headerSign) {
        if (params == null || StringUtils.isEmpty(headerSign)) {
            return false;
        }
        // 把参数加密
        String paramsSign = getParamsSign(params);
        log.info("Param Sign : {}", paramsSign);
        return !StringUtils.isEmpty(paramsSign) && headerSign.equals(paramsSign);
    }

    /**
     * @param params
     *            所有的请求参数都会在这里进行排序加密
     * @return 得到签名
     */
    public static String getParamsSign(SortedMap<String, String> params) {
        //去掉 Url 里的时间戳
        params.remove("_t");
        String paramsJsonStr = JSONObject.toJSONString(params);
        log.info("Param paramsJsonStr : {}", paramsJsonStr);
        return DigestUtils.md5DigestAsHex((paramsJsonStr+signatureSecret).getBytes()).toUpperCase();
    }
}