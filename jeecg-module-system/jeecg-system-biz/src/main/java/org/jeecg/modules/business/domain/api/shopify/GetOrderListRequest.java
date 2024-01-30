package org.jeecg.modules.business.domain.api.shopify;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpMethod;

public class GetOrderListRequest extends ShopifyRequest {

    public GetOrderListRequest(ShopifyRequestBody body) {
        super(HttpMethod.GET, body);
    }

    @Override
    protected JSONObject generateJson(ShopifyRequestBody body) {
        return null;
    }
}
