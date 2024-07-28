package com.shop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Theme;
import com.shop.mapper.ThemeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 主题配置服务实现类
 * 2021-06-28 00:36:29
 */
@Service
public class ThemeService extends ServiceImpl<ThemeMapper, Theme> {

    public PageResult<Theme> listPage(PageParam<Theme> page) {
        List<Theme> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    public List<Theme> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
