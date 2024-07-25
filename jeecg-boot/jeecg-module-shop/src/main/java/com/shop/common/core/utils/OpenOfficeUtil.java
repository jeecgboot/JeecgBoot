package com.shop.common.core.utils;

import com.shop.common.core.Constants;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * OpenOfficeUtil
 * 2018-12-14 08:38
 */
public class OpenOfficeUtil {
    // pdf转换后的目录
    private static final String PDF_TEMP_DIR = Constants.UPLOAD_DIR + "pdf/";
    // 支持转换pdf的文件后缀列表
    private static final String[] CAN_CONVERTER_FILES = new String[]{"doc", "docx", "xls", "xlsx", "ppt", "pptx"};

    /**
     * 文件转pdf
     *
     * @param filePath 源文件路径
     * @return File
     */
    public static File converterToPDF(String filePath) {
        return converterToPDF(filePath, true);
    }

    /**
     * 文件转pdf
     *
     * @param filePath 源文件路径
     * @param cache    是否使用上次转换过的文件
     * @return File
     */
    public static File converterToPDF(String filePath, boolean cache) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return null;
        }
        File srcFile = new File(filePath);
        if (!srcFile.exists()) {
            return null;
        }
        // 是否转换过
        File outFile = new File(PDF_TEMP_DIR, Base64.getEncoder().encodeToString(filePath.getBytes()) + ".pdf");
        if (cache && outFile.exists()) {
            return outFile;
        }
        // 转换
        OfficeManager officeManager = null;
        try {
            officeManager = getOfficeManager();
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
            return converterFile(srcFile, outFile, converter);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (officeManager != null) {
                officeManager.stop();
            }
        }
        return null;
    }

    /**
     * 转换文件
     */
    public static File converterFile(File inFile, File outFile, OfficeDocumentConverter converter) {
        if (!outFile.getParentFile().exists()) {
            if (!outFile.getParentFile().mkdirs()) return outFile;
        }
        converter.convert(inFile, outFile);
        return outFile;
    }

    /**
     * 判断文件后缀是否可以转换pdf
     */
    public static boolean canConverter(String path) {
        try {
            String suffix = path.substring(path.lastIndexOf(".") + 1);
            return Arrays.asList(CAN_CONVERTER_FILES).contains(suffix);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 连接并启动OpenOffice
     */
    public static OfficeManager getOfficeManager() {
        String officeHome = getOfficeHome();
        if (officeHome == null) return null;
        DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();
        config.setOfficeHome(officeHome);  // 设置OpenOffice安装目录
        OfficeManager officeManager = config.buildOfficeManager();
        officeManager.start();  // 启动OpenOffice服务
        return officeManager;
    }

    /**
     * 根据操作系统获取OpenOffice安装目录
     */
    public static String getOfficeHome() {
        String osName = System.getProperty("os.name");
        if (Pattern.matches("Linux.*", osName)) {
            return Constants.OPEN_OFFICE_PATH_LINUX;
        } else if (Pattern.matches("Windows.*", osName)) {
            return Constants.OPEN_OFFICE_PATH_WINDOWS;
        } else if (Pattern.matches("Mac.*", osName)) {
            return Constants.OPEN_OFFICE_PATH_MAC;
        }
        return null;
    }

}
