package org.jeecg.modules.business.domain.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.common.api.dto.message.TemplateMessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.*;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuList.SkuData;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.service.ISkuListMabangService;
import org.jeecg.modules.online.cgform.mapper.OnlCgformFieldMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A Job that retrieves all Sku from Mabang
 * if the sku is of status 3 (normal) and not in DB, then we insert it in DB
 */
@Slf4j
@Component
public class MabangSkuJob implements Job {

    private static final String SECTION_START = "<section style=\"display:flex;align-items:stretch;justify-content:space-between;flex-wrap:wrap;\">";
    private static final String DIV_START = "<div style=\"flex:1 1 160px;border:1px solid;padding-left:10px;\">";
    private static final String DIV_END_NEXT_DIV_START = "</div><div style=\"flex:1 1 160px;border:1px solid;padding-left:10px;\">";
    private static final String ROW_START = "<tr><td>";
    private static final String ROW_END = "</td></tr>";
    private static final String NEXT_COLUMN = "</td><td>";
    private static final String TABLE_START = "<style>\n" +
            "table, th, td {\n" +
            "  border: 1px solid black;\n" +
            "  width: 800px;\n" +
            "  text-align: center;\n" +
            "    vertical-align: middle;" +
            "}\n" +
            "</style><table><tr><th style=\"width:20%\">SKU</th><th style=\"width:50%\">中文名</th><th style=\"width:10%\">重量</th><th style=\"width:10%\">申报价</th><th style=\"width:10%\">售价</th></tr>";
    private static final String SECTION_END = "</div></section>";
    private static final String TABLE_END = "</table>";
    @Autowired
    @Setter
    private ISkuListMabangService skuListMabangService;
    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;
    private static final Integer NBR_SKU_PER_PAGE = 400;
    private static final DateType DEFAULT_DATE_TYPE = DateType.CREATE;

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

        Map<Sku, String> newSkusMap = new HashMap<>();
        Map<String, SkuData> skuDataMap = new HashMap<>();
        try {
            while (startDateTime.until(endDateTime, ChronoUnit.HOURS) > 0) {
                LocalDateTime dayBeforeEndDateTime = endDateTime.minusDays(1);
                SkuListRequestBody body = SkuListRequestBodys.allSkuOfDateType(dayBeforeEndDateTime, endDateTime, dateType);
                SkuListRawStream rawStream = new SkuListRawStream(body);
                SkuListStream stream = new SkuListStream(rawStream);
                // the status is directly filtered in all() method
                List<SkuData> skusFromMabang = stream.all();
                skusFromMabang.forEach(skuData -> skuDataMap.put(skuData.getErpCode(), skuData));
                log.info("{} skus from {} to {} ({})to be inserted.", skusFromMabang.size(),
                        dayBeforeEndDateTime, endDateTime, dateType);

                if (skusFromMabang.size() > 0) {
                    // we save the skuDatas in DB
                    newSkusMap.putAll(skuListMabangService.saveSkuFromMabang(skusFromMabang));
                }
                endDateTime = dayBeforeEndDateTime;
            }
        } catch (SkuListRequestErrorException e) {
            throw new RuntimeException(e);
        }

        updateSkuId();
        log.info("SKU codes replaced by new created SKU IDs");

        // here we send system notification with the number and list of new skus saved in DB
        if (newSkusMap.size() == 0) {
            return;
        }
        List<String> messageContentList = new ArrayList<>();
        List<String> newSkuCodeList = newSkusMap.keySet().stream().map(Sku::getErpCode).sorted().collect(Collectors.toList());
        String skuListContent = TABLE_START;
        // we truncate the message and only send skus by groups of 400
        for (int i = 0; i < newSkuCodeList.size(); i++) {
            String skuErpCode = newSkuCodeList.get(i);
            SkuData skuData = skuDataMap.get(skuErpCode);
            if (skuData != null) {
                skuListContent = skuListContent
                        .concat(ROW_START)
                        .concat(skuErpCode)
                        .concat(NEXT_COLUMN)
                        .concat(skuData.getNameCN())
                        .concat(NEXT_COLUMN)
                        .concat(String.valueOf(skuData.getWeight() == null ? 0 : skuData.getWeight()))
                        .concat(NEXT_COLUMN)
                        .concat(String.valueOf(skuData.getDeclareValue() == null ? 0 : skuData.getDeclareValue()))
                        .concat(NEXT_COLUMN)
                        .concat(String.valueOf(skuData.getSalePrice() == null ? 0 : skuData.getSalePrice()))
                        .concat(ROW_END);
            }
            if (i == newSkuCodeList.size() - 1 || i % NBR_SKU_PER_PAGE == NBR_SKU_PER_PAGE - 1) {
                skuListContent = skuListContent.concat(TABLE_END);
                messageContentList.add(skuListContent);
                skuListContent = TABLE_START;
            }
        }

        // here we are printing the list of skus with extra info
        Map<String, String> needTreatmentSkuMap = new HashMap<>();
        for (Map.Entry<Sku, String> entry : newSkusMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                needTreatmentSkuMap.put(entry.getKey().getErpCode(), entry.getValue());
            }
        }
        String needTreatmentSku = SECTION_START;
        int cpt = 0;
        for (Map.Entry<String, String> entry : needTreatmentSkuMap.entrySet()) {
            if (cpt % 10 == 0 && cpt != 0) {
                needTreatmentSku = needTreatmentSku.concat(DIV_END_NEXT_DIV_START);
            }
            if (cpt == 0) {
                needTreatmentSku = needTreatmentSku.concat(DIV_START);
            }
            needTreatmentSku = needTreatmentSku.concat("<p>" + entry.getKey() + " : " + entry.getValue() + "</p>");
            cpt++;
        }
        needTreatmentSku = needTreatmentSku.concat(SECTION_END);

        int page = 1;
        for (String msg : messageContentList) {
            Map<String, String> param = new HashMap<>();
            param.put("nb_of_entries", String.valueOf(newSkusMap.size()));
            param.put("sku_list", msg);
            param.put("need_treatment", needTreatmentSkuMap.size() > 0 ? needTreatmentSku : "None");
            param.put("current_page", String.valueOf(page));
            param.put("total_page", String.valueOf(messageContentList.size()));
            TemplateMessageDTO message = new TemplateMessageDTO("admin", "admin", "SKU导入任务", param, "sku_mabang_job_result");
            ISysBaseApi.sendTemplateAnnouncement(message);
//            message = new TemplateMessageDTO("admin", "Alice", "SKU导入任务", param, "sku_mabang_job_result");
            ISysBaseApi.sendTemplateAnnouncement(message);
            message = new TemplateMessageDTO("admin", "Jessyca", "SKU导入任务", param, "sku_mabang_job_result");
            ISysBaseApi.sendTemplateAnnouncement(message);
            log.info("Page {} of recap sent through system announcement", page);
            page++;
        }
    }

    /**
     * Call a routine to replace SKU codes (from MabangAPI)
     * by SKU IDs in platform_order_content table after creating new SKUs
     */
    private static void updateSkuId() {
        OnlCgformFieldMapper onlCgformFieldMapper = SpringContextUtils.getBean(OnlCgformFieldMapper.class);
        Map<String, Object> params = new HashMap<>();
        String sql = "UPDATE platform_order_content SET sku_id = skuErpToId(sku_id) WHERE sku_id NOT LIKE '1%'";
        params.put("execute_sql_string", sql);
        onlCgformFieldMapper.executeUpdatetSQL(params);
    }
}