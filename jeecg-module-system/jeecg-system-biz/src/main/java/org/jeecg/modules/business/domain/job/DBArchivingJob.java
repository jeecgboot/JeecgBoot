package org.jeecg.modules.business.domain.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.entity.Parcel;
import org.jeecg.modules.business.entity.ParcelTrace;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.service.IParcelService;
import org.jeecg.modules.business.service.IParcelTraceService;
import org.jeecg.modules.business.service.IPlatformOrderContentService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A  job that archives entries from platform_order, platform_order_content, parcel, parcel_trace
 */
@Slf4j
@Component
public class DBArchivingJob implements Job {
    @Autowired
    @Setter
    private IParcelService parcelService;
    @Autowired
    @Setter
    private IParcelTraceService parcelTraceService;
    @Autowired
    @Setter
    private IPlatformOrderService platformOrderService;
    @Autowired
    @Setter
    private IPlatformOrderContentService platformOrderContentService;
    private static final Integer DEFAULT_NUMBER_OF_DAYS = 365;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime endDateTime = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
        LocalDateTime startDateTime = endDateTime.minusDays(DEFAULT_NUMBER_OF_DAYS);
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
            }
            catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }
        if (!endDateTime.isAfter(startDateTime)) {
            throw new RuntimeException("EndDateTime must be strictly greater than StartDateTime !");
        }
        System.out.println("startdatetime : " + startDateTime + "\nendDateTime : " + endDateTime);
        String startDate = startDateTime.toString().substring(0,10);
        endDateTime = endDateTime.plusDays(1);
        String endDate = endDateTime.toString().substring(0,10);
        System.out.println("startdatetime : " + startDateTime + "\nendDateTime : " + endDateTime);
        System.out.println("startdate : " + startDate + "\nendDate : " + endDate);

        // step1: sauvegarde des entrées dans des objets
        // insertion des objets dans les tables d'archives
        // drop les entrées dans l'ancienne table

        List<PlatformOrder> platformOrders = platformOrderService.fetchPlatformOrdersToArchive(startDate, endDate);
        List<String> platformOrderIDs = platformOrders.stream().map(PlatformOrder::getId).collect(Collectors.toList());
        List<PlatformOrderContent> platformOrderContents = platformOrderContentService.fetchPlatformOrderContentsToArchive(platformOrderIDs);
        List<String> platformOrderTrackingNumber = platformOrders.stream().map(PlatformOrder::getTrackingNumber).collect(Collectors.toList());
        try {
            List<Parcel> parcels = parcelService.fetchParcelsToArchive(platformOrderTrackingNumber);
            List<String> parcelIDs = parcels.stream().map(Parcel::getId).collect(Collectors.toList());
            List<ParcelTrace> parcelTraces = parcelTraceService.fetchParcelTracesToArchive(parcelIDs);
            System.out.println("Parcel count : " + parcels.size());
            System.out.println("Parcel_trace count : " + parcelTraces.size());
        } catch (Exception ignored) {

        }
        System.out.println(platformOrderIDs);
    }
}
