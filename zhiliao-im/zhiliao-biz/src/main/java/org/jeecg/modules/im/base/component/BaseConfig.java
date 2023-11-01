package org.jeecg.modules.im.base.component;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource({"classpath:config.properties"})
@Data
public class BaseConfig {
	@Value("${web.upload-path}")
	private String baseUploadPath;

	@Value("${jpush.master-secret}")
	private String jpushMasterSecret;
	@Value("${jpush.app-key}")
	private String jpushAppKey;
}