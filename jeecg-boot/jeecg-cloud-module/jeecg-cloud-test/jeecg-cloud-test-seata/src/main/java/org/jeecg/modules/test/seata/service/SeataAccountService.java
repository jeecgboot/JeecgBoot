package org.jeecg.modules.test.seata.service;

/**
 * @Description: 账户接口
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
public interface SeataAccountService {
    /**
     * @param userId 用户 ID
     * @param price  扣减金额
     */
    void reduceBalance(Long userId, Double price);
}
