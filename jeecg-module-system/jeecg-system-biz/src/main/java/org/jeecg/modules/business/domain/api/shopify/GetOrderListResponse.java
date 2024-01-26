package org.jeecg.modules.business.domain.api.shopify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class GetOrderListResponse {

    @JsonProperty("orders")
    private List<Order> orders;

    public GetOrderListResponse() {
    }

    public GetOrderListResponse(List<Order> orders) {
        this.orders = orders;
    }
}
