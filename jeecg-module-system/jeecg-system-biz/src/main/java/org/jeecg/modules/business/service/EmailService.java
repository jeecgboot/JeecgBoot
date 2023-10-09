package org.jeecg.modules.business.service;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import java.util.Properties;

public interface EmailService {
    public void sendSimpleMessage(String recipient, String subject, String text, Session session) throws MessagingException;
    public void sendMessageWithAttachment(String recipient, String subject, String text, String attachment, Session session) throws MessagingException, IOException;
    public Properties getMailSender();
    public FreeMarkerConfigurer freemarkerClassLoaderConfig() throws IOException;
}
