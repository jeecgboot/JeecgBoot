package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ManualInvoiceOrderParam {
    private final String clientID;
    private final List<String> orderIds;
    private final String type;
    private final List<String> ordersWithStock = new ArrayList<>();
    public ManualInvoiceOrderParam(@JsonProperty("clientID") String clientID, @JsonProperty("orderIds") List<String> orderIds, @JsonProperty("type") String type, @JsonProperty("ordersWithStock") List<String> ordersWithStock) {
        this.clientID = clientID;
        this.orderIds = orderIds;
        this.type = type;
        if (ordersWithStock != null) {
            this.ordersWithStock.addAll(ordersWithStock);
        }
    }
}
