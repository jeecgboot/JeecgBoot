package com.vmq.constant;

import lombok.Getter;
import lombok.Setter;


public enum PayChannelEnum {
    VMQ("vmq", "码支付"),
    EPAY("epay", "易支付"),
    EPUSDT("epusdt", "usdt");

    @Getter
    @Setter
    String code;

    @Getter
    @Setter
    String name;

    PayChannelEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
