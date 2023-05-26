package org.jeecg.modules.business.vo.clientPlatformOrder.section;


import lombok.Data;
import org.jeecg.modules.business.entity.OrderContentDetail;
import org.jeecg.modules.business.entity.ShippingFeesWaiver;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class is used to display details of multiple orders
 * in the head section of client platform order page
 */
@Data
public class OrdersStatisticData {
    private final int totalQuantity;

    private final int skuNumber;

    private final BigDecimal estimatedTotalPrice;

    private final BigDecimal reducedAmount;

    private final BigDecimal extraShippingFees;

    private OrdersStatisticData(int totalQuantity, int skuNumber, BigDecimal estimatedTotalPrice,
                                BigDecimal reducedAmount, BigDecimal extraShippingFees) {
        this.totalQuantity = totalQuantity;
        this.skuNumber = skuNumber;
        this.estimatedTotalPrice = estimatedTotalPrice;
        this.reducedAmount = reducedAmount;
        this.extraShippingFees = extraShippingFees;
    }

    /**
     * Generated a statisticData object of a list of orderContentDetail.
     *
     * @param source a list of orderContentDetail
     * @param shippingFeesWaiverMap map for SKU and its ShippingFeesWaiver
     * @return a orderStatisticData object
     */
    public static OrdersStatisticData makeData(List<OrderContentDetail> source, Map<ShippingFeesWaiver, List<String>> shippingFeesWaiverMap) {
        int totalQuantity = source.stream().mapToInt(OrderContentDetail::getQuantity).sum();

        int skuNumber = source.size();

        BigDecimal estimatedTotalPrice = BigDecimal.ZERO;
        BigDecimal reducedAmount = BigDecimal.ZERO;

        for (OrderContentDetail d : source) {
            estimatedTotalPrice = estimatedTotalPrice.add(d.totalPrice());
            reducedAmount = reducedAmount.add(d.reducedAmount());
        }

        BigDecimal extraShippingFees = BigDecimal.ZERO;
        Map<ShippingFeesWaiver, Integer> thresholdMap = new HashMap<>();
        if (shippingFeesWaiverMap != null) {
            for (OrderContentDetail orderContentDetail : source) {
                Integer quantity = orderContentDetail.getQuantity();
                if (quantity > 0) {
                    String skuId = orderContentDetail.getSkuDetail().getSkuId();
                    // Find each SKU's waiver (if exists) and decrease the threshold by the SKU's quantity
                    for (Map.Entry<ShippingFeesWaiver, List<String>> entry : shippingFeesWaiverMap.entrySet()) {
                        if (entry.getValue().contains(skuId)) {
                            ShippingFeesWaiver waiver = entry.getKey();
                            if (thresholdMap.containsKey(waiver)) {
                                Integer cumulatedQuantity = thresholdMap.get(waiver);
                                cumulatedQuantity += quantity;
                                thresholdMap.put(waiver, cumulatedQuantity);
                            } else {
                                thresholdMap.put(waiver, quantity);
                            }
                        }
                    }
                }
            }
            Optional<BigDecimal> reduce = thresholdMap.entrySet().stream()
                    .filter(entry -> entry.getKey().getThreshold() > entry.getValue())
                    .map(Map.Entry::getKey)
                    .map(ShippingFeesWaiver::getFees)
                    .reduce(BigDecimal::add);
            extraShippingFees = reduce.orElse(BigDecimal.ZERO);
        }
        return new OrdersStatisticData(totalQuantity, skuNumber, estimatedTotalPrice.add(extraShippingFees), reducedAmount, extraShippingFees);
    }

    public BigDecimal finalAmount() {
        return getEstimatedTotalPrice().subtract(getReducedAmount()).add(getExtraShippingFees());
    }
}
