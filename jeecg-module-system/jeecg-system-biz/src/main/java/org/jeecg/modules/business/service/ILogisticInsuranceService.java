package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.LogisticInsurance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: logistic insurance
 * @Author: jeecg-boot
 * @Date:   2025-03-04
 * @Version: V1.0
 */
public interface ILogisticInsuranceService extends IService<LogisticInsurance> {

    List<LogisticInsurance> getInsuranceFeesBySkuIds(List<String> skuIds, String logisticChannelId);
}
