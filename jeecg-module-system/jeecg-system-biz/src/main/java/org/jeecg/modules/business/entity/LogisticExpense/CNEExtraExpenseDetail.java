package org.jeecg.modules.business.entity.LogisticExpense;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

@Data
public class CNEExtraExpenseDetail {
    @Excel(name="附加费类型")
    private String feeType;
    @Excel(name="关联单号类型")
    private String relatedExpenseField;
    @Excel(name="关联单号")
    private String relatedExpenseValue;
    @Excel(name="金额")
    private BigDecimal totalFee;
    @Excel(name="币种")
    private String currency;
    @Excel(name="费用备注")
    private String remark;
}
