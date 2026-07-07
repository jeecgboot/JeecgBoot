package com.xxl.job.admin.business.schedule.route;

import com.xxl.job.admin.business.scheduler.route.strategy.ExecutorRouteConsistentHash;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExecutorRouteConsistentHashTest {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorRouteConsistentHashTest.class);

    @Test
    public void test() {

        List<String> addressList = List.of("192.168.0.1:1111", "192.168.0.2:2222", "192.168.0.3:3333");
        ExecutorRouteConsistentHash executorRouteConsistentHash = new ExecutorRouteConsistentHash();

        for (int i = 0; i < 100; i++) {
            int jobId = 1000 + i;
            String address = executorRouteConsistentHash.hashJob(jobId, addressList);
            logger.info("jobId:{}, address:{}", jobId, address);
        }
    }
}
