package org.jeecg.modules.business.domain.api.yd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class YDParcelTraceResponse extends YDResponse {

    @JsonProperty("data")
    private List<YDTraceData> traceDataList;

    public YDParcelTraceResponse(Integer returnValue, String cnMessage, String enMessage, List<YDTraceData> traceDataList) {
        super(returnValue, cnMessage, enMessage);
        this.traceDataList = traceDataList;
    }

    public YDParcelTraceResponse() {
    }
}
