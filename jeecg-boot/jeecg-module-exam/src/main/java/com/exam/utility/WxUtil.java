package com.exam.utility;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @version 3.5.0
 * @description: The type Wx util.
 * Copyright (C), 2020-2024
 * @date 2021/12/25 9:45
 */
public class WxUtil {
    private static final Logger logger = LoggerFactory.getLogger(WxUtil.class);
    private static final String openIdUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    /**
     * Gets open id.
     *
     * @param appId  the app id
     * @param secret the secret
     * @param code   the code
     * @return the open id
     */
    public static String getOpenId(String appId, String secret, String code) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            String requestUrl = String.format(openIdUrl, appId, secret, code);
            HttpGet httpGet = new HttpGet(requestUrl);
            HttpEntity responseEntity = httpClient.execute(httpGet).getEntity();
            if (responseEntity != null) {
                String responseStr = EntityUtils.toString(responseEntity);
                if (responseStr.contains("openid")) {
                    WxResponse wxResponse = JsonUtil.toJsonObject(responseStr, WxResponse.class);
                    return wxResponse.getOpenid();
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
