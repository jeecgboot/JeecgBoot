package org.jeecg.modules.test.seata.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author zyf
 */
@FeignClient(value ="seata-account")
public interface AccountClient {

    /**
     *  扣减余额
     * @param userId
     * @param amount
     * @return
     */
    @PostMapping("/test/seata/account/reduceBalance")
    String reduceBalance(@RequestParam("userId") Long userId, @RequestParam("amount") BigDecimal amount);
}
