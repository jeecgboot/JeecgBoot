package com.shop.common.core.enmu;

public enum USDT {
    EPUSDT("epusdt");

    USDT(String code) {
        this.code = code;
    }

    private final String code;

    public String getCode() {
        return code;
    }

    public static boolean getByValue(String value) {
        for (USDT usdt : values()) {
            if (usdt.getCode().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
