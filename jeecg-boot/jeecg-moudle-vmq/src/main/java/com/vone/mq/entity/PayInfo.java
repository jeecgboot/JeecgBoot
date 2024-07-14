package com.vone.mq.entity;

import com.vone.mq.utils.StringUtils;
import lombok.Data;

@Data
public class PayInfo {

    private String username;

    //支付云端唯一订单号
    private String orderId;

    //支付商户订单号
    private String payId;

    //支付类型 1：微信 2：支付宝
    private String type;

    //订单价格
    private double price;

    //实际支付价格
    private double reallyPrice;

    private String passUrl;

    private String backUrl;

    private String delUrl;

    //创建时间
    private String createDate;

    //支付时间
    private String payDate;

    public PayInfo(PayOrder order) {
        this.username = order.getUsername();
        this.orderId = order.getOrderId();
        this.payId = order.getPayId();
        this.type = order.getType() == 2 ? "支付宝" : "微信";
        this.price = order.getPrice();
        this.reallyPrice = order.getReallyPrice();
        this.createDate = StringUtils.format(order.getCreateDate(), "yyyy-MM-dd HH:mm:ss");
        this.payDate = StringUtils.format(order.getPayDate(), "yyyy-MM-dd HH:mm:ss");
    }
}
