package org.jeecg.modules.business.domain.api.mabang.getorderlist;

import com.alibaba.fastjson.JSONArray;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.domain.remote.RemoteFileSystem;
import org.jeecg.modules.business.service.IPlatformOrderMabangService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class RetrieveOrderListJob implements Job {

    @Autowired
    @Setter
    private IPlatformOrderMabangService platformOrderMabangService;

    private final static Duration EXECUTION_DURATION = Duration.ofMinutes(300);

    @Value("${jeecg.s3.upload.prefix}")
    private String dir;

    @Value("${jeecg.s3.key.access}")
    private String ACCESS_KEY;

    @Value("${jeecg.s3.key.secret}")
    private String SECRET_KEY;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("Looking for new orders");
            updateNewOrder();
        } catch (OrderListRequestErrorException | IOException e) {
            throw new RuntimeException(e);
        }

//        try {
//            log.info("Looking for merged orders");
//            updateMergedOrder();
//        } catch (OrderListRequestErrorException e) {
//            throw new RuntimeException(e);
//        }

    }

    /**
     * Retrieve newly paid order from Mabang, upload them to remote.
     * Duration is specified by {@code EXECUTION_DURATION}
     */
    public void updateNewOrder() throws OrderListRequestErrorException, IOException {
        // request time parameter period
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime begin = end.minus(EXECUTION_DURATION);

        // sent request for newly paid orders
        OrderListRequestBody body = new OrderListRequestBody();
        body.setDatetimeType(DateType.PAID)
                .setStartDate(begin)
                .setEndDate(end)
                .setStatus(OrderStatus.AllUnshipped);
        OrderListRawStream rawStream = new OrderListRawStream(body);
        // get data in json array format
        List<OrderListResponse> rawStreamAll = rawStream.all();
        log.info(rawStreamAll.toString());
        List<JSONArray> rawData = rawStreamAll.stream()
                .map(OrderListResponse::getData)
                .collect(Collectors.toList());
        log.info("Raw data {}", rawData);
        /* For each array, we will save them as an individual file to remote */
        // Create temporary file to contain data.
        Path out = Files.createTempFile("json", "json");
        // Establish connexion with remote
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        RemoteFileSystem fileSystem = new RemoteFileSystem(credentials, dir);
        // uploading file prefix format
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd=hh:mm:ss");
        Date now = new Date();
        for (int i = 0; i < rawData.size(); i++) {
            // concatenate remote file name
            String name = format.format(now) + "-" + i + ".json";
            log.info("Copying file {}", name);
            JSONArray data = rawData.get(i);
            /* Overwrite the local temporary file at each time */
            BufferedWriter writer = Files.newBufferedWriter(out);
            writer.write(data.toJSONString());
            writer.close();
            fileSystem.cp(name, out.toFile());
        }
    }

    /**
     * Retrieve newly changed order and merge them.
     * Duration is specified by {@code EXECUTION_DURATION}
     */
    public void updateMergedOrder() throws OrderListRequestErrorException {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime begin = end.minus(EXECUTION_DURATION);

        // Query orders that updated in a certain duration of time in the past.
        OrderListRequestBody updatedOrderBody = new OrderListRequestBody();
        updatedOrderBody
                .setStartDate(begin)
                .setEndDate(end)
                .setDatetimeType(DateType.UPDATE)
                .setStatus(OrderStatus.Pending);

        OrderListRawStream rawStream = new OrderListRawStream(updatedOrderBody);
        OrderListStream stream = new OrderListStream(rawStream);
        List<Order> updatedOrders = stream.all();

        log.debug("Size of updated order:{}", updatedOrders.size());

        // filter orders that are merged
        List<Order> mergedOrders = updatedOrders.stream()
                .filter(Order::isUnion)
                .collect(Collectors.toList());
        log.info("Size of merged order:{}", mergedOrders.size());
        // search other source orders of the merged order and make a map

        Map<Order, Set<String>> mergedOrderToSourceOrdersErpId = mergedOrders.stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                o -> o.getOrderItems().stream()
                                        .map(OrderItem::getOriginOrderId)
                                        .filter(
                                                originOrderId -> !originOrderId.equals(o.getErpOrderId())
                                        )
                                        .collect(Collectors.toSet())

                        )
                );

        // update in DB
        mergedOrderToSourceOrdersErpId.forEach(platformOrderMabangService::updateMergedOrderFromMabang);
    }
}
