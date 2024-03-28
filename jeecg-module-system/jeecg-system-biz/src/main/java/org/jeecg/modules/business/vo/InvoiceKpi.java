package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceKpi extends Kpi{
    private Long qty;
    private Long total;
    private BigDecimal growthQty;
    private BigDecimal growthTotal;

    public InvoiceKpi(Long qty, Long total) {
        this.qty = qty;
        this.total = total;
    }

}
