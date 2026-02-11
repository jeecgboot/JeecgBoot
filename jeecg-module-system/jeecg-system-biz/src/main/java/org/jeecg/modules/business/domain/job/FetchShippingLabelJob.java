package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.getOrderLogisticsLabel.ShippingLabelRequest;
import org.jeecg.modules.business.domain.api.mabang.getOrderLogisticsLabel.ShippingLabelRequestBody;
import org.jeecg.modules.business.domain.api.mabang.getOrderLogisticsLabel.ShippingLabelResponse;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class FetchShippingLabelJob implements Job {

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;

    private static final List<String> DEFAULT_INCLUDED_SHOPS = Arrays.asList("VA DE", "VA IT", "VA UK", "VA3");
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private static final Integer MABANG_API_RATE_LIMIT_PER_MINUTE = 300;

    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private IPlatformOrderMabangService platformOrderMabangService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Started FetchShoppingLabelJob");
        LocalDateTime endDateTime = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
        LocalDateTime startDateTime = endDateTime.minusDays(DEFAULT_NUMBER_OF_DAYS);
        List<String> shops = DEFAULT_INCLUDED_SHOPS;
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

        if (!endDateTime.isAfter(startDateTime)) {
            throw new RuntimeException("EndDateTime must be strictly greater than StartDateTime !");
        }

        List<String> platformOrderIds = platformOrderService.fetchPlatformOrderIdsWithoutShippingLabelUrlForShops(startDateTime, endDateTime, shops);

        ExecutorService throttlingExecutorService = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS,
                MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);

        log.info("Constructing shipping label requests");
        List<ShippingLabelRequestBody> shippingLabelRequestBodies = new ArrayList<>();
        platformOrderIds.forEach(s -> shippingLabelRequestBodies.add(new ShippingLabelRequestBody(s)));

        log.info("{} shipping label requests to be sent to MabangAPI", shippingLabelRequestBodies.size());
        List<CompletableFuture<Boolean>> changeOrderFutures = shippingLabelRequestBodies.stream()
                .map(shippingLabelRequestBody -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        ShippingLabelRequest shippingLabelRequest = new ShippingLabelRequest(shippingLabelRequestBody);
                        ShippingLabelResponse response = shippingLabelRequest.send();
                        success = response.success();
                    } catch (RuntimeException e) {
                        log.error("Error communicating with MabangAPI", e);
                    }
                    return success;
                }, throttlingExecutorService))
                .collect(Collectors.toList());
        List<Boolean> results = changeOrderFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} shipping label requests have succeeded.", nbSuccesses, shippingLabelRequestBodies.size());
        log.info("Finished FetchShoppingLabelJob");
    }
}
