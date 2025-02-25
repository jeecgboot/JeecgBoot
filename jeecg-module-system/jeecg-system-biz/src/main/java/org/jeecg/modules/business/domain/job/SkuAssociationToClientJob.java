package org.jeecg.modules.business.domain.job;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.ClientSku;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.service.*;
import org.quartz.Job;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * A Job that pairs sku with client based on sku suffix
 */
@Slf4j
@Component
public class SkuAssociationToClientJob implements Job {

    @Autowired
    private ISkuService skuService;
    @Autowired
    private IClientSkuService clientSkuService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    Environment env;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("SkuAssociationToClientJob start");
        List<Sku> unpairedSkus = clientSkuService.getUnpairedSkus();
        System.out.println("There is " + unpairedSkus.size() + "Unpaired Skus.");
        List<String> unknownClientSkus = clientSkuService.saveClientSku(unpairedSkus);

        // send email for manual check
        if(!unknownClientSkus.isEmpty()) {
            log.info("Sending email for manual check.");
            Properties prop = emailService.getMailSender();
            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
                }
            });

            String subject = "Association of Sku to Client failed";
            String destEmail = env.getProperty("spring.mail.username");
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("skus", unknownClientSkus);
            try {
                freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
                Template template = freemarkerConfigurer.getConfiguration().getTemplate("admin/unknownClientForSku.ftl");
                String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
                emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
                log.info("Mail sent successfully");
            } catch (Exception e) {
                log.error("Error sending mail: " + e.getMessage());
            }
        }

    }
}