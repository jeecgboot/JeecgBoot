package org.jeecg.modules.business.domain.api.shopify;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
public class Order {

    @JSONField(deserialize = false)
    private BigInteger id;

    @JsonProperty("note")
    private String note;

    @JsonProperty("discount_codes")
    private List<DiscountCode> discountCodes;

    public Order() {
    }

    public boolean hasNote() {
        return note != null;
    }

    public boolean hasDiscountCodes() {
        return discountCodes != null && !discountCodes.isEmpty();
    }

    public String getDiscountCode() {
        if (discountCodes != null && !discountCodes.isEmpty()) {
            return discountCodes.stream().map(DiscountCode::getCode).collect(Collectors.joining(","));
        }
        return null;
    }
}
