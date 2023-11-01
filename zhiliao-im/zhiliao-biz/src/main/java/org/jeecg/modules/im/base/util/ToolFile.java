package org.jeecg.modules.im.base.util;

import java.net.FileNameMap;
import java.net.URLConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.List;

@Slf4j
public class ToolFile {
    private final static String PREFIX_VIDEO="video/";
    public static void delete(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                if(!file.delete()){
                    log.error("文件删除失败:"+file.getAbsolutePath());
                }
            }
            else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i=0; i<files.length; i++) {
                    delete(files[i]);
                }
            }
            file.delete();
        }
    }

    /**
     * 获取文件夹下的所有文件
     * @param fileList
     * @param file
     * @return
     */
    public static List<File> getAllFile(List<File> fileList,File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                fileList.add(file);
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i=0; i<files.length; i++) {
                    getAllFile(fileList,files[i]);
                }
            }
        }
        return fileList;
    }
    public static String getExtension(String fileName){
        if(fileName.contains("?")){
            String str = fileName.substring(fileName.lastIndexOf("."));
            return str.substring(0,str.indexOf("?"));
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * Get the Mime Type from a File
     * @param fileName 文件名
     * @return 返回MIME类型
     * thx https://www.oschina.net/question/571282_223549
     * add by fengwenhua 2017年5月3日09:55:01
     */
    private static String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileName);
        return type;
    }

    /**
     * 根据文件后缀名判断 文件是否是视频文件
     * @param fileName 文件名
     * @return 是否是视频文件
     */
    public static boolean isVideoFile(String fileName){
        String mimeType = getMimeType(fileName);
        if (StringUtils.isNotBlank(fileName)&&mimeType.contains(PREFIX_VIDEO)){
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(getExtension("e2b2e49b5b7846f1b5a01cc6e7a22c2f.svg?job=coverage"));
    }

    public static Date getCreateTime(File file){
        BasicFileAttributes attributes = null;
        try {
            attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return new Date(attributes.creationTime().toMillis());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static long getSize(File file){
        BasicFileAttributes attributes = null;
        try {
            attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return attributes.size();
        } catch (IOException e) {
            e.printStackTrace();
            return 0l;
        }
    }

}
