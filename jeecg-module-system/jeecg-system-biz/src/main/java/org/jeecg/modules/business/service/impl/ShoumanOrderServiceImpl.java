package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.Shouman.ShoumanOrder;
import org.jeecg.modules.business.mapper.ShoumanOrderMapper;
import org.jeecg.modules.business.service.IShoumanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 首曼订单
 * @Author: jeecg-boot
 * @Date:   2023-11-29
 * @Version: V1.0
 */
@Service
public class ShoumanOrderServiceImpl extends ServiceImpl<ShoumanOrderMapper, ShoumanOrder> implements IShoumanOrderService {

    @Autowired
    private ShoumanOrderMapper shoumanOrderMapper;

    @Override
    public List<ShoumanOrder> findShoumanOrderToSend() {
        return shoumanOrderMapper.findShoumanOrderToSend();
    }
}
