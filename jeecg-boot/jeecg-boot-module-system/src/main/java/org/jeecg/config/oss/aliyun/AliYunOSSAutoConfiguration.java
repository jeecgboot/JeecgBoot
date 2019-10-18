package org.jeecg.config.oss.aliyun;

import java.util.Map;
import java.util.Properties;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import org.jeecg.config.oss.OSSCondition;
import org.jeecg.config.oss.OSSManager;
import org.jeecg.config.oss.OSSProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * AliYun OSS configuration.
 */
@Configuration
@ConditionalOnClass({ OSS.class })
@ConditionalOnProperty(prefix = "jeecg.oss", name = "type",havingValue = "aliyun")
@Conditional(OSSCondition.class)
public class AliYunOSSAutoConfiguration {

	private final OSSProperties properties;

	public AliYunOSSAutoConfiguration(OSSProperties ossProperties) {
		this.properties = ossProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public ClientBuilderConfiguration clientConfiguration(OSSProperties ossProperties) {
		Properties properties = asProperties(ossProperties.getProperties());
		ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
		configuration.setMaxConnections(Integer.parseInt(properties.getProperty("aliyun.maxConnections", "5")));
		configuration.setSocketTimeout(Integer.parseInt(properties.getProperty("aliyun.socketTimeout", "50000")));
		configuration
				.setConnectionTimeout(Integer.parseInt(properties.getProperty("aliyun.connectionTimeout", "50000")));
		configuration.setConnectionRequestTimeout(
				Integer.parseInt(properties.getProperty("aliyun.connectionRequestTimeout", "-1")));
		configuration
				.setIdleConnectionTime(Integer.parseInt(properties.getProperty("aliyun.idleConnectionTime", "60000")));
		configuration.setMaxErrorRetry(Integer.parseInt(properties.getProperty("aliyun.maxErrorRetry", "3")));
		configuration.setSupportCname(Boolean.parseBoolean(properties.getProperty("aliyun.supportCname", "false")));
		configuration.setSLDEnabled(Boolean.parseBoolean(properties.getProperty("aliyun.sldEnabled", "false")));
		configuration.setProtocol(Protocol.HTTP);
		if (Protocol.HTTPS.toString().equals(properties.getProperty("aliyun.protocol"))) {
			configuration.setProtocol(Protocol.HTTPS);
		}
		if (properties.getProperty("aliyun.userAgent") != null) {
			configuration.setUserAgent(properties.getProperty("aliyun.userAgent"));
		}

		return configuration;
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public OSS ossClient(ClientBuilderConfiguration clientConfiguration) {
		return new OSSClientBuilder().build(this.properties.getEndpoint(), this.properties.getAccessKey(),
				this.properties.getSecretKey(), clientConfiguration);
	}

	@Bean
	@ConditionalOnMissingBean
	public OSSManager ossManager(OSS ossClient) {
		return new AliYunOSSManager(ossClient, this.properties);
	}

	private Properties asProperties(Map<String, String> source) {
		Properties properties = new Properties();
		properties.putAll(source);
		return properties;
	}

}
