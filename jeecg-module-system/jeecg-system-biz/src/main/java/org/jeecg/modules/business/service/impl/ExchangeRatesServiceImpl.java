package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.ExchangeRates;
import org.jeecg.modules.business.mapper.ExchangeRatesMapper;
import org.jeecg.modules.business.service.IExchangeRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @Description: 汇率表
 * @Author: jeecg-boot
 * @Date: 2021-06-26
 * @Version: V1.0
 */
@Service
@Slf4j
public class ExchangeRatesServiceImpl extends ServiceImpl<ExchangeRatesMapper, ExchangeRates> implements IExchangeRatesService {
    @Autowired
    private ExchangeRatesMapper exchangeRatesMapper;

    @Override
    @Transactional
    public BigDecimal getExchangeRate(String originalCurrency, String targetCurrency) {
        return exchangeRatesMapper.getLatestExchangeRate(originalCurrency, targetCurrency);
    }

    @Override
    public BigDecimal getExchangeRateFromDate(String originalCurrency, String targetCurrency, String dateTime) {
        return exchangeRatesMapper.getExchangeRateFromDate(originalCurrency, targetCurrency, dateTime);
    }
}
