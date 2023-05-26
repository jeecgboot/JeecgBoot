package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.models.auth.In;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.domain.excel.SheetManager;
import org.jeecg.modules.business.domain.shippingInvoice.CompleteInvoice;
import org.jeecg.modules.business.domain.shippingInvoice.ShippingInvoice;
import org.jeecg.modules.business.domain.shippingInvoice.ShippingInvoiceFactory;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.ShippingInvoiceEntity;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.vo.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlatformOrderShippingInvoiceService {

    @Autowired
    ShippingInvoiceMapper shippingInvoiceMapper;
    @Autowired
    PlatformOrderMapper platformOrderMapper;
    @Autowired
    ClientMapper clientMapper;
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

    @Value("${jeecg.path.shippingTemplatePath_EU}")
    private String SHIPPING_INVOICE_TEMPLATE_EU;

    @Value("${jeecg.path.shippingTemplatePath_US}")
    private String SHIPPING_INVOICE_TEMPLATE_US;

    @Value("${jeecg.path.completeTemplatePath_EU}")
    private String COMPLETE_INVOICE_TEMPLATE_EU;

    @Value("${jeecg.path.completeTemplatePath_US}")
    private String COMPLETE_INVOICE_TEMPLATE_US;

    @Value("${jeecg.path.shippingInvoiceDir}")
    private String INVOICE_DIR;

    @Value("${jeecg.path.shippingInvoiceDetailDir}")
    private String INVOICE_DETAIL_DIR;

    private final static String[] titles = {
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

    public Period getValidPeriod(List<String> shopIDs) {
        Date begin = platformOrderMapper.findEarliestUninvoicedPlatformOrder(shopIDs);
        Date end = platformOrderMapper.findLatestUninvoicedPlatformOrder(shopIDs);
        return new Period(begin, end);
    }
    public Period getValidOrderTimePeriod(List<String> shopIDs, List<Integer> erpStatuses) {
        Date begin = platformOrderMapper.findEarliestUninvoicedPlatformOrderTime(shopIDs, erpStatuses);
        Date end = platformOrderMapper.findLatestUninvoicedPlatformOrderTime(shopIDs, erpStatuses);
        return new Period(begin, end);
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
    public InvoiceMetaData makeInvoice(ShippingInvoiceParam param) throws UserException, ParseException, IOException {
        // Creates factory
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService);
        String username = ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
        // Creates invoice by factory
        ShippingInvoice invoice = factory.createInvoice(param.clientID(),
                param.shopIDs(),
                param.start(),
                param.end(),
                param.getErpStatuses()
        );
        // Chooses invoice template based on client's preference on currency
        return getInvoiceMetaData(username, invoice);
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
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService);
        String username = ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
        // Creates invoice by factory
        ShippingInvoice invoice = factory.createShippingInvoice(param.clientID(), param.orderIds(), param.getType(), param.getStart(), param.getEnd());
        return getInvoiceMetaData(username, invoice);
    }

    /**
     * Make a complete pre-shipping (purchase + shipping) invoice for specified orders
     *
     * @param param the parameters to make the invoice
     * @return name of the invoice, can be used to in {@code getInvoiceBinary}.
     * @throws UserException  exception due to error of user input, message will contain detail
     * @throws ParseException exception because of format of "start" and "end" date does not follow
     *                        pattern: "yyyy-MM-dd"
     * @throws IOException    exception related to invoice file IO.
     */
    @Transactional
    public InvoiceMetaData makeCompleteInvoice(ShippingInvoiceOrderParam param) throws UserException, ParseException, IOException {
        // Creates factory
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService);
        String username = ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
        // Creates invoice by factory
        CompleteInvoice invoice = factory.createCompleteShippingInvoice(username, param.clientID(), param.orderIds(), param.getType(), param.getStart(), param.getEnd());
        return getInvoiceMetaData(username, invoice);
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
    public InvoiceMetaData makeCompleteInvoicePostShipping(ShippingInvoiceParam param, String method) throws UserException, ParseException, IOException {
        // Creates factory
        ShippingInvoiceFactory factory = new ShippingInvoiceFactory(
                platformOrderService, clientMapper, shopMapper, logisticChannelMapper, logisticChannelPriceMapper,
                platformOrderContentService, skuDeclaredValueService, countryService, exchangeRatesMapper,
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService);
        String username = ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getUsername();
        List<PlatformOrder> platformOrderList;
        if(param.getErpStatuses().toString().equals("post")) {
            //On récupère les commandes entre 2 dates d'expédition avec un status 3
            platformOrderList = platformOrderMapper.fetchUninvoicedShippedOrderIDInShops(param.getStart(), param.getEnd(), param.shopIDs());
        } else {
            // On récupère les commandes entre 2 dates de commandes avec un status (1,2) ou (1,2,3)
            platformOrderList = platformOrderMapper.fetchUninvoicedShippedOrderIDInShopsAndOrderTime(param.getStart(), param.getEnd(), param.shopIDs(), param.getErpStatuses());
        }
        // on récupère seulement les ID des commandes
        List<String> orderIds = platformOrderList.stream().map(PlatformOrder::getId).collect(Collectors.toList());
        // Creates invoice by factory
        CompleteInvoice invoice = factory.createCompleteShippingInvoice(username, param.clientID(), orderIds, method, param.getStart(), param.getEnd());
        return getInvoiceMetaData(username, invoice);
    }
    @NotNull
    private InvoiceMetaData getInvoiceMetaData(String username, ShippingInvoice invoice) throws IOException {
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
        // save to DB
        org.jeecg.modules.business.entity.ShippingInvoice shippingInvoiceEntity = org.jeecg.modules.business.entity.ShippingInvoice.of(
                username,
                invoice.code(),
                invoice.getTotalAmount(),
                invoice.reducedAmount(),
                invoice.paidAmount()
        );
        shippingInvoiceMapper.insert(shippingInvoiceEntity);
        return new InvoiceMetaData(filename, invoice.code());
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
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService);
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
                purchaseOrderService, purchaseOrderContentMapper, skuPromotionHistoryMapper, savRefundService, savRefundWithDetailService);
        return factory.getEstimations(clientId, orderIds, errorMessages);
    }

    /**
     * Returns byte stream of a invoice file
     *
     * @param filename identifiant name of the invoice file
     * @return byte array of the file
     * @throws IOException error when reading file
     */
    public byte[] getInvoiceBinary(String filename) throws IOException {
        Path out = Paths.get(INVOICE_DIR, filename);
        return Files.readAllBytes(out);
    }

    public List<FactureDetail> getInvoiceDetail(String invoiceNumber) {

        QueryWrapper<FactureDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`N° de facture`", invoiceNumber);

        return factureDetailMapper.selectList(queryWrapper);
    }

    public byte[] exportToExcel(List<FactureDetail> details, String invoiceNumber) throws IOException {
        SheetManager sheetManager = SheetManager.createXLSX();
        for (String title : titles) {
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

        Path target = Paths.get(INVOICE_DETAIL_DIR, "Détail_calcul_de_facture_" + invoiceNumber + ".xlsx");
        int i = 2;
        while (Files.exists(target)) {
            target = Paths.get(INVOICE_DETAIL_DIR, "Détail_calcul_de_facture_" + invoiceNumber + "_" + i + ".xlsx");
            i++;
        }
        Files.createFile(target);
        sheetManager.export(target);
        sheetManager.getWorkbook().close();
        return Files.readAllBytes(target);
    }
}
