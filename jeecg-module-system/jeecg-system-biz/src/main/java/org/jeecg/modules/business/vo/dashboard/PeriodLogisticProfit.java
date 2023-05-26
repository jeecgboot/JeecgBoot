package org.jeecg.modules.business.vo.dashboard;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class PeriodLogisticProfit {

    @JSONField(name = "invoicedOrderNumber")
    private final int invoicedOrderNumber;

    @JSONField(name = "uninvoicedOrderNumber")
    private final int uninvoicedOrderNumber;

    /**
     * Index of elements is the date, begin from 0 to end of the month.
     */
    @JSONField(name = "invoicedAmountDue")
    private final Map<LocalDate, BigDecimal> invoicedAmountDue;

    /**
     * Index of elements is the date, begin from 0 to end of the month.
     */
    @JSONField(name = "invoicedAmountDueWithoutVat")
    private final Map<LocalDate, BigDecimal> invoicedAmountDueWithoutVat;

    /**
     * Index of elements is the date, begin from 0 to end of the month.
     */
    @JSONField(name = "invoicedActualCosts")
    private final Map<LocalDate, BigDecimal> invoicedActualCosts;

    /**
     * Index of elements is the date, begin from 0 to end of the month.
     */
    @JSONField(name = "invoicedActualCosts")
    private final Map<LocalDate, BigDecimal> invoicedActualCostsWithoutVat;

    /**
     * Index of elements is the date, begin from 0 to end of the month.
     */
    @JSONField(name = "nonInvoicedActualCosts")
    private final Map<LocalDate, BigDecimal> nonInvoicedActualCosts;

    /**
     * Index of elements is the date, begin from 0 to end of the month.
     */
    @JSONField(name = "nonInvoicedActualCosts")
    private final Map<LocalDate, BigDecimal> nonInvoicedActualCostsWithoutVat;

    @JSONField(name = "exchangeRate")
    private final BigDecimal exchangeRate;
}
