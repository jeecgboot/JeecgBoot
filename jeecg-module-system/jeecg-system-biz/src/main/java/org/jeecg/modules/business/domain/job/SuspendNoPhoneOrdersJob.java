package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.Order;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.OrderListRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequest;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendResponse;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SuspendNoPhoneOrdersJob implements Job {

    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private IPlatformOrderMabangService platformOrderMabangService;

    private static final String DEFAULT_COUNTRY = "France";
    private static final String DEFAULT_LOGISTIC = "义速宝Colissimo专线普货（深圳）";
    private static final String DEFAULT_ABNORMAL_LABEL_NAME = "法国义达缺电话号码";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("[Test] Starting SuspendNoPhoneOrdersJob (Filtering and printing localOrders pending review)");
        // Default parameters
        String country = DEFAULT_COUNTRY;
        String logistic = DEFAULT_LOGISTIC;
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = endDateTime.minusDays(30);
        // Retrieve parameters from job data map
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = (String) jobDataMap.get("parameter");
        try {
            if (parameter != null && !parameter.isEmpty()) {
                JSONObject json = JSONObject.fromObject(parameter);
                if (json.has("country")) {
                    country = json.getString("country");
                }
                if (json.has("logistic")) {
                    logistic = json.getString("logistic");
                }
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                if (json.has("startDateTime")) {
                    startDateTime = LocalDateTime.parse(json.getString("startDateTime"), formatter);
                }
                if (json.has("endDateTime")) {
                    endDateTime = LocalDateTime.parse(json.getString("endDateTime"), formatter);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to parse parameters, fallback to default values. Reason: {}", e.getMessage());
        }
        if (!endDateTime.isAfter(startDateTime)) {
            throw new JobExecutionException("endDateTime must be after startDateTime");
        }
        log.info("Using parameters - Country: {}, Logistic: {}, Start Date: {}, End Date: {}",
                country, logistic, startDateTime, endDateTime);
        // Query localOrders based on the criteria
        List<PlatformOrder> localOrders = platformOrderService.lambdaQuery()
                .eq(PlatformOrder::getCountry, country)
                .eq(PlatformOrder::getLogisticChannelName, logistic)
                .eq(PlatformOrder::getCanSend, "1")
                .eq(PlatformOrder::getHasPhoneNumber, "0")
                .in(PlatformOrder::getErpStatus, Arrays.asList("1", "2"))
                .between(PlatformOrder::getUpdateTime, startDateTime, endDateTime)
                .list();
        log.info("Found {} localOrders matching criteria from {} to {}", localOrders.size(), startDateTime, endDateTime);
        List<String> platformOrderIds = localOrders.stream()
                .map(PlatformOrder::getPlatformOrderId)
                .collect(Collectors.toList());

        List<List<String>> partitionedIds = Lists.partition(platformOrderIds, 10);
        List<OrderListRequestBody> requests = partitionedIds.stream()
                .map(list -> new OrderListRequestBody().setPlatformOrderIds(list))
                .collect(Collectors.toList());
        log.info("Fetching orders from Mabang for {} platformOrderIds", platformOrderIds.size());
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Order> mabangOrders = platformOrderMabangService.getOrdersFromMabang(requests, executor);
        Map<String, Order> mabangOrderMap = mabangOrders.stream()
                .collect(Collectors.toMap(Order::getPlatformOrderId, Function.identity()));
        List<String> finalOrdersToSuspend = new ArrayList<>();
        for (PlatformOrder localOrder : localOrders) {
            Order mabang = mabangOrderMap.get(localOrder.getPlatformOrderId());
            if (mabang == null) continue;
            boolean stillCanSend = "1".equals(mabang.getCanSend());
            boolean stillValidStatus = Arrays.asList("1", "2").contains(mabang.getStatus());

            if (stillCanSend && stillValidStatus) {
                log.info("Order {} eligible for suspension: CanSend={}, ERP Status={}",
                        localOrder.getPlatformOrderId(), mabang.getCanSend(), mabang.getStatus());
                finalOrdersToSuspend.add(localOrder.getPlatformOrderId());
            } else {
                log.info("Order {} skipped due to updated Mabang status: CanSend={}, ERP Status={}",
                        localOrder.getPlatformOrderId(), mabang.getCanSend(), mabang.getStatus());
            }
        }
        log.info("Setting orders to abnormal...");
        List<CompletableFuture<Boolean>> futures = finalOrdersToSuspend.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> {
                    try {
                        OrderSuspendRequestBody body = new OrderSuspendRequestBody(id, DEFAULT_ABNORMAL_LABEL_NAME, "自动标记：无电话号码转为待审核");
                        OrderSuspendRequest request = new OrderSuspendRequest(body);
                        OrderSuspendResponse response = request.send();
                        boolean success = response.success();
                        if (success) {
                            log.info("Successfully suspended order {} from Mabang", id);
                        } else {
                            log.warn("Failed to suspend order {} from Mabang", id);
                        }
                        return success;
                    } catch (Exception e) {
                        log.error("Exception while suspending order {}: {}", id, e.getMessage());
                        return false;
                    }
                }, executor)).collect(Collectors.toList());
        futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        executor.shutdown();
        log.info("Successfully suspended {} orders", finalOrdersToSuspend.size());
        log.info("[Test] SuspendNoPhoneOrdersJob completed successfully.");
    }
}