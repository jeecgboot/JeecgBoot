package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.OrderStatus;

public class ArchiveOrderRequestBody implements RequestBody {

    private String platformOrderId;

    public ArchiveOrderRequestBody(String platformOrderId) {
        this.platformOrderId = platformOrderId;
    }

    @Override
    public String api() {
        return "order-do-change-order";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "platformOrderId", platformOrderId);
        putNonNull(json, "orderStatus", OrderStatus.Completed.getCode());
        return json;
    }

    public String getPlatformOrderId() {
        return platformOrderId;
    }

    public void setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
    }

    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }
}
