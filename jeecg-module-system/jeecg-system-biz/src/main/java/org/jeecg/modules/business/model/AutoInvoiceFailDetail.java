package org.jeecg.modules.business.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AutoInvoiceFailDetail {
    private String clientCode;
    private String shopName;
    private String invoiceCode;
    private Level level;
    private String platformOrderId;
    private String sku;
    private Step step;
    private String errorReason;
    private LocalDateTime time;

    public enum Step {
        PRE_CHECK,
        CALCULATE,
        CREATE_INVOICE,
        APPROVE_INVOICE,
        EDIT_ORDER_REMARK,
    }
    public enum Level {
        CLIENT,
        SHOP,
        ORDER,
        SKU
    }
}
