package org.jeecg.modules.business.domain.shippingInvoice;

import org.apache.poi.ss.usermodel.*;
import org.jeecg.modules.business.domain.invoice.AbstractInvoice;
import org.jeecg.modules.business.domain.invoice.InvoiceStyleFactory;
import org.jeecg.modules.business.domain.invoice.Row;
import org.jeecg.modules.business.entity.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represent the invoice file needed in business process, since the generation of this file
 * need complex data, instance of class can only be created by its factory.
 */
public class ShippingInvoice extends AbstractInvoice<String, Object, Integer, Object, BigDecimal> {
    private final Map<PlatformOrder, List<PlatformOrderContent>> ordersToContent;

    private final List<SavRefundWithDetail> savRefunds;

    private BigDecimal totalAmount;

    public ShippingInvoice(Client targetClient, String code,
                           String subject,
                           Map<PlatformOrder, List<PlatformOrderContent>> ordersToContent,
                           List<SavRefundWithDetail> savRefunds, BigDecimal exchangeRate) {
        super(targetClient, code, subject, exchangeRate);
        this.ordersToContent = ordersToContent;
        this.savRefunds = savRefunds;
        totalAmount = BigDecimal.ZERO;
    }

    /**
     * Generates row content based on package data, content of row is determined by business process.
     *
     * @return a list of generated row
     */
    @Override
    protected List<Row<String, Object, Integer, Object, BigDecimal>> tableData() {
        Map<String, List<PlatformOrder>> countryPackageMap = ordersToContent.keySet().stream()
                .collect(
                        Collectors.groupingBy(PlatformOrder::getCountry)
                );
        Map<String, List<PlatformOrderContent>> orderMap = ordersToContent.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getPlatformOrderId(), Map.Entry::getValue));

        List<Row<String, Object, Integer, Object, BigDecimal>> rows = new ArrayList<>();

        BigDecimal vatForEU = BigDecimal.ZERO;
        BigDecimal totalServiceFees = BigDecimal.ZERO;
        BigDecimal totalPickingFees = BigDecimal.ZERO;
         BigDecimal totalPackageMatFeePerOrder = BigDecimal.ZERO;
        for (Map.Entry<String, List<PlatformOrder>> entry : countryPackageMap.entrySet()) {
            String country = entry.getKey();
            List<PlatformOrder> orders = entry.getValue();

            BigDecimal countryShippingFees = BigDecimal.ZERO;
            BigDecimal countryFretFees = BigDecimal.ZERO;
            BigDecimal countryServiceFeesPerOrder = BigDecimal.ZERO;
            BigDecimal countryServiceFeesPerSKU = BigDecimal.ZERO;
            BigDecimal countryPickingFeesPerOrder = BigDecimal.ZERO;
            BigDecimal countryPickingFeesPerSKU = BigDecimal.ZERO;
            BigDecimal countryPackageMatFeePerOrder = BigDecimal.ZERO;
            for (PlatformOrder po : orders) {
                countryFretFees = countryFretFees.add(po.getFretFee());
                countryServiceFeesPerOrder = countryServiceFeesPerOrder.add(po.getOrderServiceFee());
                countryPickingFeesPerOrder = countryPickingFeesPerOrder.add(po.getPickingFee());
                countryPackageMatFeePerOrder = countryPackageMatFeePerOrder.add(po.getPackagingMaterialFee());
                for (PlatformOrderContent poc : orderMap.get(po.getPlatformOrderId())) {
                    countryShippingFees = countryShippingFees.add(poc.getShippingFee());
                    vatForEU = vatForEU.add(poc.getVat());
                    countryServiceFeesPerSKU = countryServiceFeesPerSKU.add(poc.getServiceFee());
                    countryPickingFeesPerSKU = countryPickingFeesPerSKU.add(poc.getPickingFee());
                }
            }
            totalServiceFees = totalServiceFees.add(countryServiceFeesPerOrder).add(countryServiceFeesPerSKU);
            totalPickingFees = totalPickingFees.add(countryPickingFeesPerOrder).add(countryPickingFeesPerSKU);
            totalPackageMatFeePerOrder = totalPackageMatFeePerOrder.add(countryPackageMatFeePerOrder);
            Row<String, Object, Integer, Object, BigDecimal> row = new Row<>(
                    String.format("Total shipping cost for %s", country),
                    null,
                    orders.size(),
                    null,
                    countryShippingFees.add(countryFretFees)
            );
            rows.add(row);
            totalAmount = totalAmount
                    .add(countryShippingFees)
                    .add(countryFretFees)
                    .add(countryServiceFeesPerOrder)
                    .add(countryServiceFeesPerSKU)
                    .add(countryPickingFeesPerOrder)
                    .add(countryPickingFeesPerSKU)
                    .add(countryPackageMatFeePerOrder);
        }
        Row<String, Object, Integer, Object, BigDecimal> vatRow = new Row<>(
                "Total VAT fee for EU",
                null,
                null,
                null,
                vatForEU
        );
        rows.add(vatRow);
        Row<String, Object, Integer, Object, BigDecimal> serviceFeeRow = new Row<>(
                "Total service fee",
                null,
                null,
                null,
                totalServiceFees
        );
        rows.add(serviceFeeRow);
        Row<String, Object, Integer, Object, BigDecimal> pickingFeeRow = new Row<>(
                "Total picking fee",
                null,
                null,
                null,
                totalPickingFees
        );
        rows.add(pickingFeeRow);
        Row<String, Object, Integer, Object, BigDecimal> packageMatFeeRow = new Row<>(
                "Total packaging material fee",
                null,
                null,
                null,
                totalPackageMatFeePerOrder
        );
        rows.add(packageMatFeeRow);
        if (savRefunds != null) {
            for (SavRefundWithDetail savRefund : savRefunds) {
                BigDecimal refundForOrder = BigDecimal.ZERO
                        .subtract(savRefund.getTotalRefundAmount());
                Row<String, Object, Integer, Object, BigDecimal> savRefundRow = new Row<>(
                        String.format("Refund for order %s", savRefund.getPlatformOrderNumber()),
                        null,
                        null,
                        null,
                        refundForOrder
                );
                rows.add(savRefundRow);
                totalAmount = totalAmount.add(refundForOrder);
            }
        }
        totalAmount = totalAmount.add(vatForEU);
        return rows;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal reducedAmount() {
        return BigDecimal.ZERO;
    }

    public BigDecimal paidAmount() {
        // function not implemented yet, temporarily zero
        return BigDecimal.ZERO;
    }

    /**
     * In addition to super's operation, if target client prefer dollar, write the formula of exchange to
     * a specific cell.
     *
     * @param factory factory that provide style
     */
    @Override
    protected void fillTable(InvoiceStyleFactory factory) {
        super.fillTable(factory);
    }
}
