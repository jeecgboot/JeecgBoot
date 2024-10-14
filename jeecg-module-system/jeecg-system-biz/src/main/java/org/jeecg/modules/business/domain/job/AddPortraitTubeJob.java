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
import org.jeecg.modules.business.entity.PlatformOrder;
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
    // TODO 2024-08-28 To delete when no longer in use
    private static final String TUBE_40_SKU_MULTIPLE = "PJ95430040-WIA";
    private static final String TUBE_50_SKU_SINGLE = "PJ95530032-WIA";
    // TODO 2024-08-28 To delete when no longer in use
    private static final String TUBE_50_SKU_MULTIPLE = "PJ95530040-WIA";

//    private static final String TUBE_NEW_40_SKU_SINGLE = "PJ349400032-JCH";
    private static final String TUBE_NEW_40_SKU_MULTIPLE = "PJ349400045-JCH";
//    private static final String TUBE_NEW_50_SKU_SINGLE = "PJ349500032-JCH";
    private static final String TUBE_NEW_50_SKU_MULTIPLE = "PJ349500045-JCH";
    private static final String TUBE_NEW_60_SKU_SINGLE = "PJ349600032-JCH";
    private static final String TUBE_NEW_60_SKU_MULTIPLE = "PJ349600045-JCH";

    private static final List<String> TUBE_SKUS = Arrays.asList(TUBE_30_SKU_SINGLE_DOUBLE, TUBE_50_SKU_SINGLE,
            TUBE_40_SKU_SINGLE, TUBE_NEW_40_SKU_MULTIPLE, TUBE_NEW_50_SKU_MULTIPLE, TUBE_NEW_60_SKU_SINGLE,
            TUBE_NEW_60_SKU_MULTIPLE, TUBE_40_SKU_MULTIPLE, TUBE_50_SKU_MULTIPLE);
    private static final String PREFIX_50_CANVAS = "JJ2501";
    private static final String PREFIX_50_CANVAS_CHROME = "JJ2001";
    private static final String PREFIX_40_CANVAS = "JJ2500";
    private static final String PREFIX_40_CANVAS_CHROME = "JJ2000";
    private static final String PREFIX_30_CANVAS = "JJ2502";
    private static final String PREFIX_30_CANVAS_CHROME = "JJ2002";
    private static final String PREFIX_NEW_56_CANVAS = "JJ314VF02";
    private static final String PREFIX_NEW_46_CANVAS = "JJ314VF01";
    private static final String PREFIX_NEW_36_CANVAS = "JJ314VF00";

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

        List<PlatformOrder> platformOrders = platformOrderService.fetchUninvoicedOrdersForShops(startDateTime, endDateTime, shops);
        List<String> platformOrderIds = platformOrders.stream().map(PlatformOrder::getPlatformOrderId).collect(Collectors.toList());
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
                ChangeOrderRequestBody changeOrderRequestBody = ChangeOrderRequestBody.buildChangeOrderRequestBody(
                        mabangOrder.getPlatformOrderId(), null, currentTubes, adequateTubes, null);
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
    private Pair<HashSet<Pair<String, Integer>>, HashSet<Pair<String, Integer>>> findCurrentAndAdequateTubes(List<OrderItem> orderItems) {
        int canvas30Count = 0;
        int canvas40Count = 0;
        int canvas50Count = 0;
        int canvasNew36Count = 0;
        int canvasNew46Count = 0;
        int canvasNew56Count = 0;
        HashSet<Pair<String, Integer>> currentTubes = new HashSet<>();
        HashSet<Pair<String, Integer>> adequateTubes = new HashSet<>();
        for (OrderItem orderItem : orderItems) {
            String sku = orderItem.getErpCode();
            int quantity = orderItem.getQuantity();
            if (TUBE_SKUS.contains(sku)) {
                currentTubes.add(Pair.of(sku, quantity));
            } else if (sku.startsWith(PREFIX_50_CANVAS) || sku.startsWith(PREFIX_50_CANVAS_CHROME)) {
                canvas50Count += quantity;
            } else if (sku.startsWith(PREFIX_40_CANVAS) || sku.startsWith(PREFIX_40_CANVAS_CHROME)) {
                canvas40Count += quantity;
            } else if (sku.startsWith(PREFIX_30_CANVAS) || sku.startsWith(PREFIX_30_CANVAS_CHROME)) {
                canvas30Count += quantity;
            } else if (sku.startsWith(PREFIX_NEW_36_CANVAS)) {
                canvasNew36Count += quantity;
            } else if (sku.startsWith(PREFIX_NEW_46_CANVAS)) {
                canvasNew46Count += quantity;
            } else if (sku.startsWith(PREFIX_NEW_56_CANVAS)) {
                canvasNew56Count += quantity;
            }
        }

        int canvas30RemainderCount = canvas30Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int canvas40RemainderCount = canvas40Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int canvas50RemainderCount = canvas50Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int canvasNew36RemainderCount = canvasNew36Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int canvasNew46RemainderCount = canvasNew46Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int canvasNew56RemainderCount = canvasNew56Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
        int totalRemainderCount = canvas30RemainderCount + canvas40RemainderCount + canvas50RemainderCount +
                canvasNew36RemainderCount + canvasNew46RemainderCount + canvasNew56RemainderCount;
        int tube50SingleCount = 0;
        int tubeNew60MultipleCount = (int) Math.floor(canvas50Count / MAXIMUM_CANVAS_IN_TUBE);
        int tube40SingleCount = 0;
        int tubeNew50MultipleCount = (int) Math.floor(canvas40Count / MAXIMUM_CANVAS_IN_TUBE);
        int tube30SingleDoubleCount = 0;
        int tubeNew40MultipleCount = (int) Math.floor(canvas30Count / MAXIMUM_CANVAS_IN_TUBE);

        int tubeNew60SingleCount = 0;
        tubeNew60MultipleCount += (int) Math.floor(canvasNew56Count / MAXIMUM_CANVAS_IN_TUBE);
        tubeNew50MultipleCount += (int) Math.floor(canvasNew46Count / MAXIMUM_CANVAS_IN_TUBE);
        tubeNew40MultipleCount += (int) Math.floor(canvasNew36Count / MAXIMUM_CANVAS_IN_TUBE);

        // When remaining 1 to 3 canvases
        if (totalRemainderCount > 0 && totalRemainderCount < 4) {
            if (canvas50RemainderCount > 0 || canvasNew56RemainderCount > 0) {
                // It only takes one 50cm/56cm canvas with any other canvas to impose the use of NEW 60cm multiple tube
                if (totalRemainderCount > 1) {
                    tubeNew60MultipleCount++;
                } else if (canvasNew56RemainderCount > 0) {
                    // Only NEW 60cm tubes can contain NEW 56cm canvases
                    tubeNew60SingleCount++;
                } else {
                    tube50SingleCount++;
                }
            } else {
                // No 50/56cm canvases
                if (totalRemainderCount > 1) {
                    // It only takes one 40cm/46cm canvas with any other canvas to impose the use of NEW 50cm multiple tube
                    if (canvas40RemainderCount > 0 || canvasNew46RemainderCount > 0) {
                        tubeNew50MultipleCount++;
                    } else if (canvasNew36RemainderCount > 0) {
                        tubeNew40MultipleCount++;
                    } else {
                        tube30SingleDoubleCount++;
                    }
                } else {
                    if (canvasNew46RemainderCount > 0) {
                        // TODO 2024-08-28 Temporarily use OLD 50cm tubes for NEW 46cm canvases
                        tube50SingleCount++;
                    } else if (canvas40RemainderCount > 0 || canvasNew36RemainderCount > 0) {
                        tube40SingleCount++;
                    } else if (canvas30RemainderCount > 0){
                        tube30SingleDoubleCount++;
                    }
                }
            }
        } else if (totalRemainderCount >= 4) {
            // When remaining 4 to 6 canvases, one 50/56cm canvas imposes one NEW 60cm multiple tube
            if (canvas50RemainderCount > 0 || canvasNew56RemainderCount > 0) {
                tubeNew60MultipleCount++;
                if (canvas50RemainderCount + canvasNew56RemainderCount > 1) {
                    // If we have two 50/56cm canvases and a total of 5 of 6 canvases
                    if (totalRemainderCount > 4) {
                        if (canvas40RemainderCount > 1 || canvasNew46RemainderCount > 1) {
                            tubeNew50MultipleCount++;
                        } else {
                            tube30SingleDoubleCount++;
                        }
                    } else {
                        if (canvasNew46RemainderCount > 1) {
                            // TODO 2024-08-28 Temporarily use OLD 50cm tubes for NEW 46cm canvases
                            tube50SingleCount++;
                        } else if (canvas40RemainderCount > 1) {
                            tube40SingleCount++;
                        } else {
                            tube30SingleDoubleCount++;
                        }
                    }
                } else {
                    // Only case possible : 1 * 50/56cm, 2 * 40/46cm, 2 * 30/36cm
                    if (canvasNew36RemainderCount > 0) {
                        tubeNew40MultipleCount++;
                    } else {
                        tube30SingleDoubleCount++;
                    }
                }
            } else {
                // No 50/56cm canvases : only 30,36,40,46cm canvases, each type having no more than 2
                if (canvas40RemainderCount > 0 || canvasNew46RemainderCount > 0) {
                    // Any 40/46cm canvas means at least one NEW 50cm multiple tube is needed
                    tubeNew50MultipleCount++;
                    if (canvas40RemainderCount > 1 && canvasNew46RemainderCount > 1) {
                        if (totalRemainderCount > 4) {
                            tubeNew50MultipleCount++;
                        } else {
                            tubeNew40MultipleCount++;
                        }
                    } else if (canvasNew36RemainderCount > 0 && totalRemainderCount > 4) {
                        tubeNew40MultipleCount++;
                    } else {
                        tube30SingleDoubleCount++;
                    }
                } else {
                    // Only 30/36cm canvases, meaning only one possible case : two 30cm and two 36cm canvases
                    tubeNew40MultipleCount++;
                    tube30SingleDoubleCount++;
                }
            }
        }

        if (tube50SingleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_50_SKU_SINGLE, tube50SingleCount));
        }
        if (tubeNew60MultipleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_NEW_60_SKU_MULTIPLE, tubeNew60MultipleCount));
        }
        if (tube40SingleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_40_SKU_SINGLE, tube40SingleCount));
        }
        if (tubeNew50MultipleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_NEW_50_SKU_MULTIPLE, tubeNew50MultipleCount));
        }
        if (tube30SingleDoubleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_30_SKU_SINGLE_DOUBLE, tube30SingleDoubleCount));
        }
        if (tubeNew40MultipleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_NEW_40_SKU_MULTIPLE, tubeNew40MultipleCount));
        }
        if (tubeNew60SingleCount > 0) {
            adequateTubes.add(Pair.of(TUBE_NEW_60_SKU_SINGLE, tubeNew60SingleCount));
        }
        return Pair.of(currentTubes, adequateTubes);
    }
}
