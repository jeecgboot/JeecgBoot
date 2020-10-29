package com.crj.java.task.front.config.oss;

import com.crj.java.task.front.common.util.oss.OssBootUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 云存储 配置
 */
@Configuration
public class OssConfiguration {

    @Value("${task.oss.endpoint}")
    private String endpoint;
    @Value("${task.oss.accessKey}")
    private String accessKeyId;
    @Value("${task.oss.secretKey}")
    private String accessKeySecret;
    @Value("${task.oss.bucketName}")
    private String bucketName;
    @Value("${task.oss.staticDomain}")
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
