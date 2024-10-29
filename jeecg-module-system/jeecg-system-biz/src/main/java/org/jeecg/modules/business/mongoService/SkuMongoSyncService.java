package org.jeecg.modules.business.mongoService;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.base.event.SkuDeclaredValueModifiedEvent;
import org.jeecg.modules.base.event.SkuModifiedEvent;
import org.jeecg.modules.base.event.SkuPriceModifiedEvent;
import org.jeecg.modules.business.entity.SensitiveAttribute;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuDeclaredValue;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.model.SkuDocument;
import org.jeecg.modules.business.service.ISensitiveAttributeService;
import org.jeecg.modules.business.service.ISkuDeclaredValueService;
import org.jeecg.modules.business.service.ISkuPriceService;
import org.jeecg.modules.business.service.ISkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SkuMongoSyncService {
    @Autowired
    private SkuMongoService skuMongoService;
    @Autowired
    private ISkuService skuService;
    @Autowired
    private ISkuDeclaredValueService skuDeclaredValueService;
    @Autowired
    private ISkuPriceService skuPriceService;
    @Autowired
    private ISensitiveAttributeService sensitiveAttributeService;

    /**
     * Listens to an event fired by MyBatisInterceptor in jeecg-boot-base-core
     * /!\ the id in event SHOULD be a sku ID.
     * The condition being if no mapper method uses other criteria to delete a sku.
     * Which should be the case since we never delete an SKU, except if it was created by mistake.
     * @param event contains the sku ID and the operation type (INSERT, UPDATE, DELETE)
     */
    @EventListener
    public void handleSkuModifiedEvent(SkuModifiedEvent event) {
        log.info("Received a SkuModifiedEvent: skuId : {} - operation: {}", event.getSkuId(), event.getOperation());
        String skuId = event.getSkuId();
        String operation = event.getOperation();

        switch (operation) {
            case "INSERT":
            case "UPDATE":
                Sku sku = skuService.getById(skuId);
                SensitiveAttribute sensitiveAttribute = sensitiveAttributeService.getById(sku.getSensitiveAttributeId());
                SkuDocument skuDocument = SkuDocument.builder()
                        .skuId(sku.getId())
                        .erpCode(sku.getErpCode())
                        .enName(sku.getEnName())
                        .zhName(sku.getZhName())
                        .imageSource(sku.getImageSource())
                        .isGift(sku.getIsGift())
                        .sensitiveAttribute(SkuDocument.SensitiveAttribute.builder()
                                .zhName(sensitiveAttribute.getZhName())
                                .enName(sensitiveAttribute.getEnName())
                                .build()
                        )
                        .build();
                skuMongoService.findAndReplace("skuId", skuId, skuDocument);
                break;
            case "DELETE":
                skuMongoService.deleteBySkuId(skuId);
                break;
            default:
                break;
        }
    }

    /**
     * Listens to an event fired by MyBatisInterceptor in jeecg-boot-base-core
     * /!\ the id in event SHOULD be a sku_declared_value ID.
     * The condition being if no mapper method uses other criteria to delete a sku_declared_value
     * @param event contains the sku_declared_value ID and the operation type (INSERT, UPDATE, DELETE)
     */
    @EventListener
    public void handleSkuDeclaredModifiedEvent(SkuDeclaredValueModifiedEvent event) {
        log.info("Received a SkuDeclaredValueModifiedEvent: {}", event);
        String id = event.getId();
        String operation = event.getOperation();

        SkuDeclaredValue sdv = skuDeclaredValueService.getById(id);

        switch (operation) {
            case "INSERT":
            case "UPDATE":
                skuMongoService.upsertSkuDeclaredValue(sdv);
                break;
            case "DELETE":
                skuMongoService.deleteSkuDeclaredValueBySkuId(sdv.getSkuId());
                break;
            default:
                break;
        }
    }

    /**
     * Listens to an event fired by MyBatisInterceptor in jeecg-boot-base-core
     * /!\ the id in event SHOULD be a sku_price ID.
     * The condition being if no mapper method uses other criteria to delete a sku_price
     * @param event contains the sku_price ID and the operation type (INSERT, UPDATE, DELETE)
     */
    @EventListener
    public void handleSkuPriceModifiedEvent(SkuPriceModifiedEvent event) {
        log.info("Received a SkuPriceModifiedEvent: {}", event);
        String id = event.getId();
        String operation = event.getOperation();

        SkuPrice skuPrice = skuPriceService.getById(id);

        switch (operation) {
            case "INSERT":
            case "UPDATE":
                skuMongoService.upsertSkuPrice(skuPrice);
                break;
            case "DELETE":
                skuMongoService.deleteSkuPriceBySkuId(skuPrice.getSkuId());
                break;
            default:
                break;
        }
    }
}
