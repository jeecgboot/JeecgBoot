package org.jeecg.modules.business.service.impl;

import com.amazonaws.services.dynamodbv2.xspec.NULL;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.mapper.PlatformOrderContentMapper;
import org.jeecg.modules.business.service.IPlatformOrderContentService;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.vo.SkuWeightDiscountServiceFees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class PlatformOrderContentServiceImpl extends ServiceImpl<PlatformOrderContentMapper, PlatformOrderContent> implements IPlatformOrderContentService {
    @Autowired
    private final PlatformOrderContentMapper platformOrderContentMapper;

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
    public Pair<BigDecimal, List<String>> calculateWeight(Map<String, Integer> contentMap,
                                                          Map<String, BigDecimal> skuRealWeights)
    {
        List<String> errorMessages = new ArrayList<>();
        List<String> skuIDs = new ArrayList<>(contentMap.keySet());
        log.info("skus : " + skuIDs);

        BigDecimal total = BigDecimal.ZERO;
        for(Map.Entry<String, Integer> entry: contentMap.entrySet()) {
            if(skuRealWeights.get(entry.getKey()) == null) {
                errorMessages.add("Can not find weight for one sku in: " + contentMap);
                continue;
            }
            total = total.add(skuRealWeights.get(entry.getKey()).multiply(BigDecimal.valueOf(entry.getValue())));
        }
        log.info("total weight : " + total);
        return new MutablePair<>(total, errorMessages);

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
