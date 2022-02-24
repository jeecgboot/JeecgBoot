package org.jeecg.modules.test.seata.service.impl;


import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.test.seata.entity.SeataAccount;
import org.jeecg.modules.test.seata.mapper.SeataAccountMapper;
import org.jeecg.modules.test.seata.service.SeataAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @Description: TODO
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
@Slf4j
@Service
public class SeataAccountServiceImpl implements SeataAccountService {
    @Resource
    private SeataAccountMapper accountMapper;

    /**
     * 事务传播特性设置为 REQUIRES_NEW 开启新的事务
     */
    @DS("account")
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void reduceBalance(Long userId, Double price) {
        log.info("=============ACCOUNT START=================");
        SeataAccount account = accountMapper.selectById(userId);
        Assert.notNull(account, "用户不存在");
        Double balance = account.getBalance();
        log.info("下单用户{}余额为 {},商品总价为{}", userId, balance, price);

        if (balance < price) {
            log.warn("用户 {} 余额不足，当前余额:{}", userId, balance);
            throw new RuntimeException("余额不足");
        }
        log.info("开始扣减用户 {} 余额", userId);
        double currentBalance = account.getBalance() - price;
        account.setBalance(currentBalance);
        accountMapper.updateById(account);
        log.info("扣减用户 {} 余额成功,扣减后用户账户余额为{}", userId, currentBalance);
        log.info("=============ACCOUNT END=================");
    }
}
