package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Currency;
import org.jeecg.modules.business.entity.Transaction;
import org.jeecg.modules.business.mapper.TransactionMapper;
import org.jeecg.modules.business.service.IClientService;
import org.jeecg.modules.business.service.ICurrencyService;
import org.jeecg.modules.business.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: transaction
 * @Author: jeecg-boot
 * @Date:   2023-09-08
 * @Version: V1.0
 */
@Service
@Slf4j
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements ITransactionService {

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private IClientService clientService;
    @Autowired
    private ICurrencyService currencyService;

    @Override
    public List<Transaction> list() {
        return transactionMapper.list();
    }

    @Override
    public List<Currency> getAllCurrenciesByClient(String clientId) {
        List<Currency> currencies = transactionMapper.getAllCurrenciesByClient(clientId);
        if (currencies.isEmpty()) {
            Client client = clientService.getById(clientId);
            Currency currency = currencyService.getByCode(client.getCurrency());
            currencies.add(currency);
        }
        return currencies;
    }
}
