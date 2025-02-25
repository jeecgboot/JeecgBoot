package org.jeecg.modules.business.mongoService;

import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuDeclaredValue;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.entity.SkuWeight;
import org.jeecg.modules.business.model.SkuDocument;
import org.jeecg.modules.business.vo.SkuOrderPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SkuMongoService {
    List<SkuDocument> findByErpCode(String erpCode);
    List<SkuDocument> findBySkuId(String skuId);

    void findAndReplace(String field, String value, SkuDocument skuDocument);

    void deleteBySkuId(String skuId);

    void upsertSkuPrice(SkuPrice price);

    void deleteSkuPriceBySkuId(String skuId);

    void upsertSkuDeclaredValue(SkuDeclaredValue skuDeclaredValue);

    void deleteSkuDeclaredValueBySkuId(String skuId);

    void upsertSkuWeight(SkuWeight skuWeight);

    void deleteSkuWeightBySkuId(String skuId);

    void updateStock(Sku sku);

    void updateSkuFromMabangSync(Sku sku);

    List<SkuOrderPage> textSearch(String keywords);

    @Transactional
    void migrateSkuData();

    void migrateOneSku(Sku sku);
}
