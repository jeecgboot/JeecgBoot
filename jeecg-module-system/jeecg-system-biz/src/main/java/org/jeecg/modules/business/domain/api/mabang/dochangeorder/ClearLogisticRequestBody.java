package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

@Data
public class ClearLogisticRequestBody implements RequestBody {

    private String platformOrderId;

    public ClearLogisticRequestBody(String platformOrderId) {
        this.platformOrderId = platformOrderId;
    }

    @Override
    public String api() {
        return "order-do-order-logistics";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "platformOrderId", platformOrderId);
        putNonNull(json, "type", 1);
        return json;
    }

    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }
}
