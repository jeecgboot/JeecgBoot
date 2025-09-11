package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Currency;
import org.jeecg.modules.business.entity.Transaction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: transaction
 * @Author: jeecg-boot
 * @Date:   2023-09-08
 * @Version: V1.0
 */
public interface ITransactionService extends IService<Transaction> {
    List<Transaction> list();
    List<Currency> getAllCurrenciesByClient(String clientId);
}
