package org.jeecg.modules.business.domain.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.common.api.dto.message.TemplateMessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.*;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.SkuData;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.service.ISkuListMabangService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.SkuStatus.*;

/**
 * A Job that retrieves all Sku from Mabang
 * if the sku is of status 3 (normal) and not in DB, then we insert it in DB
 */
@Slf4j
public class MabangSkuJob implements Job {

    @Autowired
    @Setter
    private ISkuListMabangService skuListMabangService;
    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;
    private static final DateType DEFAULT_DATE_TYPE = DateType.UPDATE;

    @Autowired
    private ISysBaseAPI ISysBaseApi;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime endDateTime = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
        LocalDateTime startDateTime = endDateTime.minusDays(DEFAULT_NUMBER_OF_DAYS);
        DateType dateType = DEFAULT_DATE_TYPE;
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
                if (!jsonObject.isNull("dateType")) {
                    dateType = DateType.fromCode(jsonObject.getInt("dateType"));
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        if (!endDateTime.isAfter(startDateTime)) {
            throw new RuntimeException("EndDateTime must be strictly greater than StartDateTime !");
        }
//        else if (endDateTime.minusDays(30).isAfter(startDateTime)) {
//            throw new RuntimeException("No more than 30 days can separate startDateTime and endDateTime !");
//        }

        Map<Sku, String> newSkusMap = new HashMap<>();
        try {
            while (startDateTime.until(endDateTime, ChronoUnit.HOURS) > 0) {
                LocalDateTime dayBeforeEndDateTime = endDateTime.minusDays(1);
                SkuListRequestBody body = SkuListRequestBodys.allSkuOfDateType(dayBeforeEndDateTime, endDateTime, dateType);
                SkuListRawStream rawStream = new SkuListRawStream(body);
                SkuListStream stream = new SkuListStream(rawStream);
                // the status is directly filtered in all() method
                List<SkuData> skusFromMabang = stream.all();
                log.info("{} skus from {} to {} ({})to be inserted.", skusFromMabang.size(),
                        dayBeforeEndDateTime, endDateTime, dateType);

                if(skusFromMabang.size() > 0) {
                    // we save the skuDatas in DB
                    newSkusMap.putAll(skuListMabangService.saveSkuFromMabang(skusFromMabang));
                }
                endDateTime = dayBeforeEndDateTime;
            }
        } catch (SkuListRequestErrorException e) {
            throw new RuntimeException(e);
        }

        // here we send system notification with the number and list of new skus saved in DB
        if(newSkusMap.size() > 0) {
            List<String> messageContentList = new ArrayList<>();
            List<String> newSkuCodeList = newSkusMap.keySet().stream().map(Sku::getErpCode).sorted().collect(Collectors.toList());
            String skuListContent = "";
            // we truncate the message and only send skus by groups of 400
            for (int i = 0; i < newSkuCodeList.size(); i++) {
                if (i % 10 == 0 && i != 0 && i % 400 != 0) {
                    skuListContent = skuListContent.concat("</div><div style=\"flex:1 1 160px;border:1px solid;padding-left:10px;\">");
                }
                if (i == 0) {
                    skuListContent = skuListContent.concat("<section style=\"display:flex;align-items:stretch;justify-content:space-between;flex-wrap:wrap;\">" +
                            "<div style=\"flex:1 1 160px;border:1px solid;padding-left:10px;\">");
                }
                skuListContent = skuListContent.concat("<p>" + newSkuCodeList.get(i) + "</p>");
                if (i == newSkuCodeList.size() - 1 || i % 400 == 399) {
                    skuListContent = skuListContent.concat("</div></section>");
                    messageContentList.add(skuListContent);
                    skuListContent = "<section style=\"display:flex;align-items:stretch;justify-content:space-between;flex-wrap:wrap;\">" +
                            "<div style=\"flex:1 1 160px;border:1px solid;padding-left:10px;\">";
                }
            }

            // here we are printing the list of skus with extra info
            Map<String, String> needTreatmentSkuMap = new HashMap<>();
            for (Map.Entry<Sku, String> entry : newSkusMap.entrySet()) {
                if (!entry.getValue().equals("")) {
                    needTreatmentSkuMap.put(entry.getKey().getErpCode(), entry.getValue());
                }
            }
            String needTreatmentSku = "<section style=\"display:flex;align-items:stretch;justify-content:space-between;flex-wrap:wrap;\">";
            int cpt = 0;
            for (Map.Entry<String, String> entry : needTreatmentSkuMap.entrySet()) {
                if (cpt % 10 == 0 && cpt != 0) {
                    needTreatmentSku = needTreatmentSku.concat("</div><div style=\"flex:1 1 160px;border:1px solid;padding-left:10px;\">");
                }
                if (cpt == 0) {
                    needTreatmentSku = needTreatmentSku.concat("<div style=\"flex:1 1 160px;border:1px solid;padding-left:10px;\">");
                }
                needTreatmentSku = needTreatmentSku.concat("<p>" + entry.getKey() + " : " + entry.getValue() + "</p>");
                cpt++;
            }
            needTreatmentSku = needTreatmentSku.concat("</div></section>");

            int page = 1;
            for (String msg : messageContentList) {
                Map<String, String> param = new HashMap<>();
                param.put("nb_of_entries", String.valueOf(newSkusMap.size()));
                param.put("sku_list", msg);
                param.put("need_treatment", needTreatmentSkuMap.size() > 0 ? needTreatmentSku : "None");
                param.put("current_page", String.valueOf(page));
                param.put("total_page", String.valueOf(messageContentList.size()));
                page++;
                TemplateMessageDTO message = new TemplateMessageDTO("admin", "admin", "SKU导入任务", param, "sku_mabang_job_result");
                ISysBaseApi.sendTemplateAnnouncement(message);
                message = new TemplateMessageDTO("admin", "Alice", "SKU导入任务", param, "sku_mabang_job_result");
                ISysBaseApi.sendTemplateAnnouncement(message);
                message = new TemplateMessageDTO("admin", "Jessyca", "SKU导入任务", param, "sku_mabang_job_result");
                ISysBaseApi.sendTemplateAnnouncement(message);
            }
        }
    }

}
