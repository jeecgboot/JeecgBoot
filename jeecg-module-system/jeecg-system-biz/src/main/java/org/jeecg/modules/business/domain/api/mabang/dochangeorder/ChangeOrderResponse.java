package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.Response;

public class ChangeOrderResponse extends Response {
    private final String message;
    /**
     * Erp order number
     */
    private final String orderId;


    private ChangeOrderResponse(Code status, String message, String orderId) {
        super(status);
        this.message = message;
        this.orderId = orderId;
    }

    public static ChangeOrderResponse parse(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String code = jsonObject.getString("code");
        String message = jsonObject.getString("message");
        if (code.equals("200")) {
            JSONObject data = jsonObject.getJSONObject("data");
            String orderId = data.getString("orderId");
            return new ChangeOrderResponse(Code.SUCCESS, message, orderId);
        } else {
            return new ChangeOrderResponse(Code.ERROR, message, null);
        }
    }

    public String getMessage() {
        return message;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "ChangeOrderResponse{" +
                "message='" + message + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
