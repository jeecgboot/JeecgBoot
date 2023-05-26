package org.jeecg.modules.business.domain.api.shopify;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

@Slf4j
@Data
public class FulfillmentOrder {

    @JSONField(deserialize = false)
    private BigInteger id;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("status")
    private String status;

    public FulfillmentOrder() {
    }

    public boolean isOpen() {
        return status.equalsIgnoreCase("open");
    }

    public boolean isSuccess() {
        return status.equalsIgnoreCase("success");
    }
}
