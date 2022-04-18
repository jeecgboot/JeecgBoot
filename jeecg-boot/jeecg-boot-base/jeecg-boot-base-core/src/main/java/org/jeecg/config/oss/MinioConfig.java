package org.jeecg.config.oss;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.util.MinioUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio文件上传配置文件
 * @author: jeecg-boot
 */
@Slf4j
@Configuration
public class MinioConfig {
    @Value(value = "${jeecg.minio.minio_url}")
    private String minioUrl;
    @Value(value = "${jeecg.minio.minio_name}")
    private String minioName;
    @Value(value = "${jeecg.minio.minio_pass}")
    private String minioPass;
    @Value(value = "${jeecg.minio.bucketName}")
    private String bucketName;

    @Bean
    public void initMinio(){
        if(!minioUrl.startsWith(CommonConstant.STR_HTTP)){
            minioUrl = "http://" + minioUrl;
        }
        if(!minioUrl.endsWith(SymbolConstant.SINGLE_SLASH)){
            minioUrl = minioUrl.concat(SymbolConstant.SINGLE_SLASH);
        }
        MinioUtil.setMinioUrl(minioUrl);
        MinioUtil.setMinioName(minioName);
        MinioUtil.setMinioPass(minioPass);
        MinioUtil.setBucketName(bucketName);
    }

}
