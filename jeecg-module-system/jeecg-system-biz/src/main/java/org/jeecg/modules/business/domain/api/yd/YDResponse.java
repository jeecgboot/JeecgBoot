package org.jeecg.modules.business.domain.api.yd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class YDResponse {

    /**
     * 返回值
     */
    @JsonProperty("success")
    private Integer returnValue;

    /**
     * 返回中文信息
     */
    @JsonProperty("cnmessage")
    private String cnMessage;

    /**
     * 返回英文信息
     */
    @JsonProperty("enmessage")
    private String enMessage;

    @JsonProperty("data")
    private List<YDTraceData> traceDataList;

    public YDResponse(Integer returnValue, String cnMessage, String enMessage, List<YDTraceData> traceDataList) {
        this.returnValue = returnValue;
        this.cnMessage = cnMessage;
        this.enMessage = enMessage;
        this.traceDataList = traceDataList;
    }

    public YDResponse() {
    }
}
