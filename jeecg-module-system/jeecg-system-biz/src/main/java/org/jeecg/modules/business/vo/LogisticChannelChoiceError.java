package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class LogisticChannelChoiceError {
    private String shop;
    private String orderId;
    private String country;
    private String sensitiveAttribute;

    public LogisticChannelChoiceError(String shop, String orderId, String country, String sensitiveAttribute) {
        this.shop = shop;
        this.orderId = orderId;
        this.country = country;
        this.sensitiveAttribute = sensitiveAttribute;
    }
}
