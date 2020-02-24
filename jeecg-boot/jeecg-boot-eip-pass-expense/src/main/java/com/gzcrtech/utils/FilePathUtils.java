package com.gzcrtech.utils;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileCopyUtils;

@Configuration
@EnableCaching // 开启缓存支持
public class FilePathUtils {
	
	@Value("${jeecg.path.news}")
	private String newsPath;
	/**
     * 检查文件夹(目录)是否存在，不存在则创建
     * 
     * @param path
     *            文件夹(目录)路径
     * @return 文件夹是否已成功创建
     */
    public static boolean dirChecker(String path) throws SecurityException {

        File directory = new File(path);
        if (directory.exists()) {
            return true;
        } else {
            return directory.mkdirs();
        }
    }
    
    /**
     * 获取静态化页面存放的路径
     * @return
     */
    public String getHtmlFilePath() {
		return newsPath+"/html";
	}
    
    public String getTempFilePath() {
		return newsPath+"/temp";
	}
    
    /**
     * 获取附件上传的路径
     * @return
     */
    public String getAttachmentPath() {
		return newsPath+"/attachment";
	}
    
    private static boolean isWindows() {
		if ("/".equals(File.separator)) {
			return false;
		}
		return true;
	}

	private static boolean isUnix() {
		if ("/".equals(File.separator)) {
			return true;
		}
		return false;
	}
}
