package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.util.HashSet;

@Data
public class EditLogisticRequestBody implements RequestBody {


    private String platformOrderId;
    private String logisticChannelName;
    /**
     *  1 : edit, 2: delete, 3: add
     */
    private Integer type;

    public EditLogisticRequestBody(String platformOrderId, String logisticChannelName, int type) {
        this.platformOrderId = platformOrderId;
        this.logisticChannelName = logisticChannelName;
        this.type = type;
    }

    @Override
    public String api() {
        return "order-do-change-order";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "platformOrderId", platformOrderId);
        putNonNull(json, "myLogisticsId", logisticChannelName);
        putNonNull(json, "type", type);
        return json;
    }

    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }
}
