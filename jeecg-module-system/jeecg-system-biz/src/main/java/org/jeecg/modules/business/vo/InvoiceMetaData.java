package org.jeecg.modules.business.vo;

import lombok.Data;

@Data
public class InvoiceMetaData {

    private final String filename;

    private final String invoiceCode;

    private final String internalCode;

    private final String invoiceEntity;

    private final String errorMsg;
}
