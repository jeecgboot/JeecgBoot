package org.jeecg.common.util.dynamic.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DynamicDataSourceModel;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.HashMap;
import java.util.Map;


/**
 * 数据源缓存池
 */
public class DataSourceCachePool {
    /** 数据源连接池缓存【本地 class缓存 - 不支持分布式】 */
    private static Map<String, DruidDataSource> dbSources = new HashMap<>();
    private static RedisTemplate<String, Object> redisTemplate;

    private static RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = (RedisTemplate<String, Object>) SpringContextUtils.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    /**
     * 获取多数据源缓存
     *
     * @param dbKey
     * @return
     */
    public static DynamicDataSourceModel getCacheDynamicDataSourceModel(String dbKey) {
        String redisCacheKey = CacheConstant.SYS_DYNAMICDB_CACHE + dbKey;
        if (getRedisTemplate().hasKey(redisCacheKey)) {
            return (DynamicDataSourceModel) getRedisTemplate().opsForValue().get(redisCacheKey);
        }
        ISysBaseAPI sysBaseAPI = SpringContextUtils.getBean(ISysBaseAPI.class);
        DynamicDataSourceModel dbSource = sysBaseAPI.getDynamicDbSourceByCode(dbKey);
        if (dbSource != null) {
            getRedisTemplate().opsForValue().set(redisCacheKey, dbSource);
        }
        return dbSource;
    }

    public static DruidDataSource getCacheBasicDataSource(String dbKey) {
        return dbSources.get(dbKey);
    }

    /**
     * put 数据源缓存
     *
     * @param dbKey
     * @param db
     */
    public static void putCacheBasicDataSource(String dbKey, DruidDataSource db) {
        dbSources.put(dbKey, db);
    }

    /**
     * 清空数据源缓存
     */
    public static void cleanAllCache() {
        //关闭数据源连接
        for(Map.Entry<String, DruidDataSource> entry : dbSources.entrySet()){
            String dbkey = entry.getKey();
            DruidDataSource druidDataSource = entry.getValue();
            if(druidDataSource!=null && druidDataSource.isEnable()){
                druidDataSource.close();
            }
            //清空redis缓存
            getRedisTemplate().delete(CacheConstant.SYS_DYNAMICDB_CACHE + dbkey);
        }
        //清空缓存
        dbSources.clear();
    }

    public static void removeCache(String dbKey) {
        //关闭数据源连接
        DruidDataSource druidDataSource = dbSources.get(dbKey);
        if(druidDataSource!=null && druidDataSource.isEnable()){
            druidDataSource.close();
        }
        //清空redis缓存
        getRedisTemplate().delete(CacheConstant.SYS_DYNAMICDB_CACHE + dbKey);
        //清空缓存
        dbSources.remove(dbKey);
    }

}
