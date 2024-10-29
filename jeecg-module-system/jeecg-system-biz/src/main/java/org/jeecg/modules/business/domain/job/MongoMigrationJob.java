package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.service.MigrationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MongoMigrationJob implements Job {

    @Autowired
    private MigrationService migrationService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("MongoMigrationJob start ..");
        migrationService.migrateSkuData();
        log.info("MongoMigrationJob end ..");
    }
}
