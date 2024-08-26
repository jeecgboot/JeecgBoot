package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.*;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.service.EmailService;
import org.jeecg.modules.business.service.ISkuListMabangService;
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
 * Updates product name (en, zh), sku status, weight and isGift
 * By default checks sku updates within the last 5 days
 * If "skus" param is not empty then, dates are ignored and it will only update the given skus (erpCode)
 * Execution periodicity : everyday at 2:10pm : 10 14 * * *
 */
@Slf4j
@Component
public class MabangSkuSyncJob implements Job {

    @Autowired
    private ISkuListMabangService skuListMabangService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    private Environment env;

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;
    private static final DateType DEFAULT_DATE_TYPE = DateType.UPDATE;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime endDateTime = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
        LocalDateTime startDateTime = endDateTime.minusDays(DEFAULT_NUMBER_OF_DAYS);
        List<String> skus = new ArrayList<>();
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
                if (!jsonObject.isNull("skus")) {
                    JSONArray array = jsonObject.getJSONArray("skus");
                    for(int i = 0; i < array.length(); i++) {
                        skus.add(array.getString(i));
                    }
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        if (!endDateTime.isAfter(startDateTime)) {
            throw new RuntimeException("EndDateTime must be strictly greater than StartDateTime !");
        }
        Map<Sku, String> skusNeedTreatmentMap = new HashMap<>();
        try {
            if(skus.isEmpty()) {
                log.info("Updating skus by date");
                while (startDateTime.until(endDateTime, ChronoUnit.HOURS) > 0) {
                    LocalDateTime dayBeforeEndDateTime = endDateTime.minusDays(1);
                    SkuListRequestBody body = SkuListRequestBodys.allSkuOfDateType(dayBeforeEndDateTime, endDateTime, dateType);
                    SkuListRawStream rawStream = new SkuListRawStream(body);
                    SkuUpdateListStream stream = new SkuUpdateListStream(rawStream);
                    // the status is directly filtered in all() method
                    List<SkuData> skusFromMabang = stream.all();
                    log.info("{} skus from {} to {} ({})to be updated.", skusFromMabang.size(),
                            dayBeforeEndDateTime, endDateTime, dateType);

                    if (!skusFromMabang.isEmpty()) {
                        // we save the skuDatas in DB
                        skusNeedTreatmentMap.putAll(skuListMabangService.updateSkusFromMabang(skusFromMabang));
                    }
                    endDateTime = dayBeforeEndDateTime;
                }
            }
            else {
                log.info("Updating skus by erpCode : {}", skus);
                List<List<String>> skusPartition = Lists.partition(skus, 50);
                for(List<String> skuPartition : skusPartition) {
                    SkuListRequestBody body = new SkuListRequestBody();
                    body.setStockSkuList(String.join(",", skuPartition));
                    SkuListRawStream rawStream = new SkuListRawStream(body);
                    SkuUpdateListStream stream = new SkuUpdateListStream(rawStream);
                    List<SkuData> skusFromMabang = stream.all();
                    log.info("{} skus to be updated.", skusFromMabang.size());
                    if (!skusFromMabang.isEmpty()) {
                        // we save the skuDatas in DB
                        skusNeedTreatmentMap.putAll(skuListMabangService.updateSkusFromMabang(skusFromMabang));
                    }
                }
            }
        } catch (SkuListRequestErrorException e) {
            throw new RuntimeException(e);
        }
        if(skusNeedTreatmentMap.isEmpty()) {
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
        templateModel.put("operation", "mis à jour");
        templateModel.put("skusMap", skusNeedTreatmentMap);
        try {
            freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
            Template template = freemarkerConfigurer.getConfiguration().getTemplate("admin/skusNeedTreatment.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
            emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
            log.info("Mail sent successfully");
        } catch (Exception e) {
            log.error("Error sending mail: {}", e.getMessage());
        }
    }
}