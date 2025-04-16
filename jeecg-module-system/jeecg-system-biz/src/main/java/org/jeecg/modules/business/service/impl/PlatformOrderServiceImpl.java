package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.domain.api.yd.YDTrackingNumberData;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.ExchangeRatesMapper;
import org.jeecg.modules.business.mapper.PlatformOrderContentMapper;
import org.jeecg.modules.business.mapper.PlatformOrderMapper;
import org.jeecg.modules.business.service.IClientService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.service.IShippingFeesWaiverProductService;
import org.jeecg.modules.business.vo.*;
import org.jeecg.modules.business.vo.clientPlatformOrder.ClientPlatformOrderPage;
import org.jeecg.modules.business.vo.clientPlatformOrder.PurchaseConfirmation;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.ClientInfo;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.OrderQuantity;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.OrdersStatisticData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * @Description: 平台订单表
 * @Author: jeecg-boot
 * @Date: 2021-04-08
 * @Version: V1.0
 */
@Service
@Slf4j
public class PlatformOrderServiceImpl extends ServiceImpl<PlatformOrderMapper, PlatformOrder> implements IPlatformOrderService {

    private final PlatformOrderMapper platformOrderMap;
    private final PlatformOrderContentMapper platformOrderContentMap;
    private final IShippingFeesWaiverProductService shippingFeesWaiverProductService;
    private final IClientService clientService;
    private final ExchangeRatesMapper exchangeRatesMapper;

    @Autowired
    public PlatformOrderServiceImpl(PlatformOrderMapper platformOrderMap, PlatformOrderContentMapper platformOrderContentMap,
                                    IShippingFeesWaiverProductService shippingFeesWaiverProductService, IClientService clientService,
                                    ExchangeRatesMapper exchangeRatesMapper) {
        this.platformOrderMap = platformOrderMap;
        this.platformOrderContentMap = platformOrderContentMap;
        this.shippingFeesWaiverProductService = shippingFeesWaiverProductService;
        this.clientService = clientService;
        this.exchangeRatesMapper = exchangeRatesMapper;
    }

    @Override
    @Transactional
    public void saveMain(PlatformOrder platformOrder, List<PlatformOrderContent> platformOrderContentList) {
        platformOrderMap.insert(platformOrder);
        if (platformOrderContentList != null && platformOrderContentList.size() > 0) {
            for (PlatformOrderContent entity : platformOrderContentList) {
                //外键设置
                entity.setStatus(platformOrder.getStatus());
                entity.setPlatformOrderId(platformOrder.getId());
                platformOrderContentMap.insert(entity);
            }
        }
    }

    @Transactional
    public boolean saveBatch(Map<PlatformOrder, List<PlatformOrderContent>> orderMap) {
        String orderInsertStm = SqlHelper.getSqlStatement(PlatformOrderMapper.class, SqlMethod.INSERT_ONE);
        String orderContentInsertStm = SqlHelper.getSqlStatement(PlatformOrderContentMapper.class, SqlMethod.INSERT_ONE);

        return this.executeBatch((sqlSession) -> {
            for (Map.Entry<PlatformOrder, List<PlatformOrderContent>> entry : orderMap.entrySet()) {
                PlatformOrder platformOrder = entry.getKey();
                List<PlatformOrderContent> platformOrderContentList = entry.getValue();
                sqlSession.insert(orderInsertStm, platformOrder);
                if (platformOrderContentList != null && platformOrderContentList.size() > 0) {
                    for (PlatformOrderContent orderContent : platformOrderContentList) {
                        //外键设置
                        orderContent.setStatus(platformOrder.getStatus());
                        orderContent.setPlatformOrderId(platformOrder.getId());
                        sqlSession.insert(orderContentInsertStm, orderContent);
                    }
                }
            }
        });
    }

    @Override
    @Transactional
    public void updateMain(PlatformOrder platformOrder, List<PlatformOrderContent> platformOrderContentList) {
        platformOrderMap.updateById(platformOrder);

        //1.先删除子表数据
        platformOrderContentMap.deleteByMainId(platformOrder.getId());

        //2.子表数据重新插入
        if (platformOrderContentList != null && platformOrderContentList.size() > 0) {
            for (PlatformOrderContent entity : platformOrderContentList) {
                //外键设置
                entity.setStatus(platformOrder.getStatus());
                platformOrderContentMap.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        platformOrderContentMap.deleteByMainId(id);
        platformOrderMap.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            platformOrderContentMap.deleteByMainId(id.toString());
            platformOrderMap.deleteById(id);
        }
    }

    @Override
    public void pendingPlatformOrderPage(IPage<ClientPlatformOrderPage> page) {
        // search client id for current user
        Client client = clientService.getCurrentClient();
        // in case of other roles
        if (null == client) {
            page.setRecords(Collections.emptyList());
            page.setTotal(0);
        } else {
            String clientId = client.getId();
            List<ClientPlatformOrderPage> orders = platformOrderMap.pagePendingOrderByClientId(clientId, page.offset(), page.getSize());
            page.setRecords(orders);
            page.setTotal(platformOrderMap.queryQuantities(clientId).getPending());
        }
    }

    @Override
    public void purchasingPlatformOrderPage(IPage<ClientPlatformOrderPage> page) {
        // search client id for current user
        Client client = clientService.getCurrentClient();
        // in case of other roles
        if (null == client) {
            page.setRecords(Collections.emptyList());
            page.setTotal(0);
        } else {
            String clientId = client.getId();
            List<ClientPlatformOrderPage> orders = platformOrderMap.pagePurchasingOrderByClientId(clientId, page.offset(), page.getSize());
            page.setRecords(orders);
            page.setTotal(platformOrderMap.queryQuantities(clientId).getPurchasing());
        }
    }

    @Override
    public void processedPlatformOrderPage(IPage<ClientPlatformOrderPage> page) {
        // search client id for current user
        Client client = clientService.getCurrentClient();
        // in case of other roles
        if (null == client) {
            page.setRecords(Collections.emptyList());
            page.setTotal(0);
        } else {
            String clientId = client.getId();
            List<ClientPlatformOrderPage> orders = platformOrderMap.pageProcessedOrderByClientId(clientId, page.offset(), page.getSize());
            page.setRecords(orders);
            page.setTotal(platformOrderMap.queryQuantities(clientId).getProcessed());
        }
    }

    @Override
    public OrdersStatisticData getPlatformOrdersStatisticData(List<String> orderIds) throws UserException {
        List<SkuQuantity> skuIDQuantityMap = platformOrderContentMap.searchOrderContent(orderIds);
        List<OrderContentDetail> data = searchPurchaseOrderDetail(skuIDQuantityMap);
        return OrdersStatisticData.makeData(data, null);
    }


    @Override
    public List<PlatformOrderContent> selectByMainId(String mainId) {
        return platformOrderContentMap.selectByMainId(mainId);
    }

    @Override
    public List<ClientPlatformOrderContent> selectClientVersionByMainId(String mainId) {
        return platformOrderContentMap.selectClientVersionByMainId(mainId);
    }

    @Override
    public PurchaseConfirmation confirmPurchaseByPlatformOrder(List<String> platformOrderIdList) throws UserException {
        List<SkuQuantity> skuIDQuantityMap = platformOrderContentMap.searchOrderContent(platformOrderIdList);
        return confirmPurchaseBySkuQuantity(skuIDQuantityMap);
    }


    @Override
    public PurchaseConfirmation confirmPurchaseBySkuQuantity(List<SkuQuantity> skuIDQuantityMap) throws UserException {
        Client client = clientService.getCurrentClient();
        ClientInfo clientInfo = new ClientInfo(client);
        return new PurchaseConfirmation(clientInfo, searchPurchaseOrderDetail(skuIDQuantityMap),
                getShippingFeesWaiverMap(skuIDQuantityMap.stream().map(SkuQuantity::getID).collect(toList())));
    }

    @Override
    public PurchaseConfirmation confirmPurchaseBySkuQuantity(ClientInfo clientInfo, List<SkuQuantity> skuIDQuantityMap) throws UserException {
        return new PurchaseConfirmation(clientInfo, searchPurchaseOrderDetail(skuIDQuantityMap),
                getShippingFeesWaiverMap(skuIDQuantityMap.stream().map(SkuQuantity::getID).collect(toList())));
    }

    @Override
    public List<OrderContentDetail> searchPurchaseOrderDetail(List<SkuQuantity> skuQuantities) throws UserException {
        BigDecimal eurToRmb = exchangeRatesMapper.getLatestExchangeRate("EUR", "RMB");
        // convert list of (ID, quantity) to map between ID and quantity
        Map<String, Integer> skuQuantity =
                skuQuantities.stream()
                        .collect(
                                Collectors.toMap(
                                        SkuQuantity::getID,
                                        SkuQuantity::getQuantity
                                )
                        );

        // Get list of sku ID
        List<String> skuList = new ArrayList<>(skuQuantity.keySet());

        List<SkuDetail> skuDetails = platformOrderContentMap.searchSkuDetail(skuList);
        for(SkuDetail detail : skuDetails) {
            if(detail.getPrice().getId() == null || detail.getPrice().getPrice() == null) {
                throw new UserException("SKU " + detail.getSkuId() + " has no price or price id");
            }
        }
        List<OrderContentDetail> details = skuDetails.stream()
                .map(
                        skuDetail -> new OrderContentDetail(
                                skuDetail,
                                skuQuantity.get(skuDetail.getSkuId()),
                                eurToRmb
                        )
                )
                .collect(toList());
        log.info(details.toString());
        // SKU ID -> SKU detail -- (quantity) --> Order Content Detail
        return details;
    }

    public Map<ShippingFeesWaiver, List<String>> getShippingFeesWaiverMap(List<String> skuIds) {
        List<SkuShippingFeesWaiver> skuShippingFeesWaivers = shippingFeesWaiverProductService.selectBySkuIds(skuIds);
        Map<ShippingFeesWaiver, List<String>> waiverSkuIdsMap = skuShippingFeesWaivers.stream().collect(
                groupingBy(
                        SkuShippingFeesWaiver::getShippingFeesWaiver,
                        collectingAndThen(mapping(SkuShippingFeesWaiver::getSkuId, toList()), Collections::unmodifiableList)));
        return waiverSkuIdsMap;
    }

    @Override
    public OrderQuantity queryOrderQuantities() {
        // search client id for current user
        Client client = clientService.getCurrentClient();
        // in case of other roles
        if (null == client) {
            return new OrderQuantity();
        } else {
            return platformOrderMap.queryQuantities(client.getId());
        }
    }

    @Override
    public Map<PlatformOrder, List<PlatformOrderContent>> findUninvoicedOrders(List<String> shopIds, Date begin, Date end, List<String> warehouses) {
        List<PlatformOrder> orderList = platformOrderMap.findUninvoicedOrders(shopIds, begin, end, warehouses);
        List<PlatformOrderContent> orderContents = platformOrderContentMap.fetchOrderContent(orderList.stream().map(PlatformOrder::getId).collect(toList()));
        Map<String, PlatformOrder> orderMap = orderList.stream().collect(toMap(PlatformOrder::getId, Function.identity()));
        return orderContents.stream().collect(groupingBy(platformOrderContent -> orderMap.get(platformOrderContent.getPlatformOrderId())));
    }

    @Override
    public Map<String, Map<PlatformOrder, List<PlatformOrderContent>>> findUninvoicedOrders() {
        List<PlatformOrder> orderList = platformOrderMap.findUninvoicedShippedOrders();
        List<PlatformOrderContent> orderContents = platformOrderContentMap.findUninvoicedShippedOrderContents();
        Map<String, PlatformOrder> orderMap = orderList.stream().collect(toMap(PlatformOrder::getId, Function.identity()));
        Map<String, String> orderMapByShopId = orderList.stream().collect(toMap(PlatformOrder::getId, PlatformOrder::getShopId));
        return orderContents.stream()
                .collect(
                        groupingBy(
                                platformOrderContent -> orderMapByShopId.get(platformOrderContent.getPlatformOrderId()),
                                groupingBy(platformOrderContent -> orderMap.get(platformOrderContent.getPlatformOrderId()))
                        )
                );
    }
    @Override
    public Map<PlatformOrder, List<PlatformOrderContent>> findUninvoicedOrderContentsForShopsAndStatus(List<String> shopIds, Date begin, Date end, List<Integer> erpStatuses, List<String> warehouses) {
        List<PlatformOrder> orderList = platformOrderMap.findUninvoicedOrdersForShopsAndStatus(shopIds, begin, end, erpStatuses);
        List<PlatformOrderContent> orderContents = platformOrderContentMap.findUninvoicedOrderContentsForShopsAndStatus(shopIds, begin, end, erpStatuses);
        Map<String, PlatformOrder> orderMap = orderList.stream().collect(toMap(PlatformOrder::getId, Function.identity()));
        return orderContents.stream().collect(groupingBy(platformOrderContent -> orderMap.get(platformOrderContent.getPlatformOrderId())));
    }

    @Override
    public Map<PlatformOrder, List<PlatformOrderContent>> fetchOrderData(List<String> orderIds) {
        List<PlatformOrder> orderList = platformOrderMap.selectBatchIds(orderIds);
        List<PlatformOrderContent> orderContents = platformOrderContentMap.fetchOrderContent(orderIds);
        Map<String, PlatformOrder> orderMap = orderList.stream().collect(toMap(PlatformOrder::getId, Function.identity()));
        return orderContents.stream().collect(groupingBy(platformOrderContent -> orderMap.get(platformOrderContent.getPlatformOrderId())));
    }
    @Override
    public Map<PlatformOrder, List<PlatformOrderContent>> fetchOrderDataByInvoiceCode(String invoiceCode) {
        List<PlatformOrder> orderList = platformOrderMap.getPlatformOrdersByInvoiceNumber(invoiceCode);
        List<String> orderIds = orderList.stream().map(PlatformOrder::getId).collect(toList());
        List<PlatformOrderContent> orderContents = platformOrderContentMap.fetchOrderContent(orderIds);
        Map<String, PlatformOrder> orderMap = orderList.stream().collect(toMap(PlatformOrder::getId, Function.identity()));
        return orderContents.stream().collect(groupingBy(platformOrderContent -> orderMap.get(platformOrderContent.getPlatformOrderId())));

    }

    @Override
    public String findPreviousInvoice() {
        return platformOrderMap.findPreviousInvoice();
    }

    @Override
    public String findPreviousCompleteInvoice() {
        return platformOrderMap.findPreviousCompleteInvoice();
    }

    @Override
    public List<PlatformOrderQuantity> monthOrderNumber() {
        Client client = clientService.getCurrentClient();
        List<PlatformOrder> orders = platformOrderMap.findByClient(client.getId());
        if (orders.isEmpty())
            return Collections.emptyList();

        LocalDate now = LocalDate.now();

        Predicate<PlatformOrder> sameYearAndMonthOrder = order -> {
            LocalDate localDate = order.getOrderTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return localDate.getYear() == now.getYear() && now.getMonth().equals(localDate.getMonth());
        };

        return orders.stream()
                .filter(sameYearAndMonthOrder)
                .collect(
                        Collectors.groupingBy(
                                (order) -> order.getOrderTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        )
                )
                .entrySet()
                .stream()
                .map(
                        entry -> new PlatformOrderQuantity(entry.getKey(), entry.getValue().size())

                )
                .collect(toList());
    }

    @Override
    public List<String> fetchBillCodesOfParcelsWithoutTrace(Date startDate, Date endDate, List<String> transporters) {
        return platformOrderMap.fetchBillCodesOfParcelsWithoutTrace(startDate, endDate, transporters);
    }

    @Override
    public List<PlatformOrder> fetchUninvoicedOrdersForShops(LocalDateTime startDate, LocalDateTime endDate, List<String> shops) {
        return platformOrderMap.fetchUninvoicedOrdersForShops(startDate, endDate, shops);
    }

    @Override
    public List<String> fetchInvoicedShippedOrdersNotInShops(LocalDateTime startDatetime, LocalDateTime endDatetime, List<String> shopCodes, String excludedTrackingNumbersRegex) {
        return platformOrderMap.fetchInvoicedShippedOrdersNotInShops(startDatetime, endDatetime, shopCodes, excludedTrackingNumbersRegex);
    }

    @Override
    public List<PlatformOrderShopSync> fetchOrderInShopsReadyForShopifySync(List<String> shopCodes) {
        return platformOrderMap.fetchOrderInShopsReadyForShopifySync(shopCodes);
    }

    @Override
    public List<PlatformOrderShopSync> fetchOrderInShopsWithoutShopifyNote(List<String> shopCodes) {
        return platformOrderMap.fetchOrderInShopsWithoutShopifyNote(shopCodes);
    }

    @Override
    public List<PlatformOrder> fetchOrderInShopsReadyForAbnNumberJob(List<String> shopCodes) {
        return platformOrderMap.fetchOrderInShopsReadyForAbnNumberJob(shopCodes);
    }

    @Override
    public List<PlatformOrder> fetchUninvoicedShippedOrderIDInShops(String startDate, String endDate, List<String> shops, List<String> warehouses) {
        return platformOrderMap.fetchUninvoicedShippedOrderIDInShops(startDate, endDate, shops, warehouses);
    }
    @Override
    public List<PlatformOrder> fetchOrdersToArchiveBetweenDate(String startDate, String endDate) {
        return platformOrderMap.fetchOrdersToArchiveBetweenDate(startDate, endDate);
    }
    @Override
    public List<PlatformOrder> fetchOrdersToArchiveBeforeDate(String endDate) {
        return platformOrderMap.fetchOrdersToArchiveBeforeDate(endDate);
    }
    @Override
    public void savePlatformOrderArchive(List<PlatformOrder> platformOrders) {
        platformOrderMap.insertPlatformOrdersArchives(platformOrders);
    }
    @Override
    public void cancelInvoice(String invoiceNumber, String clientId) {
        platformOrderMap.cancelInvoice(invoiceNumber, clientId);
    }
    @Override
    public void cancelBatchInvoice(List<String> invoiceNumbers) {
        platformOrderMap.cancelBatchInvoice(invoiceNumbers);
    }

    @Override
    public List<PlatformOrder> findUninvoicedShippingOrdersByShopForClient(List<String> shopIds, List<Integer> erpStatuses) {
        return platformOrderMap.findUninvoicedShippingOrdersByShopForClient(shopIds, erpStatuses);
    }

    @Override
    public List<PlatformOrder> fetchUninvoicedPurchaseOrdersByShopForClient(List<String> shopIds, List<Integer> erpStatuses) {
        return platformOrderMap.fetchUninvoicedPurchaseOrdersByShopForClient(shopIds, erpStatuses);
    }

    @Override
    public List<PlatformOrder> findUninvoicedOrdersByShopForClient(List<String> shopIds, List<Integer> erpStatuses, String column, String order, Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        return platformOrderMap.findUninvoicedOrdersByShopForClient(shopIds, erpStatuses, column, order, offset, pageSize);
    }
    @Override
    public List<PlatformOrderFront> fetchUninvoicedOrdersByShopForClientFullSQL(List<String> shopIds, List<Integer> erpStatuses, String column, String order, Integer pageNo, Integer pageSize,
                                                                                List<Integer> productAvailable, List<Integer> shippingAvailable, List<Integer> purchaseAvailable, Date startDate, Date endDate) {
        int offset = (pageNo - 1) * pageSize;
        return platformOrderMap.fetchUninvoicedOrdersByShopForClientFullSQL(shopIds, erpStatuses, column, order, offset, pageSize, productAvailable, shippingAvailable, purchaseAvailable, startDate, endDate);
    }

    @Override
    public List<String> findUninvoicedOrderIdsByShopForClient(List<String> shopIds, List<Integer> erpStatuses) {
        return platformOrderMap.findUninvoicedOrderIdsByShopForClient(shopIds, erpStatuses);
    }

    @Override
    public List<PlatformOrder> fetchEmptyLogisticChannelOrders(String startDate, String endDate) {
        return platformOrderMap.fetchEmptyLogisticChannelOrders(startDate, endDate);
    }

    @Override
    public Map<PlatformOrder, List<PlatformOrderContent>> fetchOrdersWithProductAvailable() {
        List<PlatformOrder> orderList = platformOrderMap.fetchOrdersWithProductAvailable();
        List<String> orderIdsWithProductAvailable = orderList.stream().map(PlatformOrder::getId).collect(toList());
        List<PlatformOrderContent> orderContents = platformOrderContentMap.fetchOrderContent(orderIdsWithProductAvailable);
        Map<String, PlatformOrder> orderMap = orderList.stream().collect(toMap(PlatformOrder::getId, Function.identity()));
        return orderContents.stream().collect(groupingBy(platformOrderContent -> orderMap.get(platformOrderContent.getPlatformOrderId())));
    }

    @Override
    public List<PlatformOrder> fetchOrdersWithMissingStock(LocalDateTime start) {
        return platformOrderMap.fetchOrdersWithMissingStock(start);
    }

    @Override
    public List<PlatformOrder> selectByPlatformOrderIds(List<String> platformOrderIds) {
        return platformOrderMap.selectByPlatformOrderIds(platformOrderIds);
    }

    @Override
    public void removePurchaseInvoiceNumber(String purchaseInvoiceNumber, String clientId) {
        platformOrderMap.removePurchaseInvoiceNumber(purchaseInvoiceNumber, clientId);
    }

    @Override
    public void removePurchaseInvoiceNumbers(List<String> invoiceNumbers) {
        platformOrderMap.removePurchaseInvoiceNumbers(invoiceNumbers);
    }

    @Override
    public void updatePurchaseInvoiceNumber(List<String> orderIds, String invoiceCode) {
        platformOrderMap.updatePurchaseInvoiceNumber(orderIds, invoiceCode);
    }

    @Override
    public List<ShippingFeeBillableOrders> fetchShippingFeeBillableOrders() {
        return platformOrderMap.fetchShippingFeeBillableOrders();
    }

    @Override
    public List<PlatformOrder> getPlatformOrdersByInvoiceNumber(String invoiceNumber) {
        return platformOrderMap.getPlatformOrdersByInvoiceNumber(invoiceNumber);
    }

    @Override
    public Map<String, String> fetchShippingPeriodAndType(String invoiceNumber) {
        return platformOrderMap.fetchShippingPeriodAndType(invoiceNumber);
    }

    @Override
    public void anonymizePersonalData(int indirectClientAnonymizationPeriod) {
        platformOrderMap.anonymizePersonalData(indirectClientAnonymizationPeriod);
    }

    @Override
    public List<PlatformOrderOption> ordersByShop(String shopID) {
        return platformOrderMap.ordersByShop(shopID);
    }

    @Override
    public List<String> fetchUninvoicedOrdersWithSkusInCountry(LocalDateTime startDateTime, LocalDateTime endDateTime, String shop, List<String> skus, List<String> countries) {
        return platformOrderMap.fetchUninvoicedOrdersWithSkusInCountry(startDateTime, endDateTime, shop, skus, countries);
    }
    @Override
    public List<String> fetchUninvoicedOrdersWithSkusNotInCountry(LocalDateTime startDateTime, LocalDateTime endDateTime, String shop, List<String> skus, List<String> countries) {
        return platformOrderMap.fetchUninvoicedOrdersWithSkusNotInCountry(startDateTime, endDateTime, shop, skus, countries);
    }

    @Override
    public List<String> findReadyAbnormalOrders(List<String> skus, List<String> shops) {
        return platformOrderMap.findReadyAbnormalOrders(skus, shops);
    }

    @Override
    public List<String> findReadyAbnormalOrdersWithSkus(List<String> skus) {
        return platformOrderMap.findReadyAbnormalOrdersWithSkus(skus);
    }

    @Override
    public void updateShopifySynced(Collection<String> platformOrderIds) {
        platformOrderMap.updateShopifySynced(platformOrderIds);
    }

    @Override
    public List<String> fetchShippedOrdersFromShopAndTransporters(String shopCode, List<String> transporters) {
        return platformOrderMap.fetchShippedOrdersFromShopAndTransporters(shopCode, transporters);
    }

    @Override
    public void updateLocalTrackingNumber(List<YDTrackingNumberData> data) {
        platformOrderMap.updateLocalTrackingNumber(data);
    }

    @Override
    public void pagePotentialShoumanOrders(IPage<PlatformOrderPage> page, String column, String order) {
        List<PlatformOrderPage> potentialShoumanOrders = platformOrderMap.pagePotentialShoumanOrders(page.offset(), page.getSize(), column, order);
        page.setRecords(potentialShoumanOrders);
        page.setTotal(platformOrderMap.countPotentialShoumanOrders());
    }

}
