package org.jeecg.modules.business.service;

import java.util.List;

public interface ICreatePurchaseOrderAsyncService {
    void createMabangPurchaseOrderAsync(List<String> invoiceNumbers, String userId);

    boolean isCreating(List<String> invoiceList);
}