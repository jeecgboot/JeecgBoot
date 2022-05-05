package org.jeecg.boot.starter.job.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.job.prop.XxlJobProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务配置
 *
 * @author jeecg
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(value = XxlJobProperties.class)
@ConditionalOnProperty(value = "jeecg.xxljob.enabled", havingValue = "true", matchIfMissing = true)
public class XxlJobConfiguration {


    @Autowired
    private XxlJobProperties xxlJobProperties;

    //@Bean(initMethod = "start", destroyMethod = "destroy")
    @Bean
    @ConditionalOnClass()
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        //log.info(">>>> ip="+xxlJobProperties.getIp()+"，Port="+xxlJobProperties.getPort()+"，address="+xxlJobProperties.getAdminAddresses());
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppname());
        //update-begin--Author:scott -- Date:20210305 -- for：system服务和demo服务有办法同时使用xxl-job吗 #2313---
        //xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
        //xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
        //update-end--Author:scott -- Date:20210305 -- for：system服务和demo服务有办法同时使用xxl-job吗 #2313---
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
        return xxlJobSpringExecutor;
    }


}
