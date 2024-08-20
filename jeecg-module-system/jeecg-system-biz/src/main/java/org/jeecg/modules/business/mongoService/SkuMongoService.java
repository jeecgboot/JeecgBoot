package org.jeecg.modules.business.mongoService;

import org.jeecg.modules.business.model.Sku;

public interface SkuMongoService {
    void updateSkuWeight(String erpCode, Integer weigth);
    Sku findByErpCode(String erpCode);
    Sku findBySkuId(String skuId);
}
