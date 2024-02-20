package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.business.domain.api.mabang.purDoGetProvider.*;
import org.jeecg.modules.business.service.IProviderMabangService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Job that retrieves all Providers from Mabang
 */
@Slf4j
@Component
public class MabangGetProviderJob implements Job {

    @Autowired
    private ISysBaseAPI ISysBaseApi;
    @Autowired
    private IProviderMabangService ProviderMabangService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        int flag = 1; // 1: actif, 2: inactif
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("flag")) {
                    flag = Integer.parseInt(jsonObject.getString("flag"));
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        Map<String, ProviderData> providerDataMap = new HashMap<>();
        try {
            ProviderRequestBody body = (new ProviderRequestBody()).setFlag(flag);
            ProviderRawStream rawStream = new ProviderRawStream(body);
            ProviderStream stream = new ProviderStream(rawStream);
            // the status is directly filtered in all() method
            List<ProviderData> providersFromMabang = stream.all();
            providersFromMabang.forEach(providerData -> providerDataMap.put(providerData.getId(), providerData));
            log.info("{} providers to be inserted.", providersFromMabang.size());

            if (providersFromMabang.size() > 0) {
                // we save the skuDatas in DB
                ProviderMabangService.saveProviderFromMabang(providersFromMabang);
            }
        } catch (ProviderRequestErrorException e) {
            throw new RuntimeException(e);
        }
    }

}