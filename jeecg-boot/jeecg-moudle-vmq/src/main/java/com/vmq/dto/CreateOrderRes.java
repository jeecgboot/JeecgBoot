package com.vmq.dto;

import lombok.Data;

@Data
public class CreateOrderRes {
    private String payId;
    private String orderId;
    private int payType;
    private String price;
    private String reallyPrice;
    private String payUrl;
    private int isAuto;
    private int state;
    private int timeOut;
    private long date;

    public CreateOrderRes(String payId, String orderId, int payType, String price, String reallyPrice, String payUrl, int isAuto, int state, int timeOut, long date) {
        this.payId = payId;
        this.orderId = orderId;
        this.payType = payType;
        this.price = price;
        this.reallyPrice = reallyPrice;
        this.payUrl = payUrl;
        this.isAuto = isAuto;
        this.state = state;
        this.timeOut = timeOut;
        this.date = date;
    }
}
