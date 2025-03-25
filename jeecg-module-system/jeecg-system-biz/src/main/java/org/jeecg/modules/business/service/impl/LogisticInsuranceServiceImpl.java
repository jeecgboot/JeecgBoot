package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.LogisticInsurance;
import org.jeecg.modules.business.mapper.LogisticInsuranceMapper;
import org.jeecg.modules.business.service.ILogisticInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: logistic insurance
 * @Author: jeecg-boot
 * @Date:   2025-03-04
 * @Version: V1.0
 */
@Service
public class LogisticInsuranceServiceImpl extends ServiceImpl<LogisticInsuranceMapper, LogisticInsurance> implements ILogisticInsuranceService {
    @Autowired
    private LogisticInsuranceMapper logisticInsuranceMapper;

    @Override
    public List<LogisticInsurance> getInsuranceFeesBySkuIds(List<String> skuIds, String logisticChannelId) {
        return logisticInsuranceMapper.getInsuranceFeesBySkuIds(skuIds, logisticChannelId);
    }
}
