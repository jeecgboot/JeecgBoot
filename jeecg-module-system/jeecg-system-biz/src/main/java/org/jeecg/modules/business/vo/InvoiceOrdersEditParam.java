package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jeecg.modules.business.entity.Invoice;

@Data
public class InvoiceOrdersEditParam {
    private final String invoiceNumber;
    private final Invoice.InvoicingMethod invoicingMethod;

    public InvoiceOrdersEditParam(@JsonProperty("invoiceNumber") String invoiceNumber,
                                  @JsonProperty("invoicingMethod") Invoice.InvoicingMethod invoicingMethod) {
        this.invoiceNumber = invoiceNumber;
        this.invoicingMethod = invoicingMethod;

    }
}
