package com.shop.service;

import lombok.SneakyThrows;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * 发送邮件
 * 2019-06-19 04:07
 */
@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String formEmail;  // 发件人

    @Autowired
    private JavaMailSender mailSender;


    public void sendTextEmail(String title, String content, String[] toEmails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(formEmail);
        message.setTo(toEmails);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendFullTextEmail(String title, String html, String[] toEmails) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(formEmail);
        helper.setTo(toEmails);
        helper.setSubject(title);
        // 发送邮件
        helper.setText(html, true);
        mailSender.send(mimeMessage);
    }

    @Async
    @SneakyThrows
    public void sendHtmlEmail(String title, String htmlPath, Map<String, Object> map, String[] toEmails) {
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("templates/");
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate(htmlPath);  // 加载html模板
        t.binding(map);  // 填充数据
        String html = t.render();  // 获得渲染后的html
        sendFullTextEmail(title, html, toEmails);  // 发送邮件
    }

}
