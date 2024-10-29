package org.jeecg.modules.business.domain.api.shouman;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class ShoumanOrderRequest {
    private String platformOrderId;
    private JSONArray remarks;
}