package org.jeecg.modules.business.controller.admin.shippingInvoice;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class InvoiceDatas {
    @JSONField(name = "invoiceNumber")
    private String invoiceNumber;
    @JSONField(name = "feeAndQtyPerCountry")
    private Map<String, Map.Entry<Integer, BigDecimal>> feeAndQtyPerCountry;
    @JSONField(name = "vat")
    private BigDecimal vat;
    @JSONField(name = "serviceFee")
    private BigDecimal serviceFee;
    @JSONField(name = "pickingFee")
    private BigDecimal pickingFee;
    @JSONField(name = "packagingMaterialFee")
    private BigDecimal packagingMaterialFee;
    @JSONField(name = "discount")
    private BigDecimal discount;
    @JSONField(name = "refund")
    private BigDecimal refund;
    @JSONField(name = "finalAmountEur")
    private BigDecimal finalAmountEur;
    @JSONField(name = "finalAmount")
    private BigDecimal finalAmount;

}
