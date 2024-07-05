package org.springframework.base.system.core;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 
 * 系统数据库操作工具对象
 * 
 * @author 00fly
 * @version [版本号, 2018年11月22日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SysDataBaseUtil {
    
    private static JdbcTemplate jdbcTemplate;
    
    // 静态初始化 DataSource
    public static void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    /**
     * 返回Map
     * 
     * @param sql
     * @param params
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, Object> queryForMap(String sql, Object... params) {
        return jdbcTemplate.queryForMap(sql, params);
    }
}
