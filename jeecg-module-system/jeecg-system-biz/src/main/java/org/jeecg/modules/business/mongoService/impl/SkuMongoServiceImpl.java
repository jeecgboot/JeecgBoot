package org.jeecg.modules.business.mongoService.impl;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuDeclaredValue;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.entity.SkuWeight;
import org.jeecg.modules.business.model.SkuDocument;
import org.jeecg.modules.business.mongoRepository.SkuRepository;
import org.jeecg.modules.business.mongoService.SkuMongoService;
import org.jeecg.modules.business.vo.SkuOrderPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SkuMongoServiceImpl implements SkuMongoService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SkuRepository skuRepository;

    @Override
    public SkuDocument findByErpCode(String erpCode) {
        return skuRepository.findByErpCode(erpCode);
    }

    @Override
    public SkuDocument findBySkuId(String skuId) {
        return skuRepository.findBySkuId(skuId);
    }

    @Override
    public void findAndReplace(String field, String value, SkuDocument skuDocument) {
        Query query = new Query(Criteria.where(field).is(value));
        mongoTemplate.update(SkuDocument.class)
                .matching(query)
                .replaceWith(skuDocument)
                .withOptions(FindAndReplaceOptions.options().upsert())
                .as(SkuDocument.class)
                .findAndReplace();
    }

    @Override
    public void deleteBySkuId(String skuId) {
        Query query = new Query(Criteria.where("skuId").is(skuId));
        mongoTemplate.findAndRemove(query, SkuDocument.class);
    }

    @Override
    public void upsertSkuPrice(SkuPrice price) {
        Query query = new Query(Criteria.where("skuId").is(price.getSkuId()));
        if(!mongoTemplate.exists(query, SkuDocument.class)) {
            log.error("Cannot insert or update SkuPrice of Sku Mongo Document, SkuDocument with skuId was {} not found.", price.getSkuId());
            throw new RuntimeException("SkuDocument with skuId: " + price.getSkuId() + " not found");
        }
        SkuDocument.LatestSkuPrice skuPrice =  SkuDocument.LatestSkuPrice.builder()
                .date(price.getDate())
                .price(price.getPrice())
                .discountedPrice(price.getDiscountedPrice())
                .threshold(price.getThreshold())
                .priceRmb(price.getPriceRmb())
                .discountedPriceRmb(price.getDiscountedPriceRmb())
                .build();

        mongoTemplate.update(SkuDocument.class)
                .matching(query)
                .apply(new Update()
                        .set("latestSkuPrice", skuPrice)
                        .set("updateTime", price.getUpdateTime())
                        .set("updateBy", price.getUpdateBy())
                )
                .withOptions(FindAndModifyOptions.options().upsert(true))
                .findAndModifyValue();
    }

    @Override
    public void deleteSkuPriceBySkuId(String skuId) {
        Query query = new Query(Criteria.where("skuId").is(skuId));
        mongoTemplate.update(SkuDocument.class)
                .matching(query)
                .apply(new Update()
                        .unset("latestSkuPrice")
                )
                .findAndModifyValue();
    }

    @Override
    public void upsertSkuDeclaredValue(SkuDeclaredValue skuDeclaredValue) {
        Query query = new Query(Criteria.where("skuId").is(skuDeclaredValue.getSkuId()));
        SkuDocument.LatestDeclaredValue declaredValue = SkuDocument.LatestDeclaredValue.builder()
                .declaredValue(skuDeclaredValue.getDeclaredValue())
                .effectiveDate(skuDeclaredValue.getEffectiveDate())
                .build();
        mongoTemplate.update(SkuDocument.class)
                .matching(query)
                .apply(new Update()
                        .set("latestDeclaredValue", declaredValue)
                        .set("updateTime", skuDeclaredValue.getUpdateTime())
                        .set("updateBy", skuDeclaredValue.getUpdateBy())
                )
                .withOptions(FindAndModifyOptions.options().upsert(true))
                .findAndModifyValue();
    }

    @Override
    public void deleteSkuDeclaredValueBySkuId(String skuId) {
        Query query = new Query(Criteria.where("skuId").is(skuId));
        mongoTemplate.update(SkuDocument.class)
                .matching(query)
                .apply(new Update()
                        .unset("latestDeclaredValue")
                )
                .findAndModifyValue();
    }

    @Override
    public void upsertSkuWeight(SkuWeight skuWeight) {
        Query query = new Query(Criteria.where("skuId").is(skuWeight.getSkuId()));
        SkuDocument.LatestSkuWeight latestSkuWeight = SkuDocument.LatestSkuWeight.builder()
                .weight(skuWeight.getWeight())
                .effectiveDate(skuWeight.getEffectiveDate())
                .build();
        mongoTemplate.update(SkuDocument.class)
                .matching(query)
                .apply(new Update()
                        .set("latestSkuWeight", latestSkuWeight)
                        .set("updateTime", skuWeight.getCreateTime())
                        .set("updateBy", skuWeight.getCreateBy())
                )
                .withOptions(FindAndModifyOptions.options().upsert(true))
                .findAndModifyValue();
    }

    @Override
    public void deleteSkuWeightBySkuId(String skuId) {
        Query query = new Query(Criteria.where("skuId").is(skuId));
        mongoTemplate.update(SkuDocument.class)
                .matching(query)
                .apply(new Update()
                        .unset("latestSkuWeight")
                )
                .findAndModifyValue();
    }
    @Override
    public void updateStock(Sku sku) {
        Query query = new Query(Criteria.where("skuId").is(sku.getId()));
        mongoTemplate.update(SkuDocument.class)
                .matching(query)
                .apply(new Update()
                        .set("updateTime", sku.getUpdateTime())
                        .set("updateBy", sku.getUpdateBy())
                        .set("availableAmount", sku.getAvailableAmount())
                        .set("purchasingAmount", sku.getPurchasingAmount())
                )
                .findAndModifyValue();
    }

    /**
     * Update Sku status, ifGift, zhName and enName from Mabang Sync
     * @param sku updated Sku
     */
    @Override
    public void updateSkuFromMabangSync(Sku sku) {
        Query query = new Query(Criteria.where("skuId").is(sku.getId()));
        mongoTemplate.update(SkuDocument.class)
                .matching(query)
                .apply(new Update()
                        .set("status", sku.getStatus())
                        .set("updateTime", sku.getUpdateTime())
                        .set("updateBy", sku.getUpdateBy())
                        .set("isGift", sku.getIsGift())
                        .set("zhName", sku.getZhName())
                        .set("enName", sku.getEnName())
                )
                .findAndModifyValue();
    }

    /**
     * Note : in other to perform text search, we need to create a text index on the fields we want to search
     * via mongo shell: db.sku.createIndex({zhName: "text", enName: "text", erpCode: "text"})
     * @param keywords list of keywords to search for in different fields
     * @return List<SkuOrderPage> list of SkuOrderPage
     */
    @Override
    public List<SkuOrderPage> textSearch(String keywords) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage()
                .matchingAny(keywords.split(" "));
        Query query = TextQuery.queryText(criteria)
                .sortByScore();
//                .with(PageRequest.of(0, 5));
        List<SkuDocument> skuOrderPages = mongoTemplate.find(query, SkuDocument.class);
        return skuOrderPages.stream()
                .map(this::convertSkuDocumentToSkuOrderPage).collect(Collectors.toList());
    }

    public SkuOrderPage convertSkuDocumentToSkuOrderPage(SkuDocument skuDocument) {
        SkuOrderPage skuOrderPage = new SkuOrderPage();
        skuOrderPage.setId(skuDocument.getSkuId());
            skuOrderPage.setErpCode(skuDocument.getErpCode());
            skuOrderPage.setZhName(skuDocument.getZhName());
            skuOrderPage.setEnName(skuDocument.getEnName());
            skuOrderPage.setWeightEffectiveDate(Optional.ofNullable(skuDocument.getLatestSkuWeight())
                    .map(SkuDocument.LatestSkuWeight::getEffectiveDate)
                    .orElse(null));
            skuOrderPage.setWeight(Optional.ofNullable(skuDocument.getLatestSkuWeight())
                    .map(SkuDocument.LatestSkuWeight::getWeight)
                    .orElse(null));
            skuOrderPage.setAvailableAmount(skuDocument.getAvailableAmount());
            skuOrderPage.setPurchasingAmount(skuDocument.getPurchasingAmount());
            skuOrderPage.setImageSource(skuDocument.getImageSource());
            skuOrderPage.setShippingDiscount(skuDocument.getShippingDiscount());
            skuOrderPage.setServiceFee(skuDocument.getServiceFee());
            skuOrderPage.setStatus(skuDocument.getStatus());
            skuOrderPage.setMoq(skuDocument.getMoq());
            skuOrderPage.setSensitiveAttribute(Optional.ofNullable(skuDocument.getSensitiveAttribute())
                    .map(SkuDocument.SensitiveAttribute::getZhName)
                    .orElse(null));
            skuOrderPage.setIsGift(skuDocument.getIsGift());
            skuOrderPage.setSkuPrice(Optional.ofNullable(skuDocument.getLatestSkuPrice())
                    .map(SkuDocument.LatestSkuPrice::getPrice)
                    .orElse(null));
            skuOrderPage.setSkuPriceEffectiveDate(Optional.ofNullable(skuDocument.getLatestSkuPrice())
                    .map(SkuDocument.LatestSkuPrice::getDate)
                    .orElse(null));
            skuOrderPage.setDiscountedPrice(Optional.ofNullable(skuDocument.getLatestSkuPrice())
                    .map(SkuDocument.LatestSkuPrice::getDiscountedPrice)
                    .orElse(null));
            skuOrderPage.setDeclaredValue(
                    Optional.ofNullable(skuDocument.getLatestDeclaredValue())
                            .map(SkuDocument.LatestDeclaredValue::getDeclaredValue)
                            .orElse(null));
            skuOrderPage.setDeclaredValueEffectiveDate(
                    Optional.ofNullable(skuDocument.getLatestDeclaredValue())
                            .map(SkuDocument.LatestDeclaredValue::getEffectiveDate)
                            .orElse(null));
        return skuOrderPage;
    }
}
