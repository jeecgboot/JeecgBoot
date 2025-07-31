package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.Invoice;
import org.jeecg.modules.business.vo.ShippingInvoiceOrderParam;

import java.util.List;

public interface InvoiceService extends IService<Invoice> {
    boolean cancelInvoice(String id, String invoiceNumber, String clientId, boolean isEmployee);

    boolean cancelBatchInvoice(List<Invoice> invoices);

    void asyncEstimateAndPushUpdates(String userId);

    Result<?> checkSkuPrices(ShippingInvoiceOrderParam param);
}
