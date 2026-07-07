package com.xxl.job.admin.business.scheduler.config;

import com.xxl.job.admin.business.mapper.*;
import com.xxl.job.admin.business.scheduler.alarm.JobAlarmer;
import com.xxl.job.admin.business.scheduler.complete.JobCompleter;
import com.xxl.job.admin.business.scheduler.thread.*;
import com.xxl.job.admin.business.scheduler.trigger.JobTrigger;
import com.xxl.job.core.constant.Const;
import com.xxl.job.core.openapi.ExecutorBiz;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.http.HttpTool;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */

@Component
public class XxlJobAdminBootstrap implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobAdminBootstrap.class);

    // ---------------------- instance ----------------------

    private static XxlJobAdminBootstrap adminConfig = null;
    public static XxlJobAdminBootstrap getInstance() {
        return adminConfig;
    }


    // ---------------------- start / stop ----------------------

    @Override
    public void afterPropertiesSet() throws Exception {
        // init instance
        adminConfig = this;

        // start
        doStart();
    }

    @Override
    public void destroy() throws Exception {
        // stop
        doStop();
    }

    // job module
    private JobTriggerPoolHelper jobTriggerPoolHelper;
    private JobRegistryHelper jobRegistryHelper;
    private JobFailAlarmMonitorHelper jobFailAlarmMonitorHelper;
    private JobCompleteHelper jobCompleteHelper;
    private JobLogReportHelper jobLogReportHelper;
    private JobScheduleHelper jobScheduleHelper;

    public JobTriggerPoolHelper getJobTriggerPoolHelper() {
        return jobTriggerPoolHelper;
    }
    public JobRegistryHelper getJobRegistryHelper() {
        return jobRegistryHelper;
    }
    public JobCompleteHelper getJobCompleteHelper() {
        return jobCompleteHelper;
    }

    /**
     * do start
     */
    private void doStart() throws Exception {
        // trigger-pool start
        jobTriggerPoolHelper = new JobTriggerPoolHelper();
        jobTriggerPoolHelper.start();

        // registry monitor start
        jobRegistryHelper = new JobRegistryHelper();
        jobRegistryHelper.start();

        // fail-alarm monitor start
        jobFailAlarmMonitorHelper = new JobFailAlarmMonitorHelper();
        jobFailAlarmMonitorHelper.start();

        // job complate start  ( depend on JobTriggerPoolHelper ) for callback and result-lost
        jobCompleteHelper = new JobCompleteHelper();
        jobCompleteHelper.start();

        // log-report start
        jobLogReportHelper = new JobLogReportHelper();
        jobLogReportHelper.start();

        // job-schedule start  ( depend on JobTriggerPoolHelper )
        jobScheduleHelper = new JobScheduleHelper();
        jobScheduleHelper.start();

        logger.info(">>>>>>>>> xxl-job admin start success.");
    }

    /**
     * do stop
     */
    private void doStop(){
        // job-schedule stop
        jobScheduleHelper.stop();

        // log-report stop
        jobLogReportHelper.stop();

        // job complate stop
        jobCompleteHelper.stop();

        // fail-alarm monitor stop
        jobFailAlarmMonitorHelper.stop();

        // registry monitor stop
        jobRegistryHelper.stop();

        // trigger-pool stop
        jobTriggerPoolHelper.stop();

        logger.info(">>>>>>>>> xxl-job admin stopped.");
    }


    // ---------------------- executor-client ----------------------

    private static ConcurrentMap<String, ExecutorBiz> executorBizRepository = new ConcurrentHashMap<String, ExecutorBiz>();
    public static ExecutorBiz getExecutorBiz(String address) throws Exception {
        // valid
        if (StringTool.isBlank(address)) {
            return null;
        }

        // load-cache
        address = address.trim();
        ExecutorBiz executorBiz = executorBizRepository.get(address);
        if (executorBiz != null) {
            return executorBiz;
        }

        // set-cache
        executorBiz = HttpTool.createClient()
                .url(address)
                .timeout(XxlJobAdminBootstrap.getInstance().getTimeout() * 1000)
                .header(Const.XXL_JOB_ACCESS_TOKEN, XxlJobAdminBootstrap.getInstance().getAccessToken())
                .proxy(ExecutorBiz.class);
        executorBizRepository.put(address, executorBiz);
        return executorBiz;
    }


    // ---------------------- field ----------------------

    // conf
    @Value("${xxl.job.i18n}")
    private String i18n;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.timeout}")
    private int timeout;

    @Value("${spring.mail.from}")
    private String emailFrom;

    @Value("${xxl.job.triggerpool.fast.max}")
    private int triggerPoolFastMax;

    @Value("${xxl.job.triggerpool.slow.max}")
    private int triggerPoolSlowMax;

    @Value("${xxl.job.schedule.batchsize}")
    private int scheduleBatchSize;

    @Value("${xxl.job.logretentiondays}")
    private int logretentiondays;

    // service, mapper
    @Resource
    private XxlJobLogMapper xxlJobLogMapper;
    @Resource
    private XxlJobInfoMapper xxlJobInfoMapper;
    @Resource
    private XxlJobRegistryMapper xxlJobRegistryMapper;
    @Resource
    private XxlJobGroupMapper xxlJobGroupMapper;
    @Resource
    private XxlJobLogReportMapper xxlJobLogReportMapper;
    @Resource
    private XxlJobLockMapper xxlJobLockMapper;
    @Resource
    private JavaMailSender mailSender;
    /*@Resource
    private DataSource dataSource;*/
    @Resource
    private PlatformTransactionManager transactionManager;
    @Resource
    private JobAlarmer jobAlarmer;
    @Resource
    private JobTrigger jobTrigger;
    @Resource
    private JobCompleter jobCompleter;


    public String getI18n() {
        if (!Arrays.asList("zh_CN", "zh_TC", "en").contains(i18n)) {
            return "zh_CN";
        }
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public int getTriggerPoolFastMax() {
        if (triggerPoolFastMax < 200) {
            return 200;
        }
        return triggerPoolFastMax;
    }

    public int getTriggerPoolSlowMax() {
        if (triggerPoolSlowMax < 100) {
            return 100;
        }
        return triggerPoolSlowMax;
    }

    public int getScheduleBatchSize() {
        if (!(scheduleBatchSize >=50 && scheduleBatchSize <= 500)) {
            return 100;
        }
        return scheduleBatchSize;
    }

    public int getLogretentiondays() {
        if (logretentiondays < 3) {
            return -1;  // Limit greater than or equal to 3, otherwise close
        }
        return logretentiondays;
    }

    public XxlJobLogMapper getXxlJobLogMapper() {
        return xxlJobLogMapper;
    }

    public XxlJobInfoMapper getXxlJobInfoMapper() {
        return xxlJobInfoMapper;
    }

    public XxlJobRegistryMapper getXxlJobRegistryMapper() {
        return xxlJobRegistryMapper;
    }

    public XxlJobGroupMapper getXxlJobGroupMapper() {
        return xxlJobGroupMapper;
    }

    public XxlJobLogReportMapper getXxlJobLogReportMapper() {
        return xxlJobLogReportMapper;
    }

    public XxlJobLockMapper getXxlJobLockMapper() {
        return xxlJobLockMapper;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    /*public DataSource getDataSource() {
        return dataSource;
    }*/

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public JobAlarmer getJobAlarmer() {
        return jobAlarmer;
    }

    public JobTrigger getJobTrigger() {
        return jobTrigger;
    }

    public JobCompleter getJobCompleter() {
        return jobCompleter;
    }

}
