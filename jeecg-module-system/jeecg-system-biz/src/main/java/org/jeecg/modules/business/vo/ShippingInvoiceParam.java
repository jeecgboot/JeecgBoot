package org.jeecg.modules.business.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShippingInvoiceParam {

    private final String clientID;
    private final List<String> shopIDs;
    private final String start;
    private final String end;
    private final List<Integer> erpStatuses;
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public ShippingInvoiceParam(@JsonProperty("clientID") String clientID, @JsonProperty("shopIDs") List<String> shopIDs, @JsonProperty("start") String start, @JsonProperty("end") String end, @JsonProperty("erpStatuses") List<Integer> erpStatuses) {
        this.clientID = clientID;
        this.shopIDs = shopIDs;
        this.start = start;
        this.end = end;
        this.erpStatuses = erpStatuses;
    }

    public String clientID() {
        return clientID;
    }

    public List<String> shopIDs() {
        return shopIDs;
    }

    public Date start() throws ParseException {
        return format.parse(start);
    }

    public Date end() throws ParseException {
        return format.parse(end);
    }
    public List<Integer> getErpStatuses() { return erpStatuses; }
    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }
    @Override
    public String toString() {
        return "ShippingInvoiceParam{" + clientID +
                ", shopIDs=" + shopIDs +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
