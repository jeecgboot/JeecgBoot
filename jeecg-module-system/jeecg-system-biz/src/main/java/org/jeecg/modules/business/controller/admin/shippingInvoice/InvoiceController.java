package org.jeecg.modules.business.controller.admin.shippingInvoice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.TemplateMessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.domain.api.mabang.getorderlist.OrderStatus;
import org.jeecg.modules.business.domain.purchase.invoice.PurchaseInvoiceEntry;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.ExchangeRatesMapper;
import org.jeecg.modules.business.mapper.PlatformOrderContentMapper;
import org.jeecg.modules.business.mapper.PlatformOrderMapper;
import org.jeecg.modules.business.mapper.PurchaseOrderContentMapper;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.*;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.jeecg.common.util.SqlInjectionUtil.specialFilterContentForDictSql;
import static org.jeecg.modules.business.entity.Invoice.InvoiceType.*;
import static org.jeecg.modules.business.entity.Task.TaskCode.SI_G;
import static org.jeecg.modules.business.entity.TaskHistory.TaskStatus.*;
import static org.jeecg.modules.business.vo.PlatformOrderFront.invoiceStatus.*;

/**
 * Controller for request related to shipping invoice
 */
@Api(tags = "shippingInvoice")
@RestController
@RequestMapping("/shippingInvoice")
@Slf4j
public class InvoiceController {
    @Autowired
    private IBalanceService balanceService;
    @Autowired
    private IClientCategoryService clientCategoryService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private ExchangeRatesMapper exchangeRatesMapper;
    @Autowired
    private IShopService shopService;
    @Autowired
    private PlatformOrderShippingInvoiceService shippingInvoiceService;
    @Autowired
    private PlatformOrderMapper platformOrderMapper;
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    private PlatformOrderContentMapper platformOrderContentMap;
    @Autowired
    private ISkuWeightService skuWeightService;
    @Autowired
    private IPurchaseOrderService purchaseOrderService;
    @Autowired
    private PurchaseOrderContentMapper purchaseOrderContentMapper;
    @Autowired
    private IShippingInvoiceService iShippingInvoiceService;
    @Autowired
    private ISavRefundService iSavRefundService;
    @Autowired
    private ISavRefundWithDetailService savRefundWithDetailService;
    @Autowired
    private ISkuService skuService;
    @Autowired
    private IExchangeRatesService iExchangeRatesService;
    @Autowired
    private IQuartzJobService quartzJobService;
    @Autowired
    private ITaskHistoryService taskHistoryService;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ISysBaseAPI ISysBaseApi;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IUserClientService userClientService;
    @Autowired
    Environment env;

    @Value("${jeecg.path.shippingInvoiceDir}")
    private String INVOICE_DIR;

    @Value("${jeecg.path.shippingInvoiceDetailDir}")
    private String INVOICE_DETAIL_DIR;
    @Value("${jeecg.path.shippingInvoicePdfDir}")
    private String INVOICE_PDF_DIR;

    private final String SECTION_START = "<section><ul>";
    private final String SECTION_END = "</ul></section>";

    @GetMapping(value = "/shopsByClient")
    public Result<List<Shop>> getShopsByClient(@RequestParam("clientID") String clientID) {
        log.info("Request for shop by client {}", clientID);
        return Result.OK(shopService.listByClient(clientID));
    }

    /**
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/checkSkuPrices")
    public Result<?> checkSkuPrices(@RequestBody ShippingInvoiceOrderParam param) {
        List<PlatformOrderContent> orderContents = platformOrderContentMap.fetchOrderContent(param.orderIds());
        Set<String> skuIds = orderContents.stream().map(PlatformOrderContent::getSkuId).collect(Collectors.toSet());
        List<String> skusWithoutPrice = platformOrderContentMap.searchSkuDetail(new ArrayList<>(skuIds))
                .stream()
                .filter(skuDetail -> skuDetail.getPrice().getPrice() == null && skuDetail.getPrice().getPriceRmb() == null)
                .map(SkuDetail::getErpCode)
                .collect(Collectors.toList());
        if (skusWithoutPrice.isEmpty()) {
            return Result.OK();
        }
        return Result.error("Couldn't find prices for following SKUs : " + skusWithoutPrice);
    }
    @GetMapping(value = "/orders")
    public Result<?> getOrdersByClientAndShops(PlatformOrder platformOrder,
                                               @RequestParam("clientId") String clientId,
                                               @RequestParam(name = "shopIds[]", required = false) List<String> shopIDs,
                                               @RequestParam(name = "start", required = false) String start,
                                               @RequestParam(name = "end", required = false) String end,
                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                               @RequestParam(name = "type") String type,
                                               @RequestParam(name = "warehouses[]") List<String> warehouses,
                                               HttpServletRequest req) {
        String warehouseString = String.join(",", warehouses);
        QueryWrapper<PlatformOrder> queryWrapper = QueryGenerator.initQueryWrapper(platformOrder, req.getParameterMap());
        LambdaQueryWrapper<PlatformOrder> lambdaQueryWrapper = queryWrapper.lambda();
        switch (type) {
            case "shipping":
                lambdaQueryWrapper.in(PlatformOrder::getErpStatus, OrderStatus.Shipped.getCode());
                break;
            case "pre-shipping":
                lambdaQueryWrapper.in(PlatformOrder::getErpStatus, Arrays.asList(OrderStatus.Pending.getCode(), OrderStatus.Preparing.getCode()));
                break;
            case "all":
                lambdaQueryWrapper.in(PlatformOrder::getErpStatus, Arrays.asList(OrderStatus.Pending.getCode(), OrderStatus.Preparing.getCode(), OrderStatus.Shipped.getCode()));
                break;
            default:
                return Result.error("Error 404 : page not found.");
        }
        lambdaQueryWrapper.isNull(PlatformOrder::getShippingInvoiceNumber);
        Page<PlatformOrder> page = new Page<>(pageNo, pageSize);
        IPage<PlatformOrder> pageList;
        log.info("Request for " + type + " orders from client : " + clientId);
        if (shopIDs == null || shopIDs.isEmpty()) { // obsolete, used in old pages only
            lambdaQueryWrapper.inSql(PlatformOrder::getId, "SELECT po.id FROM platform_order po\n" +
                    " JOIN shop s ON po.shop_id = s.id\n" +
                    " JOIN client c ON s.owner_id = c.id WHERE c.id = '" + clientId + "'");
            pageList = platformOrderMapper.selectPage(page, lambdaQueryWrapper);
        } else {
            log.info("Specified shop IDs : " + shopIDs);
            lambdaQueryWrapper.in(PlatformOrder::getShopId, shopIDs);
            if(start != null || end != null){
                log.info("Specified period between " + start + " and " + end);
                if (type.equals("shipping"))
                    lambdaQueryWrapper.inSql(PlatformOrder::getId, "SELECT po.id FROM platform_order po\n" +
                            "LEFT JOIN logistic_channel lc ON po.logistic_channel_name = lc.zh_name\n" +
                            "WHERE po.shipping_time between '" + start + "' AND '" + end + "'\n" +
                            "AND (lc.warehouse_in_china IN (" + warehouseString + ") OR po.logistic_channel_name = '' OR po.logistic_channel_name IS NULL)");
                else
                    lambdaQueryWrapper.inSql(PlatformOrder::getId, "SELECT po.id FROM platform_order po\n" +
                            "LEFT JOIN logistic_channel lc ON po.logistic_channel_name = lc.zh_name\n" +
                            "WHERE po.order_time between '" + start + "' AND '" + end + "'\n" +
                            "AND (lc.warehouse_in_china IN (" + warehouseString + ") OR po.logistic_channel_name = '' OR po.logistic_channel_name IS NULL)");
            }
            else {// obsolete
                lambdaQueryWrapper.inSql(PlatformOrder::getId, "SELECT po.id FROM platform_order po\n" +
                        "JOIN logistic_channel lc ON po.logistic_channel_name = lc.zh_name\n" +
                        "WHERE (lc.warehouse_in_china IN (" + warehouseString + ") OR po.logistic_channel_name = '' OR po.logistic_channel_name IS NULL)");
            }
            pageList = platformOrderMapper.selectPage(page, lambdaQueryWrapper);
            return Result.OK(pageList);
        }
        if (pageList.getSize() > 0) {
            return Result.OK(pageList);
        }
        return Result.error("No orders for selected client/shops");
    }
    @PostMapping(value = "/period")
    public Result<?> getValidPeriod(@RequestBody List<String> shopIDs) {
        log.info("Request for valid period for shops: {}", shopIDs.toString());
        Period period = shippingInvoiceService.getValidPeriod(shopIDs);
        if (period.isValid())
            return Result.OK(period);
        else return Result.error("No package in the selected period");
    }
    /**
     * Make shipping invoice for shops between 2 dates and orders with specified status.
     *
     * @param param  ClientID, shopIDs[], startDate, endDate, erpStatuses[], warehouses[]
     * @return Result of the generation, in case of error, message will be contained,
     * in case of success, data will contain filename.
     */
    @PostMapping(value = "/make")
    @Transactional
    public Result<?> makeInvoice(@RequestBody ShippingInvoiceParam param) {
        try {
            InvoiceMetaData metaData = shippingInvoiceService.makeInvoice(param);
            balanceService.updateBalance(param.clientID(), metaData.getInvoiceCode(), "shipping");
            return Result.OK(metaData);
        } catch (UserException e) {
            return Result.error(e.getMessage());
        } catch (IOException | ParseException e) {
            log.error(e.getMessage());
            return Result.error("Sorry, server error, please try later");
        }
    }
    /**
     * Make complete invoice (Purchase + shipping) for shops between 2 dates and orders with specified status.
     * @param param ClientID, shopIDs[], startDate, endDate, erpStatuses[], warehouses[]
     * @return
     */
    @Transactional
    @PostMapping(value = "/makeComplete")
    public Result<?> makeCompleteShippingInvoice(@RequestBody ShippingInvoiceParam param) {
        try {
            String method = param.getErpStatuses().toString().equals("[3]") ? "post" : param.getErpStatuses().toString().equals("[1, 2]") ? "pre-shipping" : "all";
            InvoiceMetaData metaData = shippingInvoiceService.makeCompleteInvoicePostShipping(param, method);
            balanceService.updateBalance(param.clientID(), metaData.getInvoiceCode(), "complete");
            return Result.OK(metaData);
        } catch (UserException e) {
            return Result.error(e.getMessage());
        } catch (IOException | ParseException e) {
            log.error(e.getMessage());
            return Result.error("Sorry, server error, please try later");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Make invoice for specified orders and type
     *
     * @param param Parameters for creating an invoice
     * @return Result of the generation, in case of error, message will be contained,
     * in case of success, data will contain filename.
     */
    @Transactional
    @PostMapping(value = "/makeManualInvoice")
    public Result<?> makeManualShippingInvoice(@RequestBody ShippingInvoiceOrderParam param) {
        try {
            InvoiceMetaData metaData = shippingInvoiceService.makeInvoice(param);
            String clientCategory = clientCategoryService.getClientCategoryByClientId(param.clientID());
            if(clientCategory.equals(ClientCategory.CategoryName.CONFIRMED.getName()) || clientCategory.equals(ClientCategory.CategoryName.VIP.getName())) {
                balanceService.updateBalance(param.clientID(), metaData.getInvoiceCode(), "shipping");
            }
            if(clientCategory.equals(ClientCategory.CategoryName.SELF_SERVICE.getName())) {
                String subject = "Self-service shipping invoice";
                String destEmail = env.getProperty("spring.mail.username");
                Properties prop = emailService.getMailSender();
                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("invoiceType", "shipping invoice");
                templateModel.put("invoiceEntity", clientService.getById(param.clientID()).getInternalCode());
                templateModel.put("invoiceNumber", metaData.getInvoiceCode());

                Session session = Session.getInstance(prop, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
                    }
                });
                freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
                Template template = freemarkerConfigurer.getConfiguration().getTemplate("admin/invoiceNotification.ftl");
                String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
                emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
                log.info("Mail sent successfully");
            }
            return Result.OK(metaData);
        } catch (UserException e) {
            return Result.error(e.getMessage());
        } catch (IOException | ParseException e) {
            log.error(e.getMessage());
            return Result.error("Sorry, server error, please try later");
        } catch (TemplateException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Make purchase invoice for specified orders and type
     * used by self-service clients
     * creates a purchase order in Mabang
     * @param param Parameters for creating an invoice
     * @return Result of the generation, in case of error, message will be contained,
     * in case of success, data will contain filename.
     */
    @Transactional
    @PostMapping(value = "/makeManualPurchaseInvoice")
    public Result<?> makeManualPurchaseInvoice(@RequestBody ShippingInvoiceOrderParam param) {
        InvoiceMetaData metaData;
        try {
            List<SkuQuantity> skuQuantities = skuService.getSkuQuantitiesFromOrderIds(param.orderIds());
            String purchaseId = purchaseOrderService.addPurchase(skuQuantities ,param.orderIds());
            metaData = purchaseOrderService.makeInvoice(purchaseId);
            platformOrderService.updatePurchaseInvoiceNumber(param.orderIds(), metaData.getInvoiceCode());

            String clientCategory = clientCategoryService.getClientCategoryByClientId(param.clientID());
//            if(clientCategory.equals(ClientCategory.CategoryName.CONFIRMED.getName()) || clientCategory.equals(ClientCategory.CategoryName.VIP.getName())) {
//                balanceService.updateBalance(param.clientID(), metaData.getCode(), "purchase");
//            }
            if(clientCategory.equals(ClientCategory.CategoryName.SELF_SERVICE.getName())) {
                String subject = "Self-service purchase invoice";
                String destEmail = env.getProperty("spring.mail.username");
                Properties prop = emailService.getMailSender();
                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("invoiceType", "purchase invoice");
                templateModel.put("invoiceEntity", clientService.getById(param.clientID()).getInternalCode());
                templateModel.put("invoiceNumber", metaData.getInvoiceCode());

                Session session = Session.getInstance(prop, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
                    }
                });
                freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
                Template template = freemarkerConfigurer.getConfiguration().getTemplate("admin/invoiceNotification.ftl");
                String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
                emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
                log.info("Mail sent successfully");
            }
            return Result.OK(metaData);
        } catch (UserException e) {
            return Result.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
            return Result.error("Sorry, server error, please try later");
        } catch (URISyntaxException | MessagingException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Make complete shipping invoice (Purchase + shipping) for specified orders and statuses
     *
     * @param param Parameters for creating a pre-shipping invoice
     * @return Result of the generation, in case of error, message will be contained,
     * in case of success, data will contain filename.
     */
    @Transactional
    @PostMapping(value = "/makeManualComplete")
    public Result<?> makeManualCompleteInvoice(@RequestBody ShippingInvoiceOrderParam param) {
        try {
            InvoiceMetaData metaData = shippingInvoiceService.makeCompleteInvoice(param);
            String clientCategory = clientCategoryService.getClientCategoryByClientId(param.clientID());
            if(clientCategory.equals(ClientCategory.CategoryName.CONFIRMED.getName()) || clientCategory.equals(ClientCategory.CategoryName.VIP.getName())) {
                balanceService.updateBalance(param.clientID(), metaData.getInvoiceCode(), "complete");
            }
            if(clientCategory.equals(ClientCategory.CategoryName.SELF_SERVICE.getName())) {
                String subject = "Self-service complete invoice";
                String destEmail = env.getProperty("spring.mail.username");
                Properties prop = emailService.getMailSender();
                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("invoiceType", "complete invoice");
                templateModel.put("invoiceEntity", clientService.getById(param.clientID()).getInternalCode());
                templateModel.put("invoiceNumber", metaData.getInvoiceCode());

                Session session = Session.getInstance(prop, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
                    }
                });
                freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
                Template template = freemarkerConfigurer.getConfiguration().getTemplate("admin/invoiceNotification.ftl");
                String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
                emailService.sendSimpleMessage(destEmail, subject, htmlBody, session);
                log.info("Mail sent successfully");
            }
            return Result.OK(metaData);
        } catch (UserException e) {
            return Result.error(e.getMessage());
        } catch (IOException | ParseException e) {
            log.error(e.getMessage());
            return Result.error("Sorry, server error, please try later");
        } catch (MessagingException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/makeManualSkuPurchaseInvoice")
    public Result<?> createOrder(@RequestBody Map<String, Integer> payload) {
        InvoiceMetaData metaData;
        List<SkuQuantity> skuQuantities = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : payload.entrySet()) {
            String skuId = skuService.getIdFromErpCode(entry.getKey());
            skuQuantities.add(new SkuQuantity(skuId, entry.getKey(), entry.getValue()));
        }
        try {
            String purchaseId = purchaseOrderService.addPurchase(skuQuantities);
            PurchaseOrder purchaseOrder = purchaseOrderService.getById(purchaseId);
            String clientCategory = clientCategoryService.getClientCategoryByClientId(purchaseOrder.getClientId());
            if(clientCategory.equals(ClientCategory.CategoryName.CONFIRMED.getName()) || clientCategory.equals(ClientCategory.CategoryName.VIP.getName())) {
                balanceService.updateBalance(purchaseOrder.getClientId(), purchaseOrder.getInvoiceNumber(), "purchase");
            }
            metaData = purchaseOrderService.makeInvoice(purchaseId);
            return Result.OK(metaData);
        } catch (UserException e) {
            return Result.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
            return Result.error("Sorry, server error, please try later");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping(value = "/preShipping/orderTime")
    public Result<?> getValidOrderTimePeriod(@RequestParam("shopIds[]") List<String> shopIDs, @RequestParam("erpStatuses[]") List<Integer> erpStatuses) {
        log.info("Request for valid order time period for shops: {} and erpStatuses : {}", shopIDs.toString(), erpStatuses.toString());
        Period period = shippingInvoiceService.getValidOrderTimePeriod(shopIDs, erpStatuses);
        if (period.isValid()) {
            return Result.OK(period);
        }
        return Result.error("No package in the selected period");
    }

    /**
     *  Checks if all sku of order from shops between 2 dates are available
     * @param platformOrder
     * @param param clientID, shopIDs, start (date), end (date)
     * @param req
     * @return sku prices available code : 200 = success
     */
    @PostMapping(value = "/preShipping/ordersBetweenOrderDates")
    public Result<?> getOrdersBetweenOrderDates(PlatformOrder platformOrder, @RequestBody ShippingInvoiceParam param, HttpServletRequest req) {
        QueryWrapper<PlatformOrder> queryWrapper = QueryGenerator.initQueryWrapper(platformOrder, req.getParameterMap());
        LambdaQueryWrapper<PlatformOrder> lambdaQueryWrapper = queryWrapper.lambda();
        log.info("Requesting orders for : {}", param.toString());
        if (param.shopIDs() == null || param.shopIDs().isEmpty()) {
            return Result.error("Missing shop IDs");
        }
        log.info("Specified shop IDs : {}", param.shopIDs());
        lambdaQueryWrapper.in(PlatformOrder::getShopId, param.shopIDs());
        lambdaQueryWrapper.isNull(PlatformOrder::getShippingInvoiceNumber);
        if(param.getErpStatuses() != null) {
            log.info("Specified erpStatuses : {}", param.getErpStatuses());
            lambdaQueryWrapper.in(PlatformOrder::getErpStatus, param.getErpStatuses());
        }
        lambdaQueryWrapper.inSql(PlatformOrder::getId, "SELECT id FROM platform_order po WHERE po.order_time between '" + param.getStart() + "' AND '" + param.getEnd() + "'" );
        // on récupère les résultats de la requete
        List<PlatformOrder> orderID = platformOrderMapper.selectList(lambdaQueryWrapper);
        // on récupère seulement les ID des commandes
        List<String> orderIds = orderID.stream().map(PlatformOrder::getId).collect(Collectors.toList());
        ShippingInvoiceOrderParam args = new ShippingInvoiceOrderParam(param.clientID(), orderIds, "pre-shipping");
        // on check s'il y a des SKU sans prix
        return checkSkuPrices(args);
    }

    @GetMapping(value = "/preShipping/orderByShops")
    public Result<?> getOrdersByShops(@RequestParam("shopIds") String shopIds) {
        log.info("User : {} is requesting uninvoiced orders for shops : [{}]",
                ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername(),
                shopIds);
        List<String> shopIdList = Arrays.asList(shopIds.split(","));
        List<PlatformOrder> orders = platformOrderService.findUninvoicedShippingOrdersByShopForClient(shopIdList, Collections.singletonList(1));

        IPage<PlatformOrder> page = new Page<>();
        page.setRecords(orders);
        page.setTotal(orders.size());
        return Result.OK(page);
    }

    /**
     * Get uninvoiced orders and specify if shipping and purchase invoicing is available
     * @param shopIds shop ids
     * @return A triplet of order, shipping invoice availability and purchase invoice availability
     */
    @GetMapping(value = "/preShipping/ordersStatusByShops")
    public Result<?> getOrdersStatusByShops(@RequestParam(name = "shopIds") String shopIds,
                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                            @RequestParam(name = "pageSize", defaultValue = "50") Integer pageSize,
                                            @RequestParam(name = "column", defaultValue = "order_time") String column,
                                            @RequestParam(name = "order", defaultValue = "ASC") String order) {
        log.info("User : {} is requesting uninvoiced orders for shops : [{}]",
                ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername(),
                shopIds);
        String parsedColumn = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, column.replace("_dictText",""));
        String parsedOrder = order.toUpperCase();
        if(!parsedOrder.equals("ASC") && !parsedOrder.equals("DESC")) {
            return Result.error("Error 400 Bad Request");
        }
        try {
            specialFilterContentForDictSql(parsedColumn);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            return Result.error("Error 400 : Bad Request");
        }
        // checking shipping data availability
        List<String> shopIdList = Arrays.asList(shopIds.split(","));
        // fetch order that can be invoiced either by shipping or purchase or both
        List<PlatformOrder> allOrders = platformOrderService.findUninvoicedOrdersByShopForClient(shopIdList, Collections.singletonList(1), parsedColumn, parsedOrder, 1, -1);
        int total = allOrders.size();
        List<PlatformOrder> orders = platformOrderService.findUninvoicedOrdersByShopForClient(shopIdList, Collections.singletonList(1), parsedColumn, parsedOrder, pageNo, pageSize);
//        LinkedList<PlatformOrder> sortedOrders = orders.stream().sorted(Comparator.comparing(PlatformOrder::getOrderTime)).collect(Collectors.toCollection(LinkedList::new));
        if(orders.isEmpty())
            return Result.OK("No order to invoice.");
        Map<String, String> shops = shopService.listByIds(shopIdList).stream().collect(Collectors.toMap(Shop::getId, Shop::getName));
        List<String> orderIds = orders.stream().map(PlatformOrder::getId).collect(Collectors.toList());
        Map<PlatformOrder, List<PlatformOrderContent>> orderContentMap = platformOrderService.fetchOrderData(orderIds);
        // some orders may not have content, so we have to re-add them
        if(orderIds.size() != orderContentMap.size()) {
            List<String> orderIdsInMap = orderContentMap.keySet().stream().map(PlatformOrder::getId).collect(Collectors.toList());
            List<String> orderIdsWithoutContent = orderIds.stream().filter(id -> !orderIdsInMap.contains(id)).collect(Collectors.toList());
            List<PlatformOrder> ordersWithoutContent = platformOrderService.listByIds(orderIdsWithoutContent);
            Map<PlatformOrder, List<PlatformOrderContent>> orderContentMapWithoutContent = new HashMap<>();
            for(PlatformOrder po : ordersWithoutContent) {
                orderContentMapWithoutContent.put(po, new ArrayList<>());
            }
            orderContentMap.putAll(orderContentMapWithoutContent);
        }

        Map<String, String> errorMapToOrderId = new HashMap<>();
        List<PlatformOrderFront> orderFronts = new ArrayList<>();

        for(Map.Entry<PlatformOrder, List<PlatformOrderContent>> entry : orderContentMap.entrySet()) {
            PlatformOrderFront orderFront = new PlatformOrderFront();
            BeanUtils.copyProperties(entry.getKey(), orderFront);

            //rename shop id by shop name to prevent it to leak in front
            orderFront.setShopId(shops.get(orderFront.getShopId()));
            // set default value of shipping and purchase availability
            orderFront.setShippingAvailable(Available.code);
            orderFront.setPurchaseAvailable(Available.code);

            if(entry.getValue().isEmpty()) {
                if(!errorMapToOrderId.containsKey(entry.getKey().getPlatformOrderId()))
                    errorMapToOrderId.put(entry.getKey().getPlatformOrderId(), "Error : order has no content : " + entry.getKey().getPlatformOrderId());
                else
                    errorMapToOrderId.put(entry.getKey().getPlatformOrderId(), errorMapToOrderId.get(entry.getKey().getPlatformOrderId()) + " and has no content");
                orderFront.setShippingAvailable(Unavailable.code);
                orderFront.setPurchaseAvailable(Unavailable.code);
                orderFronts.add(orderFront);
                continue;
            }

            List<String> skuIds = entry.getValue().stream().map(PlatformOrderContent::getSkuId).distinct().collect(Collectors.toList());
            // finds the first sku that isn't in db
            List<Sku> skuIdsFound = skuService.listByIds(skuIds);
            if(skuIdsFound.size() != skuIds.size()) {
                if(!errorMapToOrderId.containsKey(entry.getKey().getPlatformOrderId()))
                    errorMapToOrderId.put(entry.getKey().getPlatformOrderId(), "Error : Missing one or more sku in db for order : " + entry.getKey().getPlatformOrderId());
                else
                    errorMapToOrderId.put(entry.getKey().getPlatformOrderId(), errorMapToOrderId.get(entry.getKey().getPlatformOrderId()) + " and missing one or more sku in db");

                orderFront.setShippingAvailable(Unavailable.code);
                orderFront.setPurchaseAvailable(Unavailable.code);
                continue;
            }

            if(entry.getKey().getShippingInvoiceNumber() == null) {
                // checks if logistic channel is missing
                if(entry.getKey().getLogisticChannelName().isEmpty() && entry.getKey().getInvoiceLogisticChannelName() == null) {
                    if(!errorMapToOrderId.containsKey(entry.getKey().getPlatformOrderId()))
                        errorMapToOrderId.put(entry.getKey().getPlatformOrderId(), "Error : Missing logistic channel for order : " + entry.getKey().getPlatformOrderId());
                    else
                        errorMapToOrderId.put(entry.getKey().getPlatformOrderId(), errorMapToOrderId.get(entry.getKey().getPlatformOrderId()) + " and missing logistic channel");
                    orderFront.setShippingAvailable(Unavailable.code);
                }
                // finds the first product with missing weight
                String missingWeightSkuId = skuWeightService.searchFirstEmptyWeightSku(skuIds);
                if(missingWeightSkuId != null) {
                    if(!errorMapToOrderId.containsKey(entry.getKey().getPlatformOrderId()))
                        errorMapToOrderId.put(entry.getKey().getPlatformOrderId(), "Error : Missing one or more weight for order : " + entry.getKey().getPlatformOrderId());
                    else
                        errorMapToOrderId.put(entry.getKey().getPlatformOrderId(), errorMapToOrderId.get(entry.getKey().getPlatformOrderId()) + " and missing weight");
                    orderFront.setShippingAvailable(Unavailable.code);
                }
            }
            if(entry.getKey().getPurchaseInvoiceNumber() == null) {
                // finds the first sku with missing price
                String missingPriceSkuId = skuService.searchFirstMissingPriceSku(skuIds);
                if(missingPriceSkuId != null) {
                    if(!errorMapToOrderId.containsKey(entry.getKey().getPlatformOrderId()))
                        errorMapToOrderId.put(entry.getKey().getPlatformOrderId(), "Error : Missing one or more sku price for order : " + entry.getKey().getPlatformOrderId());
                    else
                        errorMapToOrderId.put(entry.getKey().getPlatformOrderId(), errorMapToOrderId.get(entry.getKey().getPlatformOrderId()) + " and missing one or more sku price");
                    orderFront.setPurchaseAvailable(Unavailable.code);
                }
            }

            // set purchase order status (-1 = unavailable, 0 = available, 1 = invoiced, 2 = paid)
            if(entry.getKey().getProductAvailable() == null) {
                orderFront.setProductAvailable(PlatformOrderFront.productStatus.Unavailable.code);
                entry.getKey().setProductAvailable(PlatformOrderFront.productStatus.Unavailable.code);
            }
            if(entry.getKey().getProductAvailable().equals(PlatformOrderFront.productStatus.Unavailable.code)
                    && entry.getKey().getVirtualProductAvailable().equals(PlatformOrderFront.productStatus.Available.code)
                    && entry.getKey().getPurchaseInvoiceNumber() == null
            )
                orderFront.setProductAvailable(PlatformOrderFront.productStatus.Ordered.code);
            if(entry.getKey().getPurchaseInvoiceNumber() != null) {
                PurchaseOrder purchase =  purchaseOrderService.getPurchaseByInvoiceNumber(entry.getKey().getPurchaseInvoiceNumber());
                if(purchase.getPaidAmount().compareTo(BigDecimal.ZERO) == 0)
                    orderFront.setPurchaseAvailable(Invoiced.code);// invoiced
                else
                    orderFront.setPurchaseAvailable(Paid.code);// paid
            }
            // set shipping order status (-1 = unavailable, 0 = available, 1 = invoiced, 2 = paid)
            if(entry.getKey().getShippingInvoiceNumber() != null) {
                ShippingInvoice shippingInvoice = iShippingInvoiceService.getShippingInvoice(entry.getKey().getShippingInvoiceNumber());
                if(shippingInvoice.getPaidAmount().compareTo(BigDecimal.ZERO) == 0) {
                    orderFront.setShippingAvailable(Invoiced.code); // invoiced
                }
                else {
                    orderFront.setShippingAvailable(Paid.code); // paid
                }
            }
            orderFronts.add(orderFront);
        }

        List<String> errorMessages = new ArrayList<>(errorMapToOrderId.values());

        // sorting by order time ascending
        orderFronts = orderFronts.stream().sorted(Comparator.comparing(PlatformOrderFront::getOrderTime)).collect(Collectors.toList());
        // system notification
        String errors = SECTION_START;
        int max_entries = 100;
        int current_page = 0;
        int total_page = (int) Math.ceil((double) errorMessages.size() /max_entries);
        for(int i = 1; i <= errorMessages.size(); i++) {
            if(i%max_entries == 1) {
                errors = SECTION_START;
                current_page++;
            }
            errors = errors.concat("<li>" + i + " : " + errorMessages.get(i-1) +"</li>");
            if(i%max_entries==0 || i == errorMessages.size()) {
                errors = errors.concat(SECTION_END);
                Map<String, String> templateParam = new HashMap<>();
                templateParam.put("nb_entries", String.valueOf(errorMessages.size()));
                templateParam.put("errors", errors);
                templateParam.put("current_page", String.valueOf(current_page));
                templateParam.put("total_page", String.valueOf(total_page));
                TemplateMessageDTO message = new TemplateMessageDTO("admin", "admin", "Self Service invoicing estimation Errors", templateParam, "expenses_overview_errors");
                ISysBaseApi.sendTemplateAnnouncement(message);
            }
        }

        IPage<PlatformOrderFront> page = new Page<>();
        page.setRecords(orderFronts);
        page.setSize(pageSize);
        page.setCurrent(pageNo);
        page.setTotal(total);
        return Result.OK(page);
    }

    /**
     *  Checks if all sku of order from shops between 2 dates are available
     * @param platformOrder
     * @param param clientID, shopIDs, start (date), end (date)
     * @param req
     * @return sku prices available code : 200 = success
     */
    @PostMapping(value = "/postShipping/ordersBetweenDates")
    public Result<?> getOrdersBetweenDates(PlatformOrder platformOrder, @RequestBody ShippingInvoiceParam param, HttpServletRequest req) {
        QueryWrapper<PlatformOrder> queryWrapper = QueryGenerator.initQueryWrapper(platformOrder, req.getParameterMap());
        LambdaQueryWrapper<PlatformOrder> lambdaQueryWrapper = queryWrapper.lambda();
        log.info("Requesting orders for : {}", param.toString());
        if (param.shopIDs() == null || param.shopIDs().isEmpty()) {
            return Result.error("Missing shop IDs");
        }
        log.info("Specified shop IDs : {}", param.shopIDs());
        lambdaQueryWrapper.in(PlatformOrder::getShopId, param.shopIDs());
        lambdaQueryWrapper.isNull(PlatformOrder::getShippingInvoiceNumber);
        lambdaQueryWrapper.inSql(PlatformOrder::getId, "SELECT id FROM platform_order po WHERE po.erp_status = '3' AND po.shipping_time between '" + param.getStart() + "' AND '" + param.getEnd() + "'" );
        // on récupère les résultats de la requete
        List<PlatformOrder> orderID = platformOrderMapper.selectList(lambdaQueryWrapper);
        // on récupère seulement les ID des commandes
        List<String> orderIds = orderID.stream().map(PlatformOrder::getId).collect(Collectors.toList());
        ShippingInvoiceOrderParam args = new ShippingInvoiceOrderParam(param.clientID(), orderIds, "postShipping");
        // on check s'il y a des SKU sans prix
        return checkSkuPrices(args);
    }

    /**
     * Get binary data of the invoice file.
     *
     * @param filename name of the invoice
     * @return byte array, in case of error, an empty array will be returned.
     */
    @GetMapping(value = "/download")
    public byte[] downloadInvoice(@RequestParam("filename") String filename, @RequestParam(value = "type", required = false) String type) {
        log.info("request for downloading shipping invoice");
        String invoiceType = type == null ? "shipping" : type;
        try {
            return shippingInvoiceService.getInvoiceBinary(filename, invoiceType);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new byte[0];
        }
    }

    @GetMapping(value = "/downloadInvoiceDetail")
    public byte[] downloadInvoiceDetail(@RequestParam("invoiceNumber") String invoiceNumber, @RequestParam("invoiceEntity") String invoiceEntity, @RequestParam("internalCode") String internalCode) throws IOException {
        List<FactureDetail> factureDetails = shippingInvoiceService.getInvoiceDetail(invoiceNumber);
        List<SavRefundWithDetail> refunds = savRefundWithDetailService.getRefundsByInvoiceNumber(invoiceNumber);
        return shippingInvoiceService.exportToExcel(factureDetails, refunds, invoiceNumber, invoiceEntity, internalCode);
    }
    @GetMapping(value = "/downloadInventory")
    public byte[] downloadInventory(@RequestParam("invoiceCode") String invoiceCode, @RequestParam("internalCode") String internalCode, @RequestParam("invoiceEntity") String invoiceEntity) throws IOException {
        InvoiceMetaData metaData = new InvoiceMetaData("", invoiceCode, internalCode, invoiceEntity, "");
        List<SkuOrderPage> skuOrderPages = skuService.getInventoryByInvoiceNumber(metaData.getInvoiceCode());
        return shippingInvoiceService.exportPurchaseInventoryToExcel(skuOrderPages, metaData);
    }

    /**
     * Returns a breakdown of all invoicable shops
     *
     * @return List of Shipping fees estimation
     */
    @GetMapping(value = "/breakdown/byShop")
    public Result<?> getOrdersByClientAndShops() {
        List<String> errorMessages = new ArrayList<>();
        List<ShippingFeesEstimation> shippingFeesEstimation = shippingInvoiceService.getShippingFeesEstimation(errorMessages);
        if (shippingFeesEstimation.isEmpty()) {
            return Result.error("No data");
        }
        Map<String, String> clientIDCodeMap = new HashMap<>();
        for(ShippingFeesEstimation estimation: shippingFeesEstimation) {
            String clientId;
            if(clientIDCodeMap.containsKey(estimation.getCode())){
                clientId = clientIDCodeMap.get(estimation.getCode());
            }
            else {
                clientId = clientService.getClientIdByCode(estimation.getCode());
                clientIDCodeMap.put(estimation.getCode(), clientId);
            }
            if (estimation.getIsCompleteInvoice().equals("1")) {
                List<String> shopIds = shopService.listIdByClient(clientId);
                Period period = shippingInvoiceService.getValidPeriod(shopIds);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(period.start());
                String start = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1 < 10 ? "0" : "") + (calendar.get(Calendar.MONTH) + 1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + (calendar.get(Calendar.DAY_OF_MONTH));
                calendar.setTime(period.end());
                String end = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1 < 10 ? "0" : "") + (calendar.get(Calendar.MONTH) + 1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) + 1 < 10 ? "0" : "") + (calendar.get(Calendar.DAY_OF_MONTH) + 1);

                List<String> orderIds = shippingInvoiceService.getShippingOrderIdBetweenDate(shopIds, start, end, Arrays.asList("0", "1"));
                ShippingInvoiceOrderParam param = new ShippingInvoiceOrderParam(clientId, orderIds, "post");
                Result<?> checkSkuPrices = checkSkuPrices(param);
                estimation.setErrorMessage(checkSkuPrices.getCode() == 200 ? "" : checkSkuPrices.getMessage());
            }
            System.gc();
        }
        return Result.OK(errorMessages.toString(), shippingFeesEstimation);
    }

    /**
     * Takes a list of ShippingFeesEstimations and groups the estimations by client
     * @param estimationsByShop List of estimations
     * @return List of estimation grouped by client
     */
    @PostMapping(value = "/breakdown/byClient")
    public Result<?> getOrdersByClient(@RequestBody List<ShippingFeesEstimation> estimationsByShop) {
        Map<String, List<ShippingFeesEstimation>> estimationClientMap = new HashMap<>();
        List<ShippingFeesEstimationClient> estimationByClients = new ArrayList<>();
        estimationsByShop.forEach(estimation -> {
            if(estimationClientMap.containsKey(estimation.getCode())){
                estimationClientMap.get(estimation.getCode()).add(estimation);
            }else {
                List<ShippingFeesEstimation> l = new ArrayList<>();
                l.add(estimation);
                estimationClientMap.put(estimation.getCode(), l);
            }
        });
        for(Map.Entry<String, List<ShippingFeesEstimation>> entry : estimationClientMap.entrySet()) {
            String code = entry.getKey();
            String clientId = clientService.getClientIdByCode(code);
            List<String> shops = new ArrayList<>();
            int ordersToProcess = 0;
            int processedOrders = 0;
            BigDecimal dueForProcessedOrders = BigDecimal.ZERO;
            String isCompleteInvoice = "0";
            int hasErrors = 0;
            for(ShippingFeesEstimation estimation: entry.getValue()) {
                shops.add(estimation.getShop());
                isCompleteInvoice = estimation.getIsCompleteInvoice();
                ordersToProcess += estimation.getOrdersToProcess();
                processedOrders += estimation.getProcessedOrders();
                dueForProcessedOrders = dueForProcessedOrders.add(estimation.getDueForProcessedOrders());
                hasErrors = estimation.getErrorMessage().isEmpty() ? hasErrors : hasErrors+1;
            }
            ShippingFeesEstimationClient estimationClient = new ShippingFeesEstimationClient(clientId, code, ordersToProcess, processedOrders, dueForProcessedOrders, isCompleteInvoice, hasErrors);
            estimationByClients.add(estimationClient);
            System.gc();
        }
        return Result.ok(estimationByClients);
    }

    /**
     * Invoices all available orders with status 3 for a list of client
     * @param shippingClientIds list of clients to invoice shipping fees
     * @param completeClientIds list of clients to invoice complete fees
     * @return list of invoice infos
     */
    @Transactional
    @GetMapping(value = "/breakdown/makeInvoice")
    public Result<?> makeBreakdownInvoice(@RequestParam(value = "shipping[]", required = false) List<String> shippingClientIds,
                                          @RequestParam(value = "complete[]", required = false) List<String> completeClientIds) throws IOException {
        List<InvoiceMetaData> metaDataErrorList = new ArrayList<>();
        TaskHistory ongoingBITask = taskHistoryService.getLatestRunningTask(SI_G.name());
        if(ongoingBITask != null) {
            return Result.error("Task is already run by " + ongoingBITask.getCreateBy() + ", please retry in a moment !");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<InvoiceMetaData> invoiceList = new ArrayList<>();
        taskHistoryService.insert(new TaskHistory(sysUser.getUsername(), RUNNING.getCode(), SI_G.name()));
        TaskHistory lastRunningTask = taskHistoryService.getLatestRunningTask(SI_G.name());

        if(shippingClientIds != null) {
            log.info("Making shipping invoice for clients : {}", shippingClientIds);
            invoiceList.addAll(shippingInvoiceService.breakdownInvoiceClientByType(shippingClientIds, 0));
        }
        if(completeClientIds != null) {
            log.info("Making complete shipping invoice for clients : {}", completeClientIds);
            invoiceList.addAll(shippingInvoiceService.breakdownInvoiceClientByType(completeClientIds, 1));
        }
        if (invoiceList.isEmpty()) {
            lastRunningTask.setOngoing(SUCCESS.getCode());
            taskHistoryService.updateById(lastRunningTask);
            return Result.ok("Nothing invoiced");
        }
        List<String> filenameList = new ArrayList<>();
        log.info("Generating detail files ...0/{}", invoiceList.size());
        int cpt = 1;
        for(InvoiceMetaData metaData: invoiceList){
            if(metaData.getInvoiceCode().equals("error")) {
                metaDataErrorList.add(metaData);
            }
            else {
                filenameList.add(INVOICE_PDF_DIR + "//" + "Invoice N°" + metaData.getInvoiceCode() + " (" + metaData.getInvoiceEntity() + ").pdf");
                filenameList.add(INVOICE_DIR + "//" + metaData.getFilename());
                List<FactureDetail> factureDetails = shippingInvoiceService.getInvoiceDetail(metaData.getInvoiceCode());
                List<SavRefundWithDetail> refunds = savRefundWithDetailService.getRefundsByInvoiceNumber(metaData.getInvoiceCode());
                shippingInvoiceService.exportToExcel(factureDetails, refunds, metaData.getInvoiceCode(), metaData.getInvoiceEntity(), metaData.getInternalCode());
                filenameList.add(INVOICE_DETAIL_DIR + "//" + metaData.getInternalCode() + "_(" + metaData.getInvoiceEntity() + ")_" + metaData.getInvoiceCode() + "_Détail_calcul_de_facture.xlsx");
            }
            log.info("Generating detail files ...{}/{}", cpt++, invoiceList.size());
        }
        String zipFilename = shippingInvoiceService.zipInvoices(filenameList);
        String subject = "[" + LocalDate.now() + "] Invoices generated from Breakdown Page";
        String destEmail = sysUser.getEmail() == null ? env.getProperty("spring.mail.username") : sysUser.getEmail();
        Properties prop = emailService.getMailSender();
        Map <String, Object> templateModel = new HashMap<>();
        templateModel.put("errors", metaDataErrorList);

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
            }
        });
        try {
            freemarkerConfigurer = emailService.freemarkerClassLoaderConfig();
            Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                    .getTemplate("breakdownInvoiceMail.ftl");
            String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, templateModel);
            emailService.sendMessageWithAttachment(destEmail, subject, htmlBody, zipFilename,session);
            log.info("Mail sent successfully");

            lastRunningTask.setOngoing(SUCCESS.getCode());
            taskHistoryService.updateById(lastRunningTask);
            return Result.OK("component.email.emailSent");
        }
        catch(Exception e) {
            e.printStackTrace();
            lastRunningTask.setOngoing(CANCELLED.getCode());
            taskHistoryService.updateById(lastRunningTask);
            return Result.error("An error occurred while trying to send an email.");
        }
    }

    /**
     * Get an estimate of shipping fees for selected orders
     * @param param Parameters for creating a pre-shipping invoice
     * @return One ShippingFeesEstimation
     */
    @PostMapping(value = "/estimate")
    public Result<?> getShippingFeesEstimateByOrders(@RequestBody ShippingInvoiceOrderParam param) {
        List<String> errorMessages = new ArrayList<>();
        List<ShippingFeesEstimation> shippingFeesEstimations = shippingInvoiceService.getShippingFeesEstimation(param.clientID(),
                param.orderIds(), errorMessages);
        if (!errorMessages.isEmpty()){
            return Result.error(errorMessages.toString());
        } else {
            return Result.OK(errorMessages.toString(), shippingFeesEstimations);
        }
    }

    /**
     * Get an estimate of shipping fees and purchase fees for selected orders
     * @param param Parameters for creating a pre-shipping invoice
     * @return One ShippingFeesEstimation
     */
    @PostMapping(value = "/completeFeesEstimation")
    public Result<?> getCompleteFeesEstimation(@RequestBody ShippingInvoiceOrderParam param) {
        boolean isEmployee = securityService.checkIsEmployee();
        String currency = clientService.getById(param.clientID()).getCurrency();
        List<PlatformOrder> orders = platformOrderMapper.fetchByIds(param.orderIds());
        Map<String, List<PlatformOrder>> ordersMapByShop = orders.stream().collect(Collectors.groupingBy(PlatformOrder::getShopId));
        Map<String, Estimation> estimationsByShop = new HashMap<>();
        for(Map.Entry<String, List<PlatformOrder>> entry : ordersMapByShop.entrySet()) {
            String shopId = entry.getKey();
            String shop;
            if(!isEmployee)
                shop = shopService.getNameById(shopId);
            else
                shop = shopService.getCodeById(shopId);
            List<String> orderIds = entry.getValue().stream().map(PlatformOrder::getId).collect(Collectors.toList());
            ShippingInvoiceOrderParam finalParam = new ShippingInvoiceOrderParam(param.clientID(), orderIds, param.getType());
            List<String> errorMessages = new ArrayList<>();
            List<ShippingFeesEstimation> shippingFeesEstimations = shippingInvoiceService.getShippingFeesEstimation(finalParam.clientID(),
                    finalParam.orderIds(), errorMessages);
            if(shippingFeesEstimations.isEmpty())
                return Result.OK("No estimation found.");
            String internalCode = shippingFeesEstimations.get(0).getCode();

            // purchase estimation
            // only calculate purchase estimation if products are not available and purchaseInvoiceNumber is null, else it's already been paid
            List<String> orderIdsWithProductUnavailable = entry.getValue().stream()
                    .filter(
                        order -> order.getProductAvailable().equals("0")
                                && order.getPurchaseInvoiceNumber() == null
                                && order.getVirtualProductAvailable().equals("0"))
                    .map(PlatformOrder::getId).collect(Collectors.toList());

            BigDecimal shippingFeesEstimation = BigDecimal.ZERO;
            BigDecimal purchaseEstimation = BigDecimal.ZERO;
            int ordersToProccess = 0;
            int processedOrders = 0;
            boolean isCompleteInvoiceReady = true;

            for(ShippingFeesEstimation estimation: shippingFeesEstimations) {
                shippingFeesEstimation = shippingFeesEstimation.add(estimation.getDueForProcessedOrders());
                ordersToProccess += estimation.getOrdersToProcess();
                processedOrders += estimation.getProcessedOrders();
            }
            if(!orderIdsWithProductUnavailable.isEmpty()) {
                List<PlatformOrderContent> orderContents = platformOrderContentMap.fetchOrderContent(orderIdsWithProductUnavailable);
                List<String> skuIds = orderContents.stream().map(PlatformOrderContent::getSkuId).collect(Collectors.toList());
                List<SkuPrice> skuPrices = platformOrderContentMap.searchSkuPrice(skuIds);
                BigDecimal exchangeRateEurToRmb = exchangeRatesMapper.getLatestExchangeRate("EUR", "RMB");
                for(SkuPrice skuPrice : skuPrices) {
                    if(skuPrice.getPrice() == null) {
                        isCompleteInvoiceReady = false;
                        errorMessages.add("Some sku prices are missing.");
                        break;
                    }
                }
                for(PlatformOrderContent content : orderContents){
                    for (SkuPrice skuPrice : skuPrices) {
                        if(content.getSkuId().equals(skuPrice.getSkuId())) {
                            BigDecimal price = skuPrice.getPrice(content.getQuantity(), exchangeRateEurToRmb);
                            price = price.multiply(new BigDecimal(content.getQuantity()));
                            purchaseEstimation = purchaseEstimation.add(price);
                            break;
                        }
                    }
                }
            }
            else {
                isCompleteInvoiceReady = false;
            }
            if(!currency.equals("EUR")) {
                BigDecimal exchangeRate = exchangeRatesMapper.getLatestExchangeRate("EUR", currency);
                purchaseEstimation = purchaseEstimation.multiply(exchangeRate).setScale(2, RoundingMode.CEILING);
                shippingFeesEstimation = shippingFeesEstimation.multiply(exchangeRate).setScale(2, RoundingMode.CEILING);
            }
            log.info("Purchase Fee {} : {}", currency, purchaseEstimation);
            log.info("Shipping Fee {} : {}", currency, shippingFeesEstimation);
            estimationsByShop.put(shopId, new Estimation(internalCode, ordersToProccess, processedOrders, shippingFeesEstimation, purchaseEstimation, currency, errorMessages, shop, Collections.singletonList(shopId), "", "", isCompleteInvoiceReady, orderIds));
        }
        // return list of estimation by shop
        return Result.ok(estimationsByShop);
    }

    @GetMapping(value = "/checkInvoiceValidity")
    public Result<?> checkInvoiceValidity(@RequestParam("invoiceNumber") String invoiceNumber) {
        boolean isEmployee = securityService.checkIsEmployee();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String email = sysUser.getEmail();
        String invoiceID;
        String customerFullName;
        boolean isShippingInvoice = Invoice.getType(invoiceNumber).equalsIgnoreCase(COMPLETE.name()) || Invoice.getType(invoiceNumber).equalsIgnoreCase(SHIPPING.name());
        if(isShippingInvoice)
            invoiceID = iShippingInvoiceService.getShippingInvoiceId(invoiceNumber);
        else
            invoiceID = purchaseOrderService.getInvoiceId(invoiceNumber);
        // if invoice exists
        if (invoiceID == null) {
            return Result.error(404,"Error 404 page not found.");
        }
        // if user is a customer, we check if he's the owner of the shops
        Client client;
        if(isShippingInvoice)
            client = iShippingInvoiceService.getShopOwnerFromInvoiceNumber(invoiceNumber);
        else
            client = clientService.getClientFromPurchase(invoiceID);
        customerFullName = client.fullName();
        String destEmail;
        if(!isEmployee) {
            log.info("User {} is trying to access invoice {} from client {}", sysUser.getUsername(), invoiceNumber, client.getInternalCode());
            Client userClient = userClientService.getClientByUserId(sysUser.getId());

            if(!client.getId().equals(userClient.getId())) {
                return Result.error(403,"Not authorized to view this page.");
            }
            else {
                destEmail = client.getEmail();
            }
        }
        else {
            destEmail = email;
        }
        JSONObject json = new JSONObject();
        json.put("name", customerFullName);
        json.put("email", destEmail);
        json.put("invoiceEntity", client.getInvoiceEntity());
        json.put("invoiceNumber", invoiceNumber);
        json.put("currency", client.getCurrency());
        return Result.OK(json);
    }

    /**
     *
     * @param invoiceNumber
     * @param originalCurrency
     * @param targetCurrency
     * @return
     */
    @GetMapping(value = "/invoiceData")
    public Result<?> getInvoiceData(@RequestParam("invoiceNumber") String invoiceNumber,
                                    @RequestParam("originalCurrency") String originalCurrency,
                                    @RequestParam("targetCurrency") String targetCurrency
    ){
        InvoiceDatas invoiceDatas = new InvoiceDatas();

        ShippingInvoice invoice = iShippingInvoiceService.getShippingInvoice(invoiceNumber);
        List<PlatformOrder> platformOrderList = iShippingInvoiceService.getPlatformOrder(invoiceNumber);

        List<BigDecimal> refundList = iSavRefundService.getRefundAmount(invoiceNumber);
        Map<String, Map.Entry<Integer, BigDecimal>> feeAndQtyPerCountry = new HashMap<>(); // it maps number of order and shipping fee per country : <France,<250, 50.30>>, <UK, <10, 2.15>>
        BigDecimal serviceFee = BigDecimal.ZERO; // po.order_service_fee + poc.service_fee
        BigDecimal pickingFee = BigDecimal.ZERO;
        BigDecimal packagingMatFee = BigDecimal.ZERO;
        BigDecimal vat = BigDecimal.ZERO;
        BigDecimal refund = BigDecimal.ZERO;

        // on parcours toutes les commandes pour récupérer : country, order_service_fee, fret_fee, picking_fee
        for(PlatformOrder p : platformOrderList) {
            String country = countryNameFormatting(p.getCountry());

            BigDecimal shippingFee = p.getFretFee() == null ? BigDecimal.ZERO : p.getFretFee(); // po.fret_fee + poc.shipping_fee
            serviceFee = p.getOrderServiceFee() == null ? serviceFee : serviceFee.add(p.getOrderServiceFee()) ;
            pickingFee = p.getPickingFee() == null ? pickingFee : pickingFee.add(p.getPickingFee());
            packagingMatFee = p.getPackagingMaterialFee() == null ? packagingMatFee : packagingMatFee.add(p.getPackagingMaterialFee());
            List<PlatformOrderContent> poc = iShippingInvoiceService.getPlatformOrderContent(p.getId());
            // le contenu des commandes pour la vat, service_fee, quantity et picking_fee
            for(PlatformOrderContent pc : poc) {
                serviceFee = pc.getServiceFee() == null ? serviceFee : serviceFee.add(pc.getServiceFee());
                vat = pc.getVat() == null ? vat : vat.add(pc.getVat());
                pickingFee = pc.getPickingFee() == null ? pickingFee : pickingFee.add(pc.getPickingFee());
                shippingFee = pc.getShippingFee() == null ? shippingFee : shippingFee.add(pc.getShippingFee());
            }
            // On vérifie si on a déjà ce pays dans la map
            // si oui on additionne la "qty" et "shipping fee"
            // sinon on ajoute juste à la map
            if(!feeAndQtyPerCountry.containsKey(country)) {
                feeAndQtyPerCountry.put(country, new AbstractMap.SimpleEntry<>(1, shippingFee));
            }
            else {
                BigDecimal existingGlobalFee = feeAndQtyPerCountry.get(country).getValue();
                Integer existingOrderQuantity = feeAndQtyPerCountry.get(country).getKey();
                existingOrderQuantity ++;
                existingGlobalFee = existingGlobalFee.add(shippingFee);
                feeAndQtyPerCountry.remove(country);
                feeAndQtyPerCountry.put(country, new AbstractMap.SimpleEntry<>(existingOrderQuantity, existingGlobalFee));
            }
        }
        // on fait la somme des remboursements
        if(!refundList.isEmpty()) {
            for (BigDecimal amount : refundList) {
                refund = refund.add(amount);
            }
        }

        // si la monnaie utilisé par le client n'est pas l'euro on calcul le total dans sa monnaie
        if(!targetCurrency.equals(originalCurrency)) {
            BigDecimal exchangeRate = iExchangeRatesService.getExchangeRate(originalCurrency,targetCurrency);
            BigDecimal finalAmount = invoice.getFinalAmount().multiply(exchangeRate);
            finalAmount = finalAmount.setScale(2, RoundingMode.DOWN);
            invoiceDatas.setFinalAmount(finalAmount);
        }
        invoiceDatas.setInvoiceNumber(invoiceNumber);
        invoiceDatas.setDiscount(invoice.getDiscountAmount());
        invoiceDatas.setRefund(refund);
        invoiceDatas.setVat(vat);
        invoiceDatas.setFinalAmountEur(invoice.getFinalAmount());
        invoiceDatas.setServiceFee(serviceFee);
        invoiceDatas.setPickingFee(pickingFee);
        invoiceDatas.setPackagingMaterialFee(packagingMatFee);
        invoiceDatas.setFeeAndQtyPerCountry(feeAndQtyPerCountry);

        return Result.OK(invoiceDatas);
    }
    @GetMapping(value = "/purchaseInvoiceData")
    public Result<?> getPurchaseInvoiceData(@RequestParam("invoiceNumber") String invoiceNumber,
                                    @RequestParam("originalCurrency") String originalCurrency,
                                    @RequestParam("targetCurrency") String targetCurrency
    ){
        InvoiceDatas invoiceDatas = new InvoiceDatas();

        List<PurchaseOrder> invoices = purchaseOrderService.getPurchasesByInvoiceNumber(invoiceNumber);
        if(invoices == null) {
            return Result.error("No data for product found.");
        }
        List<PurchaseInvoiceEntry> invoiceData = new ArrayList<>();
        for(PurchaseOrder order : invoices) {
            invoiceData.addAll(purchaseOrderContentMapper.selectInvoiceDataByID(order.getId()));
        }
        List<BigDecimal> refundList = iSavRefundService.getRefundAmount(invoiceNumber);
        Map<String, Map.Entry<Integer, BigDecimal>> feeAndQtyPerSku = new HashMap<>(); // it maps number of order and purchase fee per item : <France,<250, 50.30>>, <UK, <10, 2.15>>
        BigDecimal refund = BigDecimal.ZERO;

        // on parcours toutes les commandes pour récupérer : country, purchaseFee
        for(PurchaseInvoiceEntry data : invoiceData) {
            String sku = data.getSku_en_name();
            BigDecimal purchaseFeePerSku = data.getTotalAmount();
            Integer qty = data.getQuantity();
            // On vérifie si on a déjà ce pays dans la map
            // si oui on additionne la "qty" et "purchase fee"
            // sinon on ajoute juste à la map
            if(!feeAndQtyPerSku.containsKey(sku)) {
                feeAndQtyPerSku.put(sku, new AbstractMap.SimpleEntry<>(qty, purchaseFeePerSku));
            }
            else {
                BigDecimal existingGlobalFee = feeAndQtyPerSku.get(sku).getValue();
                Integer existingOrderQuantity = feeAndQtyPerSku.get(sku).getKey();
                existingOrderQuantity += qty;
                existingGlobalFee = existingGlobalFee.add(purchaseFeePerSku);
                feeAndQtyPerSku.remove(sku);
                feeAndQtyPerSku.put(sku, new AbstractMap.SimpleEntry<>(existingOrderQuantity, existingGlobalFee));
            }
        }
        // on fait la somme des remboursements
        if(!refundList.isEmpty()) {
            for (BigDecimal amount : refundList) {
                refund = refund.add(amount);
            }
        }

        // si la monnaie utilisé par le client n'est pas l'euro on calcul le total dans sa monnaie
        if(!targetCurrency.equals(originalCurrency)) {
            BigDecimal exchangeRate = iExchangeRatesService.getExchangeRate(originalCurrency,targetCurrency);
            BigDecimal finalAmount = invoices.stream().map(PurchaseOrder::getFinalAmount).reduce(BigDecimal.ZERO, BigDecimal::add).multiply(exchangeRate);
            finalAmount = finalAmount.setScale(2, RoundingMode.DOWN);
            invoiceDatas.setFinalAmount(finalAmount);
        }
        invoiceDatas.setInvoiceNumber(invoiceNumber);
        invoiceDatas.setDiscount(invoices.stream().map(PurchaseOrder::getDiscountAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        invoiceDatas.setRefund(refund);
        invoiceDatas.setFinalAmountEur(invoices.stream().map(PurchaseOrder::getFinalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        invoiceDatas.setFeeAndQtyPerSku(feeAndQtyPerSku);

        return Result.OK(invoiceDatas);
    }
    public String countryNameFormatting(String country) {
        Pattern p = Pattern.compile("(\\w*)", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher m = p.matcher(country);
        String res = "";
        while(m.find()) {
            res = res.concat(m.group(1));
        }
        return res;
    }

    /**
     * Sync latest changes of orders whose IDs are specified
     * @param orderIds Ids of order to sync
     * @return Message on whether the request was successfully sent
     */
    @GetMapping(value = "/syncByIds")
    public Result<?> getShopOwnerFromInvoice(@RequestParam("orderIds[]") List<String> orderIds) {
        log.info("Syncing following orders {}", orderIds);
        List<QuartzJob> jobs = quartzJobService.findByJobClassName("org.jeecg.modules.business.domain.job.MabangOrderSyncJob");
        if (jobs == null || jobs.isEmpty()) {
            return Result.error("sys.api.syncOrderJobNotFound");
        }
        try {
            QuartzJob job = jobs.get(0);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderIds", orderIds);
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            jsonObject.put("username", sysUser.getUsername());
            job.setParameter(jsonObject.toJSONString());
            quartzJobService.execute(job);
        } catch (Exception e) {
            log.info("定时任务 立即执行失败>>" + e.getMessage());
            return Result.error("sys.api.executionFailed");
        }
        return Result.OK().success("sys.api.syncRequestSubmitted");
    }
    @GetMapping(value = "/duplicateInvoiceNumberCheck")
    public Result<?> duplicateInvoiceNumberCheck(@RequestParam(value = "id", required = false) String invoiceId,
                                                 @RequestParam("invoiceNumber") String invoiceNumber,
                                                 @RequestParam("invoiceType") String invoiceType) {
        if(invoiceType.equals("purchase")) {
            if(purchaseOrderService.getInvoiceId(invoiceNumber) != null) {
                if(invoiceId != null && purchaseOrderService.getInvoiceId(invoiceNumber).equals(invoiceId))
                    return Result.OK("Invoice number not changed.");
                else
                    return Result.error("Invoice number already exists.");
            }
            else {
                return Result.OK("Invoice number is available.");
            }
        }
        else {
            if(iShippingInvoiceService.getShippingInvoice(invoiceNumber) != null) {
                if(invoiceId != null && iShippingInvoiceService.getShippingInvoice(invoiceNumber).getId().equals(invoiceId))
                    return Result.OK("Invoice number not changed.");
                else 
                    return Result.error("Invoice number already exists.");
            }
            else {
                return Result.OK("Invoice number is available.");
            }
        }
    }
}
