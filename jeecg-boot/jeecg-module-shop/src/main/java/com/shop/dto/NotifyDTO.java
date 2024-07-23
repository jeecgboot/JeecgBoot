package com.shop.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Author chengtianqi
 * @create 2020/8/28 17:13
 */
@Data
@ToString
public class NotifyDTO {

    // 1：支付成功
    private String return_code;
    // 金额。单位：分
    private String total_fee;
    // 用户端自主生成的订单号
    private String out_trade_no;
    // PAYJS 订单号
    private String payjs_order_id;
    // 微信用户手机显示订单号
    private String transaction_id;
    // 支付成功时间
    private String time_end;
    //
    private String openid;
    // 用户自定义数据
    private String attach;
    // 商户号
    private String mchid;
    //
    private String type;

    private String sign;
}
