package com.bomaos.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.content.entity.Carousel;

import java.util.List;
import java.util.Map;

/**
 * 轮播图管理服务类
 * Created by Panyoujie on 2021-11-10 02:54:31
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
