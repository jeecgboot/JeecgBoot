package org.jeecg.modules.business.domain.api.mabang.orderUpdateOrderNewOrder;

import org.jeecg.modules.business.domain.api.mabang.Request;

public class OrderUpdateOrderNewOrderRequest extends Request {
    public OrderUpdateOrderNewOrderRequest(OrderUpdateOrderNewOrderRequestBody body) {
        super(body);
    }

    @Override
    public OrderUpdateOrderNewOrderResponse send() {
        String jsonString = rawSend().getBody();
        return OrderUpdateOrderNewOrderResponse.parse(jsonString);
    }
}