package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.SavRefund;
import org.jeecg.modules.business.mapper.SavRefundMapper;
import org.jeecg.modules.business.service.ISavRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 售后退款
 * @Author: jeecg-boot
 * @Date: 2022-08-12
 * @Version: V1.0
 */
@Service
@Slf4j
public class SavRefundServiceImpl extends ServiceImpl<SavRefundMapper, SavRefund> implements ISavRefundService {
    @Autowired
    private SavRefundMapper savRefundMapper;
    @Autowired
    public SavRefundServiceImpl(SavRefundMapper savRefundMapper) {
        this.savRefundMapper = savRefundMapper;
    }
    @Override
    @Transactional
    public List<BigDecimal> getRefundAmount(String invoiceNumber) {
        return savRefundMapper.fetchRefundAmount(invoiceNumber);
    }
}
