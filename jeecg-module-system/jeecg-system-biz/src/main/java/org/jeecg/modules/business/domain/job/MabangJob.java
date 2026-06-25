package org.jeecg.modules.business.domain.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequest;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderAbnormal.OrderSuspendResponse;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.service.IShopService;
import org.jeecg.modules.business.vo.Responses;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.jeecg.modules.business.domain.api.mabang.getorderlist.OrderStatus.*;

@Setter
@Component
@Slf4j
public class MabangJob implements Job {

    @Autowired
    private IPlatformOrderMabangService platformOrderMabangService;

    @Autowired
    private IPlatformOrderService platformOrderService;

    @Autowired
    private IShopService shopService;

    private static final Integer DEFAULT_NUMBER_OF_DAYS = 5;
    private static final Integer SKIP_RECENT_MINUTES = 5;
    private static final DateType DEFAULT_DATE_TYPE = DateType.EXPRESS;
    private static final String ABNORMAL_TYPE_LABEL = "订单在平台已发货";
    private static final List<OrderStatus> DEFAULT_STATUSES = Arrays.asList(AllUnshipped, Shipped, Completed);
    private static final Integer DEFAULT_NUMBER_OF_THREADS = 2;
    private static final Integer MABANG_API_RATE_LIMIT_PER_MINUTE = 10;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime endDateTime = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).minusMinutes(SKIP_RECENT_MINUTES);
        LocalDateTime startDateTime = endDateTime.minusDays(DEFAULT_NUMBER_OF_DAYS);
        DateType dateType = DEFAULT_DATE_TYPE;
        boolean overrideRestriction = false;
        List<OrderStatus> statuses = DEFAULT_STATUSES;
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                JSONArray statusCodes = jsonObject.getJSONArray("status");
                if (null != statusCodes) {
                    statuses = new ArrayList<>();
                    for (int i = 0; i < statusCodes.length(); i++) {
                        statuses.add(OrderStatus.fromCode(statusCodes.getInt(i)));
                    }
                }
                if (!jsonObject.isNull("startDateTime")) {
                    String startDateStr = jsonObject.getString("startDateTime");
                    startDateTime = LocalDateTime.parse(startDateStr);
                }
                if (!jsonObject.isNull("endDateTime")) {
                    String endDateStr = jsonObject.getString("endDateTime");
                    endDateTime = LocalDateTime.parse(endDateStr);
                }
                if (!jsonObject.isNull("dateType")) {
                    dateType = DateType.fromCode(jsonObject.getInt("dateType"));
                }
                if (!jsonObject.isNull("override")) {
                    overrideRestriction = jsonObject.getBoolean("override");
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }

        if (!endDateTime.isAfter(startDateTime)) {
            throw new RuntimeException("EndDateTime must be strictly greater than StartDateTime !");
        } else if (endDateTime.minusDays(30).isAfter(startDateTime) && !overrideRestriction) {
            throw new RuntimeException("No more than 30 days can separate startDateTime and endDateTime !");
        }

        List<String> fulfilledOrderIds = new ArrayList<>();
        try {
            while (startDateTime.until(endDateTime, ChronoUnit.HOURS) > 0) {
                LocalDateTime dayBeforeEndDateTime = endDateTime.minusDays(1);
                for (OrderStatus status : statuses) {
                    OrderListRequestBody body = OrderListRequestBodys
                            .allOrderOfDateTypeOfStatus(dayBeforeEndDateTime, endDateTime, dateType, status);
                    OrderListRawStream rawStream = new OrderListRawStream(body);
                    OrderListStream stream = new OrderListStream(rawStream);
                    List<Order> unshipped = stream.all();
                    log.info("{} {} orders from {} to {} ({})to be inserted/updated.", unshipped.size(), status,
                            dayBeforeEndDateTime, endDateTime, dateType);
                    platformOrderMabangService.saveOrderFromMabang(unshipped);
                    fulfilledOrderIds.addAll(
                            unshipped.stream()
                                    .filter(order -> !order.isResend() && (order.isPending() || order.isPreparing()))
                                    .filter(Order::isFulfilled)
                                    .map(Order::getPlatformOrderId)
                                    .collect(Collectors.toList()));
                }
                endDateTime = dayBeforeEndDateTime;
            }
        } catch (OrderListRequestErrorException e) {
            throw new RuntimeException(e);
        }

        try {
            ExecutorService throttlingExecutorService = ThrottlingExecutorService.createExecutorService(
                    DEFAULT_NUMBER_OF_THREADS,
                    MABANG_API_RATE_LIMIT_PER_MINUTE,
                    TimeUnit.MINUTES);
            Responses responses = new Responses();
            List<String> orderIdsToSuspend = filterOrdersAllowedToSetAbnormal(fulfilledOrderIds);
            log.info("{} orders are at least partially fulfilled by third party, suspending those orders now.",
                    orderIdsToSuspend.size());
            List<CompletableFuture<Responses>> futures = orderIdsToSuspend.stream()
                    .map(id -> CompletableFuture.supplyAsync(() -> {
                        OrderSuspendRequestBody body = new OrderSuspendRequestBody(
                                id,
                                ABNORMAL_TYPE_LABEL,
                                "mabangJob"
                        );
                        OrderSuspendRequest request = new OrderSuspendRequest(body);
                        OrderSuspendResponse response = request.send();
                        Responses r = new Responses();
                        if (response.success())
                            r.addSuccess(id);
                        else
                            r.addFailure(id);
                        return r;
                    }, throttlingExecutorService))
                    .collect(toList());
            List<Responses> results = futures.stream().map(CompletableFuture::join).collect(toList());
            results.forEach(r -> {
                responses.getSuccesses().addAll(r.getSuccesses());
                responses.getFailures().addAll(r.getFailures());
            });
            log.info("{}/{} orders suspended successfully by {}.", responses.getSuccesses().size(), orderIdsToSuspend.size(), "mabangJob");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> filterOrdersAllowedToSetAbnormal(List<String> fulfilledOrderIds) {
        if (fulfilledOrderIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> distinctFulfilledOrderIds = new ArrayList<>(new LinkedHashSet<>(fulfilledOrderIds));
        List<PlatformOrder> localOrders = platformOrderService.lambdaQuery()
                .select(PlatformOrder::getPlatformOrderId, PlatformOrder::getAlreadySetAbnormal, PlatformOrder::getShopId)
                .in(PlatformOrder::getPlatformOrderId, distinctFulfilledOrderIds)
                .list();
        Set<String> alreadySetAbnormalOrderIds = localOrders.stream()
                .filter(order -> "1".equals(order.getAlreadySetAbnormal()))
                .map(PlatformOrder::getPlatformOrderId)
                .collect(Collectors.toSet());
        Set<String> skipPlatformFulfilledAbnormalShopIds = getSkipPlatformFulfilledAbnormalShopIds(localOrders);
        Set<String> shopSkippedOrderIds = localOrders.stream()
                .filter(order -> skipPlatformFulfilledAbnormalShopIds.contains(order.getShopId()))
                .map(PlatformOrder::getPlatformOrderId)
                .collect(Collectors.toSet());

        if (!alreadySetAbnormalOrderIds.isEmpty()) {
            log.info("{} fulfilled orders already set abnormal, skipping suspension: {}",
                    alreadySetAbnormalOrderIds.size(), alreadySetAbnormalOrderIds);
        }
        if (!shopSkippedOrderIds.isEmpty()) {
            log.info("{} fulfilled orders belong to shops configured to skip platform fulfilled abnormal, skipping suspension: {}",
                    shopSkippedOrderIds.size(), shopSkippedOrderIds);
        }

        return distinctFulfilledOrderIds.stream()
                .filter(id -> !shopSkippedOrderIds.contains(id))
                .filter(id -> !alreadySetAbnormalOrderIds.contains(id))
                .collect(toList());
    }

    private Set<String> getSkipPlatformFulfilledAbnormalShopIds(List<PlatformOrder> localOrders) {
        List<String> shopIds = localOrders.stream()
                .map(PlatformOrder::getShopId)
                .filter(id -> id != null && !id.trim().isEmpty())
                .distinct()
                .collect(toList());
        if (shopIds.isEmpty()) {
            return Collections.emptySet();
        }

        return shopService.lambdaQuery()
                .select(Shop::getId, Shop::getSkipPlatformFulfilledAbnormal)
                .in(Shop::getId, shopIds)
                .eq(Shop::getSkipPlatformFulfilledAbnormal, "1")
                .list()
                .stream()
                .map(Shop::getId)
                .collect(Collectors.toSet());
    }

}
