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
    private static final Double MAXIMUM_UV_CANVAS_IN_45_TUBE = 4.0;

    private static final List<String> DEFAULT_SHOPS = Arrays.asList("JCH3", "JCH4", "JCH5");
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private static final String TUBE_30_SKU_SINGLE_DOUBLE = "PJ95330032-WIA";

    // 30cm 1-2 40*32
    private static final String TUBE_NEW_40_SKU_SINGLE = "PJ349400032-JCH";
    // 30cm 3-4 40*45
    private static final String TUBE_NEW_40_SKU_MULTIPLE = "PJ349400045-JCH";
    // 40cm 1-2 50*32
    private static final String TUBE_NEW_50_SKU_SINGLE = "PJ349500032-JCH";
    // 40cm 3-4 50*45
    private static final String TUBE_NEW_50_SKU_MULTIPLE = "PJ349500045-JCH";
    // 50cm 1-2 60*32
    // todo 2026-06-25 temporarily changed PJ349600032-JCH -> PJ349600045-JCH, may revert one day
    private static final String TUBE_NEW_60_SKU_SINGLE = "PJ349600045-JCH";
    // 50cm 1-2 60*45
    private static final String TUBE_NEW_60_SKU_DOUBLE = "PJ349600045-JCH";
    private static final String TUBE_NEW_60_SKU_TREBLE = "PJ349600048-JCH";

    private static final List<String> TUBE_SKUS = Arrays.asList(TUBE_30_SKU_SINGLE_DOUBLE, TUBE_NEW_40_SKU_MULTIPLE,
            TUBE_NEW_50_SKU_MULTIPLE, TUBE_NEW_60_SKU_SINGLE, TUBE_NEW_50_SKU_SINGLE,
            TUBE_NEW_60_SKU_DOUBLE, TUBE_NEW_40_SKU_SINGLE, TUBE_NEW_60_SKU_TREBLE);
    private static final String PREFIX_50_CANVAS = "JJ2501";
    private static final String PREFIX_50_CANVAS_CHROME = "JJ2001";
    private static final String PREFIX_40_CANVAS = "JJ2500";
    private static final String PREFIX_40_CANVAS_CHROME = "JJ2000";
    private static final String PREFIX_30_CANVAS = "JJ2502";
    private static final String PREFIX_30_CANVAS_CHROME = "JJ2002";
    // 亚麻棉
    private static final String REGEX_NEW_56_CANVAS = "JJ314.*02.*-JCH";
    private static final String REGEX_NEW_46_CANVAS = "JJ314.*01.*-JCH";
    private static final String REGEX_NEW_36_CANVAS = "JJ314.*00.*-JCH";
    // UV
    private static final String REGEX_UV_30_CANVAS = "JJ989.*00.*-JCH";
    private static final String REGEX_UV_40_CANVAS = "JJ989.*01.*-JCH";
    private static final String REGEX_UV_50_CANVAS = "JJ989.*02.*-JCH";

    /**
     * SKU-to-canvas rule mapping. The order matters: prefix rules should be evaluated before regex rules.
     */
    enum CanvasSkuRule {
        // Prefix rules for standard canvas SKUs
        PREFIX_50(PREFIX_50_CANVAS, null) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvas50Count += quantity;
            }
        },
        PREFIX_50_CHROME(PREFIX_50_CANVAS_CHROME, null) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvas50Count += quantity;
            }
        },
        PREFIX_40(PREFIX_40_CANVAS, null) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvas40Count += quantity;
            }
        },
        PREFIX_40_CHROME(PREFIX_40_CANVAS_CHROME, null) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvas40Count += quantity;
            }
        },
        PREFIX_30(PREFIX_30_CANVAS, null) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvas30Count += quantity;
            }
        },
        PREFIX_30_CHROME(PREFIX_30_CANVAS_CHROME, null) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvas30Count += quantity;
            }
        },
        // Regex rules for special canvases
        REGEX_NEW_36(null, REGEX_NEW_36_CANVAS) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvasNew36Count += quantity;
            }
        },
        REGEX_NEW_46(null, REGEX_NEW_46_CANVAS) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvasNew46Count += quantity;
            }
        },
        REGEX_NEW_56(null, REGEX_NEW_56_CANVAS) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvasNew56Count += quantity;
            }
        },
        REGEX_UV_30(null, REGEX_UV_30_CANVAS) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvasUV30Count += quantity;
            }
        },
        REGEX_UV_40(null, REGEX_UV_40_CANVAS) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvasUV40Count += quantity;
            }
        },
        REGEX_UV_50(null, REGEX_UV_50_CANVAS) {
            @Override
            void apply(CanvasCounts counts, int quantity) {
                counts.canvasUV50Count += quantity;
            }
        };

        private final String prefix;
        private final String regex;

        CanvasSkuRule(String prefix, String regex) {
            this.prefix = prefix;
            this.regex = regex;
        }

        boolean matches(String sku) {
            if (prefix != null) {
                return sku.startsWith(prefix);
            }
            return sku.matches(regex);
        }

        abstract void apply(CanvasCounts counts, int quantity);
    }

    static boolean applyCanvasRules(String sku, int quantity, CanvasCounts counts) {
        for (CanvasSkuRule rule : CanvasSkuRule.values()) {
            if (rule.matches(sku)) {
                rule.apply(counts, quantity);
                return true;
            }
        }
        return false;
    }

    @Autowired
    private IPlatformOrderService platformOrderService;

    private static class JobParams {
        private final LocalDateTime startDateTime;
        private final LocalDateTime endDateTime;
        private final List<String> shops;

        private JobParams(LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> shops) {
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
            this.shops = shops;
        }
    }

    private JobParams resolveJobParams(JobExecutionContext context) {
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
        return new JobParams(startDateTime, endDateTime, shops);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobParams params = resolveJobParams(context);
        LocalDateTime startDateTime = params.startDateTime;
        LocalDateTime endDateTime = params.endDateTime;
        List<String> shops = params.shops;
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
        HashSet<Pair<String, Integer>> currentTubes = new HashSet<>();
        HashSet<Pair<String, Integer>> adequateTubes = new HashSet<>();
        CanvasCounts counts = collectCanvasCounts(orderItems, currentTubes);
        TubeCounts tubes = determineTubeCounts(counts);
        applyUvRemainders(counts, tubes);
        addAdequateTubes(adequateTubes, tubes);
        return Pair.of(currentTubes, adequateTubes);
    }

    private static CanvasCounts collectCanvasCounts(List<OrderItem> orderItems,
                                                    HashSet<Pair<String, Integer>> currentTubes) {
        CanvasCounts counts = new CanvasCounts();
        for (OrderItem orderItem : orderItems) {
            if (orderItem.isObsolete()) continue;
            String sku = orderItem.getErpCode();
            int quantity = orderItem.getQuantity();
            if (TUBE_SKUS.contains(sku)) {
                currentTubes.add(Pair.of(sku, quantity));
            } else {
                applyCanvasRules(sku, quantity, counts);
            }
        }
        counts.computeRemainders();
        return counts;
    }

    private static TubeCounts determineTubeCounts(CanvasCounts counts) {
        TubeCounts tubes = new TubeCounts();
        applyFullCanvasTubes(counts, tubes);
        applyRemainderRules(counts, tubes);
        return tubes;
    }

    private static void applyFullCanvasTubes(CanvasCounts counts, TubeCounts tubes) {
        tubes.tubeNew60TrebleCount = (int) Math.floor(counts.canvas50Count / MAXIMUM_CANVAS_IN_TUBE);
        tubes.tubeNew50MultipleCount = (int) Math.floor(counts.canvas40Count / MAXIMUM_CANVAS_IN_TUBE);
        tubes.tubeNew40MultipleCount = (int) Math.floor(counts.canvas30Count / MAXIMUM_CANVAS_IN_TUBE);

        tubes.tubeNew60DoubleCount = (int) Math.floor(counts.canvasUV50Count / MAXIMUM_UV_CANVAS_IN_45_TUBE);
        tubes.tubeNew60TrebleCount += (int) Math.floor(counts.canvasNew56Count / MAXIMUM_CANVAS_IN_TUBE);
        tubes.tubeNew50MultipleCount += (int) Math.floor(counts.canvasNew46Count / MAXIMUM_CANVAS_IN_TUBE);
        tubes.tubeNew40MultipleCount += (int) Math.floor(counts.canvasNew36Count / MAXIMUM_CANVAS_IN_TUBE);
        tubes.tubeNew40MultipleCount += (int) Math.floor(counts.canvasUV30Count / MAXIMUM_UV_CANVAS_IN_45_TUBE);
        tubes.tubeNew50MultipleCount += (int) Math.floor(counts.canvasUV40Count / MAXIMUM_UV_CANVAS_IN_45_TUBE);
    }

    private static void applyRemainderRules(CanvasCounts counts, TubeCounts tubes) {
        if (counts.totalRemainderCount > 0 && counts.totalRemainderCount < 4) {
            applySmallRemainderRules(counts, tubes);
        } else if (counts.totalRemainderCount >= 4) {
            applyLargeRemainderRules(counts, tubes);
        }
    }

    private static void applySmallRemainderRules(CanvasCounts counts, TubeCounts tubes) {
        if (counts.canvas50RemainderCount > 0 || counts.canvasNew56RemainderCount > 0) {
            // It only takes one 50cm/56cm canvas with any other canvas to impose the use of NEW 60cm multiple tube
            if (counts.totalRemainderCount > 1) {
                // Only if there are 3 56cm/46cm canvases, we need a 60cm TREBLE tube, otherwise 60cm DOUBLE tube suffice
                if (counts.canvasNew56RemainderCount + counts.canvasNew46RemainderCount == 3) {
                    tubes.tubeNew60TrebleCount++;
                } else {
                    tubes.tubeNew60DoubleCount++;
                }
            } else if (counts.canvasNew56RemainderCount > 0) {
                // Only NEW 60cm tubes can contain NEW 56cm canvases
                tubes.tubeNew60SingleCount++;
            } else {
                tubes.tubeNew50SingleCount++;
            }
        } else {
            // No 50/56cm canvases
            if (counts.totalRemainderCount > 1) {
                // It only takes one 40cm/46cm canvas with any other canvas to impose the use of NEW 50cm multiple tube
                if (counts.canvas40RemainderCount > 0 || counts.canvasNew46RemainderCount > 0) {
                    tubes.tubeNew50MultipleCount++;
                } else if (counts.canvasNew36RemainderCount > 0) {
                    tubes.tubeNew40MultipleCount++;
                } else {
                    tubes.tube30SingleDoubleCount++;
                }
            } else {
                if (counts.canvasNew46RemainderCount > 0) {
                    tubes.tubeNew50SingleCount++;
                } else if (counts.canvas40RemainderCount > 0 || counts.canvasNew36RemainderCount > 0) {
                    tubes.tube40SingleCount++;
                } else if (counts.canvas30RemainderCount > 0) {
                    tubes.tube30SingleDoubleCount++;
                }
            }
        }
    }

    private static void applyLargeRemainderRules(CanvasCounts counts, TubeCounts tubes) {
        // When remaining 4 to 6 canvases, one 50/56cm canvas imposes one NEW 60cm multiple tube
        if (counts.canvas50RemainderCount > 0 || counts.canvasNew56RemainderCount > 0) {
            // If there are 3 or more 56cm/46cm canvases, we need a 60cm TREBLE tube, otherwise 60cm DOUBLE tube suffice
            if (counts.canvasNew56RemainderCount + counts.canvasNew46RemainderCount >= 3) {
                tubes.tubeNew60TrebleCount++;
            } else {
                tubes.tubeNew60DoubleCount++;
            }
            if (counts.canvas50RemainderCount + counts.canvasNew56RemainderCount > 1) {
                // If we have two 50/56cm canvases and a total of 5 of 6 canvases
                if (counts.totalRemainderCount > 4) {
                    if (counts.canvas40RemainderCount > 1 || counts.canvasNew46RemainderCount > 1) {
                        tubes.tubeNew50MultipleCount++;
                    } else {
                        tubes.tube30SingleDoubleCount++;
                    }
                } else {
                    if (counts.canvasNew46RemainderCount > 1) {
                        tubes.tubeNew50SingleCount++;
                    } else if (counts.canvas40RemainderCount > 1) {
                        tubes.tube40SingleCount++;
                    } else {
                        tubes.tube30SingleDoubleCount++;
                    }
                }
            } else {
                // Only case possible : 1 * 50/56cm, 2 * 40/46cm, 2 * 30/36cm
                if (counts.canvasNew36RemainderCount > 0) {
                    tubes.tubeNew40MultipleCount++;
                } else {
                    tubes.tube30SingleDoubleCount++;
                }
            }
        } else {
            // No 50/56cm canvases : only 30,36,40,46cm canvases, each type having no more than 2
            if (counts.canvas40RemainderCount > 0 || counts.canvasNew46RemainderCount > 0) {
                // Any 40/46cm canvas means at least one NEW 50cm multiple tube is needed
                tubes.tubeNew50MultipleCount++;
                if (counts.canvas40RemainderCount > 1 && counts.canvasNew46RemainderCount > 1) {
                    if (counts.totalRemainderCount > 4) {
                        tubes.tubeNew50MultipleCount++;
                    } else {
                        tubes.tubeNew40MultipleCount++;
                    }
                } else if (counts.canvasNew36RemainderCount > 0 && counts.totalRemainderCount > 4) {
                    tubes.tubeNew40MultipleCount++;
                } else {
                    tubes.tube30SingleDoubleCount++;
                }
            } else {
                // Only 30/36cm canvases, meaning only one possible case : two 30cm and two 36cm canvases
                tubes.tubeNew40MultipleCount++;
                tubes.tube30SingleDoubleCount++;
            }
        }
    }

    private static void applyUvRemainders(CanvasCounts counts, TubeCounts tubes) {
        // Special case for UV canvases
        if (counts.canvasUV30RemainderCount == 1 || counts.canvasUV30RemainderCount == 2) {
            tubes.tube40SingleCount++;
        }
        if (counts.canvasUV30RemainderCount == 3) {
            tubes.tubeNew40MultipleCount++;
        }
        if (counts.canvasUV40RemainderCount == 1 || counts.canvasUV40RemainderCount == 2) {
            tubes.tubeNew50SingleCount++;
        }
        if (counts.canvasUV40RemainderCount == 3) {
            tubes.tubeNew50MultipleCount++;
        }
        if (counts.canvasUV50RemainderCount == 1 || counts.canvasUV50RemainderCount == 2) {
            tubes.tubeNew60SingleCount++;
        }
        if (counts.canvasUV50RemainderCount == 3) {
            tubes.tubeNew60DoubleCount++;
        }
    }

    private static void addAdequateTubes(HashSet<Pair<String, Integer>> adequateTubes, TubeCounts tubes) {
        addIfPositive(adequateTubes, TUBE_30_SKU_SINGLE_DOUBLE, tubes.tube30SingleDoubleCount);
        addIfPositive(adequateTubes, TUBE_NEW_40_SKU_SINGLE, tubes.tube40SingleCount);
        addIfPositive(adequateTubes, TUBE_NEW_40_SKU_MULTIPLE, tubes.tubeNew40MultipleCount);
        addIfPositive(adequateTubes, TUBE_NEW_50_SKU_SINGLE, tubes.tubeNew50SingleCount);
        addIfPositive(adequateTubes, TUBE_NEW_50_SKU_MULTIPLE, tubes.tubeNew50MultipleCount);
        addIfPositive(adequateTubes, TUBE_NEW_60_SKU_SINGLE, tubes.tubeNew60SingleCount);
        addIfPositive(adequateTubes, TUBE_NEW_60_SKU_DOUBLE, tubes.tubeNew60DoubleCount);
        addIfPositive(adequateTubes, TUBE_NEW_60_SKU_TREBLE, tubes.tubeNew60TrebleCount);
    }

    static class CanvasCounts {
        private int canvas30Count;
        private int canvas40Count;
        private int canvas50Count;
        private int canvasNew36Count;
        private int canvasNew46Count;
        private int canvasNew56Count;
        private int canvasUV30Count;
        private int canvasUV40Count;
        private int canvasUV50Count;

        private int canvas30RemainderCount;
        private int canvas40RemainderCount;
        private int canvas50RemainderCount;
        private int canvasNew36RemainderCount;
        private int canvasNew46RemainderCount;
        private int canvasNew56RemainderCount;
        private int canvasUV30RemainderCount;
        private int canvasUV40RemainderCount;
        private int canvasUV50RemainderCount;
        private int totalRemainderCount;


        int getCanvas50Count() {
            return canvas50Count;
        }

        int getCanvasNew36Count() {
            return canvasNew36Count;
        }

        int getCanvasUV50Count() {
            return canvasUV50Count;
        }

        private void computeRemainders() {
            canvas30RemainderCount = canvas30Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
            canvas40RemainderCount = canvas40Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
            canvas50RemainderCount = canvas50Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
            canvasNew36RemainderCount = canvasNew36Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
            canvasNew46RemainderCount = canvasNew46Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
            canvasNew56RemainderCount = canvasNew56Count % MAXIMUM_CANVAS_IN_TUBE.intValue();
            canvasUV30RemainderCount = canvasUV30Count % MAXIMUM_UV_CANVAS_IN_45_TUBE.intValue();
            canvasUV40RemainderCount = canvasUV40Count % MAXIMUM_UV_CANVAS_IN_45_TUBE.intValue();
            canvasUV50RemainderCount = canvasUV50Count % MAXIMUM_UV_CANVAS_IN_45_TUBE.intValue();
            totalRemainderCount = canvas30RemainderCount + canvas40RemainderCount + canvas50RemainderCount +
                    canvasNew36RemainderCount + canvasNew46RemainderCount + canvasNew56RemainderCount;
        }
    }

    private static class TubeCounts {
        private int tubeNew50SingleCount;
        private int tube40SingleCount;
        private int tube30SingleDoubleCount;
        private int tubeNew60TrebleCount;
        private int tubeNew50MultipleCount;
        private int tubeNew40MultipleCount;
        private int tubeNew60SingleCount;
        private int tubeNew60DoubleCount;
    }

    private static void addIfPositive(HashSet<Pair<String, Integer>> adequateTubes, String sku, int quantity) {
        if (quantity > 0) {
            adequateTubes.add(Pair.of(sku, quantity));
        }
    }
}
