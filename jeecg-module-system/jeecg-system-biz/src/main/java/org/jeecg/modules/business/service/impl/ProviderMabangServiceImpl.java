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
import org.jeecg.modules.business.domain.api.mabang.purDoChangePurchase.ChangePurchaseOrderRequest;
import org.jeecg.modules.business.domain.api.mabang.purDoChangePurchase.ChangePurchaseOrderRequestBody;
import org.jeecg.modules.business.domain.api.mabang.purDoChangePurchase.ChangePurchaseOrderResponse;
import org.jeecg.modules.business.domain.api.mabang.purDoGetProvider.ProviderData;
import org.jeecg.modules.business.domain.job.ThrottlingExecutorService;
import org.jeecg.modules.business.entity.Provider;
import org.jeecg.modules.business.mapper.ProviderMabangMapper;
import org.jeecg.modules.business.service.IProviderMabangService;
import org.jeecg.modules.business.service.IProviderService;
import org.jeecg.modules.business.service.IPurchaseOrderService;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.jeecg.modules.business.vo.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.jeecg.modules.business.domain.api.mabang.purDoChangePurchase.ChangePurchaseOrderRequestBody.ActionType.CANCEL;
import static org.jeecg.modules.business.domain.api.mabang.purDoChangePurchase.ChangePurchaseOrderRequestBody.ScrapOrder.ALL;

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
    private IPurchaseOrderService purchaseOrderService;

    private final static String TEMPORARY_PROVIDER_NAME = "临时供货商";

    private static final Integer DEFAULT_NUMBER_OF_THREADS = 1;
    private static final Integer MABANG_API_RATE_LIMIT_PER_MINUTE = 10;

    private static final Integer SLEEP_TIME = 30000;

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

    /**
     * Add purchase order to Mabang.
     * @param skuQuantities Quantities mapped to sku erpCodes
     * @param metaData invoice meta data
     */
    @Transactional
    @Override
    public Responses addPurchaseOrderToMabang(Map<String, Integer> skuQuantities, InvoiceMetaData metaData, AtomicReference<Map<String, LocalDateTime>> providersHistory) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String mabangUsername = sysUser.getMabangUsername();
        Responses responses = new Responses();
        String content = metaData.getFilename();
        List<String> stockSkuList = new ArrayList<>();
        String stockSkus;
        log.info("Creating purchase order to Mabang {} : {} skus", metaData.getInvoiceCode(),skuQuantities.size());
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
            log.error("Couldn't get SKU data from Mabang API for invoice : {}", metaData.getInvoiceCode());
            responses.addFailure("Couldn't get SKU data from Mabang API for invoice : " + metaData.getInvoiceCode());
            return responses;
        }

        for(SkuData skuData : skuDataList) {
            skuDataMap.put(skuData.getErpCode(), skuData);
        }
        List<SkuStockData> skuStockData = new ArrayList<>();
        for(Map.Entry<String, SkuData> entry : skuDataMap.entrySet()) {
            SkuStockData stockData = new SkuStockData();
            stockData.setStockSku(entry.getKey());
            stockData.setPrice(entry.getValue().getPurchasePrice().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : entry.getValue().getPurchasePrice().setScale(2, RoundingMode.CEILING));
            stockData.setPurchaseNum(skuQuantities.get(entry.getKey()));
            if(entry.getValue().getSupplier() == null || entry.getValue().getSupplier().isEmpty()) {
                stockData.setProvider(TEMPORARY_PROVIDER_NAME);
            } else {
                stockData.setProvider(entry.getValue().getSupplier());
            }
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
        log.info("Creating {} purchase orders to Mabang - {}", stockProviderMap.size(), metaData.getInvoiceCode());

        // group id is the response from mabang API
        ExecutorService throttlingExecutorService = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS,
                MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);

        List<String> errors = new ArrayList<>();
        List<String> groupIds = new ArrayList<>(); // results from Mabang API
        List<CompletableFuture<Boolean>> changeOrderFutures = stockProviderMap.entrySet().stream()
                .map(entry -> CompletableFuture.supplyAsync(() -> {
                    String providerName = entry.getKey();
                    if(providersHistory.get().containsKey(providerName)) {
                        log.info("Last processed provider : {} - {}", providerName, providersHistory.get().get(providerName));
                        LocalDateTime lastProcessed = providersHistory.get().get(providerName);
                        if(lastProcessed.isAfter(LocalDateTime.now().minusSeconds(10))) {
                            log.info("Thread sleeping for {} seconds to avoid rate limit on Mabang API", SLEEP_TIME/1000);
                            try {
                                long sleepTime = SLEEP_TIME - (LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli() - lastProcessed.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli());
                                System.out.println("======= SLEEP TIME : " + sleepTime);
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e) {
                                log.error("Thread interrupted while sleeping", e);
                            }
                        }
                    }
                    List<SkuStockData> stockDataList = entry.getValue();
                    log.info("Creating purchase order to Mabang {} :\n -provider : {}\n-content : {}\n-stockDataList : {}",metaData.getInvoiceCode(), providerName, content , stockDataList);
                    AddPurchaseOrderRequestBody body = new AddPurchaseOrderRequestBody(mabangUsername, providerName, content, stockDataList);
                    AddPurchaseOrderRequest request = new AddPurchaseOrderRequest(body);
                    AddPurchaseOrderResponse response = request.send();
                    log.info("Mabang Add purchase API response | Invoice : {} - Provider : {} - Message : {}", metaData.getInvoiceCode(), providerName,response.toString());
                    if(response.getGroupId() == null) {
                        log.error("Failed to create purchase order to Mabang for Invoice : {} - Provider : {} - Skus : {} - Reason : {}", metaData.getInvoiceCode(), providerName, stockDataList.stream().map(SkuStockData::getStockSku).collect(Collectors.joining(",")), response.getMessage());
                        errors.add("Failed to create purchase order to Mabang for Invoice : " + metaData.getInvoiceCode() + " - Provider : " + providerName + " - Skus : " + stockDataList.stream().map(SkuStockData::getStockSku).collect(Collectors.joining(",")) + " - Reason : " + response.getMessage());
                        return false;
                    }
                    groupIds.add(response.getGroupId());
                    responses.addSuccess(response.getGroupId());
                    providersHistory.get().put(providerName, LocalDateTime.now());
                    return true;
                }, throttlingExecutorService))
                .collect(Collectors.toList());
        List<Boolean> results = changeOrderFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} purchase orders created successfully on Mabang. {} : GroupIds : {}", nbSuccesses, stockProviderMap.size(), metaData.getInvoiceCode(),groupIds);

        // change status of purchase order to 'ordered' = true
        if(nbSuccesses == stockProviderMap.size()) {
            purchaseOrderService.updatePurchaseOrderStatus(metaData.getInvoiceCode(), true);
            purchaseOrderService.updatePurchaseOrderGroupIds(metaData.getInvoiceCode(), groupIds);
        }
        responses.setFailures(errors);
        return responses;
    }

    @Override
    public Responses deletePurchaseOrderFromMabang(List<String> groupIds) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String mabangUsername = sysUser.getMabangUsername();
        Responses groupIdsDeleteResult = new Responses();
        log.info("Deleting purchase orders from Mabang : {}", groupIds);
            // delete purchase order from Mabang via group_id
        ExecutorService throttlingExecutorService = ThrottlingExecutorService.createExecutorService(DEFAULT_NUMBER_OF_THREADS,
                MABANG_API_RATE_LIMIT_PER_MINUTE, TimeUnit.MINUTES);
        List<CompletableFuture<Boolean>> changeOrderFutures = groupIds.stream()
                .map(groupId -> CompletableFuture.supplyAsync(() -> {
                    ChangePurchaseOrderRequestBody body = new ChangePurchaseOrderRequestBody(mabangUsername, groupId, CANCEL.getValue(), ALL.getValue());
                    ChangePurchaseOrderRequest request = new ChangePurchaseOrderRequest(body);
                    ChangePurchaseOrderResponse response = request.send();
                    if(!response.success()) {
                        log.error("Failed to delete purchase order from Mabang. GroupId : {} - Reason : {}", groupId, response.getMessage());
                        groupIdsDeleteResult.addFailure(groupId);
                    } else {
                        groupIdsDeleteResult.addSuccess(groupId);
                    }
                    return response.success();
                }, throttlingExecutorService))
                .collect(Collectors.toList());
        List<Boolean> results = changeOrderFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} purchase orders deleted successfully from Mabang. GroupIds : {}", nbSuccesses, groupIds.size(), groupIds);
        return groupIdsDeleteResult;
    }
}
