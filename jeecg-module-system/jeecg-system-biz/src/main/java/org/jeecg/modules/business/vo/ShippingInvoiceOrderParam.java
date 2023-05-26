package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ShippingInvoiceOrderParam {
    private final String clientID;
    private final List<String> orderIds;
    private final String type;
    private static String start = "";
    private static String end = "";
    private static String[] period = null;
    public ShippingInvoiceOrderParam(@JsonProperty("clientID") String clientID, @JsonProperty("orderIds") List<String> orderIds, @JsonProperty("type") String type, @JsonProperty("period") String ... period) {
        this.clientID = clientID;
        this.orderIds = orderIds;
        this.type = type;
        if(period != null && period.length > 0) {
            start = period[0].split(",")[0];
            end = period[0].split(",")[1];
        }
        ShippingInvoiceOrderParam.period = period;
    }

    public String clientID() {
        return clientID;
    }
    public List<String> orderIds() {
        return orderIds;
    }
    public String getType() { return this.type; }
    public String getStart() { return start; }
    public String getEnd() { return end; }

}
