package org.jeecg.modules.business.domain.api.yd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class YDTrackingNumberData {

    @JsonProperty("refrence_no")
    private String platformOrderId;

    @JsonProperty("channel_hawbcode")
    private String localTrackingNumber;

    public YDTrackingNumberData(String platformOrderId, String localTrackingNumber) {
        this.platformOrderId = platformOrderId;
        this.localTrackingNumber = localTrackingNumber;
    }

    public YDTrackingNumberData() {
    }
}
