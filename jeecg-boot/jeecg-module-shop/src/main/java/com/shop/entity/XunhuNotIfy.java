package com.shop.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class XunhuNotIfy {
    /**
     * 商户订单号
     */
    private String trade_order_id;
    /**
     * 订单支付金额
     */
    private BigDecimal total_fee;
    /**
     * 交易号
     */
    private String transaction_id;
    /**
     * 虎皮椒内部订单号
     */
    private String open_order_id;
    /**
     * 订单标题
     */
    private String order_title;
    /**
     * 订单状态
     */
    private String status;
    /**
     * 插件ID
     */
    private String plugins;
    /**
     * 支付渠道ID
     */
    private String appid;
    /**
     * 时间戳
     */
    private String time;
    /**
     * 随即字符串
     */
    private String nonce_str;
    /**
     * 签名
     */
    private String hash;

    /**
     * 用户名
     */
    private String username;
}
