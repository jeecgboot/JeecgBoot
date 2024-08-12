package com.vmq.constant;

import com.vmq.entity.PayOrder;
import com.vmq.entity.VmqSetting;
import com.vmq.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;


public enum PayTypeEnum {
    WX(1, "微信"),
    ZFB(2, "支付宝"),
    QQ(3, "QQ"),
    ZSM(4, "微信赞赏码"),
    ZFBTR(5, "支付宝转账");

    @Getter
    @Setter
    int code;

    @Getter
    @Setter
    String name;

    PayTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static boolean isContainsCode(int type) {
        for (PayTypeEnum typeEnum : PayTypeEnum.values()) {
            if (typeEnum.code == type) {
                return true;
            }
        }
        return false;
    }

    public static String getNameByCode(int payType) {
        for (PayTypeEnum typeEnum : PayTypeEnum.values()) {
            if (typeEnum.code == payType) {
                return typeEnum.name;
            }
        }
        return "";
    }

    /**
     * 获取支付链接
     * @param vmqSetting
     * @param payOrder
     * @return
     */
    public static String getPayUrlByOrder(VmqSetting vmqSetting, PayOrder payOrder) {
        int type = payOrder.getType();
        String payUrl = "";
        if (type == 0) {
            return "payPage?orderId="+payOrder.getOrderId();
        }
        if (type == PayTypeEnum.WX.getCode()){ // 收款码>赞赏码
            payUrl = vmqSetting.getWxpay();
            if (StringUtils.isBlank(payUrl)) {
                payUrl = vmqSetting.getWxzspay();
            }
        }else if (type == PayTypeEnum.ZFB.getCode()){ // 收款码>转账
            payUrl = vmqSetting.getZfbpay();
            if (StringUtils.isBlank(payUrl)) {
                payUrl = getAliTransUrl(payOrder, vmqSetting);
            }
        }else if (type == PayTypeEnum.ZSM.getCode()){
            payUrl = vmqSetting.getWxzspay();
        }else if (type == PayTypeEnum.QQ.getCode()){
            payUrl = vmqSetting.getQqpay();
        }else if (type == PayTypeEnum.ZFBTR.getCode()){
            payUrl = getAliTransUrl(payOrder, vmqSetting);
        }
        return payUrl;
    }

    private static String getAliTransUrl(PayOrder payOrder, VmqSetting vmqSetting) {
        String userId = vmqSetting.getAliUserId();
        if (StringUtils.isBlank(userId)) {
            return "";
        }
        double reallyPrice = payOrder.getReallyPrice();
        return String.format(Constant.ALI_TRANS_URL, userId, reallyPrice, "VMQ");
    }
}
