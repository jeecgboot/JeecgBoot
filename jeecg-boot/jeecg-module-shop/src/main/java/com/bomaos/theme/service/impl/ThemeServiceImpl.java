package com.bomaos.theme.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.theme.entity.Theme;
import com.bomaos.theme.mapper.ThemeMapper;
import com.bomaos.theme.service.ThemeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 主题配置服务实现类
 * Created by Panyoujie on 2021-06-28 00:36:29
 */
@Service
public class ThemeServiceImpl extends ServiceImpl<ThemeMapper, Theme> implements ThemeService {

    @Override
    public PageResult<Theme> listPage(PageParam<Theme> page) {
        List<Theme> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Theme> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
