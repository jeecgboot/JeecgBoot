package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.*;
import org.jeecg.modules.business.mapper.BalanceMapper;
import org.jeecg.modules.business.mapper.ClientCategoryMapper;
import org.jeecg.modules.business.mapper.ClientMapper;
import org.jeecg.modules.business.service.*;
import org.jeecg.modules.business.vo.BalanceData;
import org.jeecg.modules.business.vo.InvoiceMetaData;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.jeecg.modules.business.entity.Invoice.InvoiceType.*;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date:   2023-10-12
 * @Version: V1.0
 */
@Service
@Slf4j
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements IBalanceService {
    @Autowired
    private BalanceMapper balanceMapper;
    @Autowired
    private ClientCategoryMapper clientCategoryMapper;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private ICurrencyService currencyService;
    @Autowired
    private IPurchaseOrderService purchaseOrderService;
    @Autowired
    IShippingInvoiceService shippingInvoiceService;
    @Override
    public BigDecimal getBalanceByClientIdAndCurrency(String clientId, String currency) {
        return balanceMapper.getBalanceByClientIdAndCurrency(clientId, currency);
    }
    @Override
    public void initBalance(String clientId) {
        // TODO : check if the balance already exists for this client
        Client client = clientMapper.getClientById(clientId);
        if(client == null) {
            throw new RuntimeException("Client not found for id: " + clientId);
        }
        String currency = client.getCurrency();
        if(currency == null) {
            throw new RuntimeException("Client currency is not set for client id: " + clientId);
        }
        String currencyId = currencyService.getIdByCode(currency);
        BigDecimal previousBalance = balanceMapper.getBalanceByClientIdAndCurrency(clientId, currency);
        if(previousBalance != null) {
            log.info("Balance already exists for client: {}, currency: {}, balance: {}", clientId, currency, previousBalance);
            return;
        }
        // initialize balance to zero
        SysUser sysUser = new SysUser();
        Balance balance = Balance.of(sysUser.getUsername(), clientId, currencyId, Balance.OperationType.Init.name(), null, BigDecimal.ZERO)
                        .setCreateBy(sysUser.getUsername());
        balanceMapper.insert(balance);
        log.info("Initialized balance for client: {}, currency: {}, , currencyId: {},balance: {}", clientId, currency, currencyId, BigDecimal.ZERO);
    }

    @Override
    public void updateBalance(String clientId, String invoiceCode, String invoiceType) {
        // balance update
        if(invoiceType.equals(SHIPPING.name())) {
            ShippingInvoice invoice = shippingInvoiceService.getShippingInvoice(invoiceCode);
            String currency = currencyService.getCodeById(invoice.getCurrencyId());
            BigDecimal previousBalance = getBalanceByClientIdAndCurrency(clientId, currency);
            BigDecimal currentBalance = previousBalance.subtract(invoice.getFinalAmount());
            SysUser sysUser = new SysUser();
            Balance balance = Balance.of(sysUser.getUsername(), clientId, invoice.getCurrencyId(), Balance.OperationType.Debit.name(), invoice.getId(), currentBalance);
            balanceMapper.insert(balance);
        }
        if(invoiceType.equals(COMPLETE.name())) {
            ShippingInvoice invoice = shippingInvoiceService.getShippingInvoice(invoiceCode);
            String currency = currencyService.getCodeById(invoice.getCurrencyId());
            BigDecimal previousBalance = getBalanceByClientIdAndCurrency(clientId, currency);
            BigDecimal currentBalance = previousBalance.subtract(invoice.getFinalAmount());
            BigDecimal purchaseFees = purchaseOrderService.getPurchaseFeesByInvoiceCode(invoiceCode);
            if(purchaseFees == null) {
                purchaseFees = BigDecimal.ZERO;
            }
            currentBalance = currentBalance.subtract(purchaseFees);
            SysUser sysUser = new SysUser();
            Balance balance = Balance.of(sysUser.getUsername(), clientId, invoice.getCurrencyId(), Balance.OperationType.Debit.name(), invoice.getId(), currentBalance);
            balanceMapper.insert(balance);
        }
        if(invoiceType.equals(PURCHASE.name())) {
            PurchaseOrder invoice = purchaseOrderService.getPurchaseByInvoiceNumber(invoiceCode);
            String currency = currencyService.getCodeById(invoice.getCurrencyId());
            BigDecimal previousBalance = getBalanceByClientIdAndCurrency(clientId, currency);
            BigDecimal currentBalance = previousBalance.subtract(invoice.getFinalAmount());
            SysUser sysUser = new SysUser();
            Balance balance = Balance.of(sysUser.getUsername(), clientId, invoice.getCurrencyId(), Balance.OperationType.Debit.name(), invoice.getId(), currentBalance);
            balanceMapper.insert(balance);
        }
    }

    @Override
    public void updateBalance(String clientId, String CreditId, BigDecimal amount, String currencyId) {
        String currency = currencyService.getCodeById(currencyId);
        BigDecimal previousBalance = getBalanceByClientIdAndCurrency(clientId, currency);

        if(previousBalance == null) {
            throw new RuntimeException("Please initialize balance first !");
        }
        BigDecimal currentBalance = previousBalance.add(amount);
        SysUser sysUser = new SysUser();
        Balance balance = Balance.of(sysUser.getUsername(), clientId, currencyId, Balance.OperationType.Credit.name(), CreditId, currentBalance);
        balanceMapper.insert(balance);
    }

    @Override
    public void deleteBalance(String operationId, String operationType) {
        balanceMapper.deleteBalance(operationId, operationType);
    }

    @Override
    public void deleteBatchBalance(List<String> operationIds, String operationType) {
        balanceMapper.deleteBatchBalance(operationIds, operationType);
    }
    @Override
    public void deleteBalanceByClientId(String clientId) {
        QueryWrapper<Balance> query = new QueryWrapper<>();
        query.eq("client_id", clientId);
        this.remove(query);
    }


    @Override
    public void editBalance(String operationId, String operationType, String clientId, BigDecimal amount, String currencyId) throws Exception {
        log.info("editing balance");
        SysUser sysUser = new SysUser();
        String currency = currencyService.getCodeById(currencyId);
        Balance balance = balanceMapper.getBalanceByOperation(operationId, operationType);
        if(balance == null) {
            throw new Exception("Balance not found !");
        }
        balanceMapper.deleteBalance(operationId, operationType);
        BigDecimal currentBalance = balanceMapper.getBalanceByClientIdAndCurrency(clientId, currency);
        BigDecimal finalBalance = operationType.equals(Balance.OperationType.Credit.name()) ? currentBalance.add(amount) : currentBalance.subtract(amount);
        Balance newBalance = Balance.of(sysUser.getUsername(), clientId, currencyId, operationType, operationId, finalBalance);
        balanceMapper.insert(newBalance);
    }

    @Override
    public List<BalanceData> getLowBalanceClients(List<InvoiceMetaData> metaDataList) {
        List<BalanceData> lowBalanceDataList = new ArrayList<>();
        for(InvoiceMetaData metaData : metaDataList) {
            Client client = clientMapper.getClientByCode(metaData.getInternalCode());
            Currency currency = shippingInvoiceService.getInvoiceCurrencyByCode(metaData.getInvoiceCode());
            BigDecimal balance = getBalanceByClientIdAndCurrency(client.getId(), currency.getCode());
            BigDecimal balanceThreshold = client.getBalanceThreshold() == null ?
                    clientCategoryMapper.getBalanceThresholdByCategoryId(client.getClientCategoryId()) : client.getBalanceThreshold();
            if(balance.compareTo(balanceThreshold) < 0) {
                lowBalanceDataList.add(new BalanceData(client, currency.getCode(), balance));
            }
        }
        return lowBalanceDataList;
    }

    @Override
    public void cancelBalance(String invoiceId, String originalOperationType, String operationType, BigDecimal amount, String currencyId, String clientId) {
        String currency = currencyService.getCodeById(currencyId);
        BigDecimal balanceAmount = balanceMapper.getBalanceByClientIdAndCurrency(clientId, currency);
        if(balanceAmount == null) {
            return;
        }
        Balance balanceEntryToCancel = balanceMapper.getBalanceByOperation(invoiceId, originalOperationType);
        if(balanceEntryToCancel == null) {
            return;
        }
        Balance balanceAdjustment = Balance.of("system", clientId, currencyId, operationType, invoiceId, balanceAmount.subtract(amount));
        balanceMapper.insert(balanceAdjustment);
    }
}