package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.shouman.OrderCreationRequestBody;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrder;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrderBase;
import org.jeecg.modules.business.mapper.PlatformOrderContentMapper;
import org.jeecg.modules.business.service.IShoumanOrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ShoumanOrderCreationJob implements Job {

    @Autowired
    private PlatformOrderContentMapper platformOrderContentMapper;
    @Autowired
    private IShoumanOrderService shoumanOrderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Started Shouman order creation job");

        List<ShoumanOrderBase> shoumanOrderBases = platformOrderContentMapper.searchShoumanOrderContent();
        log.info("Fetched {} shouman order contents", shoumanOrderBases.size());
        Map<String, ShoumanOrderBase> groupedByPlatformOrderId = shoumanOrderBases
                .stream()
                .collect(Collectors.toMap(ShoumanOrderBase::getPlatformOrderId, Function.identity()));
        log.info("After grouping by PlatformOrderId, {} Shouman Orders should be created", groupedByPlatformOrderId.size());

        log.info("Started constructing Shouman request bodies");
        List<ShoumanOrder> shoumanOrders = new ArrayList<>();
        for (Map.Entry<String, ShoumanOrderBase> entry : groupedByPlatformOrderId.entrySet()) {
            OrderCreationRequestBody requestBody = new OrderCreationRequestBody(entry.getValue());
            ShoumanOrder shoumanOrder = new ShoumanOrder();
            shoumanOrder.setOrderJson(requestBody.parameters().toJSONString());
            shoumanOrder.setPlatformOrderId(entry.getKey());
            shoumanOrder.setCreateBy("shouman job");
            shoumanOrders.add(shoumanOrder);
        }
        log.info("Finished constructing Shouman request bodies");

        log.info("{} shouman orders to be inserted into DB", shoumanOrders.size());
        shoumanOrderService.saveBatch(shoumanOrders);

        log.info("Finished Shouman order creation job");
    }

}
