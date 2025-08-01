package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.entity.ThirdPartyStock;
import org.springframework.stereotype.Repository;

/**
 * @Description: 第三方库存
 * @Author: jeecg-boot
 * @Date:   2025-07-10
 * @Version: V1.0
 */
@Repository
public interface ThirdPartyStockMapper extends BaseMapper<ThirdPartyStock> {

    ThirdPartyStock getBySkuId(String skuId);
}
