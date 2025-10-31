package org.jeecg.modules.business.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InvoiceAmountDTO {
    private BigDecimal purchaseAmount;
    private BigDecimal shippingAmount;
}
