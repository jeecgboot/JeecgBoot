package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderResponse;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeWarehouseRequest;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeWarehouseRequestBody;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequest;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendResponse;
import org.jeecg.modules.business.entity.ThirdPartyStockAttribution;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.service.IThirdPartyStockAttributionService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class ThirdPartyStockAttributionJob implements Job {
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private IThirdPartyStockAttributionService attributionService;
    @Autowired
    private IPlatformOrderMabangService platformOrderMabangService;

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private final String DEFAULT_ABNORMAL_LABEL_NAME = "RP办公室仓";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Executing ThirdPartyStockAttributionJob...");
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(DEFAULT_NUMBER_OF_DAYS);
        List<String> shopCodes = new ArrayList<>();
        List<String> countries = new ArrayList<>();

        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("startDateTime")) {
                    String startDateStr = jsonObject.getString("startDateTime");
                    startDateTime = LocalDateTime.parse(startDateStr);
                }
                if (!jsonObject.isNull("shops")) {
                    JSONArray shopJsonArray = jsonObject.getJSONArray("shops");
                    for (int i = 0; i < shopJsonArray.length(); i++) {
                        shopCodes.add(shopJsonArray.getString(i));
                    }
                }
                if (!jsonObject.isNull("countries")) {
                    JSONArray countryJsonArray = jsonObject.getJSONArray("countries");
                    for (int i = 0; i < countryJsonArray.length(); i++) {
                        countries.add(countryJsonArray.getString(i));
                    }
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);

        List<ThirdPartyStockAttributionParam> params = platformOrderService.fetchThirdPartyStockAttributionParams(startDateTime, shopCodes, countries);
        Map<String, List<ThirdPartyStockAttributionParam>> map = params.stream().collect(Collectors.groupingBy(ThirdPartyStockAttributionParam::getPlatformOrderId));

        // fetch orders from mabang
        log.info("Fetching orders from mabang...");
        List<List<String>> platformOrderIdLists = Lists.partition(new ArrayList<>(map.keySet()), 10);
        List<Order> mabangOrders = new ArrayList<>();
        List<OrderListRequestBody> requests = new ArrayList<>();
        for (List<String> platformOrderIdList : platformOrderIdLists) {
            requests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList));
        }
        List<CompletableFuture<Boolean>> completableFutures = requests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        OrderListRawStream rawStream = new OrderListRawStream(request);
                        OrderListStream stream = new OrderListStream(rawStream);
                        List<Order> orders = stream.all();
                        mabangOrders.addAll(orders);
                        success = !orders.isEmpty();
                    } catch (RuntimeException e) {
                        log.error("Error while fetching orders from mabang: {}", e.getMessage());
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> fetchResults = completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long fetchSuccessCount = fetchResults.stream().filter(Boolean::booleanValue).count();
        log.info("Successfully fetched {} out of {} orders from mabang.", fetchSuccessCount, fetchResults.size());

        log.info("Clearing logistic channel names before inserting gifts");
        platformOrderMabangService.clearLogisticChannel(mabangOrders.stream().filter(Order::hasLogisticChannelAssigned)
                .collect(Collectors.toList()), executor);

        List<ThirdPartyStockAttribution> thirdPartyStockAttributions = new ArrayList<>();

        log.info("Updating warehouse name...");
        List<CompletableFuture<Boolean>> futures = mabangOrders.stream()
                .map(order -> CompletableFuture.supplyAsync(() -> {
                    String platformOrderId = order.getPlatformOrderId();
                    List<ThirdPartyStockAttributionParam> paramsByOrder = map.get(platformOrderId);
                    List<String> skusInOrder = order.getOrderItems().stream().map(OrderItem::getErpCode).collect(Collectors.toList());
                    Map<String, String> skuCodeWarehouseNameMap = new HashMap<>();
                    for (ThirdPartyStockAttributionParam param : paramsByOrder) {
                        if (skusInOrder.contains(param.getSku())) {
                            skuCodeWarehouseNameMap.put(param.getSku(), param.getWarehouseName());
                        }
                    }
                    ChangeWarehouseRequestBody body = new ChangeWarehouseRequestBody(order, skuCodeWarehouseNameMap);
                    ChangeWarehouseRequest request = new ChangeWarehouseRequest(body);
                    ChangeOrderResponse response = request.send();
                    boolean success = response.success();
                    if (success) {
                        for (ThirdPartyStockAttributionParam param : paramsByOrder) {
                            ThirdPartyStockAttribution attribution = new ThirdPartyStockAttribution();
                            attribution.setSkuId(param.getSku());
                            attribution.setPlatformOrderId(platformOrderId);
                            attribution.setCreateBy("third party stock attribution job");
                            thirdPartyStockAttributions.add(attribution);
                        }
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long successCount = results.stream().filter(Boolean::booleanValue).count();
        log.info("Successfully updated warehouse for {} out of {} orders.", successCount, results.size());

        log.info("Setting orders to abnormal...");
        List<CompletableFuture<Boolean>> abnormalFutures =  mabangOrders.stream()
                .map(order -> CompletableFuture.supplyAsync(() -> {
                    OrderSuspendRequestBody body = new OrderSuspendRequestBody(order.getPlatformOrderId(), DEFAULT_ABNORMAL_LABEL_NAME, null);
                    OrderSuspendRequest request = new OrderSuspendRequest(body);
                    OrderSuspendResponse response = request.send();
                    return response.success();
                }, executor))
                .collect(toList());
        List<Boolean> abnormalResults = abnormalFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        log.info("Successfully set {}/{} orders to abnormal.", abnormalResults.size(), mabangOrders.size());


        executor.shutdown();

        log.info("Started saving {} third party stock attributions in database", thirdPartyStockAttributions.size());
        attributionService.saveBatch(thirdPartyStockAttributions);
        log.info("Finished saving third party stock attributions in database");
    }
}
