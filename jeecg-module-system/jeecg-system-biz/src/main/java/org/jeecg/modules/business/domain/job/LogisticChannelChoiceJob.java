package org.jeecg.modules.business.domain.job;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.PlatformOrderContentMapper;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.LogisticChannelChoiceError;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * @Description: A job that fills all 'invoice_logistic_channel_name' field with 'NULL' value with a default channel
 * @Author: jeecg-boot
 * @Date:   2023-10-03
 * @Version: V1.0
 */
@Slf4j
@Component
public class LogisticChannelChoiceJob implements Job {
    @Autowired
    private CountryService countryService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ILogisticChannelChoiceService logisticChannelChoiceService;
    @Autowired
    private ILogisticChannelService logisticChannelService;
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private ISensitiveAttributeService sensitiveAttributeService;
    @Autowired
    private  IShopService shopService;
    @Autowired
    Environment env;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    private static final Integer DEFAULT_NUMBER_OF_DAYS = 180;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDate endDate = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
        LocalDate startDate = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).minusDays(DEFAULT_NUMBER_OF_DAYS);
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String parameter = ((String) jobDataMap.get("parameter"));
        if (parameter != null) {
            try {
                JSONObject jsonObject = new JSONObject(parameter);
                if (!jsonObject.isNull("startDate")) {
                    String startDateStr = jsonObject.getString("startDate");
                    startDate = LocalDate.parse(startDateStr);
                }
                if (!jsonObject.isNull("endDate")) {
                    String endDateStr = jsonObject.getString("endDate");
                    endDate = LocalDate.parse(endDateStr);
                }
            } catch (JSONException e) {
                log.error("Error while parsing parameter as JSON, falling back to default parameters.");
            }
        }
        if(!endDate.isAfter(startDate))
            throw new RuntimeException("EndDate must be strictly greater than StartDate !");

        // 1 : on doit collecter les commandes entres les 2 dates qui n'ont ni logistic_channel_name, ni invoice_logistic_channel_name
        // 2 : on doit ensuite déterminer quelle ligne utilisée
        // 2.1 : on doit déterminer quel est le sensitive_attribute avec la priorité la plus elevée dans la commande
        // 2.2 : on cherche ensuite la ligne par défaut pour le shop, le pays et l'attribut
        // 3 : on rempli le champs invoice_logistic_channel_name dans la commande

        // commandes sans logistic channel
        List<PlatformOrder> platformOrders = platformOrderService.fetchEmptyLogisticChannelOrders(startDate.toString(), endDate.toString());
        log.info("Filling default invoice logistic channel name between ["+startDate+" and "+endDate+"]");
        if(!platformOrders.isEmpty()) {
            log.info("Number of orders to process: "+platformOrders.size());
            List<String> orderIds = platformOrders.stream().map(PlatformOrder::getId).collect(Collectors.toList());
            Map<String, List<PlatformOrder>> orderMapByAttribute = new HashMap<>();
            List<PlatformOrder> tempPlatformOrders = new ArrayList<>(platformOrders);
            for(String orderId: orderIds) {
                String attributeId = sensitiveAttributeService.getHighestPriorityAttributeId(orderId);
                PlatformOrder orderToAdd = tempPlatformOrders.stream().filter
                                (
                                        order -> order.getId().equals(orderId))
                        .findAny()
                        .orElse(null
                        );
                if(!orderMapByAttribute.containsKey(attributeId)){
                    orderMapByAttribute.put(attributeId, new ArrayList<>());
                    orderMapByAttribute.get(attributeId).add(orderToAdd);
                }
                else {
                    orderMapByAttribute.get(attributeId).add(orderToAdd);
                }
                tempPlatformOrders.remove(orderToAdd);
            }
            System.gc();
            Map<String, Map<String, List<PlatformOrder>>> orderMapByShopAndCountry = new HashMap<>();
            for (PlatformOrder platformOrder : platformOrders) {
                if (orderMapByShopAndCountry.containsKey(platformOrder.getShopId())) {
                    if (orderMapByShopAndCountry.get(platformOrder.getShopId()).containsKey(platformOrder.getCountry())) {
                        orderMapByShopAndCountry.get(platformOrder.getShopId()).get(platformOrder.getCountry()).add(platformOrder);
                    } else {
                        orderMapByShopAndCountry.get(platformOrder.getShopId()).put(platformOrder.getCountry(), new ArrayList<>());
                        orderMapByShopAndCountry.get(platformOrder.getShopId()).get(platformOrder.getCountry()).add(platformOrder);
                    }
                } else {
                    orderMapByShopAndCountry.put(platformOrder.getShopId(), new HashMap<>());
                    orderMapByShopAndCountry.get(platformOrder.getShopId()).put(platformOrder.getCountry(), new ArrayList<>());
                    orderMapByShopAndCountry.get(platformOrder.getShopId()).get(platformOrder.getCountry()).add(platformOrder);
                }
            }
            System.gc();
            List<Shop> shops = orderMapByShopAndCountry.keySet().stream().map(shopId -> shopService.getById(shopId)).collect(Collectors.toList());
            List<String> countries = platformOrders.stream().map(PlatformOrder::getCountry).distinct().collect(Collectors.toList());

            List<Country> countryList = countryService.findIdByEnName(countries);
            Map<String, String> countryNameToIdMap = countryList.stream().collect(toMap(Country::getNameEn, Country::getId));

            Map<String, String> logisticChannelIdToNameMap = logisticChannelService.listByIdAndZhName().stream().collect(toMap(LogisticChannel::getId, LogisticChannel::getZhName));

            List<SensitiveAttribute> sensitiveAttributes = sensitiveAttributeService.listIdAndPriority();
            Map<String, Integer> attributeIdToPriorityMap = sensitiveAttributes.stream().collect(toMap(SensitiveAttribute::getId, SensitiveAttribute::getPriority));
            Map<String, Integer> sortedAttributeIdToPriorityMap = attributeIdToPriorityMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new
                    ));
            ListIterator<Map.Entry<String, Integer>> attributeMapIterator = new LinkedList(sortedAttributeIdToPriorityMap.entrySet()).listIterator();

            List<LogisticChannelChoice> logisticChannelChoiceList = logisticChannelChoiceService.fetchByShopId(platformOrders.stream().map(PlatformOrder::getShopId).collect(Collectors.toList()));

            List<PlatformOrder> ordersToUpdate = new ArrayList<>();
            List<LogisticChannelChoiceError> logisticChoiceErrorList = new ArrayList<>();
            for( Map.Entry<String, Map<String, List<PlatformOrder>>> entry: orderMapByShopAndCountry.entrySet()) {
                String shopId = entry.getKey();
                Map<String, List<PlatformOrder>> orderMapByCountry = entry.getValue();
                for(Map.Entry<String, List<PlatformOrder>> countryMapEntry: orderMapByCountry.entrySet()) {
                    String countryName = countryMapEntry.getKey();
                    List<PlatformOrder> orders = countryMapEntry.getValue();
                    for(PlatformOrder order: orders) {
                        // reset iterator
                        attributeMapIterator = new LinkedList(sortedAttributeIdToPriorityMap.entrySet()).listIterator();
                        String orderAttributeId = sensitiveAttributeService.getHighestPriorityAttributeId(order.getId());
                        Integer orderAttributePriority = sortedAttributeIdToPriorityMap.get(orderAttributeId);
                        if(orderMapByAttribute.get(orderAttributeId) == null || orderAttributeId == null) {
                            continue;
                        }
                        List<LogisticChannelChoice> choices = logisticChannelChoiceList.stream().filter(
                                c -> c.getShopId().equals(shopId) && c.getCountryId().equals(countryNameToIdMap.get(countryName)) && c.getSensitiveAttributeId().equals(orderAttributeId))
                                .collect(Collectors.toList());
                        if(choices.isEmpty()) {
                            while(attributeMapIterator.hasNext()) {
                                if(attributeMapIterator.next().getKey().equals(orderAttributeId)) {
                                    break;
                                }
                            }
                            // on cherche la ligne avec une priorité plus élevée
                            if(attributeMapIterator.hasNext()) {
                                LogisticChannelChoice choice = getHigherLogisticChannelChoice(shopId, countryName, orderAttributePriority, logisticChannelChoiceList, attributeMapIterator, countryNameToIdMap);
                                if(choice != null){
                                    PlatformOrder orderToAdd = new PlatformOrder();
                                    orderToAdd.setId(order.getId());
                                    orderToAdd.setInvoiceLogisticChannelName(logisticChannelIdToNameMap.get(choice.getLogisticChannelId()));
                                    ordersToUpdate.add(orderToAdd);
                                    log.info("La ligne " + choice.getLogisticChannelId() + " a été attribué à commande : " + order.getId());
                                    break;
                                }
                                //reset search to prepare for lower priority search
                                attributeMapIterator = new LinkedList(sortedAttributeIdToPriorityMap.entrySet()).listIterator();
                                while(attributeMapIterator.hasNext()) {
                                    if(attributeMapIterator.next().getKey().equals(orderAttributeId)) {
                                        break;
                                    }
                                }
                            }
                            // on se rabat sur une ligne avec une priorité plus faible
                            if(attributeMapIterator.hasPrevious()){
                                LogisticChannelChoice choice = getLowerLogisticChannelChoice(shopId, countryName, orderAttributePriority, logisticChannelChoiceList, attributeMapIterator, countryNameToIdMap);
                                if(choice != null){
                                    PlatformOrder orderToAdd = new PlatformOrder();
                                    orderToAdd.setId(order.getId());
                                    orderToAdd.setInvoiceLogisticChannelName(logisticChannelIdToNameMap.get(choice.getLogisticChannelId()));
                                    log.info("La ligne " + choice.getLogisticChannelId() + " a été attribué à commande : " + order.getId());
                                    ordersToUpdate.add(orderToAdd);
                                    break;
                                }
                            }
                            log.info("No logistic channel choice found for shop : " + shopId + ", country : " + countryName + ", attribute : " + orderAttributeId);
                            List<Shop> shop = shops.stream().filter(s -> (s != null && s.getId().equals(shopId))).collect(Collectors.toList());
                            logisticChoiceErrorList.add(new LogisticChannelChoiceError(
                                    shop.isEmpty() ? shopId : shop.get(0).getErpCode(),
                                    order.getPlatformOrderId(),
                                    countryName,
                                    sensitiveAttributes.stream().filter(attribute -> attribute.getId().equals(orderAttributeId)).collect(Collectors.toList()).get(0).getZhName()
                                    ));
                        }
                        else {
                            PlatformOrder orderToAdd = new PlatformOrder();
                            orderToAdd.setId(order.getId());
                            orderToAdd.setInvoiceLogisticChannelName(logisticChannelIdToNameMap.get(choices.get(0).getLogisticChannelId()));
                            ordersToUpdate.add(orderToAdd);
                            log.info("La ligne " + choices.get(0).getLogisticChannelId() + " a été attribué à commande : " + order.getId());
                            continue;
                        }
                    } // end for orders
                } // end for in orderMapByCountry
            } // end for orderMapByShopAndCountry
            log.info("Orders to Update => ");
            for(PlatformOrder order : ordersToUpdate) {
                log.info(order.getId());
            }
            platformOrderService.updateBatchById(ordersToUpdate);
            if(!logisticChoiceErrorList.isEmpty()) {
                String subject = "Invoice logistic channel choice error";
                String destEmail = env.getProperty("spring.mail.username");
                Properties prop = emailService.getMailSender();
                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("errors", logisticChoiceErrorList);

                Session session = Session.getInstance(prop, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
                    }
                });
                try {
                    freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
                    Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                            .getTemplate("logisticChannelChoiceError.ftl");
                    String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
                    emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
                    log.info("Mail sent successfully");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private LogisticChannelChoice getHigherLogisticChannelChoice(String shopId, String countryName, Integer priority,
                                                           List<LogisticChannelChoice> logisticChannelChoiceList, ListIterator<Map.Entry<String, Integer>> attributeMapIterator,
                                                           Map<String, String> countryNameToIdMap) throws JobExecutionException {
        log.info("On se rabat sur une priorité plus élevée");
        Map.Entry<String, Integer> nextEntry = attributeMapIterator.next();
        List<LogisticChannelChoice> logisticChannelChoices;
        LogisticChannelChoice logisticChannelChoice;
        if(nextEntry.getValue() > priority) {
            priority = nextEntry.getValue();
            String attributeId = nextEntry.getKey();
            logisticChannelChoices = logisticChannelChoiceList.stream().filter(choice -> choice.getShopId().equals(shopId) && choice.getCountryId().equals(countryNameToIdMap.get(countryName)) && choice.getSensitiveAttributeId().equals(attributeId)).collect(Collectors.toList());
            logisticChannelChoice = logisticChannelChoices.isEmpty() ? null : logisticChannelChoices.get(0);
            while(logisticChannelChoice == null && attributeMapIterator.hasNext()) {
                logisticChannelChoice = getHigherLogisticChannelChoice(shopId, countryName, priority, logisticChannelChoiceList, attributeMapIterator, countryNameToIdMap);
            }
        }
        else {
            throw new JobExecutionException("Sort error");
        }
        return logisticChannelChoice;
    }
    private LogisticChannelChoice getLowerLogisticChannelChoice(String shopId, String countryName, Integer priority,
                                                           List<LogisticChannelChoice> logisticChannelChoiceList, ListIterator<Map.Entry<String, Integer>> attributeMapIterator,
                                                           Map<String, String> countryNameToIdMap) throws JobExecutionException {
        log.info("On se rabat sur une priorité plus faible");
        Map.Entry<String, Integer> previousEntry = attributeMapIterator.previous();
        List<LogisticChannelChoice> logisticChannelChoices;
        LogisticChannelChoice logisticChannelChoice;
        if(previousEntry.getValue() <= priority) {
            priority = previousEntry.getValue();
            String attributeId = previousEntry.getKey();
            logisticChannelChoices = logisticChannelChoiceList.stream().filter(choice -> choice.getShopId().equals(shopId) && choice.getCountryId().equals(countryNameToIdMap.get(countryName)) && choice.getSensitiveAttributeId().equals(attributeId)).collect(Collectors.toList());
            logisticChannelChoice = logisticChannelChoices.isEmpty() ? null : logisticChannelChoices.get(0);
            while(logisticChannelChoice == null && attributeMapIterator.hasPrevious()) {
                logisticChannelChoice = getLowerLogisticChannelChoice(shopId, countryName, priority, logisticChannelChoiceList, attributeMapIterator, countryNameToIdMap);
            }
        }
        else {
            throw new JobExecutionException("Sort error");
        }
        return logisticChannelChoice;
    }
}
