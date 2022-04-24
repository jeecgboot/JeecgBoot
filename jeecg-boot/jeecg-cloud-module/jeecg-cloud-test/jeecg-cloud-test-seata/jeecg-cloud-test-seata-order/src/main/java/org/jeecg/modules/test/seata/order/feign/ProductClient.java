package org.jeecg.modules.test.seata.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 分布式事务产品feign客户端
 * @author: zyf
 * @date: 2022/04/21
 */
@FeignClient(value ="seata-product")
public interface ProductClient {
    /**
     * 扣减库存
     *
     * @param productId
     * @param count
     * @return
     */
    @PostMapping("/test/seata/product/reduceStock")
    BigDecimal reduceStock(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}
