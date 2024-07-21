package com.bomaos.website.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.website.entity.Website;
import com.bomaos.website.mapper.WebsiteMapper;
import com.bomaos.website.service.WebsiteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 网站设置服务实现类
 * Created by Panyoujie on 2021-06-06 02:14:54
 */
@Service
public class WebsiteServiceImpl extends ServiceImpl<WebsiteMapper, Website> implements WebsiteService {

    @Override
    public PageResult<Website> listPage(PageParam<Website> page) {
        List<Website> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Website> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
