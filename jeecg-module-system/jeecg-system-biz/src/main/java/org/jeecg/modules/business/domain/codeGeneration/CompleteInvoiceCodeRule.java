package org.jeecg.modules.business.domain.codeGeneration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CompleteInvoiceCodeRule extends ShippingInvoiceCodeRule {


    /**
     * Generate the first invoice code of current month
     *
     * @return the first invoice code of current month
     */
    @Override
    public String next() {
        String yearAndMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return "" + yearAndMonth + "-7001";
    }
}
