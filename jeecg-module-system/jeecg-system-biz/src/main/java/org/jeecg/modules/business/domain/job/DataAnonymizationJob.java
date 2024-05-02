package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.service.IClientService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Data Anonymization job, following EU GDPR guidelines.
 * This job is responsible for anonymizing personal data in the database.
 * It is scheduled to run every day at XX.
 * The job will anonymize personal data in the database, such as names, addresses, and phone numbers.
 * It will replace the personal data with UUIDs to ensure that the data is no longer identifiable.
 * Personal data will be anonymized after 3 years of inactivity for our direct clients.
 * And 2 years after creation for our indirect clients.
 * ---
 * Job frequency : 1 month
 */

@Slf4j
public class DataAnonymizationJob implements Job {
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private IClientService clientService;

    private final static int DIRECT_CLIENT_ANONYMIZATION_PERIOD = 3;
    private final static int INDIRECT_CLIENT_ANONYMIZATION_PERIOD = 2;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Data Anonymization job is running...");
        log.info("Anonymizing personal data for indirect clients...");
        platformOrderService.anonymizePersonalData(INDIRECT_CLIENT_ANONYMIZATION_PERIOD);
        log.info("Anonymizing personal data for direct clients...");
        clientService.anonymizePersonalData(DIRECT_CLIENT_ANONYMIZATION_PERIOD);
        log.info("Data Anonymization job completed.");
    }
}
