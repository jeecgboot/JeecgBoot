package com.bomaos.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.content.entity.Carousel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 轮播图管理Mapper接口
 * Created by Panyoujie on 2021-11-10 02:54:31
 */
public interface CarouselMapper extends BaseMapper<Carousel> {

    /**
     * 分页查询
     */
    List<Carousel> listPage(@Param("page") PageParam<Carousel> page);

    /**
     * 查询全部
     */
    List<Carousel> listAll(@Param("page") Map<String, Object> page);

}
