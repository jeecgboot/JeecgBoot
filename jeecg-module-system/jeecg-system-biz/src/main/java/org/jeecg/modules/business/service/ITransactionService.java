package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Transaction;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: transaction
 * @Author: jeecg-boot
 * @Date:   2023-09-08
 * @Version: V1.0
 */
@Repository
public interface ITransactionService extends IService<Transaction> {
    List<Transaction> list();
}
