package org.jeecg.modules.business.domain.job;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.controller.admin.PurchaseOrderController;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.*;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.jeecg.modules.business.entity.Invoice.InvoicingMethod.PRESHIPPING;

/**
 *Automatically generates complete invoices (purchase + shipping) for clients
 *until their balance becomes insufficient.
 *Each shop is processed in a separate transaction to ensure isolation.
 */
@Slf4j
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class DepotAutoInvoiceJob implements Job {

    @Autowired private IClientService clientService;
    @Autowired private IShopService shopService;
    @Autowired private IBalanceService balanceService;
    @Autowired private PlatformOrderShippingInvoiceService platformOrderShippingInvoiceService;
    @Autowired private EmailService emailService;
    @Autowired private ISysUserService sysUserService;
    @Autowired private IPurchaseOrderService purchaseOrderService;
    @Autowired private ApplicationContext applicationContext;
    @Autowired private InvoiceService invoiceService;

    private static final String TEMPLATE_ACCOUNTANT = "components/autoInvoiceForAccountant.ftl";
    private static final String TEMPLATE_CLIENT = "components/autoInvoiceForClient.ftl";
    private static final List<Integer> PRESHIPPING_ERP_STATUSES = Arrays.asList(1, 2);
    private static final List<String> DEFAULT_WAREHOUSES = Arrays.asList("0", "1");

    @Override
    public void execute(JobExecutionContext context) {
        // Find all clients with shops that have auto-invoice enabled
        List<Client> clients = clientService.getClientsWithAutoInvoice();
        log.info("Detected {} clients with auto-invoicing enabled.", clients.size());
        for (Client client : clients) {
            try {
                processClient(client);
            } catch (Exception e) {
                log.error("Error while processing client {}: {}", client.getInternalCode(), e.getMessage(), e);
            }
        }
    }
   // Process a single client
    public void processClient(Client client) {
        BigDecimal balance = balanceService.getBalanceByClientIdAndCurrency(client.getId(), client.getCurrency());
        if (balance == null) balance = BigDecimal.ZERO;
        //check balance
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Client {} has insufficient balance ({} {}). Skipping auto-invoice.",
                    client.getInternalCode(), balance, client.getCurrency());
            return;
        }
        List<Map<String, Object>> generatedInvoices = new ArrayList<>();
        List<Shop> shops = shopService.listByClient(client.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // Process each shop independently
        for (Shop shop : shops) {
            try {
                DepotAutoInvoiceJob self = applicationContext.getBean(DepotAutoInvoiceJob.class);
                self.processShop(client, shop, sdf, generatedInvoices);
            } catch (Exception e) {
                log.warn("Client {} Shop {} processing error: {}", client.getInternalCode(), shop.getName(), e.getMessage(), e);
            }
        }
        if (!generatedInvoices.isEmpty()) {
            balance = balanceService.getBalanceByClientIdAndCurrency(client.getId(), client.getCurrency());
            notifyAutoInvoiceGenerated(client, balance, generatedInvoices);
        }
    }
 // Process a single shop within a transaction
    @Transactional(rollbackFor = Exception.class)
    public void processShop(Client client, Shop shop, SimpleDateFormat sdf, List<Map<String, Object>> generatedInvoices) throws Exception {
        if (shop.getId() == null) return;
        Period period = platformOrderShippingInvoiceService.getAutoInvoicePeriod(
                Collections.singletonList(shop.getId()),
                PRESHIPPING_ERP_STATUSES
        );
        ShippingInvoiceParam param = new ShippingInvoiceParam(
                client.getId(),
                null,
                Collections.singletonList(shop.getId()),
                sdf.format(period.getStart()),
                sdf.format(period.getEnd()),
                PRESHIPPING_ERP_STATUSES,
                DEFAULT_WAREHOUSES
        );
        // Attempt to generate the invoice
        try {
            Response<InvoiceMetaData, List<Response<String, String>>> response =
                    platformOrderShippingInvoiceService.makeCompleteInvoicePostShipping(param, PRESHIPPING.getMethod(), "system");
            if (response == null || response.getData() == null) {
                log.info("Client {} Shop {} has no eligible orders for invoicing in the period {} to {}.",
                        client.getInternalCode(), shop.getName(),
                        sdf.format(period.getStart()), sdf.format(period.getEnd()));
                return;
            }
            InvoiceMetaData data = response.getData();
            // Update balance and attempt to approve the invoice
            balanceService.updateBalance(client.getId(), data.getInvoiceCode(), "COMPLETE");
            BigDecimal balance = balanceService.getBalanceByClientIdAndCurrency(client.getId(), client.getCurrency());
            boolean approved = approveInvoiceIfPossible(client, data.getInvoiceCode(), balance);
            InvoiceAmountDTO invoiceAmounts = invoiceService.getInvoiceAmounts(data.getInvoiceCode());
            BigDecimal totalAmount = invoiceAmounts.getPurchaseAmount().add(invoiceAmounts.getShippingAmount());
            Map<String, Object> invoiceInfo = new HashMap<>();
            invoiceInfo.put("invoiceCode", data.getInvoiceCode());
            invoiceInfo.put("shopName", shop.getName());
            invoiceInfo.put("amount", totalAmount);
            invoiceInfo.put("currency", client.getCurrency());
            invoiceInfo.put("approved", approved);
            generatedInvoices.add(invoiceInfo);
            log.info("Generated invoice {} for Client {} Shop {} purchase {} + shipping {} = total {}. New balance: {} {}. Approved: {}",
                    data.getInvoiceCode(), client.getInternalCode(), shop.getName(),
                    invoiceAmounts, invoiceAmounts.getShippingAmount(), totalAmount,
                    balance, client.getCurrency(), approved);
            if (balance.compareTo(BigDecimal.ZERO) <= 0) {
               log.info("Client {} balance depleted after invoicing. Stopping further invoicing for this client.",
                        client.getInternalCode());
            }
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("IN")) {
                log.warn("Client {} Shop {} has no eligible orders for invoicing. {}",
                        client.getInternalCode(), shop.getName(), e.getMessage());
                return;
            }
            throw e;
        }
    }
    // Attempt to approve the invoice if balance allows
    private boolean approveInvoiceIfPossible(Client client, String invoiceCode, BigDecimal balance) {
        try {
            PurchaseOrderController controller = applicationContext.getBean(PurchaseOrderController.class);
            boolean approved = balance.compareTo(BigDecimal.ZERO) >= 0;
            Map<String, Object> payload = new HashMap<>();
            payload.put("invoiceNumber", invoiceCode);
            payload.put("clientId", client.getId());
            payload.put("approved", approved);
            controller.setPaymentApproved(payload);
            if (approved) {
                purchaseOrderService.update(Wrappers.<PurchaseOrder>lambdaUpdate()
                        .set(PurchaseOrder::isOrdered, false)
                        .eq(PurchaseOrder::getInvoiceNumber, invoiceCode));
            } else {
                log.warn("Insufficient balance to approve invoice {} for client {}. Marked as pending.",
                        invoiceCode, client.getInternalCode());
            }
            return approved;

        } catch (Exception e) {
            log.error("Failed to approve invoice {} for client {}: {}",
                    invoiceCode, client.getInternalCode(), e.getMessage(), e);
            return false;
        }
    }
    // Notify accountant and client about generated invoices
    private void notifyAutoInvoiceGenerated(Client client, BigDecimal balance, List<Map<String, Object>> invoicesGenerated) {
        try {
            if (invoicesGenerated.isEmpty()) {
                log.info("Client {} has no generated invoices to notify.", client.getInternalCode());
                return;
            }
            // ===== Notify Accountants =====
            Map<String, Object> accountantModel = new HashMap<>();
            accountantModel.put("clientCode", client.getInternalCode());
            accountantModel.put("clientName", client.fullName());
            accountantModel.put("currency", client.getCurrency());
            accountantModel.put("invoices", invoicesGenerated);
            accountantModel.put("currentBalance", balance);
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
            // ====== Notify Client =====
            Map<String, Object> clientModel = new HashMap<>();
            clientModel.put("clientName", client.fullName());
            clientModel.put("currency", client.getCurrency());
            clientModel.put("invoices", invoicesGenerated);
            clientModel.put("currentBalance", balance);
            clientModel.put("hasDebt", invoicesGenerated.stream().anyMatch(i -> !(Boolean) i.get("approved")));
            clientModel.put("clientInvoiceLink", "https://app.wia-sourcing.com/business/client/overview/ExpensesOverview");
            if (StringUtils.isNotBlank(client.getEmail())) {
                 emailService.newSendSimpleMessage(
                        client.getEmail(),
                        "New Invoice Notification",
                        TEMPLATE_CLIENT,
                        clientModel
                );
            }
        } catch (Exception e) {
            log.error("Failed to send auto-invoice notification for client {}: {}",
                    client.getInternalCode(), e.getMessage(), e);
        }
    }
}