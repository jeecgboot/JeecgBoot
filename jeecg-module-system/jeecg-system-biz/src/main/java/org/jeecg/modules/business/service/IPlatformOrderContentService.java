package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.vo.SkuWeightDiscountServiceFees;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 平台订单内容
 * @Author: jeecg-boot
 * @Date: 2021-04-08
 * @Version: V1.0
 */
public interface IPlatformOrderContentService extends IService<PlatformOrderContent> {

    /**
     * Calculate weight of a platform order
     *
     * @param contentMap Map of <SKU ID, Quantity>
     * @param skuRealWeights All SKU's real weights
     * @return weight
     */
    BigDecimal calculateWeight(Map<String, Integer> contentMap, Map<String, BigDecimal> skuRealWeights) throws UserException;

    /**
     * Retrieve all SKU weights and discounts
     * @return
     */
    List<SkuWeightDiscountServiceFees> getAllSKUWeightsDiscountsServiceFees();

    List<SkuQuantity> searchOrderContent(List<String> orderIDList);
}
