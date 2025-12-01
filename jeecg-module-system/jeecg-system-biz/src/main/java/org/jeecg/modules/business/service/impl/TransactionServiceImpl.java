package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.TransactionMapper;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.InvoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: transaction
 * @Author: jeecg-boot
 * @Date:   2023-09-08
 * @Version: V1.0
 */
@Service
@Slf4j
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements ITransactionService {

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private IClientService clientService;
    @Autowired
    private ICurrencyService currencyService;
    @Autowired
    private IPurchaseOrderService purchaseOrderService;
    @Autowired
    private IShippingInvoiceService shippingInvoiceService;

    @Override
    public List<Transaction> list() {
        return transactionMapper.list();
    }

    @Override
    public List<Currency> getAllCurrenciesByClient(String clientId) {
        List<Currency> currencies = transactionMapper.getAllCurrenciesByClient(clientId);
        if (currencies.isEmpty()) {
            Client client = clientService.getById(clientId);
            Currency currency = currencyService.getByCode(client.getCurrency());
            currencies.add(currency);
        }
        return currencies;
    }

    @Override
    public Result<?> checkPaymentApproved(String invoiceNumber) {
        InvoiceType type = InvoiceType.fromInvoiceNumber(invoiceNumber);
        if (type == null) {
            return Result.error("Unsupported invoice number format");
        }
        switch (type) {
            case PURCHASE_INVOICE: {
                PurchaseOrder po = purchaseOrderService.getPurchaseByInvoiceNumber(invoiceNumber);
                if (po == null) {
                    return Result.error("Cannot find purchase order for invoice number: " + invoiceNumber);
                }
                if (po.getPaymentApproved() != null && po.getPaymentApproved()) {
                    return Result.error("Payment already approved. Upload is not allowed.");
                }
                break;
            }
            case SHIPPING_INVOICE: {
                ShippingInvoice si = shippingInvoiceService.getShippingInvoice(invoiceNumber);
                if (si == null) {
                    return Result.error("Cannot find shipping invoice for invoice number: " + invoiceNumber);
                }
                if (si.getPaymentApproved() != null && si.getPaymentApproved() ) {
                    return Result.error("Payment already approved. Upload is not allowed.");
                }
                break;
            }
            case COMPLETE_INVOICE: {
                ShippingInvoice si = shippingInvoiceService.getShippingInvoice(invoiceNumber);
                if (si != null && si.getPaymentApproved() != null && si.getPaymentApproved() ) {
                    return Result.error("Payment already approved for shipping invoice. Upload is not allowed.");
                }
                PurchaseOrder po = purchaseOrderService.getPurchaseByInvoiceNumber(invoiceNumber);
                if (po != null && po.getPaymentApproved() != null && po.getPaymentApproved()) {
                    return Result.error("Payment already approved for purchase order. Upload is not allowed.");
                }
                break;
            }
        }
        return Result.ok();
    }

}
