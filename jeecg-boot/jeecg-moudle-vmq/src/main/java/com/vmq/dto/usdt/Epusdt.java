package com.vmq.dto.usdt;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class Epusdt {
    private String order_id;
    private BigDecimal amount;
    private String notify_url;
    private String redirect_url;
    private String signature;
}
