package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.skuShippingQty.SkuShippingQtyData;
import org.jeecg.modules.business.domain.api.mabang.skuShippingQty.SkuShippingQtyRawStream;
import org.jeecg.modules.business.domain.api.mabang.skuShippingQty.SkuShippingQtyRequestBody;
import org.jeecg.modules.business.domain.api.mabang.skuShippingQty.SkuShippingQtyStream;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.service.*;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Slf4j
@Component
public class SkuShippingQtyJob implements Job {
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private IPlatformOrderContentService platformOrderContentService;
    @Autowired
    private ISkuService skuService;
    @Autowired
    Environment env;
    private static final Integer DEFAULT_NUMBER_OF_DAYS = 365;

    /**
     * Job that finds all order contents from uninvoiced orders with missing stock and checks if products have been ordered
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Transactional
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Sku Shipping Quantity Retrieval Job started ...");
        LocalDateTime startDateTime = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).minusDays(DEFAULT_NUMBER_OF_DAYS);
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("startDateTime")) {
                    String startDateStr = jsonObject.getString("startDateTime");
                    startDateTime = LocalDateTime.parse(startDateStr);
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        Map<String, String> skuIdToErpCodeMap = skuService.findMissingSkusInNotShippedOrders(startDateTime)
                .stream().collect(Collectors.toMap(Sku::getId, Sku::getErpCode));
        if(skuIdToErpCodeMap.isEmpty()) {
            log.info("All products are in stock in not shipped orders");
            return;
        }
        log.info("Missing Skus in not shipped orders : " + skuIdToErpCodeMap.size());
        for(Map.Entry<String, String> entry : skuIdToErpCodeMap.entrySet()) {
            log.info("Sku ID : " + entry.getKey() + " --- ErpCode : " + entry.getValue());
        }
        // Couper la liste des sku en plusieurs listes de 100 sku
        List<List<String>> skuErpCodeSplitList = new ArrayList<>();
        List<String> skuErpCodeList = new ArrayList<>();
        int i = 1;
        for(Map.Entry<String, String> entry : skuIdToErpCodeMap.entrySet()) {
            skuErpCodeList.add(entry.getValue());
            if(i%100 == 0 || i == skuIdToErpCodeMap.size()) {
                skuErpCodeSplitList.add(skuErpCodeList);
                skuErpCodeList = new ArrayList<>();
            }
            i++;
        }
        for(List<String> list : skuErpCodeSplitList) {
            log.info("Sku ErpCode Split list size : " + list.size());
        }
        // Requete API Mabang pour récupérer l'état des stocks des produits
        Map<String, SkuShippingQtyData> skuShippingQtyDataMap = new HashMap<>();
        for(List<String> skuErpCodesList : skuErpCodeSplitList) {
            SkuShippingQtyRequestBody requestBody = new SkuShippingQtyRequestBody()
                    .setStockSkus(String.join(",", skuErpCodesList));
            SkuShippingQtyRawStream rawStream = new SkuShippingQtyRawStream(requestBody);
            SkuShippingQtyStream stream = new SkuShippingQtyStream(rawStream);

            Map<String, SkuShippingQtyData> skuShippingQtyDataResponseMap = new HashMap<>();
            skuShippingQtyDataResponseMap = stream.all().stream().collect(Collectors.toMap(SkuShippingQtyData::getStockSku, Function.identity()));

            if(skuShippingQtyDataResponseMap.isEmpty()) {
                log.info("No data returned from API");
                break;
            }
            skuShippingQtyDataMap.putAll(skuShippingQtyDataResponseMap);
        }
        for(Map.Entry<String, SkuShippingQtyData> entry : skuShippingQtyDataMap.entrySet()) {
            log.info("Sku ErpCode : " + entry.getKey() + " --- Stock : " + entry.getValue().getStockQuantity() + " --- Shipping Quantity : " + entry.getValue().getShippingQuantity());
        }

        // because orders with productAvailable = 1, are using stock, we need to subtract the qty from Mabang API response
        Map<PlatformOrder, List<PlatformOrderContent>> orderContentMapWithStock = platformOrderService.fetchOrdersWithProductAvailable();

        List<PlatformOrder> ordersWithMissingStock = platformOrderService.fetchOrdersWithMissingStock(startDateTime);
        List<String> orderIdsWithMissingStock = ordersWithMissingStock.stream().map(PlatformOrder::getId).collect(toList());
        List<PlatformOrderContent> orderContentsWithMissingStock = platformOrderContentService.findOrderContentsWithMissingStock(orderIdsWithMissingStock);
        List<PlatformOrderContent> orderContentsWithProductAvailable = platformOrderContentService.findOrderContentsWithStock(orderIdsWithMissingStock);
        Map<String, PlatformOrder> orderMap = ordersWithMissingStock.stream().collect(toMap(PlatformOrder::getId, Function.identity()));
        Map<PlatformOrder, List<PlatformOrderContent>> orderMapWithMissingStockUnordered = orderContentsWithMissingStock.stream()
                .collect(
                        groupingBy(
                                platformOrderContent -> orderMap.get(platformOrderContent.getPlatformOrderId()
                                )
                        )
                );
        Map<PlatformOrder, List<PlatformOrderContent>> orderMapWithMissingStock = orderMapWithMissingStockUnordered.entrySet().stream().
                sorted(
                        Map.Entry.comparingByKey(Comparator.comparing(PlatformOrder::getOrderTime))
                ).collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new)
                );

        // Subtract the virtual stock quantity from the orders with product available
        for (Map.Entry<PlatformOrder, List<PlatformOrderContent>> orderMapEntry : orderContentMapWithStock.entrySet()) {
            for(PlatformOrderContent content : orderMapEntry.getValue()) {
                SkuShippingQtyData skuData = skuShippingQtyDataMap.get(skuIdToErpCodeMap.get(content.getSkuId()));
                if(skuData == null) {
                    continue;
                }
                skuData.setStockAfterDistribution(skuData.getStockQuantity() - content.getQuantity());
                skuData.calculateVirtualQuantity();
            }
        }
        log.info("Sku Shipping Quantity after subtracting the quantity from orders with product available");
        for(Map.Entry<String, SkuShippingQtyData> entry : skuShippingQtyDataMap.entrySet()) {
            log.info("Sku ErpCode : " + entry.getKey() + " --- Stock : " + entry.getValue().getStockQuantity() + " --- Stock after subtracting : " + entry.getValue().getStockAfterDistribution() + " --- Virtual Stock : " + entry.getValue().getVirtualQuantity() + " --- Shipping Quantity : " + entry.getValue().getShippingQuantity());
        }
        // Subtract the virtual stock quantity from the order with missing stock but content with stock
        for(PlatformOrderContent content : orderContentsWithProductAvailable) {
            SkuShippingQtyData skuData = skuShippingQtyDataMap.get(skuIdToErpCodeMap.get(content.getSkuId()));
            if(skuData == null) {
                continue;
            }
            skuData.setVirtualQuantity(skuData.getVirtualQuantity() - content.getQuantity());
        }


        List<PlatformOrder> ordersToUpdate = new ArrayList<>();
        List<PlatformOrderContent> orderContentsToUpdate = new ArrayList<>();
        // Subtract the virtual stock quantity from the orders with missing stock, then update stock availability
        for (Map.Entry<PlatformOrder, List<PlatformOrderContent>> orderMapEntry : orderMapWithMissingStock.entrySet()) {
            boolean allContentsHaveStock = true;
            for(PlatformOrderContent content : orderMapEntry.getValue()) {
                // subtract the quantity from skuShippingQtyDataMap, if enough stock, set virtualProductAvailable to 1
                SkuShippingQtyData skuData = skuShippingQtyDataMap.get(skuIdToErpCodeMap.get(content.getSkuId()));
                if(skuData == null) {
                    log.info("SKU data is null for Sku erpCode : {} - Sku ID : {}", skuIdToErpCodeMap.get(content.getSkuId()), content.getSkuId());
                    allContentsHaveStock = false;
                    continue;
                }
                int quantity = skuData.getVirtualQuantity() - content.getQuantity();
                if(quantity >= 0) {
                    skuData.setVirtualQuantity(quantity);
                    content.setVirtualProductAvailable("1");
                    orderContentsToUpdate.add(content);
                    log.info("Sku {} was distributed to order {}", skuData.getStockSku(), orderMapEntry.getKey().getPlatformOrderId());
                    log.info("Sku stock quantity : {} --- Stock after subtracting : {} --- Shipping quantity : {} --- Virtual quantity : {} --- Order quantity : {}", skuData.getStockQuantity(), skuData.getStockAfterDistribution(), skuData.getShippingQuantity(), skuData.getVirtualQuantity(), content.getQuantity());
                } else {
                    skuData.setVirtualQuantity(0);
                    allContentsHaveStock = false;
                }
            }
            // if all contents have stock, set virtualProductAvailable to 1
            if(allContentsHaveStock) {
                orderMapEntry.getKey().setVirtualProductAvailable("1");
                ordersToUpdate.add(orderMapEntry.getKey());
            }
        }

        // Update the database
        if(!ordersToUpdate.isEmpty()) {
            platformOrderService.updateBatchById(ordersToUpdate);
        }
        if(!orderContentsToUpdate.isEmpty()) {
            platformOrderContentService.updateBatchById(orderContentsToUpdate);
        }
        log.info("Sku Shipping Quantity Retrieval invoicing job finished.");
    }
}
