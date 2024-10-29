package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.SkuCriteriaValue;
import org.jeecg.modules.business.mapper.SkuCriteriaValueMapper;
import org.jeecg.modules.business.service.ISkuCriteriaValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: SKU criteria value
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Service
public class SkuCriteriaValueServiceImpl extends ServiceImpl<SkuCriteriaValueMapper, SkuCriteriaValue> implements ISkuCriteriaValueService {
    @Autowired
    private SkuCriteriaValueMapper skuCriteriaValueMapper;

    @Override
    public List<SkuCriteriaValue> listByCriteria(String criteriaId) {
        return skuCriteriaValueMapper.listByCriteria(criteriaId);
    }

    @Override
    public String translateValueByCriteria(String criteria, String field, String value) {
        if(!field.equals("enName") && !field.equals("zhName")) {
            return value;
        }
        return skuCriteriaValueMapper.translateValueByCriteria(criteria, field, value);
    }
}
