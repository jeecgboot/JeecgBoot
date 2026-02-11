package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.message.TemplateMessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.business.controller.admin.PurchaseOrderController;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.PlatformOrderMapper;
import org.jeecg.modules.business.model.AutoInvoiceFailDetail;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.*;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.jeecg.modules.business.entity.Invoice.InvoicingMethod.PRESHIPPING;

@Slf4j
@Service
public class AutoInvoiceServiceImpl implements IAutoInvoiceService {

    @Autowired private IBalanceService balanceService;
    @Autowired private PlatformOrderShippingInvoiceService platformOrderShippingInvoiceService;
    @Autowired private InvoiceService invoiceService;
    @Autowired private IPurchaseOrderService purchaseOrderService;
    @Autowired private EmailService emailService;
    @Autowired private ISysUserService sysUserService;
    @Autowired private PlatformOrderMapper platformOrderMapper;
    @Autowired private ApplicationContext applicationContext;
    @Autowired private ISysBaseAPI sysBaseAPI;
    @Autowired private IShopOptionsService shopOptionsService;
    @Autowired private IPlatformOrderMabangService platformOrderMabangService;
    public static final String REASON_NO_BALANCE = "余额不足，自动开票未执行";
    public static final String REASON_NO_SHOP = "该客户没有可处理的店铺";
    public static final String REASON_SHOP_EXCEPTION = "店铺开票处理过程中发生异常";


    private static final String TEMPLATE_ACCOUNTANT = "components/autoInvoiceForAccountant.ftl";
    private static final String TEMPLATE_CLIENT = "components/autoInvoiceForClient.ftl";
    private static final List<Integer> PRESHIPPING_ERP_STATUSES = Arrays.asList(1, 2);
    private static final List<String> DEFAULT_WAREHOUSES = Arrays.asList("0", "1");
    private static final String AUTO_OPERATOR = "AUTO_INVOICE_TASK";
    private static final String TAG_ORDER = "ORDER|";
    private static final String TAG_SKU   = "SKU|";
    private static final String TAG_SKUS  = "SKUS|";
    private static final String ANNOUNCEMENT_TEMPLATE_CODE = "auto_invoice_job_result";
    private static final List<String> ANNOUNCEMENT_ROLE_CODES = Arrays.asList(
            "admin",
            "accountant",
            "sales"
    );
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processShop(
            Client client,
            Shop shop,
            SimpleDateFormat sdf,
            List<Map<String, Object>> generatedInvoices,
            List<AutoInvoiceFailDetail> failDetails
    ) throws Exception {
        Period period = platformOrderShippingInvoiceService.getAutoInvoicePeriod(
                Collections.singletonList(shop.getId()),
                PRESHIPPING_ERP_STATUSES
        );
        String start = sdf.format(period.getStart());
        String end = sdf.format(period.getEnd());
        List<String> orderIds = platformOrderMapper.fetchUninvoicedOrderIDInShopsAndOrderTime(
                start,
                end,
                Collections.singletonList(shop.getId()),
                PRESHIPPING_ERP_STATUSES,
                DEFAULT_WAREHOUSES
        );
        if (orderIds == null || orderIds.isEmpty()) {
            recordShopFail(
                    failDetails,
                    client,
                    shop,
                    null,
                    AutoInvoiceFailDetail.Step.CREATE_INVOICE,
                    "No order to invoice.",
                    null
            );
            return;
        }
        List<PlatformOrder> orders = platformOrderMapper.fetchByIds(orderIds);
        log.info("[AUTO_INVOICE][SYNC_PREP] dbOrderIds={}, fetchedOrders={}",
                orderIds.size(), orders == null ? null : orders.size());
        List<String> platformOrderIds = null;
        if (orders != null) {
            platformOrderIds = orders.stream()
                    .map(PlatformOrder::getPlatformOrderId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
        }
        if (platformOrderIds != null && !platformOrderIds.isEmpty()) {
            try {
                platformOrderMabangService.syncOrdersFromMabang(platformOrderIds);
            } catch (Exception e) {
                log.warn("[AUTO_INVOICE][SYNC_FAIL] client={}, shop={}, reason={}",
                        client.getInternalCode(), shop.getName(), e.getMessage(), e);
            }
        }
        ManualInvoiceOrderParam manualParam = new ManualInvoiceOrderParam(
                client.getId(),
                orderIds,
                PRESHIPPING.getMethod(),
                Collections.emptyList()
        );
        try {
            Response<InvoiceMetaData, List<Response<String, String>>> response =
                    platformOrderShippingInvoiceService.makeManualCompleteInvoice(
                            manualParam, AUTO_OPERATOR
                    );
            List<Response<String, String>> invoiceErrors = (response == null) ? null : response.getError();
           // Response<InvoiceMetaData, List<Response<String, String>>> response = platformOrderShippingInvoiceService.makeManualCompleteInvoice(param);
            if (response == null || response.getData() == null) {
                if (invoiceErrors != null && !invoiceErrors.isEmpty()) {
                    ingestInvoiceErrorsToFailDetails(client, shop, null, invoiceErrors, failDetails);
                }
                recordFail(
                        failDetails,
                        client,
                        shop,
                        null,
                        AutoInvoiceFailDetail.Step.CREATE_INVOICE,
                        "创建发票失败，返回结果为空",
                        null
                );
                return;
            }
            InvoiceMetaData data = response.getData();
            if (invoiceErrors != null && !invoiceErrors.isEmpty()) {
                ingestInvoiceErrorsToFailDetails(client, shop, data.getInvoiceCode(), invoiceErrors, failDetails);
            }
            balanceService.updateBalance(client.getId(), data.getInvoiceCode(), "COMPLETE");
            BigDecimal balance = balanceService.getBalanceByClientIdAndCurrency(
                    client.getId(), client.getCurrency()
            );
            tryEditOrdersRemarkAfterInvoice(data.getInvoiceCode(), PRESHIPPING);
            boolean approved = approveInvoiceIfPossible(
                    client, data.getInvoiceCode(), balance
            );
            if (!approved) {
                recordFail(
                        failDetails,
                        client,
                        shop,
                        data.getInvoiceCode(),
                        AutoInvoiceFailDetail.Step.APPROVE_INVOICE,
                        "余额不足，发票未能审批",
                        null
                );
            }
            InvoiceAmountDTO amounts = invoiceService.getInvoiceAmounts(data.getInvoiceCode());
            BigDecimal total = amounts.getPurchaseAmount().add(amounts.getShippingAmount());
            Map<String, Object> invoiceInfo = new HashMap<>();
            invoiceInfo.put("invoiceCode", data.getInvoiceCode());
            invoiceInfo.put("shopName", shop.getName());
            invoiceInfo.put("orderIds", orderIds);
            invoiceInfo.put("amount", total);
            invoiceInfo.put("currency", client.getCurrency());
            invoiceInfo.put("approved", approved);
            generatedInvoices.add(invoiceInfo);
        } catch (Exception e) {
            String reason = REASON_SHOP_EXCEPTION + "；技术信息：" + extractShortError(e);
            recordFail(
                    failDetails,
                    client,
                    shop,
                    null,
                    AutoInvoiceFailDetail.Step.CREATE_INVOICE,
                    reason,
                    e
            );
            throw e;
        }
    }
    @Override
    public boolean approveInvoiceIfPossible(
            Client client,
            String invoiceCode,
            BigDecimal balance
    ) {
        try {
            PurchaseOrderController controller = applicationContext.getBean(PurchaseOrderController.class);
            boolean approved = balance != null && balance.compareTo(BigDecimal.ZERO) >= 0;
            Map<String, Object> payload = new HashMap<>();
            payload.put("invoiceNumber", invoiceCode);
            payload.put("clientId", client.getId());
            payload.put("approved", approved);
            controller.setPaymentApproved(payload);
            if (approved) {
                purchaseOrderService.update(
                        Wrappers.<PurchaseOrder>lambdaUpdate()
                                .set(PurchaseOrder::isOrdered, false)
                                .eq(PurchaseOrder::getInvoiceNumber, invoiceCode)
                );
            }
            return approved;
        } catch (Exception e) {
            log.error("Failed to approve invoice {} for client {}",
                    invoiceCode, client.getInternalCode(), e);
            return false;
        }
    }

    @Override
    public void notifyAutoInvoiceGenerated(Client client, BigDecimal balance, List<Map<String, Object>> invoicesGenerated, List<AutoInvoiceFailDetail> failDetails) {
        try {
            boolean hasSuccess = invoicesGenerated != null && !invoicesGenerated.isEmpty();
            boolean hasFailure = failDetails != null && !failDetails.isEmpty();
            if (!hasSuccess && !hasFailure) {
                log.info("Client {} has no auto-invoice result to notify.", client.getInternalCode());
                return;
            }
            // Notify Accountants
            Map<String, Object> accountantModel = new HashMap<>();
            accountantModel.put("clientCode", client.getInternalCode());
            accountantModel.put("clientName", client.fullName());
            accountantModel.put("currency", client.getCurrency());
            accountantModel.put("invoices", invoicesGenerated);
            accountantModel.put("currentBalance", balance);
            accountantModel.put("failDetails", failDetails);
            accountantModel.put("reviewLink", "https://app.wia-sourcing.com/business/admin/purchasing/PaymentProofReview");
            accountantModel.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            List<SysUser> accountants = sysUserService.getUsersByRoleCode("accountant");
            for (SysUser accountant : accountants) {
                if (StringUtils.isNotBlank(accountant.getEmail())) {
                    emailService.newSendSimpleMessage(
                            accountant.getEmail(),
                            "自动开票任务汇总通知",
                            TEMPLATE_ACCOUNTANT,
                            accountantModel
                    );
                }
            }
            //Notify Client
            if (hasSuccess && StringUtils.isNotBlank(client.getEmail())) {
                Map<String, Object> clientModel = new HashMap<>();
                clientModel.put("clientName", client.fullName());
                clientModel.put("currency", client.getCurrency());
                clientModel.put("invoices", invoicesGenerated);
                clientModel.put("currentBalance", balance);
                clientModel.put("hasDebt", invoicesGenerated.stream().anyMatch(i -> Boolean.FALSE.equals(i.get("approved"))));
                clientModel.put("clientInvoiceLink", "https://app.wia-sourcing.com/business/client/overview/ExpensesOverview");
                emailService.newSendSimpleMessage(
                        client.getEmail(),
                        "New Invoice Notification",
                        TEMPLATE_CLIENT,
                        clientModel
                );
            }
            //Save System Announcement
            Map<String, String> param = new HashMap<>();
            param.put("client_code", client.getInternalCode());
            param.put("client_name", client.fullName());
            param.put("currency", client.getCurrency());
            param.put("current_balance", balance == null ? "N/A" : balance.toPlainString());
            param.put("success_invoices", toHtmlInvoices(invoicesGenerated));
            param.put("fail_details", toHtmlFails(failDetails));
            Set<String> receivers = listAnnouncementReceivers();
            for (String toUser : receivers) {
                TemplateMessageDTO message = new TemplateMessageDTO(
                        "admin",
                        toUser,
                        "自动开票任务结果",
                        param,
                        ANNOUNCEMENT_TEMPLATE_CODE
                );
                sysBaseAPI.sendTemplateAnnouncement(message);
            }
        } catch (Exception e) {
            log.error("Failed to send auto-invoice notification for client {}: {}",
                    client.getInternalCode(), extractShortError(e), e);
        }
    }

    @Override
    public void recordFail(
            List<AutoInvoiceFailDetail> failDetails,
            Client client,
            Shop shop,
            String invoiceCode,
            AutoInvoiceFailDetail.Step step,
            String errorReason,
            Throwable throwable
    ) {
        AutoInvoiceFailDetail fail = new AutoInvoiceFailDetail();
        fail.setClientCode(client.getInternalCode());
        fail.setShopName(shop != null ? shop.getName() : null);
        fail.setInvoiceCode(invoiceCode);
        fail.setLevel(shop == null ? AutoInvoiceFailDetail.Level.CLIENT : AutoInvoiceFailDetail.Level.SHOP);
        fail.setStep(step);
        fail.setErrorReason(errorReason);
        fail.setTime(LocalDateTime.now());
        failDetails.add(fail);
        if (throwable != null) {
            log.error("[AUTO_INVOICE_FAIL] {}", fail, throwable);
        } else {
            log.warn("[AUTO_INVOICE_FAIL] {}", fail);
        }
    }

    @Override
    public String extractShortError(Throwable t) {
        if (t == null) return "unknown";
        Throwable root = t;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        String msg = root.getMessage();
        if (StringUtils.isBlank(msg)) msg = root.toString();
        msg = msg.replaceAll("[\\r\\n\\t]+", " ").trim();
        int max = 300;
        if (msg.length() > max) {
            msg = msg.substring(0, max) + "...";
        }
        return msg;
    }
    @Override
    public String toHtmlInvoices(List<Map<String, Object>> invoices) {
        if (invoices == null || invoices.isEmpty()) return "<i>无</i>";
        StringBuilder sb = new StringBuilder("<ul>");
        for (Map<String, Object> inv : invoices) {
            sb.append("<li>")
                    .append("店铺：").append(inv.get("shopName"))
                    .append("，发票：").append(inv.get("invoiceCode"))
                    .append("，金额：").append(inv.get("amount")).append(" ").append(inv.get("currency"))
                    .append("，订单：").append((inv.get("orderIds")))
                    .append("，审核：").append(Boolean.TRUE.equals(inv.get("approved")) ? "通过" : "<span style='color:red'>待补款</span>")
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    @Override
    public String toHtmlFails(List<AutoInvoiceFailDetail> fails) {
        if (fails == null || fails.isEmpty()) return "<i>无</i>";
        StringBuilder sb = new StringBuilder("<ul>");
        for (AutoInvoiceFailDetail f : fails) {
            sb.append("<li>");
            String shop = (f.getShopName() == null) ? "（客户级）" : f.getShopName();
            sb.append("店铺：").append(shop);
            if (f.getLevel() != null) sb.append("，级别：").append(f.getLevel());
            if (f.getStep() != null) sb.append("，阶段：").append(f.getStep());
            if (f.getPlatformOrderId() != null) sb.append("，订单：").append(f.getPlatformOrderId());
            if (f.getSku() != null) sb.append("，SKU：").append(f.getSku());
            sb.append("，原因：").append(f.getErrorReason());
            sb.append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
    @Override
    public Set<String> listAnnouncementReceivers() {
        Set<String> usernames = new HashSet<>();
        for (String roleCode : ANNOUNCEMENT_ROLE_CODES) {
            List<SysUser> users = sysUserService.getUsersByRoleCode(roleCode);
            if (users == null) continue;
            for (SysUser u : users) {
                if (u == null) continue;
                if (StringUtils.isBlank(u.getUsername())) continue;
                if (u.getDelFlag() != null && u.getDelFlag() == 1) continue;
                if (u.getStatus() != null && u.getStatus() != 1) continue;
                usernames.add(u.getUsername());
            }
        }
        return usernames;
    }
    @Override
    public void tryEditOrdersRemarkAfterInvoice(String invoiceNumber, Invoice.InvoicingMethod invoicingMethod) {
        try {
            boolean hasRemarkInShippingInvoice = false;
            List<ShopOptions> options = shopOptionsService.getByInvoiceNumber(invoiceNumber);
            if (options == null || options.isEmpty()) {
                log.info("[AUTO_INVOICE][REMARK] skip invoice={} (no shop options)", invoiceNumber);
                return;
            }
            for (ShopOptions option : options) {
                if (Boolean.TRUE.equals(option.getHasShippingInvoiceRemark())) {
                    hasRemarkInShippingInvoice = true;
                    break;
                }
            }
            boolean shouldApply =
                    (invoicingMethod == PRESHIPPING)
                            || (invoicingMethod == null && hasRemarkInShippingInvoice);
            if (!shouldApply) {
                log.info("[AUTO_INVOICE][REMARK] skip invoice={} (method not supported / no remark needed)", invoiceNumber);
                return;
            }
            ResponsesWithMsg<String> mabangResponses = platformOrderMabangService.editOrdersRemark(invoiceNumber);
            if (mabangResponses != null && mabangResponses.getFailures() != null && !mabangResponses.getFailures().isEmpty()) {
                log.warn("[AUTO_INVOICE][REMARK] invoice={} remark has failures: {}", invoiceNumber, mabangResponses.getFailures());
            } else {
                log.info("[AUTO_INVOICE][REMARK] invoice={} remark success", invoiceNumber);
            }
        } catch (Exception e) {
            log.warn("[AUTO_INVOICE][REMARK] invoice={} remark failed: {}", invoiceNumber, extractShortError(e), e);
        }
    }
    private void ingestInvoiceErrorsToFailDetails(
            Client client,
            Shop shop,
            String invoiceCode,
            List<Response<String, String>> errors,
            List<AutoInvoiceFailDetail> failDetails
    ) {
        if (errors == null || errors.isEmpty()) return;
        Set<String> seen = new HashSet<>();
        for (Response<String, String> err : errors) {
            if (err == null) continue;
            String platformOrderId = err.getData();
            if (StrUtil.isBlank(platformOrderId)) {
                platformOrderId = "UNKNOWN_ORDER";
                log.warn("[AUTO_INVOICE][PARSE] Missing order id in invoice error, msg={}", err.getError());
            }
            String msg = err.getError();
            if (msg == null) msg = "unknown error";
            // ORDER|reason
            if (msg.startsWith(TAG_ORDER)) {
                String reason = msg.substring(TAG_ORDER.length()).trim();
                String key = "ORDER|"
                        + (shop == null ? "null" : shop.getId()) + "|"
                        + invoiceCode + "|"
                        + platformOrderId + "|"
                        + AutoInvoiceFailDetail.Step.CALCULATE + "|"
                        + reason;
                if (!seen.add(key)) continue;

                recordOrderFail(failDetails, client, shop, invoiceCode,
                        platformOrderId, AutoInvoiceFailDetail.Step.CALCULATE, reason);
                continue;
            }
            // SKU|ERP|reason
            if (msg.startsWith(TAG_SKU)) {
                String rest = msg.substring(TAG_SKU.length());
                String[] parts = rest.split("\\|", 2);
                String skuErp = parts.length > 0 ? parts[0].trim() : null;
                String reason = parts.length > 1 ? parts[1].trim() : "SKU error";

                String key = "SKU|"
                        + (shop == null ? "null" : shop.getId()) + "|"
                        + invoiceCode + "|"
                        + platformOrderId + "|"
                        + skuErp + "|"
                        + AutoInvoiceFailDetail.Step.CALCULATE + "|"
                        + reason;
                if (!seen.add(key)) continue;

                recordSkuFail(failDetails, client, shop, invoiceCode,
                        platformOrderId, skuErp, AutoInvoiceFailDetail.Step.CALCULATE, reason);
                continue;
            }
            // SKUS|ERP1,ERP2|reason
            if (msg.startsWith(TAG_SKUS)) {
                String rest = msg.substring(TAG_SKUS.length());
                String[] parts = rest.split("\\|", 2);
                String skuList = parts.length > 0 ? parts[0].trim() : "";
                String reason = parts.length > 1 ? parts[1].trim() : "SKU error";

                for (String skuErp : splitSkuList(skuList)) {
                    String key = "SKU|"
                            + (shop == null ? "null" : shop.getId()) + "|"
                            + invoiceCode + "|"
                            + platformOrderId + "|"
                            + skuErp + "|"
                            + AutoInvoiceFailDetail.Step.CALCULATE + "|"
                            + reason;
                    if (!seen.add(key)) continue;

                    recordSkuFail(failDetails, client, shop, invoiceCode,
                            platformOrderId, skuErp, AutoInvoiceFailDetail.Step.CALCULATE, reason);
                }
                continue;
            }
            //for all other fallback errors
            String key = "FALLBACK|"
                    + (shop == null ? "null" : shop.getId()) + "|"
                    + invoiceCode + "|"
                    + platformOrderId + "|"
                    + AutoInvoiceFailDetail.Step.CREATE_INVOICE + "|"
                    + msg;
            if (!seen.add(key)) continue;

            recordOrderFail(failDetails, client, shop, invoiceCode,
                    platformOrderId, AutoInvoiceFailDetail.Step.CREATE_INVOICE, msg);
        }
    }

    private List<String> splitSkuList(String skuPart) {
        if (skuPart == null || StrUtil.isBlank(skuPart)) return Collections.emptyList();
        String cleaned = skuPart.replace("[", "").replace("]", "").trim();
        String[] arr = cleaned.split("[,;\\s]+");
        List<String> out = new ArrayList<>();
        for (String s : arr) {
            if (s != null && !StrUtil.isBlank(s)) out.add(s.trim());
        }
        return out;
    }
    private void recordShopFail(
            List<AutoInvoiceFailDetail> failDetails,
            Client client,
            Shop shop,
            String invoiceCode,
            AutoInvoiceFailDetail.Step step,
            String errorReason,
            Throwable throwable
    ) {
        AutoInvoiceFailDetail fail = new AutoInvoiceFailDetail();
        fail.setClientCode(client.getInternalCode());
        fail.setShopName(shop != null ? shop.getName() : null);
        fail.setInvoiceCode(invoiceCode);
        fail.setLevel(shop == null ? AutoInvoiceFailDetail.Level.CLIENT : AutoInvoiceFailDetail.Level.SHOP);
        fail.setStep(step);
        fail.setErrorReason(errorReason);
        fail.setTime(LocalDateTime.now());
        failDetails.add(fail);
        if (throwable != null) log.error("[AUTO_INVOICE_FAIL] {}", fail, throwable);
        else log.warn("[AUTO_INVOICE_FAIL] {}", fail);
    }

    private void recordOrderFail(
            List<AutoInvoiceFailDetail> failDetails,
            Client client,
            Shop shop,
            String invoiceCode,
            String platformOrderId,
            AutoInvoiceFailDetail.Step step,
            String errorReason
    ) {
        AutoInvoiceFailDetail fail = new AutoInvoiceFailDetail();
        fail.setClientCode(client.getInternalCode());
        fail.setShopName(shop != null ? shop.getName() : null);
        fail.setInvoiceCode(invoiceCode);
        fail.setLevel(AutoInvoiceFailDetail.Level.ORDER);
        fail.setPlatformOrderId(platformOrderId);
        fail.setStep(step);
        fail.setErrorReason(errorReason);
        fail.setTime(LocalDateTime.now());
        failDetails.add(fail);
        log.warn("[AUTO_INVOICE_FAIL][ORDER] {}", fail);
    }
    private void recordSkuFail(
            List<AutoInvoiceFailDetail> failDetails,
            Client client,
            Shop shop,
            String invoiceCode,
            String platformOrderId,
            String skuErpCode,
            AutoInvoiceFailDetail.Step step,
            String errorReason
    ) {
        AutoInvoiceFailDetail fail = new AutoInvoiceFailDetail();
        fail.setClientCode(client.getInternalCode());
        fail.setShopName(shop != null ? shop.getName() : null);
        fail.setInvoiceCode(invoiceCode);
        fail.setLevel(AutoInvoiceFailDetail.Level.SKU);
        fail.setPlatformOrderId(platformOrderId);
        fail.setSku(skuErpCode);
        fail.setStep(step);
        fail.setErrorReason(errorReason);
        fail.setTime(LocalDateTime.now());
        failDetails.add(fail);
        log.warn("[AUTO_INVOICE_FAIL][SKU] {}", fail);
    }
}