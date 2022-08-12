package org.jeecg.modules.test.seata.product.service;

import java.math.BigDecimal;

/**
 * @Description: 产品接口
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
public interface SeataProductService {
    /**
     * 扣减库存
     *
     * @param productId 商品 ID
     * @param count    扣减数量
     * @return 商品总价
     */
    BigDecimal reduceStock(Long productId, Integer count);
}
