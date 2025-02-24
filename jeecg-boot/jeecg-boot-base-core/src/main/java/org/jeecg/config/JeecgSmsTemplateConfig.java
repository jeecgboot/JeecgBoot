package org.jeecg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
* @Description: 短信模板
*
* @author: wangshuai
* @date: 2024/11/5 下午3:44
*/
@Data
@Component("jeecgSmsTemplateConfig")
@ConfigurationProperties(prefix = "jeecg.oss.sms-template")
public class JeecgSmsTemplateConfig {

    /**
     * 短信签名
     */
    private String signature;


    /**
     * 短信模板code
     *
     * @return
     */
    private Map<String,String> templateCode;
    
}
