package org.jeecg.config.oss;

import org.jeecg.common.util.oss.OssBootUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 云存储 配置
 */
@Configuration
public class OssConfiguration {

    @Value("${jeecg.oss.endpoint}")
    private String endpoint;
    @Value("${jeecg.oss.accessKey}")
    private String accessKeyId;
    @Value("${jeecg.oss.secretKey}")
    private String accessKeySecret;
    @Value("${jeecg.oss.bucketName}")
    private String bucketName;
    @Value("${jeecg.oss.staticDomain}")
    private String staticDomain;


    @Bean
    public void initOssBootConfiguration() {
        OssBootUtil.setEndPoint(endpoint);
        OssBootUtil.setAccessKeyId(accessKeyId);
        OssBootUtil.setAccessKeySecret(accessKeySecret);
        OssBootUtil.setBucketName(bucketName);
        OssBootUtil.setStaticDomain(staticDomain);
    }
}