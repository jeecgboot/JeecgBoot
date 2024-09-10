package org.jeecg.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jeecg.common.exception.JeecgBootException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @program: file
 * @description: 文件下载
 * @author: chenrui
 * @date: 2019-05-24 16:34
 **/
@Slf4j
public class FileDownloadUtils {

    /**
     * 单文件下载
     *
     * @param response
     * @param storePath 下载文件储存地址
     * @param fileName  文件名称
     * @author: chenrui
     * @date: 2019/5/24 17:10
     */
    public static void downloadFile(HttpServletResponse response, String storePath, String fileName) {
        response.setCharacterEncoding("UTF-8");
        File file = new File(storePath);
        if (!file.exists()) {
            throw new NullPointerException("Specified file not found");
        }
        if (fileName == null || fileName.isEmpty()) {
            throw new NullPointerException("The file name can not null");
        }
        // 配置文件下载
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        // 实现文件下载
        byte[] buffer = new byte[1024];
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);) {
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 多文件下载
     *
     * @param filesPath   下载文件集合
     * @param zipFileName 多文件合称名
     * @author: chenrui
     * @date: 2019/5/24 17:48
     */
    public static void downloadFileMulti(HttpServletResponse response, List<String> filesPath, String zipFileName) throws IOException {
        //设置压缩包的名字
        String downloadName = zipFileName + ".zip";
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadName, "UTF-8"));
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        log.info("开始压缩文件:" + filesPath);
        //设置压缩流：直接写入response，实现边压缩边下载
        try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
             DataOutputStream os = new DataOutputStream(zipOut);) {
            //设置压缩方法
            zipOut.setMethod(ZipOutputStream.DEFLATED);
            for (String filePath : filesPath) {
                //循环将文件写入压缩流
                File file = new File(filePath);
                if (file.exists()) {
                    //添加ZipEntry，并ZipEntry中写入文件流也就是将文件压入zip文件的目录下
                    String fileName = file.getName();
                    zipOut.putNextEntry(new ZipEntry(fileName));
                    //格式输出流文件

                    InputStream is = Files.newInputStream(file.toPath());
                    byte[] b = new byte[1024];
                    int length;
                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                    is.close();
                    zipOut.closeEntry();
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new JeecgBootException(e);
        }
    }

    /**
     * 下载网络资源到磁盘
     *
     * @param fileUrl
     * @param storePath
     * @author chenrui
     * @date 2024/1/19 10:09
     */
    public static String download2DiskFromNet(String fileUrl, String storePath) {
        try {
            URL url = new URL(fileUrl);
            URLConnection conn = url.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            // 防止屏蔽程序
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            // 确保目录存在
            File file = ensureDestFileDir(storePath);
            try (InputStream inStream = conn.getInputStream();
                 FileOutputStream fs = new FileOutputStream(file);) {
                int byteread;
                byte[] buffer = new byte[1204];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                return storePath;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new JeecgBootException(e);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new JeecgBootException(e);
        }
    }


    /**
     * 获取不重名的文件
     *
     * @param file
     * @return
     * @author chenrui
     * @date 2017年5月24日下午6:29:13
     * @version v0.0.1
     */
    public static File getUniqueFile(final File file) {
        if (!file.exists()) {
            return file;
        }

        File tmpFile = new File(file.getAbsolutePath());
        File parentDir = tmpFile.getParentFile();
        int count = 1;
        String extension = FilenameUtils.getExtension(tmpFile.getName());
        String baseName = FilenameUtils.getBaseName(tmpFile.getName());
        do {
            tmpFile = new File(parentDir, baseName + "(" + count++ + ")." + extension);
        } while (tmpFile.exists());
        return tmpFile;
    }

    /**
     * 确保输出文件目录
     *
     * @param destFilePath
     * @return
     * @author: chenrui
     * @date: 2019-05-21 16:49
     */
    private static File ensureDestFileDir(String destFilePath) {
        File destFile = new File(destFilePath);
        FileDownloadUtils.checkDirAndCreate(destFile.getParentFile());
        return destFile;
    }

    /**
     * 验证文件夹存在且创建目录
     *
     * @param dir
     * @author chenrui
     * @date 2017年5月24日下午6:29:24
     * @version v0.0.1
     */
    public static void checkDirAndCreate(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
