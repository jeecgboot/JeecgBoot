package org.jeecg.modules.business.domain.job;

import freemarker.template.Template;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.*;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.SkuData;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.service.EmailService;
import org.jeecg.modules.business.service.ISkuListMabangService;
import org.jeecg.modules.online.cgform.mapper.OnlCgformFieldMapper;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * A Job that retrieves all Sku from Mabang
 * if the sku is of status 3 (normal) and not in DB, then we insert it in DB
 */
@Slf4j
@Component
public class MabangSkuJob implements Job {

    @Autowired
    @Setter
    private ISkuListMabangService skuListMabangService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    Environment env;
    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;
    private static final DateType DEFAULT_DATE_TYPE = DateType.CREATE;

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

        Map<Sku, String> newSkusNeedTreatmentMap = new HashMap<>();
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

                if (!skusFromMabang.isEmpty()) {
                    // we save the skuDatas in DB
                    newSkusNeedTreatmentMap.putAll(skuListMabangService.saveSkuFromMabang(skusFromMabang));
                }
                endDateTime = dayBeforeEndDateTime;
            }
        } catch (SkuListRequestErrorException e) {
            throw new RuntimeException(e);
        }

        updateSkuId();
        log.info("SKU codes replaced by new created SKU IDs");

        // here we send system notification with the list of new skus that need extra treatment
        if (newSkusNeedTreatmentMap.isEmpty()) {
            return;
        }
        Properties prop = emailService.getMailSender();
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
            }
        });

        String subject = "Association of Sku to Client failed while creating new Sku";
        String destEmail = env.getProperty("company.jessy.email");
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("operation", "créés");
        templateModel.put("skusMap", newSkusNeedTreatmentMap);
        try {
            freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
            Template template = freemarkerConfigurer.getConfiguration().getTemplate("admin/unknownClientForSku.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
            emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
            log.info("Mail sent successfully");
        } catch (Exception e) {
            log.error("Error sending mail: {}", e.getMessage());
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