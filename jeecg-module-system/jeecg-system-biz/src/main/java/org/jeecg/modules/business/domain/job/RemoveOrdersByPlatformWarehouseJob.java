package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Triple;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.CancelOrderRequest;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.CancelOrderRequestBody;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderResponse;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.service.EmailService;
import org.jeecg.modules.business.service.IPlatformOrderContentService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * 将指定店铺（shopCodes）和指定平台发货仓库（platformWarehouses）的订单批量作废
 * 默认：AC 店铺 + 2种指定平台发货仓库
 */
@Slf4j
@Component
public class RemoveOrdersByPlatformWarehouseJob implements Job {
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private IPlatformOrderContentService platformOrderContentService;
    @Autowired
    private EmailService emailService;

    private static final List<String> DEFAULT_SHOP_ERP_CODES = Arrays.asList("EV", "SF");
    private static final int DEFAULT_CANCEL_BATCH_SIZE = 50;
    private static final String DEFAULT_DEST_EMAIL = "service@wia-sourcing.com";
    private static final List<String> DEFAULT_PLATFORM_WAREHOUSES = Arrays.asList(
            "PEAKFAST",
            "LOGISTIC INT",
            "MFP ECOM"
    );

    @Override
    public void execute(JobExecutionContext ctx) {
        List<String> shopCodes = new ArrayList<>(DEFAULT_SHOP_ERP_CODES);
        List<String> platformWarehouses = new ArrayList<>(DEFAULT_PLATFORM_WAREHOUSES);
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
                if (json.has("platformWarehouses")) {
                    platformWarehouses = ((Collection<?>) json.getJSONArray("platformWarehouses"))
                            .stream().map(Object::toString).collect(Collectors.toList());
                } else if (json.has("platformWarehouse")) {
                    platformWarehouses = Collections.singletonList(json.getString("platformWarehouse"));
                }
            }
        } catch (Exception e) {
            log.warn("Parameter parse failed, using defaults. {}", e.getMessage());
        }

        log.info("Start fetching orders from shops {} and platform warehouses {}", shopCodes, platformWarehouses);
        List<PlatformOrder> orders = platformOrderService.fetchByPlatformWarehouse(shopCodes, platformWarehouses);
        List<PlatformOrderContent> orderContents = platformOrderContentService
                .fetchOrderContent(orders.stream().map(PlatformOrder::getId)
                        .collect(Collectors.toList()));
        Map<String, String> orderMapByShopId = orders.stream().collect(toMap(PlatformOrder::getId, PlatformOrder::getShopId));
        Map<String, PlatformOrder> orderIdMap = orders.stream().collect(toMap(PlatformOrder::getId, Function.identity()));
        Map<String, Map<PlatformOrder, List<PlatformOrderContent>>> orderByShopMap = orderContents.stream()
                .collect(
                        groupingBy(
                                platformOrderContent -> orderMapByShopId.get(platformOrderContent.getPlatformOrderId()),
                                groupingBy(platformOrderContent -> orderIdMap.get(platformOrderContent.getPlatformOrderId()))
                        )
                );
        log.info("Found {} order candidates", orders.size());

        List<Triple<String, String, String>> infos = new ArrayList<>();
        List<String> finalPlatformWarehouses = platformWarehouses;
        List<String> ordersToRemove = new ArrayList<>();
        orderByShopMap.forEach((shopCode, orderMap) -> {
            log.info("Going through orders from shop {}", shopCode);
            orderMap.forEach((order, contents) -> {
                log.info("Processing order {}", order.getPlatformOrderId());
                List<PlatformOrderContent> validContents = contents.stream()
                        .filter(content -> !content.getErpStatus().equalsIgnoreCase("5"))
                        .collect(Collectors.toList());
                List<PlatformOrderContent> contentsToRemove = validContents.stream()
                        .filter(content -> finalPlatformWarehouses.contains(content.getPlatformWarehouseName()))
                        .collect(Collectors.toList());
                int nbContentsToRemove = contentsToRemove.size();
                int nbValidContents = validContents.size();
                if (nbContentsToRemove == nbValidContents) {
                    log.info("All contents to be removed are valid, order to be removed entirely");
                    ordersToRemove.add(order.getPlatformOrderId());
                    infos.add(Triple.of(shopCode, order.getPlatformOrderId(), validContents.get(0).getPlatformWarehouseName()));
                } else {
                    log.info("{}/{} contents to be removed are valid, order will remain untouched", nbContentsToRemove, nbValidContents);
                }
            });
        });

        List<String> success = new ArrayList<>();
        List<List<String>> orderIdLists = Lists.partition(ordersToRemove, DEFAULT_CANCEL_BATCH_SIZE);
        for (List<String> orderIds : orderIdLists) {
            CancelOrderRequestBody body =
                    new CancelOrderRequestBody(orderIds, "自动删单: 指定平台发货仓库");
            ChangeOrderResponse resp = new CancelOrderRequest(body).send();
            if (resp.success()) {
                success.addAll(orderIds);
                log.info("Batch removal of {} orders succeeded: {}", orderIds.size(), resp.getMessage());
            } else {
                log.warn("Batch removal of {} orders failed： {}, order IDs={}", orderIds.size(), resp.getMessage(), orderIds);
            }
        }
        log.info("Canceled {}/{} orders", success.size(), ordersToRemove.size());

        if (!success.isEmpty()) {
            try {
                String emailSubject = String.format("系统作废了 %d 个指定平台发货仓库订单，请关注", success.size());
                String templateName = "removeOrdersByPlatformWarehouse.ftl";
                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("infos", infos);
                emailService.newSendSimpleMessage(DEFAULT_DEST_EMAIL, emailSubject, templateName, templateModel);
                log.info("Cancel job email sent to {}", DEFAULT_DEST_EMAIL);
            } catch (Exception e) {
                log.error("Failed to send email notification for cancel job", e);
            }
        }
        log.info("Job done.");
    }
}
