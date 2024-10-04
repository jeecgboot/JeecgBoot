package org.jeecg.modules.business.mongoService;

import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuDeclaredValue;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.model.SkuDocument;
import org.jeecg.modules.business.vo.SkuOrderPage;

import java.math.BigDecimal;
import java.util.List;

public interface SkuMongoService {
    void updateSkuWeight(String erpCode, Integer weight);
    SkuDocument findByErpCode(String erpCode);
    SkuDocument findBySkuId(String skuId);

    void findAndReplace(String field, String value, SkuDocument skuDocument);

    void upsertSkuPrice(SkuPrice price);

    void deleteBySkuId(String skuId);

    void upsertSkuDeclaredValue(SkuDeclaredValue skuDeclaredValue);

    void deleteSkuDeclaredValueBySkuId(String skuId);

    void deleteSkuPriceBySkuId(String skuId);

    void updateStock(Sku sku);

    void updateSkuFromMabangSync(Sku sku);

    List<SkuOrderPage> textSearch(String keywords);
}
