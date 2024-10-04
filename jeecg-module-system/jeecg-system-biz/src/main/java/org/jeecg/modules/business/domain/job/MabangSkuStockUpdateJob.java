package org.jeecg.modules.business.domain.job;

import com.amazonaws.services.dynamodbv2.xspec.S;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity.SkuStockData;
import org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity.SkuStockRawStream;
import org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity.SkuStockRequestBody;
import org.jeecg.modules.business.domain.api.mabang.stockGetStockQuantity.SkuStockStream;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.mongoService.SkuMongoService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.service.MigrationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Component
public class MabangSkuStockUpdateJob implements Job {
    @Autowired
    private ISkuService skuService;
    @Autowired
    private SkuMongoService skuMongoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<String> skuList = skuService.listSkus().stream().map(Sku::getErpCode).collect(Collectors.toList());
        StringBuilder skus = new StringBuilder();
        List<SkuStockData> updateList = new ArrayList<>();
        List<Sku> skuToUpdate = new ArrayList<>();
        log.info("Sku stock update Job has started.");
        int count = 1;
        for(int i = 1; i <= skuList.size(); i++) {
            if(i%100 != 1)
                skus.append(",");
            skus.append(skuList.get(i - 1));
            if(i%100 == 0) {
                SkuStockRequestBody body = (new SkuStockRequestBody())
                        .setStockSkus(skus.toString())
                        .setTotal(skuList.size());
                log.info("Sending request for page {}/{}.", count++, body.getTotalPages());

                SkuStockRawStream rawStream = new SkuStockRawStream(body);
                SkuStockStream stream = new SkuStockStream(rawStream);
                updateList.addAll(stream.all());
                skus = new StringBuilder();
            }
        }
        if(skus.length() != 0) {
            SkuStockRequestBody body = (new SkuStockRequestBody())
                    .setStockSkus(skus.toString())
                    .setTotal(skuList.size());
            SkuStockRawStream rawStream = new SkuStockRawStream(body);
            SkuStockStream stream = new SkuStockStream(rawStream);
            updateList.addAll(stream.all());
        }
        updateList.forEach(skuStockData -> {
            Sku sku = skuService.getByErpCode(skuStockData.getStockSku());
            Integer availableAmount = skuStockData.getWarehouseStock("SZBA宝安仓").getStockQuantity();
            Integer purchasingAmount = skuStockData.getWarehouseStock("SZBA宝安仓").getShippingQuantity();
            if(sku.getAvailableAmount().equals(availableAmount) && sku.getPurchasingAmount().equals(purchasingAmount)) {
                return;
            }
            sku.setAvailableAmount(availableAmount);
            sku.setPurchasingAmount(purchasingAmount);
            sku.setUpdateBy("mabang api");
            sku.setUpdateTime(new Date());
            skuToUpdate.add(sku);
        });
        if(skuToUpdate.isEmpty()) {
            return;
        }
        log.info("Updating stock for {} skus.", skuToUpdate.size());
        skuService.updateBatchStockByIds(skuToUpdate);
        for(Sku sku : skuToUpdate) {
            skuMongoService.updateStock(sku);
        }
        log.info("Sku stock update Job has ended.");
    }
}
