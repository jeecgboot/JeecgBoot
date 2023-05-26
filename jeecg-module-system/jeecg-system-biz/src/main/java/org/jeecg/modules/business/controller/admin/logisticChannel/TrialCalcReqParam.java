package org.jeecg.modules.business.controller.admin.logisticChannel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jeecg.modules.business.vo.SkuQuantity;

import java.util.List;

@Data
public class TrialCalcReqParam {
    @JsonProperty("country")
    private final String countryCode;

    @JsonProperty("skuList")
    private final List<SkuQuantity> skuQuantities;
}
