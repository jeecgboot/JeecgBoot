package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.PlatformOrderOperation;
import org.jeecg.modules.business.vo.Responses;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
/**
 * verify that SKU.erp_code matches the configured client.
 * Orders that do not match will be sent to pending review via the Mabang API.
 */
@Slf4j
@Component
public class SkuClientCodeCheckJob implements Job {
    @Autowired private IShopService shopService;
    @Autowired private IPlatformOrderService platformOrderService;
    @Autowired private IPlatformOrderContentService platformOrderContentService;
    @Autowired private ISkuService skuService;
    @Autowired private IPlatformOrderMabangService platformOrderMabangService;
    private static final String ERP_STATUS_PENDING = "1";
    private static final String CAN_SEND_YES = "1";
    private static final Map<String, String> DEFAULT_SHOP_CODE_TO_CLIENT = new HashMap<>();
    static {
        DEFAULT_SHOP_CODE_TO_CLIENT.put("BA_orthosteps", "BA");
    }
    @Override
    public void execute(JobExecutionContext ctx) {
        JobDataMap map = ctx.getMergedJobDataMap();
        // -------- 1)（shopCode → client）--------
        Map<String, String> shopCodeToClientCode = new HashMap<>();
        // -------- 2) parameter --------
        try {
            String param = (String) map.get("parameter");
            if (param != null && !param.isEmpty()) {
                JSONObject json = JSONObject.fromObject(param);
                if (json.has("shopCodeToClient")) {
                    JSONObject codeMapping = json.getJSONObject("shopCodeToClient");
                    for (Object key : codeMapping.keySet()) {
                        shopCodeToClientCode.put(key.toString(), codeMapping.getString(key.toString()));
                    }
                    log.warn("shopCodeToClient: {}", shopCodeToClientCode);
                }
            }
        } catch (Exception e) {
            shopCodeToClientCode.putAll(DEFAULT_SHOP_CODE_TO_CLIENT);
            log.error("fail to parse parameter, using default configuration: {}", DEFAULT_SHOP_CODE_TO_CLIENT, e);
    }
        if (shopCodeToClientCode.isEmpty()) {
            shopCodeToClientCode.putAll(DEFAULT_SHOP_CODE_TO_CLIENT);
        }
        // -------- 3) check the orders --------
        for (Map.Entry<String, String> entry : shopCodeToClientCode.entrySet()) {
            String shopCode = entry.getKey();
            String expectedClient = entry.getValue();
            String shopId = shopService.getIdByCode(shopCode);
            if (shopId == null) {
                log.warn("<UNK> Shop={}", shopCode);
                continue;
            }
            Shop shop = shopService.getById(shopId);
            if (shop == null) {
                log.warn("<UNK> shopId={}", shopId);
                continue;
            }
            //find orders abnormal
            List<PlatformOrder> platformOrders = platformOrderService.lambdaQuery()
                    .eq(PlatformOrder::getShopId, shop.getId())
                    .eq(PlatformOrder::getErpStatus, ERP_STATUS_PENDING)
                    .eq(PlatformOrder::getCanSend, CAN_SEND_YES)
                    .list();
            Map<String, String> abnormalOrderReasons = new HashMap<>();
            for (PlatformOrder platformOrder : platformOrders) {
                List<String> mismatches = new ArrayList<>();
                List<PlatformOrderContent> contents = platformOrderContentService.lambdaQuery()
                        .eq(PlatformOrderContent::getPlatformOrderId, platformOrder.getId())
                        .list();
                for (PlatformOrderContent content : contents) {
                    Sku sku = skuService.getById(content.getSkuId());
                    if (sku == null || sku.getErpCode() == null) continue;
                    String erpCode = sku.getErpCode();
                    String suffix = erpCode.contains("-")
                            ? erpCode.substring(erpCode.lastIndexOf("-") + 1)
                            : erpCode;
                    if (!suffix.equals(expectedClient)) {
                        mismatches.add(String.format(
                                "订单=%s, SKU=%s, 客户=%s",
                                platformOrder.getPlatformOrderId(),
                                erpCode,
                                expectedClient
                        ));
                    }
                }
                if (!mismatches.isEmpty()) {
                    String reason = String.format("店铺=%s, 不一致: %s", shopCode, String.join("; ", mismatches));
                    abnormalOrderReasons.put(platformOrder.getPlatformOrderId(), reason);
                    log.warn("Abnormal: {}", reason);
                }
            }
            // -------- 4) suspendOrder API --------
            for (Map.Entry<String, String> abnormalOrderReason : abnormalOrderReasons.entrySet()) {
                String orderId = abnormalOrderReason.getKey();
                String reason = abnormalOrderReason.getValue();
                PlatformOrderOperation operation = new PlatformOrderOperation();
                operation.setOrderIds(orderId);
                operation.setReason(reason);
                operation.setLabelName("虚拟SKU和客户不匹配");
                Responses resp = platformOrderMabangService.suspendOrderBySystem(operation);
                log.info("店铺 {} 异常订单 {}：原因='{}'，成功 {} 单，失败 {} 单",
                        shopCode, orderId, reason, resp.getSuccesses().size(), resp.getFailures().size());
            }
        }
        log.info("SkuClientCodeCheckJob done.");
    }
}
