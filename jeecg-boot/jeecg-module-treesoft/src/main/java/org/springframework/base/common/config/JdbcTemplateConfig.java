package org.springframework.base.common.config;

import org.springframework.base.system.core.SysDataBaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * 
 * 注册 JdbcTemplate
 * 
 * @author 00fly
 * @version [版本号, 2018年11月20日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Configuration
public class JdbcTemplateConfig {
    @Autowired
    private DataSource dataSource;
    
    @PostConstruct
    public void initSysDataSources() {
        SysDataBaseUtil.setDataSource(dataSource);
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
