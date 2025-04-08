package org.jeecg.modules.business.domain.api.mabang.orderUpdateOrderNewOrder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Updates order from erp status 1 to 2
 */
public class OrderUpdateOrderNewOrderRequestBody implements RequestBody {
    private final List<String> platformOrderIds;

    public OrderUpdateOrderNewOrderRequestBody(List<String> platformOrderIds) {
        this.platformOrderIds = platformOrderIds;
    }

    @Override
    public String api() {
        return "order-update-order-new-order";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        JSONArray platformOrderIdsArray = new JSONArray();
        if (platformOrderIds != null) {
            platformOrderIdsArray.addAll(platformOrderIds);
        } else {
            platformOrderIdsArray = new JSONArray(Collections.emptyList());
        }
        putNonNull(json,"platformOrderIds", platformOrderIdsArray);
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