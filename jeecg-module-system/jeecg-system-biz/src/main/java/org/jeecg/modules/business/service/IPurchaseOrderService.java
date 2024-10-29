package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.jeecg.modules.business.vo.PurchaseOrderPage;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 商品采购订单
 * @Author: jeecg-boot
 * @Date: 2021-04-03
 * @Version: V1.0
 */
public interface IPurchaseOrderService extends IService<PurchaseOrder> {
        /**
     * 添加一对多
     */
        void saveMain(PurchaseOrder purchaseOrder, List<PurchaseOrderSku> purchaseOrderSkuList, List<SkuPromotionHistory> skuPromotionHistoryList);

    /**
     * 修改一对多
     */
    public void updateMain(PurchaseOrder purchaseOrder, List<PurchaseOrderSku> purchaseOrderSkuList, List<SkuPromotionHistory> skuPromotionHistoryList);

    /**
     * 删除一对多
     */
    void delMain(String id);

    /**
     * 批量删除一对多
     */
    public void delBatchMain(Collection<? extends Serializable> idList);

    /**
     * Set purchase orders to the page indicated by argument.
     *
     * @param page the page to put data
     */
    void setPageForCurrentClient(IPage<PurchaseOrder> page);

    String addPurchase(List<SkuQuantity> skuQuantities) throws UserException;

    /**
     * Add a new purchase. The purchase contains sku and its quantity indicated by
     * first argument.
     * This purchase may built from some orders, in this case, indicate these
     * orders' ids in the second argument, their status will be updated. Attention,
     * The skus in first argument can not be less than ones in the second. This function doesn't check it.
     * <p>
     * If not, put null.
     *
     * @param SkuQuantity map of sku ID and quantity.
     * @param orderIDs    orders on which the purchase is based
     * @return the new purchase order identifier
     */
    @Transactional
    String addPurchase(List<SkuQuantity> SkuQuantity, List<String> orderIDs) throws UserException;

    @Transactional
    String addPurchase(String username, Client client, String invoiceNumber, List<SkuQuantity> skuQuantities, Map<PlatformOrder, List<PlatformOrderContent>> platformOrderIDs) throws UserException;

    void savePaymentDocumentForPurchase(String purchaseID, MultipartFile in) throws IOException;

    /**
     * Download the file of the purchase order indicated by its name.
     *
     * @param filename the file's name
     * @return the file in binary
     * @throws IOException IO error while reading the file.
     */
    byte[] downloadPaymentDocumentOfPurchase(String filename) throws IOException;

    /**
     * Change the status of a purchase to confirmed
     *
     * @param purchaseID the identifier of the purchase order to confirm
     */
    void confirmPayment(String purchaseID);

    /**
     * Change the status of a confirmed to purchasing
     *
     * @param purchaseID the identifier of the purchase order to confirm
     */
    void confirmPurchase(String purchaseID);


    /**
     * Make invoice file
     * @param purchaseID
     * @return the file in binary
     * @throws IOException IO error while reading the file.
     */
    InvoiceMetaData makeInvoice(String purchaseID) throws IOException, URISyntaxException, UserException;

    byte[] getInvoiceByte(String invoiceCode) throws IOException;

    BigDecimal getPurchaseFeesByInvoiceCode(String invoiceCode);

    void cancelInvoice(String invoiceNumber, String clientId);

    void cancelBatchInvoice(List<String> invoiceNumbers);

    String getInvoiceId(String invoiceNumber);

    PurchaseOrder getPurchaseByInvoiceNumber(String invoiceNumber);

    List<PurchaseOrder> getPurchasesByInvoiceNumber(String invoiceNumber);

    List<PlatformOrder> getPlatformOrder(String invoiceNumber);

    List<SkuQuantity> getSkuQuantityByInvoiceNumber(String invoiceNumber);

    InvoiceMetaData getMetaDataFromInvoiceNumbers(String invoiceNumber);

    void setPageForList(Page<PurchaseOrderPage> page, String clientId);

    void updatePurchaseOrderStatus(String invoiceNumber, boolean isOrdered);
    void updatePurchaseOrderGroupIds(String invoiceCode, List<String> groupIds);

    void setPaid(List<String> invoiceNumbers);

    PurchaseOrder getPurchaseByInvoiceNumberAndClientId(String invoiceNumber, String clientId);

    List<PurchaseOrder> getPurchasesByInvoices(List<Invoice> invoices);

}
