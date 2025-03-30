package org.jeecg.config.flyway;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Map;

/**
* @Description: 初始化flyway配置 修改之后支持多数据源，当出现异常时打印日志，不影响项目启动
*
* @author: wangshuai
* @date: 2024/3/12 10:03
*/
@Slf4j
@Configuration
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment environment;

    /**
     * 是否开启flyway
     */
    @Value("${spring.flyway.enabled:false}")
    private Boolean enabled;
    
    /**
     * 编码格式，默认UTF-8
     */
    @Value("${spring.flyway.encoding:UTF-8}")
    private String encoding;

    /**
     * 迁移sql脚本文件存放路径，官方默认db/migration
     */
    @Value("${spring.flyway.locations:classpath:flyway/sql/mysql}")
    private String locations;

    /**
     * 迁移sql脚本文件名称的前缀，默认V
     */
    @Value("${spring.flyway.sql-migration-prefix:V}")
    private String sqlMigrationPrefix;

    /**
     * 迁移sql脚本文件名称的分隔符，默认2个下划线__
     */
    @Value("${spring.flyway.sql-migration-separator:__}")
    private String sqlMigrationSeparator;

    /**
     * 文本前缀
     */
    @Value("${spring.flyway.placeholder-prefix:#(}")
    private String placeholderPrefix;

    /**
     * 文本后缀
     */
    @Value("${spring.flyway.placeholder-suffix:)}")
    private String placeholderSuffix;

    /**
     * 迁移sql脚本文件名称的后缀
     */
    @Value("${spring.flyway.sql-migration-suffixes:.sql}")
    private String sqlMigrationSuffixes;

    /**
     * 迁移时是否进行校验，默认true
     */
    @Value("${spring.flyway.validate-on-migrate:true}")
    private Boolean validateOnMigrate;

    /**
     * 当迁移发现数据库非空且存在没有元数据的表时，自动执行基准迁移，新建schema_version表
     */
    @Value("${spring.flyway.baseline-on-migrate:true}")
    private Boolean baselineOnMigrate;

    /**
     * 是否关闭要清除已有库下的表功能,生产环境必须为true,否则会删库，非常重要！！！
     */
    @Value("${spring.flyway.clean-disabled:true}")
    private Boolean cleanDisabled;

    @PostConstruct
    public void migrate() {
        if(!enabled){
            return;
        }
        
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        Map<String, DataSource> dataSources = ds.getDataSources();
        dataSources.forEach((k, v) -> {
            if("master".equals(k)){
                String databaseType = environment.getProperty("spring.datasource.dynamic.datasource." + k + ".url");
                if (databaseType != null && databaseType.contains("mysql")) {
                    try {
                            Flyway flyway = Flyway.configure()
                                    .dataSource(v)
                                    .locations(locations)
                                    .encoding(encoding)
                                    .sqlMigrationPrefix(sqlMigrationPrefix)
                                    .sqlMigrationSeparator(sqlMigrationSeparator)
                                    .placeholderPrefix(placeholderPrefix)
                                    .placeholderSuffix(placeholderSuffix)
                                    .sqlMigrationSuffixes(sqlMigrationSuffixes)
                                    .validateOnMigrate(validateOnMigrate)
                                    .baselineOnMigrate(baselineOnMigrate)
                                    .cleanDisabled(cleanDisabled)
                                    .load();
                            flyway.migrate();
                            log.info("【数据库升级】平台集成了MySQL库的Flyway，数据库版本自动升级! ");
                    } catch (FlywayException e) {
                        log.error("【数据库升级】flyway执行sql脚本失败", e);
                    }
                } else {
                    log.warn("【数据库升级】平台只集成了MySQL库的Flyway，实现了数据库版本自动升级! 其他类型的数据库，您可以考虑手工升级~");
                }
            }
        });
    }
}