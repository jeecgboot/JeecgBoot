package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.archive.OrderArchiveJobParam;
import org.jeecg.modules.business.domain.archive.OrderArchiveReport;
import org.jeecg.modules.business.service.IOrderArchiveService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class ArchiveOldPoPocJob implements Job {

    @Autowired
    private IOrderArchiveService orderArchiveService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        OrderArchiveJobParam param = parseParam(context);
        OrderArchiveReport report = orderArchiveService.archiveOldPoPoc(param);
        orderArchiveService.sendReportEmail(report, param.getEmail());
        if ("FAILED".equals(report.getStatus())) {
            throw new JobExecutionException(report.getMessage());
        }
    }

    private OrderArchiveJobParam parseParam(JobExecutionContext context) {
        OrderArchiveJobParam param = new OrderArchiveJobParam();
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = (String) jobDataMap.get("parameter");
        if (parameter == null || parameter.trim().isEmpty()) {
            return param;
        }

        try {
            JSONObject jsonObject = new JSONObject(parameter);
            if (!jsonObject.isNull("archiveBeforeDate")) {
                param.setArchiveBeforeDate(LocalDate.parse(jsonObject.getString("archiveBeforeDate")));
            }
            if (!jsonObject.isNull("batchSize")) {
                param.setBatchSize(jsonObject.getInt("batchSize"));
            }
            if (!jsonObject.isNull("maxBatch")) {
                param.setMaxBatch(jsonObject.getInt("maxBatch"));
            }
            if (!jsonObject.isNull("dryRun")) {
                param.setDryRun(jsonObject.getBoolean("dryRun"));
            }
            if (!jsonObject.isNull("deleteSourceEnabled")) {
                param.setDeleteSourceEnabled(jsonObject.getBoolean("deleteSourceEnabled"));
            }
            if (!jsonObject.isNull("showDetailLog")) {
                param.setShowDetailLog(jsonObject.getBoolean("showDetailLog"));
            }
            if (!jsonObject.isNull("email")) {
                param.setEmail(jsonObject.getString("email"));
            }
        } catch (Exception e) {
            log.error("Invalid ArchiveOldPoPocJob parameter: {}", parameter, e);
            throw new IllegalArgumentException("Invalid ArchiveOldPoPocJob parameter: " + parameter, e);
        }

        return param;
    }
}
