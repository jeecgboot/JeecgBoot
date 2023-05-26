package org.jeecg.modules.business.domain.api.mabang.getorderlist;

public enum DateType {
    UPDATE(1, "updateTime"),
    PAID(2, "paidtime"),
    CREATE(3, "createDate"),
    EXPRESS(4, "expressTime");

    private final int code;
    private final String text;

    DateType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return String.valueOf(code);
    }

    public String text() {
        return text;
    }

    public static DateType fromCode(Integer code) {
        for (DateType dateType : DateType.values()) {
            if (dateType.code == code) {
                return dateType;
            }
        }
        return null;
    }
}
