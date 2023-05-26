package org.jeecg.modules.business.domain.codeGeneration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PurchaseInvoiceCodeRule implements CodeGenerationRule<String> {

    @Override
    public String next(String previous) {
        if (previous != null) {
            String[] codeParts = previous.split("-");
            codeParts[2] = "" + (Integer.parseInt(codeParts[2]) + 1);
            return String.join("-", codeParts);
        }
        return next();
    }

    @Override
    public String next() {
        String yearAndMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return "" + yearAndMonth + "-1001";
    }
}
