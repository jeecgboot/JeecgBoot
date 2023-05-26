package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.ExchangeRates;

import java.math.BigDecimal;

/**
 * @Description: 汇率表
 * @Author: jeecg-boot
 * @Date: 2021-06-26
 * @Version: V1.0
 */
public interface IExchangeRatesService extends IService<ExchangeRates> {
    BigDecimal getExchangeRate(String originalCurrency, String targetCurrency);
}
