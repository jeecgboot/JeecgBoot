package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.*;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequest;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendResponse;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.vo.Responses;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class ChangeWarehouseJob implements Job {
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private IPlatformOrderMabangService platformOrderMabangService;

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private final String DEFAULT_WAREHOUSE_NAME = "法国巴黎仓库-唯客路";
    private final String DEFAULT_ABNORMAL_LABEL_NAME = "AC海外仓非法国比利时不发货";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Executing ChangeWarehouseJob...");
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = endDateTime.minusDays(DEFAULT_NUMBER_OF_DAYS);
        List<String> allSkus = new ArrayList<>();
        List<ChangeWareHouseParam> changeWareHouseParams = new ArrayList<>();

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
                if (!jsonObject.isNull("data")) {
                    JSONArray dataJsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataJsonArray.length(); i++) {
                        JSONObject object = dataJsonArray.getJSONObject(i);
                        ChangeWareHouseParam param = new ChangeWareHouseParam();
                        if(!object.isNull("shop")) {
                            param.setShop(object.getString("shop"));
                        }
                        if(!object.isNull("skus")) {
                            JSONArray skusJsonArray = object.getJSONArray("skus");
                            List<String> skus = new ArrayList<>();
                            for(int j = 0; j < skusJsonArray.length(); j++) {
                                skus.add(skusJsonArray.getString(j));
                            }
                            param.setSkus(skus);
                            allSkus.addAll(skus);
                        }
                        if(!object.isNull("countries")) {
                            List<String> countries = new ArrayList<>();
                            JSONArray countriesJsonArray = object.getJSONArray("countries");
                            for (int j = 0; j < countriesJsonArray.length(); j++) {
                                countries.add(countriesJsonArray.getString(j));
                            }
                            param.setCountries(countries);
                        }
                        changeWareHouseParams.add(param);
                    }
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        if (!endDateTime.isAfter(startDateTime)) {
            throw new RuntimeException("EndDateTime must be strictly greater than StartDateTime !");
        }
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);

        List<String> platformOrderIds = new ArrayList<>();
        List<String> ordersToSetAbnormal = new ArrayList<>();
        for(ChangeWareHouseParam param: changeWareHouseParams) {
            List<String> poIds = platformOrderService.fetchUninvoicedOrdersWithSkusInCountry(startDateTime, endDateTime, param.getShop(), param.getSkus(), param.getCountries());
            List<String> abnormalPoIds = platformOrderService.fetchUninvoicedOrdersWithSkusNotInCountry(startDateTime, endDateTime, param.getShop(), allSkus, param.getCountries());
            platformOrderIds.addAll(poIds);
            ordersToSetAbnormal.addAll(abnormalPoIds);
        }

        // fetch orders from mabang
        log.info("Fetching orders from mabang...");
        List<List<String>> platformOrderIdLists = Lists.partition(platformOrderIds, 10);
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

        log.info("Updating recipients info...");
        replaceOrdersAccents(mabangOrders);

        log.info("Updating warehouse name...");
        List<CompletableFuture<Boolean>> futures = mabangOrders.stream()
                .map(order -> CompletableFuture.supplyAsync(() -> {
                    ChangeWarehouseRequestBody body = new ChangeWarehouseRequestBody(order, DEFAULT_WAREHOUSE_NAME);
                    ChangeWarehouseRequest request = new ChangeWarehouseRequest(body);
                    ChangeOrderResponse response = request.send();
                    return response.success();
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long successCount = results.stream().filter(Boolean::booleanValue).count();
        log.info("Successfully changed warehouse for {} out of {} orders.", successCount, results.size());

        log.info("Setting orders to abnormal...");
        Responses responses = new Responses();
        List<CompletableFuture<Responses>> abnormalFutures =  ordersToSetAbnormal.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> {
                    OrderSuspendRequestBody body = new OrderSuspendRequestBody(id, "");
                    body.setAbnormal_label_name(DEFAULT_ABNORMAL_LABEL_NAME);
                    OrderSuspendRequest request = new OrderSuspendRequest(body);
                    OrderSuspendResponse response = request.send();
                    Responses r = new Responses();
                    if(response.success())
                        r.addSuccess(id);
                    else
                        r.addFailure(id);
                    return r;
                }, executor))
                .collect(toList());
        List<Responses> abnormalResults = abnormalFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
        abnormalResults.forEach(r -> {
            responses.getSuccesses().addAll(r.getSuccesses());
            responses.getFailures().addAll(r.getFailures());
        });
        log.info("Successfully set {}/{} orders to abnormal.", responses.getSuccesses().size(), ordersToSetAbnormal.size());
        executor.shutdown();
    }
    public void replaceOrdersAccents(List<Order> orders) {
        for(Order order: orders) {
                order.setRecipient(platformOrderMabangService.stripAccents(order.getRecipient()));
                order.setAddress(platformOrderMabangService.stripAccents(order.getAddress()));
                order.setAddress2(platformOrderMabangService.stripAccents(order.getAddress2()));
        }
    }
}
