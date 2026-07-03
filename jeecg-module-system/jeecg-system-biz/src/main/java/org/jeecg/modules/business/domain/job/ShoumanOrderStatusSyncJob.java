package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.shouman.OrderStatusRequest;
import org.jeecg.modules.business.domain.api.shouman.OrderStatusRequestBody;
import org.jeecg.modules.business.domain.api.shouman.OrderStatusResponse;
import org.jeecg.modules.business.domain.api.shouman.OrderStatusResultBody;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrder;
import org.jeecg.modules.business.service.IShoumanOrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShoumanOrderStatusSyncJob implements Job {


    @Autowired
    private IShoumanOrderService shoumanOrderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Started Shouman order status sync job");
        List<ShoumanOrder> shoumanOrders = shoumanOrderService.findShoumanOrderWithStatusLessThan6();
        log.info("Retrieved {} Shouman orders with status less than 6", shoumanOrders.size());

        List<ShoumanOrder> ordersToUpdate = new ArrayList<>();
        for (ShoumanOrder shoumanOrder : shoumanOrders) {
            String platformOrderId = shoumanOrder.getPlatformOrderId();
            log.info("Started querying Shouman order status for {}", platformOrderId);

            OrderStatusRequest request = new OrderStatusRequest(new OrderStatusRequestBody(platformOrderId));
            OrderStatusResponse response = request.send();
            if (response == null) {
                log.warn("Shouman order status query returned null response for {}", platformOrderId);
                continue;
            }

            if (response.getStatus() == null || response.getStatus() != 1) {
                log.warn("Shouman order status query failed for {}, response status={}", platformOrderId, response.getStatus());
                continue;
            }

            OrderStatusResultBody resultBody = response.getResultBody();
            if (resultBody == null || resultBody.getOrderStatus() == null) {
                log.warn("Shouman order status query has empty result body for {}", platformOrderId);
                continue;
            }

            shoumanOrder.setStatus(resultBody.getOrderStatus());
            ordersToUpdate.add(shoumanOrder);
            log.info("Finished querying Shouman order status for {}, status={}", platformOrderId, resultBody.getOrderStatus());
        }

        if (!ordersToUpdate.isEmpty()) {
            log.info("Started updating {} Shouman orders in DB", ordersToUpdate.size());
            shoumanOrderService.updateBatchById(ordersToUpdate);
            log.info("Finished updating Shouman orders in DB");
        }

        log.info("Finished Shouman order status sync job");
    }
}
