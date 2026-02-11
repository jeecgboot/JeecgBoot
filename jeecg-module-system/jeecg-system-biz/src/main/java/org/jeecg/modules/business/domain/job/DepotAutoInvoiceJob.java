package org.jeecg.modules.business.domain.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.model.AutoInvoiceFailDetail;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.service.impl.AutoInvoiceServiceImpl;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired private IAutoInvoiceService autoInvoiceService;
    @Override
    public void execute(JobExecutionContext context) {
        List<Client> clients = clientService.getClientsWithAutoInvoice();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Client client : clients) {
            List<Map<String, Object>> generatedInvoices = new ArrayList<>();
            List<AutoInvoiceFailDetail> failDetails = new ArrayList<>();
            BigDecimal balance = balanceService.getBalanceByClientIdAndCurrency(
                    client.getId(), client.getCurrency());
            if (balance == null || balance.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("Client {} has insufficient balance, skip", client.getInternalCode());
                autoInvoiceService.recordFail(
                        failDetails,
                        client,
                        null,
                        null,
                        AutoInvoiceFailDetail.Step.PRE_CHECK,
                        AutoInvoiceServiceImpl.REASON_NO_BALANCE,
                        null
                );
                autoInvoiceService.notifyAutoInvoiceGenerated(
                        client,
                        balance,
                        generatedInvoices,
                        failDetails
                );
                continue;
            }
            List<Shop> shops = shopService.listByClient(client.getId());
            if (shops == null || shops.isEmpty()) {
                log.warn("Client {} skipped: no shops", client.getInternalCode());
                autoInvoiceService.recordFail(
                        failDetails,
                        client,
                        null,
                        null,
                        AutoInvoiceFailDetail.Step.PRE_CHECK,
                        AutoInvoiceServiceImpl.REASON_NO_SHOP,
                        null
                );
                autoInvoiceService.notifyAutoInvoiceGenerated(
                        client,
                        balance,
                        generatedInvoices,
                        failDetails
                );
                continue;
            }
            for (Shop shop : shops) {
                try {
                    autoInvoiceService.processShop(
                            client,
                            shop,
                            sdf,
                            generatedInvoices,
                            failDetails
                    );
                } catch (Exception e) {
                    log.warn("Client {} Shop {} processing error", client.getInternalCode(), shop.getName(), e);
                }
                BigDecimal latestBalance = balanceService.getBalanceByClientIdAndCurrency(client.getId(), client.getCurrency());
                if (latestBalance == null || latestBalance.compareTo(BigDecimal.ZERO) <= 0) {
                    log.info("[AUTO_INVOICE][STOP_CLIENT_BALANCE] client={}, balance={}", client.getInternalCode(), latestBalance);
                    break;
                }
            }
            if (!generatedInvoices.isEmpty()|| !failDetails.isEmpty()) {
                BigDecimal latestBalance =
                        balanceService.getBalanceByClientIdAndCurrency(client.getId(), client.getCurrency());
                autoInvoiceService.notifyAutoInvoiceGenerated(
                        client,
                        latestBalance,
                        generatedInvoices,
                        failDetails
                );
            }
        }
    }
}
