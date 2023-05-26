package org.jeecg.modules.business.domain.codeGeneration;

public class InvoiceRef implements CodeGenerationRule<String>{
    @Override
    public String next(String previous) {
        return String.format("%05d", Integer.parseInt(previous));
    }

    @Override
    public String next() {
        return null;
    }
}
