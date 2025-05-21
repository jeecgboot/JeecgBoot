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
import static java.util.stream.Collectors.toList;

@Slf4j
public class ShopifyPromoCodeJob implements Job {

    private static final List<String> DEFAULT_INCLUDED_SHOPS = Arrays.asList("AC");

    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;

    private final String DEFAULT_ABNORMAL_LABEL_NAME = "AC自行处理";

    private final String DEFAULT_PROMO_CODE = "BARBE20";

    @Autowired
    private IPlatformOrderService platformOrderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<String> shops = DEFAULT_INCLUDED_SHOPS;
        String promoCode = DEFAULT_PROMO_CODE;
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
                if (!jsonObject.isNull("promoCode")) {
                    promoCode = jsonObject.getString("promoCode");
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        List<PlatformOrderShopSync> ordersWithoutShopifyNote = platformOrderService.fetchOrderInShopsWithoutShopifyNote(shops);
        log.info("Fetched {} orders without Shopify promo code", ordersWithoutShopifyNote.size());
        Map<String, List<PlatformOrderShopSync>> ordersByShop = ordersWithoutShopifyNote.stream().collect(
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

        log.info("Started adding Shopify promo code to orders without one");
        Map<String, String> orderNoteMap = orders.stream().filter(Order::hasDiscountCodes).collect(toMap(order -> order.getId().toString(), Order::getDiscountCode));
        if (orderNoteMap.isEmpty()) {
            log.info("No promo code can be added to orders, quitting now");
        } else {
            List<String> ordersToAddPromoCode = new ArrayList<>(orderNoteMap.keySet());
            List<PlatformOrder> platformOrders = platformOrderService.selectByPlatformOrderIds(ordersToAddPromoCode);
            platformOrders.forEach(platformOrder -> platformOrder.setShopifyNote(orderNoteMap.get(platformOrder.getPlatformOrderId()).trim()));
            platformOrderService.updateBatchById(platformOrders);
            log.info("Finished adding Shopify promo code to {} orders without one into DB.", platformOrders.size());
        }

        log.info("Setting orders to abnormal...");
        List<String> orderIdsByShopifyNote = platformOrderService.fetchPlatformOrderIdsByShopifyNote(promoCode);
        String finalPromoCode = promoCode;
        List<CompletableFuture<Boolean>> abnormalFutures =  orderIdsByShopifyNote.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> {
                    OrderSuspendRequestBody body = new OrderSuspendRequestBody(id, DEFAULT_ABNORMAL_LABEL_NAME, finalPromoCode);
                    OrderSuspendRequest request = new OrderSuspendRequest(body);
                    OrderSuspendResponse response = request.send();
                    return response.success();
                }, executor))
                .collect(toList());
        List<Boolean> abnormalResults = abnormalFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        log.info("Successfully set {}/{} orders to abnormal.", abnormalResults.size(), orderIdsByShopifyNote.size());
        executor.shutdown();
    }
}
