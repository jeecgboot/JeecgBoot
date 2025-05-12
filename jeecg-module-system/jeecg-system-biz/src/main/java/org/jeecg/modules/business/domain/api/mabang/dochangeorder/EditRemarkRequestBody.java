package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.util.Map;

@Getter
public class EditRemarkRequestBody implements RequestBody {
    private final String platformOrderId;
    private final String remark;

    public EditRemarkRequestBody(String platformOrderId, String remark) {
        this.platformOrderId = platformOrderId;
        this.remark = remark;
    }

    @Override
    public String api() {
        return "order-do-change-order";
    }

    @Override
    public Map<String, Object> parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "platformOrderId", platformOrderId);
        putNonNull(json, "remark", remark);
        return json;
    }
    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }
}
