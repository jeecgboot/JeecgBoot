package org.jeecg.modules.business.mongoRepository;

import org.jeecg.modules.business.model.SkuDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @Description: Mongo Repository for Sku Collection
 * Version: V1.0
 * Date: 2024-08-16
 */
public interface SkuRepository extends MongoRepository<SkuDocument, String> {
    @Query("{'erpCode': ?0}")
    SkuDocument findByErpCode(String erpCode);

    @Query("{'skuId': ?0}")
    SkuDocument findBySkuId(String skuId);

    long count();

}
