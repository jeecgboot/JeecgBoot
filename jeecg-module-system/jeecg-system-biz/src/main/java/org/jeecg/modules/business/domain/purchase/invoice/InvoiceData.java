package org.jeecg.modules.business.domain.purchase.invoice;

import lombok.Getter;

/**
 * DTO for purchase invoice data between service and controller
 */
@Getter
public class InvoiceData {
    private final String entity;

    private final String code;

    public InvoiceData(String entity, String code) {
        this.entity = entity;
        this.code = code;
    }

}
