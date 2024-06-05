package org.jeecg.modules.business.domain.job;

import com.google.common.collect.Lists;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.*;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderNormal.OrderToNormalRequest;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderNormal.OrderToNormalRequestBody;
import org.jeecg.modules.business.domain.api.mabang.orderDoOrderNormal.OrderToNormalResponse;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.service.EmailService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class SetOrderToNormalJob implements Job {

    @Autowired
    private EmailService emailService;
    @Autowired
    private IPlatformOrderService platformOrderService;

    @Autowired
    Environment env;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;

    private static final Integer DEFAULT_NUMBER_OF_THREADS = 10;
    private final String WAREHOUSE_NAME = "法国巴黎仓库-唯客路";
    private final String ABNORMAL_LABEL_NAME = "自动创建SKU的订单";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Executing SetOrderToNormalJob...");
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));

        List<String> skus = new ArrayList<>();
        List<String> shops = new ArrayList<>();
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if(!jsonObject.isNull("skus")) {
                    JSONArray skusJsonArray = jsonObject.getJSONArray("skus");
                    for (int i = 0; i < skusJsonArray.length(); i++) {
                        skus.add(skusJsonArray.getString(i));
                    }
                }
                if(!jsonObject.isNull("shops")) {
                    JSONArray shopsJsonArray = jsonObject.getJSONArray("shops");
                    for (int i = 0; i < shopsJsonArray.length(); i++) {
                        shops.add(shopsJsonArray.getString(i));
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        List<String> orderIds = platformOrderService.findReadyAbnormalOrders(skus, shops);
        List<String> orderIdsWithSkus = platformOrderService.findReadyAbnormalOrdersWithSkus(skus);
        List<String> orderReadyWithSkus = orderIds.stream()
            .filter(orderIdsWithSkus::contains)
            .collect(Collectors.toList());
        if(!orderReadyWithSkus.isEmpty()) {
            Map<PlatformOrder, List<PlatformOrderContent>> orderContents = platformOrderService.fetchOrderData(orderReadyWithSkus);
            // Remove orders that contains sku from "skus" list and have wrong warehouse
            for(Map.Entry<PlatformOrder, List<PlatformOrderContent>> entry : orderContents.entrySet()) {
                for(PlatformOrderContent content : entry.getValue()) {
                    if(!content.getWarehouseName().equals(WAREHOUSE_NAME)) {
                        orderIds.remove(content.getPlatformOrderId());
                        break;
                    }
                }
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);

        List<String> platform_order_ids = platformOrderService.listByIds(orderIds).stream()
            .map(PlatformOrder::getPlatformOrderId)
            .collect(Collectors.toList());
        List<Order> mabangOrders = new ArrayList<>();

        List<List<String>> partitionedPlatformOrderIds = Lists.partition(platform_order_ids, 10);
        List<OrderListRequestBody> requests = new ArrayList<>();
        for (List<String> platformOrderIdList : partitionedPlatformOrderIds) {
            requests.add(new OrderListRequestBody().setPlatformOrderIds(platformOrderIdList));
        }
        // Fetch orders from mabang
        List<CompletableFuture<Boolean>> mabangFutures = requests.stream()
            .map(request -> CompletableFuture.supplyAsync(() -> {
                boolean success = false;
                OrderListRawStream rawStream = new OrderListRawStream(request);
                OrderListStream stream = new OrderListStream(rawStream);
                List<Order> orders = stream.all();
                mabangOrders.addAll(orders);
                success = !orders.isEmpty();
                return success;
            }, executor))
            .collect(Collectors.toList());
        List<Boolean> fetchResults = mabangFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long fetchSuccessCount = fetchResults.stream().filter(Boolean::booleanValue).count();
        log.info("Successfully fetched {} out of {} orders groups from mabang.", fetchSuccessCount, fetchResults.size());

        List<String> poIdsToSetNormal = new ArrayList<>();
        if(!mabangOrders.isEmpty()) {
            for(Order order : mabangOrders) {
                OrderTypeLabel[] labels = order.getOrderType().getAbnormalLabels();
                if(labels.length > 1) {
                    continue;
                }
                if(labels[0].getName().equals(ABNORMAL_LABEL_NAME)) {
                    poIdsToSetNormal.add(order.getPlatformOrderId());
                }
            }
        }

        List<CompletableFuture<Boolean>> futures = poIdsToSetNormal.stream()
            .map(orderId -> CompletableFuture.supplyAsync(() -> {
                log.info("Setting order {} to normal", orderId);
                OrderToNormalRequestBody requestBody = new OrderToNormalRequestBody(orderId);
                OrderToNormalRequest request = new OrderToNormalRequest(requestBody);
                OrderToNormalResponse response = request.send();
                return response.success();
            }, executor))
            .collect(Collectors.toList());
        List<Boolean> results = futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
        long successCount = results.stream().filter(Boolean::booleanValue).count();
        log.info("Successfully set {}/{} orders to normal", successCount, poIdsToSetNormal.size());

        executor.shutdown();

        // send list of order ids by mail
        if(!poIdsToSetNormal.isEmpty()) {
            String subject = "[" + LocalDate.now() + "] Set Orders To Normal Job Report";
            String destEmail = env.getProperty("spring.mail.username");
            Properties prop = emailService.getMailSender();
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("orderIds", poIdsToSetNormal);
            templateModel.put("count", poIdsToSetNormal.size());
            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
                }
            });
            try {
                freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
                Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                        .getTemplate("admin/ordersSetToNormalJobReport.ftl");
                String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
                emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
                log.info("Mail sent successfully !");
            } catch (Exception e) {
                log.error("Error while sending Set Orders To Normal Job report mail !");
                e.printStackTrace();
            }
        }
    }
}
