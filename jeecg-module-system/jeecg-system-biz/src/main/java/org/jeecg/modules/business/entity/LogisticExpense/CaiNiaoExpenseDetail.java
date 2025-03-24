package org.jeecg.modules.business.entity.LogisticExpense;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

@Data
public class CaiNiaoExpenseDetail {
    @Excel(name="费用项")
    private String expenseName;
    @Excel(name="物流商品")
    private String logisticChannelName;
    @Excel(name="计费币种")
    private String chargingCurrency;
    @Excel(name="计费金额")
    private BigDecimal billingAmount;
    @Excel(name="支付币种")
    private String paymentCurrency;
    @Excel(name="支付金额")
    private BigDecimal paymentAmount;
    @Excel(name="ERP单号")
    private String platformOrderId;
    @Excel(name="包裹计费重(克)")
    private String chargingWeight;
    @Excel(name="外单计泡包裹泡重")
    private BigDecimal volumetricWeight;
    @Excel(name="外单计泡包裹实重")
    private BigDecimal realWeight;
    @Excel(name="收件地址-国家")
    private String targetCountry;
    @Excel(name="目的国家-中文")
    private String targetCountryCn;
}
