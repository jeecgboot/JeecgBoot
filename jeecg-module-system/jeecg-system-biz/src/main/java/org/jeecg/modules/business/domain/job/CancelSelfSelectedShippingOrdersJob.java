package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.CancelOrderRequest;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.CancelOrderRequestBody;

import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderResponse;

import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.service.EmailService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.service.IShopService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 将指定店铺（shopCodes）和指定物流方式（shippingServices）的订单批量作废
 * 默认：JW 店铺 + 最近 15 天 + 4 种买家自选物流方式
 */
@Slf4j
@Component
public class CancelSelfSelectedShippingOrdersJob implements Job {
    @Autowired private IPlatformOrderService platformOrderService;
    @Autowired private IShopService shopService;
    @Autowired private EmailService emailService;

    private static final ZoneId CTT = ZoneId.of("Asia/Shanghai");
    private static final List<String> DEFAULT_SHOP_ERP_CODES = Collections.singletonList("JW");
    private static final int DEFAULT_LOOKBACK_DAYS = 15;
    private static final int DEFAULT_CANCEL_BATCH_SIZE = 50;
    private static final int DEFAULT_MAX_TO_CANCEL = 50;
    private static final String DEFAULT_DEST_EMAIL = "service@wia-sourcing.com";
    private static final List<String> DEFAULT_SHIPPING_SERVICE  =  Arrays.asList(
            "Mondial Relay - Livraison Point de retrait 72H",
            "Colissimo - Livraison Express à Domicile 24-48H",
            "Chronopost - Livraison relais Pickup 72H",
            "Colissimo - Livraison Express à Domicile 72H"
    );
    @Override
    public void execute(JobExecutionContext ctx) {
        // -------- 1) Parse parameters --------
        List<String> shopCodes = new ArrayList<>(DEFAULT_SHOP_ERP_CODES);
        List<String> shippingServices = new ArrayList<>(DEFAULT_SHIPPING_SERVICE);
        ZonedDateTime nowCtt = ZonedDateTime.now(CTT);
        LocalDateTime end = nowCtt.toLocalDateTime();
        LocalDateTime start = end.minusDays(DEFAULT_LOOKBACK_DAYS);
        int batchSize   = Math.max(1, DEFAULT_CANCEL_BATCH_SIZE);
        int maxToCancel = DEFAULT_MAX_TO_CANCEL;
        JobDataMap map = ctx.getMergedJobDataMap();
        try {
            String param = (String) map.get("parameter");
            if (param != null && !param.isEmpty()) {
                param = param.replaceAll("(?m)//.*$", "");
                param = param.replaceAll(",(\\s*[}\\]])", "$1");
                JSONObject json = JSONObject.fromObject(param);
                DateTimeFormatter f = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                if (json.has("shopCodes")) {
                    shopCodes = ((Collection<?>) json.getJSONArray("shopCodes"))
                            .stream().map(Object::toString).collect(Collectors.toList());
                } else if (json.has("shopCode")) {
                    shopCodes = Collections.singletonList(json.getString("shopCode"));
                }
                if (json.has("shippingServices")) {
                    shippingServices = ((Collection<?>) json.getJSONArray("shippingServices"))
                            .stream().map(Object::toString).collect(Collectors.toList());
                } else if (json.has("shippingService")) {
                    shippingServices = Collections.singletonList(json.getString("shippingService"));
                }
                if (json.has("startDateTime"))   start = LocalDateTime.parse(json.getString("startDateTime"), f);
                if (json.has("endDateTime"))     end   = LocalDateTime.parse(json.getString("endDateTime"), f);
            }
        } catch (Exception e) {
            log.warn("Parameter parse failed, using defaults. {}", e.getMessage());
        }
        if (!end.isAfter(start)) throw new IllegalArgumentException("endDateTime must be after startDateTime");
        // ---------2) shopCode -> shopId
        List<String> shopIds = shopCodes.stream()
                .map(code -> {
                    String id = shopService.getIdByCode(code);
                    if (id == null) {
                        log.warn("Shop not found by code={}", code);
                    }
                    return id;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (shopIds.isEmpty()) {
            log.warn("No valid shopIds found for shopCodes={}", shopCodes);
            return;
        }
        //----------3) Find candidate orders to cancel : shop + shipping + time range --------
        List<String> platformOrderIds = platformOrderService.lambdaQuery()
                .in(PlatformOrder::getShopId, shopIds)
                .in(PlatformOrder::getShippingService, shippingServices)
                .between(PlatformOrder::getOrderTime, start, end)
                .ne(PlatformOrder::getErpStatus, 5)
                .list().stream()
                .map(PlatformOrder::getPlatformOrderId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        log.info("Candidates (shopCode={}, shipping='{}'): {} from {} ~ {}",
                shopCodes, shippingServices, platformOrderIds.size(), start, end);
        if (platformOrderIds.isEmpty()) return;
        //------Set the max to cancel limit ------
        if (platformOrderIds.size() > maxToCancel) {
            platformOrderIds = platformOrderIds.subList(0, maxToCancel);
            log.info("Truncated to maxToCancel={}, remaining {}", maxToCancel, platformOrderIds.size());
        }
        //----------4) Call Mabang API to cancel the orders in batch : orderStatus=5 (Cancelled) --------
        List<String> success = new ArrayList<>();

        for (List<String> batch : Lists.partition(platformOrderIds, batchSize)) {
            CancelOrderRequestBody body =
                    new CancelOrderRequestBody(batch, "Auto-cancel: " + String.join(",", shopCodes) + " 买家自选物流海外仓");
            ChangeOrderResponse resp = new CancelOrderRequest(body).send();
            if (resp.success()) {
                success.addAll(batch);
                log.info("Void batch success: +{}, msg={}", batch.size(), resp.getMessage());
            } else {
                log.warn("Void batch failed. msg={}, size={}, batch={}",
                        resp.getMessage(), batch.size(), batch);
            }
        }
        log.info("Canceled {}/{} orders", success.size(), platformOrderIds.size());
        // -------- 5) notify by email --------
        if (!success.isEmpty()) {
            try {
                String emailSubject = String.format("系统作废了 %d 个买家自选物流订单，请关注", success.size());
                String templateName = "cancelSelfSelectedShippingOrders.ftl";
                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("shopCodes", shopCodes);
                templateModel.put("shippingServices", shippingServices);
                templateModel.put("orderCount", success.size());
                templateModel.put("orders", success);
                templateModel.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                emailService.newSendSimpleMessage(DEFAULT_DEST_EMAIL, emailSubject, templateName, templateModel);
                log.info("Cancel-job email sent to {}", DEFAULT_DEST_EMAIL);
            } catch (Exception e) {
                log.error("Failed to send email notification for cancel job", e);
            }
        }
        log.info("Job done.");
    }
}
