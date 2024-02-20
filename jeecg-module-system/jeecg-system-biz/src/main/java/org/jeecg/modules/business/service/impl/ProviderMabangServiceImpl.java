package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.domain.api.mabang.doSearchSkuListNew.*;
import org.jeecg.modules.business.domain.api.mabang.purDoAddPurchase.AddPurchaseOrderRequest;
import org.jeecg.modules.business.domain.api.mabang.purDoAddPurchase.AddPurchaseOrderRequestBody;
import org.jeecg.modules.business.domain.api.mabang.purDoAddPurchase.AddPurchaseOrderResponse;
import org.jeecg.modules.business.domain.api.mabang.purDoAddPurchase.SkuStockData;
import org.jeecg.modules.business.domain.api.mabang.purDoGetProvider.ProviderData;
import org.jeecg.modules.business.domain.api.mabang.purDoGetProvider.ProviderRequestErrorException;
import org.jeecg.modules.business.entity.Provider;
import org.jeecg.modules.business.mapper.ProviderMabangMapper;
import org.jeecg.modules.business.service.IProviderMabangService;
import org.jeecg.modules.business.service.IProviderService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: provider
 * @Author: jeecg-boot
 * @Date:   2024-02-14
 * @Version: V1.0
 */
@Service
@Slf4j
public class ProviderMabangServiceImpl extends ServiceImpl<ProviderMabangMapper, ProviderData> implements IProviderMabangService {
    @Autowired
    private IProviderService providerService;
    @Autowired
    ISkuService skuService;

    @Override
    public void saveProviderFromMabang(List<ProviderData> providerDataList) {
        if(providerDataList.isEmpty()) {
            return;
        }
        List<String> allProviderIds = providerDataList.stream().map(ProviderData::getId).collect(Collectors.toList());
        List<Provider> existingProviders = providerService.listByMabangIds(allProviderIds);
        List<String> existingProviderIds = existingProviders.stream().map(Provider::getIdMabang).collect(Collectors.toList());
        List<String> newProviderIds = allProviderIds.stream().filter(providerId -> !existingProviderIds.contains(providerId)).collect(Collectors.toList());
        List<Provider> newProviders = providerDataList.stream().filter(providerData -> newProviderIds.contains(providerData.getId())).map(providerData -> (new Provider()).providerFromData(providerData)).collect(Collectors.toList());
        if(!newProviders.isEmpty()) {
            providerService.saveBatch(newProviders);
        }
    }

    @Transactional
    @Override
    public void addPurchaseOrderToMabang(Map<String, Integer> skuQuantities, InvoiceMetaData metaData) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String mabangUsername = sysUser.getMabangUsername();
        String content = metaData.getFilename().substring(0, metaData.getFilename().lastIndexOf("."));
        List<String> stockSkuList = new ArrayList<>();
        String stockSkus;
        log.info("Creating purchase order to Mabang : {} skus", skuQuantities.size());
        Map<String, SkuData> skuDataMap = new HashMap<>();
        List<SkuData> skuDataList = new ArrayList<>();
        log.info("Requesting SKU data from Mabang API.");
        for(String stockSku : skuQuantities.keySet()) {
            stockSkuList.add(stockSku);
            if(stockSkuList.size() == 50) {
                stockSkus = String.join(",", stockSkuList);
                stockSkuList.clear();

                SkuListRequestBody skuListRequestBody = new SkuListRequestBody()
                        .setSkuStockList(stockSkus)
                        .setShowProvider(1)
                        .setDatetimeType(DateType.CREATE)
                        .setTotal(50);
                SkuListRawStream skuListRawStream = new SkuListRawStream(skuListRequestBody);
                SkuListStream skuListStream = new SkuListStream(skuListRawStream);
                skuDataList.addAll(skuListStream.all());
            }
        }
        if(!stockSkuList.isEmpty()) {
            stockSkus = String.join(",", stockSkuList);
            SkuListRequestBody skuListRequestBody = new SkuListRequestBody()
                    .setSkuStockList(stockSkus)
                    .setShowProvider(1)
                    .setDatetimeType(DateType.CREATE)
                    .setTotal(stockSkuList.size());
            SkuListRawStream skuListRawStream = new SkuListRawStream(skuListRequestBody);
            SkuListStream skuListStream = new SkuListStream(skuListRawStream);
            skuDataList.addAll(skuListStream.all());
        }
        if(skuDataList.isEmpty()) {
            throw new SkuListRequestErrorException("Couldn't get SKU data from Mabang API.");
        }

        for(SkuData skuData : skuDataList) {
            skuDataMap.put(skuData.getErpCode(), skuData);
        }
        List<SkuStockData> skuStockData = new ArrayList<>();
        for(Map.Entry<String, SkuData> entry : skuDataMap.entrySet()) {
            SkuStockData stockData = new SkuStockData();
            stockData.setStockSku(entry.getKey());
            stockData.setPrice(entry.getValue().getPurchasePrice());
            stockData.setPurchaseNum(skuQuantities.get(entry.getKey()));
            stockData.setProvider(entry.getValue().getProvider());
            skuStockData.add(stockData);
        }
        // group by provider
        Map<String, List<SkuStockData>> stockProviderMap = new HashMap<>();
        for(SkuStockData stockData : skuStockData) {
            if(stockProviderMap.containsKey(stockData.getProvider())) {
                stockProviderMap.get(stockData.getProvider()).add(stockData);
            } else {
                List<SkuStockData> stockDataList = new ArrayList<>();
                stockDataList.add(stockData);
                stockProviderMap.put(stockData.getProvider(), stockDataList);
            }
        }
        log.info("Create {} purchase orders to Mabang.", stockProviderMap.size());

        // group id is the response from mabang API
        List<String> groupIds = new ArrayList<>();
        for(Map.Entry<String, List<SkuStockData>> entry : stockProviderMap.entrySet()) {
            String providerName = entry.getKey();
            List<SkuStockData> stockDataList = entry.getValue();
            AddPurchaseOrderRequestBody body = new AddPurchaseOrderRequestBody(mabangUsername, providerName, content, stockDataList);
            AddPurchaseOrderRequest request = new AddPurchaseOrderRequest(body);
            AddPurchaseOrderResponse response = request.send();
            log.info("Response from Mabang Add purchase API : " + response.toString());
            if(!response.success())
                throw new ProviderRequestErrorException("Couldn't add purchase order to Mabang.");
            groupIds.add(response.getGroupId());
        }
        log.info("Purchase orders created to Mabang, groupIds : {}", groupIds);
    }
}
