package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.service.ISkuListMabangService;
import org.jeecg.modules.business.service.ISkuService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Component
public class MabangSkuStockUpdateJob implements Job {
    @Autowired
    private ISkuListMabangService skuListMabangService;
    @Autowired
    private ISkuService skuService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Sku stock update Job has started.");
        List<String> erpCodes = skuService.listSkus().stream().map(Sku::getErpCode).collect(Collectors.toList());
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("skus")) {
                    JSONArray array = jsonObject.getJSONArray("skus");
                    for(int i = 0; i < array.length(); i++) {
                        erpCodes.add(array.getString(i));
                    }
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }
        skuListMabangService.mabangSkuStockUpdate(erpCodes);
        log.info("Sku stock update Job has ended.");
    }
}
