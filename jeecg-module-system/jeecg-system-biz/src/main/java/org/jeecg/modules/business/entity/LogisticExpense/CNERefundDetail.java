package org.jeecg.modules.business.entity.LogisticExpense;

import lombok.Data;

import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

@Data
public class CNERefundDetail {
    @Excel(name="转单号")
    private String trackingNumber;
    @Excel(name="退款")
    private BigDecimal totalFee;
    @Excel(name="币种")
    private String currency;
    @Excel(name="退款原因")
    private String remark;
}
