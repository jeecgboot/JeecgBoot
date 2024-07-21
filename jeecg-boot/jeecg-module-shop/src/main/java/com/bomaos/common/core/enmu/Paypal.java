package com.bomaos.common.core.enmu;

public enum Paypal {
    PAYPAL("paypal");

    Paypal(String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }

    public static boolean getByValue(String value) {
        for (Paypal paypal : values()) {
            if (paypal.getCode().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
