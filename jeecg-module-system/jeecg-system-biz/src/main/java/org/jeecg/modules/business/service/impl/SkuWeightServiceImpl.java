package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.SkuWeight;
import org.jeecg.modules.business.mapper.SkuWeightMapper;
import org.jeecg.modules.business.service.ISkuWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: sku_weight
 * @Author: jeecg-boot
 * @Date:   2024-08-12
 * @Version: V1.0
 */
@Service
@Slf4j
public class SkuWeightServiceImpl extends ServiceImpl<SkuWeightMapper, SkuWeight> implements ISkuWeightService {
    @Autowired
    private SkuWeightMapper skuWeightMapper;

    @Override
    public SkuWeight getBySkuId(String skuId) {
        return skuWeightMapper.getBySkuId(skuId);
    }

    @Override
    public String searchFirstEmptyWeightSku(List<String> skuIds) {
        return skuWeightMapper.searchFirstEmptyWeightSku(skuIds);
    }
}
