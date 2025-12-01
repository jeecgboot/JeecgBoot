package org.jeecg.modules.business.vo;

public enum InvoiceType {
    PURCHASE_INVOICE(1, "purchase_invoice"),
    SHIPPING_INVOICE(2, "shipping_invoice"),
    COMPLETE_INVOICE(7, "complete_invoice");

    private final int code;
    private final String text;

    InvoiceType(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return String.valueOf(code);
    }

    public String text() {
        return text;
    }

    public static InvoiceType fromCode(Integer code) {
        for (InvoiceType invoiceType : InvoiceType.values()) {
            if (invoiceType.code == code) {
                return invoiceType;
            }
        }
        return null;
    }

    public static InvoiceType fromInvoiceNumber(String invoiceNumber) {
        if (invoiceNumber == null || !invoiceNumber.contains("-")) {
            return null;
        }
        String[] parts = invoiceNumber.split("-");
        String lastPart = parts[parts.length - 1];
        char prefix = lastPart.charAt(0);
        switch (prefix) {
            case '1': return PURCHASE_INVOICE;
            case '2': return SHIPPING_INVOICE;
            case '7': return COMPLETE_INVOICE;
            default: return null;
        }
    }
}