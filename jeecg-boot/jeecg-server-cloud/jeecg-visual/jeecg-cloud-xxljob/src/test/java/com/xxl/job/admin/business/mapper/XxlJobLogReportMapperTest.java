package com.xxl.job.admin.business.mapper;

import com.xxl.job.admin.business.model.XxlJobLogReport;
import com.xxl.tool.core.DateTool;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XxlJobLogReportMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobLogMapperTest.class);

    @Resource
    private XxlJobLogReportMapper xxlJobLogReportMapper;

    @Test
    public void test(){

        Date date = DateTool.parseDate("2025-10-01");

        XxlJobLogReport xxlJobLogReport = new XxlJobLogReport();
        xxlJobLogReport.setTriggerDay(date);
        xxlJobLogReport.setRunningCount(444);
        xxlJobLogReport.setSucCount(555);
        xxlJobLogReport.setFailCount(666);
        xxlJobLogReport.setUpdateTime(new Date());

        int ret = xxlJobLogReportMapper.saveOrUpdate(xxlJobLogReport);
        logger.info("ret:{}", ret);
    }
}
