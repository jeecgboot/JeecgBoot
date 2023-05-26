package org.jeecg.modules.business.domain.api.shopify;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RestUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Slf4j
public abstract class ShopifyRequest {
    private final static String BASE_URL = "https://%1$s.myshopify.com/admin/api/2022-10/%2$s";
    private final static String SHOPIFY_TOKEN_HEADER_NAME = "X-Shopify-Access-Token";

    private final HttpMethod method;

    private final ShopifyRequestBody body;

    protected ShopifyRequest(HttpMethod method, ShopifyRequestBody body) {
        this.method = method;
        this.body = body;
    }

    public ResponseEntity<String> rawSend() {
        int attempts = 0;
        HttpHeaders headers = buildHeaders();

        JSONObject jsonBody = generateJson(body);
        while (attempts++ < 5){
            try {
                return RestUtil.request(String.format(BASE_URL, body.getSitePrefix(), body.endpoint()), method,
                        headers, null, jsonBody, String.class);
            } catch (Exception e) {
                log.error("Request failed on attempt n°" + attempts);
            }
        }
        return null;
    }

    protected abstract JSONObject generateJson(ShopifyRequestBody body);

    protected HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SHOPIFY_TOKEN_HEADER_NAME, body.getShopToken());
        return headers;
    }
}
