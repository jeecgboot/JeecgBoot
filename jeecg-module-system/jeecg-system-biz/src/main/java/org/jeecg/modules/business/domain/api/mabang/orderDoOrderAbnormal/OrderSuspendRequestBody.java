package org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
public class OrderSuspendRequestBody implements RequestBody {

    private String platformOrderId;
    private String abnormalLabelName;
    private String description;

    public OrderSuspendRequestBody(String platformOrderId, String abnormalLabelName, String description) {
        this.platformOrderId = platformOrderId;
        this.abnormalLabelName = abnormalLabelName;
        this.description = description;
    }

    @Override
    public String api() {
        return "order-do-order-abnormal";
    }

    @Override
    public Map<String, Object> parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "platformOrderId", platformOrderId);
        putNonNull(json, "abnormal_label_name", abnormalLabelName);
        putNonNull(json, "descr", description);
        return json;
    }
    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }
    private <E, T> void putNonNull(JSONObject json, String key, E value, Function<E, T> mapper) {
        if (value != null) {
            json.put(key, mapper.apply(value));
        }
    }
}
