package org.jeecg.modules.business.domain.job;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.FactureDetail;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class VipInvoicingJob implements Job {
    @Autowired
    private IClientService clientService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PlatformOrderShippingInvoiceService platformOrderShippingInvoiceService;
    @Autowired
    private ISavRefundWithDetailService savRefundWithDetailService;

    @Autowired
    Environment env;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("VIP Invoicing Job executed.");
        List<Client> clients = clientService.getClientsByType("vip");
        List<String> shippingClientIds = clients.stream().filter(client -> client.getIsCompleteInvoice().equals("0")).map(Client::getId).collect(Collectors.toList());
        List<String> completeClientIds = clients.stream().filter(client -> client.getIsCompleteInvoice().equals("1")).map(Client::getId).collect(Collectors.toList());

        log.info("shippingClientIds size : " + shippingClientIds.size());
        log.info("completeClientIds size : " + completeClientIds.size());
        List<InvoiceMetaData> invoiceList = new ArrayList<>();
        if(!shippingClientIds.isEmpty()) {
            log.info("Making shipping invoice for clients : {}", shippingClientIds);
            invoiceList = new ArrayList<>(platformOrderShippingInvoiceService.breakdownInvoiceClientByType(shippingClientIds, 0));
        }
        if(!completeClientIds.isEmpty()) {
            log.info("Making complete shipping invoice for clients : {}", completeClientIds);
            invoiceList.addAll(platformOrderShippingInvoiceService.breakdownInvoiceClientByType(completeClientIds, 1));
        }
        if (invoiceList.isEmpty()) {
            log.info("Nothing to invoice.");
            return;
        }

        List<InvoiceMetaData> metaDataErrorList = new ArrayList<>();
        log.info("Generating detail files ...0/{}", invoiceList.size());
        int cpt = 1;
        for(InvoiceMetaData metaData: invoiceList){
            if(metaData.getInvoiceCode().equals("error")) {
                metaDataErrorList.add(metaData);
            }
            else {
                List<FactureDetail> factureDetails = platformOrderShippingInvoiceService.getInvoiceDetail(metaData.getInvoiceCode());
                List<SavRefundWithDetail> refunds = savRefundWithDetailService.getRefundsByInvoiceNumber(metaData.getInvoiceCode());
                try {
                    platformOrderShippingInvoiceService.exportToExcel(factureDetails, refunds, metaData.getInvoiceCode(), metaData.getInvoiceEntity(), metaData.getInternalCode());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("Generating detail files ...{}/{}", cpt++, invoiceList.size());
        }
        if(!metaDataErrorList.isEmpty()) {
            String subject = "[" + LocalDate.now() + "] VIP invoicing job report";
            String destEmail = env.getProperty("spring.mail.username");
            Properties prop = emailService.getMailSender();
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("errors", metaDataErrorList);

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
                }
            });
            try {
                freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
                Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                        .getTemplate("vipInvoicingJobReport.ftl");
                String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
                emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
                log.info("Mail sent successfully");
            } catch (Exception e) {
                log.error("Error while sending mail in VipInvoicingJob", e);
                e.printStackTrace();
            }
        }
        log.info("VIP invoicing job finished.");
    }
}
