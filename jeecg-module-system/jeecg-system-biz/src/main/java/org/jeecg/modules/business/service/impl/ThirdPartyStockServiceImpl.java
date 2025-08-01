package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.ThirdPartyStock;
import org.jeecg.modules.business.mapper.ThirdPartyStockMapper;
import org.jeecg.modules.business.service.IThirdPartyStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 第三方库存
 * @Author: jeecg-boot
 * @Date:   2025-07-10
 * @Version: V1.0
 */
@Service
public class ThirdPartyStockServiceImpl extends ServiceImpl<ThirdPartyStockMapper, ThirdPartyStock> implements IThirdPartyStockService {

    @Autowired
    private ThirdPartyStockMapper thirdPartyStockMapper;

    @Override
    public ThirdPartyStock getBySkuId(String skuId) {
        return thirdPartyStockMapper.getBySkuId(skuId);
    }
}
