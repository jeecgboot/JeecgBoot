package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Data
public class ShippingInvoiceParam {

    private final String clientID;
    private final BigDecimal balance;
    private final List<String> shopIDs;
    private final String start;
    private final String end;
    private final List<Integer> erpStatuses;
    private final List<String> warehouses;
    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public ShippingInvoiceParam(@JsonProperty("clientID") String clientID,
                                @JsonProperty("balance") BigDecimal balance,
                                @JsonProperty("shopIDs") List<String> shopIDs,
                                @JsonProperty("start") String start,
                                @JsonProperty("end") String end,
                                @JsonProperty("erpStatuses") List<Integer> erpStatuses,
                                @JsonProperty("warehouses") List<String> warehouses) {
        this.clientID = clientID;
        this.balance = balance;
        this.shopIDs = shopIDs;
        this.start = start;
        this.end = end;
        this.erpStatuses = erpStatuses;
        this.warehouses = warehouses;
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
    @Override
    public String toString() {
        return "ShippingInvoiceParam{" + clientID +
                ", shopIDs=" + shopIDs +
                ", start=" + start +
                ", end=" + end +
                ", end=" + erpStatuses +
                ", warehouses=" + warehouses +
                '}';
    }
}
