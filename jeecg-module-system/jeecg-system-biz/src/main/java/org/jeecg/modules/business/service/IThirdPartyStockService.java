package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.ThirdPartyStock;

/**
 * @Description: 第三方库存
 * @Author: jeecg-boot
 * @Date: 2025-07-10
 * @Version: V1.0
 */
public interface IThirdPartyStockService extends IService<ThirdPartyStock> {
    ThirdPartyStock getBySkuId(String skuId);
}
