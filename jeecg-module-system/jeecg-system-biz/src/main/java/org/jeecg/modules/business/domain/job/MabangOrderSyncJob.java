package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.common.api.dto.message.TemplateMessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.jetbrains.annotations.NotNull;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class MabangOrderSyncJob implements Job {

    @Autowired
    private IPlatformOrderMabangService platformOrderMabangService;
    @Autowired
    private ISysBaseAPI ISysBaseApi;
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

        try {
            JSONObject res = platformOrderMabangService.syncOrdersFromMabang(platformOrderIds);
            String syncedOrderNumber = String.valueOf(res.getInt("synced_order_number"));
            List<String> syncedOrderIds = new ArrayList<>();
            JSONArray syncedOrderIdsArray = res.getJSONArray("synced_order_ids");
            for (int i = 0; i < syncedOrderIdsArray.length(); i++) {
                syncedOrderIds.add(syncedOrderIdsArray.getString(i));
            }
            Map<String, String> param = new HashMap<>();
            param.put("requested_order_number", String.valueOf(platformOrderIds.size()));
            param.put("synced_order_number", syncedOrderNumber);
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
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

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