package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.SkuDeclaredValue;
import org.jeecg.modules.business.mapper.SkuDeclaredValueMapper;
import org.jeecg.modules.business.service.ISkuDeclaredValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: SKU申报价格
 * @Author: jeecg-boot
 * @Date: 2021-06-28
 * @Version: V1.0
 */
@Service
public class SkuDeclaredValueServiceImpl extends ServiceImpl<SkuDeclaredValueMapper, SkuDeclaredValue> implements ISkuDeclaredValueService {

    @Autowired
    private SkuDeclaredValueMapper skuDeclaredValueMapper;

    @Override
    public List<SkuDeclaredValue> selectByMainId(String mainId) {
        return skuDeclaredValueMapper.selectByMainId(mainId);
    }

    @Override
    public BigDecimal getDeclaredValueForDate(String skuId, Date date) {
        return skuDeclaredValueMapper.getDeclaredValueForDate(skuId, date);
    }

    @Override
    public List<SkuDeclaredValue> getLatestDeclaredValues() {
        return skuDeclaredValueMapper.getLatestSkuDeclaredValues();
    }
}
