package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.SkuCriteria;
import org.jeecg.modules.business.mapper.SkuCriteriaMapper;
import org.jeecg.modules.business.service.ISkuCriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: SKU erpCode criteria
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Service
public class SkuCriteriaServiceImpl extends ServiceImpl<SkuCriteriaMapper, SkuCriteria> implements ISkuCriteriaService {
    @Autowired
    private SkuCriteriaMapper skuCriteriaMapper;

    @Override
    public List<SkuCriteria> listByCategoryCode(String categoryCode) {
        return skuCriteriaMapper.listByCategoryCode(categoryCode);
    }
}
