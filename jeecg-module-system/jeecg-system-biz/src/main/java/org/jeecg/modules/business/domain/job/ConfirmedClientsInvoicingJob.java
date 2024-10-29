package org.jeecg.modules.business.domain.job;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.SavRefundWithDetail;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.BalanceData;
import org.jeecg.modules.business.vo.FactureDetail;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Component
@Slf4j
public class ConfirmedClientsInvoicingJob implements Job {
    @Autowired
    private IBalanceService balanceService;
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
        log.info("Confirmed clients Invoicing Job started ...");
        List<Client> confirmedClients = clientService.getClientsByType("confirmed");
        List<Client> clients = new ArrayList<>();
        List<BalanceData> balanceDataList = new ArrayList<>();
        List<BalanceData> shippingBalanceDataList = new ArrayList<>();
        List<BalanceData> completeBalanceDataList = new ArrayList<>();
        for(Client client : confirmedClients) {
            BigDecimal balance = balanceService.getBalanceByClientIdAndCurrency(client.getId(), client.getCurrency());
            if(balance.compareTo(BigDecimal.ZERO) > 0) {
                clients.add(client);
                balanceDataList.add(new BalanceData(client, client.getCurrency(), balance));
                if(client.getIsCompleteInvoice().equals("0")) {
                    shippingBalanceDataList.add(new BalanceData(client, client.getCurrency(), balance));
                }
                else {
                    completeBalanceDataList.add(new BalanceData(client, client.getCurrency(), balance));
                }
            }
        }

        log.info("shipping clients list size : " + shippingBalanceDataList.size());
        log.info("complete clients list size : " + completeBalanceDataList.size());
        List<InvoiceMetaData> invoiceList = new ArrayList<>();
        // we need to make sure that the client has enough balance to be invoiced
        // step 1 : get the list of clients that have enough positive balance
        // step 2 : calculer au pro rata le montant de la facture pour qu'elle ne dépasse pas le solde du client
        // step 3 : update balance
        // step 4 : generate invoice
        // step 5 : send mail to client if balance is low
        if(!shippingBalanceDataList.isEmpty()) {
            log.info("Making shipping invoice for clients : {}", shippingBalanceDataList);
            invoiceList = new ArrayList<>(platformOrderShippingInvoiceService.breakdownInvoiceClientByTypeAndBalance(shippingBalanceDataList, 0));
        }
        if(!completeBalanceDataList.isEmpty()) {
            log.info("Making complete shipping invoice for clients : {}", completeBalanceDataList);
            invoiceList.addAll(platformOrderShippingInvoiceService.breakdownInvoiceClientByTypeAndBalance(completeBalanceDataList, 1));
        }
        if(invoiceList.isEmpty()) {
            log.info("Nothing to invoice.");
            return;
        }

        List<InvoiceMetaData> metaDataErrorList = new ArrayList<>();
        List<InvoiceMetaData> invoicedMetaDataList = new ArrayList<>(); // list of invoice metadata that has been invoiced
        log.info("Generating detail files ...0/{}", invoiceList.size());
        int cpt = 1;
        for(InvoiceMetaData metaData: invoiceList){
            if(metaData.getInvoiceCode().equals("error")) {
                metaDataErrorList.add(metaData);
            }
            else {
                invoicedMetaDataList.add(metaData);
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
            String subject = "[" + LocalDate.now() + "] Confirmed clients invoicing job report";
            String destEmail = env.getProperty("spring.mail.username");
            String templateName = "vipInvoicingJobReport.ftl";
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("job", "Confirmed clients");
            templateModel.put("errors", metaDataErrorList);
            try {
                emailService.newSendSimpleMessage(destEmail, subject, templateName, templateModel);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        // emailing for low balance clients
        List<BalanceData> lowBalanceDataList = balanceService.getLowBalanceClients(invoicedMetaDataList);
        //todo : send mail to clients in prod
        if(!lowBalanceDataList.isEmpty()) {
            for(BalanceData data : lowBalanceDataList) {
                log.info("Low balance client : {}", data.getClient().getInternalCode());
                String subject = "[" + LocalDate.now() + "] WIA App low balance notification";
                String destEmail = env.getProperty("spring.mail.username");
                Properties prop = emailService.getMailSender();
                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("firstname", data.getClient().getFirstName());
                templateModel.put("lastname", data.getClient().getSurname());
                templateModel.put("balance", data.getBalance());
                templateModel.put("currency", data.getCurrency());
                templateModel.put("clientCategory", "confirmed");

                Session session = Session.getInstance(prop, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
                    }
                });
                try {
                    freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
                    Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                            .getTemplate("client/lowBalanceNotification.ftl");
                    String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
                    emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
                    //todo : update balance_notification date and reason
                    log.info("Mail sent successfully !");
                } catch (Exception e) {
                    log.error("Error while sending low balance notification mail in VipInvoicingJob : ", e);
                    e.printStackTrace();
                }
            }
        }
        log.info("Confirmed clients invoicing job finished.");
    }
}
