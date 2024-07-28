package com.shop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Website;
import com.shop.mapper.WebsiteMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 网站设置服务实现类
 * 2021-06-06 02:14:54
 */
@Service
public class WebsiteService extends ServiceImpl<WebsiteMapper, Website> {

    public PageResult<Website> listPage(PageParam<Website> page) {
        List<Website> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    public List<Website> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
