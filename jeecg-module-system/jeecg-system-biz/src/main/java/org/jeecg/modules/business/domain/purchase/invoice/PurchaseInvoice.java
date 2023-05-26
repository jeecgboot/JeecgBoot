package org.jeecg.modules.business.domain.purchase.invoice;

import org.jeecg.modules.business.domain.invoice.AbstractInvoice;
import org.jeecg.modules.business.domain.invoice.Row;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.vo.PromotionDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Control content of purchase invoice
 */
public class PurchaseInvoice extends AbstractInvoice<String, BigDecimal, Integer, BigDecimal, BigDecimal> {

    private final List<PurchaseInvoiceEntry> purchaseInvoiceEntries;

    private final List<PromotionDetail> promotions;

    public PurchaseInvoice(Client targetClient, String code, String subject, List<PurchaseInvoiceEntry> purchaseInvoiceEntries,
                           List<PromotionDetail> promotions, BigDecimal eurToUsd) {
        super(targetClient, code, subject, eurToUsd);
        this.purchaseInvoiceEntries = purchaseInvoiceEntries;
        this.promotions = promotions;
    }

    /**
     * Fill cells that belong to the table section.
     *
     * @return map between location and table cell
     */
    protected List<Row<String, BigDecimal, Integer, BigDecimal, BigDecimal>> tableData() {
        List<Row<String, BigDecimal, Integer, BigDecimal, BigDecimal>> res = purchaseInvoiceEntries.stream()
                .map(entry ->
                        new Row<>(
                                entry.getSku_en_name(),
                                entry.unitPrice(),
                                entry.getQuantity(),
                                (BigDecimal)null,
                                entry.getTotalAmount()
                        )
                ).collect(Collectors.toList());

        res.addAll(promotions.stream()
                .map(
                        pd -> new Row<>(
                                String.format("Promotion: %s", pd.getName()),
                                (BigDecimal) null,
                                (Integer) null,
                                pd.getUnitAmount().multiply(BigDecimal.valueOf(pd.getCount())),
                                (BigDecimal)null
                        )
                ).collect(Collectors.toList())
        );
        return res;
    }


}

