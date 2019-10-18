package org.jeecg.config.oss.tencent;

import java.util.Map;
import java.util.Properties;

import com.qcloud.cos.COS;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.jeecg.config.oss.OSSCondition;
import org.jeecg.config.oss.OSSManager;
import org.jeecg.config.oss.OSSProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ COS.class })
@ConditionalOnProperty(prefix = "jeecg.oss", name = "type",havingValue = "qc")
@Conditional(OSSCondition.class)
public class QcCOSAutoConfiguration {

	private final OSSProperties properties;

	public QcCOSAutoConfiguration(OSSProperties ossProperties) {
		this.properties = ossProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public ClientConfig clientConfiguration(OSSProperties ossProperties) {
		Properties properties = asProperties(ossProperties.getProperties());
		ClientConfig configuration = new ClientConfig();
		configuration.setMaxConnectionsCount(Integer.parseInt(properties.getProperty("qc.maxConnectionsCount", "5")));
		configuration.setSocketTimeout(Integer.parseInt(properties.getProperty("qc.socketTimeout", "50000")));
		configuration.setConnectionTimeout(Integer.parseInt(properties.getProperty("qc.connectionTimeout", "50000")));
		configuration.setConnectionRequestTimeout(
				Integer.parseInt(properties.getProperty("qc.connectionRequestTimeout", "-1")));
		configuration.setRegion(new Region(properties.getProperty("qc.region")));
		if (properties.getProperty("qc.userAgent") != null) {
			configuration.setUserAgent(properties.getProperty("qc.userAgent"));
		}

		return configuration;
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public COS ossClient(ClientConfig clientConfig) {
		COSCredentials cred = new BasicCOSCredentials(this.properties.getAccessKey(), this.properties.getSecretKey());
		return new COSClient(cred, clientConfig);
	}

	@Bean
	@ConditionalOnMissingBean
	public OSSManager ossManager(COS client) {
		return new QcCOSManager(client, this.properties);
	}

	private Properties asProperties(Map<String, String> source) {
		Properties properties = new Properties();
		properties.putAll(source);
		return properties;
	}

}
