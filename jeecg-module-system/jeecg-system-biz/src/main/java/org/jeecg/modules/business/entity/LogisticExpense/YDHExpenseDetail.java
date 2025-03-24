package org.jeecg.modules.business.entity.LogisticExpense;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

@Data
public class YDHExpenseDetail {
    @Excel(name="客户单号")
    private String platformOrderId;
    @Excel(name="跟踪单号")
    private String trackingNumber;
    @Excel(name="目的国家")
    private String targetCountry;
    @Excel(name="实重")
    private BigDecimal realWeight;
    @Excel(name="体积重")
    private BigDecimal volumetricWeight;
    @Excel(name="计费重")
    private BigDecimal chargingWeight;
    @Excel(name="运费")
    private BigDecimal shippingFee;
    @Excel(name="燃油费")
    private BigDecimal fuelCosts;
    @Excel(name="挂号费")
    private BigDecimal registrationFee;
    @Excel(name="其他费用")
    private BigDecimal additionalFee;
    @Excel(name="时间段费用")
    private BigDecimal timePeriodFee;
    @Excel(name="总费用")
    private BigDecimal totalFee;
}
