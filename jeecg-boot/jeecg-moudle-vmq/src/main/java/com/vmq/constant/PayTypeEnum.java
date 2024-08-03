package com.vmq.constant;

import lombok.Getter;
import lombok.Setter;


public enum PayTypeEnum {

    WX(1, "微信"),
    ZFB(2, "支付宝"),
    ZSM(3, "赞赏码"),
    QQ(4, "QQ");

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
}
