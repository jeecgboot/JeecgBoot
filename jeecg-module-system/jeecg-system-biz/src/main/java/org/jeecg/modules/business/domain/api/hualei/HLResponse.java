package org.jeecg.modules.business.domain.api.hualei;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.jt.JTResponseItem;

import java.util.List;

@Slf4j
@Data
public class HLResponse {

    @JsonProperty("ack")
    private Boolean acknowledgment;

    @JsonProperty("data")
    private List<HLResponseItem> responseItems;

    public HLResponse(Boolean acknowledgment, List<HLResponseItem> responseItems) {
        this.acknowledgment = acknowledgment;
        this.responseItems = responseItems;
    }

    public HLResponse() {
    }
}
