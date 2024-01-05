package org.jeecg.modules.business.service.impl.purchase;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.domain.codeGeneration.PurchaseInvoiceCodeRule;
import org.jeecg.modules.business.domain.purchase.invoice.PurchaseInvoice;
import org.jeecg.modules.business.domain.purchase.invoice.PurchaseInvoiceEntry;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.*;
import org.jeecg.modules.business.vo.clientPlatformOrder.section.OrdersStatisticData;
import org.jeecg.modules.message.handle.enums.SendMsgTypeEnum;
import org.jeecg.modules.message.util.PushMsgUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 商品采购订单
 * @Author: jeecg-boot
 * @Date: 2021-04-03
 * @Version: V1.0
 */
@Slf4j
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements IPurchaseOrderService {
    private final PurchaseOrderMapper purchaseOrderMapper;

    private final PurchaseOrderContentMapper purchaseOrderContentMapper;

    private final SkuPromotionHistoryMapper skuPromotionHistoryMapper;


    private final ExchangeRatesMapper exchangeRatesMapper;
    private final IClientService clientService;
    private final IPlatformOrderService platformOrderService;
    private final PlatformOrderMapper platformOrderMapper;
    private final IPlatformOrderContentService platformOrderContentService;
    private final IShippingInvoiceService shippingInvoiceService;
    private final ISkuService skuService;
    private final ICurrencyService currencyService;

    /**
     * Directory where payment documents are put
     */
    @Value("${jeecg.path.save}")
    private String PAYMENT_DOC_DIR;

    @Value("${jeecg.path.purchaseTemplatePath}")
    private String INVOICE_TEMPLATE;

    @Value("${jeecg.path.purchaseInvoiceDir}")
    private String INVOICE_DIR;
    @Value("${jeecg.path.shippingInvoiceDetailDir}")
    private String INVOICE_DETAIL_DIR;
    @Value("${jeecg.path.shippingInvoicePdfDir}")
    private String INVOICE_PDF_DIR;
    @Value("${jeecg.path.shippingInvoiceDetailPdfDir}")
    private String INVOICE_DETAIL_PDF_DIR;

    @Autowired
    private PushMsgUtil pushMsgUtil;

    public PurchaseOrderServiceImpl(PurchaseOrderMapper purchaseOrderMapper,
                                    PurchaseOrderContentMapper purchaseOrderContentMapper,
                                    SkuPromotionHistoryMapper skuPromotionHistoryMapper, IClientService clientService,
                                    IPlatformOrderService platformOrderService, PlatformOrderMapper platformOrderMapper,
                                    IPlatformOrderContentService platformOrderContentService, ISkuService skuService,
                                    ExchangeRatesMapper exchangeRatesMapper, IShippingInvoiceService shippingInvoiceService, ICurrencyService currencyService) {
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.purchaseOrderContentMapper = purchaseOrderContentMapper;
        this.skuPromotionHistoryMapper = skuPromotionHistoryMapper;
        this.clientService = clientService;
        this.platformOrderService = platformOrderService;
        this.platformOrderMapper = platformOrderMapper;
        this.platformOrderContentService = platformOrderContentService;
        this.skuService = skuService;
        this.exchangeRatesMapper = exchangeRatesMapper;
        this.shippingInvoiceService = shippingInvoiceService;
        this.currencyService = currencyService;
    }

    @Override
    @Transactional
    public void saveMain(PurchaseOrder purchaseOrder, List<PurchaseOrderSku> purchaseOrderSkuList, List<SkuPromotionHistory> skuPromotionHistoryList) {
        purchaseOrderMapper.insert(purchaseOrder);
        if (purchaseOrderSkuList != null && purchaseOrderSkuList.size() > 0) {
            for (PurchaseOrderSku entity : purchaseOrderSkuList) {
                //外键设置
                entity.setSkuId(purchaseOrder.getId());
                purchaseOrderContentMapper.insert(entity);
            }
        }
        if (skuPromotionHistoryList != null && skuPromotionHistoryList.size() > 0) {
            for (SkuPromotionHistory entity : skuPromotionHistoryList) {
                //外键设置
                entity.setPromotionId(purchaseOrder.getId());
                skuPromotionHistoryMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(PurchaseOrder purchaseOrder, List<PurchaseOrderSku> purchaseOrderSkuList, List<SkuPromotionHistory> skuPromotionHistoryList) {
        purchaseOrderMapper.updateById(purchaseOrder);

        //1.先删除子表数据
        purchaseOrderContentMapper.deleteByMainId(purchaseOrder.getId());
        skuPromotionHistoryMapper.deleteByMainId(purchaseOrder.getId());

        //2.子表数据重新插入
        if (purchaseOrderSkuList != null && purchaseOrderSkuList.size() > 0) {
            for (PurchaseOrderSku entity : purchaseOrderSkuList) {
                //外键设置
                entity.setSkuId(purchaseOrder.getId());
                purchaseOrderContentMapper.insert(entity);
            }
        }
        if (skuPromotionHistoryList != null && skuPromotionHistoryList.size() > 0) {
            for (SkuPromotionHistory entity : skuPromotionHistoryList) {
                //外键设置
                entity.setPromotionId(purchaseOrder.getId());
                skuPromotionHistoryMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        String invoiceNumber = purchaseOrderMapper.getInvoiceNumber(id);
        purchaseOrderContentMapper.deleteByMainId(id);
        skuPromotionHistoryMapper.deleteByMainId(id);
        purchaseOrderMapper.deleteById(id);
        platformOrderService.removePurchaseInvoiceNumber(invoiceNumber);

    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            String invoiceNumber = purchaseOrderMapper.getInvoiceNumber(id.toString());
            purchaseOrderContentMapper.deleteByMainId(id.toString());
            skuPromotionHistoryMapper.deleteByMainId(id.toString());
            purchaseOrderMapper.deleteById(id);
            platformOrderService.removePurchaseInvoiceNumber(invoiceNumber);
        }
    }

    public void setPageForCurrentClient(IPage<PurchaseOrder> page) {
        String clientID = null;
        Client client = clientService.getCurrentClient();
        if (client != null) {
            clientID = client.getId();
        }
        List<PurchaseOrder> purchaseOrderList = purchaseOrderMapper.pageByClientID(clientID, page.offset(), page.getSize());
        page.setRecords(purchaseOrderList);
        long total = purchaseOrderMapper.countTotal(clientID);
        page.setTotal(total);
    }

    @Override
    @Transactional
    public void cancelInvoice(String purchaseId, String invoiceNumber) {
        String invoiceEntity = clientService.getClientFromPurchase(purchaseId).getInvoiceEntity();
        platformOrderService.removePurchaseInvoiceNumber(invoiceNumber);
        purchaseOrderMapper.deleteInvoice(invoiceNumber);

        List<Path> invoicePathList = shippingInvoiceService.getPath(INVOICE_DIR, invoiceNumber, invoiceEntity);
        List<Path> detailPathList = shippingInvoiceService.getPath(INVOICE_DETAIL_DIR, invoiceNumber, invoiceEntity);
        boolean invoiceDeleted = false, detailDeleted = false;

        if(invoicePathList.isEmpty()) {
            log.error("FILE NOT FOUND : " + invoiceNumber);
        } else {
            for (Path path : invoicePathList) {
                log.info(path.toString());
            }
            try {
                File invoiceFile = new File(invoicePathList.get(0).toString());
                if(invoiceFile.delete()) {
                    log.info("Invoice file {} delete successful.", invoicePathList.get(0).toString());
                    invoiceDeleted = true;
                } else {
                    log.error("Invoice file delete fail.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(detailPathList.isEmpty()) {
            log.error("DETAIL FILE NOT FOUND : " + invoiceNumber);
        } else {
            for (Path path : detailPathList) {
                log.info(path.toString());
            }
            try {
                File detailFile = new File(detailPathList.get(0).toString());
                if(detailFile.delete()) {
                    log.info("Detail file {} delete successful.", detailPathList.get(0).toString());
                    detailDeleted = true;
                } else {
                    log.error("Detail file delete fail.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("Invoice cancel successful." + (invoiceDeleted ? "" : " Failed to delete invoice file.") + (detailDeleted ? "" : " Failed to delete detail file."));
    }

    @Transactional
    @Override
    public void cancelBatchInvoice(String purchaseIds) {
        List<String> purchaseIdList = Arrays.asList(purchaseIds.split(","));
        List<PurchaseOrder> purchaseOrders = purchaseOrderMapper.selectBatchIds(purchaseIdList);
        List<String> invoiceNumbers = purchaseOrders.stream().map(PurchaseOrder::getInvoiceNumber).collect(Collectors.toList());
        log.info("Cancelling invoices : {}", invoiceNumbers);
        platformOrderService.removePurchaseInvoiceNumbers(invoiceNumbers);
        cancelBatchInvoice(invoiceNumbers);
        purchaseOrderContentMapper.deleteFromPurchaseIds(purchaseIdList);

        log.info("Deleting invoice files ...");
        for(PurchaseOrder purchaseOrder : purchaseOrders) {
            if(purchaseOrder.getInvoiceNumber() == null)
                continue;
            String invoiceNumber = purchaseOrder.getInvoiceNumber();
            String invoiceEntity = clientService.getClientEntity(purchaseOrder.getClientId());
            List<Path> invoicePathList = shippingInvoiceService.getPath(INVOICE_DIR, invoiceNumber, invoiceEntity);
            List<Path> detailPathList = shippingInvoiceService.getPath(INVOICE_DETAIL_DIR, invoiceNumber, invoiceEntity);

            if(invoicePathList.isEmpty()) {
                log.error("FILE NOT FOUND : " + invoiceNumber + ", " +  invoiceEntity);
            } else {
                for (Path path : invoicePathList) {
                    log.info(path.toString());
                }
                try {
                    File invoiceFile = new File(invoicePathList.get(0).toString());
                    if(invoiceFile.delete()) {
                        log.info("Invoice file {} delete successful.", invoicePathList.get(0).toString());
                    } else {
                        log.error("Invoice file delete fail.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(detailPathList.isEmpty()) {
                log.error("DETAIL FILE NOT FOUND : " + invoiceNumber + ", " +  invoiceEntity);
            } else {
                for (Path path : detailPathList) {
                    log.info(path.toString());
                }
                try {
                    File detailFile = new File(detailPathList.get(0).toString());
                    if(detailFile.delete()) {
                        log.info("Detail file {} delete successful.", detailPathList.get(0).toString());
                    } else {
                        log.error("Detail file {} delete fail.", detailPathList.get(0).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.gc();
        }
        log.info("End of invoice files deletion.");
    }
    @Transactional
    @Override
    public void confirmPayment(String purchaseID) {
        // update data in DB
        purchaseOrderMapper.confirmPayment(purchaseID);
        // send email to buyer
        Map<String, String> map = new HashMap<>();
        PurchaseOrder purchaseOrder = getById(purchaseID);
        Client client = clientService.getById(purchaseOrder.getClientId());
        map.put("client_name", client.fullName());
        String invoiceNumber = purchaseOrderMapper.getInvoiceNumber(purchaseID);
        map.put("order_number", invoiceNumber);
        pushMsgUtil.sendMessage(
                SendMsgTypeEnum.EMAIL.getType(),
                "purchase_to_process",
                map,
                "service@wia-sourcing.com"
        );
    }

    @Transactional
    @Override
    public void confirmPurchase(String purchaseID) {
        // update data in DB
        purchaseOrderMapper.confirmPurchase(purchaseID);
        // send email to client
        PurchaseOrder purchaseOrder = getById(purchaseID);
        Client client = clientService.getById(purchaseOrder.getClientId());
        String invoiceNumber = purchaseOrderMapper.getInvoiceNumber(purchaseID);
        Map<String, String> map = new HashMap<>();
        map.put("client", client.getFirstName());
        map.put("order_number", invoiceNumber);
        pushMsgUtil.sendMessage(
                SendMsgTypeEnum.EMAIL.getType(),
                "purchase_order_processed",
                map,
                "service@wia-sourcing.com"
        );
    }

    /**
     * Generated a purchase order based on sku quantity, and
     * these quantities are recorded to client's inventory.
     *
     * @param skuQuantities a list of platform orders
     * @return the purchase order's identifier (UUID)
     */
    @Override
    public String addPurchase(List<SkuQuantity> skuQuantities) throws UserException {
        return addPurchase(skuQuantities, Collections.emptyList());
    }


    /**
     * Generated a purchase order based on sku quantity, these sku are bought
     * for some platform orders, their quantity may higher than those in platform
     * orders. In case of higher, extra quantity are recorded to client's inventory.
     *
     * @param skuQuantities    a list of platform orders
     * @param platformOrderIDs identifiers of the platform orders for which the sku are bought.
     * @return the purchase order's identifier (UUID)
     */
    @Override
    @Transactional
    public String addPurchase(List<SkuQuantity> skuQuantities, List<String> platformOrderIDs) throws UserException {
        Objects.requireNonNull(platformOrderIDs);

        Client client = clientService.getCurrentClient();
        if(client == null) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if(sysUser.getUsername().equals("admin")) {
                String poId = platformOrderIDs.get(0);
                client = clientService.getClientFromOrder(poId);
            }
            else
                throw new UserException("User is not a client");
        }
        String currencyId = currencyService.getIdByCode(client.getCurrency());
        List<OrderContentDetail> details = platformOrderService.searchPurchaseOrderDetail(skuQuantities);
        OrdersStatisticData data = OrdersStatisticData.makeData(details, null);

        String purchaseID = UUID.randomUUID().toString();

        String lastInvoiceNumber = purchaseOrderMapper.lastInvoiceNumber();
        String invoiceNumber = new PurchaseInvoiceCodeRule().next(lastInvoiceNumber);
        // 1. save purchase itself
        purchaseOrderMapper.addPurchase(
                purchaseID,
                client.fullName(),
                currencyId,
                client.getId(),
                data.getEstimatedTotalPrice(),
                data.getReducedAmount(),
                data.finalAmount(),
                invoiceNumber
        );

        // 2. save purchase's content
        List<OrderContentEntry> entries = details.stream()
                .map(
                        d -> (
                                new OrderContentEntry(
                                        d.getQuantity(),
                                        d.totalPrice(),
                                        d.getSkuDetail().getSkuId()
                                )
                        )
                )
                .collect(Collectors.toList());
        purchaseOrderContentMapper.addAll(client.fullName(), purchaseID, entries);

        // 2.1 save extra sku
        skuService.addInventory(skuQuantities, platformOrderIDs);

        // 3. save the application of promotion information
        List<PromotionHistoryEntry> promotionHistoryEntries = details.stream()
                .filter(orderContentDetail -> orderContentDetail.getSkuDetail().getPromotion() != Promotion.ZERO_PROMOTION)
                .map(orderContentDetail -> {
                    String promotion = orderContentDetail.getSkuDetail().getPromotion().getId();
                    int count = orderContentDetail.promotionCount();
                    return new PromotionHistoryEntry(promotion, count);
                }).collect(Collectors.toList());
        if (!promotionHistoryEntries.isEmpty()) {
            skuPromotionHistoryMapper.addAll(client.fullName(), promotionHistoryEntries, purchaseID);
        }

        // TODO use real client address
        // send email to client
        Map<String, String> map = new HashMap<>();
        map.put("client", client.getFirstName());
        map.put("order_number", invoiceNumber);
        pushMsgUtil.sendMessage(
                SendMsgTypeEnum.EMAIL.getType(),
                "purchase_order_confirmation",
                map,
                "service@wia-sourcing.com"
        );

        // 5. update platform order status to "purchasing" (optional)
        if (!platformOrderIDs.isEmpty()) {
            platformOrderMapper.batchUpdateStatus(platformOrderIDs, PlatformOrder.Status.Purchasing.code);
        }

        // 4. return purchase id
        return purchaseID;
    }


    /**
     * Generated a purchase order based on sku quantity, these sku are bought
     * for some platform orders, their quantity may higher than those in platform
     * orders. In case of higher, extra quantity are recorded to client's inventory.
     *
     * @param username current user name
     * @param client client
     * @param invoiceNumber invoice number
     * @param skuQuantities list of platform orders
     * @param orderAndContent PlatformOrders and their contents
     * @return the purchase order's identifier (UUID)
     */
    @Override
    @Transactional
    public String addPurchase(String username, Client client, String invoiceNumber, List<SkuQuantity> skuQuantities,
                              Map<PlatformOrder, List<PlatformOrderContent>> orderAndContent) throws UserException {
        Objects.requireNonNull(orderAndContent);

        List<OrderContentDetail> details = platformOrderService.searchPurchaseOrderDetail(skuQuantities);
        OrdersStatisticData data = OrdersStatisticData.makeData(details, null);

        String purchaseID = UUID.randomUUID().toString();
        String currencyId = currencyService.getIdByCode(client.getCurrency());
        // 1. save purchase itself
        purchaseOrderMapper.addPurchase(
                purchaseID,
                username,
                currencyId,
                client.getId(),
                data.getEstimatedTotalPrice(),
                data.getReducedAmount(),
                data.finalAmount(),
                invoiceNumber
        );

        // 2. save purchase's content
        List<OrderContentEntry> entries = details.stream()
                .map(
                        d -> (
                                new OrderContentEntry(
                                        d.getQuantity(),
                                        d.totalPrice(),
                                        d.getSkuDetail().getSkuId()
                                )
                        )
                )
                .collect(Collectors.toList());
        purchaseOrderContentMapper.addAll(username, purchaseID, entries);

        // 2.1 save extra sku
        skuService.addInventory(skuQuantities, orderAndContent.keySet().stream().map(PlatformOrder::getId).collect(Collectors.toList()));

        // 3. save the application of promotion information
        List<PromotionHistoryEntry> promotionHistoryEntries = details.stream()
                .filter(orderContentDetail -> orderContentDetail.getSkuDetail().getPromotion() != Promotion.ZERO_PROMOTION)
                .map(orderContentDetail -> {
                    String promotion = orderContentDetail.getSkuDetail().getPromotion().getId();
                    int count = orderContentDetail.promotionCount();
                    return new PromotionHistoryEntry(promotion, count);
                }).collect(Collectors.toList());
        if (!promotionHistoryEntries.isEmpty()) {
            skuPromotionHistoryMapper.addAll(client.fullName(), promotionHistoryEntries, purchaseID);
        }

        // 4. update platform orders and contents for prices and statuses
        Map<String, Double> skuAvgPrices = new HashMap<>();
        for (OrderContentEntry entry : entries) {
            skuAvgPrices.put(entry.getSkuID(), entry.getTotalAmount().doubleValue() / entry.getQuantity());
        }
        for (Map.Entry<PlatformOrder, List<PlatformOrderContent>> entry : orderAndContent.entrySet()) {
            PlatformOrder platformOrder = entry.getKey();
            List<PlatformOrderContent> orderContents = entry.getValue();
            platformOrder.setStatus(PlatformOrder.Status.Purchasing.code);
            for (PlatformOrderContent orderContent : orderContents) {
                orderContent.setStatus(PlatformOrder.Status.Purchasing.code);
                orderContent.setPurchaseFee(BigDecimal.valueOf(skuAvgPrices.get(orderContent.getSkuId()) * orderContent.getQuantity()));
            }
        }

        // TODO use real client address
        // 5. send email to client
        Map<String, String> map = new HashMap<>();
        map.put("client", client.getFirstName());
        map.put("order_number", invoiceNumber);
        pushMsgUtil.sendMessage(
                SendMsgTypeEnum.EMAIL.getType(),
                "purchase_order_confirmation",
                map,
                "service@wia-sourcing.com"
        );
        return purchaseID;
    }

    /**
     * Save a payment file for a purchase order in the folder indicated by constant
     * {@code PAYMENT_DOC_DIR},
     * in case that the target purchase order already has a payment file, the previous file
     * will be replaced.
     * Only the basename of the file will be saved to DB, not full path name.
     *
     * @param purchaseID the purchase's identifier
     * @param in         the payment file
     * @throws IOException if an I/O error occurs when deleting previous
     *                     file, saving current file
     */
    @Transactional
    @Override
    public void savePaymentDocumentForPurchase(String purchaseID, @NotNull MultipartFile in) throws IOException {
        // save file
        String filename = purchaseID + "_" + in.getOriginalFilename();
        Path target = Paths.get(PAYMENT_DOC_DIR, filename);
        Files.deleteIfExists(target);
        Files.copy(in.getInputStream(), target);
        purchaseOrderMapper.updatePaymentDocument(purchaseID, filename);

        // send email to accountant
        Client client = clientService.getCurrentClient();
        Map<String, String> map = new HashMap<>();
        map.put("client_name", client.fullName());
        String invoiceNumber = purchaseOrderMapper.getInvoiceNumber(purchaseID);
        map.put("order_number", invoiceNumber);
        map.put("accountant", "the real account name");
        pushMsgUtil.sendMessage(
                SendMsgTypeEnum.EMAIL.getType(),
                "payment_proof_upload",
                map,
                "service@wia-sourcing.com"
        );
    }

    /**
     * Download the payment file of the purchase order by its basename
     *
     * @param filename the basename of the file
     * @return byte array of the file
     * @throws IOException IO error while reading the file.
     */
    @Override
    public byte[] downloadPaymentDocumentOfPurchase(String filename) throws IOException {
        Path target = new File(PAYMENT_DOC_DIR, filename).toPath();
        return Files.readAllBytes(target);
    }

    @Override
    public InvoiceMetaData makeInvoice(String purchaseID) throws IOException, URISyntaxException {
        Client client = clientService.getCurrentClient();
        if(client == null) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            if (sysUser.getUsername().equals("admin")) {
                client = clientService.getClientFromPurchase(purchaseID);
            }
        }
        List<PurchaseInvoiceEntry> purchaseOrderSkuList = purchaseOrderContentMapper.selectInvoiceDataByID(purchaseID);
        List<PromotionDetail> promotionDetails = skuPromotionHistoryMapper.selectPromotionByPurchase(purchaseID);
        String invoiceCode = purchaseOrderMapper.selectById(purchaseID).getInvoiceNumber();
        BigDecimal eurToUsd = exchangeRatesMapper.getLatestExchangeRate("EUR", "USD");

        String filename = "Invoice N°" + invoiceCode + " (" + client.getInvoiceEntity() + ").xlsx";
        Path template = Paths.get(INVOICE_TEMPLATE);
        Path newInvoice = Paths.get(INVOICE_DIR, filename);
        if (Files.notExists(newInvoice)) {
            Files.copy(template, newInvoice);
            PurchaseInvoice pv = new PurchaseInvoice(client, invoiceCode, "Purchase Invoice", purchaseOrderSkuList, promotionDetails, eurToUsd);
            pv.toExcelFile(newInvoice);
            return new InvoiceMetaData(filename,invoiceCode, pv.client().getInternalCode(), pv.client().getInvoiceEntity(), "");
        }
        return new InvoiceMetaData(filename, invoiceCode, client.getInternalCode(), client.getInvoiceEntity(), "");
    }

    @Override
    public byte[] getInvoiceByte(String invoiceCode) throws IOException {
        Path invoice = Paths.get(INVOICE_DIR, invoiceCode + ".xlsx");
        return Files.readAllBytes(invoice);
    }

    @Override
    public BigDecimal getPurchaseFeesByInvoiceCode(String invoiceCode) {
        return purchaseOrderMapper.getPurchaseFeesByInvoiceCode(invoiceCode);
    }

    @Override
    public void cancelInvoice(String invoiceNumber) {
        purchaseOrderMapper.deleteInvoice(invoiceNumber);
    }

    @Override
    public void cancelBatchInvoice(List<String> invoiceNumbers) {
        purchaseOrderMapper.deleteBatchInvoice(invoiceNumbers);
    }

    @Override
    public String getInvoiceId(String invoiceNumber) {
        return purchaseOrderMapper.getInvoiceId(invoiceNumber);
    }

    @Override
    public List<PurchaseOrder> getPurchasesByInvoiceNumber(String invoiceNumber) {
        return purchaseOrderMapper.getPurchasesByInvoiceNumber(invoiceNumber);
    }

    @Override
    public List<PlatformOrder> getPlatformOrder(String invoiceNumber) {
        return purchaseOrderMapper.getPlatformOrder(invoiceNumber);
    }
}