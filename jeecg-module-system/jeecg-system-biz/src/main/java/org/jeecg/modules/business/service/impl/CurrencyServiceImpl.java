package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Currency;
import org.jeecg.modules.business.mapper.CurrencyMapper;
import org.jeecg.modules.business.service.ICurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 货币
 * @Author: jeecg-boot
 * @Date:   2023-10-17
 * @Version: V1.0
 */
@Slf4j
@Service
public class CurrencyServiceImpl extends ServiceImpl<CurrencyMapper, Currency> implements ICurrencyService {
    @Autowired
    private CurrencyMapper currencyMapper;
    @Override
    public String getCodeById(String currencyId) {
        return currencyMapper.getCodeById(currencyId);
    }

    @Override
    public String getIdByCode(String code) {
        return currencyMapper.getIdByCode(code);
    }
}
