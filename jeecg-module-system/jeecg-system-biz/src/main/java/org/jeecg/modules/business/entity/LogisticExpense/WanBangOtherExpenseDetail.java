package org.jeecg.modules.business.entity.LogisticExpense;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

@Data
public class WanBangOtherExpenseDetail {
    @Excel(name="类型")
    private String additionalFeeType;
    @Excel(name="金额")
    private BigDecimal totalFee;
    @Excel(name="快递单号")
    private String trackingNumber;
    @Excel(name="重量(Kg)")
    private BigDecimal weight;
    @Excel(name="备注")
    private String remark;
}
