package org.jeecg.modules.business.service;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.model.SkuDocument;
import org.jeecg.modules.business.mongoRepository.SkuRepository;
import org.jeecg.modules.business.mongoService.SkuMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class MigrationService {
    // MySql
    @Autowired
    ISensitiveAttributeService sensitiveAttributeService;
    @Autowired
    ISkuService skuService;
    @Autowired
    ISkuDeclaredValueService skuDeclaredValueService;
    @Autowired
    ISkuWeightService skuWeightService;
    @Autowired
    ISkuPriceService skuPriceService;
    @Autowired
    SkuMongoService skuMongoService;
    // Mongo
    @Autowired
    private SkuRepository skuRepository;

    @Transactional
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
            skuDocument.setLatestSkuPrice(SkuDocument.LatestSkuPrice.builder()
                    .date(latestPrice.getDate())
                    .price(latestPrice.getPrice())
                    .discountedPrice(latestPrice.getDiscountedPrice())
                    .threshold(latestPrice.getThreshold())
                    .priceRmb(latestPrice.getPriceRmb())
                    .discountedPriceRmb(latestPrice.getDiscountedPriceRmb())
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
        skuMongoService.findAndReplace("skuId", sku.getId(), skuDocument);
    }
}
