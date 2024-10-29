package org.jeecg.modules.business.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Invoice;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PurchaseOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.InvoiceKpi;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.jeecg.modules.business.vo.PurchaseOrderPage;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.springframework.stereotype.Repository;

/**
 * @Description: 商品采购订单
 * @Author: jeecg-boot
 * @Date: 2021-04-03
 * @Version: V1.0
 */
@Repository
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
    /**
     * Add a purchase recording of a client.
     *
     * @param creator     creator's name
     * @param clientID    identifier of the client
     * @param totalAmount total amount of the purchase
     * @param discount    discount amount of the purchase
     * @param finalAmount final amount of the purchase
     */
    void addPurchase(
            @Param("ID") String ID, @Param("creator") String creator,
            @Param("currencyID") String currencyID,
            @Param("clientID") String clientID,
            @Param("totalAmount") BigDecimal totalAmount,
            @Param("discount") BigDecimal discount,
            @Param("finalAmount") BigDecimal finalAmount,
            @Param("invoiceNumber") String invoiceNumber
    );

    /**
     * Search purchase order by client's ID.
     *
     * @param clientID identifier of client
     * @return list of purchase order
     */
    List<PurchaseOrder> pageByClientID(
            @Param("clientID") String clientID,
            @Param("offset") long offset,
            @Param("size") long size
    );

    long countTotal(@Param("clientID") String clientID);

    /**
     * Get the last invoice number of the purchase order in this month.
     *
     * @return the last invoice number or null in case of no purchase this month.
     */
    String lastInvoiceNumber();

    /**
     * Update in DB for a purchase order its payment filename and change the status of
     * the purchase order to 'paid'.
     * The previous value of filename will be overwrite.
     *
     * @param purchaseID the purchase order's identifier
     * @param filename   the filename.
     */
    void updatePaymentDocument(@Param("purchaseID") String purchaseID,
                               @Param("fileName") String filename);

    /**
     * Query payment filename of a certain purchase order.
     *
     * @param purchaseID identifier of the purchase order.
     * @return the filename
     */
    String selectPaymentDocument(@Param("purchaseID") String purchaseID);

    /**
     * Change a purchase's status to confirmed in DB.
     *
     * @param purchaseID the identifier of the purchase to change.
     */
    void confirmPayment(@Param("purchaseID") String purchaseID);

    /**
     * Change a purchase's status to purchasing in DB.
     *
     * @param purchaseID the identifier of the purchase to change.
     */
    void confirmPurchase(@Param("purchaseID") String purchaseID);

    String getInvoiceNumber(@Param("purchaseID") String purchaseID);


    BigDecimal getPurchaseFeesByInvoiceCode(@Param("invoiceCode") String invoiceCode);

    void deleteInvoice(@Param("invoiceNumber") String invoiceNumber, @Param("clientId") String clientId);

    void deleteBatchInvoice(@Param("invoiceNumbers") List<String> invoiceNumbers);

    String getInvoiceId(@Param("invoiceNumber") String invoiceNumber);

    PurchaseOrder getPurchaseByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);
    List<PurchaseOrder> getPurchasesByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);

    List<PlatformOrder> getPlatformOrder(@Param("invoiceNumber") String invoiceNumber);

    List<SkuQuantity> getSkuQuantityByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);

    InvoiceMetaData getMetaDataFromInvoiceNumbers(@Param("invoiceNumber") String invoiceNumber);

    List<PurchaseOrderPage> getPage(@Param("offset") long offset, @Param("size") long size, @Param("clientId") String clientId);

    long countPurchaseOrders();

    void updatePurchaseOrderStatus(@Param("invoiceNumber") String invoiceNumber, @Param("isOrdered") boolean isOrdered);

    void updatePurchaseOrderGroupIds(@Param("invoiceNumber") String invoiceCode, @Param("groupIds") String groupIds);

    InvoiceKpi countPurchaseInvoices(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("showAllData") boolean showAllData, @Param("username") String username);

    void setPaid(@Param("invoiceNumbers") List<String> invoiceNumbers);

    PurchaseOrder getPurchaseByInvoiceNumberAndClientId(@Param("invoiceNumber") String invoiceNumber, @Param("clientId") String clientId);

    List<PurchaseOrder> getPurchasesByInvoices(@Param("invoices") List<Invoice> invoices);
}
