package org.jeecg.modules.business.domain.shippingInvoice;

import org.jeecg.modules.business.domain.invoice.InvoiceStyleFactory;
import org.jeecg.modules.business.domain.invoice.Row;
import org.jeecg.modules.business.domain.purchase.invoice.PurchaseInvoiceEntry;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.vo.PromotionDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represent the invoice file needed in business process, since the generation of this file
 * need complex data, instance of class can only be created by its factory.
 */
public class CompleteInvoice extends ShippingInvoice {
    private final List<PurchaseInvoiceEntry> purchaseInvoiceEntries;

    private final List<PromotionDetail> promotions;

    public CompleteInvoice(Client targetClient, String code,
                           String subject,
                           Map<PlatformOrder, List<PlatformOrderContent>> ordersToContent,
                           List<PurchaseInvoiceEntry> purchaseInvoiceEntries, List<PromotionDetail> promotions, BigDecimal exchangeRate) {
        super(targetClient, code, subject, ordersToContent, null, exchangeRate);
        this.purchaseInvoiceEntries = purchaseInvoiceEntries;
        this.promotions = promotions;
    }

    /**
     * Generates row content based on package data, content of row is determined by business process.
     *
     * @return a list of generated row
     */
    @Override
    protected List<Row<String, Object, Integer, Object, BigDecimal>> tableData() {
        List<Row<String, Object, Integer, Object, BigDecimal>> rows = super.tableData();
        List<Row<String, Object, Integer, Object, BigDecimal>> res = purchaseInvoiceEntries.stream()
                .map(entry ->
                        new Row<>(
                                entry.getSku_en_name(),
                                (Object) entry.unitPrice(),
                                entry.getQuantity(),
                                null,
                                entry.getTotalAmount()
                        )
                ).collect(Collectors.toList());

        res.addAll(promotions.stream()
                .map(
                        pd -> new Row<>(
                                String.format("Promotion: %s", pd.getName()),
                                null,
                                (Integer) null,
                                (Object) pd.getUnitAmount().multiply(BigDecimal.valueOf(pd.getCount())),
                                (BigDecimal) null
                        )
                ).collect(Collectors.toList())
        );
        res.addAll(rows);
        return res;
    }
}
