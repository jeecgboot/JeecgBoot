package org.jeecg.modules.business.domain.api.mabang.orderUpdateOrderNewOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.jeecg.modules.business.domain.api.mabang.Response;

import java.util.List;

@Getter
public class OrderUpdateOrderNewOrderResponse extends Response {
    private final String message;
    private final List<UpdateResult> ordersNotFound;
    private final List<UpdateResult> successOrders;


    private OrderUpdateOrderNewOrderResponse(Code status, String message, List<UpdateResult> ordersNotFound, List<UpdateResult> successOrders) {
        super(status);
        this.message = message;
        this.ordersNotFound = ordersNotFound;
        this.successOrders = successOrders;
    }

    public static OrderUpdateOrderNewOrderResponse parse(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String code = jsonObject.getString("code");
        String message = jsonObject.getString("message");
        if (code.equals("200")) {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray notFoundPlatformOrderIds = data.getJSONArray("notFoundPlatformOrderIds");
            List<UpdateResult> notFoundPlatformOrderIdsList = notFoundPlatformOrderIds.toJavaList(UpdateResult.class);
            JSONArray platformOrderIdsForSuccess = data.getJSONArray("platformOrderIdsForSuccess");
            List<UpdateResult> platformOrderIdsForSuccessList = platformOrderIdsForSuccess.toJavaList(UpdateResult.class);
            return new OrderUpdateOrderNewOrderResponse(Code.SUCCESS, message, notFoundPlatformOrderIdsList, platformOrderIdsForSuccessList);
        } else {
            return new OrderUpdateOrderNewOrderResponse(Code.ERROR, message, null, null);
        }
    }
    @Override
    public String toString() {
        return "AddPurchaseOrderResponse{" +
                "message='" + message + '\'' +
                ", order not found='" + ordersNotFound + '\'' +
                ", success orders='" + successOrders + '\'' +
                '}';
    }
}