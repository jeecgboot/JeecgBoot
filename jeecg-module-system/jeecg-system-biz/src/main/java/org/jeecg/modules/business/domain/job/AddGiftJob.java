package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderRequest;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderRequestBody;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderResponse;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.entity.GiftRule;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.service.IGiftRulesService;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jetbrains.annotations.NotNull;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class AddGiftJob implements Job {

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 30;

    private static final List<String> DEFAULT_SHOPS = Arrays.asList("FC Takumiya", "FCFR");
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private static final String OBSOLETE_STATUS_CODE = "4";

    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private IGiftRulesService giftRulesService;
    @Autowired
    private IPlatformOrderMabangService platformOrderMabangService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = endDateTime.minusDays(DEFAULT_NUMBER_OF_DAYS);
        List<String> shops = DEFAULT_SHOPS;
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("startDateTime")) {
                    String startDateStr = jsonObject.getString("startDateTime");
                    startDateTime = LocalDateTime.parse(startDateStr);
                }
                if (!jsonObject.isNull("endDateTime")) {
                    String endDateStr = jsonObject.getString("endDateTime");
                    endDateTime = LocalDateTime.parse(endDateStr);
                }
                if (!jsonObject.isNull("shops")) {
                    JSONArray shopsArray = jsonObject.getJSONArray("shops");
                    List<String> shopList = new ArrayList<>();
                    for (int i = 0; i < shopsArray.length(); i++) {
                        shopList.add(shopsArray.getString(i));
                    }
                    shops = shopList;
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        if (!endDateTime.isAfter(startDateTime)) {
            throw new RuntimeException("EndDateTime must be strictly greater than StartDateTime !");
        }

        List<PlatformOrder> platformOrders = platformOrderService.fetchUninvoicedOrdersForShops(startDateTime, endDateTime, shops);
        List<String> platformOrderIds = platformOrders.stream().map(PlatformOrder::getPlatformOrderId).collect(Collectors.toList());
        List<List<String>> platformOrderIdLists = Lists.partition(platformOrderIds, 10);
        List<GiftRule> giftRules = giftRulesService.findGiftRulesByShopCode(shops);
        Map<String, Map<Boolean, List<GiftRule>>> rulesMap = giftRules.stream()
                .collect(groupingBy(GiftRule::getShopCode,
                        groupingBy(GiftRule::getMatchQuantity)));
        Map<String, Set<String>> giftSetByShop = giftRules.stream()
                .collect(Collectors.groupingBy(GiftRule::getShopCode,
                        Collector.of(HashSet::new, (s, rule) -> s.add(rule.getSku()), (s1, s2) -> {
                            s1.addAll(s2);
                            return s1;
                        })));

        List<OrderListRequestBody> requests = new ArrayList<>();
        for (List<String> platformOrderIdList : platformOrderIdLists) {
            requests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList));
        }
        List<Order> mabangOrders = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);
        List<CompletableFuture<Boolean>> futures = requests.stream()
                .map(request -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        OrderListRawStream rawStream = new OrderListRawStream(request);
                        OrderListStream stream = new OrderListStream(rawStream);
                        List<Order> orders = stream.all();
                        mabangOrders.addAll(orders);
                        success = !orders.isEmpty();
                    } catch (RuntimeException e) {
                        log.error("Error communicating with MabangAPI", e);
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        List<Boolean> results = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} requests have succeeded.", nbSuccesses, requests.size());
        log.info("{}/{} mabang orders have been retrieved.", mabangOrders.size(), platformOrderIds.size());

        List<Order> ordersWithLogistic = new ArrayList<>();
        log.info("Constructing gift insertion requests");
        List<ChangeOrderRequestBody> giftInsertionRequests = constructGiftInsertionRequests(mabangOrders, rulesMap, giftSetByShop, ordersWithLogistic);
        log.info("{} gift insertion requests to be sent to MabangAPI", giftInsertionRequests.size());

        log.info("Clearing logistic channel names before inserting gifts");
        platformOrderMabangService.clearLogisticChannel(ordersWithLogistic, executor);

        List<CompletableFuture<Boolean>> giftInsertionFutures = giftInsertionRequests.stream()
                .map(giftInsertionRequestBody -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        ChangeOrderRequest changeOrderRequest = new ChangeOrderRequest(giftInsertionRequestBody);
                        ChangeOrderResponse response = changeOrderRequest.send();
                        success = response.success();
                    } catch (RuntimeException e) {
                        log.error("Error communicating with MabangAPI", e);
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        results = giftInsertionFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} gift insertion requests have succeeded.", nbSuccesses, giftInsertionRequests.size());
    }

    @NotNull
    private static List<ChangeOrderRequestBody> constructGiftInsertionRequests(List<Order> mabangOrders,
                                                                               Map<String, Map<Boolean, List<GiftRule>>> rulesMap,
                                                                               Map<String, Set<String>> giftSetByShop,
                                                                               List<Order> ordersWithLogistic) {
        List<ChangeOrderRequestBody> giftInsertionRequests = new ArrayList<>();
        Map<String, List<Order>> ordersByShop = mabangOrders.stream().collect(groupingBy(Order::getShopErpCode));
        // Go through orders by Shop
        for (Map.Entry<String, List<Order>> entry : ordersByShop.entrySet()) {
            String shopCode = entry.getKey();
            log.info("Going through orders from shop {}", shopCode);
            Map<Boolean, List<GiftRule>> rulesByType = rulesMap.get(shopCode);
            Set<String> giftSkuSet = giftSetByShop.get(shopCode);
            List<GiftRule> matchingQuantityRules = rulesByType.get(Boolean.TRUE);
            List<GiftRule> nonMatchingQuantityRules = rulesByType.get(Boolean.FALSE);

            for (Order order : entry.getValue()) {
                log.info("Processing order {} ", order.getPlatformOrderId());
                // Non matching-quantity rules only apply once per order
                boolean nonMatchingRulesApplied = false;
                HashMap<String, Integer> newGiftMap = new HashMap<>();
                Map<Boolean, List<OrderItem>> orderItemMap = order.getOrderItems()
                        .stream()
                        .filter(orderItem -> !orderItem.getStatus().equalsIgnoreCase(OBSOLETE_STATUS_CODE))
                        .collect(groupingBy(orderItem -> giftSkuSet.contains(orderItem.getErpCode())));
                for (OrderItem orderItem : orderItemMap.get(Boolean.FALSE)) {
                    String erpCode = orderItem.getErpCode();
                    if (!nonMatchingRulesApplied) {
                        for (GiftRule giftRule : nonMatchingQuantityRules) {
                            if (erpCode.matches(giftRule.getRegex())) {
                                nonMatchingRulesApplied = true;
                                putValueInMapOrReduce(giftRule.getSku(), 1, newGiftMap);
                                break;
                            }
                        }
                    }
                    for (GiftRule giftRule : matchingQuantityRules) {
                        if (erpCode.matches(giftRule.getRegex())) {
                            putValueInMapOrReduce(giftRule.getSku(), orderItem.getQuantity(), newGiftMap);
                            break;
                        }
                    }
                }
                log.debug("Order {} 's new gift map : ", newGiftMap);
                HashSet<Triple<String, String, Integer>> oldSkuData = new HashSet<>();
                HashMap<String, Integer> oldGiftMap = new HashMap<>();
                List<OrderItem> oldGifts = orderItemMap.get(Boolean.TRUE) == null ? new ArrayList<>() : orderItemMap.get(Boolean.TRUE);
                oldGifts.forEach(orderItem -> {
                    oldSkuData.add(Triple.of(orderItem.getErpCode(),
                            orderItem.getErpOrderItemId(), orderItem.getQuantity()));
                    putValueInMapOrReduce(orderItem.getErpCode(), orderItem.getQuantity(), oldGiftMap);
                });
                HashSet<Pair<String, Integer>> newSkuData = new HashSet<>();
                newGiftMap.forEach((key, value) -> newSkuData.add(Pair.of(key, value)));

                if (!newGiftMap.isEmpty() && !newGiftMap.equals(oldGiftMap)) {
                    ChangeOrderRequestBody changeOrderRequestBody = new ChangeOrderRequestBody(order.getPlatformOrderId(), null,
                            oldSkuData, newSkuData, null);
                    giftInsertionRequests.add(changeOrderRequestBody);

                    // If the order already has a logistic channel name, then we need to clear it before inserting gifts
                    if (order.getLogisticChannelName() != null && !order.getLogisticChannelName().isEmpty()) {
                        ordersWithLogistic.add(order);
                    }
                }
            }
            log.info("Ended processing orders from shop {}", shopCode);
        }
        return giftInsertionRequests;
    }

    private static void putValueInMapOrReduce(String key, Integer value, HashMap<String, Integer> giftMap) {
        if (giftMap.containsKey(key)) {
            giftMap.put(key, giftMap.get(key) + value);
        } else {
            giftMap.put(key, value);
        }
    }
}
