package org.jeecg.modules.business.service.impl;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    Environment env;
    @Autowired
    FreeMarkerConfigurer freemarkerConfigurer;
    @Override
    @Transactional
    public Properties getMailSender() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth"));
        prop.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
        prop.put("mail.smtp.host", env.getProperty("spring.mail.host"));
        prop.put("mail.smtp.port", "587");
        prop.put("mail.debug", "true");

        return prop;
    }
    @Override
    @Transactional
    public void sendSimpleMessage(String recipient, String subject, String text, Session session) throws MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(Objects.requireNonNull(env.getProperty("spring.mail.username"))));
        message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(recipient)[0]);
        if(!recipient.equals(env.getProperty("spring.mail.username")))
            message.setRecipient(Message.RecipientType.CC, InternetAddress.parse(Objects.requireNonNull(env.getProperty("spring.mail.username")))[0]);

        message.setSubject(subject);
        message.setContent(text, "text/html; charset=utf-8");

        Transport.send(message);
    }

    @Override
    @Transactional
    public void newSendSimpleMessage(String recipient, String subject, String templateName, Map<String, Object> templateModel) {
        Properties prop = getMailSender();
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
            }
        });
        try {
            freemarkerConfigurer = freemarkerClassLoaderConfig();
            Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                    .getTemplate(templateName);
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(Objects.requireNonNull(env.getProperty("spring.mail.username"))));
            message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(recipient)[0]);
            if(!recipient.equals(env.getProperty("spring.mail.username")))
                message.setRecipient(Message.RecipientType.CC, InternetAddress.parse(Objects.requireNonNull(env.getProperty("spring.mail.username")))[0]);

            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);

            log.info("Mail sent successfully");
        } catch (Exception e) {
            log.error("Error while sending mail in VipInvoicingJob", e);
            e.printStackTrace();
        }

    }
    @Override
    @Transactional
    public void sendMessageWithAttachment(String recipient, String subject, String text, String attachment, Session session) throws MessagingException, IOException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(Objects.requireNonNull(env.getProperty("spring.mail.username"))));
        message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(recipient)[0]);
        if(!recipient.equals(env.getProperty("spring.mail.username")))
            message.setRecipient(Message.RecipientType.CC, InternetAddress.parse(Objects.requireNonNull(env.getProperty("spring.mail.username")))[0]);

        message.setSubject(subject);

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(text, "text/html; charset=utf-8");

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(new File(attachment));

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);
        Transport.send(message);
    }
    @Override
    @Transactional
    public FreeMarkerConfigurer freemarkerClassLoaderConfig() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setTemplateLoader(new ClassTemplateLoader(getClass(), "/templates"));
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }
}
