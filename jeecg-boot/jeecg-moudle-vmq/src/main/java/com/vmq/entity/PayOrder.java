package com.vmq.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@ApiModel("订单信息")
public class PayOrder {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("账号")
    private String username;

    @ApiModelProperty("云端订单号")
    private String orderId;

    @ApiModelProperty("商户订单号")
    private String payId;

    @ApiModelProperty("自定义参数")
    private String param;

    @ApiModelProperty("通知邮箱")
    private String email;

    @ApiModelProperty("支付类型：1微信 2支付宝")
    private int type;

    @ApiModelProperty("订单价格")
    private String price;

    @ApiModelProperty("实际支付价格")
    private String reallyPrice;

    @ApiModelProperty("异步通知地址")
    private String notifyUrl;

    @ApiModelProperty("同步通知地址")
    private String returnUrl;

    // -1：订单过期 0：等待支付 1：支付成功
    @ApiModelProperty("订单状态")
    private int state;

    @ApiModelProperty("二维码类型：1手动输入金额 0固定金额")
    private int isAuto;

    @ApiModelProperty("支付跳转链接")
    private String payUrl;

    @ApiModelProperty("创建时间")
    private long createDate;

    @ApiModelProperty("支付时间")
    private long payDate;

    @ApiModelProperty("关闭时间")
    private long closeDate;

    @Transient
    private String sign;

    @Transient
    private int isHtml = 1;
}
