package org.jeecg.modules.business.domain.api.mabang.orderDoDeliverOrder;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;
import org.jeecg.modules.business.entity.PlatformOrder;

import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
public class OrderDeliverRequestBody implements RequestBody {

    private String platformOrderId;

    public OrderDeliverRequestBody(PlatformOrder platformOrder) {
        this.platformOrderId = platformOrder.getPlatformOrderId();
    }

    @Override
    public String api() {
        return "order-do-deliver-order";
    }

    @Override
    public Map<String, Object> parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "platformOrderId", platformOrderId);
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
