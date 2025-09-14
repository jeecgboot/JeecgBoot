package org.jeecg.modules.system.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 导入缓存类，为了前台显示进度
 * @author: wangshuai
 * @date: 2025/9/6 14:09
 */
public class ImportSysUserCache {

    private static final Map<String, Double> importSysUserMap = new HashMap<>();

    /**
     * 获取导入的列
     *
     * @param key
     * @param type user 用户 可扩展
     * @return
     */
    public static Double getImportSysUserMap(String key, String type) {
        if (importSysUserMap.containsKey(key + "__" + type)) {
            return importSysUserMap.get(key + "__" + type);
        }
        return 0.0;
    }

    /**
     * 设置导入缓存
     *
     * @param key    前村传过来的随机key
     * @param num    导入行数
     * @param length 总长度
     * @param type   导入类型 user 用户列表
     */
    public static void setImportSysUserMap(String key, int num, int length, String type) {
        double percent = (num * 100.0) / length;
        if(num == length){
            percent = 100;
        }
        importSysUserMap.put(key + "__" + type, percent);
    }

    /**
     * 移除导入缓存
     *
     * @param key
     */
    public static void removeImportLowAppMap(String key) {
        importSysUserMap.remove(key);
    }
}
