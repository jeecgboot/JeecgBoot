package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.OrderStatus;
import org.jeecg.modules.business.domain.api.mabang.orderDoDeliverOrder.OrderDeliverRequest;
import org.jeecg.modules.business.domain.api.mabang.orderDoDeliverOrder.OrderDeliverRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderDoDeliverOrder.OrderDeliverResponse;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShoumanOrderDeliverToMabangJob implements Job {

    @Autowired
    private IPlatformOrderService platformOrderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Started Shouman order deliver-to-Mabang job");
        List<PlatformOrder> platformOrders = platformOrderService.findShoumanShippedButMabangUnshippedOrders();
        log.info("Retrieved {} orders shipped by Shouman but not yet shipped in Mabang", platformOrders.size());

        List<String> shippedPlatformOrderIds = new ArrayList<>();
        for (PlatformOrder platformOrder : platformOrders) {
            String platformOrderId = platformOrder.getPlatformOrderId();
            log.info("Started marking order {} as shipped in Mabang", platformOrderId);
            try {
                OrderDeliverRequest request = new OrderDeliverRequest(new OrderDeliverRequestBody(platformOrder));
                OrderDeliverResponse response = request.send();
                if (response == null) {
                    log.warn("Mabang deliver-order returned null response for {}", platformOrderId);
                    continue;
                }
                if (!response.success()) {
                    log.warn("Mabang deliver-order failed for {}, message={}", platformOrderId, response.getMessage());
                    continue;
                }
                shippedPlatformOrderIds.add(platformOrderId);
                log.info("Finished marking order {} as shipped in Mabang, message={}", platformOrderId, response.getMessage());
            } catch (Exception e) {
                log.error("Failed to mark order {} as shipped in Mabang", platformOrderId, e);
            }
        }

        if (!shippedPlatformOrderIds.isEmpty()) {
            log.info("Started updating {} local platform orders to shipped status", shippedPlatformOrderIds.size());
            platformOrderService.batchUpdateErpStatusByPlatformOrderIds(
                    shippedPlatformOrderIds,
                    OrderStatus.Shipped.getCodeInt()
            );
            log.info("Finished updating local platform orders to shipped status");
        }

        log.info("Finished Shouman order deliver-to-Mabang job");
    }
}
