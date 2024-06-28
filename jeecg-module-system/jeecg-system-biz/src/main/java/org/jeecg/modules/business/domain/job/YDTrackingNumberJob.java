package org.jeecg.modules.business.domain.job;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.yd.YDRequest;
import org.jeecg.modules.business.domain.api.yd.YDTrackingNumberData;
import org.jeecg.modules.business.domain.api.yd.YDTrackingNumberRequestBody;
import org.jeecg.modules.business.domain.api.yd.YDTrackingNumberResponse;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class YDTrackingNumberJob implements Job {

    @Autowired
    private IPlatformOrderService platformOrderService;

    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;

    private final static String APP_TOKEN = "y553qci626dds5d6lcughy3ogicvfaxmh";
    private final static String APP_KEY = "ynpoeds5511hg791mmksg6xccqxhax11j16eqz1itylq7whijki20egl0nmyql5h9";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<String> shops = new ArrayList<>();
        Map<String, List<String>> transportersByShop = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("transportersByShop")) {
                    JSONArray transportersByShopArray = jsonObject.getJSONArray("transportersByShop");
                    for (int i = 0; i < transportersByShopArray.length(); i++) {
                        JSONObject object = transportersByShopArray.getJSONObject((i));
                        if (!object.isNull("shop")) {
                            String shopCode = object.getString("shop");
                            shops.add(shopCode);
                            if (!object.isNull("transporters")) {
                                JSONArray transportersArray = object.getJSONArray("transporters");
                                List<String> transporters = new ArrayList<>();
                                for (int j = 0; j < transportersArray.length(); j++) {
                                    transporters.add(transportersArray.getString(j));
                                }
                                transportersByShop.put(shopCode, transporters);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        log.info("Starting to retrieve local tracking numbers");
        List<YDTrackingNumberData> localTrackingNumbers = new ArrayList<>();
        List<String> platformOrderIds = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : transportersByShop.entrySet()) {
            platformOrderIds.addAll(platformOrderService.fetchShippedOrdersFromShopAndTransporters(entry.getKey(), entry.getValue()));
        }

        List<YDRequest> ydRequests = new ArrayList<>();
        platformOrderIds.forEach(platformOrderId -> {
            YDTrackingNumberRequestBody ydParcelTraceRequestBody = new YDTrackingNumberRequestBody(platformOrderId);
            YDRequest ydRequest = new YDRequest(APP_TOKEN, APP_KEY, ydParcelTraceRequestBody);
            ydRequests.add(ydRequest);
        });


        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);
        List<CompletableFuture<Boolean>> futures = ydRequests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    HttpEntity entity = request.send().getEntity();
                    try {
                        // String of the response
                        String responseString = EntityUtils.toString(entity, "UTF-8");
                        YDTrackingNumberResponse ydResponse = mapper.readValue(responseString, YDTrackingNumberResponse.class);
                        if (ydResponse.getTrackingNumberData().getLocalTrackingNumber() != null &&
                                !ydResponse.getTrackingNumberData().getLocalTrackingNumber().isEmpty()) {
                            localTrackingNumbers.add(ydResponse.getTrackingNumberData());
                        }
                        success = true;
                    } catch (IOException e) {
                        log.error("Error while parsing response into String", e);
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} local tracking numbers have been retrieved.", nbSuccesses, ydRequests.size());

        log.info("Started updating {} local tracking numbers", localTrackingNumbers.size());
        platformOrderService.updateLocalTrackingNumber(localTrackingNumbers);
        log.info("Ended updating local tracking numbers for the following orders : {}", localTrackingNumbers.stream()
                .map(YDTrackingNumberData::getPlatformOrderId)
                .collect(Collectors.toList()));
    }
}
