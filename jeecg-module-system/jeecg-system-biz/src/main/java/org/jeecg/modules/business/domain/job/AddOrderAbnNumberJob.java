package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.common.api.dto.message.TemplateMessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.*;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderShopSync;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jetbrains.annotations.NotNull;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class AddOrderAbnNumberJob implements Job {

    private static final List<String> DEFAULT_INCLUDED_SHOPS = Arrays.asList("JCH8 KR");
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private static final Integer MABANG_API_RATE_LIMIT_PER_MINUTE = 300;

    @Autowired
    private IPlatformOrderService platformOrderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<String> shops = DEFAULT_INCLUDED_SHOPS;
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("includedShops")) {
                    JSONArray shopsArray = jsonObject.getJSONArray("includedShops");
                    List<String> shopList = new ArrayList<>();
                    for (int i = 0; i < shopsArray.length(); i++) {
                        shopList.add(shopsArray.getString(i));
                    }
                    shops = shopList;
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        List<PlatformOrder> platformOrders = platformOrderService.fetchOrderInShopsReadyForAbnNumberJob(shops);
        Map<String, String> shopifyNoteMap = platformOrders.stream()
                .collect(Collectors.toMap(PlatformOrder::getPlatformOrderId, PlatformOrder::getShopifyNote));
        if (platformOrders.isEmpty()) {
            log.info("No order with abnNumbers ready to be added via MabangAPI, quitting now.");
            return;
        }
        log.info("{} orders with abnNumbers ready to be added via MabangAPI", platformOrders.size());
        List<String> platformOrderIds = new ArrayList<>(shopifyNoteMap.keySet());
        List<List<String>> platformOrderIdLists = Lists.partition(platformOrderIds, 10);
        List<OrderListRequestBody> requests = new ArrayList<>();
        for (List<String> platformOrderIdList : platformOrderIdLists) {
            requests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList));
        }

        log.info("Started retrieving those orders from Mabang for comparison to avoid re-writing");
        List<Order> mabangOrders = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);
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
        log.info("{}/{} mabang orders have been retrieved.", syncedOrderNumber, platformOrderIds.size());

        log.info("Started comparison of {} orders to construct order modification request", mabangOrders.size());
        List<AddOrderAbnNumberRequestBody> addOrderAbnNumberRequestBodies = new ArrayList<>();
        mabangOrders.forEach(order -> {
            String taxNumber = order.getTaxNumber();
            String platformOrderId = order.getPlatformOrderId();
            String abnNumber = shopifyNoteMap.get(platformOrderId);
            if (taxNumber == null || taxNumber.isEmpty()) {
                addOrderAbnNumberRequestBodies.add(new AddOrderAbnNumberRequestBody(platformOrderId, abnNumber));
            } else {
                if (order.getTaxNumber().equalsIgnoreCase(abnNumber)) {
                    log.info("AbnNumber {} already present for order {}, ignoring", order.getTaxNumber(), platformOrderId);
                } else {
                    // If the number EVER changes (EXTREMELY UNLIKELY), send it to Mabang anyway
                    addOrderAbnNumberRequestBodies.add(new AddOrderAbnNumberRequestBody(platformOrderId, abnNumber));
                }
            }
        });

        ExecutorService throttlingExecutorService = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS,
                MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);
        List<CompletableFuture<Boolean>> changeOrderFutures = addOrderAbnNumberRequestBodies.stream()
                .map(addOrderAbnNumberRequestBody -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        AddOrderAbnNumberRequest addOrderAbnNumberRequest = new AddOrderAbnNumberRequest(addOrderAbnNumberRequestBody);
                        ChangeOrderResponse response = addOrderAbnNumberRequest.send();
                        success = response.success();
                    } catch (RuntimeException e) {
                        log.error("Error communicating with MabangAPI", e);
                    }
                    return success;
                }, throttlingExecutorService))
                .collect(Collectors.toList());
        results = changeOrderFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} order abnNumber adding requests have succeeded.", nbSuccesses, addOrderAbnNumberRequestBodies.size());
    }
}
