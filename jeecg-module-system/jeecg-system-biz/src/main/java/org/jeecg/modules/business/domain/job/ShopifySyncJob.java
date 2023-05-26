package org.jeecg.modules.business.domain.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.shopify.*;
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
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Slf4j
public class ShopifySyncJob implements Job {

    private static final List<String> DEFAULT_INCLUDED_SHOPS = Arrays.asList("JCH3", "JCH4", "JCH5");
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;

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

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<PlatformOrderShopSync> ordersReadyForShopifySync = platformOrderService.fetchOrderInShopsReadyForShopifySync(shops);
        Map<String, PlatformOrderShopSync> syncMap = ordersReadyForShopifySync.stream()
                .collect(toMap(PlatformOrderShopSync::getPlatformOrderId, Function.identity()));

        List<FulfillmentOrder> fulfillmentOrders = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);
        log.info("Constructing fulfillment retrieval requests");
        List<GetFulfillmentRequestBody> getFulfillmentRequestBodyList = new ArrayList<>();
        ordersReadyForShopifySync.forEach(o -> getFulfillmentRequestBodyList.add(new GetFulfillmentRequestBody(o)));

        log.info("{} fulfillment retrieval requests to be sent to ShopifyAPI", getFulfillmentRequestBodyList.size());

        List<CompletableFuture<Boolean>> futures = getFulfillmentRequestBodyList.stream()
                .map(body -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        GetFulfillmentRequest getFulfillmentRequest = new GetFulfillmentRequest(body);
                        String responseStr = getFulfillmentRequest.rawSend().getBody();
                        FulfillmentOrdersResponse response = mapper.readValue(responseStr, FulfillmentOrdersResponse.class);
                        fulfillmentOrders.add(response.getFulfillmentOrders().get(0));
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
        log.info("{}/{} fulfillment retrieval requests have succeeded.", nbSuccesses, getFulfillmentRequestBodyList.size());

        log.info("Constructing fulfillment creation requests");
        List<CreateFulfillmentRequestBody> createFulfillmentRequests = new ArrayList<>();
        for (FulfillmentOrder fulfillmentOrder : fulfillmentOrders) {
            if (fulfillmentOrder.isOpen()) {
                PlatformOrderShopSync platformOrderShopSync = syncMap.get(fulfillmentOrder.getOrderId());
                createFulfillmentRequests.add(new CreateFulfillmentRequestBody(fulfillmentOrder.getId(), platformOrderShopSync));
            } else {
                log.info("Order {} is already fulfilled, skipping.", fulfillmentOrder.getOrderId());
            }
        }
        log.info("{} fulfillment creation requests to be sent to ShopifyAPI", createFulfillmentRequests.size());

        List<CompletableFuture<Boolean>> fulfillmentCreationFutures = createFulfillmentRequests.stream()
                .map(changeOrderRequestBody -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        CreateFulfillmentRequest createFulfillmentRequest = new CreateFulfillmentRequest(changeOrderRequestBody);
                        String responseStr = createFulfillmentRequest.rawSend().getBody();
                        FulfillmentCreationResponse response = mapper.readValue(responseStr, FulfillmentCreationResponse.class);
                        success = response.getFulfillment().isSuccess();
                    } catch (RuntimeException e) {
                        log.error("Error communicating with ShopifyAPI", e);
                    } catch (JsonProcessingException e) {
                        log.error("Error processing json", e);
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        results = fulfillmentCreationFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} fulfillment creation requests have succeeded.", nbSuccesses, createFulfillmentRequests.size());
    }
}
