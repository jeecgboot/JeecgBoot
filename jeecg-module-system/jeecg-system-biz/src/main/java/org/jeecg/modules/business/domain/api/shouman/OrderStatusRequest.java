package org.jeecg.modules.business.domain.api.shouman;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class OrderStatusRequest extends Request {

    public OrderStatusRequest(OrderStatusRequestBody body) {
        super(body);
    }

    @Override
    protected HttpMethod method() {
        return HttpMethod.GET;
    }

    @Override
    protected boolean useDefaultParameters() {
        return false;
    }

    @Override
    protected String buildUrl() {
        OrderStatusRequestBody requestBody = (OrderStatusRequestBody) body();
        return UriComponentsBuilder.fromHttpUrl(BASE_URL + requestBody.path())
                .queryParam("orderId", requestBody.getOrderId())
                .queryParam("shopCode", SHOP_CODE)
                .build()
                .toUriString();
    }

    @Override
    protected Object requestBody(String bodyString) {
        return null;
    }

    public OrderStatusResponse send() {
        ResponseEntity<String> response = rawSend();
        if (response == null || response.getBody() == null) {
            return null;
        }
        return JSON.parseObject(response.getBody(), OrderStatusResponse.class);
    }
}
