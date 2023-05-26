package org.jeecg.modules.business.controller.admin.shippingInvoice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PeriodRequestParam {
    @JsonProperty(value = "shops")
    private final List<String> shopIDs;

    public PeriodRequestParam( List<String> shopIDs) {
        this.shopIDs = shopIDs;
    }

    public List<String> shopIDs() {
        return shopIDs;
    }


    @Override
    public String toString() {
        return "PeriodRequestParam{" +
                ", shopIDs=" + shopIDs +
                '}';
    }
}
