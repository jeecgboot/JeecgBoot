package org.jeecg.modules.business.domain.api.mabang.dochangeorder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jeecg.modules.business.domain.api.mabang.RequestBody;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.Order;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.OrderItem;
import org.jeecg.modules.business.vo.PlatformOrderOperation;

import java.util.function.Function;

import static org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderRequestBody.OperationType;
@Getter
@Setter
public class ChangeWarehouseRequestBody implements RequestBody {
    private Order order;
    private String platformOrderId;
    private String warehouseName;
    private String recipient;
    private String street1;
    private String street2;
    private String phone;

    public ChangeWarehouseRequestBody(Order order, String warehouseName) {
        this.platformOrderId = order.getPlatformOrderId();
        this.warehouseName = warehouseName;
        this.recipient = order.getRecipient();
        this.street1 = order.getAddress();
        this.street2 = order.getAddress2();
        this.phone = order.getPhone1();
        this.order = order;
    }
    public ChangeWarehouseRequestBody(PlatformOrderOperation order) {
        this.setOrderId(order.getOrderIds());
        this.recipient = order.getRecipient();
        this.street1 = order.getStreet1();
        this.street2 = order.getStreet2();
        this.phone = order.getPhone();
    }

    @Override
    public String api() {
        return "order-do-change-order";
    }

    @Override
    public JSONObject parameters() {
        JSONObject json = new JSONObject();
        JSONArray stockDataArray = new JSONArray();
        putNonNull(json, "platformOrderId", platformOrderId);
        putNonNull(json, "buyerName", recipient);
        putNonNull(json, "street1", street1);
        putNonNull(json, "street2", street2);
        putNonNull(json, "phone1", phone);
        if(order == null)
            return json;
        if(order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            for(OrderItem orderItem : order.getOrderItems()) {
                if(!isSkuValidFormat(orderItem.getErpCode()))
                    continue;
                JSONObject stockData = new JSONObject();
                stockData.put("type", OperationType.MODIFY.getCode());
                stockData.put("stockSku", orderItem.getErpCode());
                stockData.put("warehouseName", warehouseName);
                stockData.put("erpOrderItemId", orderItem.getErpOrderItemId());
                stockDataArray.add(stockData);
            }
        }
        putNonNull(json, "stockData", stockDataArray.toJSONString());
        return json;
    }
    private boolean isSkuValidFormat(String sku) {
        return sku != null && !sku.matches("^[0-9]+$");
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
    public void setOrderId(String orderId) {
        // clean string from spaces, comas and quotes
        this.platformOrderId = orderId.replaceAll("[,\\s\"]", "");
    }
}
