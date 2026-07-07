package com.xxl.job.admin.business.schedule;

import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.tool.core.DateTool;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class JobScheduleTest {
    private static Logger logger = LoggerFactory.getLogger(JobScheduleTest.class);

    @Test
    public void test() throws InterruptedException {

        // thread
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                lockTest("threadName-" + finalI);
            }).start();
        }

        TimeUnit.MINUTES.sleep(10);
    }

    private void lockTest(String threadName) {

        TransactionStatus transactionStatus = XxlJobAdminBootstrap.getInstance().getTransactionManager().getTransaction(new DefaultTransactionDefinition());
        try {
            String lockedRecord = XxlJobAdminBootstrap.getInstance().getXxlJobLockMapper().scheduleLock(); // for update

            logger.info(threadName + " : start at " + DateTool.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS") );
            TimeUnit.MILLISECONDS.sleep(500);
            logger.info(threadName + " : end at " + DateTool.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS") );
        } catch (Throwable e) {
            logger.error("error: ",  e);
        } finally {
            logger.info(threadName + " : commit at " + DateTool.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS") );
            XxlJobAdminBootstrap.getInstance().getTransactionManager().commit(transactionStatus);
        }
    }
}
