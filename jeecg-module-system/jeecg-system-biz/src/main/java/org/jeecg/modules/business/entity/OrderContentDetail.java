package org.jeecg.modules.business.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.service.ISkuPriceService;
import org.jeecg.modules.business.vo.SkuDetail;

import java.math.BigDecimal;


/**
 * This class describes the relation among a sku identified by its id, its quantity,
 * and its correspondent price and promotion.
 */
@Data
@Slf4j
public class OrderContentDetail {

    private final SkuDetail skuDetail;

    private final Integer quantity;

    private final BigDecimal exchangeRate;

    private final ISkuPriceService skuPriceService;

    /**
     * Calculate the reduced amount by applying the promotion to the sku.
     *
     * @return the reduced amount
     */
    public BigDecimal reducedAmount() {
        return skuDetail.getPromotion().calculateDiscountAmount(quantity);
    }

    /**
     * Calculate the total price by applying the price to the sku.
     * Total price = price * quantity
     *
     * @return the total price (price * quantity)
     */
    public BigDecimal totalPrice() {
        BigDecimal unit = skuPriceService.getPrice(skuDetail.getPrice(), quantity, exchangeRate);
        BigDecimal total = unit.multiply(new BigDecimal(quantity));
        log.info("unit: {}", unit);
        log.info("total: {}", total);
        return total;
    }

    public BigDecimal unitPrice(){
        return skuPriceService.getPrice(skuDetail.getPrice(), quantity, exchangeRate);
    }

    public int promotionCount() {
        return skuDetail.getPromotion().promotionCount(quantity);
    }

    @Override
    public String toString() {
        return String.format("%d X %s", quantity, skuDetail);
    }
}
