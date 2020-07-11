package org.jeecg.common.util;

import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    public static String getMinioUrl() {
        return minioUrl;
    }

    public static String getBucketName() {
        return bucketName;
    }

    private static MinioClient minioClient = null;

    /**
     * 上传文件
     * @param file
     * @return
     */
    public static String upload(MultipartFile file, String bizPath, String customBucket) {
        String file_url = "";
        String newBucket = bucketName;
        if(oConvertUtils.isNotEmpty(customBucket)){
            newBucket = customBucket;
        }
        try {
            initMinio(minioUrl, minioName,minioPass);
            // 检查存储桶是否已经存在
            if(minioClient.bucketExists(newBucket)) {
                log.info("Bucket already exists.");
            } else {
                // 创建一个名为ota的存储桶
                minioClient.makeBucket(newBucket);
                log.info("create a new bucket.");
            }
            InputStream stream = file.getInputStream();
            // 获取文件名
            String orgName = file.getOriginalFilename();
            if("".equals(orgName)){
                orgName=file.getName();
            }
            orgName = CommonUtils.getFileName(orgName);
            String objectName = bizPath+"/"+orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));

            // 使用putObject上传一个本地文件到存储桶中。
            minioClient.putObject(newBucket,objectName, stream,stream.available(),"application/octet-stream");
            stream.close();
            file_url = minioUrl+newBucket+"/"+objectName;
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
     * 文件上传
     * @param file
     * @param bizPath
     * @return
     */
    public static String upload(MultipartFile file, String bizPath) {
        return  upload(file,bizPath,null);
    }

    /**
     * 获取文件流
     * @param bucketName
     * @param objectName
     * @return
     */
    public static InputStream getMinioFile(String bucketName,String objectName){
        InputStream inputStream = null;
        try {
            initMinio(minioUrl, minioName, minioPass);
            inputStream = minioClient.getObject(bucketName, objectName);
        } catch (Exception e) {
            log.info("文件获取失败" + e.getMessage());
        }
        return inputStream;
    }

    /**
     * 删除文件
     * @param bucketName
     * @param objectName
     * @throws Exception
     */
    public static void removeObject(String bucketName, String objectName) {
        try {
            initMinio(minioUrl, minioName,minioPass);
            minioClient.removeObject(bucketName, objectName);
        }catch (Exception e){
            log.info("文件删除失败" + e.getMessage());
        }
    }

    /**
     * 获取文件外链
     * @param bucketName
     * @param objectName
     * @param expires
     * @return
     */
    public static String getObjectURL(String bucketName, String objectName, Integer expires) {
        initMinio(minioUrl, minioName,minioPass);
        try{
            String url = minioClient.presignedGetObject(bucketName, objectName, expires);
            return URLDecoder.decode(url,"UTF-8");
        }catch (Exception e){
            log.info("文件路径获取失败" + e.getMessage());
        }
        return null;
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

    /**
     * 上传文件到minio
     * @param stream
     * @param relativePath
     * @return
     */
    public static String upload(InputStream stream,String relativePath) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidArgumentException {
        initMinio(minioUrl, minioName,minioPass);
        if(minioClient.bucketExists(bucketName)) {
            log.info("Bucket already exists.");
        } else {
            // 创建一个名为ota的存储桶
            minioClient.makeBucket(bucketName);
            log.info("create a new bucket.");
        }
        minioClient.putObject(bucketName,relativePath, stream, stream.available(),"application/octet-stream");
        stream.close();
        return minioUrl+bucketName+"/"+relativePath;
    }

}
