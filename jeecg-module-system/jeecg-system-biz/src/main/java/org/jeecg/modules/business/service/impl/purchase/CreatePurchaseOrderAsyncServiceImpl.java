package org.jeecg.modules.business.service.impl.purchase;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.jeecg.modules.business.domain.api.mabang.orderUpdateOrderNewOrder.UpdateResult;
import org.jeecg.modules.business.domain.job.ThrottlingExecutorService;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.jeecg.modules.business.vo.Response;
import org.jeecg.modules.business.vo.Responses;
import org.jeecg.modules.message.websocket.WebSocketSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CreatePurchaseOrderAsyncServiceImpl implements ICreatePurchaseOrderAsyncService {
    private static final Set<String> CREATING_INVOICES =
            ConcurrentHashMap.newKeySet();
    @Autowired private IPurchaseOrderService purchaseOrderService;
    @Autowired private IPlatformOrderService platformOrderService;
    @Autowired private IProviderMabangService providerMabangService;
    @Autowired private IPlatformOrderMabangService platformOrderMabangService;

    private static final Integer DEFAULT_NUMBER_OF_THREADS = 2;
    private static final Integer MABANG_API_RATE_LIMIT_PER_MINUTE = 10;
    /**
     * Asynchronous version of creating purchase orders in Mabang.
     * Fully equivalent to the synchronous version's business logic.
     */
    @Override
    @Async
    public void createMabangPurchaseOrderAsync(List<String> invoiceNumbers, String userId) {
        for (String inv : invoiceNumbers) {
            if (!CREATING_INVOICES.add(inv)) {
                log.warn("Invoice {} is already being processed, skip", inv);
                return;
            }
        }
        try {
            log.info("[ASYNC] Start creating Mabang purchase orders. Invoices: {}", invoiceNumbers);
            Map<String, Responses> responsesMappedByInvoiceNumber = new ConcurrentHashMap<>();
            Map<String, List<String>> tempCreatedGroupIds = new ConcurrentHashMap<>();
            ExecutorService throttlingExecutorService =
                    ThrottlingExecutorService.createExecutorService(
                            DEFAULT_NUMBER_OF_THREADS,
                            MABANG_API_RATE_LIMIT_PER_MINUTE,
                            TimeUnit.MINUTES);
            AtomicReference<Map<String, LocalDateTime>> providersHistory =
                    new AtomicReference<>(new ConcurrentHashMap<>());
            // Process each invoice asynchronously
            List<CompletableFuture<Boolean>> tasks = invoiceNumbers.stream()
                    .map(invoiceNumber -> CompletableFuture.supplyAsync(() -> {
                        log.info("[ASYNC] Processing invoice: {}", invoiceNumber);
                        // 1. Fetch SKU quantities
                        List<SkuQuantity> skuQuantities =
                                purchaseOrderService.getSkuQuantityByInvoiceNumber(invoiceNumber);
                        if (skuQuantities.isEmpty()) {
                            log.warn("[ASYNC] Invoice {} has no SKU data. Skipping.", invoiceNumber);
                            return false;
                        }
                        Map<String, Integer> skuQuantityMap = skuQuantities.stream()
                                .collect(Collectors.toMap(SkuQuantity::getErpCode, SkuQuantity::getQuantity));
                        skuQuantityMap.forEach((sku, qty) ->
                                log.info("[SKU] {} → Qty: {}", sku, qty));
                        // Remove zero-quantity SKUs
                        Map<String, Integer> skuQtyNotEmptyMap = skuQuantityMap.entrySet()
                                .stream()
                                .filter(e -> e.getValue() > 0)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        // 2. Fetch metadata
                        InvoiceMetaData metaData =
                                purchaseOrderService.getMetaDataFromInvoiceNumbers(invoiceNumber);
                        // 3. Call Mabang API
                        log.info("[MABANG] Creating purchase order for invoice {}", invoiceNumber);
                        Responses result = providerMabangService.addPurchaseOrderToMabang(
                                skuQtyNotEmptyMap, metaData, providersHistory);
                        // Combine results
                        responsesMappedByInvoiceNumber.merge(
                                invoiceNumber,
                                result,
                                (oldVal, newVal) -> {
                                    oldVal.getFailures().addAll(newVal.getFailures());
                                    // success
                                    return oldVal;
                                });
                        tempCreatedGroupIds
                                .computeIfAbsent(invoiceNumber, k -> new ArrayList<>())
                                .addAll(result.getSuccesses());
                        boolean success = result.getFailures().isEmpty();
                        log.info("[MABANG] Invoice {} creation result: {}", invoiceNumber,
                                success ? "SUCCESS" : "FAILURE");
                        return success;
                    }, throttlingExecutorService))
                    .collect(Collectors.toList());
            // Wait all tasks
            tasks.forEach(CompletableFuture::join);
            long successes = responsesMappedByInvoiceNumber.values().stream()
                    .filter(r -> r.getFailures().isEmpty())
                    .count();
            log.info("[ASYNC] Finished creating purchase orders. Success {}/{}",
                    successes, invoiceNumbers.size());
            // 5. Delete failed groupIds
            List<String> groupIdsToDelete = new ArrayList<>();
            List<String> successfulInvoices = new ArrayList<>();
            for (Map.Entry<String, Responses> entry : responsesMappedByInvoiceNumber.entrySet()) {
                Responses res = entry.getValue();
                if (!res.getFailures().isEmpty()) {
                    groupIdsToDelete.addAll(res.getSuccesses());
                } else {
                    successfulInvoices.add(entry.getKey());
                }
            }
            if (!groupIdsToDelete.isEmpty()) {
                log.warn("[MABANG] Removing partially created purchase orders. GroupIds: {}", groupIdsToDelete);
                Responses deleteRes =
                        providerMabangService.deletePurchaseOrderFromMabang(groupIdsToDelete);
                responsesMappedByInvoiceNumber.put("groupIdDelete", deleteRes);
            }
            // 6. Update platform order to PREPARING
            if (!successfulInvoices.isEmpty()) {
                log.info("[MABANG] Updating platform order status to PREPARING. Successful invoices: {}",
                        successfulInvoices);
                List<String> platformOrderIds =
                        platformOrderService.getPlatformOrderIdsByInvoiceNumbers(successfulInvoices);
                Response<List<UpdateResult>, List<UpdateResult>> resp =
                        platformOrderMabangService.updateOrderStatusToPreparing(platformOrderIds);
                Responses statusRes = new Responses();
                if (resp.getStatus() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                    String errMsg = resp.getError().get(0).getReason();
                    log.error("[MABANG] Order status update failed: {}", errMsg);
                    statusRes.addFailure(errMsg);
                    responsesMappedByInvoiceNumber.put("UpdateOrderStatusError", statusRes);
                } else {
                    List<String> okIds = resp.getData().stream()
                            .map(UpdateResult::getPlatformOrderId)
                            .collect(Collectors.toList());
                    List<String> failIds = resp.getError().stream()
                            .map(UpdateResult::getPlatformOrderId)
                            .collect(Collectors.toList());
                    log.info("[MABANG] Status update results. Success: {}, Failed: {}", okIds, failIds);
                    statusRes.getSuccesses().addAll(okIds);
                    statusRes.getFailures().addAll(failIds);
                    responsesMappedByInvoiceNumber.put("UpdateOrderStatus", statusRes);
                }
            }
            // 7. Build FINAL result for frontend
            Map<String, Object> finalResult = new HashMap<>();
            for (String invoice : invoiceNumbers) {
                Responses origin = responsesMappedByInvoiceNumber.get(invoice);
                Map<String, Object> invoiceResult = new HashMap<>();
                boolean success = origin != null && origin.getFailures().isEmpty();
                invoiceResult.put("invoice", invoice);
                invoiceResult.put("finalStatus", success ? "SUCCESS" : "FAILURE");
                if (success) {
                    List<String> effectiveGroupIds =
                            tempCreatedGroupIds.getOrDefault(invoice, Collections.emptyList());
                    invoiceResult.put("effectiveCount", effectiveGroupIds.size());
                    invoiceResult.put("groupIds", effectiveGroupIds);
                } else {
                    invoiceResult.put("effectiveCount", 0);
                    invoiceResult.put("groupIds", Collections.emptyList());
                }
                finalResult.put(invoice, invoiceResult);
            }
            sendWebsocketDone(userId, finalResult);
        } catch (Exception e) {
            log.error("[ASYNC] Create purchase order failed", e);
            throw e;
        } finally {
            invoiceNumbers.forEach(CREATING_INVOICES::remove);
        }
    }
    // WebSocket Done
    private void sendWebsocketDone(String userId, Map<String, Object> finalResult) {
        JSONObject msg = new JSONObject();
        msg.put("task", "createMabangPurchaseOrder");
        msg.put("msgTxt", "DONE");
        JSONObject data = new JSONObject();
        data.put("result", finalResult);
        msg.put("data", data);
        WebSocketSender.sendToUser(userId, msg.toJSONString());
    }
    @Override
    public boolean isCreating(List<String> invoices) {
        return invoices.stream().anyMatch(CREATING_INVOICES::contains);
    }
}