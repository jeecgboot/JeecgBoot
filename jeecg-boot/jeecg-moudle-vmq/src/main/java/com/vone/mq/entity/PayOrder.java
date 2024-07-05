package com.vone.mq.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PayOrder {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    //支付云端唯一订单号
    private String orderId;

    //支付商户订单号
    private String payId;


    //创建时间
    private long createDate;
    //支付时间
    private long payDate;
    //关闭时间
    private long closeDate;


    //订单自定义参数，会原封返回给异步接口和同步接口
    private String param;

    //支付类型 1：微信 2：支付宝
    private int type;

    //订单价格
    private double price;

    //实际支付价格
    private double reallyPrice;

    //异步地址
    private String notifyUrl;
    //支付完成后跳转地址
    private String returnUrl;

    //订单状态  -1：订单过期 0：等待支付 1：支付成功
    private int state;

    //是否为通用二维码，1为通用二维码 0为固定转账二维码
    private int isAuto;
    //二维码内容
    private String payUrl;

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getPayDate() {
        return payDate;
    }

    public void setPayDate(long payDate) {
        this.payDate = payDate;
    }

    public long getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(long closeDate) {
        this.closeDate = closeDate;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getReallyPrice() {
        return reallyPrice;
    }

    public void setReallyPrice(double reallyPrice) {
        this.reallyPrice = reallyPrice;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(int isAuto) {
        this.isAuto = isAuto;
    }

    @Override
    public String toString() {
        return "PayOrder{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", createDate=" + createDate +
                ", payDate=" + payDate +
                ", closeDate=" + closeDate +
                ", param='" + param + '\'' +
                ", type=" + type +
                ", price=" + price +
                ", reallyPrice=" + reallyPrice +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", state=" + state +
                '}';
    }
}
