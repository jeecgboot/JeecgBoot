package org.jeecg.modules.demo.test.controller;

import io.minio.MinioClient;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @Author scott
 * @Date 2020/1/12 17:19
 * @Description: Minio文件服务测试
 */
@RestController
@RequestMapping("/test/minio")
public class MinioController {

    //minio服务的IP端口
    private static String url = "http://111.225.222.176:9000";
    private static String accessKey = "admin";
    private static String secretKey = "jeecg1357";
    private static String bucketName = "jeecgtest";

    /**
     * 上传文件到minio服务
     *
     * @param file
     * @return
     */
    @PostMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            InputStream is = file.getInputStream(); //得到文件流
            String fileName = "/upload/img/" + file.getOriginalFilename(); //文件名
            String contentType = file.getContentType();  //类型
            minioClient.putObject(bucketName, fileName, is, contentType); //把文件放置Minio桶(文件夹)
            return "上传成功";
        } catch (Exception e) {
            return "上传失败";
        }
    }

    /**
     * 下载minio服务的文件
     *
     * @param response
     * @return
     */
    @GetMapping("download")
    public String download(HttpServletResponse response) {
        try {
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            InputStream fileInputStream = minioClient.getObject(bucketName, "11.jpg");
            response.setHeader("Content-Disposition", "attachment;filename=" + "11.jpg");
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(fileInputStream, response.getOutputStream());
            return "下载完成";
        } catch (Exception e) {
            e.printStackTrace();
            return "下载失败";
        }
    }

    /**
     * 获取minio文件的下载地址
     *
     * @return
     */
    @GetMapping("url")
    public String getUrl() {
        try {
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            String url = minioClient.presignedGetObject(bucketName, "11.jpg");
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }
    }
}
