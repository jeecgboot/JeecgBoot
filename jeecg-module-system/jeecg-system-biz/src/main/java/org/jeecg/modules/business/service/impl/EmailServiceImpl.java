package org.jeecg.modules.business.service.impl;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import org.jeecg.modules.business.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    Environment env;
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
    public void sendSimpleMessage() {

    }
    @Override
    @Transactional
    public void sendMessageWithAttachment(String recipient, String subject, String text, String attachment, Session session) throws MessagingException, IOException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(Objects.requireNonNull(env.getProperty("spring.mail.username"))));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

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
    public FreeMarkerConfigurer freemarkerClassLoaderConfig() throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        TemplateLoader templateLoader = new FileTemplateLoader(new File(env.getProperty("jeecg.path.emailTemplateDir"))) {
        };
        configuration.setTemplateLoader(templateLoader);
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }
}
