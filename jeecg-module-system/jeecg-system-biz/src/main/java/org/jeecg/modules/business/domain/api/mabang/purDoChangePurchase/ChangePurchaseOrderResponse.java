package org.jeecg.modules.business.domain.api.mabang.purDoChangePurchase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.jeecg.modules.business.domain.api.mabang.Response;

@Getter
public class ChangePurchaseOrderResponse extends Response {
    private final String message;


    private ChangePurchaseOrderResponse(Code status, String message) {
        super(status);
        this.message = message;
    }

    public static ChangePurchaseOrderResponse parse(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String code = jsonObject.getString("code");
        String message = jsonObject.getString("message");
        if (code.equals("200")) {
            return new ChangePurchaseOrderResponse(Code.SUCCESS, message);
        } else {
            return new ChangePurchaseOrderResponse(Code.ERROR, message);
        }
    }
    @Override
    public String toString() {
        return "AddPurchaseOrderResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
