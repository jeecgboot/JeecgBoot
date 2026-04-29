package org.jeecg.modules.business.domain.api.shouman;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class OrderStatusRequestBody implements RequestBody {

    private String orderId;

    @Override
    public String path() {
        return "/order/openapi/orders/getOrderStatus";
    }

    @Override
    public Map<String, Object> parameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderId", orderId);
        return parameters;
    }
}
