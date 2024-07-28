package com.shop.common.core.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.shop.common.core.Constants;
import com.shop.common.core.web.JsonResult;
import com.shop.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件上传下载工具类
 * 2018-12-14 08:38
 */
@Slf4j
public class FileUploadUtil {
    // 文件上传的目录
    public static final String UPLOAD_FILE_DIR = Constants.UPLOAD_DIR + "file/";
    // 缩略图存放的目录
    private static final String UPLOAD_SM_DIR = Constants.UPLOAD_DIR + "thumbnail/";

    /**
     * 上传文件
     *
     * @param file MultipartFile
     * @return 示例：{"code": 0, "msg": "", "url": "", "fileName": ""}
     */
    public static JsonResult upload(MultipartFile file, HttpServletRequest request) {
        String path;  // 文件保存路径
        // 文件原始名称
        String orgName = file.getOriginalFilename(), dir = getDateDir();

        if (orgName == null) return JsonResult.error("上传失败");
        File outFile;
        String suffix = orgName.substring(orgName.lastIndexOf(".") + 1);  // 获取文件后缀

        if (Constants.UPLOAD_MD5_NAME) { // 使用md5命名方式解决图片重复上传问题
            try {
                String md5 = SecureUtil.md5(file.getInputStream());
                path = dir + md5 + "." + suffix;
                outFile = new File(UPLOAD_FILE_DIR, path);
            } catch (Exception e) {
                e.printStackTrace();
                return JsonResult.error("上传失败").put("error", e.getMessage());
            }
        } else if (Constants.UPLOAD_UUID_NAME) {  // uuid命名
            path = dir + UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
            outFile = new File(UPLOAD_FILE_DIR, path);
        } else {  // 使用原名称，存在相同着加(1)
            path = dir + orgName;
            outFile = new File(UPLOAD_FILE_DIR, path);
            int sameSize = 1;
            String prefix = orgName.substring(0, orgName.lastIndexOf("."));  // 获取文件名称
            while (outFile.exists()) {
                path = dir + prefix + "(" + sameSize + ")." + suffix;
                outFile = new File(UPLOAD_FILE_DIR, path);
                sameSize++;
            }
        }
        try {
            if (!outFile.getParentFile().exists()) {
                if (!outFile.getParentFile().mkdirs()) return JsonResult.error("上传失败");
            }
            file.transferTo(outFile);
            JsonResult jsonResult = JsonResult.ok("上传成功").put("url", path).put("fileName", orgName)
                    .put("dir", "/" + StrUtil.removeSuffix(dir, "/"));
            if (request != null) {
                jsonResult.put("location", "/file/" + path);
            }
            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("上传失败").put("error", e.getMessage());
        }
    }

    /**
     * 上传文件base64格式
     *
     * @param base64 base64编码字符
     * @return 示例：{"code": 0, "msg": "", "url": ""}
     */
    public static JsonResult upload(String base64, HttpServletRequest request) {
        if (base64 == null || base64.trim().isEmpty()) return JsonResult.error("上传失败");
        String suffix = base64.substring(11, base64.indexOf(";"));  // 获取文件格式
        String dir = getDateDir();
        String path = dir + UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
        File outFile = new File(UPLOAD_FILE_DIR, path);
        if (!outFile.getParentFile().exists()) {
            if (!outFile.getParentFile().mkdirs()) return JsonResult.error("上传失败");
        }
        try {
            byte[] bytes = Base64.getDecoder().decode(base64.substring(base64.indexOf(";") + 8).getBytes());
            IoUtil.write(new FileOutputStream(outFile), true, bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("上传失败").put("error", e.getMessage());
        }
        JsonResult jsonResult = JsonResult.ok("上传成功").put("url", path)
                .put("dir", "/" + StrUtil.removeSuffix(dir, "/"));
        if (request != null) {
            jsonResult.put("location", StrUtil.removeSuffix(
                    StrUtil.removeSuffix(request.getRequestURL(), "upload/base64")
                    , "upload") + path);
        }
        return jsonResult;
    }

    /**
     * 按照日期分存放目录
     */
    public static String getDateDir() {
        String dir = "";
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            dir = user.getUsername() + "/";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd/");
        return dir  + sdf.format(new Date());
    }

    /**
     * 预览文件
     */
    public static void preview(File file, HttpServletResponse response) {
        if (file == null || !file.exists()) {
            outNotFund(response);
            return;
        }
        // 支持word、excel预览
        if (OpenOfficeUtil.canConverter(file.getName())) {
            File pdfFile = OpenOfficeUtil.converterToPDF(file.getAbsolutePath());
            if (pdfFile != null) file = pdfFile;
        }
        String contentType = getFileType(file);  // 获取文件类型
        if (contentType != null) {
            response.setContentType(contentType);
        } else {
            setDownloadHeader(response, file.getName());
        }
        try {
            output(file, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预览源文件
     */
    public static void preview(String path, HttpServletResponse response) {
        preview(new File(UPLOAD_FILE_DIR, path), response);
    }

    /**
     * 下载源文件
     */
    public static void download(String path, HttpServletResponse response) {
        File file = new File(UPLOAD_FILE_DIR, path);
        if (!file.exists()) {
            outNotFund(response);
            return;
        }
        setDownloadHeader(response, file.getName());
        try {
            output(file, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预览缩略图
     */
    public static void thumbnail(String path, HttpServletResponse response) {
        // 如果是图片并且缩略图不存在则生成
        File file = new File(UPLOAD_FILE_DIR, path);
        File smFile = new File(UPLOAD_SM_DIR, path);
        if (!smFile.exists() && isImgFile(file)) {
            // 大于100kb生成100kb的缩略图
            long fileSize = file.length();
            if ((fileSize / 1024) > 100) {
                try {
                    if (smFile.getParentFile().mkdirs()) {
                        ImgUtil.scale(file, smFile, 100f / (fileSize / 1024f));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                preview(file, response);
                return;
            }
        }
        preview(smFile, response);
    }

    /**
     * 输出文件流
     */
    public static void output(File file, OutputStream os) {
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] bytes = new byte[1024 * 256];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     */
    public static boolean delete(String path) {
        File file = new File(UPLOAD_FILE_DIR, path);
        if (file.delete()) new File(UPLOAD_SM_DIR, path).delete();
        return true;
    }

    /**
     * 获取文件列表
     */
    public static List<Map<String, Object>> list(String dir) {
        dir = dir == null ? "" : dir;
        String path = UPLOAD_FILE_DIR;
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user != null) {
            if (dir.length()>0 && !dir.startsWith("/")) {
                dir = "/" + dir;
            }
            dir = user.getUsername() + dir;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        File file = new File(path + dir);
        File[] files = file.listFiles();
        if (files == null) return list;
        for (File f : files) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", f.getName());
            map.put("size", f.length());
            map.put("isDir", f.isDirectory());
            if (!f.isDirectory()) {
                map.put("url", "/" + dir + "/" + f.getName());
                map.put("smUrl", "thumbnail"  + "/" + dir + "/" + f.getName());
            }
            map.put("updateTime", f.lastModified());
            list.add(map);
        }
        return list;
    }

    /**
     * 获取文件类型
     */
    public static String getFileType(File file) {
        String contentType = null;
        try {
            contentType = new Tika().detect(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentType;
    }

    /**
     * 判断是否是图片类型
     */
    public static boolean isImgFile(File file) {
        String contentType = getFileType(file);
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * 设置下载文件的header
     */
    public static void setDownloadHeader(HttpServletResponse response, String fileName) {
        response.setContentType("application/force-download");
        try {
            fileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
    }

    /**
     * 输出文件不存在
     */
    public static void outNotFund(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        outMessage("404 Not Found", response);
    }

    /**
     * 输出错误信息
     */
    public static void outMessage(String message, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write("<!doctype html>");
            writer.write("<title>" + message + "</title>");
            writer.write("<h1 style=\"text-align: center\">" + message + "</h1>");
            writer.write("<hr/><p style=\"text-align: center\">Zlianpay File Server</p>");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
