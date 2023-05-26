package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.common.api.dto.message.TemplateMessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.jetbrains.annotations.NotNull;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class MabangOrderSyncJob implements Job {

    @Autowired
    private IPlatformOrderMabangService platformOrderMabangService;
    @Autowired
    private ISysBaseAPI ISysBaseApi;
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        List<String> platformOrderIds = new ArrayList<>();
        String username = null;
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                JSONArray orderIds = jsonObject.getJSONArray("orderIds");
                if (null != orderIds) {
                    for (int i = 0; i < orderIds.length(); i++) {
                        platformOrderIds.add(orderIds.get(i).toString());
                    }
                }
                username = jsonObject.getString("username");
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }
        if (platformOrderIds.isEmpty()) {
            throw new RuntimeException("PlatformOrder ID list can't be empty !");
        }

        log.info("Syncing following orders {}", platformOrderIds);
        List<List<String>> platformOrderIdLists = Lists.partition(platformOrderIds, 10);
        List<OrderListRequestBody> requests = new ArrayList<>();
        for (List<String> platformOrderIdList : platformOrderIdLists) {
            // There's no other way to sync orders of all statuses, so we duplicate requests to make sure
            // that we get all orders
            requests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList).setStatus(OrderStatus.AllUnshipped));
            requests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList).setStatus(OrderStatus.AllNonUnshipped));
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
        int syncedOrderNumber = mabangOrders.size();
        List<String> syncedOrderIds = mabangOrders.stream().map(Order::getPlatformOrderId).collect(Collectors.toList());
        log.info("{}/{} mabang orders have been retrieved.", syncedOrderNumber, platformOrderIds.size());

        log.info("{} orders to be updated.", syncedOrderNumber);
        platformOrderMabangService.saveOrderFromMabang(mabangOrders);

        Map<String, String> param = new HashMap<>();
        param.put("requested_order_number", String.valueOf(platformOrderIds.size()));
        param.put("synced_order_number", String.valueOf(syncedOrderNumber));
        param.put("requested_order_ids", getHtmlListFromStringList(platformOrderIds));
        List<String> failedToSyncOrderIds = new ArrayList<>();
        for (String platformOrderId : platformOrderIds) {
            if (!syncedOrderIds.contains(platformOrderId)) {
                failedToSyncOrderIds.add(platformOrderId);
            }
        }
        param.put("failed_to_sync_order_ids", getHtmlListFromStringList(failedToSyncOrderIds));
        TemplateMessageDTO message = new TemplateMessageDTO("admin", username == null ? "admin" : username, "马帮订单同步任务", param, "mabang_order_sync_job_result");
        ISysBaseApi.sendTemplateAnnouncement(message);
        log.info("Order sync job recap message sent");
    }

    @NotNull
    private static String getHtmlListFromStringList(List<String> strings) {
        String orderIdsInList = "<ul>";
        for (String platformOrderId : strings) {
            orderIdsInList = orderIdsInList.concat("<li>")
                    .concat(platformOrderId)
                    .concat("</li>");
        }
        orderIdsInList = orderIdsInList.concat("</ul>");
        return orderIdsInList;
    }
}