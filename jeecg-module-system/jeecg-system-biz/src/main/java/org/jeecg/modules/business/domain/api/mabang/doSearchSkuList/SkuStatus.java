package org.jeecg.modules.business.domain.api.mabang.doSearchSkuList;

public enum SkuStatus {
    //"status": "状态:1.自动创建;2.待开发;3.正常;4.清仓;5.停止销售",

    AutomaticallyCreated(1, "自动创建"),
    ToBePaired(2, "待开发"),
    Normal(3, "正常"),
    Liquidation(4, "清仓"),
    StoppedSelling(5, "停止销售");

    private final int code;
    private final String name;

    SkuStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return String.valueOf(code);
    }

    public static SkuStatus fromCode(Integer code) {
        for (SkuStatus skuStatus : SkuStatus.values()) {
            if (skuStatus.code == code) {
                return skuStatus;
            }
        }
        return null;
    }
}
