package org.jeecg.modules.business.domain.codeGeneration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ShippingInvoiceCodeRule implements CodeGenerationRule<String> {

    /**
     * Return next shipping invoice code based on previous code.
     * <p>
     * If previous is null, it calls next();
     * If previous code is not null but is not the code of this month, it calls next();
     *
     * @param previous the previous code
     * @return new code
     */
    @Override
    public String next(String previous) {
        if (previous != null) {
            String[] codeParts = previous.split("-");
            String this_month = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM"));
            if (this_month.equals(codeParts[1])) {
                codeParts[2] = "" + (Integer.parseInt(codeParts[2]) + 1);
                return String.join("-", codeParts);
            }
        }
        return next();
    }

    /**
     * Generate the first invoice code of current month
     *
     * @return the first invoice code of current month
     */
    @Override
    public String next() {
        String yearAndMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return "" + yearAndMonth + "-2001";
    }
}
