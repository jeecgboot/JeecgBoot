package com.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.ShopSettings;
import com.shop.mapper.ShopSettingsMapper;
import com.shop.service.ShopSettingsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商店设置服务实现类
 * 2021-07-04 03:54:31
 */
@Service
public class ShopSettingsServiceImpl extends ServiceImpl<ShopSettingsMapper, ShopSettings> implements ShopSettingsService {

    @Override
    public PageResult<ShopSettings> listPage(PageParam<ShopSettings> page) {
        List<ShopSettings> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<ShopSettings> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
