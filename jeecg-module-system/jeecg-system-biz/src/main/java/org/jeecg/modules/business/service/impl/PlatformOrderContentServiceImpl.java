package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.mapper.PlatformOrderContentMapper;
import org.jeecg.modules.business.mapper.SkuMapper;
import org.jeecg.modules.business.service.IPlatformOrderContentService;
import org.jeecg.modules.business.vo.Response;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.vo.SkuWeightDiscountServiceFees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class PlatformOrderContentServiceImpl extends ServiceImpl<PlatformOrderContentMapper, PlatformOrderContent> implements IPlatformOrderContentService {
    @Autowired
    private final PlatformOrderContentMapper platformOrderContentMapper;
    @Autowired
    private SkuMapper skuMapper;

    public PlatformOrderContentServiceImpl(PlatformOrderContentMapper platformOrderContentMapper) {
        this.platformOrderContentMapper = platformOrderContentMapper;
    }
    public List<SkuWeightDiscountServiceFees> getAllSKUWeightsDiscountsServiceFees() {
        return platformOrderContentMapper.getAllWeightsDiscountsServiceFees();
    }

    @Override
    public List<SkuQuantity> searchOrderContent(List<String> orderIDList) {
        return platformOrderContentMapper.searchOrderContent(orderIDList);
    }

    @Override
    public Response<BigDecimal, List<String>> calculateWeight(Map<String, Integer> contentMap,
                                                          Map<String, BigDecimal> skuRealWeights)
    {
        Response<BigDecimal, List<String>> response = new Response<>();
        List<String> errorMessages = new ArrayList<>();
        List<String> skuIDs = new ArrayList<>(contentMap.keySet());
        log.info("skus : " + skuIDs);

        BigDecimal total = BigDecimal.ZERO;
        List<String> missingIds = new ArrayList<>();
        for(Map.Entry<String, Integer> entry: contentMap.entrySet()) {
            String skuId = entry.getKey();
            Integer qty = entry.getValue();
            BigDecimal w = skuRealWeights.get(skuId);
            if(w == null) {
                missingIds.add(skuId);
                continue;
            }
            total = total.add(w.multiply(BigDecimal.valueOf(qty)));
            if (!missingIds.isEmpty()) {
                List<String> erpCodes = skuMapper.listErpCodesByIds(missingIds);
                if (erpCodes != null && !erpCodes.isEmpty()) {
                    errorMessages.add("SKUs missing weight: " + String.join(", ", erpCodes));
                } else {
                    errorMessages.add("SKUs missing weight (id): " + String.join(", ", missingIds));
                }
            }
        }
        log.info("total weight : " + total);
        response.setData(total);
        response.setError(errorMessages);
        return response;

    }
    @Override
    public List<PlatformOrderContent> fetchPlatformOrderContentsToArchive(List<String> orderIDs) {
        return platformOrderContentMapper.fetchPlatformOrderContentsToArchive(orderIDs);
    }
    public void savePlatformOrderContentArchive(List<PlatformOrderContent> platformOrderContents) {
        platformOrderContentMapper.insertPlatformOrderContentsArchives(platformOrderContents);
    }
    @Override
    public void cancelInvoice(String invoiceNumber, String clientId) {
        platformOrderContentMapper.cancelInvoice(invoiceNumber, clientId);
    }
    @Override
    public void cancelBatchInvoice(List<String> invoiceNumbers) {
        platformOrderContentMapper.cancelBatchInvoice(invoiceNumbers);
    }

    @Override
    public List<PlatformOrderContent> findOrderContentsWithMissingStock(List<String> orderIds) {
        return platformOrderContentMapper.findOrderContentsWithMissingStock(orderIds);
    }

    @Override
    public List<PlatformOrderContent> fetchOrderContent(List<String> orderIds) {
        return platformOrderContentMapper.fetchOrderContent(orderIds);
    }

    @Override
    public List<PlatformOrderContent> findOrderContentsWithStock(List<String> orderIds) {
        return platformOrderContentMapper.findOrderContentsWithStock(orderIds);
    }
}
