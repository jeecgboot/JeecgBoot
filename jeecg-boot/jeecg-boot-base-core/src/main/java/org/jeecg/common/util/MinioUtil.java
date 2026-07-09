package org.jeecg.common.util;

import io.minio.*;
import io.minio.Http.Method;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.util.filter.SsrfFileTypeFilter;
import org.jeecg.common.util.filter.StrAttackFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLDecoder;

/**
 * minio文件上传工具类
 * @author: jeecg-boot
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
    public static String upload(MultipartFile file, String bizPath, String customBucket) throws Exception {
        String fileUrl = "";
        // 业务路径过滤，防止攻击
        bizPath = StrAttackFilter.filter(bizPath);

        // 文件安全校验，防止上传漏洞文件
        SsrfFileTypeFilter.checkUploadFileType(file, bizPath);

        String newBucket = bucketName;
        if(oConvertUtils.isNotEmpty(customBucket)){
            newBucket = customBucket;
        }
        try {
            initMinio(minioUrl, minioName,minioPass);
            // 检查存储桶是否已经存在
            if(minioClient.bucketExists(BucketExistsArgs.builder().bucket(newBucket).build())) {
                log.info("Bucket already exists.");
            } else {
                // 创建一个名为ota的存储桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(newBucket).build());
                log.info("create a new bucket.");
            }
            InputStream stream = file.getInputStream();
            // 获取文件名
            String orgName = file.getOriginalFilename();
            if("".equals(orgName)){
                orgName=file.getName();
            }
            orgName = CommonUtils.getFileName(orgName);
            String objectName = bizPath+"/"
                                +( orgName.indexOf(".")==-1
                                   ?orgName + "_" + System.currentTimeMillis()
                                   :orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.lastIndexOf("."))
                                 );

            // 使用putObject上传一个本地文件到存储桶中。
            if(objectName.startsWith(SymbolConstant.SINGLE_SLASH)){
                objectName = objectName.substring(1);
            }
            PutObjectArgs objectArgs = PutObjectArgs.builder().object(objectName)
                    .bucket(newBucket)
                    .contentType("application/octet-stream")
                    .stream(stream, (long) stream.available(), -1L).build();
            minioClient.putObject(objectArgs);
            stream.close();
            fileUrl = minioUrl+newBucket+"/"+objectName;
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return fileUrl;
    }

    /**
     * 文件上传
     * @param file
     * @param bizPath
     * @return
     */
    public static String upload(MultipartFile file, String bizPath) throws Exception {
        return upload(file,bizPath,null);
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
            GetObjectArgs objectArgs = GetObjectArgs.builder().object(objectName)
                    .bucket(bucketName).build();
            inputStream = minioClient.getObject(objectArgs);
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
            RemoveObjectArgs objectArgs = RemoveObjectArgs.builder().object(objectName)
                    .bucket(bucketName).build();
            minioClient.removeObject(objectArgs);
        }catch (Exception e){
            log.info("文件删除失败" + e.getMessage());
        }
    }

    /**
     * 获取私有桶文件的预签名访问URL（带过期时间）
     * 通过MinIO预签名机制生成临时GET链接，无需公开桶即可让外部访问文件
     *
     * @param bucketName 桶名称
     * @param objectName 文件对象路径（如 "eoafile/2026/04/test.pdf"）
     * @param expires    链接有效期，单位：秒（注意不是天）
     * @return 预签名URL，失败返回null
     */
    public static String getObjectUrl(String bucketName, String objectName, Integer expires) {
        initMinio(minioUrl, minioName,minioPass);
        try{
            // 代码逻辑说明: 获取文件外链报错提示method不能为空，导致文件下载和预览失败----
            GetPresignedObjectUrlArgs objectArgs = GetPresignedObjectUrlArgs.builder().object(objectName)
                    .bucket(bucketName)
                    .expiry(expires).method(Method.GET).build();
            String url = minioClient.getPresignedObjectUrl(objectArgs);
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
                minioClient = MinioClient.builder()
                        .endpoint(minioUrl)
                        .credentials(minioName, minioPass)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return minioClient;
    }

    /**
     * 通过输入流上传文件到MinIO默认桶
     * 若桶不存在会自动创建，上传成功后关闭输入流
     *
     * @param stream       文件输入流
     * @param relativePath 文件在桶中的相对路径（如 "upload/2026/04/test.pdf"）
     * @return 文件完整访问URL（格式：minioUrl + bucketName + "/" + relativePath）
     * @throws Exception 桶操作或上传过程中的异常
     */
    public static String upload(InputStream stream,String relativePath) throws Exception {
        initMinio(minioUrl, minioName,minioPass);
        if(minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            log.info("Bucket already exists.");
        } else {
            // 创建一个名为ota的存储桶
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("create a new bucket.");
        }
        PutObjectArgs objectArgs = PutObjectArgs.builder().object(relativePath)
                .bucket(bucketName)
                .contentType("application/octet-stream")
                .stream(stream, (long) stream.available(), -1L).build();
        minioClient.putObject(objectArgs);
        stream.close();
        return minioUrl+bucketName+"/"+relativePath;
    }

}
