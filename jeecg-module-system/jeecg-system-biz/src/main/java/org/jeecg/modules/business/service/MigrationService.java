package org.jeecg.modules.business.service;

import org.jeecg.modules.business.model.Sku;
import org.jeecg.modules.business.mongoService.SkuMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MigrationService {
    // MySql
    @Autowired
    ISkuService skuService;

    // Mongo
    @Autowired
    private SkuMongoService mongoSkuService;

    public void migrateSkuData() {
        List<Sku> skuList = skuService.listAsMongoCollection();
    }
}
