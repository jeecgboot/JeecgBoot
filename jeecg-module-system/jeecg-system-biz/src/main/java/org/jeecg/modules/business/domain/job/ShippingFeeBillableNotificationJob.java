package org.jeecg.modules.business.domain.job;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.service.EmailService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.vo.ShippingFeeBillableOrders;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Slf4j
public class ShippingFeeBillableNotificationJob implements Job {
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    Environment env;

    /**
     * Job that finds all order contents from uninvoiced orders with missing stock and checks if products have been ordered
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Transactional
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Shipping Fee Billable Notification Job started ...");

        List<ShippingFeeBillableOrders> shippingFeeBillableOrders = platformOrderService.fetchShippingFeeBillableOrders();
        if (shippingFeeBillableOrders.isEmpty()) {
            log.info("No orders awaiting shipping fees to be billed.");
            return;
        }
        Map<String, List<ShippingFeeBillableOrders>> ordersByClientEmail = shippingFeeBillableOrders.stream().collect(groupingBy(ShippingFeeBillableOrders::getEmail));


        Properties prop = emailService.getMailSender();
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
            }
        });

        for(Map.Entry<String, List<ShippingFeeBillableOrders>> entry : ordersByClientEmail.entrySet()) {
            String subject = "Shipping Fee Billable Notification";
            String destEmail = entry.getKey();
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("fullName", entry.getValue().get(0).getFullName());
            templateModel.put("orderNumbers", entry.getValue().stream().map(ShippingFeeBillableOrders::getOrderNumber).collect(Collectors.toList()));
            try {
                freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
                Template template = freemarkerConfigurer.getConfiguration().getTemplate("client/shippingFeeBillableNotification.ftl");
                String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
                emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
                log.info("Mail sent successfully");
            } catch (Exception e) {
                log.error("Error sending mail: " + e.getMessage());
            }
        }
        log.info("Shipping Fee Billable Notification Job finished ...");
    }
}
