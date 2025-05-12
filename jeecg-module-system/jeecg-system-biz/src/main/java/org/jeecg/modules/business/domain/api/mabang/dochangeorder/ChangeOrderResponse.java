package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.jeecg.modules.business.domain.api.mabang.Response;

@Getter
public class ChangeOrderResponse extends Response {
    private final String message;
    /**
     * Erp order number
     */
    private final String orderId;
    private final String platformOrderId;


    private ChangeOrderResponse(Code status, String message, String orderId, String platformOrderId) {
        super(status);
        this.message = message;
        this.orderId = orderId;
        this.platformOrderId = platformOrderId;
    }

    public static ChangeOrderResponse parse(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String code = jsonObject.getString("code");
        String message = jsonObject.getString("message");
        if (code.equals("200")) {
            JSONObject data = jsonObject.getJSONObject("data");
            String orderId = data.getString("orderId");
            String platformOrderId = data.getString("platformOrderId");
            return new ChangeOrderResponse(Code.SUCCESS, message, orderId, platformOrderId);
        } else {
            return new ChangeOrderResponse(Code.ERROR, message, null, null);
        }
    }

    @Override
    public String toString() {
        return "ChangeOrderResponse{" +
                "message='" + message + '\'' +
                ", orderId='" + orderId + '\'' +
                ", platformOrderId='" + platformOrderId + '\'' +
                '}';
    }
}
