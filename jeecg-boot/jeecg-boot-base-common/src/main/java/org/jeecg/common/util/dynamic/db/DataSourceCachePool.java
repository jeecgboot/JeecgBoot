package org.jeecg.common.util.dynamic.db;

import org.apache.commons.dbcp.BasicDataSource;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.DynamicDataSourceModel;
import org.jeecg.common.util.SpringContextUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 数据源缓存池
 */
public class DataSourceCachePool {

    /**
     * DynamicDataSourceModel的 code 和 id 的对应关系。key为id，value为code
     */
    private static Map<String, String> dynamicDbSourcesIdToCode = new HashMap<>();
    /**
     * DynamicDataSourceModel的缓存。key为code，value为数据源
     */
    private static Map<String, DynamicDataSourceModel> dynamicDbSourcesCache = new HashMap<>();

    /**
     * 动态数据源参数配置【缓存】
     */
    public static Map<String, DynamicDataSourceModel> getCacheAllDynamicDataSourceModel() {
        return dynamicDbSourcesCache;
    }

    /**
     * 获取多数据源缓存
     *
     * @param dbKey
     * @return
     */
    public static DynamicDataSourceModel getCacheDynamicDataSourceModel(String dbKey) {
        DynamicDataSourceModel dbSource = dynamicDbSourcesCache.get(dbKey);
        if (dbSource == null) {
            ISysBaseAPI sysBaseAPI = SpringContextUtils.getBean(ISysBaseAPI.class);
            dbSource = sysBaseAPI.getDynamicDbSourceByCode(dbKey);
            dynamicDbSourcesCache.put(dbKey, dbSource);
            dynamicDbSourcesIdToCode.put(dbSource.getId(), dbKey);
        }
        return dbSource;
    }

    /**
     * 动态数据源连接池【本地class缓存 - 不支持分布式】
     */
    private static Map<String, BasicDataSource> dbSources = new HashMap<>();

    public static BasicDataSource getCacheBasicDataSource(String dbKey) {
        return dbSources.get(dbKey);
    }

    /**
     * put 数据源缓存
     *
     * @param dbKey
     * @param db
     */
    public static void putCacheBasicDataSource(String dbKey, BasicDataSource db) {
        dbSources.put(dbKey, db);
    }

    /**
     * 清空数据源缓存
     */
    public static void cleanCacheBasicDataSource() {
        dbSources.clear();
    }

    public static void removeCache(String dbKey) {
        dbSources.remove(dbKey);
        dynamicDbSourcesCache.remove(dbKey);
    }

    public static void removeCacheById(String dbId) {
        String dbKey = dynamicDbSourcesIdToCode.get(dbId);
        dbSources.remove(dbKey);
        dynamicDbSourcesCache.remove(dbKey);
    }

}
