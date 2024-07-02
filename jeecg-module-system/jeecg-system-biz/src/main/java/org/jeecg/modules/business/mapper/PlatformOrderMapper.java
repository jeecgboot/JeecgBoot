package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.Order;
import org.jeecg.modules.business.domain.api.yd.YDTrackingNumberData;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderShopSync;
import org.jeecg.modules.business.vo.OrderKpi;
import org.jeecg.modules.business.vo.PlatformOrderOption;
import org.jeecg.modules.business.vo.ShippingFeeBillableOrders;
import org.jeecg.modules.business.vo.clientPlatformOrder.ClientPlatformOrderPage;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.OrderQuantity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description: 平台订单表
 * @Author: jeecg-boot
 * @Date: 2021-04-08
 * @Version: V1.0
 */
@Repository
public interface PlatformOrderMapper extends BaseMapper<PlatformOrder> {

    List<ClientPlatformOrderPage> pagePendingOrderByClientId(@Param("clientId") String clientId, @Param("offset") long offset, @Param("size") long size);

    List<ClientPlatformOrderPage> pagePurchasingOrderByClientId(@Param("clientId") String clientId, @Param("offset") long offset, @Param("size") long size);

    List<ClientPlatformOrderPage> pageProcessedOrderByClientId(@Param("clientId") String clientId, @Param("offset") long offset, @Param("size") long size);

    /**
     * Update a platform's status.
     *
     * @param orderID identifier of the platform order to update
     * @param status  the status to set
     */
    void updateStatus(@Param("orderID") String orderID, @Param("status") int status);

    /**
     * Update a batch of platforms' status
     *
     * @param orderIDList identifiers of platform orders to update
     * @param status      the status to set
     */
    void batchUpdateStatus(@Param("orderIDList") List<String> orderIDList, @Param("status") int status);

    OrderQuantity queryQuantities(@Param("clientId") String clientId);

    /**
     * Insert platform order data from Mabang side.
     * replace shop erp code by shop id.
     *
     * @param order the order to insert
     */
    void insertFromMabangOrder(Order order);

    /**
     * Get platform order ID by erp code.
     *
     * @param platformOrderNumber erp code == platform order number
     */
    String findIdByErpCode(String platformOrderNumber);

    /**
     * Update merged order.
     * Mark in the sources their target as ID of target.
     *
     * @param targetID  ID of target platform order
     * @param sourceIDs IDs of source platform order
     */
    void updateMergedOrder(@Param("target") String targetID, @Param("sources") List<String> sourceIDs);

    /**
     * Find all uninvoiced platform orders of certain shops in a period.
     *
     * @param shopIds list of the shops
     * @param begin   begin of the period
     * @param end     end of the period
     * @return list of uninvoiced orders
     */
    List<PlatformOrder> findUninvoicedOrders(
            @Param("shopIDs") List<String> shopIds,
            @Param("begin") Date begin,
            @Param("end") Date end,
            @Param("warehouses") List<String> warehouses
    );

    /**
     * Find all uninvoiced platform orders
     *
     * @return list of uninvoiced orders
     */
    List<PlatformOrder> findUninvoicedShippedOrders();
    /**
     * Find all uninvoiced platform orders of certain shops between order time period.
     *
     * @param shopIds list of the shops
     * @param begin   begin of the period
     * @param end     end of the period
     * @param erpStatuses list of erp status
     * @return list of uninvoiced orders
     */
    List<PlatformOrder> findUninvoicedOrdersForShopsAndStatus(
            @Param("shopIDs") List<String> shopIds,
            @Param("begin") Date begin,
            @Param("end") Date end,
            @Param("erpStatuses") List<Integer> erpStatuses
    );
    /**
     * Find previous invoice code.
     *
     * @return previous code.
     */
    String findPreviousInvoice();

    String findPreviousCompleteInvoice();

    Date findEarliestUninvoicedPlatformOrder(@Param("shopIDs") List<String> shopIDs);

    Date findLatestUninvoicedPlatformOrder(@Param("shopIDs") List<String> shopIDs);

    Date findEarliestUninvoicedPlatformOrderTime(@Param("shopIDs") List<String> shopIDs, @Param("erpStatuses") List<Integer> erpStatuses);

    Date findLatestUninvoicedPlatformOrderTime(@Param("shopIDs") List<String> shopIDs, @Param("erpStatuses") List<Integer> erpStatuses);

    /**
     * Find all platform order containing a particular sku
     *
     * @param id ID of the sku
     * @return list of the orders
     */
    List<PlatformOrder> findBySku(@Param("id") String id);

    /**
     * Find platform orders by client. If ID is null, all platform order will be returned.
     *
     * @param id ID of client
     * @return list of platform orders of the client
     */
    List<PlatformOrder> findByClient(@Param("id") String id);


    /**
     * Find platform order by month country and channel
     *
     * @param month       1 - 12
     * @param country     full country name for example: France, in case of null, all countries will be took into count
     * @param channelName chinese channel name in case of null, all channels will be took into count
     * @return plat form orders that fit
     */
    List<PlatformOrder> findByMonthCountryChannel(@Param("month") int month, @Param("country") String country, @Param("channelName") String channelName);

    List<String> allCountries();

    List<String> allChannels();

    List<String> fetchBillCodesOfParcelsWithoutTrace(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                                     @Param("transporters") List<String> transporters);

    List<PlatformOrder> fetchUninvoicedOrdersForShops(@Param("startDateTime") LocalDateTime startDateTime,
                                               @Param("endDateTime") LocalDateTime endDateTime,
                                               @Param("shops") List<String> shops);

    List<String> fetchInvoicedShippedOrdersNotInShops(@Param("startDateTime") LocalDateTime startDatetime,
                                                      @Param("endDateTime") LocalDateTime endDatetime,
                                                      @Param("shops") List<String> shopCodes,
                                                      @Param("excludedTrackingNumbersRegex") String excludedTrackingNumbersRegex);

    List<PlatformOrderShopSync> fetchOrderInShopsReadyForShopifySync(@Param("shops") List<String> shopCodes);
    List<PlatformOrderShopSync> fetchOrderInShopsWithoutShopifyNote(@Param("shops") List<String> shopCodes);
    List<PlatformOrder> fetchOrderInShopsReadyForAbnNumberJob(@Param("shops") List<String> shopCodes);

    List<PlatformOrder> fetchUninvoicedShippedOrderIDInShops(@Param("startDate") String startDate,
                                                             @Param("endDate") String endDate,
                                                             @Param("shops") List<String> shops,
                                                             @Param("warehouses") List<String> warehouses);

    List<PlatformOrder> fetchUninvoicedOrderIDInShopsAndOrderTime(@Param("startDate") String startDate,
                                                                         @Param("endDate") String endDate,
                                                                         @Param("shops") List<String> shops,
                                                                         @Param("erpStatuses") List<Integer> erpStatuses,
                                                                         @Param("warehouses") List<String> warehouses);
    List<PlatformOrder> fetchOrdersToArchiveBetweenDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
    List<PlatformOrder> fetchOrdersToArchiveBeforeDate(@Param("endDate") String endDate);
    void insertPlatformOrdersArchives(@Param("orders") List<PlatformOrder> platformOrders);
    void cancelInvoice(@Param("invoiceNumber") String invoiceNumber, @Param("clientId") String clientId);
    void cancelBatchInvoice(@Param("invoiceNumbers") List<String> invoiceNumbers);

    List<PlatformOrder> findUninvoicedShippingOrdersByShopForClient(@Param("shopIds") List<String> shopIds, @Param("erpStatuses") List<Integer> erpStatuses);
    List<PlatformOrder> fetchUninvoicedPurchaseOrdersByShopForClient(@Param("shopIds") List<String> shopIds, @Param("erpStatuses") List<Integer> erpStatuses);
    List<PlatformOrder> findUninvoicedOrdersByShopForClient(@Param("shopIds") List<String> shopIds, @Param("erpStatuses") List<Integer> erpStatuses,
                                                                  @Param("column") String column, @Param("order") String order, @Param("offset") Integer offset, @Param("size") Integer pageSize);
    List<String> findUninvoicedOrderIdsByShopForClient(@Param("shopIds") List<String> shopIds, @Param("erpStatuses") List<Integer> erpStatuses);

    List<PlatformOrder> fetchEmptyLogisticChannelOrders(@Param("startDate") String startDate,@Param("endDate") String endDate);

    void updateErpStatusByCode(@Param("invoiceCode") String invoiceCode, @Param("erpStatus") int erpStatus);

    List<PlatformOrder> fetchByIds(@Param("orderIds") List<String> orderNumbers);

    List<PlatformOrder> fetchOrdersWithProductAvailable();

    List<PlatformOrder> fetchOrdersWithMissingStock(@Param("start") LocalDateTime start);

    List<PlatformOrder> selectByPlatformOrderIds(@Param("platformOrderIds") List<String> platformOrderIds);

    void removePurchaseInvoiceNumber(@Param("invoiceNumber") String purchaseInvoiceNumber, @Param("clientId") String clientId);

    void removePurchaseInvoiceNumbers(@Param("invoiceNumbers") List<String> invoiceNumbers);

    void updatePurchaseInvoiceNumber(@Param("orderIds") List<String> orderIds, @Param("invoiceNumber") String invoiceNumber);

    List<ShippingFeeBillableOrders> fetchShippingFeeBillableOrders();

    List<PlatformOrder> getPlatformOrdersByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);

    OrderKpi countPlatformOrders(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("showAllData") boolean showAllData, @Param("username") String username);

    Map<String, String> fetchShippingPeriodAndType(@Param("invoiceNumber") String invoiceNumber);

    void anonymizePersonalData(@Param("period") int indirectClientAnonymizationPeriod);

    List<PlatformOrderOption> ordersByShop(@Param("shopID") String shopID);

    List<String> fetchUninvoicedOrdersWithSkusInCountry(@Param("startDate") LocalDateTime startDateTime, @Param("endDate") LocalDateTime endDateTime, @Param("shop") String shop, @Param("skus") List<String> skus, @Param("countries") List<String> countries);
    List<String> fetchUninvoicedOrdersWithSkusNotInCountry(@Param("startDate") LocalDateTime startDateTime, @Param("endDate") LocalDateTime endDateTime, @Param("shop") String shop, @Param("skus") List<String> skus, @Param("countries") List<String> countries);

    List<String> findReadyAbnormalOrders(@Param("skus") List<String> skus, @Param("shops") List<String> shops);

    List<String> findReadyAbnormalOrdersWithSkus(@Param("skus") List<String> skus);
    void updateShopifySynced(@Param("platformOrderIds") Collection<String> platformOrderIds);

    List<String> fetchShippedOrdersFromShopAndTransporters(@Param("shopCode")String shopCode, @Param("transporters") List<String> transporters);

    void updateLocalTrackingNumber(@Param("data") List<YDTrackingNumberData> data);
}
