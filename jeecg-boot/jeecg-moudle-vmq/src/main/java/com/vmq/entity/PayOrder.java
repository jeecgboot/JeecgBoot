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
    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(value = "商户订单号",required = true)
    private String payId;

    @ApiModelProperty(value = "自定义参数",required = true)
    private String param;

    @ApiModelProperty("通知邮箱")
    private String email;

    @ApiModelProperty(value = "支付类型：0聚合码，1微信，2支付宝，3QQ，4微信赞赏码，5支付宝转账",required = true)
    private Integer type;

    @ApiModelProperty(value = "订单价格",required = true)
    private double price;

    @ApiModelProperty("异步通知地址")
    private String notifyUrl;

    @ApiModelProperty("同步通知地址")
    private String returnUrl;

    @Transient
    @ApiModelProperty(value = "md5签名",required = true)
    private String sign;

    @Transient
    @ApiModelProperty("返回类型：0JSON，1页面")
    private int isHtml = 1;

    @ApiModelProperty(value = "商户账号",hidden = true)
    private String username;

    @ApiModelProperty(hidden = true)
    private String orderId;

    @ApiModelProperty(hidden = true)
    private double reallyPrice;

    // -1：订单过期 0：等待支付 1：支付成功
    @ApiModelProperty(value = "订单状态",hidden = true)
    private Integer state;

    @ApiModelProperty(value = "二维码类型：1手动输入金额 0固定金额",hidden = true)
    private int isAuto;

    @ApiModelProperty(value = "付款通道:vmq、epay、epusdt")
    private String payChannel = "vmq";

    @ApiModelProperty(hidden = true)
    private Long payCodeId;

    @ApiModelProperty(hidden = true)
    private String payUrl;

    @ApiModelProperty(hidden = true)
    private long createDate;

    @ApiModelProperty(hidden = true)
    private long payDate;

    @ApiModelProperty(hidden = true)
    private long closeDate;

    public String getUnitCode() {
        return username + type + reallyPrice;
    }

    public String getMd5ForCreate() {
        return payId + param + type + price;
    }

    public String getMd5ForNotify() {
        return payId + param + type + price + reallyPrice;
    }

}
