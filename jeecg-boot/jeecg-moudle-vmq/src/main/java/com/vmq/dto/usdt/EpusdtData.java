package com.vmq.dto.usdt;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EpusdtData {
    private String trade_id;
    private String order_id;
    private Float amount;
    private Float actual_amount;
    private String token;
    private Integer expiration_time;
    private String payment_url;
}
