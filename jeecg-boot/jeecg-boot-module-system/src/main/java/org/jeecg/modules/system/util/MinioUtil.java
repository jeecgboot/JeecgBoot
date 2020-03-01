package org.jeecg.modules.system.util;

import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * minio文件上传工具类
 */
@Slf4j
public class MinioUtil {
    private static String minioUrl;
    private static String minioName;
    private static String minioPass;
    private static String bucketName;

    public static void setMinioUrl(String minioUrl) {
        MinioUtil.minioUrl = minioUrl;
    }

    public static void setMinioName(String minioName) {
        MinioUtil.minioName = minioName;
    }

    public static void setMinioPass(String minioPass) {
        MinioUtil.minioPass = minioPass;
    }

    public static void setBucketName(String bucketName) {
        MinioUtil.bucketName = bucketName;
    }

    private static MinioClient minioClient = null;

    /**
     * 上传文件
     * @param file
     * @return
     */
    public static String upload(MultipartFile file,String bizPath) {
        String file_url = "";
        try {
            initMinio(minioUrl, minioName,minioPass);
            // 检查存储桶是否已经存在
            if(minioClient.bucketExists(bucketName)) {
                log.info("Bucket already exists.");
            } else {
                // 创建一个名为ota的存储桶
                minioClient.makeBucket(bucketName);
                log.info("create a new bucket.");
            }
            InputStream stream = file.getInputStream();
            String orgName = file.getOriginalFilename();// 获取文件名
            String objectName = bizPath+"/"+orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));

            // 使用putObject上传一个本地文件到存储桶中。
            minioClient.putObject(bucketName,objectName, stream,stream.available(),"application/octet-stream");
            stream.close();
            file_url = minioUrl+bucketName+"/"+objectName;
        }catch (IOException e){
            log.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (NoResponseException e) {
            log.error(e.getMessage(), e);
        } catch (XmlPullParserException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidArgumentException e) {
            log.error(e.getMessage(), e);
        } catch (RegionConflictException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidBucketNameException e) {
            log.error(e.getMessage(), e);
        } catch (ErrorResponseException e) {
            log.error(e.getMessage(), e);
        } catch (InternalException e) {
            log.error(e.getMessage(), e);
        } catch (InsufficientDataException e) {
            log.error(e.getMessage(), e);
        }
        return file_url;
    }

    /**
     * 初始化客户端
     * @param minioUrl
     * @param minioName
     * @param minioPass
     * @return
     */
    private static MinioClient initMinio(String minioUrl, String minioName,String minioPass) {
        if (minioClient == null) {
            try {
                minioClient = new MinioClient(minioUrl, minioName,minioPass);
            } catch (InvalidEndpointException e) {
                e.printStackTrace();
            } catch (InvalidPortException e) {
                e.printStackTrace();
            }
        }
        return minioClient;
    }
}
