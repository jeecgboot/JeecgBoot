package org.jeecg.modules.bjcl.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    /**
     * 文件是否存在
     */
    public static boolean existsFile(File file) {
        return file.exists() && file.isFile();

    }
//    /**
//     * 删除文件或文件夹
//     */
//    public static void deleteIfExists(File file){
//        deleteIfExists(file);
//    }


    /**
     * 删除文件或文件夹
     */
    public static void deleteIfExists(File file) throws IOException {
        if (file.exists()) {
            if (file.isFile()) {
                if (!file.delete()) {
                    throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                }
            } else {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File temp : files) {
                        deleteIfExists(temp);
                    }
                }
                if (!file.delete()) {
                    throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                }
            }
        }
    }
}
