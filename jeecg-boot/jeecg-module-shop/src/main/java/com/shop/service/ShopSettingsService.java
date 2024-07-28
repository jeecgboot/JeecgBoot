package com.shop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.ShopSettings;
import com.shop.mapper.ShopSettingsMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商店设置服务实现类
 * 2021-07-04 03:54:31
 */
@Service
public class ShopSettingsService extends ServiceImpl<ShopSettingsMapper, ShopSettings> {

    public PageResult<ShopSettings> listPage(PageParam<ShopSettings> page) {
        List<ShopSettings> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    public List<ShopSettings> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
