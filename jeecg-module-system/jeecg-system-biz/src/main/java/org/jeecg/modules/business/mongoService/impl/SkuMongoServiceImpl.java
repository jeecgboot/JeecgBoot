package org.jeecg.modules.business.mongoService.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.model.SkuDocument;
import org.jeecg.modules.business.mongoRepository.SkuRepository;
import org.jeecg.modules.business.mongoService.SkuMongoService;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.SkuOrderPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SkuMongoServiceImpl implements SkuMongoService {
    @Autowired
    private ISkuService skuService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SkuRepository skuRepository;
    @Autowired
    private ISkuWeightService skuWeightService;
    @Autowired
    private ISkuDeclaredValueService skuDeclaredValueService;
    @Autowired
    private ISkuPriceService skuPriceService;
    @Autowired
    private ICurrencyService currencyService;
    @Autowired
    private ISensitiveAttributeService sensitiveAttributeService;

    @Override
    public List<SkuDocument> findByErpCode(String erpCode) {
        return skuRepository.findByErpCode(erpCode);
    }

    @Override
    public List<SkuDocument> findBySkuId(String skuId) {
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
        Currency currency = currencyService.getById(price.getCurrencyId());
        String currencyCode = currency != null ? currency.getCode() : "UNKNOWN";
        SkuDocument.LatestSkuPrice skuPrice =  SkuDocument.LatestSkuPrice.builder()
                .date(price.getDate())
                .price(price.getPrice())
                .discountedPrice(price.getDiscountedPrice())
                .threshold(price.getThreshold())
                .currencyCode(currencyCode)
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
        List<SkuDocument> skuDocuments = findBySkuId(skuWeight.getSkuId());
        SkuDocument skuDocument;
        if(skuDocuments == null || skuDocuments.isEmpty()) {
            log.error(" SkuDocument with skuId was {} not found.", skuWeight.getSkuId());
            String obsoleteErpCode = skuService.getById(skuWeight.getSkuId()).getErpCode();
            List<SkuDocument> obsoleteDocuments = findByErpCode(obsoleteErpCode);
            if(obsoleteDocuments != null && !obsoleteDocuments.isEmpty()) {
                log.error("Deleting obsolete SkuDocuments with erpCode : {} ", obsoleteErpCode);
                obsoleteDocuments.forEach(obsoleteDocument -> deleteBySkuId(obsoleteDocument.getSkuId()));
            }
            log.info("Pulling Sku from DB to Mongo");
            migrateOneSku(skuService.getById(skuWeight.getSkuId()));
        }
        else if(skuDocuments.size() > 1) {
            log.error("Multiple SkuDocument with skuId was {} found.", skuWeight.getSkuId());
            skuDocuments.forEach(document -> deleteBySkuId(document.getSkuId()));
            log.info("Pulling Sku from DB to Mongo");
            migrateOneSku(skuService.getById(skuWeight.getSkuId()));
        }
        skuDocument = findBySkuId(skuWeight.getSkuId()).get(0);
        Date latestWeightInDB = Optional.ofNullable(skuDocument.getLatestSkuWeight())
                .map(SkuDocument.LatestSkuWeight::getEffectiveDate)
                .orElse(null);
        if(latestWeightInDB != null && latestWeightInDB.toInstant().isAfter(skuWeight.getEffectiveDate().toInstant())) {
            log.error("SKU {} weight was not updated in Mongo document, as the effective date {} is older than the one in the database {}",
                    skuDocument.getErpCode(), skuWeight.getEffectiveDate(), skuDocument.getLatestSkuWeight().getEffectiveDate());
            return;
        }
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

    @Transactional
    @Override
    public void migrateSkuData() {
        List<Sku> skuList = skuService.listSkus();
        log.info("MigrateSkuData: migrating {} skus.", skuList.size());

        int count = 0;
        for(Sku sku : skuList) {
            if(count == 0) {
                log.info("MigrateSkuData progressions: [----------] 0%");
            }
            if(count == skuList.size() / 10) {
                log.info("MigrateSkuData progressions: [█---------] 10%");
            }
            if(count == skuList.size() / 5) {
                log.info("MigrateSkuData progressions: [██--------] 20%");
            }
            if(count == skuList.size() / 10 * 3) {
                log.info("MigrateSkuData progressions: [███-------] 30%");
            }
            if(count == skuList.size() / 10 * 4) {
                log.info("MigrateSkuData progressions: [████------] 40%");
            }
            if(count == skuList.size() / 2) {
                log.info("MigrateSkuData progressions: [█████-----] 50%");
            }
            if(count == skuList.size() / 10 * 6) {
                log.info("MigrateSkuData progressions: [██████----] 60%");
            }
            if(count == skuList.size() / 10 * 7) {
                log.info("MigrateSkuData progressions: [███████---] 70%");
            }
            if(count == skuList.size() / 10 * 8) {
                log.info("MigrateSkuData progressions: [█████████-] 90%");
            }
            if(count == skuList.size() - 1) {
                log.info("MigrateSkuData progressions: [██████████] 100%");
            }
            count++;
            try {
                migrateOneSku(sku);
            } catch (Exception e) {
                log.error("Error while migrating skuId: {}", sku.getId());
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void migrateOneSku(Sku sku) {

        SkuWeight latestWeight = skuWeightService.getBySkuId(sku.getId());
        SkuDeclaredValue latestDeclaredValue = skuDeclaredValueService.getLatestBySkuId(sku.getId());
        SkuPrice latestPrice = skuPriceService.getLatestBySkuId(sku.getId());
        SensitiveAttribute sensitiveAttribute = sensitiveAttributeService.getById(sku.getSensitiveAttributeId());

        if(sensitiveAttribute == null) {
            log.error("SensitiveAttribute not found for skuId: {}", sku.getId());
            throw new RuntimeException("SensitiveAttribute not found for skuId: " + sku.getId());
        }

        SkuDocument skuDocument = SkuDocument.builder()
                .skuId(sku.getId())
                .createBy(sku.getCreateBy())
                .createTime(sku.getCreateTime())
                .updateBy(sku.getUpdateBy())
                .updateTime(sku.getUpdateTime())
                .erpCode(sku.getErpCode())
                .zhName(sku.getZhName())
                .enName(sku.getEnName())
                .invoiceName(sku.getInvoiceName())
                .availableAmount(sku.getAvailableAmount())
                .purchasingAmount(sku.getPurchasingAmount())
                .imageSource(sku.getImageSource())
                .shippingDiscount(sku.getShippingDiscount())
                .serviceFee(sku.getServiceFee())
                .status(sku.getStatus())
                .moq(sku.getMoq())
                .isGift(sku.getIsGift())
                .sensitiveAttribute(SkuDocument.SensitiveAttribute.builder()
                        .zhName(sensitiveAttribute.getZhName())
                        .enName(sensitiveAttribute.getEnName())
                        .build()
                )
                .build();
        if(latestWeight != null) {
            skuDocument.setLatestSkuWeight(SkuDocument.LatestSkuWeight.builder()
                    .weight(latestWeight.getWeight())
                    .effectiveDate(latestWeight.getEffectiveDate())
                    .build()
            );
        }
        if(latestPrice != null) {
            Currency currency = currencyService.getById(latestPrice.getCurrencyId());
            String currencyCode = currency != null ? currency.getCode() : "UNKNOWN";
            skuDocument.setLatestSkuPrice(SkuDocument.LatestSkuPrice.builder()
                    .date(latestPrice.getDate())
                    .price(latestPrice.getPrice())
                    .discountedPrice(latestPrice.getDiscountedPrice())
                    .threshold(latestPrice.getThreshold())
                    .currencyCode(currencyCode)
                    .build()
            );
        }
        if(latestDeclaredValue != null) {
            skuDocument.setLatestDeclaredValue(SkuDocument.LatestDeclaredValue.builder()
                    .declaredValue(latestDeclaredValue.getDeclaredValue())
                    .effectiveDate(latestDeclaredValue.getEffectiveDate())
                    .build()
            );
        }
        findAndReplace("erpCode", sku.getErpCode(), skuDocument);
    }
}
