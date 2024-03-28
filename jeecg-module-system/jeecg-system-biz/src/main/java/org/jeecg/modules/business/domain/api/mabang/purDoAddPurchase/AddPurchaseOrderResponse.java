package org.jeecg.modules.business.domain.api.mabang.purDoAddPurchase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.jeecg.modules.business.domain.api.mabang.Response;

@Getter
public class AddPurchaseOrderResponse extends Response {
    /**采购批次号*/
    private final String groupId;
    private final String message;


    private AddPurchaseOrderResponse(Code status, String message, String groupId) {
        super(status);
        this.message = message;
        this.groupId = groupId;
    }

    public static AddPurchaseOrderResponse parse(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String code = jsonObject.getString("code");
        String message = jsonObject.getString("message");
        if (code.equals("200")) {
            JSONObject data = jsonObject.getJSONObject("data");
            String groupId = data.getString("groupId");
            return new AddPurchaseOrderResponse(Code.SUCCESS, message, groupId);
        } else {
            return new AddPurchaseOrderResponse(Code.ERROR, message, null);
        }
    }
    @Override
    public String toString() {
        return "AddPurchaseOrderResponse{" +
                "message='" + message + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
