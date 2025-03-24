package org.jeecg.modules.business.entity.LogisticExpense;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

@Data
public class CNEExpenseDetail {
    @Excel(name="内单号")
    private String internalTrackingNumber;
    @Excel(name="转单号")
    private String trackingNumber;
    @Excel(name="参考号")
    private String platformOrderId;
    @Excel(name="产品名称")
    private String logisticChannelName;
    @Excel(name="目的地")
    private String targetCountryCn;
    @Excel(name="计费重（kg）")
    private BigDecimal chargingWeight;
    @Excel(name="金额")
    private BigDecimal totalFee;
    @Excel(name="币种")
    private String currency;
    @Excel(name="备注")
    private String remark;
}
