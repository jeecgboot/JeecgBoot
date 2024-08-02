package com.vmq.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("创建订单返回VO")
public class CreateOrderRes {
    @ApiModelProperty("商户订单号")
    private String payId;
    @ApiModelProperty("云端订单号")
    private String orderId;
    @ApiModelProperty("支付类型：1微信，2支付宝，3赞赏码")
    private int payType;
    @ApiModelProperty("订单价格")
    private double price;
    @ApiModelProperty("实际支付价格")
    private double reallyPrice;
    @ApiModelProperty("支付跳转链接")
    private String payUrl;
    @ApiModelProperty("二维码类型：1手动输入金额，0固定金额")
    private int isAuto;
    @ApiModelProperty("订单状态")
    private int state;
    @ApiModelProperty("订单有效时间")
    private int timeOut;
    @ApiModelProperty("创建时间戳")
    private long date;

    public CreateOrderRes(String payId, String orderId, int payType, double price, double reallyPrice, String payUrl, int isAuto, int state, int timeOut, long date) {
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
