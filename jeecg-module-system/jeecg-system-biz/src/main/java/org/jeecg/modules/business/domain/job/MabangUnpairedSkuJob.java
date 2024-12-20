package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.*;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MabangUnpairedSkuJob implements Job {
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

        SkuListRequestBody body = new SkuListRequestBody();
        body.setStockSkuList(String.join(",", skuList));
        SkuListRawStream rawStream = new SkuListRawStream(body);
        UnpairedSkuListStream stream = new UnpairedSkuListStream(rawStream);
        List<SkuData> skusFromMabang = stream.all();
//        System.out.println("skus from mabang : " + skusFromMabang);
    }
}
