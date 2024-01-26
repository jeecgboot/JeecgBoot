package org.jeecg.modules.business.domain.api.shopify;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

@Slf4j
@Data
public class Order {

    @JSONField(deserialize = false)
    private BigInteger id;

    @JsonProperty("note")
    private String note;

    public Order() {
    }

    public boolean hasNote() {
        return note != null;
    }
}
