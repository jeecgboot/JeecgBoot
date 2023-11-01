package org.jeecg.modules.im.base.util.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;

@Slf4j
@Data
public class AliyunOss {
    private String endPoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String cdnDomain;

    public AliyunOss(String endPoint, String accessKeyId, String accessKeySecret, String bucketName, String cdnDomain) {
        this.endPoint = endPoint;
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.bucketName = bucketName;
        this.cdnDomain = cdnDomain;
    }

    //上传本地图片
    public String uploadLocalFile(String key, File file) {
        OSS instance = new OSSClientBuilder().build(this.getEndPoint(), this.getAccessKeyId(), this.getAccessKeySecret());
        if (!instance.doesBucketExist(this.getBucketName())) {
            instance.createBucket(this.getBucketName());
        }
        try {
            instance.putObject(this.getBucketName(), key, file);
            log.info("上传本地文件" + key + "到OSS成功。");
            return cdnDomain+"/"+key;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传本地文件到oss异常：{0}",e);
            return null;
        } finally {
            instance.shutdown();
        }
    }
    public String uploadLocalFile(String key, InputStream inputStream) {
        OSS instance = new OSSClientBuilder().build(this.getEndPoint(), this.getAccessKeyId(), this.getAccessKeySecret());
        if (!instance.doesBucketExist(this.getBucketName())) {
            instance.createBucket(this.getBucketName());
        }
        try {
            instance.putObject(this.getBucketName(), key, inputStream);
            log.info("上传本地文件" + key + "到OSS成功。");
            return cdnDomain+"/"+key;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传本地文件到oss异常：{0}",e);
            return null;
        } finally {
            instance.shutdown();
        }
    }
    //上传远程图片
    public String uploadRemoteFile(String key, InputStream inputStream) {
        OSS instance = new OSSClientBuilder().build(this.getEndPoint(), this.getAccessKeyId(), this.getAccessKeySecret());
        if (!instance.doesBucketExist(this.getBucketName())) {
            instance.createBucket(this.getBucketName());
        }
        try {
            instance.putObject(this.getBucketName(), key, inputStream);
            log.info("上传远程文件" + key + "存入OSS成功。");
            return cdnDomain+"/"+key;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传远程文件到oss异常：{0}",e);
            return null;
        } finally {
            instance.shutdown();
        }
    }
}
