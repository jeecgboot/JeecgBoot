package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Balance;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.entity.ShippingInvoice;
import org.jeecg.modules.business.mapper.BalanceMapper;
import org.jeecg.modules.business.service.IBalanceService;
import org.jeecg.modules.business.service.ICurrencyService;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.service.IShippingInvoiceService;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private ICurrencyService currencyService;
    @Autowired
    private IPlatformOrderService platformOrderService;
    @Autowired
    IShippingInvoiceService iShippingInvoiceService;
    @Override
    public BigDecimal getBalanceByClientIdAndCurrency(String clientId, String currency) {
        return balanceMapper.getBalanceByClientIdAndCurrency(clientId, currency);
    }

    @Override
    public void updateBalance(String clientId, String invoiceCode, String invoiceType) {

        // balance update
        ShippingInvoice invoice = iShippingInvoiceService.getShippingInvoice(invoiceCode);
        String currency = currencyService.getCodeById(invoice.getCurrencyId());
        BigDecimal previousBalance = getBalanceByClientIdAndCurrency(clientId, currency);
        BigDecimal currentBalance = previousBalance.subtract(invoice.getFinalAmount());
        if(invoiceType.equals("complete")) {
            List<String> orderIds = iShippingInvoiceService.getPlatformOrder(invoiceCode).stream().map(PlatformOrder::getId).collect(Collectors.toList());
            Map<PlatformOrder, List<PlatformOrderContent>> orderMap = platformOrderService.fetchOrderData(orderIds);
            BigDecimal purchaseFees = BigDecimal.ZERO;
            for(Map.Entry<PlatformOrder, List<PlatformOrderContent>> entry : orderMap.entrySet()) {
                for(PlatformOrderContent content : entry.getValue()) {
                    purchaseFees = purchaseFees.add(content.getPurchaseFee());
                }
            }
            currentBalance = currentBalance.add(purchaseFees);
        }
        SysUser sysUser = new SysUser();
        Balance balance = Balance.of(sysUser.getUsername(), clientId, invoice.getCurrencyId(), Balance.OperationType.Debit.name(), invoice.getId(), currentBalance);
        balanceMapper.insert(balance);
    }

    @Override
    public void updateBalance(String clientId, String CreditId, BigDecimal amount, String currencyId) {
        String currency = currencyService.getCodeById(currencyId);
        BigDecimal previousBalance = getBalanceByClientIdAndCurrency(clientId, currency);
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
    public void editBalance(String operationId, String operationType, String clientId, BigDecimal amount, String currencyId) throws Exception {
        log.info("editing balance");
        SysUser sysUser = new SysUser();
        String currency = currencyService.getCodeById(currencyId);
        Balance balance = balanceMapper.getBalanceByOperation(operationId, operationType);
        System.out.println("balance exist ? : " + balance);
        if(balance == null) {
            throw new Exception("Balance not found !");
        }
        balanceMapper.deleteBalance(operationId, operationType);
        BigDecimal currentBalance = balanceMapper.getBalanceByClientIdAndCurrency(clientId, currency);
        System.out.println("current balance : " + currentBalance);
        BigDecimal finalBalance = operationType.equals(Balance.OperationType.Credit.name()) ? currentBalance.add(amount) : currentBalance.subtract(amount);
        System.out.println("final balance : " + finalBalance);
        Balance newBalance = Balance.of(sysUser.getUsername(), clientId, currencyId, operationType, operationId, finalBalance);
        balanceMapper.insert(newBalance);
    }

}
