package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.api.shouman.OrderCreationRequestBody;
import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.entity.Shouman.ShoumanOrder;
import org.jeecg.modules.business.entity.ShoumanOrderContent;
import org.jeecg.modules.business.mapper.CountryMapper;
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

import static java.util.stream.Collectors.toMap;

@Slf4j
public class ShoumanOrderCreationJob implements Job {

    @Autowired
    private PlatformOrderContentMapper platformOrderContentMapper;
    @Autowired
    private IShoumanOrderService shoumanOrderService;
    @Autowired
    private CountryMapper countryMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Started Shouman order creation job");
        Map<String, Country> countryCodeMap = countryMapper.findAll().stream()
                .collect(toMap(Country::getMabangName, Function.identity()));

        List<ShoumanOrderContent> shoumanOrderContents = platformOrderContentMapper.searchShoumanOrderContent();
        log.info("Fetched {} shouman order contents", shoumanOrderContents.size());
        Map<String, List<ShoumanOrderContent>> groupedByPlatformOrderId = shoumanOrderContents
                .stream()
                .collect(Collectors.groupingBy(ShoumanOrderContent::getPlatformOrderId));
        log.info("After grouping by PlatformOrderId, {} Shouman Orders should be created", groupedByPlatformOrderId.size());

        log.info("Started constructing Shouman request bodies");
        List<ShoumanOrder> shoumanOrders = new ArrayList<>();
        for (Map.Entry<String, List<ShoumanOrderContent>> entry : groupedByPlatformOrderId.entrySet()) {
            OrderCreationRequestBody requestBody = new OrderCreationRequestBody(entry.getValue(), countryCodeMap);
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
