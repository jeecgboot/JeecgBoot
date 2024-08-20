package org.jeecg.modules.business.mongoRepository;

import org.jeecg.modules.business.model.Sku;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @Description: Mongo Repository for Sku Collection
 * Version: V1.0
 * Date: 2024-08-16
 */
public interface SkuRepository extends MongoRepository<Sku, String> {
    @Query("{'erpCode': ?0}")
    Sku findByErpCode(String erpCode);

    @Query("{'skuId': ?0}")
    Sku findBySkuId(String skuId);

    long count();

}
