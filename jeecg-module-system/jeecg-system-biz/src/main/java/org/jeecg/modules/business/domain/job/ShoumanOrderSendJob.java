package org.jeecg.modules.business.domain.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.shouman.JsonOrderCreationRequestBody;
import org.jeecg.modules.business.domain.api.shouman.OrderCreationRequest;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrder;
import org.jeecg.modules.business.service.IShoumanOrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ShoumanOrderSendJob implements Job {

    @Autowired
    private IShoumanOrderService shoumanOrderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Started Shouman order send job");
        List<ShoumanOrder> shoumanOrders = shoumanOrderService.findShoumanOrderToSend();
        log.info("Retrieved {} Shouman orders to send to ShoumanAPI", shoumanOrders.size());

        log.info("Started building and sending Shouman order requests");
        for (ShoumanOrder shoumanOrder : shoumanOrders) {
            String platformOrderId = shoumanOrder.getPlatformOrderId();
            log.info("Started building Shouman Order {}", platformOrderId);
            OrderCreationRequest request = new OrderCreationRequest(new JsonOrderCreationRequestBody(shoumanOrder.getOrderJson()));
            log.info("Finished building Shouman Order {}", platformOrderId);
            log.info("Started sending Shouman Order {}", platformOrderId);
            String resultString = request.rawSend(shoumanOrder).getBody();
            log.info("Finished sending Shouman Order {}", platformOrderId);
            JSONObject json = JSON.parseObject(resultString);
            Object status = json.get("status");
            if (status != null) {
                if (((Integer) status) == 1) {
                    log.info("Shouman Order {} ended with success", platformOrderId);
                    shoumanOrder.setSuccess(status.toString());
                } else {
                    log.info("Shouman Order {} failed", platformOrderId);
                }
            }
        }
        log.info("Finished building and sending Shouman order requests");

        log.info("Started updating Shouman Orders in DB");
        shoumanOrderService.updateBatchById(shoumanOrders);
        log.info("Finished updating Shouman Orders in DB");

        log.info("Finished Shouman order send job");
    }

}
