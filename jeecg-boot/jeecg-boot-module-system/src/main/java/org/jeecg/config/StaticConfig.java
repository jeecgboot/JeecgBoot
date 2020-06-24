package org.jeecg.config;

import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.message.handle.impl.EmailSendMsgHandle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 设置静态参数初始化
 */
@Configuration
public class StaticConfig {

    @Value("${jeecg.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${jeecg.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;


    @Bean
    public void initStatic() {
        DySmsHelper.setAccessKeyId(accessKeyId);
        DySmsHelper.setAccessKeySecret(accessKeySecret);
        EmailSendMsgHandle.setEmailFrom(emailFrom);
    }
}
