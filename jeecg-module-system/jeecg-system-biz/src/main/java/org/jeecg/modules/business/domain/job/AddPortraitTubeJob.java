package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderRequest;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderRequestBody;
import org.jeecg.modules.business.domain.api.mabang.dochangeorder.ChangeOrderResponse;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class AddPortraitTubeJob implements Job {

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;
    private static final Double MAXIMUM_CANVAS_IN_TUBE = 3.0;

    private static final List<String> DEFAULT_SHOPS = Arrays.asList("JCH3", "JCH4", "JCH5");
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private static final String TUBE_30_SKU_SINGLE_DOUBLE = "PJ95310032-WIA";
    private static final String TUBE_40_SKU_SINGLE = "PJ95430032-WIA";
    private static final String TUBE_40_SKU_MULTIPLE = "PJ95430040-WIA";
    private static final String TUBE_50_SKU_SINGLE = "PJ95530032-WIA";
    private static final String TUBE_50_SKU_MULTIPLE = "PJ95530040-WIA";
    private static final List<String> TUBE_SKUS = Arrays.asList(TUBE_30_SKU_SINGLE_DOUBLE, TUBE_50_SKU_MULTIPLE,
            TUBE_50_SKU_SINGLE, TUBE_40_SKU_MULTIPLE, TUBE_40_SKU_SINGLE);
    private static final String PREFIX_50_CANVAS = "JJ2501";
    private static final String PREFIX_40_CANVAS = "JJ2500";
    private static final String PREFIX_30_CANVAS = "JJ2502";

    @Autowired
    private IPlatformOrderService platformOrderService;

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

        List<String> platformOrderIds = platformOrderService.fetchUninvoicedOrdersForShops(startDateTime, endDateTime, shops);
        List<List<String>> platformOrderIdLists = Lists.partition(platformOrderIds, 10);

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

        log.info("Constructing order modification requests");
        List<ChangeOrderRequestBody> changeOrderRequests = new ArrayList<>();
        for (Order mabangOrder : mabangOrders) {
            Pair<HashSet<Pair<String, Integer>>, HashSet<Pair<String, Integer>>> currentAndAdequateTubes =
                    findCurrentAndAdequateTubes(mabangOrder.getOrderItems());
            HashSet<Pair<String, Integer>> currentTubes = currentAndAdequateTubes.getLeft();
            HashSet<Pair<String, Integer>> adequateTubes = currentAndAdequateTubes.getRight();
            // Do nothing if current tubes are the adequate tubes
            if (!currentTubes.containsAll(adequateTubes) || !adequateTubes.containsAll(currentTubes)) {
                ChangeOrderRequestBody changeOrderRequestBody = new ChangeOrderRequestBody(mabangOrder.getPlatformOrderId(),
                        currentTubes, adequateTubes);
                changeOrderRequests.add(changeOrderRequestBody);
            }
        }
        log.info("{} order modification requests to be sent to MabangAPI", changeOrderRequests.size());

        List<CompletableFuture<Boolean>> changeOrderFutures = changeOrderRequests.stream()
                .map(changeOrderRequestBody -> CompletableFuture.supplyAsync(() -> {
                    boolean success = false;
                    try {
                        ChangeOrderRequest changeOrderRequest = new ChangeOrderRequest(changeOrderRequestBody);
                        ChangeOrderResponse response = changeOrderRequest.send();
                        success = response.success();
                    } catch (RuntimeException e) {
                        log.error("Error communicating with MabangAPI", e);
                    }
                    return success;
                }, executor))
                .collect(Collectors.toList());
        results = changeOrderFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        nbSuccesses = results.stream().filter(b -> b).count();
        log.info("{}/{} order modification requests have succeeded.", nbSuccesses, changeOrderRequests.size());
    }

    /**
     * Return a pair of sets of tube(String, Integer)s, on the left current tubes (if any, empty if none), on the right the adequate tubes
     *
     * @param orderItems List of order items
     * @return Set of pairs
     */
    private static Pair<HashSet<Pair<String, Integer>>, HashSet<Pair<String, Integer>>> findCurrentAndAdequateTubes(List<OrderItem> orderItems) {
        int canvas40Count = 0;
        int canvas50Count = 0;
        HashSet<Pair<String, Integer>> currentTubes = new HashSet<>();
        HashSet<Pair<String, Integer>> adequateTubes = new HashSet<>();
        for (OrderItem orderItem : orderItems) {
            String sku = orderItem.getErpCode();
            int quantity = orderItem.getQuantity();
            if (TUBE_SKUS.contains(sku)) {
                currentTubes.add(Pair.of(sku, quantity));
            } else if (sku.startsWith(PREFIX_50_CANVAS)) {
                canvas50Count += quantity;
            } else if (sku.startsWith(PREFIX_40_CANVAS)) {
                canvas40Count += quantity;
            } else if (sku.startsWith(PREFIX_30_CANVAS)) {
                canvas40Count += quantity;
            }
        }

        int canvas40RemainderCount = canvas40Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int canvas50RemainderCount = canvas50Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int tube50SingleCount = 0;
        int tube50MultipleCount = (int) Math.floor(canvas50Count / MAXIMUM_CANVAS_IN_TUBE);
        int tube40SingleCount = 0;
        int tube40MultipleCount = (int) Math.floor(canvas40Count / MAXIMUM_CANVAS_IN_TUBE);
        if (canvas50RemainderCount > 0 && canvas40RemainderCount > 0) {
            tube50MultipleCount++;
            if (canvas50RemainderCount == 2 && canvas40RemainderCount == 2) {
                tube50SingleCount++;
            }
        } else {
            if (canvas40RemainderCount == 1) {
                tube40SingleCount++;
            } else if (canvas40RemainderCount == 2) {
                tube40MultipleCount++;
            } else if (canvas50RemainderCount == 1) {
                tube50SingleCount++;
            } else if (canvas50RemainderCount == 2) {
                tube50MultipleCount++;
            }
        }
        if (tube50SingleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_50_SKU_SINGLE, tube50SingleCount));
        }
        if (tube50MultipleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_50_SKU_MULTIPLE, tube50MultipleCount));
        }
        if (tube40SingleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_40_SKU_SINGLE, tube40SingleCount));
        }
        if (tube40MultipleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_40_SKU_MULTIPLE, tube40MultipleCount));
        }
        return Pair.of(currentTubes, adequateTubes);
    }

    private static Pair<HashSet<Pair<String, Integer>>, HashSet<Pair<String, Integer>>> newFindCurrentAndAdequateTubes(List<OrderItem> orderItems) {
        int canvas30Count = 0;
        int canvas40Count = 0;
        int canvas50Count = 0;
        HashSet<Pair<String, Integer>> currentTubes = new HashSet<>();
        HashSet<Pair<String, Integer>> adequateTubes = new HashSet<>();
        for (OrderItem orderItem : orderItems) {
            String sku = orderItem.getErpCode();
            int quantity = orderItem.getQuantity();
            if (TUBE_SKUS.contains(sku)) {
                currentTubes.add(Pair.of(sku, quantity));
            } else if (sku.startsWith(PREFIX_50_CANVAS)) {
                canvas50Count += quantity;
            } else if (sku.startsWith(PREFIX_40_CANVAS)) {
                canvas40Count += quantity;
            } else if (sku.startsWith(PREFIX_30_CANVAS)) {
                canvas30Count += quantity;
            }
        }

        int canvas30RemainderCount = canvas30Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int canvas40RemainderCount = canvas40Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int canvas50RemainderCount = canvas50Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int totalRemainderCount = canvas30RemainderCount + canvas40RemainderCount + canvas50RemainderCount;
        int tube50SingleCount = 0;
        int tube50MultipleCount = (int) Math.floor(canvas50Count / MAXIMUM_CANVAS_IN_TUBE);
        int tube40SingleCount = 0;
        int tube40MultipleCount = (int) Math.floor(canvas40Count / MAXIMUM_CANVAS_IN_TUBE);
        int tube30SingleDoubleCount = 0;
        // 3 canvas of 30cm also go into 40 multiple tubes
        tube40MultipleCount += (int) Math.floor(canvas30Count / MAXIMUM_CANVAS_IN_TUBE);
        // When remaining 1 to 3 canvases
        if (totalRemainderCount > 0 && totalRemainderCount < 4) {
            if (canvas50RemainderCount > 0) {
                // It only takes one 50cm canvas with any other canvas to impose the use of 50cm multiple tube
                if (totalRemainderCount > 1) {
                    tube50MultipleCount++;
                } else {
                    tube50SingleCount++;
                }
            } else {
                if (totalRemainderCount > 1) {
                    tube40MultipleCount++;
                } else {
                    if (canvas40RemainderCount > 0) {
                        tube40SingleCount++;
                    } else if (canvas30RemainderCount > 0){
                        tube30SingleDoubleCount++;
                    }
                }
            }
        } else if (totalRemainderCount >= 4) {
            // When remaining 4 to 6 canvases, one 50cm canvas imposes one 50cm multiple tube
            if (canvas50RemainderCount > 0) {
                tube50MultipleCount++;
                // If we have two 50cm canvases and a total of 5 of 6 canvases, then we have to use 2 50cm multiple tubes
                if (totalRemainderCount > 4) {
                    tube50MultipleCount++;
                } else {
                    // There's one remaining canvas, an adequate single tube suffices
                    if (canvas50RemainderCount > 1) {
                        tube50SingleCount++;
                    } else if (canvas40RemainderCount > 0) {
                        tube40SingleCount++;
                    } else if (canvas30RemainderCount > 0){
                        tube30SingleDoubleCount++;
                    }
                }
            } else {
                // No 50cm canvases means only one combination possible: two 40cm canvases and two 30cm canvases
                tube40MultipleCount++;
                tube30SingleDoubleCount++;
            }
        }

        if (tube50SingleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_50_SKU_SINGLE, tube50SingleCount));
        }
        if (tube50MultipleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_50_SKU_MULTIPLE, tube50MultipleCount));
        }
        if (tube40SingleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_40_SKU_SINGLE, tube40SingleCount));
        }
        if (tube40MultipleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_40_SKU_MULTIPLE, tube40MultipleCount));
        }
        if (tube30SingleDoubleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_30_SKU_SINGLE_DOUBLE, tube30SingleDoubleCount));
        }
        return Pair.of(currentTubes, adequateTubes);
    }

    public static void main(String[] args) {
        List<OrderItem> orderItems = buildOrderItems(1, 0);
        compare(orderItems);

        orderItems = buildOrderItems(0, 1);
        compare(orderItems);

        orderItems = buildOrderItems(1, 1);
        compare(orderItems);

        orderItems = buildOrderItems(2, 0);
        compare(orderItems);

        orderItems = buildOrderItems(0, 2);
        compare(orderItems);

        orderItems = buildOrderItems(2, 1);
        compare(orderItems);

        orderItems = buildOrderItems(1, 2);
        compare(orderItems);

        orderItems = buildOrderItems(2, 2);
        compare(orderItems);

        orderItems = buildOrderItems(3, 0);
        compare(orderItems);

        orderItems = buildOrderItems(0, 3);
        compare(orderItems);

        orderItems = buildOrderItems(3, 1);
        compare(orderItems);

        orderItems = buildOrderItems(1, 3);
        compare(orderItems);

        orderItems = buildOrderItems(3, 2);
        compare(orderItems);

        orderItems = buildOrderItems(2, 3);
        compare(orderItems);

        orderItems = buildOrderItems(3, 3);
        compare(orderItems);

        orderItems = buildOrderItems(4, 0);
        compare(orderItems);

        orderItems = buildOrderItems(0, 4);
        compare(orderItems);

        orderItems = buildOrderItems(4, 1);
        compare(orderItems);

        orderItems = buildOrderItems(1, 4);
        compare(orderItems);

        orderItems = buildOrderItems(4, 2);
        compare(orderItems);

        orderItems = buildOrderItems(2, 4);
        compare(orderItems);

        orderItems = buildOrderItems(4, 3);
        compare(orderItems);

        orderItems = buildOrderItems(3, 4);
        compare(orderItems);

        orderItems = buildOrderItems(4, 4);
        compare(orderItems);

        orderItems = buildOrderItems(5, 0);
        compare(orderItems);

        orderItems = buildOrderItems(0, 5);
        compare(orderItems);

        orderItems = buildOrderItems(5, 1);
        compare(orderItems);

        orderItems = buildOrderItems(1, 5);
        compare(orderItems);

        orderItems = buildOrderItems(5, 2);
        compare(orderItems);

        orderItems = buildOrderItems(2, 5);
        compare(orderItems);

        orderItems = buildOrderItems(5, 3);
        compare(orderItems);

        orderItems = buildOrderItems(3, 5);
        compare(orderItems);

        orderItems = buildOrderItems(5, 4);
        compare(orderItems);

        orderItems = buildOrderItems(4, 5);
        compare(orderItems);

        orderItems = buildOrderItems(5, 5);
        compare(orderItems);

    }

    private static void compare(List<OrderItem> orderItems) {
        HashSet<Pair<String, Integer>> tubesOldWay = findCurrentAndAdequateTubes(orderItems).getRight();
        HashSet<Pair<String, Integer>> tubesNewWay = newFindCurrentAndAdequateTubes(orderItems).getRight();
        System.out.println("old way : " + tubesOldWay);
        System.out.println("new way : " + tubesNewWay);
        System.out.println(tubesOldWay.containsAll(tubesNewWay) && tubesNewWay.containsAll(tubesOldWay) ? "we're good" : "shit");
    }

    private static List<OrderItem> buildOrderItems(Integer canvas40q, Integer canvas50q) {
        List<OrderItem> orderItems = new ArrayList<>();
        if (canvas40q != null) {
            orderItems.add(buildOrderItem(PREFIX_40_CANVAS, canvas40q));
        }
        if (canvas50q != null) {
            orderItems.add(buildOrderItem(PREFIX_50_CANVAS, canvas50q));
        }
        return orderItems;
    }

    private static OrderItem buildOrderItem(String erpCode, Integer quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setErpCode(erpCode);
        orderItem.setQuantity(quantity);
        return orderItem;
    }

}
