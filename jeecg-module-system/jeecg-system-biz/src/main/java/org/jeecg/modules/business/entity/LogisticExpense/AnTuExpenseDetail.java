package org.jeecg.modules.business.entity.LogisticExpense;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class AnTuExpenseDetail {
    @Excel(name="原单号")
    private String platformOrderId;
    @Excel(name="转单号")
    private String trackingNumber;
    @Excel(name="国家")
    private String targetCountry;
    @Excel(name="计费重")
    private String chargingWeight;
    @Excel(name="运费")
    private String serviceFee;
    @Excel(name="燃油")
    private String fuelSurcharge;
    @Excel(name="杂费")
    private String additionalFee;
    @Excel(name="总金额")
    private String totalFee;
    /**
     * 备注
     * format :  配货：Sku1;Sku2;...;SkuN;
     */
    @Excel(name="备注")
    private String remark;
}
