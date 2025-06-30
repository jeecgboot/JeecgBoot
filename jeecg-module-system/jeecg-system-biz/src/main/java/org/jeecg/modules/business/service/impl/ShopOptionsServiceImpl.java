package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ShopOptions;
import org.jeecg.modules.business.mapper.ShopOptionsMapper;
import org.jeecg.modules.business.service.IShopOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 客户选项列表
 * @Author: jeecg-boot
 * @Date:   2025-06-12
 * @Version: V1.0
 */
@Service
public class ShopOptionsServiceImpl extends ServiceImpl<ShopOptionsMapper, ShopOptions> implements IShopOptionsService {

    @Autowired
    private ShopOptionsMapper shopOptionsMapper;
    @Override
    public List<ShopOptions> getByInvoiceNumber(String invoiceNumber) {
        return shopOptionsMapper.getByInvoiceNumber(invoiceNumber);
    }
}
