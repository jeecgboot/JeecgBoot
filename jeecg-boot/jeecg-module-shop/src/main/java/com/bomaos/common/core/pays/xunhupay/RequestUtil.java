package com.bomaos.common.core.pays.xunhupay;

import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 虎皮椒工具类
 * author Panyoujie
 */
public class RequestUtil {

    /**
     * 虎皮椒请求接口
     *
     * @param url
     * @param params
     * @return
     */
    public static XunhuEntity getHttpsPost(String url, Map<String, Object> params) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            multiValueMap.add(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multiValueMap, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity<XunhuEntity> exchange = restTemplate.exchange(url, method, requestEntity, XunhuEntity.class);
        return exchange.getBody();
    }

}
