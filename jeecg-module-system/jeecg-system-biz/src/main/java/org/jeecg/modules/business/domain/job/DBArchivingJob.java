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
        LocalDateTime endDateTime = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).minusDays(DEFAULT_NUMBER_OF_DAYS);
        LocalDateTime startDateTime = null;
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
                    if(endDateTime.isAfter(LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).minusDays(DEFAULT_NUMBER_OF_DAYS))) {
                        throw new RuntimeException("Error : Only orders older than 1 year can be archived !");
                    }
                }
            }
            catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        String startDate;
        endDateTime = endDateTime.plusDays(1);
        String endDate = endDateTime.toString().substring(0,10);
        List<PlatformOrder> platformOrders;
        if (startDateTime != null) {
            if(!endDateTime.isAfter(startDateTime))
                throw new RuntimeException("EndDateTime must be strictly greater than StartDateTime !");
            startDate = startDateTime.toString().substring(0,10);
            platformOrders = platformOrderService.fetchOrdersToArchiveBetweenDate(startDate, endDate);
            log.info("Archiving entries between ["+startDate+" and "+endDate+"]");
        }
        else {
            platformOrders = platformOrderService.fetchOrdersToArchiveBeforeDate(endDate);
            log.info("Archiving entries before ["+endDate+"]");
        }
        if(platformOrders.size() > 0) {
            // sauvegarde des entrées dans des listes
            // suppression des entrées dans l'ancienne table
            List<String> platformOrderIDs = platformOrders.stream().map(PlatformOrder::getId).collect(Collectors.toList());
            List<PlatformOrderContent> platformOrderContents = platformOrderContentService.fetchPlatformOrderContentsToArchive(platformOrderIDs);
            log.info("- Platform Order entries : " + platformOrders.size() + "\n"
                    + "- Platform Order Content entries : " + platformOrderContents.size());
            platformOrderService.savePlatformOrderArchive(platformOrders);
            platformOrderContentService.savePlatformOrderContentArchive(platformOrderContents);
            platformOrderService.delBatchMain(platformOrderIDs);

            List<String> platformOrderTrackingNumber = platformOrders.stream().map(PlatformOrder::getTrackingNumber).collect(Collectors.toList());
            if (platformOrderTrackingNumber.size() > 0) {
                List<Parcel> parcels = parcelService.fetchParcelsToArchive(platformOrderTrackingNumber);
                if (parcels.size() > 0) {
                    log.info("- Parcel entries : " + parcels.size());
                    parcelService.saveParcelArchive(parcels);

                    List<String> parcelIDs = parcels.stream().map(Parcel::getId).collect(Collectors.toList());
                    List<ParcelTrace> parcelTraces = parcelTraceService.fetchParcelTracesToArchive(parcelIDs);
                    if (parcelTraces.size() > 0) {
                        log.info("- Parcel trace entries : " + parcelTraces.size());
                        parcelTraceService.saveParcelTraceArchive(parcelTraces);
                    }
                    parcelService.delBatchMain(parcelIDs);
                }
            }
            log.info("Archiving Done.");
        }
        else {
            log.info("Nothing to archive !");
        }
    }
}
