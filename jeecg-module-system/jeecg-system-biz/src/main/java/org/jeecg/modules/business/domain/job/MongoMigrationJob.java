package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.service.MigrationService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MongoMigrationJob implements Job {

    @Autowired
    private MigrationService migrationService;
    @Autowired
    private ISkuService skuService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("MongoMigrationJob start ..");
        List<String> skuList = new ArrayList<>();
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if(!jsonObject.isNull("skus")) {
                    for (int i = 0; i < jsonObject.getJSONArray("skus").length(); i++) {
                        skuList.add(jsonObject.getJSONArray("skus").getString(i));
                    }
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }
        if(skuList.isEmpty()) {
            log.info("Migrating all skus ..");
            migrationService.migrateSkuData();
        }
        else {
            log.info("Migrating skus: {}", skuList);
            for(String erpCode : skuList) {
                Sku sku = skuService.getByErpCode(erpCode);
                migrationService.migrateOneSku(sku);
            }
        }
        log.info("MongoMigrationJob end ..");
    }
}
