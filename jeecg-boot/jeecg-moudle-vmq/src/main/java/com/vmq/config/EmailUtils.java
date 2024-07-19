package com.vmq.config;

import com.vmq.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Exrickx
 */
@Component
public class EmailUtils {

    private static final Logger log = LoggerFactory.getLogger(EmailUtils.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 发送模版邮件
     * @param sender
     * @param sendto
     * @param templateName
     * @param o
     */
    @Async
    public void sendTemplateMail(String sender, String sendto,String title, String templateName,Object o) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            //true表示需要创建一个multipart message html内容
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(sendto);
            helper.setSubject(title);

            Context context = new Context();
            context.setVariable("title",title);
            context.setVariables(StringUtils.beanToMap(o));
            //获取模板html代码
            String content = templateEngine.process(templateName, context);

            helper.setText(content, true);

            mailSender.send(message);
            log.info("给"+sendto+"发送邮件成功");
        }catch (Exception e){
            e.printStackTrace();
            log.info("给"+sendto+"发送邮件失败");
        }
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^\\w+@[a-zA-Z0-9]{2,10}(?:\\.[a-z]{2,4}){1,3}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
