package org.jeecg.modules.demo.gpt.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;

//update-begin---author:chenrui ---date:20240126  for：【QQYUN-7932】AI助手------------

/**
 * 聊天记录本地缓存
 * @author chenrui
 * @date 2024/1/26 20:06
 */
public class LocalCache {
    /**
     * 缓存时长
     */
    public static final long TIMEOUT = 5 * DateUnit.MINUTE.getMillis();
    /**
     * 清理间隔
     */
    private static final long CLEAN_TIMEOUT = 5 * DateUnit.MINUTE.getMillis();
    /**
     * 缓存对象
     */
    public static final TimedCache<String, Object> CACHE = CacheUtil.newTimedCache(TIMEOUT);

    static {
        //启动定时任务
        CACHE.schedulePrune(CLEAN_TIMEOUT);
    }
}

//update-end---author:chenrui ---date:20240126  for：【QQYUN-7932】AI助手------------