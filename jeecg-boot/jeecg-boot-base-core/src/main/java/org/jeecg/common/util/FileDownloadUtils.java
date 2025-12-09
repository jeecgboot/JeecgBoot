package org.jeecg.common.util;

import cn.hutool.core.io.IoUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.filter.SsrfFileTypeFilter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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


    /**
     * 下载单个文件到ZIP流
     * 核心功能：获取文件流，写入ZIP条目
     * @param fileUrl 文件URL（可以是HTTP URL或本地路径）
     * @param fileName ZIP内的文件名
     * @param zous ZIP输出流
     */
    public static void downLoadSingleFile(String fileUrl, String fileName, String uploadUrl,ZipArchiveOutputStream zous) {
        InputStream inputStream = null;
        try {
            // 创建ZIP条目：每个文件在ZIP中都是一个独立条目
            ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
            zous.putArchiveEntry(entry);

            // 获取文件输入流：区分普通文件和快捷方式
            if (fileUrl.endsWith(".url")) {
                // 处理快捷方式：生成.url文件内容
                inputStream = FileDownloadUtils.createInternetShortcut(fileName, fileUrl, "");
            } else {
                // 普通文件下载：从URL或本地路径获取流
                inputStream = getDownInputStream(fileUrl,uploadUrl);
            }

            if (inputStream != null) {
                // 将文件流写入ZIP
                IOUtils.copy(inputStream, zous);
            }
            // 关闭当前ZIP条目
            zous.closeArchiveEntry();
        } catch (IOException e) {
            log.error("文件下载失败: {}",  e);
        } finally {
            // 确保输入流关闭
            IoUtil.close(inputStream);
        }
    }

    /**
     * 获取下载文件输入流
     * 功能：根据URL类型（HTTP或本地）获取文件流
     * @param fileUrl 文件URL（支持HTTP和本地路径）
     * @return 文件输入流，失败返回null
     */
    public static InputStream getDownInputStream(String fileUrl, String uploadUrl) {
        try {
            // 处理HTTP URL：通过网络下载
            if (oConvertUtils.isNotEmpty(fileUrl) && fileUrl.startsWith(CommonConstant.STR_HTTP)) {
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000); // 连接超时5秒
                connection.setReadTimeout(30000);  // 读取超时30秒
                return connection.getInputStream();
            } else {
                // 处理本地文件：直接读取文件系统
                String downloadFilePath = uploadUrl + File.separator + fileUrl;
                // 安全检查：防止下载危险文件类型
                SsrfFileTypeFilter.checkDownloadFileType(downloadFilePath);
                return new BufferedInputStream(new FileInputStream(downloadFilePath));
            }
        } catch (IOException e) {
            // 异常时返回null，上层会处理空流情况
            return null;
        }
    }

    /**
     * 获取文件扩展名
     * 功能：从文件名中提取扩展名
     * @param fileName 文件名
     * @return 文件扩展名（不含点），如"txt"、"png"
     */
    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 创建快捷方式（.url文件内容）
     * 功能：生成Internet快捷方式文件内容
     * @param name 快捷方式名称
     * @param url 目标URL地址
     * @param icon 图标路径（可选）
     * @return 包含.url文件内容的输入流
     */
    public static InputStream createInternetShortcut(String name, String url, String icon) {
        StringWriter sw = new StringWriter();
        try {
            // 按照Windows快捷方式格式写入内容
            sw.write("[InternetShortcut]\n");
            sw.write("URL=" + url + "\n");
            if (oConvertUtils.isNotEmpty(icon)) {
                sw.write("IconFile=" + icon + "\n");
            }
            // 将字符串内容转换为输入流
            return new ByteArrayInputStream(sw.toString().getBytes(StandardCharsets.UTF_8));
        } finally {
            IoUtil.close(sw);
        }
    }
    /**
     * 从URL中提取文件名
     * 功能：从HTTP URL或本地路径中提取纯文件名
     * @param fileUrl 文件URL
     * @return 文件名（不含路径）
     */
    public static String getFileNameFromUrl(String fileUrl) {
        try {
            // 处理HTTP URL：从路径部分提取文件名
            if (fileUrl.startsWith(CommonConstant.STR_HTTP)) {
                URL url = new URL(fileUrl);
                String path = url.getPath();
                return path.substring(path.lastIndexOf('/') + 1);
            }

            // 处理本地文件路径：从文件路径提取文件名
            return fileUrl.substring(fileUrl.lastIndexOf(File.separator) + 1);
        } catch (Exception e) {
            // 如果解析失败，使用时间戳作为文件名
            return "file_" + System.currentTimeMillis();
        }
    }
    /**
     * 生成ZIP中的文件名
     * 功能：避免文件名冲突，为多个文件添加序号
     * @param fileUrl 文件URL（用于提取原始文件名）
     * @param index 文件序号（从0开始）
     * @param total 文件总数
     * @return 处理后的文件名（带序号）
     */
    public static String generateFileName(String fileUrl, int index, int total) {
        // 从URL中提取原始文件名
        String originalFileName = getFileNameFromUrl(fileUrl);

        // 如果只有一个文件，直接使用原始文件名
        if (total == 1) {
            return originalFileName;
        }

        // 多个文件时，使用序号+原始文件名
        String extension = getFileExtension(originalFileName);
        String nameWithoutExtension = originalFileName.replace("." + extension, "");

        return String.format("%s_%d.%s", nameWithoutExtension, index + 1, extension);
    }
}
