package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.Balance;
import org.jeecg.modules.business.mapper.BalanceMapper;
import org.jeecg.modules.business.service.IBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date:   2023-10-12
 * @Version: V1.0
 */
@Service
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements IBalanceService {
    @Autowired
    private BalanceMapper balanceMapper;
    @Override
    public BigDecimal getBalanceByClientIdAndCurrency(String clientId, String currency) {
        return balanceMapper.getBalanceByClientIdAndCurrency(clientId, currency);
    }
}
