package com.bomaos.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.content.entity.Carousel;
import com.bomaos.content.mapper.CarouselMapper;
import com.bomaos.content.service.CarouselService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 轮播图管理服务实现类
 * Created by Panyoujie on 2021-11-10 02:54:31
 */
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel> implements CarouselService {

    @Override
    public PageResult<Carousel> listPage(PageParam<Carousel> page) {
        List<Carousel> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Carousel> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

}
