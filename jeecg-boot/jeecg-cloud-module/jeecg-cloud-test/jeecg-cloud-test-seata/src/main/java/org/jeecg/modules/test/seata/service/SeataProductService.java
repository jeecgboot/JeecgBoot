package org.jeecg.modules.test.seata.service;

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
     * @param amount    扣减数量
     * @return 商品总价
     */
    Double reduceStock(Long productId, Integer amount);
}
