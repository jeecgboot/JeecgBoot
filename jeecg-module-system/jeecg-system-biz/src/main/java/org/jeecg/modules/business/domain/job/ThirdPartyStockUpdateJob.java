package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity.SkuStockData;
import org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity.WarehouseStock;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.ThirdPartyStock;
import org.jeecg.modules.business.service.ISkuListMabangService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.service.IThirdPartyStockService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ThirdPartyStockUpdateJob implements Job {
    @Autowired
    private ISkuListMabangService skuListMabangService;
    @Autowired
    private ISkuService skuService;
    @Autowired
    private IThirdPartyStockService thirdPartyStockService;
    private Map<String, List<Sku>> skusByWarehouse = new HashMap<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Third party stock update Job has started.");
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        List<Sku> skus = new ArrayList<>();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if(!jsonObject.isNull("clientCodeByWarehouse")) {
                    JSONArray clientCodeByWarehouseArray = jsonObject.getJSONArray("clientCodeByWarehouse");
                    for (int i = 0; i < clientCodeByWarehouseArray.length(); i++) {
                        JSONObject object = clientCodeByWarehouseArray.getJSONObject((i));
                        if (!object.isNull("clientCode")) {
                            String clientCode = object.getString("clientCode");
                            if (!object.isNull("warehouseName")) {
                                String warehouseName = object.getString("warehouseName");
                                List<Sku> clientSkus = skuService.fetchAllClientActiveSku(clientCode);
                                skus.addAll(clientSkus);
                                skusByWarehouse.put(warehouseName, clientSkus);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        Map<String, List<SkuStockData>> thirdPartyStockFromMabang = skuListMabangService.syncThirdPartyStock(skusByWarehouse);
        List<ThirdPartyStock> thirdPartyStocks = new ArrayList<>();
        Map<String, String> erpCodeToSkuId = skus.stream().collect(Collectors.toMap(Sku::getErpCode, Sku::getId));
        for (Map.Entry<String, List<SkuStockData>> entry : thirdPartyStockFromMabang.entrySet()) {
            String warehouseName = entry.getKey();
            List<SkuStockData> skuStockData = entry.getValue();
            for (SkuStockData skuStockDatum : skuStockData) {
                String erpCode = skuStockDatum.getStockSku();
                WarehouseStock warehouseStock = skuStockDatum.getWarehouseStock(warehouseName);
                Integer quantity = warehouseStock.getAvailableStockQuantity();
                String skuId = erpCodeToSkuId.get(erpCode);
                ThirdPartyStock thirdPartyStock = thirdPartyStockService.getBySkuId(skuId);
                if (thirdPartyStock == null) {
                    thirdPartyStock = new ThirdPartyStock();
                    thirdPartyStock.setSkuId(skuId);
                    thirdPartyStock.setCreateBy("mabang api");
                }
                thirdPartyStock.setUpdateBy("mabang api");
                thirdPartyStock.setQuantity(quantity);
                thirdPartyStock.setWarehouseName(warehouseName);
                thirdPartyStocks.add(thirdPartyStock);
            }
        }

        if(thirdPartyStocks.isEmpty()) {
            return;
        }
        log.info("Updating stock for {} skus.", thirdPartyStocks.size());
        thirdPartyStockService.saveOrUpdateBatch(thirdPartyStocks);
        log.info("Third party stock update Job has ended.");
    }
}
