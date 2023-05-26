package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.SkuLogisticChoice;
import org.jeecg.modules.business.mapper.SkuLogisticChoiceMapper;
import org.jeecg.modules.business.service.ISkuLogisticChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: SKU物流选择
 * @Author: jeecg-boot
 * @Date: 2021-10-07
 * @Version: V1.0
 */
@Service
public class SkuLogisticChoiceServiceImpl extends ServiceImpl<SkuLogisticChoiceMapper, SkuLogisticChoice> implements ISkuLogisticChoiceService {

    @Autowired
    private SkuLogisticChoiceMapper skuLogisticChoiceMapper;

    @Override

    public List<SkuLogisticChoice> findAllChoices(String skuId, String countryCode) {
        return skuLogisticChoiceMapper.findAllChoices(skuId, countryCode);
    }

    @Override
    public SkuLogisticChoice findLatestChoice(String skuId, String countryCode) {
        return skuLogisticChoiceMapper.findLatestChoice(skuId, countryCode);
    }

    @Override
    public SkuLogisticChoice findClosestChoice(String skuId, String countryCode, Date date) {
        return skuLogisticChoiceMapper.findClosestChoice(skuId, countryCode, date);
    }
}
