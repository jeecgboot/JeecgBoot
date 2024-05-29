package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.*;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.Order;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class ClearLogisticChannelJob implements Job {
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        List<String> platformOrderIds = new ArrayList<>();
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("platformOrderIds")) {
                    JSONArray orderIds = jsonObject.getJSONArray("platformOrderIds");
                    if(orderIds == null) {
                        throw new RuntimeException("Empty parameter");
                    }
                    for(int i = 0; i < orderIds.length(); i++) {
                        platformOrderIds.add(orderIds.get(i).toString());
                    }
                }
                else {
                    throw new RuntimeException("platformOrderIds parameter is mandatory.");
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);

        List<CompletableFuture<Boolean>> clearLogisticFutures = platformOrderIds.stream()
                .map(orderId -> CompletableFuture.supplyAsync(() -> {
                    ClearLogisticRequestBody body = new ClearLogisticRequestBody(orderId);
                    ClearLogisticRequest request = new ClearLogisticRequest(body);
                    ClearLogisticResponse response = request.send();
                    return response.success();
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> clearResults = clearLogisticFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long clearSuccessCount = clearResults.stream().filter(b -> b).count();
        log.info("{}/{} logistic channel names cleared successfully.", clearSuccessCount, platformOrderIds.size());
    }
}
