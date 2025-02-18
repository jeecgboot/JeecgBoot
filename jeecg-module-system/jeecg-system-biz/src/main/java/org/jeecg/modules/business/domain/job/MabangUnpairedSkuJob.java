package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.*;
import org.jeecg.modules.business.service.ISkuListMabangService;
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
public class MabangUnpairedSkuJob implements Job {
    @Autowired
    private ISkuListMabangService skuListMabangService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        List<String> skuList = new ArrayList<>();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("skus")) {
                    for (int i = 0; i < jsonObject.getJSONArray("skus").length(); i++) {
                        skuList.add(jsonObject.getJSONArray("skus").getString(i));
                    }
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        if(skuList.isEmpty()) {
            log.error("No skus provided, exiting job.");
            return;
        }

        List<SkuData> skuDatas = skuListMabangService.fetchUnpairedSkus(skuList);
//        System.out.println("skus from mabang : " + skusFromMabang);
    }
}
