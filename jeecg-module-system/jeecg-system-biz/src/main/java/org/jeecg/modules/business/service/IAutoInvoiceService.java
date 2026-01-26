package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Invoice;
import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.model.AutoInvoiceFailDetail;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IAutoInvoiceService {

    void processShop(
            Client client,
            Shop shop,
            SimpleDateFormat sdf,
            List<Map<String, Object>> generatedInvoices,
            List<AutoInvoiceFailDetail> failDetails
    ) throws Exception;

    boolean approveInvoiceIfPossible(
            Client client,
            String invoiceCode,
            BigDecimal balance
    );

    void notifyAutoInvoiceGenerated(
            Client client,
            BigDecimal balance,
            List<Map<String, Object>> invoicesGenerated,
            List<AutoInvoiceFailDetail> failDetails
    );

    void recordFail(
            List<AutoInvoiceFailDetail> failDetails,
            Client client,
            Shop shop,
            String invoiceCode,
            AutoInvoiceFailDetail.Step step,
            String errorReason,
            Throwable throwable
    );

    String extractShortError(Throwable t);

    String toHtmlInvoices(List<Map<String, Object>> invoices);

    String toHtmlFails(List<AutoInvoiceFailDetail> fails);

    Set<String> listAnnouncementReceivers();

    void tryEditOrdersRemarkAfterInvoice(String invoiceNumber, Invoice.InvoicingMethod invoicingMethod);
}
