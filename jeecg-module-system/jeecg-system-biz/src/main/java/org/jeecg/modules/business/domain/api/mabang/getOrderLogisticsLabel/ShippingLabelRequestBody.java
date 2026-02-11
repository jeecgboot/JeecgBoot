package org.jeecg.modules.business.domain.api.mabang.getOrderLogisticsLabel;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;

import java.util.Map;

@Getter
@Setter
public class ShippingLabelRequestBody implements RequestBody {

    private String platformOrderId;
    private static String CALL_BACK_URL = "https://app.wia-sourcing.com/app/wia/shippingLabelCallback";
    private static String CALL_BACK_URL_DEV = "http://82.96.170.75:8080/jeecg-boot/wia/shippingLabelCallback";

    public ShippingLabelRequestBody(String platformOrderId) {
        this.platformOrderId = platformOrderId;
    }

    @Override
    public String api() {
        return "wl-get-order-logistics-label";
    }

    @Override
    public Map<String, Object> parameters() {
        JSONObject json = new JSONObject();
        putNonNull(json, "platformOrderId", platformOrderId);
        putNonNull(json, "callbackurl", CALL_BACK_URL_DEV);
        return json;
    }

    private <E> void putNonNull(JSONObject json, String key, E value) {
        if (value != null) {
            json.put(key, value);
        }
    }
}
