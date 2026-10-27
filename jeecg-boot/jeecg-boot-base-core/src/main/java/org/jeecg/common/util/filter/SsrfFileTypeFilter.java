package org.jeecg.common.util.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @Description: 校验文件敏感后缀
 * @author: lsq
 * @date: 2023年09月12日 15:29
 */
@Slf4j
public class SsrfFileTypeFilter {

    /**
     * 允许操作文件类型白名单
     */
    private final static List<String> FILE_TYPE_WHITE_LIST = new ArrayList<>();
    /**初始化文件头类型，不够的自行补充*/
    final static HashMap<String, String> FILE_TYPE_MAP = new HashMap<>();
    static {
        //图片文件
        FILE_TYPE_WHITE_LIST.add("jpg");
        FILE_TYPE_WHITE_LIST.add("jpeg");
        FILE_TYPE_WHITE_LIST.add("png");
        FILE_TYPE_WHITE_LIST.add("gif");
        FILE_TYPE_WHITE_LIST.add("bmp");
        FILE_TYPE_WHITE_LIST.add("svg");
        FILE_TYPE_WHITE_LIST.add("ico");
        FILE_TYPE_WHITE_LIST.add("heic");

        //文本文件
        FILE_TYPE_WHITE_LIST.add("txt");
        FILE_TYPE_WHITE_LIST.add("doc");
        FILE_TYPE_WHITE_LIST.add("docx");
        FILE_TYPE_WHITE_LIST.add("pdf");
        FILE_TYPE_WHITE_LIST.add("csv");
//        FILE_TYPE_WHITE_LIST.add("xml");
        FILE_TYPE_WHITE_LIST.add("md");

        //音视频文件
        FILE_TYPE_WHITE_LIST.add("mp4");
        FILE_TYPE_WHITE_LIST.add("avi");
        FILE_TYPE_WHITE_LIST.add("mov");
        FILE_TYPE_WHITE_LIST.add("wmv");
        FILE_TYPE_WHITE_LIST.add("mp3");
        FILE_TYPE_WHITE_LIST.add("wav");

        //表格文件
        FILE_TYPE_WHITE_LIST.add("xls");
        FILE_TYPE_WHITE_LIST.add("xlsx");

        //压缩文件
        FILE_TYPE_WHITE_LIST.add("zip");
        FILE_TYPE_WHITE_LIST.add("rar");
        FILE_TYPE_WHITE_LIST.add("7z");
        FILE_TYPE_WHITE_LIST.add("tar");

        //app文件后缀
        FILE_TYPE_WHITE_LIST.add("apk");
        FILE_TYPE_WHITE_LIST.add("wgt");

        //幻灯片文件后缀
        FILE_TYPE_WHITE_LIST.add("ppt");
        FILE_TYPE_WHITE_LIST.add("pptx");

        //设置禁止文件的头部标记
        FILE_TYPE_MAP.put("3c25402070616765206c", "jsp");
        FILE_TYPE_MAP.put("3c3f7068700a0a2f2a2a0a202a205048", "php");
        FILE_TYPE_MAP.put("cafebabe0000002e0041", "class");
        FILE_TYPE_MAP.put("494e5345525420494e54", "sql");
       /* fileTypeMap.put("ffd8ffe000104a464946", "jpg");
        fileTypeMap.put("89504e470d0a1a0a0000", "png");
        fileTypeMap.put("47494638396126026f01", "gif");
        fileTypeMap.put("49492a00227105008037", "tif");
        fileTypeMap.put("424d228c010000000000", "bmp");
        fileTypeMap.put("424d8240090000000000", "bmp");
        fileTypeMap.put("424d8e1b030000000000", "bmp");
        fileTypeMap.put("41433130313500000000", "dwg");
        fileTypeMap.put("3c21444f435459504520", "html");
        fileTypeMap.put("3c21646f637479706520", "htm");
        fileTypeMap.put("48544d4c207b0d0a0942", "css");
        fileTypeMap.put("696b2e71623d696b2e71", "js");
        fileTypeMap.put("7b5c727466315c616e73", "rtf");
        fileTypeMap.put("38425053000100000000", "psd");
        fileTypeMap.put("46726f6d3a203d3f6762", "eml");
        fileTypeMap.put("d0cf11e0a1b11ae10000", "doc");
        fileTypeMap.put("5374616E64617264204A", "mdb");
        fileTypeMap.put("252150532D41646F6265", "ps");
        fileTypeMap.put("255044462d312e350d0a", "pdf");
        fileTypeMap.put("2e524d46000000120001", "rmvb");
        fileTypeMap.put("464c5601050000000900", "flv");
        fileTypeMap.put("00000020667479706d70", "mp4");
        fileTypeMap.put("49443303000000002176", "mp3");
        fileTypeMap.put("000001ba210001000180", "mpg");
        fileTypeMap.put("3026b2758e66cf11a6d9", "wmv");
        fileTypeMap.put("52494646e27807005741", "wav");
        fileTypeMap.put("52494646d07d60074156", "avi");
        fileTypeMap.put("4d546864000000060001", "mid");
        fileTypeMap.put("504b0304140000000800", "zip");
        fileTypeMap.put("526172211a0700cf9073", "rar");
        fileTypeMap.put("235468697320636f6e66", "ini");
        fileTypeMap.put("504b03040a0000000000", "jar");
        fileTypeMap.put("4d5a9000030000000400", "exe");
        fileTypeMap.put("3c25402070616765206c", "jsp");
        fileTypeMap.put("4d616e69666573742d56", "mf");
        fileTypeMap.put("3c3f786d6c2076657273", "xml");
        fileTypeMap.put("494e5345525420494e54", "sql");
        fileTypeMap.put("7061636b616765207765", "java");
        fileTypeMap.put("406563686f206f66660d", "bat");
        fileTypeMap.put("1f8b0800000000000000", "gz");
        fileTypeMap.put("6c6f67346a2e726f6f74", "properties");
        fileTypeMap.put("cafebabe0000002e0041", "class");
        fileTypeMap.put("49545346030000006000", "chm");
        fileTypeMap.put("04000000010000001300", "mxp");
        fileTypeMap.put("504b0304140006000800", "docx");
        fileTypeMap.put("6431303a637265617465", "torrent");
        fileTypeMap.put("6D6F6F76", "mov");
        fileTypeMap.put("FF575043", "wpd");
        fileTypeMap.put("CFAD12FEC5FD746F", "dbx");
        fileTypeMap.put("2142444E", "pst");
        fileTypeMap.put("AC9EBD8F", "qdf");
        fileTypeMap.put("E3828596", "pwl");
        fileTypeMap.put("2E7261FD", "ram");*/
    }

    /**
     * @param fileName
     * @return String
     * @description 通过文件后缀名获取文件类型
     */
    private static String getFileTypeBySuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }


    /**
     * 下载文件类型过滤
     *
     * @param filePath
     */
    public static void checkDownloadFileType(String filePath) throws IOException {
        //文件后缀
        String suffix = getFileTypeBySuffix(filePath);
        log.debug(" 【文件下载校验】文件后缀 suffix: {}", suffix);
        boolean isAllowExtension = FILE_TYPE_WHITE_LIST.contains(suffix.toLowerCase());
        //是否允许下载的文件
        if (!isAllowExtension) {
            throw new JeecgBootException("下载失败，存在非法文件类型：" + suffix);
        }
    }

    /**
     * 上传文件类型过滤
     *
     * @param file
     */
    public static void checkUploadFileType(MultipartFile file) throws Exception {
        checkUploadFileType(file, null);
    }
    
    /**
     * 上传文件类型过滤
     *
     * @param file
     */
    public static void checkUploadFileType(MultipartFile file, String customPath) throws Exception {
        //1. 路径安全校验
        validatePathSecurity(customPath);
        //2. 校验文件后缀和头
        String suffix = getFileType(file, customPath);
        log.info("【文件上传校验】文件后缀 suffix: {}，customPath：{}", suffix, customPath);
        boolean isAllowExtension = FILE_TYPE_WHITE_LIST.contains(suffix.toLowerCase());
        //是否允许下载的文件
        if (!isAllowExtension) {
            throw new JeecgBootException("上传失败，存在非法文件类型：" + suffix);
        }
        //3. SVG文件内容安全校验（issues/9693）
        if ("svg".equalsIgnoreCase(suffix)) {
            checkSvgSafety(file);
        }
    }

    /**
     * 通过读取文件头部获得文件类型
     *
     * @param file
     * @return 文件类型
     * @throws Exception
     */

    private static String getFileType(MultipartFile file, String customPath) throws Exception {
        // 代码逻辑说明: [issue/4672]方法造成的文件被占用，注释掉此方法tomcat就能自动清理掉临时文件
        String fileExtendName = null;
        InputStream is = null;
        try {
            //is = new FileInputStream(file);
            is = file.getInputStream();
            byte[] b = new byte[10];
            is.read(b, 0, b.length);
            String fileTypeHex = String.valueOf(bytesToHexString(b));
            Iterator<String> keyIter = FILE_TYPE_MAP.keySet().iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                //update-begin---author:lsq ---date:2026-05-26  for：修复SVG文件被误判为php的问题（<?xml与<?php前2.5字节相同，扩大比较长度到10位避免误判）-----------
                // 验证前10个字符比较（5字节），避免<?xml与<?php因前5位hex相同而误判
                int compareLen = Math.min(10, Math.min(key.length(), fileTypeHex.length()));
                if (key.toLowerCase().startsWith(fileTypeHex.toLowerCase().substring(0, compareLen))
                        || fileTypeHex.toLowerCase().substring(0, compareLen).startsWith(key.toLowerCase())) {
                //update-end---author:lsq ---date:2026-05-26  for：修复SVG文件被误判为php的问题（<?xml与<?php前2.5字节相同，扩大比较长度到10位避免误判）-----------
                    fileExtendName = FILE_TYPE_MAP.get(key);
                    break;
                }
            }
            log.debug("-----获取到的指定文件类型------"+fileExtendName);
            // 如果不是上述类型，则判断扩展名
            if (StringUtils.isBlank(fileExtendName)) {
                String fileName = file.getOriginalFilename();
                // 如果无扩展名，则直接返回空串
                if (-1 == fileName.indexOf(".")) {
                    return "";
                }
                // 如果有扩展名，则返回扩展名
                return getFileTypeBySuffix(fileName);
            }
            is.close();
            return fileExtendName;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * 获得文件头部字符串
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 路径安全校验
     */
    private static void validatePathSecurity(String customPath) throws JeecgBootException {
        if (customPath == null || customPath.trim().isEmpty()) {
            return;
        }

        // 统一分隔符为 /
        String normalized = customPath.replace("\\", "/");

        // 1. 防止路径遍历攻击
        if (normalized.contains("..") || normalized.contains("~")) {
            throw new JeecgBootException("上传业务路径包含非法字符！");
        }

        // 2. 限制路径深度
        int depth = normalized.split("/").length;
        if (depth > 5) {
            throw new JeecgBootException("上传业务路径深度超出限制！");
        }

        // 3. 限制字符集（只允许字母、数字、下划线、横线、斜杠）
        if (!normalized.matches("^[a-zA-Z0-9/_-]+$")) {
            throw new JeecgBootException("上传业务路径包含非法字符！");
        }
    }

    /**
     * 校验文件路径安全性，防止路径遍历攻击
     * @param filePath 文件路径
     */
    public static void checkPathTraversal(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return;
        }
        // 1. 防止路径遍历：不允许 ..
        if (filePath.contains("..")) {
            throw new JeecgBootException("文件路径包含非法字符");
        }
        // 2. 防止URL编码绕过：%2e = .
        String fileLower = filePath.toLowerCase();
        if (fileLower.contains("%2e")) {
            throw new JeecgBootException("文件路径包含非法字符");
        }
    }

    /** 
     * 【issues/9553】修复二次SSRF漏洞，对HTTP下载URL进行安全校验
     * 2026-04-15
     * 
     * 校验HTTP(S) URL，防止SSRF攻击，规则见 {@link LocalAddressCheckUtils#checkUrl(String)}
     *
     * @param fileUrl HTTP(S) URL
     */
    public static void checkSsrfHttpUrl(String fileUrl) {
        LocalAddressCheckUtils.checkUrl(fileUrl);
    }

    /**
     * 批量校验文件路径安全性（逗号分隔的多个文件路径）
     * @param files 逗号分隔的文件路径
     */
    public static void checkPathTraversalBatch(String files) {
        if (StringUtils.isBlank(files)) {
            return;
        }
        for (String file : files.split(",")) {
            if (StringUtils.isNotBlank(file)) {
                checkPathTraversal(file.trim());
            }
        }
    }

    /**
     * SVG 危险标签黑名单（标签名统一小写比较）
     */
    private static final Set<String> SVG_DANGEROUS_TAGS = new HashSet<>(Arrays.asList(
            "script", "foreignobject", "iframe", "object", "embed", "applet",
            "form", "input", "textarea", "button", "select",
            "link", "meta", "base", "import",
            "handler", "set", "animate", "animatemotion", "animatetransform"
    ));

    /**
     * SVG 危险属性正则：匹配事件属性（on*="..."）和 javascript: 协议
     */
    private static final Pattern SVG_EVENT_ATTR_PATTERN = Pattern.compile(
            "\\bon\\w+\\s*=", Pattern.CASE_INSENSITIVE
    );
    private static final Pattern SVG_JS_PROTOCOL_PATTERN = Pattern.compile(
            "javascript\\s*:", Pattern.CASE_INSENSITIVE
    );
    /**
     * HTML entity 编码的 javascript 协议（&#106;avascript: 等变体）
     */
    private static final Pattern SVG_ENTITY_JS_PATTERN = Pattern.compile(
            "&#\\d+;|&#x[0-9a-f]+;", Pattern.CASE_INSENSITIVE
    );

    /**
     * 校验 SVG 文件内容是否安全，防止存储型 XSS（issues/9693）。
     * 采用文本扫描方式检测危险标签、事件属性和 javascript: 协议。
     *
     * @param file 上传的 SVG 文件
     */
    private static void checkSvgSafety(MultipartFile file) throws Exception {
        String originalContent;
        try (InputStream is = file.getInputStream()) {
            originalContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        String content = originalContent.toLowerCase();
        // 检测危险标签
        for (String tag : SVG_DANGEROUS_TAGS) {
            if (content.contains("<" + tag + ">") || content.contains("<" + tag + " ")
                    || content.contains("<" + tag + "/") || content.contains("<" + tag + "\t")
                    || content.contains("<" + tag + "\n") || content.contains("<" + tag + "\r")) {
                throw new JeecgBootException("上传失败，SVG文件包含不安全的标签：<" + tag + ">");
            }
        }
        // 检测事件属性（onclick、onload、onerror、onbegin 等）
        if (SVG_EVENT_ATTR_PATTERN.matcher(originalContent).find()) {
            throw new JeecgBootException("上传失败，SVG文件包含不安全的事件属性");
        }
        // 检测 javascript: 协议
        if (SVG_JS_PROTOCOL_PATTERN.matcher(originalContent).find()) {
            throw new JeecgBootException("上传失败，SVG文件包含不安全的javascript协议");
        }
        // 检测 HTML entity 编码（防止 &#106;avascript: 等绕过）
        if (SVG_ENTITY_JS_PATTERN.matcher(originalContent).find()) {
            throw new JeecgBootException("上传失败，SVG文件包含不安全的编码内容");
        }
        // 检测 DOCTYPE/ENTITY 声明（防止 XML Bomb / Billion Laughs DoS 攻击）
        if (content.contains("<!doctype") || content.contains("<!entity")) {
            throw new JeecgBootException("上传失败，SVG文件包含不安全的DOCTYPE/ENTITY声明");
        }
    }

}
