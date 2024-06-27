package org.jeecg.modules.business.domain.api.yd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class YDTrackingNumberResponse extends YDResponse {

    @JsonProperty("data")
    private YDTrackingNumberData trackingNumberData;

    public YDTrackingNumberResponse(Integer returnValue, String cnMessage, String enMessage, YDTrackingNumberData trackingNumberData) {
        super(returnValue, cnMessage, enMessage);
        this.trackingNumberData = trackingNumberData;
    }

    public YDTrackingNumberResponse() {
    }
}
