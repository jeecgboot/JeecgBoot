package org.jeecg.modules.business.domain.api.mabang.getorderlist;

public enum OrderStatus {
    Pending(1, "待处理"),
    Preparing(2, "配货中"),
    Shipped(3, "已发货"),
    Completed(4, "已完成"),
    Obsolete(5, "已作废"),
    AllUnshipped(6, "所有未发货"),
    AllNonUnshipped(7, "所有非未发货");

    private final int code;
    private final String name;

    OrderStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return String.valueOf(code);
    }

    public static OrderStatus fromCode(Integer code) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.code == code) {
                return orderStatus;
            }
        }
        return null;
    }
}
