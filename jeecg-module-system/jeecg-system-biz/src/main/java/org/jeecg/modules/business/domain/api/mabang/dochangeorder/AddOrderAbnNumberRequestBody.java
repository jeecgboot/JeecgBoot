package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

public class AddOrderAbnNumberRequestBody implements RequestBody {

    private String platformOrderId;
    private final String abnNumber;

    // 韩国税号已添加在马帮系统中的ID是1042522
    private static final String KOREAN_TAX_NUMBER_LABEL_ID = "1042522";

    public AddOrderAbnNumberRequestBody(String platformOrderId, String abnNumber) {
        this.platformOrderId = platformOrderId;
        this.abnNumber = abnNumber;
    }

    @Override
    public String api() {
        return "order-do-change-order";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "platformOrderId", platformOrderId);
        putNonNull(json, "abnnumber", abnNumber);
        JSONArray labelId = new JSONArray();
        labelId.add(KOREAN_TAX_NUMBER_LABEL_ID);
        putNonNull(json, "labelId", labelId);
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
