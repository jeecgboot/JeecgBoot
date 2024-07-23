package com.shop.common.core.enmu;

public enum QQPay {
    EPAY_QQPAY("epay_qqpay");

    QQPay(String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }

    public static boolean getByValue(String value) {
        for (QQPay qqPay : values()) {
            if (qqPay.getCode().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
