package org.jeecg.modules.business.domain.shippingInvoice;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.domain.codeGeneration.CompleteInvoiceCodeRule;
import org.jeecg.modules.business.domain.codeGeneration.ShippingInvoiceCodeRule;
import org.jeecg.modules.business.domain.purchase.invoice.PurchaseInvoiceEntry;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.PromotionDetail;
import org.jeecg.modules.business.vo.ShippingFeesEstimation;
import org.jeecg.modules.business.vo.SkuQuantity;
import org.jeecg.modules.business.vo.SkuWeightDiscountServiceFees;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.date.DateTime.now;
import static java.util.stream.Collectors.*;

@Slf4j
@Component
public class ShippingInvoiceFactory {

    private final IPlatformOrderService platformOrderService;
    private final ClientMapper clientMapper;
    private final ShopMapper shopMapper;
    private final LogisticChannelPriceMapper logisticChannelPriceMapper;
    private final LogisticChannelMapper logisticChannelMapper;
    private final IPlatformOrderContentService platformOrderContentService;
    private final ISkuDeclaredValueService skuDeclaredValueService;
    private final CountryService countryService;
    private final ExchangeRatesMapper exchangeRatesMapper;
    private final IPurchaseOrderService purchaseOrderService;
    private final PurchaseOrderContentMapper purchaseOrderContentMapper;
    private final SkuPromotionHistoryMapper skuPromotionHistoryMapper;
    private final ISavRefundService savRefundService;
    private final ISavRefundWithDetailService savRefundWithDetailService;

    private final SimpleDateFormat SUBJECT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final List<String> EU_COUNTRY_LIST = Arrays.asList("Austria", "Belgium", "Bulgaria", "Croatia", "Cyprus",
            "Czech", "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hungary", "Ireland", "Italy",
            "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia",
            "Slovenia", "Spain", "Sweden");

    private LoadingCache<Pair<String, Date>, BigDecimal> declaredValueCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<Pair<String, Date>, BigDecimal>() {
                        @Override
                        public BigDecimal load(Pair<String, Date> skuIdAndDate) {
                            return skuDeclaredValueService.getDeclaredValueForDate(skuIdAndDate.getLeft(), skuIdAndDate.getRight());
                        }
                    });

    public ShippingInvoiceFactory(IPlatformOrderService platformOrderService, ClientMapper clientMapper,
                                  ShopMapper shopMapper, LogisticChannelMapper logisticChannelMapper,
                                  LogisticChannelPriceMapper logisticChannelPriceMapper,
                                  IPlatformOrderContentService platformOrderContentService,
                                  ISkuDeclaredValueService skuDeclaredValueService, CountryService countryService,
                                  ExchangeRatesMapper exchangeRatesMapper, IPurchaseOrderService purchaseOrderService,
                                  PurchaseOrderContentMapper purchaseOrderContentMapper,
                                  SkuPromotionHistoryMapper skuPromotionHistoryMapper, ISavRefundService savRefundService,
                                  ISavRefundWithDetailService savRefundWithDetailService) {
        this.platformOrderService = platformOrderService;
        this.clientMapper = clientMapper;
        this.shopMapper = shopMapper;
        this.logisticChannelMapper = logisticChannelMapper;
        this.logisticChannelPriceMapper = logisticChannelPriceMapper;
        this.platformOrderContentService = platformOrderContentService;
        this.skuDeclaredValueService = skuDeclaredValueService;
        this.countryService = countryService;
        this.exchangeRatesMapper = exchangeRatesMapper;
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderContentMapper = purchaseOrderContentMapper;
        this.skuPromotionHistoryMapper = skuPromotionHistoryMapper;
        this.savRefundService = savRefundService;
        this.savRefundWithDetailService = savRefundWithDetailService;
    }

    /**
     * Creates an invoice for a client according to type
     * <p>
     * To generate an invoice, it
     * <ol>
     * <li>Search orders, their contents by IDs and also by erpStatus</li>
     * <li>Generate a new invoice code</li>
     * <li>Find propre logistic channel price for each order </li>
     * <li>Update prices of orders and their contents</li>
     * <li>Generate a invoice</li>
     * <li>Update invoiced their orders and contents to DB</li>
     * </ol>
     *
     * @param customerId the customer id
     * @param ordersIds  the list of order IDs
     * @return the generated invoice
     * @throws UserException if package used by the invoice can not or find more than 1 logistic
     *                       channel price, this exception will be thrown.
     */
    @Transactional
    public ShippingInvoice createShippingInvoice(String customerId, List<String> ordersIds, String type, String start, String end) throws UserException {
        log.info("Creating an invoice with arguments:\n client ID: {}, order IDs: {}]", customerId, ordersIds);
        // find orders and their contents of the invoice
        Map<PlatformOrder, List<PlatformOrderContent>> uninvoicedOrderToContent = platformOrderService.fetchOrderData(ordersIds);
        Set<PlatformOrder> platformOrders = uninvoicedOrderToContent.keySet();
        List<String> shopIds = platformOrders.stream()
                .map(PlatformOrder::getShopId)
                .distinct()
                .collect(Collectors.toList());
        log.info("Orders to be invoiced: {}", uninvoicedOrderToContent);
        String subject;
        if(type.equals("shipping"))
            subject = String.format("Shipping fees from %s to %s", start, end);
        else if(type.equals("pre-shipping"))
            subject = String.format("Pre-Shipping fees, order time from %s to %s", start, end);
        else if(type.equals("all"))
            subject = String.format("Shipping fees, order time from %s to %s", start, end);
        else
            throw new UserException("Couldn't create shipping invoice of unknown type.");
        return createInvoice(customerId, shopIds, uninvoicedOrderToContent, null, subject, true);
    }

    /**
     * Creates a complete shipping (purchase + shipping) invoice for a client
     * <p>
     * To generate an invoice, it
     * <ol>
     * <li>Search orders and their contents by IDs</li>
     * <li>Generate a new invoice code</li>
     * <li>Find propre logistic channel price for each order </li>
     * <li>Update prices of orders and their contents</li>
     * <li>Generate a invoice</li>
     * <li>Update invoiced their orders and contents to DB</li>
     * </ol>
     *
     * @param username   current username
     * @param customerId the customer id
     * @param ordersIds  the list of order IDs
     * @param shippingMethod "post" = postShipping, "pre" = preShipping, "all" = all shipping methods
     * @return the generated invoice
     * @throws UserException if package used by the invoice can not or find more than 1 logistic
     *                       channel price, this exception will be thrown.
     */
    @Transactional
    public CompleteInvoice createCompleteShippingInvoice(String username, String customerId, List<String> ordersIds, String shippingMethod, String start, String end) throws UserException {
        log.info("Creating a complete invoice for \n client ID: {}, order IDs: {}]", customerId, ordersIds);
        // find orders and their contents of the invoice
        Map<PlatformOrder, List<PlatformOrderContent>> uninvoicedOrderToContent = platformOrderService.fetchOrderData(ordersIds);
        Set<PlatformOrder> platformOrders = uninvoicedOrderToContent.keySet();
        List<String> shopIds = platformOrders.stream()
                .map(PlatformOrder::getShopId)
                .distinct()
                .collect(Collectors.toList());
        log.info("Orders to be invoiced: {}", uninvoicedOrderToContent);
        String subject;
        if(shippingMethod.equals("shipping"))
            subject = String.format("Purchase and Shipping fees from %s to %s", start, end);
        else if(shippingMethod.equals("post"))
            subject = String.format("Purchase and post-Shipping fees from %s to %s", start, end);
        else if (shippingMethod.equals("pre-shipping"))
            subject = String.format("Purchase and pre-Shipping fees, order time from %s to %s", start, end);
        else if(shippingMethod.equals("all"))
            subject = String.format("Purchase and Shipping fees, order time from %s to %s", start, end);
        else throw new UserException("Couldn't create complete invoice for unknown shipping method");

        return createInvoice(username, customerId, shopIds, uninvoicedOrderToContent, subject);
    }


    /**
     * Creates an invoice based for a client, a list of shops, a date range.
     * <p>
     * To generate an invoice, it
     * <ol>
     * <li>Search orders and their contents based on shop and date range</li>
     * <li>Generate a new invoice code</li>
     * <li>Find propre logistic channel price for each order </li>
     * <li>Update prices of orders and their contents</li>
     * <li>Generate a invoice</li>
     * <li>Update invoiced their orders and contents to DB</li>
     * </ol>
     *
     * @param username   Current username
     * @param customerId Customer ID
     * @param shopIds    Shop IDs
     * @param subject    Invoice subject
     * @return the generated invoice
     * @throws UserException if package used by the invoice can not or find more than 1 logistic
     *                       channel price, this exception will be thrown.
     */
    @Transactional
    public CompleteInvoice createInvoice(String username, String customerId, List<String> shopIds,
                                         Map<PlatformOrder, List<PlatformOrderContent>> orderAndContent,
                                         String subject) throws UserException {
        Client client = clientMapper.selectById(customerId);
        log.info("User {} is creating a complete invoice for customer {}", username, client.getInternalCode());

        log.info("Orders to be invoiced: {}", orderAndContent);
        if (orderAndContent == null) {
            throw new UserException("No platform order in the selected period!");
        }
        Map<String, BigDecimal> skuRealWeights = new HashMap<>();
        Map<String, BigDecimal> skuServiceFees = new HashMap<>();
        skuDataPreparation(skuRealWeights, skuServiceFees);
        List<Country> countryList = countryService.findAll();
        Map<String, LogisticChannel> logisticChannelMap = logisticChannelMapper.getAll().stream()
                .collect(toMap(LogisticChannel::getId, Function.identity()));
        Map<LogisticChannel, List<LogisticChannelPrice>> channelPriceMap = getChannelPriceMap(logisticChannelMap, orderAndContent, true);
        List<SkuDeclaredValue> latestDeclaredValues = skuDeclaredValueService.getLatestDeclaredValues();

        List<Shop> shops = shopMapper.selectBatchIds(shopIds);
        Map<String, BigDecimal> shopServiceFeeMap = new HashMap<>();
        Map<String, BigDecimal> shopPackageMatFeeMap = new HashMap<>();
        shops.forEach(shop -> shopServiceFeeMap.put(shop.getId(), shop.getOrderServiceFee()));
        shops.forEach(shop -> shopPackageMatFeeMap.put(shop.getId(), shop.getPackagingMaterialFee()));
        String invoiceCode = generateCompleteInvoiceCode();
        log.info("New invoice code: {}", invoiceCode);
        calculateFees(logisticChannelMap, orderAndContent, channelPriceMap, countryList, skuRealWeights, skuServiceFees,
                latestDeclaredValues, client, shopServiceFeeMap, shopPackageMatFeeMap, invoiceCode);
        BigDecimal eurToUsd = exchangeRatesMapper.getLatestExchangeRate("EUR", "USD");

        List<String> orderIds = orderAndContent.keySet().stream().map(PlatformOrder::getId).collect(toList());
        List<SkuQuantity> skuQuantities = platformOrderContentService.searchOrderContent(orderIds);

        String purchaseID = purchaseOrderService.addPurchase(username, client, invoiceCode, skuQuantities, orderAndContent);

        List<PurchaseInvoiceEntry> purchaseOrderSkuList = purchaseOrderContentMapper.selectInvoiceDataByID(purchaseID);
        List<PromotionDetail> promotionDetails = skuPromotionHistoryMapper.selectPromotionByPurchase(purchaseID);

        updateOrdersAndContentsInDb(orderAndContent);

        return new CompleteInvoice(client, invoiceCode, subject, orderAndContent,
                purchaseOrderSkuList, promotionDetails, eurToUsd);
    }

    private BigDecimal calculateAndUpdateContentFees(Map<String, BigDecimal> skuRealWeights, Map<String, BigDecimal> skuServiceFees,
                                               PlatformOrder uninvoicedOrder, BigDecimal contentWeight, BigDecimal totalShippingFee,
                                               BigDecimal clientVatPercentage, Map<PlatformOrderContent, BigDecimal> contentDeclaredValueMap,
                                               BigDecimal totalDeclaredValue, BigDecimal totalVAT, boolean vatApplicable,
                                               BigDecimal pickingFeePerItem, PlatformOrderContent content, BigDecimal remainingShippingFee) {
        String skuId = content.getSkuId();
        BigDecimal realWeight = skuRealWeights.get(skuId);
        // Each content will share the total shipping fee proportionally, because minimum price and unit price
        // vary with total weight, so calculating each content's fee with its weight is just wrong
        BigDecimal shippingFee = realWeight.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO :
                totalShippingFee.multiply(BigDecimal.valueOf(content.getQuantity()))
                        .multiply(realWeight)
                        .divide(contentWeight, RoundingMode.UP)
                        .setScale(2, RoundingMode.UP);
        if (remainingShippingFee.compareTo(shippingFee) < 0) {
            content.setShippingFee(remainingShippingFee);
            remainingShippingFee = BigDecimal.ZERO;
        } else {
            content.setShippingFee(shippingFee);
            remainingShippingFee = remainingShippingFee.subtract(shippingFee);
        }
        content.setServiceFee(skuServiceFees.get(skuId)
                .multiply(BigDecimal.valueOf(content.getQuantity()))
                .setScale(2, RoundingMode.UP)
        );
        content.setPickingFee(pickingFeePerItem
                .multiply(BigDecimal.valueOf(content.getQuantity()))
                .setScale(2, RoundingMode.UP)
        );
        BigDecimal vat = BigDecimal.ZERO;
        if (vatApplicable) {
            BigDecimal contentDeclaredValue = contentDeclaredValueMap.get(content);
            // Total VAT greater than 0 means total declared value is less than minimum declared value
            // VAT of content must be re-adjusted proportionally
            if (totalVAT.compareTo(BigDecimal.ZERO) > 0) {
                log.info("VAT re-adjusted for SKU : {} of order {}", content.getSkuId(), uninvoicedOrder.getId());
                vat = totalVAT.multiply(contentDeclaredValue)
                        .divide(totalDeclaredValue, RoundingMode.UP)
                        .setScale(2, RoundingMode.UP);
            } else {
                vat = contentDeclaredValue
                        .multiply(clientVatPercentage)
                        .setScale(2, RoundingMode.UP);
            }
        }
        content.setVat(vat);
        return remainingShippingFee;
    }

    /**
     * Creates an invoice based for a client, a list of shops, a date range and erp statuses.
     * <p>
     * To generate an invoice, it
     * <ol>
     * <li>Search orders and their contents based on shop and date range</li>
     * <li>Generate a new invoice code</li>
     * <li>Find propre logistic channel price for each order </li>
     * <li>Update prices of orders and their contents</li>
     * <li>Generate a invoice</li>
     * <li>Update invoiced orders and contents to DB</li>
     * </ol>
     *
     * @param customerId the customer id
     * @param shopIds    the list of shop codes
     * @param begin      the beginning of the date range
     * @param end        the end of the date range
     * @param erpStatuses possible values : [3], [1,2] and [1,2,3] corresponding to erp statuses of orders we want to invoice
     * @return the generated invoice
     * @throws UserException if package used by the invoice can not or find more than 1 logistic
     *                       channel price, this exception will be thrown.
     */
    @Transactional
    public ShippingInvoice createInvoice(String customerId, List<String> shopIds, Date begin, Date end, List<Integer> erpStatuses, List<String> warehouses) throws UserException {
        log.info(
                "Creating an invoice with arguments:\n client ID: {}, shop IDs: {}, period:[{} - {}]",
                customerId, shopIds.toString(), begin, end
        );
        // find orders and their contents of the invoice
        Map<PlatformOrder, List<PlatformOrderContent>> uninvoicedOrderToContent;
        List<SavRefundWithDetail> savRefunds = savRefundWithDetailService.findUnprocessedRefundsByClient(customerId);
        String subject;
        if(erpStatuses.toString().equals("[3]")) {
            subject = String.format(
                    "Shipping fees from %s to %s",
                    SUBJECT_FORMAT.format(begin),
                    SUBJECT_FORMAT.format(end)
            );
            uninvoicedOrderToContent = platformOrderService.findUninvoicedOrders(shopIds, begin, end, warehouses);
        }
        else if (erpStatuses.toString().equals("[1, 2]")) {
            subject = String.format(
                    "Pre-Shipping fees order time from %s to %s",
                    SUBJECT_FORMAT.format(begin),
                    SUBJECT_FORMAT.format(end)
            );
            uninvoicedOrderToContent = platformOrderService.findUninvoicedOrderContentsForShopsAndStatus(shopIds, begin, end, erpStatuses, warehouses);
        }
        else {
            subject = String.format(
                    "Shipping fees order time from %s to %s",
                    SUBJECT_FORMAT.format(begin),
                    SUBJECT_FORMAT.format(end)
            );
            uninvoicedOrderToContent = platformOrderService.findUninvoicedOrderContentsForShopsAndStatus(shopIds, begin, end, erpStatuses, warehouses);
        }
        return createInvoice(customerId, shopIds, uninvoicedOrderToContent, savRefunds, subject, false);
    }

    /**
     * Creates an invoice based for a client, a list of shops, a date range.
     * <p>
     * To generate an invoice, it
     * <ol>
     * <li>Search orders and their contents based on shop and date range</li>
     * <li>Generate a new invoice code</li>
     * <li>Find propre logistic channel price for each order </li>
     * <li>Update prices of orders and their contents</li>
     * <li>Generate a invoice</li>
     * <li>Update invoiced their orders and contents to DB</li>
     * </ol>
     *
     * @param customerId                Customer ID
     * @param shopIds                   Shop IDs
     * @param subject                   Invoice subject
     * @param orderAndContent           Map between PlatformOrder and their contents
     * @param savRefunds                List of SAV refunds
     * @param skipShippingTimeComparing Skip comparing shipping time, true for Pre-shipping, false otherwise
     * @return the generated invoice
     * @throws UserException if package used by the invoice can not or find more than 1 logistic
     *                       channel price, this exception will be thrown.
     */
    @Transactional
    public ShippingInvoice createInvoice(String customerId, List<String> shopIds,
                                         Map<PlatformOrder, List<PlatformOrderContent>> orderAndContent,
                                         List<SavRefundWithDetail> savRefunds,
                                         String subject, boolean skipShippingTimeComparing) throws UserException {
        log.info("Orders to be invoiced: {}", orderAndContent);
        if (orderAndContent == null) {
            throw new UserException("No platform order in the selected period!");
        }
        Map<String, BigDecimal> skuRealWeights = new HashMap<>();
        Map<String, BigDecimal> skuServiceFees = new HashMap<>();
        skuDataPreparation(skuRealWeights, skuServiceFees);
        List<Country> countryList = countryService.findAll();
        Map<LogisticChannel, List<LogisticChannelPrice>> channelPriceMap;
        Map<String, LogisticChannel> logisticChannelMap = logisticChannelMapper.getAll().stream()
                .collect(toMap(LogisticChannel::getId, Function.identity()));
        if(subject.contains("Pre") || subject.contains("All")) {
            channelPriceMap = getChannelPriceMap(logisticChannelMap, orderAndContent, skipShippingTimeComparing, "order");
        }
        else {
            channelPriceMap = getChannelPriceMap(logisticChannelMap, orderAndContent, skipShippingTimeComparing);
        }
        List<SkuDeclaredValue> latestDeclaredValues = skuDeclaredValueService.getLatestDeclaredValues();

        Client client = clientMapper.selectById(customerId);
        List<Shop> shops = shopMapper.selectBatchIds(shopIds);
        Map<String, BigDecimal> shopServiceFeeMap = new HashMap<>();
        Map<String, BigDecimal> shopPackageMatFeeMap = new HashMap<>();
        shops.forEach(shop -> shopServiceFeeMap.put(shop.getId(), shop.getOrderServiceFee()));
        shops.forEach(shop -> shopPackageMatFeeMap.put(shop.getId(), shop.getPackagingMaterialFee()));
        String invoiceCode = generateInvoiceCode();
        log.info("New invoice code: {}", invoiceCode);
        calculateFees(logisticChannelMap, orderAndContent, channelPriceMap, countryList, skuRealWeights, skuServiceFees,
                latestDeclaredValues, client, shopServiceFeeMap, shopPackageMatFeeMap, invoiceCode);
        BigDecimal eurToUsd = exchangeRatesMapper.getLatestExchangeRate("EUR", "USD");
        if (savRefunds != null) {
            updateSavRefundsInDb(savRefunds, invoiceCode);
        }
        ShippingInvoice invoice = new ShippingInvoice(client, invoiceCode, subject, orderAndContent, savRefunds, eurToUsd);
        updateOrdersAndContentsInDb(orderAndContent);
        return invoice;
    }

    /**
     * Construct a map between LogisticChannel and LogisticChannelPrices, by using distinct country names and
     * distinct effective logistic channel names(invoice logistic channel names trump over logistic channel names)
     * from orders as filter.
     * In the case where the logistic channel name points to a ghost channel(i.e. a channel whose samePriceChannelId isn't null),
     * the result map may NOT contain the real channel if no order is actually shipped from it.
     * @param logisticChannelMap A map of LogisticChannels keyed by IDs
     * @param orderAndContent Map of order and contents
     * @param skipShippingTimeComparing True if shipping time comparison should be skipped, false otherwise
     * @return
     */
    private Map<LogisticChannel, List<LogisticChannelPrice>> getChannelPriceMap(Map<String, LogisticChannel> logisticChannelMap,
                                                                                Map<PlatformOrder, List<PlatformOrderContent>> orderAndContent,
                                                                                boolean skipShippingTimeComparing, String ... dateType ) {
        Date latestShippingTime;
        List<PlatformOrder> sortedList;
        if (skipShippingTimeComparing) {
            sortedList = new ArrayList<>(orderAndContent.keySet());
            latestShippingTime = now().toSqlDate();
        } else {
            if(dateType.length == 0) {
                sortedList = orderAndContent.keySet().stream()
                        .sorted(Comparator.comparing(PlatformOrder::getShippingTime).reversed())
                        .collect(Collectors.toList());
                latestShippingTime = sortedList.get(0).getShippingTime() == null ? now().toSqlDate() : sortedList.get(0).getShippingTime();
            }
            else {
                sortedList = orderAndContent.keySet().stream()
                        .sorted(Comparator.comparing(PlatformOrder::getOrderTime).reversed())
                        .collect(Collectors.toList());
                latestShippingTime = sortedList.get(0).getOrderTime() == null ? now().toSqlDate() : sortedList.get(0).getOrderTime();
            }
        }
        List<String> distinctCountries = sortedList.stream().map(PlatformOrder::getCountry).distinct().collect(toList());
        List<String> distinctChannelNames = sortedList.stream()
                .map(order -> order.getInvoiceLogisticChannelName() == null ? order.getLogisticChannelName() : order.getInvoiceLogisticChannelName())
                .distinct().collect(toList());
        List<LogisticChannelPrice> allEligiblePrices = logisticChannelPriceMapper.findPricesBy(latestShippingTime,
                distinctCountries, distinctChannelNames);
        return allEligiblePrices.stream().collect(
                groupingBy(logisticChannelPrice -> logisticChannelMap.get(logisticChannelPrice.getChannelId()))
        );
    }

    private void skuDataPreparation(Map<String, BigDecimal> skuRealWeights, Map<String, BigDecimal> skuServiceFees) {
        for (SkuWeightDiscountServiceFees skuWeightDiscountServiceFees : platformOrderContentService.getAllSKUWeightsDiscountsServiceFees()) {
            if (skuWeightDiscountServiceFees.getWeight() != null) {
                skuRealWeights.put(skuWeightDiscountServiceFees.getSkuId(),
                        skuWeightDiscountServiceFees.getDiscount().multiply(BigDecimal.valueOf(skuWeightDiscountServiceFees.getWeight())));
            }
            skuServiceFees.put(skuWeightDiscountServiceFees.getSkuId(), skuWeightDiscountServiceFees.getServiceFees());
        }
    }

    private void calculateFees(Map<String, LogisticChannel> logisticChannelMap, Map<PlatformOrder, List<PlatformOrderContent>> orderAndContent,
                               Map<LogisticChannel, List<LogisticChannelPrice>> channelPriceMap,
                               List<Country> countryList,
                               Map<String, BigDecimal> skuRealWeights,
                               Map<String, BigDecimal> skuServiceFees,
                               List<SkuDeclaredValue> latestDeclaredValues,
                               Client client,
                               Map<String, BigDecimal> shopServiceFeeMap,
                               Map<String, BigDecimal> shopPackageMatFeeMap,
                               String invoiceCode
    ) throws UserException {
        // find logistic channel price for each order based on its content
        for (PlatformOrder uninvoicedOrder : orderAndContent.keySet()) {
            List<PlatformOrderContent> contents = orderAndContent.get(uninvoicedOrder);
            if (contents.size() == 0) {
                throw new UserException("Order: {} doesn't have content", uninvoicedOrder.getPlatformOrderId());
            }
            log.info("Calculating price for {} of order {}", contents, uninvoicedOrder);
            Map<String, Integer> contentMap = new HashMap<>();
            for (PlatformOrderContent content : contents) {
                String skuId = content.getSkuId();
                if (contentMap.containsKey(skuId)) {
                    contentMap.put(skuId, contentMap.get(skuId) + content.getQuantity());
                } else {
                    contentMap.put(skuId, content.getQuantity());
                }
            }

            // calculate weight of an order
            BigDecimal contentWeight = platformOrderContentService.calculateWeight(
                    contentMap,
                    skuRealWeights
            );
            Pair<LogisticChannel, LogisticChannelPrice> logisticChannelPair = findAppropriatePrice(countryList, logisticChannelMap,
                    channelPriceMap, uninvoicedOrder, contentWeight);
            LogisticChannelPrice price = logisticChannelPair.getRight();
            // update attributes of orders and theirs content
            BigDecimal packageMatFee = shopPackageMatFeeMap.get(uninvoicedOrder.getShopId());
            if(packageMatFee.compareTo(BigDecimal.ZERO) > 0 && logisticChannelPair.getLeft().getWarehouseInChina().equalsIgnoreCase("0")) {
                uninvoicedOrder.setPackagingMaterialFee(packageMatFee);
            }
            uninvoicedOrder.setFretFee(price.getRegistrationFee());
            uninvoicedOrder.setPickingFee(price.getAdditionalCost());
            uninvoicedOrder.setOrderServiceFee(shopServiceFeeMap.get(uninvoicedOrder.getShopId()));
            uninvoicedOrder.setShippingInvoiceNumber(invoiceCode);
            BigDecimal totalShippingFee = price.calculateShippingPrice(contentWeight);
            BigDecimal pickingFeePerItem = price.getPickingFeePerItem();
            BigDecimal clientVatPercentage = client.getVatPercentage();
            Map<PlatformOrderContent, BigDecimal> contentDeclaredValueMap = new HashMap<>();
            BigDecimal totalDeclaredValue = calculateTotalDeclaredValue(contents, contentDeclaredValueMap, latestDeclaredValues);
            BigDecimal totalVAT = BigDecimal.ZERO;
            boolean vatApplicable = clientVatPercentage.compareTo(BigDecimal.ZERO) > 0
                    && EU_COUNTRY_LIST.contains(uninvoicedOrder.getCountry())
                    // If picking fee per item = 0, it means the package was sent from China so VAT applicable
                    && price.getPickingFeePerItem().compareTo(BigDecimal.ZERO) == 0;
            // In case where VAT is applicable, and the transport line has a minimum declared value (MDV) per PACKAGE
            // We need to first calculate the total declared value and compare it to the MDV
            // If the total declared value is below the MDV, then the VAT should be calculated with the MDV and
            // then proportionally applied to each content
            BigDecimal minimumDeclaredValue = price.getMinimumDeclaredValue();
            if (vatApplicable && minimumDeclaredValue != null) {
                totalVAT = calculateTotalVat(totalDeclaredValue, clientVatPercentage, minimumDeclaredValue);
            }
            // Since we always round up when distributing shipping fees to contents, sometimes the final total sum
            // is bigger than initial total shipping fee, the remedy is to deduct from the initial total, so we never go
            // above it
            BigDecimal remainingShippingFee = totalShippingFee;
            for (PlatformOrderContent content : contents) {
                remainingShippingFee = calculateAndUpdateContentFees(skuRealWeights, skuServiceFees, uninvoicedOrder, contentWeight,
                        totalShippingFee, clientVatPercentage, contentDeclaredValueMap, totalDeclaredValue, totalVAT,
                        vatApplicable, pickingFeePerItem, content, remainingShippingFee);
            }
        }
    }

    private void updateOrdersAndContentsInDb(Map<PlatformOrder, List<PlatformOrderContent>> orderAndContent) {
        // update them to DB after invoiced
        platformOrderService.updateBatchById(orderAndContent.keySet());
        platformOrderContentService.updateBatchById(orderAndContent.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList()));
    }

    private void updateSavRefundsInDb(List<SavRefundWithDetail> savRefunds, String invoiceCode) {
        List<SavRefund> savRefundList = new ArrayList<>();
        savRefunds.forEach(savRefundWithDetail -> {
            savRefundWithDetail.setInvoiceNumber(invoiceCode);
            if (savRefundWithDetail.getShippingRefund().equalsIgnoreCase("Y")) {
                savRefundWithDetail.setTotalRefundAmount(savRefundWithDetail.getFretFee()
                        .add(savRefundWithDetail.getShippingFee())
                        .add(savRefundWithDetail.getVat())
                        .add(savRefundWithDetail.getServiceFee())
                );
            }
            if (savRefundWithDetail.getPurchaseRefund().equalsIgnoreCase("Y")) {
                savRefundWithDetail.setTotalRefundAmount(savRefundWithDetail.getTotalRefundAmount()
                        .add(savRefundWithDetail.getPurchaseRefundAmount()));
            }
            savRefundWithDetail.setRefundDate(new Date());
            SavRefund target = new SavRefund();
            BeanUtils.copyProperties(savRefundWithDetail, target);
            savRefundList.add(target);
        });
        savRefundService.updateBatchById(savRefundList);
    }

    private BigDecimal calculateTotalVat(BigDecimal totalDeclaredValue, BigDecimal clientVatPercentage,
                                         BigDecimal minimumDeclaredValue) {
        BigDecimal totalVAT = BigDecimal.ZERO;
        if (totalDeclaredValue.compareTo(minimumDeclaredValue) < 0) {
            totalVAT = minimumDeclaredValue.multiply(clientVatPercentage)
                    .setScale(2, RoundingMode.UP);
        }
        return totalVAT;
    }

    private BigDecimal calculateTotalDeclaredValue(List<PlatformOrderContent> contents,
                                                   Map<PlatformOrderContent, BigDecimal> contentDeclaredValueMap,
                                                   List<SkuDeclaredValue> latestDeclaredValues) {
        BigDecimal totalDeclaredValue = BigDecimal.ZERO;
        for (PlatformOrderContent content : contents) {
            String skuId = content.getSkuId();
            Optional<SkuDeclaredValue> declaredValueForSKU = latestDeclaredValues.stream()
                    .filter(sdv -> sdv.getSkuId().equals(skuId)).findFirst();

            BigDecimal contentDeclaredValue;
            try {
                contentDeclaredValue = declaredValueForSKU.get().getDeclaredValue()
                        .multiply(BigDecimal.valueOf(content.getQuantity()));
            }
            catch(Exception e) {
                throw new RuntimeException("No declared value for SKU ID : " + skuId);
            }
            contentDeclaredValueMap.put(content, contentDeclaredValue);
            totalDeclaredValue = totalDeclaredValue.add(contentDeclaredValue);
        }
        return totalDeclaredValue;
    }

    /**
     *
     * @param countryList
     * @param logisticChannelMap Map of LogisticChannels where keys are IDs
     * @param channelPriceMap
     * @param uninvoicedOrder
     * @param contentWeight
     * @return
     * @throws UserException
     */
    @NotNull
    private Pair<LogisticChannel, LogisticChannelPrice> findAppropriatePrice(List<Country> countryList, Map<String, LogisticChannel> logisticChannelMap,
                                                                             Map<LogisticChannel, List<LogisticChannelPrice>> channelPriceMap,
                                                                             PlatformOrder uninvoicedOrder, BigDecimal contentWeight) throws UserException {
        LogisticChannelPrice price;
        LogisticChannel channel;
        try {
            /* Find channel price */
            Optional<Country> foundCountry = countryList.stream()
                    .filter(country -> country.getNameEn().equals(uninvoicedOrder.getCountry())
                            || country.getSpecialName().equals(uninvoicedOrder.getCountry())).findFirst();
            if (!foundCountry.isPresent()) {
                String msg = "Couldn't find country named " + uninvoicedOrder.getCountry();
                log.error(msg);
                throw new UserException(msg);
            }

            Date shippingTime = uninvoicedOrder.getShippingTime() == null ? now().toSqlDate() : uninvoicedOrder.getShippingTime();
            Map<String, LogisticChannel> logisticChannelNameMap = channelPriceMap.keySet().stream().collect(toMap(LogisticChannel::getZhName, Function.identity()));
            // If an invoice logistic channel already present, use it,
            // otherwise write in the same price logistic channel name (if present)
            String invoiceLogisticChannelName = uninvoicedOrder.getInvoiceLogisticChannelName();
            String logisticChannelName = invoiceLogisticChannelName == null ? uninvoicedOrder.getLogisticChannelName() : invoiceLogisticChannelName;
            channel = logisticChannelNameMap.get(logisticChannelName);
            if (invoiceLogisticChannelName == null && channel != null) {
                String samePriceChannelId = channel.getSamePriceChannelId();
                if (samePriceChannelId != null) {
                    LogisticChannel samePriceChannel = logisticChannelMap.get(samePriceChannelId);
                    if (samePriceChannel != null) {
                        // We don't affect samePriceChannel to channel here because channelPriceMap may not contain it
                        uninvoicedOrder.setInvoiceLogisticChannelName(samePriceChannel.getZhName());
                    }
                }
            }
            if (channel == null) {
                String format = "Can not find propre channel for" +
                        "package Serial No: %s, delivered at %s, " +
                        "weight: %s, channel name: %s, destination: %s";
                String msg = String.format(
                        format,
                        uninvoicedOrder.getPlatformOrderId(),
                        shippingTime,
                        contentWeight,
                        logisticChannelName,
                        uninvoicedOrder.getCountry()
                );
                log.error(msg);
                throw new UserException(msg);
            }
            Optional<LogisticChannelPrice> priceCandidate = channelPriceMap.get(channel).stream()
                    .filter(channelPrice -> channelPrice.getEffectiveCountry().equals(foundCountry.get().getCode()))
                    .filter(channelPrice -> channelPrice.getWeightRangeEnd() >= contentWeight.intValue()
                            && channelPrice.getWeightRangeStart() < contentWeight.intValue())
                    .filter(channelPrice -> channelPrice.getEffectiveDate().before(shippingTime))
                    .max(Comparator.comparing(LogisticChannelPrice::getEffectiveDate));
            price = priceCandidate.orElse(null);
            if (price == null) {
                String format = "Can not find propre channel price for" +
                        "package Serial No: %s, delivered at %s, " +
                        "weight: %s, channel name: %s, destination: %s";
                String msg = String.format(
                        format,
                        uninvoicedOrder.getPlatformOrderId(),
                        shippingTime,
                        contentWeight,
                        logisticChannelName,
                        uninvoicedOrder.getCountry()
                );
                log.error(msg);
                throw new UserException(msg);
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            String msg = "Found more than 1 channel price for package Order No: " + uninvoicedOrder.getPlatformOrderNumber()
                    + ", delivered at " + uninvoicedOrder.getShippingTime().toString();
            log.error(msg);
            throw new UserException(msg);
        }
        return Pair.of(channel, price);
    }

    /**
     * Generate a new invoice code, it is generated based on latest invoice's code.
     * <p>
     * If there is no invoice this month, the new code will be N°yyyy-MM-2001,
     * otherwise, the new code will be N°yyyy-MM-No, where "No" is the "No" part of last invoice's code + 1.
     *
     * @return the invoice code.
     */
    private String generateInvoiceCode() {
        String lastInvoiceCode = platformOrderService.findPreviousInvoice();

        ShippingInvoiceCodeRule rule = new ShippingInvoiceCodeRule();
        return rule.next(lastInvoiceCode);
    }

    /**
     * Generate a new invoice code, it is generated based on latest invoice's code.
     * <p>
     * If there is no invoice this month, the new code will be N°yyyy-MM-2001,
     * otherwise, the new code will be N°yyyy-MM-No, where "No" is the "No" part of last invoice's code + 1.
     *
     * @return the invoice code.
     */
    private String generateCompleteInvoiceCode() {
        String lastInvoiceCode = platformOrderService.findPreviousCompleteInvoice();

        CompleteInvoiceCodeRule rule = new CompleteInvoiceCodeRule();
        return rule.next(lastInvoiceCode);
    }

    public List<ShippingFeesEstimation> getEstimations(List<String> errorMessages) {
        List<ShippingFeesEstimation> estimations = new ArrayList<>();
        Map<String, Map<PlatformOrder, List<PlatformOrderContent>>> uninvoicedOrdersByShopId = platformOrderService.findUninvoicedOrders();
        Set<String> shopIds = uninvoicedOrdersByShopId.keySet();
        Set<String> clientIds = new HashSet<>();
        List<Shop> shops = shopMapper.selectBatchIds(shopIds);
        shops.forEach(shop -> clientIds.add(clientMapper.selectById(shop.getOwnerId()).getId()));
        List<Client> clients = clientMapper.selectBatchIds(clientIds);
        Map<String, Client> clientMap = clients.stream().collect(toMap(Client::getId, Function.identity()));

        List<Country> countryList = countryService.findAll();
        List<SkuDeclaredValue> latestDeclaredValues = skuDeclaredValueService.getLatestDeclaredValues();

        Map<String, BigDecimal> skuRealWeights = new HashMap<>();
        Map<String, BigDecimal> skuServiceFees = new HashMap<>();
        skuDataPreparation(skuRealWeights, skuServiceFees);

        Map<Client, List<Shop>> clientToShopsMap = shops.stream().collect(
                groupingBy(shop -> clientMap.get(shop.getOwnerId()))
        );

        Map<PlatformOrder, List<PlatformOrderContent>> flattenedOrdersMap = uninvoicedOrdersByShopId.values().stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, LogisticChannel> logisticChannelMap = logisticChannelMapper.getAll().stream()
                .collect(toMap(LogisticChannel::getId, Function.identity()));
        Map<LogisticChannel, List<LogisticChannelPrice>> channelPriceMap = getChannelPriceMap(logisticChannelMap, flattenedOrdersMap, true);

        for (Map.Entry<Client, List<Shop>> entry : clientToShopsMap.entrySet()) {
            Client client = entry.getKey();
            List<Shop> shopList = entry.getValue();
            for (Shop shop : shopList) {
                Map<String, BigDecimal> shopServiceFeeMap = new HashMap<>();
                Map<String, BigDecimal> shopPackageMatFeeMap = new HashMap<>();
                shopServiceFeeMap.put(shop.getId(), shop.getOrderServiceFee());
                shopPackageMatFeeMap.put(shop.getId(), shop.getPackagingMaterialFee());
                Map<PlatformOrder, List<PlatformOrderContent>> orders = uninvoicedOrdersByShopId.get(shop.getId());
                try {
                    calculateFees(logisticChannelMap, orders, channelPriceMap, countryList, skuRealWeights, skuServiceFees,
                            latestDeclaredValues, client, shopServiceFeeMap,shopPackageMatFeeMap, null);
                    BigDecimal eurToUsd = exchangeRatesMapper.getLatestExchangeRate("EUR", "USD");
                    ShippingInvoice invoice = new ShippingInvoice(client, "", "", orders, null, eurToUsd);
                    // Calculate total amounts
                    invoice.tableData();
                    estimations.add(new ShippingFeesEstimation(
                            client.getInternalCode(), shop.getErpCode(), 0, orders.entrySet().size(), invoice.getTotalAmount()));
                } catch (UserException e) {
                    log.error("Couldn't calculate all fees for shop {} for following reason {}", shop.getErpCode(), e.getMessage());
                    errorMessages.add(e.getMessage());
                }
            }
        }
        return estimations.stream()
                .sorted(Comparator.comparing(ShippingFeesEstimation::getDueForProcessedOrders).reversed())
                .collect(Collectors.toList());
    }

    public List<ShippingFeesEstimation> getEstimations(String clientId, List<String> orderIds, List<String> errorMessages) {
        List<ShippingFeesEstimation> estimations = new ArrayList<>();
        Map<PlatformOrder, List<PlatformOrderContent>> ordersMap = platformOrderService.fetchOrderData(orderIds);
        Set<PlatformOrder> orderSet = ordersMap.keySet();
        Map<String, PlatformOrder> orderMap = orderSet.stream().collect(toMap(PlatformOrder::getId, Function.identity()));
        Map<String, String> orderMapByShopId = orderSet.stream().collect(toMap(PlatformOrder::getId, PlatformOrder::getShopId));
        List<PlatformOrderContent> orderContents = ordersMap.values().stream().flatMap(Collection::stream).collect(toList());
        Map<String, Map<PlatformOrder, List<PlatformOrderContent>>> uninvoicedOrdersByShopId = orderContents.stream()
                .collect(
                        groupingBy(
                                platformOrderContent -> orderMapByShopId.get(platformOrderContent.getPlatformOrderId()),
                                groupingBy(platformOrderContent -> orderMap.get(platformOrderContent.getPlatformOrderId()))
                        )
                );
        Collection<String> shopIds = orderMapByShopId.values();
        Client client = clientMapper.selectById(clientId);
        List<Shop> shops = shopMapper.selectBatchIds(shopIds);

        List<Country> countryList = countryService.findAll();
        List<SkuDeclaredValue> latestDeclaredValues = skuDeclaredValueService.getLatestDeclaredValues();

        Map<String, BigDecimal> skuRealWeights = new HashMap<>();
        Map<String, BigDecimal> skuServiceFees = new HashMap<>();
        skuDataPreparation(skuRealWeights, skuServiceFees);

        Map<String, LogisticChannel> logisticChannelMap = logisticChannelMapper.getAll().stream()
                .collect(toMap(LogisticChannel::getId, Function.identity()));
        Map<LogisticChannel, List<LogisticChannelPrice>> channelPriceMap = getChannelPriceMap(logisticChannelMap, ordersMap, true);


        for (Shop shop : shops) {
            Map<String, BigDecimal> shopServiceFeeMap = new HashMap<>();
            Map<String, BigDecimal> shopPackageMatFeeMap = new HashMap<>();
            shopServiceFeeMap.put(shop.getId(), shop.getOrderServiceFee());
            shopPackageMatFeeMap.put(shop.getId(), shop.getPackagingMaterialFee());
            Map<PlatformOrder, List<PlatformOrderContent>> orders = uninvoicedOrdersByShopId.get(shop.getId());
            try {
                calculateFees(logisticChannelMap, orders, channelPriceMap, countryList, skuRealWeights, skuServiceFees,
                        latestDeclaredValues, client, shopServiceFeeMap, shopPackageMatFeeMap, null);
                BigDecimal eurToUsd = exchangeRatesMapper.getLatestExchangeRate("EUR", "USD");
                ShippingInvoice invoice = new ShippingInvoice(client, "", "", orders, null, eurToUsd);
                // Calculate total amounts
                invoice.tableData();
                estimations.add(new ShippingFeesEstimation(
                        client.getInternalCode(), shop.getErpCode(), 0, orders.entrySet().size(), invoice.getTotalAmount()));
            } catch (UserException e) {
                log.error("Couldn't calculate all fees for shop {} for following reason {}", shop.getErpCode(), e.getMessage());
                errorMessages.add(e.getMessage());
            }
        }
        return estimations;
    }
}
