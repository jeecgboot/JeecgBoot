package org.jeecg.modules.business.domain.api.shopify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class DiscountCode{
    @JsonProperty("code")
    private String code;

    public DiscountCode() {}
}