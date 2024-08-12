package com.vmq.dto;

import com.vmq.constant.PayTypeEnum;
import com.vmq.entity.PayOrder;
import com.vmq.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("邮件通知VO")
public class PayInfo {

    private String username;

    //支付云端唯一订单号
    private String orderId;

    //支付商户订单号
    private String payId;

    //支付类型 1微信，2支付宝，3QQ，4微信赞赏码，5支付宝转账
    private String type;

    //订单价格
    private String price;

    //实际支付价格
    private String reallyPrice;

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
        this.type = PayTypeEnum.getNameByCode(order.getType());
        this.price = StringUtils.getPriceStr(order.getPrice());
        this.reallyPrice = StringUtils.getPriceStr(order.getReallyPrice());
        this.createDate = StringUtils.format(order.getCreateDate(), "yyyy-MM-dd HH:mm:ss");
        this.payDate = StringUtils.format(order.getPayDate(), "yyyy-MM-dd HH:mm:ss");
    }
}
