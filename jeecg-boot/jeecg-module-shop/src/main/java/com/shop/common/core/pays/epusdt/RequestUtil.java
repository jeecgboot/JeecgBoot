package com.shop.common.core.pays.epusdt;

import com.alibaba.fastjson.JSONObject;
import com.shop.common.core.pays.epusdt.entity.EpusdtEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * epusdt sdk
 * date:   2022-05-23 15:56
 * Url：   github.com/panyoujies
 */
public class RequestUtil {

    /**
     * epusdt 请求接口
     *
     * @param url
     * @param params
     * @return
     */
    public static EpusdtEntity sendPost(String url, Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(JSONObject.toJSONString(params), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EpusdtEntity> exchange = restTemplate.postForEntity(url, requestEntity, EpusdtEntity.class);
        return exchange.getBody();
    }

}
