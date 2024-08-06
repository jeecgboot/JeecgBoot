package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderRequest;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderRequestBody;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderResponse;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class AddCardJob implements Job {

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 30;

    private static final List<String> DEFAULT_SHOPS = Arrays.asList("JCH8 KR");
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private static final String CARD_SKU = "PJ332000000-JCH";
    private static final String OBSOLETE_STATUS_CODE = "4";

    @Autowired
    private IPlatformOrderService platformOrderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = endDateTime.minusDays(DEFAULT_NUMBER_OF_DAYS);
        List<String> shops = DEFAULT_SHOPS;
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("startDateTime")) {
                    String startDateStr = jsonObject.getString("startDateTime");
                    startDateTime = LocalDateTime.parse(startDateStr);
                }
                if (!jsonObject.isNull("endDateTime")) {
                    String endDateStr = jsonObject.getString("endDateTime");
                    endDateTime = LocalDateTime.parse(endDateStr);
                }
                if (!jsonObject.isNull("shops")) {
                    JSONArray shopsArray = jsonObject.getJSONArray("shops");
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

        if (!endDateTime.isAfter(startDateTime)) {
            throw new RuntimeException("EndDateTime must be strictly greater than StartDateTime !");
        }

        List<PlatformOrder> platformOrders = platformOrderService.fetchUninvoicedOrdersForShops(startDateTime, endDateTime, shops);
        List<String> platformOrderIds = platformOrders.stream().map(PlatformOrder::getPlatformOrderId).collect(Collectors.toList());
        List<List<String>> platformOrderIdLists = Lists.partition(platformOrderIds, 10);

        List<OrderListRequestBody> requests = new ArrayList<>();
        for (List<String> platformOrderIdList : platformOrderIdLists) {
            requests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList));
        }
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
        log.info("{}/{} mabang orders have been retrieved.", mabangOrders.size(), platformOrderIds.size());

        log.info("Constructing order modification requests");
        List<ChangeOrderRequestBody> changeOrderRequests = new ArrayList<>();
        for (Order mabangOrder : mabangOrders) {
            boolean hasCard = false;
            for (OrderItem orderItem : mabangOrder.getOrderItems()) {
                // Order already contains card, skipping
                if (orderItem.getErpCode().equalsIgnoreCase(CARD_SKU)
                        && !orderItem.getStatus().equalsIgnoreCase(OBSOLETE_STATUS_CODE)) {
                    hasCard = true;
                    break;
                }
            }
            if (!hasCard) {
                // Still no card in order, add one
                HashSet<Pair<String, Integer>> card = new HashSet<>();
                card.add(Pair.of(CARD_SKU, 1));
                ChangeOrderRequestBody changeOrderRequestBody = new ChangeOrderRequestBody(mabangOrder.getPlatformOrderId(), null,
                        null, card, null);
                changeOrderRequests.add(changeOrderRequestBody);
            }
        }
        log.info("{} order modification requests to be sent to MabangAPI", changeOrderRequests.size());

        List<CompletableFuture<Boolean>> changeOrderFutures = changeOrderRequests.stream()
                .map(changeOrderRequestBody -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        ChangeOrderRequest changeOrderRequest = new ChangeOrderRequest(changeOrderRequestBody);
                        ChangeOrderResponse response = changeOrderRequest.send();
                        success = response.success();
                    } catch (RuntimeException e) {
                        log.error("Error communicating with MabangAPI", e);
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        results = changeOrderFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} order modification requests have succeeded.", nbSuccesses, changeOrderRequests.size());
    }

}
