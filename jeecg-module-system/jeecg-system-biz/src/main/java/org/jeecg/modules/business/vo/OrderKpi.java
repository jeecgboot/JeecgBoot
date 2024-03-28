package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderKpi extends Kpi {
    private Long processed;
    private Long processing;
    private BigDecimal growth;

    public OrderKpi(Long processed, Long processing) {
        this.processed = processed;
        this.processing = processing;
    }

}
