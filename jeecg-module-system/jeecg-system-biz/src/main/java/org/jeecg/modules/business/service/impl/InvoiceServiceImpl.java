package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Balance;
import org.jeecg.modules.business.entity.Invoice;
import org.jeecg.modules.business.entity.PurchaseOrder;
import org.jeecg.modules.business.mapper.InvoiceMapper;
import org.jeecg.modules.business.mapper.PurchaseOrderContentMapper;
import org.jeecg.modules.business.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.jeecg.modules.business.entity.Invoice.InvoiceType.*;

@Service
@Slf4j
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {
    @Autowired
    private IBalanceService balanceService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private IPlatformOrderContentService platformOrderContentService;
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private PurchaseOrderContentMapper purchaseOrderContentMapper;
    @Autowired
    private IPurchaseOrderService purchaseOrderService;
    @Autowired
    private ISavRefundService savRefundService;
    @Autowired
    private IShippingInvoiceService shippingInvoiceService;
    @Value("${jeecg.path.purchaseInvoiceDir}")
    private String PURCHASE_INVOICE_LOCATION;
    @Value("${jeecg.path.shippingInvoiceDir}")
    private String SHIPPING_INVOICE_LOCATION;
    @Value("${jeecg.path.shippingInvoiceDetailDir}")
    private String SHIPPING_INVOICE_DETAIL_LOCATION;

    /**
     * Cancel invoice and deletes generated files.
     * shipping : deletes shipping_invoice, resets data in platform_order_content, platform_order, sav_refund, and balance
     * purchase : deletes purchase_invoice, removes attachment files, removes number from platform_order
     * complete : deletes purchase_invoice, removes attachment files, resets data in platform_order_content, platform_order, sav_refund, and balance
     * @param id for shipping and complete invoices, it is the shipping_invoice id, for purchase invoices, it is the purchase_order id
     * @param invoiceNumber invoice number to be cancelled
     * @param clientId client id
     * @return if invoice is successfully cancelled and files are deleted, will return false even when some files are just missing
     */
    @Override
    public boolean cancelInvoice(String id, String invoiceNumber, String clientId) {
        String invoiceEntity = clientService.getById(clientId).getInvoiceEntity();

        if(Invoice.getType(invoiceNumber).equalsIgnoreCase(PURCHASE.name())) {
            PurchaseOrder po = purchaseOrderService.getById(id);
            if (po.getInventoryDocumentString() != null && !po.getInventoryDocumentString().isEmpty())
                shippingInvoiceService.deleteAttachmentFile(po.getInventoryDocumentString());
            if (po.getPaymentDocumentString() != null && !po.getPaymentDocumentString().isEmpty())
                shippingInvoiceService.deleteAttachmentFile(po.getPaymentDocumentString());
            platformOrderService.removePurchaseInvoiceNumber(invoiceNumber);
            purchaseOrderService.deleteInvoice(invoiceNumber);
        }
        if(Invoice.getType(invoiceNumber).equalsIgnoreCase(SHIPPING.name())) {
            platformOrderContentService.cancelInvoice(invoiceNumber);
            platformOrderService.cancelInvoice(invoiceNumber);
            shippingInvoiceService.delMain(id);
        }
        if(Invoice.getType(invoiceNumber).equalsIgnoreCase(COMPLETE.name())) {
            platformOrderContentService.cancelInvoice(invoiceNumber);
            platformOrderService.removePurchaseInvoiceNumber(invoiceNumber);
            platformOrderService.cancelInvoice(invoiceNumber);
            purchaseOrderService.cancelInvoice(invoiceNumber);
            shippingInvoiceService.delMain(id);
            PurchaseOrder purchase = purchaseOrderService.getPurchaseByInvoiceNumberAndClientId(invoiceNumber, clientId);
            if(purchase.getInventoryDocumentString() != null && !purchase.getInventoryDocumentString().isEmpty())
                shippingInvoiceService.deleteAttachmentFile(purchase.getInventoryDocumentString());
            if(purchase.getPaymentDocumentString() != null && !purchase.getPaymentDocumentString().isEmpty())
                shippingInvoiceService.deleteAttachmentFile(purchase.getPaymentDocumentString());
        }
        savRefundService.cancelInvoice(invoiceNumber);

        log.info("Updating balance ...");
        balanceService.deleteBalance(id, Balance.OperationType.Debit.name());

        log.info("Deleting invoice files ...");
        boolean invoiceDeleted = deleteInvoice(invoiceNumber, invoiceEntity);
        log.info("Invoice files deleted.");
        return invoiceDeleted;
    }

    @Override
    public boolean cancelBatchInvoice(List<Invoice> invoices) {
        List<String> purchaseInvoiceNumbers = invoices.stream().map(Invoice::getInvoiceNumber).filter(invoiceNumber -> Invoice.getType(invoiceNumber).equalsIgnoreCase(PURCHASE.name())).collect(Collectors.toList());
        List<String> shippingInvoiceNumbers = invoices.stream().map(Invoice::getInvoiceNumber).filter(invoiceNumber -> Invoice.getType(invoiceNumber).equalsIgnoreCase(SHIPPING.name())).collect(Collectors.toList());
        List<String> completeInvoiceNumbers = invoices.stream().map(Invoice::getInvoiceNumber).filter(invoiceNumber -> Invoice.getType(invoiceNumber).equalsIgnoreCase(COMPLETE.name())).collect(Collectors.toList());
        log.info("Cancelling {} purchase invoices : {}", purchaseInvoiceNumbers.size(), purchaseInvoiceNumbers);
        log.info("Cancelling {} shipping invoices : {}", shippingInvoiceNumbers.size(), shippingInvoiceNumbers);
        log.info("Cancelling {} complete invoices : {}", completeInvoiceNumbers.size(), completeInvoiceNumbers);

        if(!purchaseInvoiceNumbers.isEmpty()) {
            List<Invoice> purchaseInvoices = invoices.stream().filter(invoice -> Invoice.getType(invoice.getInvoiceNumber()).equalsIgnoreCase(PURCHASE.name())).collect(Collectors.toList());
            List<String> ids = purchaseInvoices.stream().map(Invoice::getId).collect(Collectors.toList());
            List<PurchaseOrder> purchaseOrders = purchaseOrderService.getPurchasesByInvoices(purchaseInvoices);

            for(PurchaseOrder po : purchaseOrders) {
                if(po.getInventoryDocumentString() != null && !po.getInventoryDocumentString().isEmpty())
                    shippingInvoiceService.deleteAttachmentFile(po.getInventoryDocumentString());
                if(po.getPaymentDocumentString() != null && !po.getPaymentDocumentString().isEmpty())
                    shippingInvoiceService.deleteAttachmentFile(po.getPaymentDocumentString());
            }
            platformOrderService.removePurchaseInvoiceNumbers(purchaseInvoiceNumbers);
            purchaseOrderService.cancelBatchInvoice(purchaseInvoiceNumbers);
            purchaseOrderContentMapper.deleteFromPurchaseIds(ids);
            savRefundService.cancelBatchInvoice(purchaseInvoiceNumbers);
            log.info("Updating balances ...");
            balanceService.deleteBatchBalance(ids, Balance.OperationType.Debit.name());

        }
        if(!shippingInvoiceNumbers.isEmpty()) {
            List<String> ids = invoices.stream().filter(invoice -> Invoice.getType(invoice.getInvoiceNumber()).equalsIgnoreCase(SHIPPING.name())).map(Invoice::getId).collect(Collectors.toList());
            platformOrderContentService.cancelBatchInvoice(shippingInvoiceNumbers);
            platformOrderService.cancelBatchInvoice(shippingInvoiceNumbers);
            savRefundService.cancelBatchInvoice(shippingInvoiceNumbers);
            shippingInvoiceService.delBatchMain(ids);
            log.info("Updating balances ...");
            balanceService.deleteBatchBalance(ids, Balance.OperationType.Debit.name());

        }
        if(!completeInvoiceNumbers.isEmpty()) {
            List<Invoice> completeInvoices = invoices.stream().filter(invoice -> Invoice.getType(invoice.getInvoiceNumber()).equalsIgnoreCase(COMPLETE.name())).collect(Collectors.toList());
            List<String> ids = completeInvoices.stream().map(Invoice::getId).collect(Collectors.toList());
            //shipping cancel
            platformOrderContentService.cancelBatchInvoice(completeInvoiceNumbers);
            platformOrderService.cancelBatchInvoice(completeInvoiceNumbers);
            shippingInvoiceService.delBatchMain(ids);

            //purchase cancel
            List<PurchaseOrder> purchaseOrders = purchaseOrderService.getPurchasesByInvoices(completeInvoices);

            for(PurchaseOrder po : purchaseOrders) {
                if(po.getInventoryDocumentString() != null && !po.getInventoryDocumentString().isEmpty())
                    shippingInvoiceService.deleteAttachmentFile(po.getInventoryDocumentString());
                if(po.getPaymentDocumentString() != null && !po.getPaymentDocumentString().isEmpty())
                    shippingInvoiceService.deleteAttachmentFile(po.getPaymentDocumentString());
            }
            platformOrderService.removePurchaseInvoiceNumbers(purchaseInvoiceNumbers);
            purchaseOrderService.cancelBatchInvoice(purchaseInvoiceNumbers);
            purchaseOrderContentMapper.deleteFromPurchaseIds(ids);

            savRefundService.cancelBatchInvoice(completeInvoiceNumbers);
            log.info("Updating balances ...");
            balanceService.deleteBatchBalance(ids, Balance.OperationType.Debit.name());

        }
        // delete files
        log.info("Deleting invoice files ...");
        boolean invoiceDeleted = deleteInvoiceFiles(invoices);
        log.info("Invoice files deleted.");
        return invoiceDeleted;
    }

    /**
     * Deletes invoice and detail files.
     * @param invoiceNumber
     * @param invoiceEntity
     * @return
     */
    public boolean deleteInvoice(String invoiceNumber, String invoiceEntity) {
        boolean invoiceDeleted = false, detailDeleted = false;

        List<Path> invoicePathList = shippingInvoiceService.getPath(Invoice.getType(invoiceNumber).equalsIgnoreCase(PURCHASE.name()) ? PURCHASE_INVOICE_LOCATION : SHIPPING_INVOICE_LOCATION, invoiceNumber, invoiceEntity);
        List<Path> detailPathList = shippingInvoiceService.getPath(SHIPPING_INVOICE_DETAIL_LOCATION, invoiceNumber, invoiceEntity);

        if(invoicePathList.isEmpty()) {
            log.error("FILE NOT FOUND : " + invoiceNumber + ", " +  invoiceEntity);
        } else {
            for (Path path : invoicePathList) {
                log.info(path.toString());
            }
            try {
                File invoiceFile = new File(invoicePathList.get(0).toString());
                if(invoiceFile.delete()) {
                    log.info("Invoice file {} delete successful.", invoicePathList.get(0).toString());
                    invoiceDeleted = true;
                } else {
                    log.error("Invoice file delete fail.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(detailPathList.isEmpty()) {
            log.error("DETAIL FILE NOT FOUND : " + invoiceNumber + ", " +  invoiceEntity);
        } else {
            for (Path path : detailPathList) {
                log.info(path.toString());
            }
            try {
                File detailFile = new File(detailPathList.get(0).toString());
                if(detailFile.delete()) {
                    log.info("Detail file {} delete successful.", detailPathList.get(0).toString());
                    detailDeleted = true;
                } else {
                    log.error("Detail file {} delete fail.", detailPathList.get(0).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return invoiceDeleted && detailDeleted;
    }
    public boolean deleteInvoiceFiles(List<Invoice> invoices) {
        boolean invoicesDeleted = true;
        for(Invoice invoice : invoices) {
            String invoiceNumber = invoice.getInvoiceNumber();
            String clientId = invoice.getClientId();
            String invoiceEntity = clientService.getClientEntity(clientId);
            log.info("Deleting invoice files ...");
            invoicesDeleted &= deleteInvoice(invoiceNumber, invoiceEntity);
            log.info("Invoice files deleted.");
        }
        return invoicesDeleted;
    }

}
