package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.*;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequest;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendResponse;
import org.jeecg.modules.business.domain.api.mabang.orderUpdateOrderNewOrder.OrderUpdateOrderNewOrderRequest;
import org.jeecg.modules.business.domain.api.mabang.orderUpdateOrderNewOrder.OrderUpdateOrderNewOrderRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderUpdateOrderNewOrder.OrderUpdateOrderNewOrderResponse;
import org.jeecg.modules.business.domain.api.mabang.orderUpdateOrderNewOrder.UpdateResult;
import org.jeecg.modules.business.domain.job.ThrottlingExecutorService;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.mapper.PlatformOrderMabangMapper;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.vo.PlatformOrderOperation;
import org.jeecg.modules.business.vo.Response;
import org.jeecg.modules.business.vo.Responses;
import org.jeecg.modules.business.vo.ResponsesWithMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @Description: 平台订单表
 * @Author: jeecg-boot
 * @Date: 2021-04-08
 * @Version: V1.0
 */
@Service
@Slf4j
public class PlatformOrderMabangServiceImpl extends ServiceImpl<PlatformOrderMabangMapper, Order> implements IPlatformOrderMabangService {
    @Autowired
    private PlatformOrderMabangMapper platformOrderMabangMapper;
    @Autowired
    private IPlatformOrderService orderservice;
    @Autowired
    private IPlatformOrderService platformOrderService;

    private static final Integer DEFAULT_NUMBER_OF_THREADS = 2;
    private static final Integer MABANG_API_RATE_LIMIT_PER_MINUTE = 10;

    @Override
    @Transactional
    public void saveOrderFromMabang(List<Order> orders) {
        if (orders.isEmpty()) {
            return;
        }
        // find orders that already exist in DB
        List<String> allPlatformOrderId = orders.stream()
                .map(Order::getPlatformOrderId)
                .collect(toList());
        List<PlatformOrder> existingOrders = platformOrderMabangMapper.searchExistence(allPlatformOrderId);

        Map<String, PlatformOrder> platformOrderIDToExistOrders = existingOrders.stream()
                .collect(
                        Collectors.toMap(
                                PlatformOrder::getPlatformOrderId, Function.identity()
                        )
                );

        ArrayList<Order> newOrders = new ArrayList<>();
        ArrayList<Order> oldOrders = new ArrayList<>();
        ArrayList<Order> ordersFromShippedToCompleted = new ArrayList<>();
        ArrayList<Order> invoicedShippedOrders = new ArrayList<>();
        ArrayList<Order> obsoleteOrders = new ArrayList<>();
        for (Order retrievedOrder : orders) {
            PlatformOrder orderInDatabase = platformOrderIDToExistOrders.get(retrievedOrder.getPlatformOrderId());
            if (orderInDatabase == null) {
                newOrders.add(retrievedOrder);
            } else {
                // re-affect retrieved orders with ID in database
                retrievedOrder.setId(orderInDatabase.getId());
                // For orders that pass from Shipped to Completed, we must NOT delete then re-insert their contents
                // Because we would lose all calculated fees (shipping, vat, service)
                if (retrievedOrder.getStatus().equals(OrderStatus.Completed.getCode())) {
                    if (orderInDatabase.getErpStatus().equals(OrderStatus.Shipped.getCode())
                            || orderInDatabase.getErpStatus().equals(OrderStatus.Preparing.getCode())
                            || orderInDatabase.getErpStatus().equals(OrderStatus.Pending.getCode())
                    ) {
                        ordersFromShippedToCompleted.add(retrievedOrder);
                    }
                } else if (retrievedOrder.getStatus().equals(OrderStatus.Shipped.getCode())) {
                    if (orderInDatabase.getErpStatus().equals(OrderStatus.Preparing.getCode())
                            || orderInDatabase.getErpStatus().equals(OrderStatus.Pending.getCode())
                            || orderInDatabase.getErpStatus().equals(OrderStatus.Obsolete.getCode())
                            || (retrievedOrder.getTrackingNumber() != null && orderInDatabase.getTrackingNumber() != null &&
                                    !retrievedOrder.getTrackingNumber().equalsIgnoreCase(orderInDatabase.getTrackingNumber()))
                    ) {
                        // If order wasn't invoiced pre-shipping, we can remove and re-insert contents
                        if (orderInDatabase.getShippingInvoiceNumber() == null) {
                            boolean hasInvoiceNumber = orderservice.getById(orderInDatabase.getId()).getShippingInvoiceNumber() != null;
                            if(hasInvoiceNumber)
                                continue;
                            oldOrders.add(retrievedOrder);
                        } else {
                            invoicedShippedOrders.add(retrievedOrder);
                        }
                    }
                } else {
                    // If order is shipped or already has shipping invoice number(pre-shipping), don't update anything,
                    // wait until it becomes Completed then only update status
                    if (!orderInDatabase.getErpStatus().equals(OrderStatus.Shipped.getCode())
                            && orderInDatabase.getShippingInvoiceNumber() == null) {
                        // for old orders get their id, update their attributes
                        oldOrders.add(retrievedOrder);
                    }
                    if (retrievedOrder.getStatus().equals(OrderStatus.Obsolete.getCode())) {
                        obsoleteOrders.add(retrievedOrder);
                    }
                }
            }
        }
        orders.clear();

        /* for new orders, insert them to DB and their children */
        List<OrderItem> allNewItems = prepareItems(newOrders);
        try {
            if (!newOrders.isEmpty()) {
                log.info("{} orders to be inserted/updated.", newOrders.size());
                platformOrderMabangMapper.insertOrdersFromMabang(newOrders);
            }
            if (!allNewItems.isEmpty()) {
                platformOrderMabangMapper.insertOrderItemsFromMabang(allNewItems);
                log.info("{} order items to be inserted/updated.", allNewItems.size());
            }
        } catch (RuntimeException e) {
            log.error(e.getLocalizedMessage());
        }

        // for old orders, update themselves and delete and reinsert their content.
        List<OrderItem> allNewItemsOfOldItems = prepareItems(oldOrders);
        try {
            if (!oldOrders.isEmpty()) {
                log.info("{} orders to be inserted/updated.", oldOrders.size());
                platformOrderMabangMapper.batchUpdateById(oldOrders);
                platformOrderMabangMapper.batchDeleteByMainID(oldOrders.stream().map(Order::getId).collect(toList()));
            }
            if (!ordersFromShippedToCompleted.isEmpty()) {
                log.info("{} orders to be updated from Shipped to Completed.", ordersFromShippedToCompleted.size());
                platformOrderMabangMapper.batchUpdateById(ordersFromShippedToCompleted);
                log.info("Contents of {} orders to be updated from Shipped to Completed.", ordersFromShippedToCompleted.size());
                platformOrderMabangMapper.batchUpdateErpStatusByMainId(
                        ordersFromShippedToCompleted.stream().map(Order::getId).collect(toList()),
                        OrderStatus.Completed.getCode());
            }
            if (!invoicedShippedOrders.isEmpty()) {
                log.info("{} orders to be updated from Pending/Preparing to Shipped.", invoicedShippedOrders.size());
                platformOrderMabangMapper.batchUpdateById(invoicedShippedOrders);
                log.info("Contents of {} orders to be updated from Pending/Preparing to Shipped.", invoicedShippedOrders.size());
                platformOrderMabangMapper.batchUpdateErpStatusByMainId(
                        invoicedShippedOrders.stream().map(Order::getId).collect(toList()),
                        OrderStatus.Shipped.getCode());
            }
            if (!obsoleteOrders.isEmpty()) {
                log.info("{} orders to become obsolete.", obsoleteOrders.size());
                platformOrderMabangMapper.batchUpdateById(obsoleteOrders);
                log.info("Contents of {} orders to be updated to Obsolete.", obsoleteOrders.size());
                platformOrderMabangMapper.batchUpdateErpStatusByMainId(
                        obsoleteOrders.stream().map(Order::getId).collect(toList()),
                        OrderStatus.Obsolete.getCode());
            }
            if (!allNewItemsOfOldItems.isEmpty()) {
                log.info("{} order items to be inserted/updated.", allNewItemsOfOldItems.size());
                platformOrderMabangMapper.insertOrderItemsFromMabang(allNewItemsOfOldItems);
            }
        } catch (RuntimeException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private List<OrderItem> prepareItems(ArrayList<Order> orders) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (Order order : orders) {
            order.resolveStatus();
            order.resolveProductAvailability();
            order.getOrderItems().forEach(
                    item -> {
                        item.setPlatformOrderId(order.getId());
                        item.resolveStatus(order.getStatus());
                        item.resolveProductAvailability();
                    }
            );
            orderItems.addAll(order.getOrderItems());
        }
        return orderItems;
    }

    @Override
    @Transactional
    public void updateMergedOrderFromMabang(Order order, Collection<String> sourceOrderErpId) {
        String targetID = platformOrderMabangMapper.findIdByErpCode(order.getPlatformOrderNumber());
        List<String> sourceIDs = sourceOrderErpId.stream()
                .map(platformOrderMabangMapper::findIdByErpId)
                .collect(toList());

        platformOrderMabangMapper.updateMergedOrder(targetID, sourceIDs);
        platformOrderMabangMapper.updateMergedOrderItems(targetID, sourceIDs);
    }

    @Override
    public Responses suspendOrder(PlatformOrderOperation orderOperation) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> orderIds = Arrays.stream(orderOperation.getOrderIds().split(",")).map(String::trim).collect(toList());
        // group id is the response from mabang API
        Responses responses = new Responses();
        ExecutorService throttlingExecutorService = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS,
                MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);

        List<CompletableFuture<Responses>> futures =  orderIds.stream()
            .map(id -> CompletableFuture.supplyAsync(() -> {
                OrderSuspendRequestBody body = new OrderSuspendRequestBody(id, sysUser.getRealname() + " : " + orderOperation.getReason());
                OrderSuspendRequest request = new OrderSuspendRequest(body);
                OrderSuspendResponse response = request.send();
                Responses r = new Responses();
                if(response.success())
                    r.addSuccess(id);
                else
                    r.addFailure(id);
                return r;
            }, throttlingExecutorService))
            .collect(toList());
        List<Responses> results = futures.stream()
            .map(CompletableFuture::join)
            .collect(toList());
        results.forEach(r -> {
            responses.getSuccesses().addAll(r.getSuccesses());
            responses.getFailures().addAll(r.getFailures());
        });
        log.info("{}/{} orders suspended successfully.", responses.getSuccesses().size(), orderIds.size());
        return responses;
    }

    @Override
    public Responses cancelOrders(PlatformOrderOperation orderOperation) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> orderIds = Arrays.stream(orderOperation.getOrderIds().split(",")).map(String::trim).collect(toList());

        Responses responses = new Responses();
        ExecutorService throttlingExecutorService = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS,
                MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);

        List<CompletableFuture<Responses>> futures =  orderIds.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> {
                    ChangeOrderRequestBody body = new ChangeOrderRequestBody(id, "5",null, null, sysUser.getRealname() + " : " + orderOperation.getReason());
                    ChangeOrderRequest request = new ChangeOrderRequest(body);
                    ChangeOrderResponse response = request.send();
                    Responses r = new Responses();
                    if(response.success())
                        r.addSuccess(id);
                    else
                        r.addFailure(id);
                    return r;
                }, throttlingExecutorService))
                .collect(toList());
        List<Responses> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
        results.forEach(r -> {
            responses.getSuccesses().addAll(r.getSuccesses());
            responses.getFailures().addAll(r.getFailures());
        });
        log.info("{}/{} orders cancelled successfully.", responses.getSuccesses().size(), orderIds.size());
        return responses;
    }

    /**
     *
     * @param requests List<OrderListRequestBody>
     * @param executor ExecutorService
     * @return orders retrieved from mabang
     */
    @Override
    public List<Order> getOrdersFromMabang(List<OrderListRequestBody> requests, ExecutorService executor) {
        List<Order> mabangOrders = new ArrayList<>();

        List<CompletableFuture<Boolean>> futures = requests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        OrderListRawStream rawStream = new OrderListRawStream(request);
                        OrderListStream stream = new OrderListStream(rawStream);
                        List<Order> orders = stream.all();
                        mabangOrders.addAll(orders);
                        success = !orders.isEmpty();
                    } catch (RuntimeException e) {
                        log.error("Error communicating with MabangAPI", e);
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} requests have succeeded.", nbSuccesses, requests.size());
        log.info("{}/{} mabang orders have been retrieved.", mabangOrders.size(), requests.size());
        return mabangOrders;

    }

    /**
     * Sends a request to clear orders Logistic Channel to Mabang
     * (usually it is called upon modifying an order)
     * @param orders List<Order>
     * @param executor Executor Service
     */
    @Override
    public void clearLogisticChannel(List<Order> orders, ExecutorService executor) {
        // First we delete the logistic channel names, otherwise we can't delete virtual skus
        List<CompletableFuture<Boolean>> clearLogisticFutures = orders.stream()
                .map(order -> CompletableFuture.supplyAsync(() -> {
                    ClearLogisticRequestBody body = new ClearLogisticRequestBody(order.getPlatformOrderId());
                    ClearLogisticRequest request = new ClearLogisticRequest(body);
                    ClearLogisticResponse response = request.send();
                    return response.success();
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> logisticResults = clearLogisticFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long logisticClearSuccessCount = logisticResults.stream().filter(b -> b).count();
        log.info("{}/{} logistic channel names cleared successfully.", logisticClearSuccessCount, orders.size());
    }
    @Override
    public String stripAccents(String input) {
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        input = input.replaceAll("[^\\p{ASCII}]", "");
        return input;
    }
    @Transactional
    @Override
    public JSONObject syncOrdersFromMabang(List<String> platformOrderIds) throws JSONException {
        log.info("Syncing following orders {}", platformOrderIds);
        List<List<String>> platformOrderIdLists = Lists.partition(platformOrderIds, 10);
        List<OrderListRequestBody> requests = new ArrayList<>();
        for (List<String> platformOrderIdList : platformOrderIdLists) {
            requests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList));
        }
        List<Order> mabangOrders = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<Boolean>> futures = requests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        OrderListRawStream rawStream = new OrderListRawStream(request);
                        OrderListStream stream = new OrderListStream(rawStream);
                        List<Order> orders = stream.all();
                        mabangOrders.addAll(orders);
                        success = !orders.isEmpty();
                    } catch (RuntimeException e) {
                        log.error("Error communicating with MabangAPI", e);
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} requests have succeeded.", nbSuccesses, requests.size());
        int syncedOrderNumber = mabangOrders.size();
        List<String> syncedOrderIds = mabangOrders.stream().map(Order::getPlatformOrderId).collect(Collectors.toList());
        log.info("{}/{} mabang orders have been retrieved.", syncedOrderNumber, platformOrderIds.size());

        log.info("{} orders to be updated.", syncedOrderNumber);
        saveOrderFromMabang(mabangOrders);

        JSONObject res = new JSONObject();
        res.put("synced_order_number", syncedOrderNumber);
        res.put("synced_order_ids", syncedOrderIds);
        return res;
    }

    @Override
    public ResponsesWithMsg<String> editOrdersRemark(String invoiceNumber) {
        ResponsesWithMsg<String> responses = new ResponsesWithMsg<>();
        List<String> platformOrderIds = platformOrderService.getPlatformOrderIdsByInvoiceNumber(invoiceNumber);
        log.info("Editing orders remark with invoice number {} for platform order ids {}", invoiceNumber, platformOrderIds);
        ExecutorService executor = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS,
                MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);
        List<OrderListRequestBody> OrderRequests = new ArrayList<>();
        List<List<String>> orderIdsPartition = Lists.partition(platformOrderIds, 10);
        for (List<String> platformOrderIdList : orderIdsPartition) {
            OrderRequests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList));
        }
        List<Order> ordersFromMabang = getOrdersFromMabang(OrderRequests, executor);
        List<EditRemarkRequestBody> editRemarkRequests = new ArrayList<>();
        for(Order order: ordersFromMabang) {
            editRemarkRequests.add(new EditRemarkRequestBody(order.getPlatformOrderId(), order.getRemark()+ " " + invoiceNumber));
        }
        List<CompletableFuture<Boolean>> editRemarkFutures = editRemarkRequests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        EditRemarkRequest editRemarkRequest = new EditRemarkRequest(request);
                        ChangeOrderResponse response = editRemarkRequest.send();
                        success = response.success();
                        if (success) {
                            responses.addSuccess(request.getPlatformOrderId());
                        } else {
                            responses.addFailure(request.getPlatformOrderId(), response.getMessage());
                        }
                    } catch (RuntimeException e) {
                        log.error("Error editing order remark {} : {}", request.getPlatformOrderId(), e.getMessage());
                        responses.addFailure(request.getPlatformOrderId(), e.getMessage());
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> results = editRemarkFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} order remarks updated successfully.", nbSuccesses, editRemarkRequests.size());
        return responses;
    }
    @Override
    public ResponsesWithMsg<String> deleteOrderRemark(String invoiceNumber) {
        ResponsesWithMsg<String> responses = new ResponsesWithMsg<>();
        List<String> orderIds = platformOrderService.getPlatformOrderIdsByInvoiceNumber(invoiceNumber);
        log.info("Removing orders remark with invoice number {} for platform order ids {}", invoiceNumber, orderIds);
        ExecutorService executor = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS,
                MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);
        List<OrderListRequestBody> OrderRequests = new ArrayList<>();
        List<List<String>> orderIdsPartition = Lists.partition(orderIds, 10);
        for (List<String> platformOrderIdList : orderIdsPartition) {
            OrderRequests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList));
        }
        List<Order> ordersFromMabang = getOrdersFromMabang(OrderRequests, executor);
        List<EditRemarkRequestBody> removeRemarkRequests = new ArrayList<>();
        for(Order order: ordersFromMabang) {
            Matcher invoiceRemarkMatcher = Pattern.compile("^(.*)("+ invoiceNumber +")(.*)$").matcher(order.getRemark());
            if(invoiceRemarkMatcher.matches() && !invoiceRemarkMatcher.group(2).isEmpty()) {
                log.info("Invoice number {} found in order remark {}", invoiceNumber, order.getPlatformOrderId());
                String orderRemark1 = invoiceRemarkMatcher.group(1);
                String orderRemark2 = invoiceRemarkMatcher.group(3);

                removeRemarkRequests.add(new EditRemarkRequestBody(order.getPlatformOrderId(), orderRemark1.trim() + " " +orderRemark2.trim()));
            } else {
                log.info("Invoice number {} not found in order remark {}", invoiceNumber, order.getPlatformOrderId());
            }
        }
        List<CompletableFuture<Boolean>> editRemarkFutures = removeRemarkRequests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        EditRemarkRequest removeRemarkRequest = new EditRemarkRequest(request);
                        ChangeOrderResponse response = removeRemarkRequest.send();
                        success = response.success();
                        if (success) {
                            responses.addSuccess(request.getPlatformOrderId());
                        } else {
                            responses.addFailure(request.getPlatformOrderId(), response.getMessage());
                        }
                    } catch (RuntimeException e) {
                        log.error("Error removing order remark {} : {}", request.getPlatformOrderId(), e.getMessage());
                        responses.addFailure(request.getPlatformOrderId(), e.getMessage());
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> results = editRemarkFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} order remarks removed successfully.", nbSuccesses, removeRemarkRequests.size());

        return responses;
    }
    @Override
    public Response<List<UpdateResult>, List<UpdateResult>> updateOrderStatusToPreparing(List<String> platformOrderIds) {
        Response<List<UpdateResult>, List<UpdateResult>> updateResponse = new Response<>();
        updateResponse.setData(new ArrayList<>());
        updateResponse.setError(new ArrayList<>());

        List<List<String>> orderPartitions = Lists.partition(platformOrderIds, 50);

        ExecutorService throttlingExecutorService = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS,
                MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);
        List<CompletableFuture<Boolean>> updateOrderFutures = orderPartitions.stream()
                .map(platformOrderIdList -> CompletableFuture.supplyAsync(() -> {
                    log.info("Updating order status on Mabang for PlatformOrderIds : {}", platformOrderIdList);
                    OrderUpdateOrderNewOrderRequestBody body = new OrderUpdateOrderNewOrderRequestBody(platformOrderIdList);
                    OrderUpdateOrderNewOrderRequest request = new OrderUpdateOrderNewOrderRequest(body);
                    OrderUpdateOrderNewOrderResponse response = request.send();

                    if(!response.success()) {
                        log.error("Failed to update order status on Mabang. PlatformOrderIds : {} - Reason : {}", platformOrderIdList, response.getMessage());
                        UpdateResult error = new UpdateResult();
                        error.setReason(response.getMessage());
                        updateResponse.setError(Collections.singletonList(error));
                        updateResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                        return false;
                    } else {
                        updateResponse.getData().addAll(response.getSuccessOrders());
                        updateResponse.getError().addAll(response.getOrdersNotFound());
                        updateResponse.setStatus(!response.getOrdersNotFound().isEmpty() ? HttpStatus.SC_NOT_FOUND : HttpStatus.SC_OK);
                        return true;
                    }
                }, throttlingExecutorService)).collect(Collectors.toList());

        List<Boolean> results = updateOrderFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} platform orders status updated successfully on Mabang. PlatformOrderIds : {}", nbSuccesses, platformOrderIds.size(), platformOrderIds);

        return updateResponse;
    }
}
