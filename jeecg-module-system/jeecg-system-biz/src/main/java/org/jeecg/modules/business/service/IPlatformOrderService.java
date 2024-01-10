package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.vo.PlatformOrderQuantity;
import org.jeecg.modules.business.vo.ShippingFeeBillableOrders;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.vo.clientPlatformOrder.ClientPlatformOrderPage;
import org.jeecg.modules.business.vo.clientPlatformOrder.PurchaseConfirmation;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.ClientInfo;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.OrderQuantity;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.OrdersStatisticData;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 平台订单表
 * @Author: jeecg-boot
 * @Date: 2021-04-08
 * @Version: V1.0
 */
public interface IPlatformOrderService extends IService<PlatformOrder> {

    /**
     * 添加一对多
     */
    void saveMain(PlatformOrder platformOrder, List<PlatformOrderContent> platformOrderContentList);

    public boolean saveBatch(Map<PlatformOrder, List<PlatformOrderContent>> orderMap);

    /**
     * 修改一对多
     */
    void updateMain(PlatformOrder platformOrder, List<PlatformOrderContent> platformOrderContentList);

    /**
     * 删除一对多
     */
    void delMain(String id);

    /**
     * 批量删除一对多
     */
    void delBatchMain(Collection<? extends Serializable> idList);

    void pendingPlatformOrderPage(IPage<ClientPlatformOrderPage> page);

    void purchasingPlatformOrderPage(IPage<ClientPlatformOrderPage> page);

    void processedPlatformOrderPage(IPage<ClientPlatformOrderPage> page);

    OrdersStatisticData getPlatformOrdersStatisticData(List<String> orderIds) throws UserException;

    List<PlatformOrderContent> selectByMainId(String mainId);

    List<ClientPlatformOrderContent> selectClientVersionByMainId(String mainId);

    PurchaseConfirmation confirmPurchaseByPlatformOrder(List<String> platformOrderIdList) throws UserException;

    PurchaseConfirmation confirmPurchaseBySkuQuantity(List<SkuQuantity> skuIDQuantityMap) throws UserException;

    PurchaseConfirmation confirmPurchaseBySkuQuantity(ClientInfo clientInfo, List<SkuQuantity> skuIDQuantityMap) throws UserException;

    List<OrderContentDetail> searchPurchaseOrderDetail(List<SkuQuantity> skuQuantities) throws UserException;

    OrderQuantity queryOrderQuantities();

    /**
     * Find all uninvoiced platform orders and its content of certain shops in a period.
     *
     * @param shopIds list of the shops
     * @param begin   begin of the period
     * @param end     end of the period
     * @return list of uninvoiced orders
     */
    Map<PlatformOrder, List<PlatformOrderContent>> findUninvoicedOrders(List<String> shopIds, Date begin, Date end, List<String> warehouses);

    /**
     * Find all uninvoiced platform orders by shop ID
     *
     * @return map of uninvoiced orders by shop ID
     */
    Map<String, Map<PlatformOrder, List<PlatformOrderContent>>> findUninvoicedOrders();

    /**
     * Find all uninvoiced platform orders and its content of certain shops between an order time period.
     *
     * @param shopIds list of the shops
     * @param begin   begin of the period
     * @param end     end of the period
     * @return list of uninvoiced orders
     */
    Map<PlatformOrder, List<PlatformOrderContent>> findUninvoicedOrderContentsForShopsAndStatus(List<String> shopIds, Date begin, Date end, List<Integer> erpStatuses, List<String> warehouses);

    /**
     * Fetch data for orders and their contents
     *
     * @param orderIds order IDs
     * @return list of pre-shipping orders and their contents
     */
    Map<PlatformOrder, List<PlatformOrderContent>> fetchOrderData(List<String> orderIds);

    /**
     * Find previous invoice code
     *
     * @return previous invoice code
     */
    String findPreviousInvoice();

    String findPreviousCompleteInvoice();

    /**
     * Find platform order quantity of each day for current login user.
     * If he's client, only orders that belonged to him will be taken into account, otherwise all orders will be included.
     *
     * @return platform order quantity of each day.
     */
    List<PlatformOrderQuantity> monthOrderNumber();

    List<String> fetchBillCodesOfParcelsWithoutTrace(Date startDate, Date endDate, List<String> transporters);

    List<String> fetchUninvoicedOrdersForShops(LocalDateTime startDate, LocalDateTime endDate, List<String> shops);

    /**
     * Fetch platformOrderId of shipped AND invoiced orders, from startDatetime to endDatetime, excluding orders from
     * shops whose codes are in shopCodes
     *
     * @param startDatetime                Start date time
     * @param endDatetime                  End date time
     * @param shopCodes                    Codes for shops which are to be excluded from request
     * @param excludedTrackingNumbersRegex Tracking numbers matching the REGEX to be excluded
     * @return List of PlatformOrderIDs
     */
    List<String> fetchInvoicedShippedOrdersNotInShops(LocalDateTime startDatetime, LocalDateTime endDatetime, List<String> shopCodes, String excludedTrackingNumbersRegex);

    List<PlatformOrderShopSync> fetchOrderInShopsReadyForShopifySync(List<String> shopCodes);

    List<PlatformOrder> fetchUninvoicedShippedOrderIDInShops(String startDate, String endDate, List<String> shops, List<String> warehouses);

    /**
     * Fetch all platform orders between 2 dates and of status erp_status 4 or 5
     * this list will then be archived
     * @param startDate Start date time
     * @param endDate End date time
     * @return List of PlatformOrder
     */
    List<PlatformOrder> fetchOrdersToArchiveBetweenDate(String startDate, String endDate);

    /**
     * Fetch all platform orders before endDate and of status erp_status 4 or 5
     * this list will then be archived
     *
     * @param endDate Start date time
     * @return List of PlatformOrder
     */
    List<PlatformOrder> fetchOrdersToArchiveBeforeDate(String endDate);

    /**
     * Archive a list of platform orders
     * @param platformOrders list of platform orders
     */
    void savePlatformOrderArchive(List<PlatformOrder> platformOrders);

    /**
     * Cancel Invoice
     * @param invoiceNumber
     */
    void cancelInvoice(String invoiceNumber);
    /**
     * Cancel Invoice
     * @param invoiceNumbers
     */
    void cancelBatchInvoice(List<String> invoiceNumbers);

    /**
     * Find all order that can be invoiced (shipping only).
     * @param shopIds list of shop id
     * @param erpStatuses list of erp_status
     * @return list of orders
     */
    List<PlatformOrder> findUninvoicedShippingOrdersByShopForClient(List<String> shopIds, List<Integer> erpStatuses);

    /**
     * Find all order that can be invoiced (shipping and purchase).
     * @param shopIds
     * @param erpStatuses
     * @return
     */
    List<PlatformOrder> findUninvoicedOrdersByShopForClient(List<String> shopIds, List<Integer> erpStatuses);
    /**
     * Get ids of all order that can be invoiced by small clients (type 2) themselves.
     * @param shopIds list of shop id
     * @param erpStatuses list of erp_status
     * @return list of orders
     */
    List<String> findUninvoicedOrderIdsByShopForClient(List<String> shopIds, List<Integer> erpStatuses);

    /**
     * Find all order with empty logistic_channel_name and invoice_logistic_channel_name
     * @param startDate
     * @param endDate
     * @return
     */
    List<PlatformOrder> fetchEmptyLogisticChannelOrders(String startDate, String endDate);

    Map<PlatformOrder, List<PlatformOrderContent>> fetchOrdersWithProductAvailable();

    List<PlatformOrder> fetchOrdersWithMissingStock(LocalDateTime start);

    List<PlatformOrder> selectByPlatformOrderIds(List<String> platformOrderIds);

    void removePurchaseInvoiceNumber(String purchaseInvoiceNumber);
    void removePurchaseInvoiceNumbers(List<String> invoiceNumbers);

    void updatePurchaseInvoiceNumber(List<String> orderIds, String invoiceCode);

    /**
     * Fetch all orders with productAvailable = 1, purchaseInvoiceNumber NOT NULL, invoiceNumber NULL and erp_status IN (1,2)
     * @return
     */
    List<ShippingFeeBillableOrders> fetchShippingFeeBillableOrders();
    List<PlatformOrder> getPlatformOrdersByInvoiceNumber(String invoiceNumber);

}
