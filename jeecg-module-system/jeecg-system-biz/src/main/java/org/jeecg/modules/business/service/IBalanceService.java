package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Balance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date:   2023-10-12
 * @Version: V1.0
 */
public interface IBalanceService extends IService<Balance> {

    BigDecimal getBalanceByClientIdAndCurrency(String clientId, String currency);
}
