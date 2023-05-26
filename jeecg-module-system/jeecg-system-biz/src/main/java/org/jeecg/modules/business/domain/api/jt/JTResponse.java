package org.jeecg.modules.business.domain.api.jt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class JTResponse {

    @JsonProperty("logisticproviderid")
    private String logisticProviderId;

    @JsonProperty("responseitems")
    private List<JTResponseItem> responseItems;

    public JTResponse(String logisticProviderId, List<JTResponseItem> responseItems) {
        this.logisticProviderId = logisticProviderId;
        this.responseItems = responseItems;
    }

    public JTResponse() {
    }
}
