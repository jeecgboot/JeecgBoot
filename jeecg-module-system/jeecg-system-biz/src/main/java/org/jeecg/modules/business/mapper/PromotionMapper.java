package org.jeecg.modules.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Promotion;

public interface PromotionMapper {
    /**
     * Find the promotion for a sku indicated by its identifier
     *
     * @param skuId the identifier of the sku with "%" at the beginning and the end.
     * @return the promotion to which it belongs
     */
    Promotion findBySku(@Param("skuId") String skuId);

    /**
     * Update the attribute quantity purchased of a promotion
     *
     * @param p the promotion to update
     */
    void updatePurchasedQuantity(Promotion p);
}
