package com.xxl.job.admin.business.scheduler.alarm;

import com.xxl.job.admin.business.model.XxlJobInfo;
import com.xxl.job.admin.business.model.XxlJobLog;
import com.xxl.tool.core.CollectionTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * xxl-job alarmer
 *
 * @author xuxueli 17/7/13.
 */
@Component
public class JobAlarmer {
    private static final Logger logger = LoggerFactory.getLogger(JobAlarmer.class);

    @Autowired
    private List<JobAlarm> jobAlarmList;

    /**
     * job alarm
     */
    public boolean alarm(XxlJobInfo info, XxlJobLog jobLog) {

        boolean result = false;
        if (CollectionTool.isNotEmpty(jobAlarmList)) {
            result = true;  // success means all-success
            for (JobAlarm alarm: jobAlarmList) {
                boolean resultItem = false;
                try {
                    resultItem = alarm.doAlarm(info, jobLog);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                if (!resultItem) {
                    result = false;
                }
            }
        }

        return result;
    }

    /*
    // implements SmartInitializingSingleton
    // implements ApplicationContextAware, InitializingBean
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, JobAlarm> serviceBeanMap = applicationContext.getBeansOfType(JobAlarm.class);
        if (MapTool.isNotEmpty(serviceBeanMap)) {
            jobAlarmList = new ArrayList<>(serviceBeanMap.values());
        }
    }*/

}
