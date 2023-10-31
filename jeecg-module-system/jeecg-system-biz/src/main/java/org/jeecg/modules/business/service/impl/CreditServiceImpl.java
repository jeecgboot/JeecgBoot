package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Credit;
import org.jeecg.modules.business.mapper.CreditMapper;
import org.jeecg.modules.business.service.ICreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: credit
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Service
@Slf4j
public class CreditServiceImpl extends ServiceImpl<CreditMapper, Credit> implements ICreditService {
    @Autowired private CreditMapper creditMapper;
    @Override
    public Credit getLastCredit(String currencyId) {
        return creditMapper.getLastCredit(currencyId);
    }
}
