package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ArchiveOrderRequest;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ArchiveOrderRequestBody;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderResponse;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class ArchiveOrderJob implements Job {

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;

    private static final List<String> DEFAULT_EXCLUDED_SHOPS = Arrays.asList("JCH3", "JCH4", "JCH5", "FB2");
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;

    @Autowired
    private IPlatformOrderService platformOrderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime endDateTime = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
        LocalDateTime startDateTime = endDateTime.minusDays(DEFAULT_NUMBER_OF_DAYS);
        List<String> shops = DEFAULT_EXCLUDED_SHOPS;
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
                if (!jsonObject.isNull("excludedShops")) {
                    JSONArray shopsArray = jsonObject.getJSONArray("excludedShops");
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

        List<String> platformOrderIds = platformOrderService.fetchInvoicedShippedOrdersNotInShops(startDateTime, endDateTime, shops);

        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);

        log.info("Constructing order archiving requests");
        List<ArchiveOrderRequestBody> archiveOrderRequestBodies = new ArrayList<>();
        platformOrderIds.forEach(s -> archiveOrderRequestBodies.add(new ArchiveOrderRequestBody(s)));

        log.info("{} order archiving requests to be sent to MabangAPI", archiveOrderRequestBodies.size());

        List<CompletableFuture<Boolean>> changeOrderFutures = archiveOrderRequestBodies.stream()
                .map(archiveOrderRequestBody -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        ArchiveOrderRequest archiveOrderRequest = new ArchiveOrderRequest(archiveOrderRequestBody);
                        ChangeOrderResponse response = archiveOrderRequest.send();
                        success = response.success();
                    } catch (RuntimeException e) {
                        log.error("Error communicating with MabangAPI", e);
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> results = changeOrderFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} order archiving requests have succeeded.", nbSuccesses, archiveOrderRequestBodies.size());
    }
}
