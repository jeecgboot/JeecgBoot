package org.jeecg.config.init;

import com.alibaba.druid.filter.config.ConfigTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.codegenerate.database.CodegenDatasourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 代码生成器,自定义DB配置
 * 【加了此类，则online模式DB连接，使用平台的配置，jeecg_database.properties配置无效;
 *  但是使用GUI模式代码生成，还是走jeecg_database.properties配置】
 *  提醒： 达梦数据库需要修改下面的参数${spring.datasource.dynamic.datasource.master.url:}配置
 * @author: scott
 * @date: 2021年02月18日 16:30
 */
@Slf4j
@Configuration
public class CodeGenerateDbConfig {
    @Value("${spring.datasource.dynamic.datasource.master.url:}")
    private String url;
    @Value("${spring.datasource.dynamic.datasource.master.username:}")
    private String username;
    @Value("${spring.datasource.dynamic.datasource.master.password:}")
    private String password;
    @Value("${spring.datasource.dynamic.datasource.master.driver-class-name:}")
    private String driverClassName;
    @Value("${spring.datasource.dynamic.datasource.master.druid.public-key:}")
    private String publicKey;


    @Bean
    public CodeGenerateDbConfig initCodeGenerateDbConfig() {
        if(StringUtils.isNotBlank(url)){
            if(StringUtils.isNotBlank(publicKey)){
                try {
                    password = ConfigTools.decrypt(publicKey, password);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(" 代码生成器数据库连接，数据库密码解密失败！");
                }
            }
            CodegenDatasourceConfig.initDbConfig(driverClassName,url, username, password);
            log.info(" Init CodeGenerate Config [ Get Db Config From application.yml ] ");
        }
        return null;
    }
}
