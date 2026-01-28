package org.jeecg.modules.business.domain.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequest;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendResponse;
import org.jeecg.modules.business.domain.api.shopify.GetOrderListRequest;
import org.jeecg.modules.business.domain.api.shopify.GetOrderListRequestBody;
import org.jeecg.modules.business.domain.api.shopify.GetOrderListResponse;
import org.jeecg.modules.business.domain.api.shopify.Order;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderShopSync;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Slf4j
public class ShopifyTagJob implements Job {

    private static final List<String> DEFAULT_INCLUDED_SHOPS = Arrays.asList("AC");

    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;

    private final String DEFAULT_ABNORMAL_LABEL_NAME = "AC复购订单";

    private static final List<String> DEFAULT_TAGS = Arrays.asList("Subscription");

    @Autowired
    private IPlatformOrderService platformOrderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<String> shops = DEFAULT_INCLUDED_SHOPS;
        List<String> tags = DEFAULT_TAGS;
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
                if (!jsonObject.isNull("tags")) {
                    JSONArray tagsArray = jsonObject.getJSONArray("tags");
                    List<String> tagList = new ArrayList<>();
                    for (int i = 0; i < tagsArray.length(); i++) {
                        tagList.add(tagsArray.getString(i));
                    }
                    tags = tagList;
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        List<PlatformOrderShopSync> ordersWithoutShopifyTags = platformOrderService.fetchOrderInShopsWithoutShopifyTags(shops);
        log.info("Fetched {} orders without Shopify tags", ordersWithoutShopifyTags.size());
        Map<String, List<PlatformOrderShopSync>> ordersByShop = ordersWithoutShopifyTags.stream().collect(
                groupingBy(PlatformOrderShopSync::getShopifyPrefix));

        List<Order> orders = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);
        log.info("Constructing order retrieval requests");
        List<GetOrderListRequestBody> getOrderListRequestBodyList = new ArrayList<>();
        ordersByShop.values().forEach(platformOrderShopSyncs -> {
            if (!platformOrderShopSyncs.isEmpty()) {
                List<String> orderIds = platformOrderShopSyncs.stream().map(PlatformOrderShopSync::getPlatformOrderId).collect(Collectors.toList());
                String shopifyPrefix = platformOrderShopSyncs.get(0).getShopifyPrefix();
                String shopifyToken = platformOrderShopSyncs.get(0).getShopifyToken();
                getOrderListRequestBodyList.add(new GetOrderListRequestBody(shopifyPrefix, shopifyToken, orderIds));
            }
        });

        List<CompletableFuture<Boolean>> futures = getOrderListRequestBodyList.stream()
                .map(body -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        GetOrderListRequest getOrderListRequest = new GetOrderListRequest(body);
                        String responseStr = getOrderListRequest.rawSend().getBody();
                        GetOrderListResponse response = mapper.readValue(responseStr, GetOrderListResponse.class);
                        orders.addAll(response.getOrders());
                        success = true;
                    } catch (RuntimeException e) {
                        log.error("Error communicating with ShopifyAPI", e);
                    } catch (JsonProcessingException e) {
                        log.error("Error processing json", e);
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} order retrieval requests have succeeded.", nbSuccesses, getOrderListRequestBodyList.size());
        log.info("{} orders have been retrieved.", orders.size());

        log.info("Started adding Shopify tags to orders without one");
        Map<String, String> orderTagMap = orders.stream().filter(Order::hasTags).collect(toMap(order -> order.getId().toString(), Order::getTags));
        if (orderTagMap.isEmpty()) {
            log.info("No tags can be added to orders, quitting now");
        } else {
            List<String> ordersToAddTags = new ArrayList<>(orderTagMap.keySet());
            List<PlatformOrder> platformOrders = platformOrderService.selectByPlatformOrderIds(ordersToAddTags);
            platformOrders.forEach(platformOrder -> platformOrder.setShopifyTags(orderTagMap.get(platformOrder.getPlatformOrderId()).trim()));
            platformOrderService.updateBatchById(platformOrders);
            log.info("Finished adding Shopify tags to {} orders without one into DB.", platformOrders.size());
        }

        log.info("Setting orders to abnormal...");
        List<PlatformOrder> ordersByShopifyTags = platformOrderService.fetchPlatformOrdersWithShopifyTags();
        List<String> finalTags = tags;
        List<PlatformOrder> qualifiedOrders = ordersByShopifyTags.stream()
                .filter(platformOrder -> {
                    String[] split = platformOrder.getShopifyTags().split(",");
                    for (String s : split) {
                        if (finalTags.contains(s)) {
                            return true;
                        }
                    }
                    return false;
                }).collect(toList());
        if (qualifiedOrders.isEmpty()) {
            log.info("No orders to be set to abnormal, quitting now");
            executor.shutdownNow();
        }
        List<CompletableFuture<Boolean>> abnormalFutures = qualifiedOrders
                .stream()
                .map(platformOrder -> CompletableFuture.supplyAsync(() -> {
                    OrderSuspendRequestBody body = new OrderSuspendRequestBody(platformOrder.getPlatformOrderId(),
                            DEFAULT_ABNORMAL_LABEL_NAME, platformOrder.getShopifyTags());
                    OrderSuspendRequest request = new OrderSuspendRequest(body);
                    OrderSuspendResponse response = request.send();
                    boolean success = response.success();
                    if (success) {
                        platformOrder.setAlreadySetAbnormal("1");
                    }
                    return success;
                }, executor))
                .collect(toList());
        List<Boolean> abnormalResults = abnormalFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        log.info("Successfully set {}/{} orders to abnormal.", abnormalResults.size(), qualifiedOrders.size());
        platformOrderService.updateBatchById(qualifiedOrders);
        executor.shutdown();
    }
}
