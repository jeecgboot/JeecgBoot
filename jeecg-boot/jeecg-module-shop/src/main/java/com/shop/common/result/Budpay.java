package com.shop.common.result;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Budpay {
    private String mch_id;
    private String trade_no;
    private String out_trade_no;
    private String amount;
    private String pay_amount;
    private String name;
    private String status;
    private String pay_type;
    private String sign;
}
