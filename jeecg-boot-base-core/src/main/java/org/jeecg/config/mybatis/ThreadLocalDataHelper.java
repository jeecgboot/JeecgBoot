package org.jeecg.config.mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Description: 本地线程变量存储工具类
 * @author: lsq
 * @date: 2022年03月25日 11:42
 */
public class ThreadLocalDataHelper {
    /**
     * 线程的本地变量
     */
    private static final ThreadLocal<ConcurrentHashMap> REQUEST_DATA = new ThreadLocal<>();

    /**
     * 存储本地参数
     */
    private static final ConcurrentHashMap DATA_MAP = new ConcurrentHashMap<>();

    /**
     * 设置请求参数
     *
     * @param key  参数key
     * @param value 参数值
     */
    public static void put(String key, Object value) {
        if(ObjectUtil.isNotEmpty(value)) {
            DATA_MAP.put(key, value);
            REQUEST_DATA.set(DATA_MAP);
        }
    }

    /**
     * 获取请求参数值
     *
     * @param key 请求参数
     * @return
     */
    public static <T> T get(String key) {
        ConcurrentHashMap dataMap = REQUEST_DATA.get();
        if (CollectionUtils.isNotEmpty(dataMap)) {
            return (T) dataMap.get(key);
        }
        return null;
    }

    /**
     * 获取请求参数
     *
     * @return 请求参数 MAP 对象
     */
    public static void clear() {
        DATA_MAP.clear();
        REQUEST_DATA.remove();
    }

}

