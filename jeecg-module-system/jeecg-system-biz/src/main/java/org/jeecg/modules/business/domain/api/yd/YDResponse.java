package org.jeecg.modules.business.domain.api.yd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class YDResponse {

    /**
     * 返回值
     */
    @JsonProperty("success")
    protected Integer returnValue;

    /**
     * 返回中文信息
     */
    @JsonProperty("cnmessage")
    protected String cnMessage;

    /**
     * 返回英文信息
     */
    @JsonProperty("enmessage")
    protected String enMessage;

    public YDResponse(Integer returnValue, String cnMessage, String enMessage) {
        this.returnValue = returnValue;
        this.cnMessage = cnMessage;
        this.enMessage = enMessage;
    }

    public YDResponse() {
    }
}
