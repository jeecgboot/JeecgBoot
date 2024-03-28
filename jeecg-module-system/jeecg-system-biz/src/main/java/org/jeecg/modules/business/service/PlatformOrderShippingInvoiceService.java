package org.jeecg.modules.business.service;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.domain.excel.SheetManager;
import org.jeecg.modules.business.domain.purchase.invoice.PurchaseInvoice;
import org.jeecg.modules.business.domain.shippingInvoice.CompleteInvoice;
import org.jeecg.modules.business.domain.shippingInvoice.ShippingInvoice;
import org.jeecg.modules.business.domain.shippingInvoice.ShippingInvoiceFactory;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.entity.ClientCategory.CategoryName;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.vo.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class PlatformOrderShippingInvoiceService {

    @Autowired
    ICurrencyService currencyService;
    @Autowired
    ShippingInvoiceMapper shippingInvoiceMapper;
    @Autowired
    PlatformOrderMapper platformOrderMapper;
    @Autowired
    IBalanceService balanceService;
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    IClientCategoryService clientCategoryService;
    @Autowired
    EmailService emailService;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    LogisticChannelPriceMapper logisticChannelPriceMapper;
    @Autowired
    LogisticChannelMapper logisticChannelMapper;
    @Autowired
    IPlatformOrderContentService platformOrderContentService;
    @Autowired
    ISkuDeclaredValueService skuDeclaredValueService;
    @Autowired
    FactureDetailMapper factureDetailMapper;
    @Autowired
    IPlatformOrderService platformOrderService;
    @Autowired
    private IShopService shopService;
    @Autowired
    CountryService countryService;
    @Autowired
    IPurchaseOrderService purchaseOrderService;
    @Autowired
    PurchaseOrderContentMapper purchaseOrderContentMapper;
    @Autowired
    SkuPromotionHistoryMapper skuPromotionHistoryMapper;
    @Autowired
    ExchangeRatesMapper exchangeRatesMapper;
    @Autowired
    ISavRefundWithDetailService savRefundWithDetailService;
    @Autowired
    ISavRefundService savRefundService;
    @Autowired
    Environment env;
    @Value("${jeecg.path.shippingTemplatePath_EU}")
    private String SHIPPING_INVOICE_TEMPLATE_EU;

    @Value("${jeecg.path.shippingTemplatePath_US}")
    private String SHIPPING_INVOICE_TEMPLATE_US;

    @Value("${jeecg.path.completeTemplatePath_EU}")
    private String COMPLETE_INVOICE_TEMPLATE_EU;

    @Value("${jeecg.path.completeTemplatePath_US}")
    private String COMPLETE_INVOICE_TEMPLATE_US;

    @Value("${jeecg.path.purchaseTemplatePath}")
    private String PURCHASE_INVOICE_TEMPLATE;

    @Value("${jeecg.path.shippingInvoiceDir}")
    private String INVOICE_DIR;

    @Value("${jeecg.path.purchaseInvoiceDir}")
    private String PURCHASE_INVOICE_DIR;
    @Value("${jeecg.path.purchaseInventoryDir}")
    private String PURCHASE_INVENTORY_DIR;

    @Value("${jeecg.path.shippingInvoiceDetailDir}")
    private String INVOICE_DETAIL_DIR;
    @Value("${jeecg.path.shippingInvoicePdfDir}")
    private String INVOICE_PDF_DIR;
    @Value("${jeecg.path.shippingInvoiceDetailPdfDir}")
    private String INVOICE_DETAIL_PDF_DIR;
    private static final String EXTENSION = ".xlsx";

    private final static String[] DETAILS_TITLES = {
            "Boutique",
            "N° de Mabang",
            "N° de commande",
            "N° de suivi",
            "Date de commande",
            "Date d'expédition",
            "Nom de client",
            "Pays",
            "Code postal",
            "SKU",
            "Nom produits",
            "Quantité",
            "Frais d'achat",
            "Frais de FRET",
            "Frais de livraison",
            "Frais de service",
            "Frais de préparation",
            "Frais de matériel d'emballage",
            "TVA",
            "N° de facture"
    };
    private final static String[] SAV_TITLES = {
            "Boutique",
            "N° de Mabang",
            "N° de commande",
            "Date du remboursement",
            "Montant d'achat remboursé",
            "Montant de livraison remboursé",
            "Montant total du remboursement",
            "N° de facture"
    };
    private final static String[] PURCHASE_INVENTORY_TITLES = {
            "SKU",
            "Nom Anglais",
            "Nom Chinois",
            "Stock disponible",
            "Achat WIA en cours",
            "Cmd en cours",
            "Stock " + (new SimpleDateFormat("dd/MM").format(new Date())),
            "Quantité à acheter",
            "Ventes 7j",
            "Ventes 28j",
            "Ventes 42j",
            "Prix à l'unité",
    };

    public Period getValidPeriod(List<String> shopIDs) {
        Date begin = platformOrderMapper.findEarliestUninvoicedPlatformOrder(shopIDs);
        Date end = platformOrderMapper.findLatestUninvoicedPlatformOrder(shopIDs);
        return new Period(begin, end, "");
    }
    public Period getValidOrderTimePeriod(List<String> shopIDs, List<Integer> erpStatuses) {
        Date begin = platformOrderMapper.findEarliestUninvoicedPlatformOrderTime(shopIDs, erpStatuses);
        if(begin == null)
            return null;
        ZoneId shanghai = ZoneId.of("Asia/Shanghai");
        ZoneId paris = ZoneId.of("Europe/Paris");
        LocalDateTime ldt = LocalDateTime.ofInstant(begin.toInstant(), shanghai);
        Date beginZoned = Date.from(ldt.atZone(paris).toInstant());
        Date end = platformOrderMapper.findLatestUninvoicedPlatformOrderTime(shopIDs, erpStatuses);
        ldt = LocalDateTime.ofInstant(end.toInstant(), shanghai);
        Date endZoned = Date.from(ldt.atZone(paris).toInstant());
        return new Period(beginZoned, endZoned, "");
    }
    public List<String> getShippingOrderIdBetweenDate(List<String> shops, String start, String end, List<String> wareHouses) {
        List<PlatformOrder> orders = platformOrderMapper.fetchUninvoicedShippedOrderIDInShops( start, end, shops, wareHouses);
        return orders.stream().map(PlatformOrder::getId).collect(Collectors.toList());
    }
    /**
     * Make an invoice based on parameters.
     *
     * @param param the parameters to make the invoice
     * @return identifiant name of the invoice, can be used to in {@code getInvoiceBinary}.
     * @throws UserException  exception due to error of user input, message will contain detail
     * @throws ParseException exception because of format of "start" and "end" date does not follow
     *                        pattern: "yyyy-MM-dd"
     * @throws IOException    exception related to invoice file IO.
     */
    @Transactional
    public InvoiceMetaData makeInvoice(ShippingInvoiceParam param, String ... user) throws UserException, ParseException, IOException {
        // Creates factory
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService, emailService, env);
        String username = user.length > 0 ? user[0] : ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
        // Creates invoice by factory
        ShippingInvoice invoice = factory.createInvoice(param.clientID(),
                param.shopIDs(),
                param.start(),
                param.end(),
                param.getErpStatuses(),
                param.getWarehouses(),
                param.getBalance()
        );
        // Chooses invoice template based on client's preference on currency
        return getInvoiceMetaDataAndInsert(username, invoice);
    }

    /**
     * Make a pre-shipping invoice for specified orders
     *
     * @param param the parameters to make the invoice
     * @return name of the invoice, can be used to in {@code getInvoiceBinary}.
     * @throws UserException  exception due to error of user input, message will contain detail
     * @throws ParseException exception because of format of "start" and "end" date does not follow
     *                        pattern: "yyyy-MM-dd"
     * @throws IOException    exception related to invoice file IO.
     */
    @Transactional
    public InvoiceMetaData makeInvoice(ShippingInvoiceOrderParam param) throws UserException, ParseException, IOException {
        // Creates factory
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService, emailService, env);
        String username = ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
        // Creates invoice by factory
        ShippingInvoice invoice = factory.createShippingInvoice(param.clientID(), param.orderIds(), param.getType(), param.getStart(), param.getEnd());
        return getInvoiceMetaDataAndInsert(username, invoice);
    }

    /**
     * Make a complete shipping invoice (purchase + shipping) invoice for specified orders and order statuses
     *
     * @param param the parameters to make the invoice
     * @return name of the invoice, can be used to in {@code getInvoiceBinary}.
     * @throws UserException  exception due to error of user input, message will contain detail
     * @throws ParseException exception because of format of "start" and "end" date does not follow
     *                        pattern: "yyyy-MM-dd"
     * @throws IOException    exception related to invoice file IO.
     */
    @Transactional
    public InvoiceMetaData makeCompleteInvoice(ShippingInvoiceOrderParam param) throws UserException, ParseException, IOException, MessagingException {
        // Creates factory
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService, emailService, env);
        String username = ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
        // Creates invoice by factory
        CompleteInvoice invoice = factory.createCompleteShippingInvoice(username, param.clientID(), null, param.orderIds(), param.getType(), param.getStart(), param.getEnd());
        return getInvoiceMetaDataAndInsert(username, invoice);
    }

    /**
     *  Make a complete post-shipping (purchase + shipping)
     * @param param clientID, shopIPs[], startDate, endDate
     * @param method "post" = postShipping, "pre" = preShipping, "all" = all shipping methods
     * @return name of the invoice, can be used to in {@code getInvoiceBinary}
     * @throws UserException
     * @throws ParseException
     * @throws IOException
     */
    @Transactional
    public InvoiceMetaData makeCompleteInvoicePostShipping(ShippingInvoiceParam param, String method, String ... user) throws UserException, ParseException, IOException, MessagingException {
        // Creates factory
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService, emailService, env);
        String username = user.length > 0 ? user[0] : ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
        List<PlatformOrder> platformOrderList;
        if(method.equals("post")) {
            //On récupère les commandes entre 2 dates d'expédition avec un status 3
            platformOrderList = platformOrderMapper.fetchUninvoicedShippedOrderIDInShops(param.getStart(), param.getEnd(), param.shopIDs(), param.getWarehouses());
        } else {
            // On récupère les commandes entre 2 dates de commandes avec un status (1,2) ou (1,2,3)
            platformOrderList = platformOrderMapper.fetchUninvoicedOrderIDInShopsAndOrderTime(param.getStart(), param.getEnd(), param.shopIDs(), param.getErpStatuses(), param.getWarehouses());
        }
        // on récupère seulement les ID des commandes
        List<String> orderIds = platformOrderList.stream().map(PlatformOrder::getId).collect(Collectors.toList());
        // Creates invoice by factory
        CompleteInvoice invoice = factory.createCompleteShippingInvoice(username, param.clientID(), param.getBalance() ,orderIds, method, param.getStart(), param.getEnd());
        return getInvoiceMetaDataAndInsert(username, invoice);
    }
    @NotNull
    private InvoiceMetaData getInvoiceMetaDataAndInsert(String username, ShippingInvoice invoice) throws IOException {
        InvoiceMetaData invoiceMetaData = getInvoiceMetaData(invoice);
        String currencyId = currencyService.getIdByCode(invoice.client().getCurrency());
        // save to DB
        org.jeecg.modules.business.entity.ShippingInvoice shippingInvoiceEntity = org.jeecg.modules.business.entity.ShippingInvoice.of(
                username,
                invoice.client().getId(),
                invoice.code(),
                invoice.getTotalAmount(),
                invoice.reducedAmount(),
                invoice.paidAmount(),
                currencyId
        );
        shippingInvoiceMapper.insert(shippingInvoiceEntity);
        return invoiceMetaData;
    }
    @NotNull
    private InvoiceMetaData getInvoiceMetaData(ShippingInvoice invoice) throws IOException {
        // Chooses invoice template based on client's preference on currency
        Path src;
        if (invoice instanceof CompleteInvoice) {
            if (invoice.client().getCurrency().equals("USD")) {
                src = Paths.get(COMPLETE_INVOICE_TEMPLATE_US);
            } else {
                src = Paths.get(COMPLETE_INVOICE_TEMPLATE_EU);
            }
        } else {
            if (invoice.client().getCurrency().equals("USD")) {
                src = Paths.get(SHIPPING_INVOICE_TEMPLATE_US);
            } else {
                src = Paths.get(SHIPPING_INVOICE_TEMPLATE_EU);
            }
        }

        // Writes invoice content to a new excel file
        String filename = "Invoice N°" + invoice.code() + " (" + invoice.client().getInvoiceEntity() + ").xlsx";
        Path out = Paths.get(INVOICE_DIR, filename);
        Files.copy(src, out, StandardCopyOption.REPLACE_EXISTING);
        invoice.toExcelFile(out);

        return new InvoiceMetaData(filename, invoice.code(), invoice.client().getInternalCode(), invoice.client().getInvoiceEntity(), "");
    }
    @NotNull
    private InvoiceMetaData getInvoiceMetaData(PurchaseInvoice invoice) throws IOException {
        // Chooses invoice template based on client's preference on currency
        Path template = Paths.get(PURCHASE_INVOICE_TEMPLATE );

        // Writes invoice content to a new excel file
        String filename = "Invoice N°" + invoice.code() + " (" + invoice.client().getInvoiceEntity() + ").xlsx";
        Path out = Paths.get(PURCHASE_INVOICE_DIR, filename);
        Files.copy(template, out, StandardCopyOption.REPLACE_EXISTING);
        invoice.toExcelFile(out);

        return new InvoiceMetaData(filename, invoice.code(), invoice.client().getInternalCode(), invoice.client().getInvoiceEntity(), "");
    }

    /**
     * Get an estimation of all shipped orders
     *
     * @return List of shipping fees estimations.
     * @param errorMessages List of error messages to be filled
     */
    public List<ShippingFeesEstimation> getShippingFeesEstimation(List<String> errorMessages) {
        // Creates factory
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService, emailService, env);
        return factory.getEstimations(errorMessages);
    }

    /**
     * Get an estimation of selected orders that are yet to be shipped
     *
     * @return List of shipping fees estimations.
     * @param errorMessages List of error messages to be filled
     */
    public List<ShippingFeesEstimation> getShippingFeesEstimation(String clientId, List<String> orderIds,List<String> errorMessages) {
        // Creates factory
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService, emailService, env);
        return factory.getEstimations(clientId, orderIds, errorMessages);
    }

    /**
     * Returns byte stream of a invoice file
     *
     * @param filename identifiant name of the invoice file
     * @return byte array of the file
     * @throws IOException error when reading file
     */
    public byte[] getInvoiceBinary(String filename, String type) throws IOException {
        String path = type.equals("shipping") ? INVOICE_DIR : PURCHASE_INVOICE_DIR;
        Path out = Paths.get(path, filename);
        return Files.readAllBytes(out);
    }

    public List<FactureDetail> getInvoiceDetail(String invoiceNumber) {

        QueryWrapper<FactureDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`N° de facture`", invoiceNumber);

        return factureDetailMapper.selectList(queryWrapper);
    }

    public byte[] exportToExcel(List<FactureDetail> details, List<SavRefundWithDetail> refunds, String invoiceNumber, String invoiceEntity, String internalCode) throws IOException {
        SheetManager sheetManager = SheetManager.createXLSX();
        sheetManager.startDetailsSheet();
        for (String title : DETAILS_TITLES) {
            sheetManager.write(title);
            sheetManager.nextCol();
        }
        sheetManager.moveCol(0);
        sheetManager.nextRow();

        for (FactureDetail detail : details) {
            sheetManager.write(detail.getBoutique());
            sheetManager.nextCol();
            sheetManager.write(detail.getMabangNum());
            sheetManager.nextCol();
            sheetManager.write(detail.getCommandeNum());
            sheetManager.nextCol();
            sheetManager.write(detail.getSuiviNum());
            sheetManager.nextCol();
            sheetManager.write(detail.getCommandeDate());
            sheetManager.nextCol();
            sheetManager.write(detail.getExpeditionDate());
            sheetManager.nextCol();
            sheetManager.write(detail.getClientName());
            sheetManager.nextCol();
            sheetManager.write(detail.getCountry());
            sheetManager.nextCol();
            sheetManager.write(detail.getPostalCode());
            sheetManager.nextCol();
            sheetManager.write(detail.getSku());
            sheetManager.nextCol();
            sheetManager.write(detail.getProductName());
            sheetManager.nextCol();
            sheetManager.write(detail.getQuantity());
            sheetManager.nextCol();
            sheetManager.write(detail.getPurchaseFee());
            sheetManager.nextCol();
            sheetManager.write(detail.getFretFee());
            sheetManager.nextCol();
            sheetManager.write(detail.getLivraisonFee());
            sheetManager.nextCol();
            sheetManager.write(detail.getServiceFee());
            sheetManager.nextCol();
            sheetManager.write(detail.getPickingFee());
            sheetManager.nextCol();
            sheetManager.write(detail.getPackagingMaterialFee());
            sheetManager.nextCol();
            sheetManager.write(detail.getTVA());
            sheetManager.nextCol();
            sheetManager.write(detail.getFactureNum());
            sheetManager.moveCol(0);
            sheetManager.nextRow();
        }
        sheetManager.startSavSheet();
        for (String title : SAV_TITLES) {
            sheetManager.write(title);
            sheetManager.nextCol();
        }
        sheetManager.moveCol(0);
        sheetManager.nextRow();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (SavRefundWithDetail refund : refunds) {
            sheetManager.write(refund.getShopName());
            sheetManager.nextCol();
            sheetManager.write(refund.getMabangId());
            sheetManager.nextCol();
            sheetManager.write(refund.getPlatformOrderNumber());
            sheetManager.nextCol();
            sheetManager.write(sdf.format(refund.getRefundDate()));
            sheetManager.nextCol();
            sheetManager.write(refund.getPurchaseRefundAmount());
            sheetManager.nextCol();
            sheetManager.write(refund.getShippingFee()
                    .add(refund.getFretFee())
                    .add(refund.getVat())
                    .add(refund.getServiceFee()));
            sheetManager.nextCol();
            sheetManager.write(refund.getTotalRefundAmount());
            sheetManager.nextCol();
            sheetManager.write(refund.getInvoiceNumber());
            sheetManager.moveCol(0);
            sheetManager.nextRow();
        }

        Path target = Paths.get(INVOICE_DETAIL_DIR, internalCode + "_(" + invoiceEntity + ")_" + invoiceNumber + "_Détail_calcul_de_facture.xlsx");
        int i = 2;
        while (Files.exists(target)) {
            target = Paths.get(INVOICE_DETAIL_DIR, internalCode + "_(" + invoiceEntity + ")_" + invoiceNumber + "_Détail_calcul_de_facture_(" + i + ").xlsx");
            i++;
        }
        Files.createFile(target);
        sheetManager.export(target);
        sheetManager.getWorkbook().close();
        System.gc();
        return Files.readAllBytes(target);
    }

    public byte[] exportPurchaseInventoryToExcel(List<SkuOrderPage> skuOrders, InvoiceMetaData metaData) throws IOException {
        SheetManager sheetManager = SheetManager.createXLSX();
        sheetManager.startDetailsSheet();
        for (String title : PURCHASE_INVENTORY_TITLES) {
            sheetManager.write(title);
            sheetManager.nextCol();
        }
        sheetManager.moveCol(0);
        sheetManager.nextRow();

        for (SkuOrderPage skuPurchase : skuOrders) {
            sheetManager.write(skuPurchase.getErpCode());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getProductEn());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getProduct());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getAvailableAmount());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getPurchasingAmount());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getQtyInOrdersNotShipped());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getStock());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getQtyOrdered());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getSalesLastWeek());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getSalesFourWeeks());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getSalesSixWeeks());
            sheetManager.nextCol();
            sheetManager.write(skuPurchase.getSkuPrice());
            sheetManager.moveCol(0);
            sheetManager.nextRow();
        }

        Path target = Paths.get(PURCHASE_INVENTORY_DIR, metaData.getInternalCode() + "_(" + metaData.getInvoiceEntity() + ")_" + metaData.getInvoiceCode() + "_Inventaire_SKU.xlsx");
        int i = 2;
        while (Files.exists(target)) {
            target = Paths.get(PURCHASE_INVENTORY_DIR, metaData.getInternalCode() + "_(" + metaData.getInvoiceEntity() + ")_" + metaData.getInvoiceCode() + "_Inventaire_SKU_(" + i + ").xlsx");
            i++;
        }
        Files.createFile(target);
        sheetManager.export(target);
        sheetManager.getWorkbook().close();
        System.gc();
        return Files.readAllBytes(target);
    }

    /**
     * make shipping invoice by client and type (shipping or complete)
     * @param clientIds list of client codes
     * @param invoiceType shipping invoice or complete invoice
     * @return list of filename (invoices and details)
     */
    @Transactional
    public List<InvoiceMetaData> breakdownInvoiceClientByType(List<String> clientIds, int invoiceType) {
        Map<String, List<String>> clientShopIDsMap = new HashMap<>();
        List<InvoiceMetaData> invoiceList = new ArrayList<>();
        for(String id: clientIds) {
            clientShopIDsMap.put(id, shopService.listIdByClient(id));
        }
        for(Map.Entry<String, List<String>> entry: clientShopIDsMap.entrySet()) {
            Client client = clientMapper.selectById(entry.getKey());
            Period period = getValidPeriod(entry.getValue());
            if(!period.isValid()) {
                String internalCode = client.getInternalCode();
                invoiceList.add(new InvoiceMetaData("", "error", internalCode, entry.getKey(), "No order to invoice."));
                continue;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(period.start());
            String start = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1 < 10 ? "0" : "") + (calendar.get(Calendar.MONTH)+1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + (calendar.get(Calendar.DAY_OF_MONTH));
            calendar.setTime(period.end());
            String end = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1 < 10 ? "0" : "") + (calendar.get(Calendar.MONTH)+1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH)+1 < 10 ? "0" : "") + (calendar.get(Calendar.DAY_OF_MONTH)+1);
            log.info( "Invoicing : " + (invoiceType == 0 ? "Shipping Invoice" : "Complete Shipping Invoice") +
                    "\nclient : " + entry.getKey() +
                    "\nbetween dates : [" + start + "] --- [" + end + "]");
            try {
                ShippingInvoiceParam param = new ShippingInvoiceParam(entry.getKey(), null, entry.getValue(), start, end, Collections.singletonList(3), Arrays.asList("0", "1"));
                InvoiceMetaData metaData;
                if(invoiceType == 0) {
                    metaData = makeInvoice(param);
                    if(client.getClientCategoryId().equals(clientCategoryService.getIdByCode(CategoryName.VIP.getName()))
                        || client.getClientCategoryId().equals(clientCategoryService.getIdByCode(CategoryName.CONFIRMED.getName())))
                        balanceService.updateBalance(entry.getKey(), metaData.getInvoiceCode(), "shipping");
                }
                else {
                    metaData = makeCompleteInvoicePostShipping(param, "post");
                    if(client.getClientCategoryId().equals(clientCategoryService.getIdByCode(CategoryName.VIP.getName()))
                            || client.getClientCategoryId().equals(clientCategoryService.getIdByCode(CategoryName.CONFIRMED.getName())))
                        balanceService.updateBalance(entry.getKey(), metaData.getInvoiceCode(), "complete");
                }
                convertToPdf(metaData.getInvoiceCode(), "invoice");
                invoiceList.add(metaData);
            } catch (UserException | IOException | ParseException e) {
                String internalCode = clientMapper.selectById(entry.getKey()).getInternalCode();
                invoiceList.add(new InvoiceMetaData("", "error", internalCode, entry.getKey(), e.getMessage()));
                log.error(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.gc();
        }
        return invoiceList;
    }

    /**
     * make shipping invoice by client and type (shipping or complete) and invoice only if client has sufficient balance
     * @param balanceDataList list of balance data with client info, balance and currency
     * @param invoiceType shipping invoice or complete invoice
     * @return list of filename (invoices and details)
     */
    @Transactional
    public List<InvoiceMetaData> breakdownInvoiceClientByTypeAndBalance(List<BalanceData> balanceDataList, int invoiceType) {
        Map<BalanceData, List<String>> clientShopIDsMap = new HashMap<>();
        List<InvoiceMetaData> invoiceList = new ArrayList<>();
        for(BalanceData data: balanceDataList) {
            String id = data.getClient().getId();
            clientShopIDsMap.put(data, shopService.listIdByClient(id));
        }
        for(Map.Entry<BalanceData, List<String>> entry: clientShopIDsMap.entrySet()) {
            String clientId = entry.getKey().getClient().getId();
            Period period = getValidOrderTimePeriod(entry.getValue(), Collections.singletonList(1));
            if(period == null || !period.isValid()) {
                String internalCode = entry.getKey().getClient().getInternalCode();
                invoiceList.add(new InvoiceMetaData("", "error", internalCode, clientId, "No order to invoice."));
                continue;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(period.start());
            String start = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1 < 10 ? "0" : "") + (calendar.get(Calendar.MONTH)+1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + (calendar.get(Calendar.DAY_OF_MONTH));
            calendar.setTime(period.end());
            String end = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1 < 10 ? "0" : "") + (calendar.get(Calendar.MONTH)+1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH)+1 < 10 ? "0" : "") + (calendar.get(Calendar.DAY_OF_MONTH)+1);
            log.info( "Invoicing : " + (invoiceType == 0 ? "Shipping Invoice" : "Complete Shipping Invoice") +
                    "\nclient : " + entry.getKey() +
                    "\nbetween dates : [" + start + "] --- [" + end + "]");
            try {
                ShippingInvoiceParam param = new ShippingInvoiceParam(clientId, entry.getKey().getBalance(), entry.getValue(), start, end, Collections.singletonList(1), Arrays.asList("0", "1"));
                InvoiceMetaData metaData;
                if(invoiceType == 0) {
                    metaData = makeInvoice(param, "system");
                    balanceService.updateBalance(clientId, metaData.getInvoiceCode(), "shipping");
                }
                else {
                    metaData = makeCompleteInvoicePostShipping(param, "pre-shipping", "system");
                    balanceService.updateBalance(clientId, metaData.getInvoiceCode(), "complete");
                }
                platformOrderMapper.updateErpStatusByCode(metaData.getInvoiceCode(), 2);
                invoiceList.add(metaData);
            } catch (UserException | IOException | ParseException | MessagingException e) {
                String internalCode = entry.getKey().getClient().getInternalCode();
                invoiceList.add(new InvoiceMetaData("", "error", internalCode, clientId, e.getMessage()));
                log.error(e.getMessage());
            }
            System.gc();
        }
        return invoiceList;
    }


    /** Finds the absolute path of invoice file by recursively walking the directory and it's subdirectories
     *
     * @param dirPath
     * @param invoiceNumber
     * @return List of paths for the file but should only find one result
     */
    public List<Path> getPath(String dirPath, String invoiceNumber) {
        List<Path> pathList = new ArrayList<>();
        //Recursively list all files
        //The walk() method returns a Stream by walking the file tree beginning with a given starting file/directory in a depth-first manner.
        try (Stream<Path> stream = Files.walk(Paths.get(dirPath))) {
            pathList = stream.map(Path::normalize)
                    .filter(Files::isRegularFile) // directories, hidden files and files without extension are not included
                    .filter(path -> path.getFileName().toString().contains(invoiceNumber))
                    .filter(path -> path.getFileName().toString().endsWith(EXTENSION))
                    .collect(Collectors.toList());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    /** Finds the absolute path of invoice file by recursively walking the directory and it's subdirectories
     *
     * @param dirPath
     * @param invoiceNumber
     * @return List of paths for the file but should only find one result
     */
    public List<Path> getPath(String dirPath, String invoiceNumber, String invoiceEntity) {
        List<Path> pathList = new ArrayList<>();
        //Recursively list all files
        //The walk() method returns a Stream by walking the file tree beginning with a given starting file/directory in a depth-first manner.
        try (Stream<Path> stream = Files.walk(Paths.get(dirPath))) {
            pathList = stream.map(Path::normalize)
                    .filter(Files::isRegularFile) // directories, hidden files and files without extension are not included
                    .filter(path -> path.getFileName().toString().contains(invoiceNumber))
                    .filter(path -> path.getFileName().toString().contains(invoiceEntity))
                    .filter(path -> path.getFileName().toString().endsWith(EXTENSION))
                    .collect(Collectors.toList());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    /**
     *  Finds the absolute path of invoice file and return the path
     * @param invoiceNumber
     * @param filetype if it's an invoice or invoice detail
     * @return String returns the path of the invoice file
     */
    public String getInvoiceList(String invoiceNumber, String filetype) throws UserException, IOException {
        log.info("Invoice number : " + invoiceNumber);
        List<Path> pathList = new ArrayList<>();
        if(filetype.equals("invoice")) {
            log.info("File asked is of type invoice");
            if(invoiceNumber.charAt(8) == '1')
                pathList = getPath(PURCHASE_INVOICE_DIR, invoiceNumber);
            else
                pathList = getPath(INVOICE_DIR, invoiceNumber);
        }
        if(filetype.equals("detail")) {
            log.info("File asked is of type invoice detail");
            pathList = getPath(INVOICE_DETAIL_DIR, invoiceNumber);
        }
        if(pathList.isEmpty()) {
            log.error("NO INVOICE FILE FOUND : " + invoiceNumber);
            log.info("Generating a new invoice file ...");
            if(filetype.equals("invoice"))
                pathList = generateInvoiceExcel(invoiceNumber, filetype);
            else {
                return "ERROR";
            }
        }
        for (Path path : pathList) {
            log.info(path.toString());
        }
        return pathList.get(0).toString();
    }
    public String convertToPdf(String invoiceNumber, String fileType) throws Exception {
        String excelFilePath = getInvoiceList(invoiceNumber, fileType);// (C:\PATH\filename.xlsx)

        if(!excelFilePath.equals("ERROR")) {
            String pdfFilePath= INVOICE_PDF_DIR + "/" + invoiceNumber + ".pdf";
            if(fileType.equals("invoice")){
                pdfFilePath = INVOICE_PDF_DIR + "/Invoice N°" + invoiceNumber + ".pdf";
            }
            if(fileType.equals("detail")) {
                pdfFilePath = INVOICE_DETAIL_PDF_DIR + "/Détail_calcul_de_facture_" + invoiceNumber + ".pdf";
            }

            Pattern p = Pattern.compile("^(.*)[\\/\\\\](.*)(\\.[a-z]+)"); //group(1): "C:\PATH" , group(2) : "filename", group(3): ".xlsx"
            Matcher m = p.matcher(excelFilePath);
            if (m.matches()) {
                pdfFilePath = INVOICE_PDF_DIR + "/" + m.group(2) + ".pdf";
            }
            // Créé un classeur pour charger le fichier Excel
            Workbook workbook = new Workbook(excelFilePath);
            // On enregistre le document au format PDF
            workbook.save(pdfFilePath, SaveFormat.PDF);
            return pdfFilePath;
        }
        return "ERROR";
    }
    @Transactional
    public String zipInvoices(List<String> invoiceList) throws IOException {
        log.info("Zipping Invoices ...");
        String username = ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
        String now = new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date());
        String zipFilename = Paths.get(INVOICE_DIR).getParent().toAbsolutePath() + "/breakdownInvoices_" + username + "_" + now +".zip";
        final FileOutputStream fos = new FileOutputStream(zipFilename);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (String srcFile : invoiceList) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();
        log.info("Zipping done ...");
        return zipFilename;
    }

    public List<Path> generateInvoiceExcel(String invoiceNumber, String filetype) throws UserException, IOException {
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService, emailService, env);
        Path out = null;
        if(filetype.equals("invoice")) {
            if(Invoice.getType(invoiceNumber).equalsIgnoreCase(Invoice.InvoiceType.PURCHASE.name())) {
                PurchaseInvoice invoice = factory.buildExistingPurchaseInvoice(invoiceNumber);
                InvoiceMetaData invoiceMetaData = getInvoiceMetaData(invoice);
                String filename = "Invoice N°" + invoice.code() + " (" + invoice.client().getInvoiceEntity() + ").xlsx";
                out = Paths.get(PURCHASE_INVOICE_DIR, filename);
            }
            if(Invoice.getType(invoiceNumber).equalsIgnoreCase(Invoice.InvoiceType.SHIPPING.name())) {
                Client client = shippingInvoiceMapper.getClientByInvoiceNumber(invoiceNumber);
                Map<String, String> period = platformOrderService.fetchShippingPeriodAndType(invoiceNumber);
                String clientId = client.getId();

                ShippingInvoice invoice = factory.buildExistingShippingInvoice(invoiceNumber, clientId, period.get("startDate"), period.get("endDate"), filetype, period.get("type"));
                InvoiceMetaData invoiceMetaData = getInvoiceMetaData(invoice);
                String filename = "Invoice N°" + invoice.code() + " (" + invoice.client().getInvoiceEntity() + ").xlsx";
                out = Paths.get(INVOICE_DIR, filename);
            }
            if(Invoice.getType(invoiceNumber).equalsIgnoreCase(Invoice.InvoiceType.COMPLETE.name())) {
                Client client = shippingInvoiceMapper.getClientByInvoiceNumber(invoiceNumber);
                Map<String, String> period = platformOrderService.fetchShippingPeriodAndType(invoiceNumber);
                String clientId = client.getId();
                CompleteInvoice invoice = factory.buildExistingCompleteInvoice(invoiceNumber, clientId, period.get("startDate"), period.get("endDate"), filetype, period.get("type"));
                InvoiceMetaData invoiceMetaData = getInvoiceMetaData(invoice);
                String filename = "Invoice N°" + invoice.code() + " (" + invoice.client().getInvoiceEntity() + ").xlsx";
                out = Paths.get(INVOICE_DIR, filename);
            }
        }
        return Collections.singletonList(out);
    }
}
