package org.jeecg.modules.business.domain.job;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.equick.EQuickRequest;
import org.jeecg.modules.business.domain.api.equick.EQuickResponse;
import org.jeecg.modules.business.service.IParcelService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class EQuickJob implements Job {

    @Autowired
    private IParcelService parcelService;
    @Autowired
    private IPlatformOrderService platformOrderService;

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 15;
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private static final List<String> DEFAULT_TRANSPORTERS = Arrays.asList("EQ快速专线小包（D）", "EQ快速专线小包（带电）", "EQ美国快速专线小包",
            "Equick快速专线小包（AT/CZ/SK/HU/BG/GR/RO/SI）", "Equick欧美速递专线", "Equick法国快速专线小包");

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DEFAULT_NUMBER_OF_DAYS);
        List<String> transporters = DEFAULT_TRANSPORTERS;
        boolean overrideRestriction = false;
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("startDate")) {
                    String startDateStr = jsonObject.getString("startDate");
                    startDate = LocalDate.parse(startDateStr);
                }
                if (!jsonObject.isNull("endDate")) {
                    String endDateStr = jsonObject.getString("endDate");
                    endDate = LocalDate.parse(endDateStr);
                }
                if (!jsonObject.isNull("transporters")) {
                    JSONArray transporterArray = jsonObject.getJSONArray("transporters");
                    List<String> transporterList = new ArrayList<>();
                    for (int i = 0; i < transporterArray.length(); i++) {
                        transporterList.add(transporterArray.getString(i));
                    }
                    transporters = transporterList;
                }
                if (!jsonObject.isNull("override")) {
                    overrideRestriction = jsonObject.getBoolean("override");
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        if (!endDate.isAfter(startDate)) {
            throw new RuntimeException("EndDate must be strictly greater than StartDate !");
        } else if (endDate.minusDays(30).isAfter(startDate) && !overrideRestriction) {
            throw new RuntimeException("No more than 30 days can separate startDate and endDate !");
        }

        log.info("Starting to retrieve parcel traces of {} from {} to {}", transporters, startDate, endDate);
        List<String> equickNumbers = platformOrderService.fetchBillCodesOfParcelsWithoutTrace(
                Date.valueOf(startDate), Date.valueOf(endDate), transporters);
        log.info("{} parcels without trace in total", equickNumbers.size());
        List<EQuickResponse> parcels = new ArrayList<>();
        List<EQuickRequest> requests = new ArrayList<>();
        equickNumbers.forEach(number -> {
            EQuickRequest equickRequest = new EQuickRequest(number);
            requests.add(equickRequest);
        });
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);
        List<CompletableFuture<Boolean>> futures = requests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    String responseString = request.send().getBody();
                    try {
                        EQuickResponse eQuickResponse = mapper.readValue(responseString, EQuickResponse.class);
                        parcels.add(eQuickResponse);
                        success = true;
                    } catch (IOException e) {
                        log.error("Error while parsing response into String", e);
                    }
                    log.info("{} parcel added to the queue to be inserted into DB.", parcels.size());
                    return success;
                }, executor))
                .collect(Collectors.toList());

        List<Boolean> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{} of {} parcel traces have been retrieved.", nbSuccesses, requests.size());
        parcelService.saveEQParcelAndTraces(parcels);
    }

}
