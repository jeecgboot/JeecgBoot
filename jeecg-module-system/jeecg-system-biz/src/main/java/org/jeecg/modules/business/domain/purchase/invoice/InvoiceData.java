package org.jeecg.modules.business.domain.purchase.invoice;

/**
 * DTO for purchase invoice data between service and controller
 */
public class InvoiceData {
    private final String entity;

    private final String code;

    public InvoiceData(String entity, String code) {
        this.entity = entity;
        this.code = code;
    }

    public String getEntity() {
        return entity;
    }

    public String getCode() {
        return code;
    }
}
