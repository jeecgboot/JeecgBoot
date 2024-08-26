package org.jeecg.modules.business.domain.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.jeecg.modules.business.domain.api.mabang.getorderlist.OrderStatus.*;

@Setter
@Component
@Slf4j
public class MabangJob implements Job {

    @Autowired
    private IPlatformOrderMabangService platformOrderMabangService;

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;
    private static final DateType DEFAULT_DATE_TYPE = DateType.EXPRESS;
    private static final List<OrderStatus> DEFAULT_STATUSES = Arrays.asList(AllUnshipped, Shipped, Completed);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime endDateTime = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
        LocalDateTime startDateTime = endDateTime.minusDays(DEFAULT_NUMBER_OF_DAYS);
        DateType dateType = DEFAULT_DATE_TYPE;
        boolean overrideRestriction = false;
        List<OrderStatus> statuses = DEFAULT_STATUSES;
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                JSONArray statusCodes = jsonObject.getJSONArray("status");
                if (null != statusCodes) {
                    statuses = new ArrayList<>();
                    for (int i = 0; i < statusCodes.length(); i++) {
                        statuses.add(OrderStatus.fromCode(statusCodes.getInt(i)));
                    }
                }
                if (!jsonObject.isNull("startDateTime")) {
                    String startDateStr = jsonObject.getString("startDateTime");
                    startDateTime = LocalDateTime.parse(startDateStr);
                }
                if (!jsonObject.isNull("endDateTime")) {
                    String endDateStr = jsonObject.getString("endDateTime");
                    endDateTime = LocalDateTime.parse(endDateStr);
                }
                if (!jsonObject.isNull("dateType")) {
                    dateType = DateType.fromCode(jsonObject.getInt("dateType"));
                }
                if (!jsonObject.isNull("override")) {
                    overrideRestriction = jsonObject.getBoolean("override");
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        if (!endDateTime.isAfter(startDateTime)) {
            throw new RuntimeException("EndDateTime must be strictly greater than StartDateTime !");
        } else if (endDateTime.minusDays(30).isAfter(startDateTime) && !overrideRestriction) {
            throw new RuntimeException("No more than 30 days can separate startDateTime and endDateTime !");
        }

        try {
            while (startDateTime.until(endDateTime, ChronoUnit.HOURS) > 0) {
                LocalDateTime dayBeforeEndDateTime = endDateTime.minusDays(1);
                for (OrderStatus status : statuses) {
                    OrderListRequestBody body = OrderListRequestBodys
                            .allOrderOfDateTypeOfStatus(dayBeforeEndDateTime, endDateTime, dateType, status);
                    OrderListRawStream rawStream = new OrderListRawStream(body);
                    OrderListStream stream = new OrderListStream(rawStream);
                    List<Order> unshipped = stream.all();
                    log.info("{} {} orders from {} to {} ({})to be inserted/updated.", unshipped.size(), status,
                            dayBeforeEndDateTime, endDateTime, dateType);
                    platformOrderMabangService.saveOrderFromMabang(unshipped);
                }
                endDateTime = dayBeforeEndDateTime;
            }
        } catch (OrderListRequestErrorException e) {
            throw new RuntimeException(e);
        }
    }

}
