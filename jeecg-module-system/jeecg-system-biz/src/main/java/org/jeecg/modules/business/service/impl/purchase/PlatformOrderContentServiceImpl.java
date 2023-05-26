package org.jeecg.modules.business.service.impl.purchase;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.mapper.PlatformOrderContentMapper;
import org.jeecg.modules.business.service.IPlatformOrderContentService;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.vo.SkuWeightDiscountServiceFees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PlatformOrderContentServiceImpl extends ServiceImpl<PlatformOrderContentMapper, PlatformOrderContent> implements IPlatformOrderContentService {
    @Autowired
    private PlatformOrderContentMapper platformOrderContentMapper;

    public List<SkuWeightDiscountServiceFees> getAllSKUWeightsDiscountsServiceFees() {
        return platformOrderContentMapper.getAllWeightsDiscountsServiceFees();
    }

    @Override
    public List<SkuQuantity> searchOrderContent(List<String> orderIDList) {
        return platformOrderContentMapper.searchOrderContent(orderIDList);
    }

    @Override
    public BigDecimal calculateWeight(Map<String, Integer> contentMap,
                                      Map<String, BigDecimal> skuRealWeights) throws UserException {
        List<String> skuIDs = new ArrayList<>(contentMap.keySet());
        log.info("skus : " + skuIDs);

        try {

            BigDecimal total = contentMap.entrySet().stream()
                    .map(
                            content ->
                                    (skuRealWeights.get(content.getKey()).multiply(BigDecimal.valueOf(content.getValue())))
                    ).reduce(BigDecimal.ZERO, BigDecimal::add);
            log.info("total weight : " + total);
            return total;
        } catch (NullPointerException e) {
            throw new UserException("Can not find weight for one sku in: " + contentMap);
        }

    }

}
