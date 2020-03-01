package org.jeecg.common.util.dynamic.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.ArrayUtils;
import org.jeecg.common.system.vo.DynamicDataSourceModel;
import org.jeecg.common.util.ReflectHelper;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring JDBC 实时数据库访问
 *
 * @author chenguobin
 * @version 1.0
 * @date 2014-09-05
 */
@Slf4j
public class DynamicDBUtil {

    /**
     * 获取数据源【最底层方法，不要随便调用】
     *
     * @param dbSource
     * @return
     */
    @Deprecated
    private static BasicDataSource getJdbcDataSource(final DynamicDataSourceModel dbSource) {
        BasicDataSource dataSource = new BasicDataSource();

        String driverClassName = dbSource.getDbDriver();
        String url = dbSource.getDbUrl();
        String dbUser = dbSource.getDbUsername();
        String dbPassword = dbSource.getDbPassword();
        //设置数据源的时候，要重新解密
//		String dbPassword  = PasswordUtil.decrypt(dbSource.getDbPassword(), dbSource.getDbUsername(), PasswordUtil.getStaticSalt());//解密字符串；
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    /**
     * 通过 dbKey ,获取数据源
     *
     * @param dbKey
     * @return
     */
    public static BasicDataSource getDbSourceByDbKey(final String dbKey) {
        //获取多数据源配置
        DynamicDataSourceModel dbSource = DataSourceCachePool.getCacheDynamicDataSourceModel(dbKey);
        //先判断缓存中是否存在数据库链接
        BasicDataSource cacheDbSource = DataSourceCachePool.getCacheBasicDataSource(dbKey);
        if (cacheDbSource != null && !cacheDbSource.isClosed()) {
            log.debug("--------getDbSourceBydbKey------------------从缓存中获取DB连接-------------------");
            return cacheDbSource;
        } else {
            BasicDataSource dataSource = getJdbcDataSource(dbSource);
            DataSourceCachePool.putCacheBasicDataSource(dbKey, dataSource);
            log.info("--------getDbSourceBydbKey------------------创建DB数据库连接-------------------");
            return dataSource;
        }
    }

    /**
     * 关闭数据库连接池
     *
     * @param dbKey
     * @return
     */
    public static void closeDbKey(final String dbKey) {
        BasicDataSource dataSource = getDbSourceByDbKey(dbKey);
        try {
            if (dataSource != null && !dataSource.isClosed()) {
                dataSource.getConnection().commit();
                dataSource.getConnection().close();
                dataSource.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static JdbcTemplate getJdbcTemplate(String dbKey) {
        BasicDataSource dataSource = getDbSourceByDbKey(dbKey);
        return new JdbcTemplate(dataSource);
    }

    /**
     * Executes the SQL statement in this <code>PreparedStatement</code> object,
     * which must be an SQL Data Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing,
     * such as a DDL statement.
     */
    public static int update(final String dbKey, String sql, Object... param) {
        int effectCount;
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);
        if (ArrayUtils.isEmpty(param)) {
            effectCount = jdbcTemplate.update(sql);
        } else {
            effectCount = jdbcTemplate.update(sql, param);
        }
        return effectCount;
    }

    /**
     * 支持miniDao语法操作的Update
     *
     * @param dbKey 数据源标识
     * @param sql   执行sql语句，sql支持minidao语法逻辑
     * @param data  sql语法中需要判断的数据及sql拼接注入中需要的数据
     * @return
     */
    public static int updateByHash(final String dbKey, String sql, HashMap<String, Object> data) {
        int effectCount;
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);
        //根据模板获取sql
        sql = FreemarkerParseFactory.parseTemplateContent(sql, data);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        effectCount = namedParameterJdbcTemplate.update(sql, data);
        return effectCount;
    }

    public static Object findOne(final String dbKey, String sql, Object... param) {
        List<Map<String, Object>> list;
        list = findList(dbKey, sql, param);
        if (oConvertUtils.listIsEmpty(list)) {
            log.error("Except one, but not find actually");
        }
        if (list.size() > 1) {
            log.error("Except one, but more than one actually");
        }
        return list.get(0);
    }

    /**
     * 支持miniDao语法操作的查询 返回HashMap
     *
     * @param dbKey 数据源标识
     * @param sql   执行sql语句，sql支持minidao语法逻辑
     * @param data  sql语法中需要判断的数据及sql拼接注入中需要的数据
     * @return
     */
    public static Object findOneByHash(final String dbKey, String sql, HashMap<String, Object> data) {
        List<Map<String, Object>> list;
        list = findListByHash(dbKey, sql, data);
        if (oConvertUtils.listIsEmpty(list)) {
            log.error("Except one, but not find actually");
        }
        if (list.size() > 1) {
            log.error("Except one, but more than one actually");
        }
        return list.get(0);
    }

    /**
     * 直接sql查询 根据clazz返回单个实例
     *
     * @param dbKey 数据源标识
     * @param sql   执行sql语句
     * @param clazz 返回实例的Class
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Object findOne(final String dbKey, String sql, Class<T> clazz, Object... param) {
        Map<String, Object> map = (Map<String, Object>) findOne(dbKey, sql, param);
        return ReflectHelper.setAll(clazz, map);
    }

    /**
     * 支持miniDao语法操作的查询 返回单个实例
     *
     * @param dbKey 数据源标识
     * @param sql   执行sql语句，sql支持minidao语法逻辑
     * @param clazz 返回实例的Class
     * @param data  sql语法中需要判断的数据及sql拼接注入中需要的数据
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Object findOneByHash(final String dbKey, String sql, Class<T> clazz, HashMap<String, Object> data) {
        Map<String, Object> map = (Map<String, Object>) findOneByHash(dbKey, sql, data);
        return ReflectHelper.setAll(clazz, map);
    }

    public static List<Map<String, Object>> findList(final String dbKey, String sql, Object... param) {
        List<Map<String, Object>> list;
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);

        if (ArrayUtils.isEmpty(param)) {
            list = jdbcTemplate.queryForList(sql);
        } else {
            list = jdbcTemplate.queryForList(sql, param);
        }
        return list;
    }

    /**
     * 支持miniDao语法操作的查询
     *
     * @param dbKey 数据源标识
     * @param sql   执行sql语句，sql支持minidao语法逻辑
     * @param data  sql语法中需要判断的数据及sql拼接注入中需要的数据
     * @return
     */
    public static List<Map<String, Object>> findListByHash(final String dbKey, String sql, HashMap<String, Object> data) {
        List<Map<String, Object>> list;
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);
        //根据模板获取sql
        sql = FreemarkerParseFactory.parseTemplateContent(sql, data);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        list = namedParameterJdbcTemplate.queryForList(sql, data);
        return list;
    }

    //此方法只能返回单列，不能返回实体类
    public static <T> List<T> findList(final String dbKey, String sql, Class<T> clazz, Object... param) {
        List<T> list;
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);

        if (ArrayUtils.isEmpty(param)) {
            list = jdbcTemplate.queryForList(sql, clazz);
        } else {
            list = jdbcTemplate.queryForList(sql, clazz, param);
        }
        return list;
    }

    /**
     * 支持miniDao语法操作的查询 返回单列数据list
     *
     * @param dbKey 数据源标识
     * @param sql   执行sql语句，sql支持minidao语法逻辑
     * @param clazz 类型Long、String等
     * @param data  sql语法中需要判断的数据及sql拼接注入中需要的数据
     * @return
     */
    public static <T> List<T> findListByHash(final String dbKey, String sql, Class<T> clazz, HashMap<String, Object> data) {
        List<T> list;
        JdbcTemplate jdbcTemplate = getJdbcTemplate(dbKey);
        //根据模板获取sql
        sql = FreemarkerParseFactory.parseTemplateContent(sql, data);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        list = namedParameterJdbcTemplate.queryForList(sql, data, clazz);
        return list;
    }

    /**
     * 直接sql查询 返回实体类列表
     *
     * @param dbKey 数据源标识
     * @param sql   执行sql语句，sql支持 minidao 语法逻辑
     * @param clazz 返回实体类列表的class
     * @param param sql拼接注入中需要的数据
     * @return
     */
    public static <T> List<T> findListEntities(final String dbKey, String sql, Class<T> clazz, Object... param) {
        List<Map<String, Object>> queryList = findList(dbKey, sql, param);
        return ReflectHelper.transList2Entrys(queryList, clazz);
    }

    /**
     * 支持miniDao语法操作的查询 返回实体类列表
     *
     * @param dbKey 数据源标识
     * @param sql   执行sql语句，sql支持minidao语法逻辑
     * @param clazz 返回实体类列表的class
     * @param data  sql语法中需要判断的数据及sql拼接注入中需要的数据
     * @return
     */
    public static <T> List<T> findListEntitiesByHash(final String dbKey, String sql, Class<T> clazz, HashMap<String, Object> data) {
        List<Map<String, Object>> queryList = findListByHash(dbKey, sql, data);
        return ReflectHelper.transList2Entrys(queryList, clazz);
    }
}
