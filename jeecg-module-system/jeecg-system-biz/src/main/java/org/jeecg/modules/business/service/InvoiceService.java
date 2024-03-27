package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.Invoice;

import java.util.List;

public interface InvoiceService extends IService<Invoice> {
    boolean cancelInvoice(String id, String invoiceNumber, String clientId);

    boolean cancelBatchInvoice(List<Invoice> invoices);
}
