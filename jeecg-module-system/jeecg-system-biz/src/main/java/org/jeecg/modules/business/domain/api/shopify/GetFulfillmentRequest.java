package org.jeecg.modules.business.domain.api.shopify;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpMethod;

public class GetFulfillmentRequest extends ShopifyRequest {

    public GetFulfillmentRequest(ShopifyRequestBody body) {
        super(HttpMethod.GET, body);
    }

    @Override
    protected JSONObject generateJson(ShopifyRequestBody body) {
        return null;
    }
}
