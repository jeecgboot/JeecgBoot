package org.jeecg.modules.system.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.jeecg.common.util.MyCommonsMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Description: http文件转MultipartFile
 * @author: wangshuai
 * @date: 2025/11/5 17:55
 */
public class HttpFileToMultipartFileUtil {

    /**
     * 获取
     *
     * @param fileUrl
     * @param filename
     * @return
     * @throws Exception
     */
    public static MultipartFile httpFileToMultipartFile(String fileUrl, String filename) throws Exception {
        byte[] bytes = downloadImageData(fileUrl);
        return convertByteToMultipartFile(bytes, filename);
    }

    /**
     * 下载图片数据
     */
    private static byte[] downloadImageData(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);
        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        connection.setRequestProperty("Accept", "image/*");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP请求失败，响应码: " + responseCode);
        }

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    /**
     * byte转 MultipartFile
     *
     * @param data
     * @param fileName
     * @return
     */
    private static MultipartFile convertByteToMultipartFile(byte[] data, String fileName) {
        FileItemFactory factory = new DiskFileItemFactory();
        FileItem item = factory.createItem(fileName, "application/octet-stream", true, fileName);

        try (OutputStream os = item.getOutputStream();
             ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException("字节数组转换失败", e);
        }

        try {
            return new MyCommonsMultipartFile(item);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}