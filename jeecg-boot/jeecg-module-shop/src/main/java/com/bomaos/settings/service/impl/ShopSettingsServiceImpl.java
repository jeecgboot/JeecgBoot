package com.bomaos.settings.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.settings.entity.ShopSettings;
import com.bomaos.settings.mapper.ShopSettingsMapper;
import com.bomaos.settings.service.ShopSettingsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商店设置服务实现类
 * Created by Panyoujie on 2021-07-04 03:54:31
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
