package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Credit;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: credit
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
public interface ICreditService extends IService<Credit> {
    Credit getLastCredit(String currencyId);
}
