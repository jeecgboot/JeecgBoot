package org.jeecg.modules.business.domain.api.shopify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class FulfillmentCreationResponse {

    @JsonProperty("fulfillment")
    private FulfillmentOrder fulfillment;

    public FulfillmentCreationResponse() {
    }

    public FulfillmentCreationResponse(FulfillmentOrder fulfillment) {
        this.fulfillment = fulfillment;
    }
}
