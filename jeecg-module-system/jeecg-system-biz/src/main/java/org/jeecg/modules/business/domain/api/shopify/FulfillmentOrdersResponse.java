package org.jeecg.modules.business.domain.api.shopify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class FulfillmentOrdersResponse {

    @JsonProperty("fulfillment_orders")
    private List<FulfillmentOrder> fulfillmentOrders;

    public FulfillmentOrdersResponse() {
    }

    public FulfillmentOrdersResponse(List<FulfillmentOrder> fulfillmentOrders) {
        this.fulfillmentOrders = fulfillmentOrders;
    }
}
