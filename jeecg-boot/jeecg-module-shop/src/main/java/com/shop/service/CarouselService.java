package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Carousel;

import java.util.List;
import java.util.Map;

/**
 * 轮播图管理服务类
 * 2021-11-10 02:54:31
 */
public interface CarouselService extends IService<Carousel> {

    /**
     * 分页查询
     */
    PageResult<Carousel> listPage(PageParam<Carousel> page);

    /**
     * 查询所有
     */
    List<Carousel> listAll(Map<String, Object> page);

}
