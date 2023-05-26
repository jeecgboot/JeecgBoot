package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuChannelHistory {
    private final String englishChannelName;

    private final String chineseChannelName;

    private final String englishCountryName;

    private final String sku;

    private final SkuPriceHistory now;

    private final SkuPriceHistory previous;

}
