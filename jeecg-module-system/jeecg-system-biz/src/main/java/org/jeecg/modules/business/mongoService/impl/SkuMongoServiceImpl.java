package org.jeecg.modules.business.mongoService.impl;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.model.Sku;
import org.jeecg.modules.business.mongoRepository.SkuRepository;
import org.jeecg.modules.business.mongoService.SkuMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SkuMongoServiceImpl implements SkuMongoService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SkuRepository skuRepository;

    @Override
    public void updateSkuWeight(String erpCode, Integer weigth) {
        log.info("updateSkuWeight erpCode: {}, weight: {}", erpCode, weigth);
        Query query = new Query(Criteria.where("erpCode").is(erpCode));
        Update update = new Update().set("weight", weigth);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Sku.class);

        if(result == null)
            log.error("updateSkuWeight failed");
        else
            log.info("{} document(s) updated ..", result.getModifiedCount());
    }

    @Override
    public Sku findByErpCode(String erpCode) {
        return skuRepository.findByErpCode(erpCode);
    }

    @Override
    public Sku findBySkuId(String skuId) {
        return skuRepository.findBySkuId(skuId);
    }
}
