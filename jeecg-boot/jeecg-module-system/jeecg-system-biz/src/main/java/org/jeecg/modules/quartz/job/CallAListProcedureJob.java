package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.config.LogUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 同步方式调用alist的存储过程
 * 1、更新storages
 * 2、更新meta
 * 3、删除search
 */
@Slf4j
public class CallAListProcedureJob implements Job {
    private static final String dbKey = "jeecg-boot";

    /**
     * 执行存储过程
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Job Execution key：" + jobExecutionContext.getJobDetail().getKey());
        LogUtil.startTime("CallAListProcedureJob");
        try {
            // 同步元信息（作废，直接跳转到IT后台）
            // DynamicDBUtil.execute(dbKey,"call insert_meta()");
            // 删除索引
            DynamicDBUtil.execute(dbKey, "call delete_search_file()");
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
        LogUtil.endTime("CallAListProcedureJob");
    }
}
