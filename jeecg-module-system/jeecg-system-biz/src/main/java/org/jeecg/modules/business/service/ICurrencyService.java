package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Currency;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 货币
 * @Author: jeecg-boot
 * @Date:   2023-10-17
 * @Version: V1.0
 */
public interface ICurrencyService extends IService<Currency> {

    String getCodeById(String currencyId);

    String getIdByCode(String code);
}
