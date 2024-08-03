package com.vmq.constant;

import lombok.Getter;
import lombok.Setter;


public enum SmsTypeEnum {

    WX("com.tencent.mm", "微信"),
    ZFB("com.eg.android.AlipayGphone", "支付宝"),
    QQ("com.tencent.mobileqq", "QQ");

    @Getter
    @Setter
    String source;

    @Getter
    @Setter
    String name;

    SmsTypeEnum(String code, String name) {
        this.source = code;
        name = name;
    }


    public static String getNameBySource(String source) {
        for (SmsTypeEnum typeEnum : SmsTypeEnum.values()) {
            if (typeEnum.source.equals(source)) {
                return typeEnum.name;
            }
        }
        return "";
    }
}
