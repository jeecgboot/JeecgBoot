package com.shop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Carousel;
import com.shop.mapper.CarouselMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 轮播图管理服务实现类
 * 2021-11-10 02:54:31
 */
@Service
public class CarouselService extends ServiceImpl<CarouselMapper, Carousel> {

    public PageResult<Carousel> listPage(PageParam<Carousel> page) {
        List<Carousel> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    public List<Carousel> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
