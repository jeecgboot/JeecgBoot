package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.SavRefund;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 售后退款
 * @Author: jeecg-boot
 * @Date: 2022-08-12
 * @Version: V1.0
 */
public interface ISavRefundService extends IService<SavRefund> {
    List<BigDecimal> getRefundAmount(String invoiceNumber);
}
